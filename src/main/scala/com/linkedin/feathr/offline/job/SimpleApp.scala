package com.linkedin.feathr.offline.job

import org.apache.spark.sql.DataFrame

object SimpleApp {
  var preprocessedDfMap: Map[String, DataFrame] = Map()
  var globalFuncMap: Map[String, String] = Map()

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

  def featuresToFuncMap(funcMap: Map[String, String]): Unit = {

//    val globalFuncMap = Map(
//      "f1,f2" -> "func_name1",
//      "f3,f4" -> "func_name2",
//    )
    println("Hello, funcMap")
    println("Hello, funcMap")
    println(funcMap)
    globalFuncMap = funcMap
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