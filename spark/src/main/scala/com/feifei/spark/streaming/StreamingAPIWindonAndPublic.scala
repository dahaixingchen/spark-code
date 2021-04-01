package com.feifei.spark.streaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object StreamingAPIWindonAndPublic {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(new SparkConf().setAppName("api").setMaster("local[3]")
      , Duration(2000))
    ssc.sparkContext.setLogLevel("error")

    //直接接受本机端口号的数据
    val data: ReceiverInputDStream[String] = ssc.socketTextStream("localhost",8889)
//    data.print()

    //需求一：单词统计，数据源（hello spark hive）
    //开的窗口是3s，所有统计的结果是(hello,3) (spark,3) (hive,3)
//    data.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

//    需求二：利用窗口机制，5s一个窗口进行输出,并统计5s内的数据量
    //1s打印一次，统计5s内的数据，这个打印频率是跟batch的时间是一直的，
    // 开头的new streamingContext的时候定义的时间，就表示一个滚动窗口的定义，如果没有添加窗口的滑动事件，默认就是滚动的时间
//    data.window(Duration(5000)).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    //    需求三：利用窗口机制，2s一个窗口进行输出，并统计5秒内的数据量
    //window后面的参数表示的是窗口的步长，
    //window后面的两个时间参数一定要是streamingContext申请的batch的倍数，如：batch是3s，那window的两个参数必须是3的倍数
    //这也是批处理倒是的，spark就是微批处理数据的
//    data.window(Duration(6000),Duration(3000)).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    //需求四：利用reduceByKeyAndWindow来实现上面的逻辑
//    data.flatMap(_.split(" ")).map((_,1)).reduceByKeyAndWindow((ov:Int,nv:Int)=>{ov+nv},Seconds(6),Seconds(3)).print()

    //需求五：利用 reduceByKeyAndWindow 来优化窗口的逻辑

    //需求五：如果一个批次阻塞了，后面的job会怎样
    // 如下，如果代码过于复杂，执行的时间太久，其他的job就会阻塞，会导致10点钟到的数据，处理的时候可能已经是11点了，这种情况
    data.window(Seconds(6),Seconds(12)).flatMap(x=>{
      Thread.sleep(1000*60*60)
      x.split("")
    }).map((_,1)).reduceByKey(_+_).print()




    ssc.start()
    ssc.awaitTermination()
  }

}
