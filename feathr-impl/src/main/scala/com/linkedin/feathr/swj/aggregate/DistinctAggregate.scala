package com.linkedin.feathr.swj.aggregate

import com.linkedin.feathr.swj.aggregate.AggregationType._
import org.apache.spark.sql.types._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * DISTINCT aggregation implementation.
  *
  * @param metricCol Name of the metric column or a Spark SQL column expression for derived metric
  *                  that will be aggregated using DISTINCT.
  */
class DistinctAggregate(val metricCol: String) extends AggregationSpec {
  override def aggregation: AggregationType = DISTINCT

  override def metricName = "distinct_col"

  override def isIncrementalAgg = false

  override def isCalculateAggregateNeeded: Boolean = true


  override def calculateAggregate(aggregate: Any, dataType: DataType): Any = {
    if (aggregate == null) {
      aggregate
    } else {
      dataType match {
        case ArrayType(IntegerType, false) => aggregate
        case ArrayType(LongType, false) => aggregate
        case ArrayType(DoubleType, false) => aggregate
        case ArrayType(FloatType, false) => aggregate
        case ArrayType(StringType, false) => aggregate
        case _ => throw new RuntimeException(s"Invalid data type for DISTINCT metric col $metricCol. " +
          s"Only Array[Int], Array[Long], Array[Double], Array[Float] and Array[String] are supported, but got ${dataType.typeName}")
      }
    }
  }

  /*
    Record is what we get from SlidingWindowFeatureUtils. Aggregate is what we return here for first time.
    The datatype of both should match. This is a limitation of Feathr
   */
   override def agg(aggregate: Any, record: Any, dataType: DataType): Any = {
    if (aggregate == null) {
      val wrappedArray = record.asInstanceOf[mutable.WrappedArray[Int]]
      return ArrayBuffer(wrappedArray: _*)
    } else if (record == null) {
      aggregate
    } else {
      dataType match {
        case ArrayType(IntegerType, false) =>
          val set1 = aggregate.asInstanceOf[mutable.ArrayBuffer[Int]].toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[Int]].toArray.toSet
          val set3 = set1.union(set2)
          val new_aggregate = ArrayBuffer(set3.toSeq: _*)
          return new_aggregate

        case ArrayType(LongType, false) =>
          val set1 = aggregate.asInstanceOf[mutable.WrappedArray[Long]]toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[Long]]toSet
          val set3 = set1.union(set2)
          val new_aggregate = ArrayBuffer(set3.toSeq: _*)
          return new_aggregate

        case ArrayType(DoubleType, false) =>
          val set1 = aggregate.asInstanceOf[mutable.WrappedArray[Double]].toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[Double]].toSet
          val set3 = set1.union(set2)
          val new_aggregate = ArrayBuffer(set3.toSeq: _*)
          return new_aggregate

        case ArrayType(FloatType, false) =>
          val set1 = aggregate.asInstanceOf[mutable.WrappedArray[Float]].toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[Float]].toSet
          val set3 = set1.union(set2)
          val new_aggregate = ArrayBuffer(set3.toSeq: _*)
          return new_aggregate

        case ArrayType(StringType, false) =>
          val set1 = aggregate.asInstanceOf[mutable.ArrayBuffer[String]].toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[String]].toArray.toSet
          val set3 = set1.union(set2)
          val new_aggregate = ArrayBuffer(set3.toSeq: _*)
          return new_aggregate

        case _ => throw new RuntimeException(s"Invalid data type for DISTINCT metric col $metricCol. " +
          s"Only Array[Int], Array[Long], Array[Double], Array[Float] and Array[String] are supported, but got ${dataType.typeName}")
      }
    }
  }

  override def deagg(aggregate: Any, record: Any, dataType: DataType): Any = {
    throw new RuntimeException("Method deagg for DISTINCT aggregate is not implemented because DISTINCT is " +
      "not an incremental aggregation.")
  }
}