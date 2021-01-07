package com.feifei.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object RDDApiPartitions {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("partition")
    val sc = new SparkContext(conf)
    sc.setLogLevel("error")

    val data: RDD[Int] = sc.parallelize(1 to 10,50)

    data.mapPartitionsWithIndex(
      (pindex,piter) => {
        //致命的 OOM风险，数据会撑爆list申请的内存空间！！！！  根据之前源码发现  spark就是一个pipeline，迭代器嵌套的模式
        val listBuffer = new ListBuffer[String]
        println(s"--$pindex----conn--mysql----")
        while (piter.hasNext){
          val value: Int = piter.next()
          println(s"---$pindex--select $value-----")
          listBuffer.+= (value + "selected")
        }
        println("-----close--mysql------")
        listBuffer.iterator
      }
    ).foreach(println)

    println("*****************利用迭代器（iterator）来规避OOM的情况********************")
    data.mapPartitionsWithIndex(
      (pindex,piter) => {
        new Iterator[String] {
          override def hasNext: Boolean = if (piter.hasNext) true else {
            println(s"---$pindex---close--mysql")
            false
          }
          //每次调用next的时候，拿到的是经过数据库查询的数据
          override def next(): String = {
            val value: Int = piter.next()
            println(s"---$pindex--select $value-----")
            value + "selected"
          }
        }
      }
    ).foreach(println)


    while (true){}
  }

}
