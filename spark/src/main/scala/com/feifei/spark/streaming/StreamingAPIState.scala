package com.feifei.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, StreamingContext}

object StreamingAPIState {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(new SparkConf().setAppName("api").setMaster("local[3]")
      , Duration(10000))
    ssc.sparkContext.setLogLevel("error")

    //直接接受本机端口号的数据
    val data: ReceiverInputDStream[String] = ssc.socketTextStream("localhost",8889)

    //有状态的计算，计算出累计到达的单词的数量，来一个增一个，然后输入累计的结果
    //有状态的计算必须要用checkpoint
    ssc.checkpoint("./")
    val resFM: DStream[(String, Int)] = data.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    resFM.updateStateByKey(
      (nv:Seq[Int],ov:Option[Int]) =>{
        val count: Int = nv.count(_ >0)
        val oldVal: Int = ov.getOrElse(0)
        Some(count + oldVal)
    }).print()


    ssc.start()
    ssc.awaitTermination()
  }

}
