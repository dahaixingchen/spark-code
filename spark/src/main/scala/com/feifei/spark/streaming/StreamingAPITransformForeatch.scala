package com.feifei.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, StreamingContext}

object StreamingAPITransformForeatch {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(new SparkConf().setAppName("api").setMaster("local[3]")
      , Duration(3000))
    ssc.sparkContext.setLogLevel("error")

    //直接接受本机端口号的数据
    val data: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 8889)

    //在中间状态用rdd的方式来处理流的问题
    val resTr: DStream[(String, Int)] = data.transform((rdd: RDD[String]) => {
      println("张晓旭")
      rdd.map(x => {
        println("何婷")
        x
      })
      //      println("chengfei")
      rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    })

    println("application级别的")
    var jobNum = 0
    //在末端用rdd的方式来处理流的问题
    data.foreachRDD((rdd) => {
      jobNum += 1
      println(s"job级别的${jobNum}")
      rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).foreach(println)
      rdd.map(x => {
        println("task级别的")
      }).foreach(println)
    })


    ssc.start()
    ssc.awaitTermination()
  }

}
