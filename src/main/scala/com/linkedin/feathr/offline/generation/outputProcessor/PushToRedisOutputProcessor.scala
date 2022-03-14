package com.linkedin.feathr.offline.generation.outputProcessor

import com.linkedin.feathr.common.{FeatureTypes, Header}
import com.linkedin.feathr.common.configObj.generation.OutputProcessorConfig
import com.linkedin.feathr.common.types.protobuf.FeatureValueOuterClass
import com.linkedin.feathr.offline.generation.FeatureGenUtils
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.functions.{concat_ws, expr, when}
import org.apache.spark.sql.types.{ArrayType, BooleanType, DoubleType, FloatType, IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

import java.util.Base64
import scala.collection.JavaConverters.asJavaIterableConverter
import scala.collection.mutable

/**
 * feature generation output processor used to push data to Redis store
 * @param config config object of output processor, built from the feature generation config
 */

private[offline] class PushToRedisOutputProcessor(config: OutputProcessorConfig, endTimeOpt: Option[String] = None) extends WriteToHDFSOutputProcessor(config, endTimeOpt) {

  /**
    * process single dataframe, e.g, convert feature data schema
    * @param ss spark session
    * @param df feature dataframe
    * @param header meta info of the input dataframe
    * @param parentPath path to save feature data
    * @return processed dataframe and header
    */
  override def processSingle(ss: SparkSession, df: DataFrame, header: Header, parentPath: String): (DataFrame, Header) = {
    val keyColumns = FeatureGenUtils.getKeyColumnsFromHeader(header)
    val nullElementGuardString = "_null_"
    val newColExpr = concat_ws("#", keyColumns.map(c => {
      val casted = expr(s"CAST (${c} as string)")
      // If any key in the keys is null, replace with special value and remove the row later
      when(casted.isNull, nullElementGuardString).otherwise(casted)
    }): _*)
    val allFeatureCols = header.featureInfoMap.map(x => (x._2.columnName, x._2.featureType))

    val newStructType = getRedisSparkSchema(allFeatureCols, df.schema)
    val encoder = RowEncoder(newStructType)

    val mappingFunc = getConversionFunction(df.schema, allFeatureCols)
    val encodedDf = df.map(row => {
      Row.fromSeq(df.schema.indices.map { i =>
      {
        mappingFunc(i)(row.get(i))
      }
      })
    })(encoder)

    val tableParam = "table_name"
    val tableName = config.getParams.getString(tableParam)
    val outputKeyColumnName = "feature_key"
    val decoratedDf = encodedDf.withColumn(outputKeyColumnName, newColExpr)
      .drop(keyColumns: _*)

    // set the host/post/auth/ssl configs in Redis again in the output directly
    // otherwise, in some environment (like databricks), the configs from the active spark session is not passed here.
    decoratedDf.write
      .format("org.apache.spark.sql.redis")
      .option("table", tableName)
      .option("key.column", outputKeyColumnName)
      .option("host", ss.conf.get("spark.redis.host"))
      .option("port", ss.conf.get("spark.redis.port"))
      .option("auth", ss.conf.get("spark.redis.auth"))
      .option("ssl", ss.conf.get("spark.redis.ssl"))
      .mode(SaveMode.Overwrite)
      .save()
    (df, header)
  }

  private[feathr] def encodeDataFrame(header: Header, df: DataFrame): DataFrame = {
    val allFeatureCols = header.featureInfoMap.map(x => (x._2.columnName, x._2.featureType))

    val schema = df.schema
    val newStructType = getRedisSparkSchema(allFeatureCols, schema)
    val encoder = RowEncoder(newStructType)

    val mappingFunc = getConversionFunction(schema, allFeatureCols)
    val encodedDf = df.map(row => {
      Row.fromSeq(schema.indices.map { i =>
      {
        val func = mappingFunc(i)
        val converted = func(row.get(i))
        converted
      }
      })
    })(encoder)
    encodedDf
  }

  /**
   * Gets the new dataframe schema after protobuf encoding.
   */
  private[feathr] def getRedisSparkSchema(
    allFeatureCols: Map[String, FeatureTypes] = Map(), // feature column name to feature type
    dfSchema: StructType
  ): StructType = {
    val newDfSchemaFields: Array[StructField] = dfSchema.indices.map {
      i => {
        val structField = dfSchema.fields(i)
        if (allFeatureCols.contains(structField.name)) {
          // we use protobuf byte string representation, so for feature, it's always StringType
          StructField(structField.name, StringType, structField.nullable, structField.metadata)
        } else {
          structField
        }
      }
    }.toArray
    StructType(newDfSchemaFields)
  }

  /**
   * Gets the function that converts the original data into protobuf data. The index of the schema is fixed so we
   * map each index to a fixed function.
   */
  private[feathr] def getConversionFunction(dfSchema: StructType, allFeatureCols: Map[String, FeatureTypes]): Map[Int, Any => Any] = {
    dfSchema.indices.map(index => {
      val field = dfSchema.fields(index)
      val fieldName = field.name
      val func = if (allFeatureCols.contains(fieldName)) {
        field.dataType match {
          case FloatType =>
            (rowData: Any) => {
              val stringFeature = rowData.asInstanceOf[Float]
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setFloatValue(stringFeature).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case DoubleType =>
            (rowData: Any) => {
              val stringFeature = rowData.asInstanceOf[Double]
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setDoubleValue(stringFeature).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case StringType =>
            (rowData: Any) => {
              val stringFeature = rowData.asInstanceOf[String]
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setStringValue(stringFeature).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case BooleanType =>
            (rowData: Any) => {
              val stringFeature = rowData.asInstanceOf[Boolean]
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setBooleanValue(stringFeature).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case IntegerType =>
            (rowData: Any) => {
              val stringFeature = rowData.asInstanceOf[Integer]
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setIntValue(stringFeature).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case LongType =>
            (rowData: Any) => {
              val stringFeature = rowData.asInstanceOf[Long]
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setLongValue(stringFeature).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case ArrayType(IntegerType, _) =>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[mutable.WrappedArray[java.lang.Integer]]
              val allElements = genericRow.asJava
              val protoStringArray = FeatureValueOuterClass.IntegerArray.newBuilder().addAllInts(allElements)
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setIntArray(protoStringArray).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case ArrayType(FloatType, _) =>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[mutable.WrappedArray[java.lang.Float]]
              val allElements = genericRow.asJava
              val protoStringArray = FeatureValueOuterClass.FloatArray.newBuilder().addAllFloats(allElements)
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setFloatArray(protoStringArray).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case ArrayType(DoubleType, _) =>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[mutable.WrappedArray[java.lang.Double]]
              val allElements = genericRow.asJava
              val protoStringArray = FeatureValueOuterClass.DoubleArray.newBuilder().addAllDoubles(allElements)
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setDoubleArray(protoStringArray).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case ArrayType(StringType, _) =>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[mutable.WrappedArray[java.lang.String]]
              val allElements = genericRow.asJava
              val protoStringArray = FeatureValueOuterClass.StringArray.newBuilder().addAllStrings(allElements)
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setStringArray(protoStringArray).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case ArrayType(BooleanType, _) =>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[mutable.WrappedArray[java.lang.Boolean]]
              val allElements = genericRow.asJava
              val protoStringArray = FeatureValueOuterClass.BooleanArray.newBuilder().addAllBooleans(allElements)
              val res = FeatureValueOuterClass.FeatureValue.newBuilder().setBooleanArray(protoStringArray).build()
              Base64.getEncoder.encodeToString(res.toByteArray)
            }
          case StructType(Array(StructField("indices0", ArrayType(IntegerType, _), _, _), StructField("values", ArrayType(StringType, _), _, _)))=>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[GenericRowWithSchema]
              val indexArray = genericRow(0).asInstanceOf[mutable.WrappedArray[Integer]]
              val valueArray = genericRow(1).asInstanceOf[mutable.WrappedArray[String]]
              val protoStringArray = FeatureValueOuterClass.SparseStringArray.newBuilder()
                .addAllIntegers(indexArray.asJava)
                .addAllStrings(valueArray.asJava).build()
              val proto = FeatureValueOuterClass.FeatureValue.newBuilder()
                .setSparseStringArray(protoStringArray).build()
              Base64.getEncoder.encodeToString(proto.toByteArray)
            }
          case StructType(Array(StructField("indices0", ArrayType(IntegerType, _), _, _), StructField("values", ArrayType(BooleanType, _), _, _)))=>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[GenericRowWithSchema]
              val indexArray = genericRow(0).asInstanceOf[mutable.WrappedArray[Integer]]
              val valueArray = genericRow(1).asInstanceOf[mutable.WrappedArray[java.lang.Boolean]]
              val protoBoolArray = FeatureValueOuterClass.SparseBoolArray.newBuilder()
                .addAllIntegers(indexArray.asJava)
                .addAllBooleans(valueArray.asJava).build()
              val proto = FeatureValueOuterClass.FeatureValue.newBuilder()
                .setSparseBoolArray(protoBoolArray).build()
              Base64.getEncoder.encodeToString(proto.toByteArray)
            }
          case StructType(Array(StructField("indices0", ArrayType(IntegerType, _), _, _), StructField("values", ArrayType(DoubleType, _), _, _)))=>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[GenericRowWithSchema]
              val indexArray = genericRow(0).asInstanceOf[mutable.WrappedArray[Integer]]
              val valueArray = genericRow(1).asInstanceOf[mutable.WrappedArray[java.lang.Double]]
              val protoArray = FeatureValueOuterClass.SparseDoubleArray.newBuilder()
                .addAllIntegers(indexArray.asJava)
                .addAllDoubles(valueArray.asJava).build()
              val proto = FeatureValueOuterClass.FeatureValue.newBuilder()
                .setSparseDoubleArray(protoArray).build()
              Base64.getEncoder.encodeToString(proto.toByteArray)
            }
          case StructType(Array(StructField("indices0", ArrayType(IntegerType, _), _, _), StructField("values", ArrayType(FloatType, _), _, _)))=>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[GenericRowWithSchema]
              val indexArray = genericRow(0).asInstanceOf[mutable.WrappedArray[Integer]]
              val valueArray = genericRow(1).asInstanceOf[mutable.WrappedArray[java.lang.Float]]
              val protoArray = FeatureValueOuterClass.SparseFloatArray.newBuilder()
                .addAllIntegers(indexArray.asJava)
                .addAllFloats(valueArray.asJava).build()
              val proto = FeatureValueOuterClass.FeatureValue.newBuilder()
                .setSparseFloatArray(protoArray).build()
              Base64.getEncoder.encodeToString(proto.toByteArray)
            }
          case StructType(Array(StructField("indices0", ArrayType(IntegerType, _), _, _), StructField("values", ArrayType(IntegerType, _), _, _)))=>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[GenericRowWithSchema]
              val indexArray = genericRow(0).asInstanceOf[mutable.WrappedArray[Integer]]
              val valueArray = genericRow(1).asInstanceOf[mutable.WrappedArray[java.lang.Integer]]
              val protoArray = FeatureValueOuterClass.SparseIntegerArray.newBuilder()
                .addAllIndexIntegers(indexArray.asJava)
                .addAllValueIntegers(valueArray.asJava).build()
              val proto = FeatureValueOuterClass.FeatureValue.newBuilder()
                .setSparseIntegerArray(protoArray).build()
              Base64.getEncoder.encodeToString(proto.toByteArray)
            }
          case StructType(Array(StructField("indices0", ArrayType(IntegerType, _), _, _), StructField("values", ArrayType(LongType, _), _, _)))=>
            (rowData: Any) => {
              val genericRow = rowData.asInstanceOf[GenericRowWithSchema]
              val indexArray = genericRow(0).asInstanceOf[mutable.WrappedArray[Integer]]
              val valueArray = genericRow(1).asInstanceOf[mutable.WrappedArray[java.lang.Long]]
              val protoArray = FeatureValueOuterClass.SparseLongArray.newBuilder()
                .addAllIndexIntegers(indexArray.asJava)
                .addAllLongs(valueArray.asJava).build()
              val proto = FeatureValueOuterClass.FeatureValue.newBuilder()
                .setSparseLongArray(protoArray).build()
              Base64.getEncoder.encodeToString(proto.toByteArray)
            }
          case _ =>
            throw new RuntimeException(f"The data type(${field.dataType}) is not supported in Protobuf so it can't be encoded.")
        }
      } else {
        (rowData: Any) => rowData
      }
      (index, func)
    }).toMap
  }
}
