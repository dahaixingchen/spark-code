package com.feifei.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object RDDApiPartitions {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("partition")
    val sc = new SparkContext(conf)
    sc.setLogLevel("error")

//    val data: RDD[Int] = sc.parallelize(1 to 10,50)

//    data.mapPartitionsWithIndex(
//      (pindex,piter) => {
//        //致命的 OOM风险，数据会撑爆list申请的内存空间！！！！  根据之前源码发现  spark就是一个pipeline，迭代器嵌套的模式
//        val listBuffer = new ListBuffer[String]
//        println(s"--$pindex----conn--mysql----")
//        while (piter.hasNext){
//          val value: Int = piter.next()
//          println(s"---$pindex--select $value-----")
//          listBuffer.+= (value + "selected")
//        }
//        println("-----close--mysql------")
//        listBuffer.iterator
//      }
//    ).foreach(println)

    println("*****************利用迭代器（iterator）来规避OOM的情况********************")
//    data.mapPartitionsWithIndex(
//      (pindex,piter) => {
//        new Iterator[String] {
//          override def hasNext: Boolean = if (piter.hasNext) true else {
//            println(s"---$pindex---close--mysql")
//            false
//          }
//          //每次调用next的时候，拿到的是经过数据库查询的数据
//          override def next(): String = {
//            val value: Int = piter.next()
//            println(s"---$pindex--select $value-----")
//            value + "selected"
//          }
//        }
//      }
//    ).foreach(println)

    println("-----------------------sample：数据抽样--------------------------")

    val dataRdd: RDD[Int] = sc.parallelize(1 to 10,5)
//    dataRdd.sample(false,0.5,100).foreach(println)
//    println("----------------------")
//    dataRdd.sample(true,0.5,100).foreach(println)
//    println("----------------------")
//    dataRdd.sample(false,0.5,100).foreach(println)

    println("-----------------------coalesce：修改job的分区数量--------------------------")
    //当把分区由多变小的时候，再设置coalesce的shffle参数为false ，可以不走shffle,
    //但是分区由小变多的时候，不过coalesce的shffle参数为什么，都会走shffle
    val repartitionRdd: RDD[(Int, Int)] = dataRdd.mapPartitionsWithIndex(
      (pi, pt) => {
        pt.map((pi, _))
      }
    )
    val reRdd: RDD[(Int, Int)] = repartitionRdd.coalesce(3,false)
    reRdd.foreach(println)

    val data2: RDD[(Int, (Int, Int))] = reRdd.mapPartitionsWithIndex((pi,pt)=>(pt.map((pi,_))))
    println(s"data:${reRdd.getNumPartitions}")
    data2.foreach(println)
    //    while (true){}
  }

}
