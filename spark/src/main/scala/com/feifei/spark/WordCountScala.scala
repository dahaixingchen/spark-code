package com.feifei.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCountScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("wordcount")
    conf.setMaster("local")
    val sc = new SparkContext(conf)

    val fileRDD: RDD[String] = sc.textFile("data/testdata.txt")
    fileRDD.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).foreach(println)
  }
}
