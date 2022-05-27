package com.linkedin.feathr.offline.fds

import com.linkedin.data.template.{IntegerArray, SetMode, StringArray}
import com.linkedin.feathr.common.FeatureTypeConfig
import com.linkedin.feathr.common.tensor
import com.linkedin.feathr.common.tensor.{DimensionType, PrimitiveDimensionType, TensorCategory, TensorType}
import com.linkedin.feathr.common.types.PrimitiveType
import com.linkedin.feathr.common.util.{FDSMetadataUtils, PegasusTensorMetadataConverter}
import com.linkedin.feathr.fds.{ColumnMetadata, DimensionTypeArray, FeatureColumnLinkageMetadata, FeatureColumnMetadata, FrameFeatureMetadata, OpaqueContextualColumnMetadata}
import com.linkedin.feathr.offline.client.DataFrameColName
import com.linkedin.feathr.offline.util.FeaturizedDatasetUtils.{FDS_1D_TENSOR_DIM, FDS_1D_TENSOR_VALUE}
import com.linkedin.feathr.offline.util.{FeaturizedDatasetUtils, SparkFeaturizedDataset}
import org.apache.spark.sql.{DataFrame, Encoder}
import org.apache.spark.sql.types.{ArrayType, DataType, FloatType, Metadata, StringType, StructField, StructType}

import java.util
import scala.collection.JavaConverters._

object FDSUtils {
  type FrameFeatureData = (String, List[String])
  case class FeatureWithKeyAndColumnName(featureName: String, keys: Seq[String], columnName: String)
  /**
   * Places a FeatureColumnMetadata in a ColumnMetadata pegasus object making it easier to set the value in the union.
   */
  def createColumnMetadata(fcm: FeatureColumnMetadata): ColumnMetadata = new ColumnMetadata().setMetadata(ColumnMetadata.Metadata.create(fcm))

  /**
   * Reads and builds a {ColumnMetadata} for a Quince tensor
   *
   * @param tensorType Quince TensorType for the feature, helps retrieve information on shape,
   *                   dimension and value types
   * @param frameFeatureData Frame feature data; i.e feature ref string, list of Frame join keys
   * @return {ColumnMetadata} for the feature
   */
  def buildColumnMetadata(tensorType: tensor.TensorType, frameFeatureData: Option[FrameFeatureData] = None):
  ColumnMetadata = {
    val linkageMetadata = frameFeatureData.map {
      case (ref, keys) => new FeatureColumnLinkageMetadata().setFeatureLinkageMetadata(
        FeatureColumnLinkageMetadata.FeatureLinkageMetadata.create(new FrameFeatureMetadata()
          .setFrameFeatureName(ref)
          .setKeyColumns(new StringArray(keys.asJava))))
    }
    val columnMetadata = new FeatureColumnMetadata()
      .setTensorCategory(FDSMetadataUtils.convertCategory(tensorType.getTensorCategory))
      .setTensorShape(PegasusTensorMetadataConverter.getShape(tensorType.getShape))
      .setDimensionTypes(PegasusTensorMetadataConverter.toPegasusDimensionTypes(tensorType.getDimensionTypes))
      .setValueType(PegasusTensorMetadataConverter.toPegasusValueType(tensorType.getValueType))

    linkageMetadata foreach columnMetadata.setLinkageMetadata

    createColumnMetadata(columnMetadata)
  }


  /**
   * Builds SparkFeaturizedDataset by computing metadata for ALL columns in given DataFrame.
   * <p/>
   * Feature columns are inferred from the given feature information and annotated with
   * [[FeatureColumnMetadata]] whereas the remaining
   * columns in the input DataFrame are annotated with [[OpaqueContextualColumnMetadata]].
   *
   * @param inputDf Input DataFrame
   * @param header Header for the input DataFrame
   * @param featureDataType DataType for features
   * @return [[SparkFeaturizedDataset]] where data is input DataFrame and metadata is computed for all columns
   */
  def buildSparkFeaturizedDatasetForFCM(
    inputDf: DataFrame,
    inferredFeatureTypeMap: Map[String, FeatureTypeConfig],
    featureMetadataList: Seq[FeatureWithKeyAndColumnName],
    featureDataType: DataType = null,
    featureTypeConfigs: Map[String, FeatureTypeConfig] = Map()): SparkFeaturizedDataset = {
    val featureColumnMetadata = featureMetadataList.map {
      case feature =>
        val featureName = feature.featureName
        val keyTags = feature.keys
        val featureColName = feature.columnName
        // If there is no specified feature type config, we should use inferred ones from featureInfo
        val featureTypeConfig = featureTypeConfigs.getOrElse(featureName, inferredFeatureTypeMap(featureName))
        val metadata = buildDataFrameColumnMetadata(featureName, keyTags, featureDataType, featureTypeConfig)
        featureColName -> metadata
    }

    val opaqueColumnMetadata = {
      val allColumns = inputDf.schema.fieldNames
      val featureColumns = featureMetadataList.map(_.columnName)
      allColumns diff featureColumns map { colName =>
        colName -> new ColumnMetadata().setMetadata(
          ColumnMetadata.Metadata.create(new OpaqueContextualColumnMetadata()))
      } toMap
    }

    val metadata = new FeaturizedDatasetMetadataBuilder()
      .addAllColumnMetadata((opaqueColumnMetadata ++ featureColumnMetadata).asJava)
      .build()
    SparkFeaturizedDataset(inputDf, metadata)
  }


  /**
   * Builds DataFrame column metadata for tensor feature columns
   * See [[ColumnMetadata]] for definition of metadata that's collected.
   *
   * @param featureName FeatureRef string
   * @param keyTags Key tags associated with the feature
   * @param featureFieldDataType DataType for the feature for {TENSOR, TERM_VECTOR}
   * @return [[ColumnMetadata]] for the feature
   * TODO(PROML-13212) Replace FeatureTypes with FeatureTypeConfig.
   * At this point, featureTypeConfig is coming from config. featureType is inferred. featureTypeConfig should override
   * featureType.
   */
  private def buildDataFrameColumnMetadata(
    featureName: String,
    keyTags: Seq[String],
    featureFieldDataType: DataType = null,
    featureTypeConfig: FeatureTypeConfig = FeatureTypeConfig.UNDEFINED_TYPE_CONFIG): ColumnMetadata = {
    val TERM_VECTOR_TENSOR_TYPE =
      new TensorType(TensorCategory.SPARSE, PrimitiveType.FLOAT, List(PrimitiveDimensionType.STRING.asInstanceOf[DimensionType]).asJava)
    val TERM_VECTOR_FDS_DATA_TYPE: StructType = StructType(
      Seq(
        StructField(FDS_1D_TENSOR_DIM, ArrayType(StringType, containsNull = false), nullable = false),
        StructField(FDS_1D_TENSOR_VALUE, ArrayType(FloatType, containsNull = false), nullable = false)))
    featureFieldDataType match {
      // Commented out here to avoid importing avro schemas for quince tensors.
//      case tensorDataType if tensorDataType == TENSOR_DATA_TYPE =>
//        buildTensorMetadata(featureName, keyTags)
      case termVectorDataType if termVectorDataType == TERM_VECTOR_FDS_DATA_TYPE =>
        buildTensorMetadata(featureName, keyTags, TERM_VECTOR_TENSOR_TYPE, featureTypeConfig)
      // auto tensor
      case _ =>
        buildTensorMetadata(featureName, keyTags, null, featureTypeConfig)
    }
  }

  /**
   * Builds DataFrame column metadata for tensor feature columns
   * It will use tensorType if provided, else check FML type resolver, else use featureType and autotensorization
   *
   * @param featureName FeatureRef string
   * @param keyTags     Key tags associated with the feature
   * @param tensorType  TensorType information for the quince tensor values for this features.
   * @param featureTypeConfig For Auto-TZ'ed features, tensorType will be resolved automatically
   * @return ColumnMetadata for the feature
   * @see [[ColumnMetadata]]
   * @see [[Metadata]]
   */
  private def buildTensorMetadata(
    featureName: String,
    keyTags: Seq[String],
    tensorType: TensorType = null,
    featureTypeConfig: FeatureTypeConfig = FeatureTypeConfig.UNDEFINED_TYPE_CONFIG): ColumnMetadata = {
    val featureRef = DataFrameColName.getFeatureRefStrFromColumnName(featureName)

    val computedTensorType =
      if (tensorType != null) tensorType
      else {
        FeaturizedDatasetUtils.lookupTensorTypeForFeatureRef(featureRef, None, featureTypeConfig)
      }

    buildColumnMetadata(computedTensorType, Some((featureRef, keyTags.toList)))
  }

}
