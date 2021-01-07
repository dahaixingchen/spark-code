package com.feifei.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/*
* 面向数据集的操作种类
* 1. 带函数的非聚合操作： map,flatmap
* 2. 带函数的聚合操作：reduceByKey，combineByKey
* 3. 单元素的无函数计算：union，cartesian
* 4. 双元素（kv）无函数的计算：cogroup,join
* 5. 排序：sortBykey
* */

object RDDApi {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("api")
    val sc = new SparkContext(conf)
    sc.setLogLevel("error")

    println("------------------------1. 带函数的非聚合操作： map,flatmap---------------------------")

    val data: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5, 4, 3, 2, 1))
    val reduceRdd: RDD[(Int, Int)] = data.map((_, 1)).reduceByKey(_ + _)
//    reduceRdd.foreach(println)

//    reduceRdd.map(_._1).foreach(println)
//    data.distinct().foreach(println)

    println("----------------------------------------------------------")

    /*总结：如果两个数据集进行操作的时候不需要区分每条记录去到哪里，那直接本地IO直接拉取数据，spark内部会做优化，不走shuffle*/

    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5))
    val rdd2: RDD[Int] = sc.parallelize(List(3, 4, 5, 6, 7))

    //计算两个集合的差集，以rdd1为主，结果是1,2
//    rdd1.subtract(rdd2).foreach(println)

    //计算两个集合的交集，没有主副集合之分
//    rdd1.intersection(rdd2).foreach(println)

    //笛卡尔积操作针对全数据集的操作，所有不需要shuffle过程，直接利用全量I/O拷贝就可以了
//    rdd1.cartesian(rdd2).foreach(println)

//    println(rdd1.partitions.size)
//    println(rdd2.partitions.size)

    //union操作也针对整个数据集操作，直接把两个数据集放在一起
    val unionRDD: RDD[Int] = rdd1.union(rdd2)
//    unionRDD.foreach(println)
//    println(unionRDD.partitions.size)

    println("-----------------------------------------------------------")

    val kv1: RDD[(String, Int)] = sc.parallelize(List(
      ("zhangsan", 11),
      ("zhangsan", 12),
      ("lisi", 13),
      ("wangwu", 14)
    ))
    val kv2: RDD[(String, Int)] = sc.parallelize(List(
      ("zhangsan", 21),
      ("zhangsan", 22),
      ("lisi", 23),
      ("zhaoliu", 28)
    ))

    //相当于SQL中一些操作
    //他们底层调用的都是cogroup算子
    kv1.join(kv2).foreach(println)
    println("=====left join====")
    kv1.leftOuterJoin(kv2).foreach(println)
    println("=====right join====")
    kv1.rightOuterJoin(kv2).foreach(println)
    println("=====full join====")
    kv1.fullOuterJoin(kv2).foreach(println)

    //cogroup算子
    println("=====cogroup====")
    kv1.cogroup(kv2).foreach(println)









    while (true){}
  }

}
