package com.feifei.spark.streaming

import java.net.Socket

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream, SocketReceiver}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Receiver {

  /**
   * @todo: 自定义receiver先把流的数据存下来，然后进行批次计算
    * @param args
   * @return void
   * @date: 2021/1/18 14:48
   */

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("receiver").setMaster("local[3]")
    val ssc = new StreamingContext(conf,Seconds(5))

    ssc.sparkContext.setLogLevel("error")

//    val server = new Socket("localhost",8889)
//    val receiver = new SocketReceiver(
//      "localhost",
//      8889,
//      server.getInputStream,
//      StorageLevel.MEMORY_AND_DISK)
    val ris: ReceiverInputDStream[String] = ssc.receiverStream(new CustormReceiver("localhost",8889))

    val ds: DStream[Array[String]] = ris.map(_.split(" "))
    ds.map(data=>{
//      Thread.sleep(2000)
      (data(0),data(1))
    }).print()


    ssc.start()
    ssc.awaitTermination()

  }


}

