package com.feifei.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.catalog.Database
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object SqlBasic {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("sparksql").setMaster("local")
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    session.sparkContext.setLogLevel("error")
    val ds: Dataset[Database] = session.catalog.listDatabases()
//    ds.show(false)
//    session.catalog.listTables().show(false)
//    session.catalog.listFunctions().show(false)
    val df: DataFrame = session.read.json("./data/json")
    df.show()
    df.printSchema()
    df.createTempView("h55")
    session.sql("select * from h55").show()
    import scala.io.StdIn._
    while (true){
      val sql: String = readLine("input your sql: ")
      session.sql(sql).show()
    }
  }

}
