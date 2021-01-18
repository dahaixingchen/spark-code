package com.feifei.spark.streaming

import org.apache.spark.streaming.dstream.SocketReceiver
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StartStreaming {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("receiver").setMaster("local[3]")
    val ssc = new StreamingContext(conf,Seconds(5))

    ssc.sparkContext.setLogLevel("error")

    val ris: ReceiverInputDStream[String] = ssc.socketTextStream("localhost",8889)

    val ds: DStream[Array[String]] = ris.map(_.split(" "))
    ds.map(data=>{
//      Thread.sleep(2000)
      (data(0),data(1))
    }).print()


    ssc.start()
    ssc.awaitTermination()

  }

}
