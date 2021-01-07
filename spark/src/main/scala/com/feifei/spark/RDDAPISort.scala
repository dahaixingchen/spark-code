package com.feifei.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDAPISort {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("sort")
    val sc = new SparkContext(conf)
    sc.setLogLevel("error")
    val data: RDD[String] = sc.textFile("./data/pvuvdata",5)


    println("----------PV:统计的是网站被访问的次数，不对用户去重-----------")
    val urlRDD: RDD[(String, Int)] = data.map(e=>(e.split("\t")(5),1))
    val reduceRDD: RDD[(String, Int)] = urlRDD.reduceByKey(_+_)

//    reduceRDD.map(e=>(e._2,e._1)).sortByKey(false).map(e=>(e._2,e._1)).take(3).foreach(println)

//    reduceRDD.map(_.swap).sortByKey(false).map(_.swap).take(3).foreach(println)

    println("---------------直接用sort来自定义实现排序--------------------")

//    reduceRDD.sortBy(_._2,false).take(5).foreach(println)

    println("----------UV:统计网站被不同用户访问的次数,同一个客户看了这个网站10次就算一次-----------")

    val repeKv: RDD[((String, String), Int)] = data.map(e => {
      val str: Array[String] = e.split("\t")
      ((str(0), str(5)), 1)
    })
    repeKv.distinct().map(e=>((e._1._2,e._2))).reduceByKey(_+_).foreach(println)
//      .reduceByKey(_+_).foreach(println)
//    val tuple: ((String, String), Int) = repeKv.distinct().reduce((v1,v2)=> (v1._1,(v1._2+v2._2)))
//    println(tuple)

    data.map(e=>{
      val strs: Array[String] = e.split("\t")
      (strs(5),strs(0))
    }).distinct().map(e=>(e._1,1)).reduceByKey(_+_).foreach(println)





















    while (true){}
  }

}
