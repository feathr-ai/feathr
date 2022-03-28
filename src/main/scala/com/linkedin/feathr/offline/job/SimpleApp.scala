package com.linkedin.feathr.offline.job

import org.apache.spark.sql.DataFrame

object SimpleApp {
  var preprocessedDfMap: Map[String, DataFrame] = null

  def hello(): Unit = {
    println("Hello, Wolrd")
  }

  def feathrDataframe(df: DataFrame): Unit = {
    println("Hello, Dataframe")
    df.show(10)
    df.printSchema()
    println("Hello, Dataframe")
  }

  def feathrDataframe(dfMap: Map[String, DataFrame]): Unit = {
    println("Hello, Dataframe")
    println("Hello, Dataframe")
    preprocessedDfMap = dfMap
  }

  def hello(args: Array[String]) : Unit = {
    println("Hello, args")
    println(args)
    println("Hello, args")
  }

  def mySimpleFunc(): Int = {
    10
  }

  def sumNumbers(x:Int, y:Int): Int = {
    x + y
  }

//  def registerPerson(s:String): Person = {
//    Person(s)
//  }
}