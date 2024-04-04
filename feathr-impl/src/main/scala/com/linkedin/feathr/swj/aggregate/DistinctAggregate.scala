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
      val result = dataType match {
        case IntegerType => aggregate.asInstanceOf[Set[Int]]
        case LongType => aggregate.asInstanceOf[Set[Long]]
        case DoubleType => aggregate.asInstanceOf[Set[Double]]
        case FloatType => aggregate.asInstanceOf[Set[Float]]
        case StringType => aggregate.asInstanceOf[Set[String]]
        case _ => throw new RuntimeException(s"Invalid data type for COUNT_DISTINCT metric col $metricCol. " +
          s"Only Int, Long, Double, Float, and String are supported, but got ${dataType.typeName}")
      }
      result.mkString(",")
    }
  }

  override def agg(aggregate: Any, record: Any, dataType: DataType): Any = {
    if (aggregate == null) {
      Set(record)
    } else if (record == null) {
      aggregate
    } else {
      dataType match {
        case IntegerType => aggregate.asInstanceOf[Set[Int]] + record.asInstanceOf[Int]
        case LongType => aggregate.asInstanceOf[Set[Long]] + record.asInstanceOf[Long]
        case DoubleType => aggregate.asInstanceOf[Set[Double]] + record.asInstanceOf[Double]
        case FloatType => aggregate.asInstanceOf[Set[Float]] + record.asInstanceOf[Float]
        case StringType=> aggregate.asInstanceOf[Set[String]] + record.asInstanceOf[String]
        case _ => throw new RuntimeException(s"Invalid data type for DISTINCT metric col $metricCol. " +
          s"Only Int, Long, Double, Float, and String are supported, but got ${dataType.typeName}")
      }
    }
  }

  override def deagg(aggregate: Any, record: Any, dataType: DataType): Any = {
    throw new RuntimeException("Method deagg for DISTINCT aggregate is not implemented because DISTINCT is " +
      "not an incremental aggregation.")
  }
}