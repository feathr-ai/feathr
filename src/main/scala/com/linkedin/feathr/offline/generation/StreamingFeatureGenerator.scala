package com.linkedin.feathr.offline.generation

import com.databricks.spark.avro.SchemaConverters
import com.google.common.collect.Lists
import com.linkedin.feathr.common.JoiningFeatureParams
import com.linkedin.feathr.offline.config.location.KafkaEndpoint
import com.linkedin.feathr.offline.generation.outputProcessor.RedisOutputUtils
import com.linkedin.feathr.offline.job.FeatureTransformation.getFeatureJoinKey
import com.linkedin.feathr.offline.job.{FeatureGenSpec, FeatureTransformation}
import com.linkedin.feathr.offline.logical.FeatureGroups
import com.linkedin.feathr.offline.transformation.{AnchorToDataSourceMapper, DataFrameBasedSqlEvaluator}
import com.linkedin.feathr.sparkcommon.SimpleAnchorExtractorSpark
import org.apache.avro.Schema
import org.apache.avro.Schema.Type
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.io.DecoderFactory
import org.apache.spark.customized.CustomGenericRowWithSchema
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

import java.nio.ByteBuffer
import scala.collection.JavaConverters._
import scala.collection.convert.wrapAll._

class StreamingFeatureGenerator {
  @transient val anchorToDataFrameMapper = new AnchorToDataSourceMapper()

  def generateFeatures(ss: SparkSession, featureGenSpec: FeatureGenSpec, featureGroups: FeatureGroups,
                       keyTaggedFeatures: Seq[JoiningFeatureParams]) = {
    val anchors = keyTaggedFeatures.map(streamingFeature => {
      featureGroups.allAnchoredFeatures.get(streamingFeature.featureName).get
    })

    assert(featureGenSpec.getOutputProcessorConfigs.size == 1, "Only one output sink is supported in streaming mode")
    val outputConfig = featureGenSpec.getOutputProcessorConfigs.head
    val timeoutMs = if (!outputConfig.getParams.hasPath("timeoutMs")) {
      Long.MaxValue
    } else {
      outputConfig.getParams.getNumber("timeoutMs").longValue()
    }
    // Load the raw streaming source data
    val anchorDFRDDMap = anchorToDataFrameMapper.getAnchorDFMapForGen(ss, anchors, None, false, true)
    anchorDFRDDMap.par.map { case (anchor, accessor) => {
      val schemaStr = anchor.source.location.asInstanceOf[KafkaEndpoint].schema.avroJson
      val schemaStruct = SchemaConverters.toSqlType(Schema.parse(schemaStr)).dataType.asInstanceOf[StructType]
      val rowForRecord = (input: Any) => {
        val values = Lists.newArrayList[Any]
        val decoder = DecoderFactory.get().binaryDecoder(input.asInstanceOf[Array[Byte]], null)

        val avroSchema = Schema.parse(schemaStr)
        val reader = new GenericDatumReader[GenericRecord](avroSchema)
        val record = reader.read(null, decoder)
        for (field <- record.getSchema.getFields) {
          var value = record.get(field.name)
          var fieldType = field.schema.getType
          if (fieldType.equals(Type.UNION)) fieldType = field.schema.getTypes.get(1).getType
          // Avro returns Utf8s for strings, which Spark SQL doesn't know how to use
          if (fieldType.equals(Type.STRING) && value != null) value = value.toString
          // Avro returns binary as a ByteBuffer, but Spark SQL wants a byte[]
          if (fieldType.equals(Type.BYTES) && value != null) value = value.asInstanceOf[ByteBuffer].array
          values.add(value)
        }

        new CustomGenericRowWithSchema(
          values.asScala.toArray, schemaStruct
        )
      }
      val convertUDF = udf(rowForRecord)

      // Streaming processing each source
      accessor.get().writeStream
        .outputMode(OutputMode.Update)
        .foreachBatch{ (batchDF: DataFrame, batchId: Long) =>
          // Convert each batch dataframe from the kafka built-in schema(which always has 'value' field) to user provided schema
          val convertedDF = batchDF.select(convertUDF(col("value")))
          val encoder = RowEncoder.apply(schemaStruct)
          // Use encoder to add user schema into each row
          val rowDF = convertedDF.map(row=>
            // Has to create a GenericRowWithSchema since encodeRow in Spark-redis expects the schema within each Row
            new GenericRowWithSchema(row.toSeq.flatMap(value=> value.asInstanceOf[GenericRowWithSchema].toSeq).toArray, row.schema)
              // Has to cast of Row to make compile happy due to the encoder
              .asInstanceOf[Row]
          )(encoder)

          val featureNamePrefixPairs = anchor.selectedFeatures.map(f => (f, "_streaming_"))
          val keyExtractor = anchor.featureAnchor.sourceKeyExtractor
          val withKeyColumnDF = keyExtractor.appendKeyColumns(rowDF)
          // Apply feature transformation
          val transformedResult = DataFrameBasedSqlEvaluator.transform(anchor.featureAnchor.extractor.asInstanceOf[SimpleAnchorExtractorSpark],
            withKeyColumnDF, featureNamePrefixPairs, anchor)
          val outputJoinKeyColumnNames = getFeatureJoinKey(keyExtractor, withKeyColumnDF)
          val selectedColumns = outputJoinKeyColumnNames ++ anchor.selectedFeatures.filter(keyTaggedFeatures.map(_.featureName).contains(_))
          val cleanedDF = transformedResult.df.select(selectedColumns.head, selectedColumns.tail:_*)
          val keyColumnNames = FeatureTransformation.getStandardizedKeyNames(outputJoinKeyColumnNames.size)
          val resultFDS: DataFrame = PostGenPruner().standardizeColumns(outputJoinKeyColumnNames, keyColumnNames, cleanedDF)
          val tableParam = "table_name"
          val tableName = outputConfig.getParams.getString(tableParam)
          val allFeatureCols = resultFDS.columns.diff(keyColumnNames).toSet
          RedisOutputUtils.writeToRedis(ss, resultFDS, tableName, keyColumnNames, allFeatureCols, SaveMode.Append)
        }
        .start()
        .awaitTermination(timeoutMs)
      }
    }
  }
}
