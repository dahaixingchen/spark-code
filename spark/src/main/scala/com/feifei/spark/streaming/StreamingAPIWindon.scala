package com.feifei.spark.streaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object StreamingAPIWindon {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(new SparkConf().setAppName("api").setMaster("local[3]")
      , Duration(3000))
    ssc.sparkContext.setLogLevel("error")

    //直接接受本机端口号的数据
    val data: ReceiverInputDStream[String] = ssc.socketTextStream("localhost",8889)
//    data.print()

    //需求一：单词统计，数据源（hello spark hive）
    //开的窗口是3s，所有统计的结果是(hello,3) (spark,3) (hive,3)
//    data.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

//    需求二：利用窗口机制，5s一个窗口进行输出,并统计5s内的数据量
//    data.window(Duration(5000)).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    //    需求三：利用窗口机制，2s一个窗口进行输出，并统计5秒内的数据量
    //window后面的参数表示的是窗口的步长，
    //window后面的两个时间参数一定要是streamingContext申请的batch的倍数，如：batch是3s，那window的两个参数必须是3的倍数
    //这也是批处理倒是的，spark就是微批处理数据的
//    data.window(Duration(6000),Duration(3000)).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()


    //上面的统计是统计各自的3s窗口中的一个job的数据量
    //需求：接下来需要统计历史的数据量，数据源是每S发送一条数据




    ssc.start()
    ssc.awaitTermination()
  }

}
