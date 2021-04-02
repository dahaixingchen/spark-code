package com.feifei.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Duration, StreamingContext}

object StreamingManualOffset {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[3]").setAppName("manualOffset")
    val ssc = new StreamingContext(conf,Duration(1000))
    ssc.sparkContext.setLogLevel("error")



    ssc.start()
    ssc.awaitTermination()
  }

}
