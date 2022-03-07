package com.linkedin.feathr.offline.transformation

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.types.StructField

private[offline] object DataFrameExt {
  implicit class DataFrameMethods(df: DataFrame) {

    /**
     * Extend the input dataframe with column provided in the schema, if it do not exist in the input dataframe
     * @param inputDF extend the input Dataframe with all fields in the input schema if it does not exist in input DF
     * @param schema schema with fields to be extended
     * @return extended input dataframe
     */
    private def extendDf(inputDF: DataFrame, schema: Seq[StructField]): DataFrame = {
      // Find fields from the schema which do not exist in the input dataframe
      val extendFieldNames = schema.map(_.name).distinct.filter(f => !inputDF.schema.map(_.name).contains(f))
      val schemaMap = schema.map(field => field.name -> field).toMap
      // Extend the fields by filling null values
      extendFieldNames.foldLeft(inputDF)((curDf, field) => {
        curDf.withColumn(field, lit(null) cast schemaMap(field).dataType)
      })
    }

    /**
     * The difference between this function and [[union]] is that this function
     * resolves columns by name (not by position).
     * The set of column names in this and other Dataset can differ.
     * missing columns will be filled with null.
     *
     * @param other other dataframe for the union
     * @return unioned dataframe
     */
    def fuzzyUnion(other: DataFrame): DataFrame = {
      val schema = df.schema.union(other.schema)
      val extendedDf: DataFrame = extendDf(df, schema)
      val extendedOtherDf = extendDf(other, schema)
      extendedDf.unionByName(extendedOtherDf)
    }
  }
}
