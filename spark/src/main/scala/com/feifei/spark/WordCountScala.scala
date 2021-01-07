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
    val flatMapRDD: RDD[String] = fileRDD.flatMap(_.split(" "))
    val mapRDD: RDD[(String, Int)] = flatMapRDD.map((_,1))
    val reduceRDD: RDD[(String, Int)] = mapRDD.reduceByKey(_+_)
    reduceRDD.foreach(println)


    sc.textFile("./data/testdata.txt").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).foreach(println)

//    while (true){}
  }
}
