package com.feifei.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, State, StateSpec, StreamingContext}
/**
 * @todo: 状态相关的计算
 */

object StreamingAPIState {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(new SparkConf().setAppName("api").setMaster("local[3]")
      , Duration(2000))
    ssc.sparkContext.setLogLevel("error")

    val data: ReceiverInputDStream[String] = ssc.socketTextStream("localhost",8889)

    //有状态的计算，计算出累计到达的单词的数量，来一个增一个，然后输入累计的结果
    //有状态的计算必须要用checkpoint
    ssc.checkpoint("./receivedBlockMetadata")
    val reM: DStream[(String, Int)] = data.flatMap(_.split(" ")).map((_, 1))
    val value: DStream[(String, Int)] = reM.reduceByKey(_+_)

//    reM.checkpoint(Duration(1000*2)) //需要加上checkPoint的时间
//    resFM.print()

    //这个方法有个弊端，它是把一个批次的数据作为新值，然后和状态中的老值发生计算
    //some是option的子类
//    reM.updateStateByKey(
//      (nv: Seq[Int], ov: Option[Int]) => {
//        val count: Int = nv.count(_ > 0)
//        val oldVal: Int = ov.getOrElse(0)
//        Some(count + oldVal)
//      }).print()


      //它是一条一条的数据作为新值，然后和状态中的老值发生计算
//    reM.mapWithState(StateSpec.function(
//      (k:String ,nv:Option[Int] ,ov:State[Int]) =>{
//        val count: Int = nv.count(_ >0)
////        k.map((_,1))
//        println(s"*************k:$k  nv:${nv.getOrElse(0)}   ov ${ov.getOption().getOrElse(0)} ")
//        (k, nv.getOrElse(0))
//      }
//    )).print()

    //算窗口中的汇总--》每次都需要计算整个窗口的数据，这个并不需要有状态的计算
//    data.flatMap(_.split(" ")).map((_,1)).reduceByKeyAndWindow(_+_,Duration(2000)).print()

    //算窗口中的汇总-->优化，利用一个窗口的滑动，只计算窗口中新增的数据
    data.flatMap(_.split(" ")).map((_,1)).reduceByKeyAndWindow(
      //相当于reduce的需要的那个函数,同一个key需要两个以条以上的数据的时候才会触发这个函数
      (ov,nv)=>{
        ov + nv
      }
      //数据被挤出去的函数
      ,(ov,oov)=>{
        ov -oov
      }
      ,Duration(6000)
      ,Duration(2000)).print()

    ssc.start()
    ssc.awaitTermination()
  }

}
