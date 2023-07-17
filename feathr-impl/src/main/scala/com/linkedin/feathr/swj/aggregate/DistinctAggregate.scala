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
   //print("dataType" + dataType)
   println("dataType class: "+ dataType.getClass)
   //println("aggregate class: "+ aggregate.getClass)
    if (aggregate == null) {
      aggregate
    } else {
      dataType match {
        // case IntegerType => aggregate.asInstanceOf[Set[Int]]
        // case LongType => aggregate.asInstanceOf[Set[Long]]
        // case DoubleType => aggregate.asInstanceOf[Set[Double]]
        // case FloatType => aggregate.asInstanceOf[Set[Float]]
        // case StringType => aggregate.asInstanceOf[Set[String]]
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

  // record is what we get from SlidingWindowFeatureUtils. Aggregate data type is what we return here for first time
   override def agg(aggregate: Any, record: Any, dataType: DataType): Any = {
   //print("dataType" + dataType)
   if (dataType != null) {
     println("dataType class: "+ dataType.getClass)
   }
   if(aggregate != null) {
     println("aggregate class: "+ aggregate.getClass)
     aggregate match {
       case set: Set[_] =>
         set.foreach(println)
       case wrappedArray: scala.collection.mutable.WrappedArray[_] =>
         wrappedArray.foreach(println)
       case _ =>
         print(" ")
     }
   }
   if(record != null) {
     println("record class class: "+ record.getClass)
     println("record " + record)
     record match {
       case wrappedArray: scala.collection.mutable.WrappedArray[_] =>
         wrappedArray.foreach(println)
       case _ =>
         print(" ")
     }
   }
    if (aggregate == null) {
      val wrappedArray = record.asInstanceOf[mutable.WrappedArray[Int]]
      return ArrayBuffer(wrappedArray: _*)
    }
    else if (record == null) {
      aggregate
    } else {
      dataType match {
        // case IntegerType => aggregate.asInstanceOf[Set[Int]] + record.asInstanceOf[Int]
        // case LongType => aggregate.asInstanceOf[Set[Long]] + record.asInstanceOf[Long]
        // case DoubleType => aggregate.asInstanceOf[Set[Double]] + record.asInstanceOf[Double]
        // case FloatType => aggregate.asInstanceOf[Set[Float]] + record.asInstanceOf[Float]
        // case StringType=> aggregate.asInstanceOf[Set[String]] + record.asInstanceOf[String]

        case ArrayType(IntegerType, false) =>
          // val set1 = aggregate.asInstanceOf[Set[Int]]
          print("Testing: ")
          // val set1 = aggregate.asInstanceOf[mutable.WrappedArray[Int]].toSet
          // val set2 = record.asInstanceOf[mutable.WrappedArray[Int]].toSet
          // val test1 = aggregate.asInstanceOf[mutable.WrappedArray[Int]].toArray
          // val set1 = aggregate.asInstanceOf[mutable.WrappedArray[Int]].toArray.toSet
          val set1 = aggregate.asInstanceOf[mutable.ArrayBuffer[Int]].toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[Int]].toArray.toSet

          val set3 = set1.union(set2)
          // val new_aggregate = mutable.WrappedArray.make(set3.toArray)
          // val new_aggregate = set3.toArray
          val new_aggregate = ArrayBuffer(set3.toSeq: _*)
          return new_aggregate

        case ArrayType(LongType, false) =>
          val set1 = aggregate.asInstanceOf[mutable.WrappedArray[Long]]toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[Long]]toSet
          val set3 = set1.union(set2)
          val new_aggregate = mutable.WrappedArray.make(set3)
          //val new_aggregate = set3.toArray
          return new_aggregate

        case ArrayType(DoubleType, false) =>
          val set1 = aggregate.asInstanceOf[mutable.WrappedArray[Double]].toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[Double]].toSet
          val set3 = set1.union(set2)
          val new_aggregate = mutable.WrappedArray.make(set3.toArray)
          return new_aggregate

        case ArrayType(FloatType, false) =>
          val set1 = aggregate.asInstanceOf[mutable.WrappedArray[Float]].toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[Float]].toSet
          val set3 = set1.union(set2)
          val new_aggregate = mutable.WrappedArray.make(set3)
          return new_aggregate

        case ArrayType(StringType, false) =>
          val set1 = aggregate.asInstanceOf[mutable.ArrayBuffer[String]].toSet
          val set2 = record.asInstanceOf[mutable.WrappedArray[String]].toArray.toSet
          val set3 = set1.union(set2)
          //val new_aggregate = mutable.WrappedArray.make(set3)
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



















