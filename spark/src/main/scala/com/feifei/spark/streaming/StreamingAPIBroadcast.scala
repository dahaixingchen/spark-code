package com.feifei.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, StreamingContext}

object StreamingAPIBroadcast {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(new SparkConf().setAppName("api").setMaster("local[3]")
      , Duration(3000))
    ssc.sparkContext.setLogLevel("error")

    //直接接受本机端口号的数据
    val data: ReceiverInputDStream[String] = ssc.socketTextStream("localhost",8889)

    val bc: Broadcast[List[Int]] = ssc.sparkContext.broadcast((1 to 20).toList)
    val value: DStream[Int] = data.flatMap(_.split(" ")).map(x => {
      1
    })
//    value.print()
    value.reduceByWindow(_+_,Duration(6000),Duration(3000)).filter(bc.value.contains(_)).print()



    ssc.start()
    ssc.awaitTermination()
  }

}
