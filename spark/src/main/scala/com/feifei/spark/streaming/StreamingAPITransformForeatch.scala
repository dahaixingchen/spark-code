package com.feifei.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, StreamingContext}

/**
 * @todo: Streaming对RDD的转换
  *       代码执行的级别，代码在哪个组件中执行问题
 * @date: 2021/4/1 10:42
 */
object StreamingAPITransformForeatch {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(new SparkConf().setAppName("api").setMaster("local[3]")
      , Duration(3000))
    ssc.sparkContext.setLogLevel("error")

    //直接接受本机端口号的数据
    val data: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 8889)

    //1、在中间状态用rdd的方式来处理流的问题
    //它的作用是数据中间处理过程中如果你想用RDD的方法来处理流数据，就可以用transform来转换
//    val resTr: DStream[(String, Int)] = data.transform((rdd: RDD[String]) => {  // 每次job都会调用一下这个函数
//      println("zhangxiaoxu") //每个job会执行一次，而且是放在drive端执行的
//
//      //job中有多少个批次，就执行多少次
//      rdd.map(x => {
//        println("chengfei") //每条数据都会执行一次
//        x
//      }).foreach(println) //没有action算子是不会执行的
//      //      println("chengfei")
//      rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
//    })
//    resTr.print()

    println("application级别的")
    var jobNum = 0

    //2、在末端用rdd的方式来处理流的问题
    data.foreachRDD((rdd) => {
      jobNum += 1
//      println(s"job级别的${jobNum}")
      rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).foreach(println)
      rdd.map(x => {
//        println("task级别的")
        (x,1)
      }).foreachPartition(println) //可以在一步把数据写入到MySQL，Redis，Hbase，Hive等数据库中
    })


    ssc.start()
    ssc.awaitTermination()
  }

}
