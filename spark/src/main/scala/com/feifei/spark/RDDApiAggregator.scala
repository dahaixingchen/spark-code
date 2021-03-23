package com.feifei.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDApiAggregator {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("aggregator")
    val sc = new SparkContext(conf)
    sc.setLogLevel("error")

    val data: RDD[(String, Int)] = sc.parallelize(List(
      ("zhangsan", 234),
      ("zhangsan", 5667),
      ("zhangsan", 343),
      ("lisi", 212),
      ("lisi", 44),
      ("lisi", 33),
      ("wangwu", 535),
      ("wangwu", 22)
    ))

    val group: RDD[(String, Iterable[Int])] = data.groupByKey()
    //    group.foreach(println)

    //行转列
    //    group.flatMap(e=>e._2.map(x=>(e._1,x))).foreach(println)

    println("----------------列转行--------------------")
    //flatMapvlaues只会得到valuse，最后输出的时候是带着你一开始的key+计算得到的value以，k、v的形式输出
    //    group.flatMapValues(e=>e).foreach(println)

    println("----------------分组求topN--以行形式展示------------------")
    //    group.mapValues(_.toList.sorted.take(2)).foreach(println)

    println("----------------分组求topN--以列形式展示------------------")
    //    group.flatMapValues(_.toList.sorted.take(2)).foreach(println)

    println("--------sum,count,min,max,avg------------")

    println("--------sum------------")
    // 按照key分组后的sum
    val sum: RDD[(String, Int)] = data.reduceByKey(_ + _)
    //    sum.foreach(println)

    //全部数据的sum
    //    val i: Int = data.map(_._2).reduce(_+_)
    //    println(i)
    //    val sumAll: (String, Int) = data.reduce( (v1,v2) => ("所有值的sum",v1._2+v2._2) )
    //    println(sumAll)

    println("--------count------------")
    //("zhangsan", 234)
    //按照key分组后的count
    //    data.map(e=>(e._1,1)).reduceByKey(_+_).foreach(println)
    //在只需要用value的时候，可以用mapVlaues
    val count: RDD[(String, Int)] = data.mapValues(_ => 1).reduceByKey(_ + _)
    //    count.foreach(println)
    //全部的的count
    //    val countAll: (String, Int) = data.mapValues(_=>1).reduce((v1,v2)=>("所有数据的count",v1._2 +v2._2))
    //    println(countAll)

    println("--------max------------")
    //    data.reduceByKey((ov,nv)=>(if (ov < nv) nv else ov)).foreach(println)

    //    val maxAll: (String, Int) = data.reduce( (ov,nv) => if(ov._2 < nv._2) ("所有数据的最大值",nv._2) else ("所有数据的最大值",ov._2))
    //    println(maxAll)

    println("--------min------------")
    //    data.reduceByKey((ov,nv)=>if (ov < nv) ov else nv).foreach(println)
    //    val mixAll: (String, Int) = data.reduce((ov,nv)=>if (ov._2 < nv._2) ("所有数据的最小值",ov._2) else ("所有数据的最小值",nv._2))
    //    println(mixAll)

    println("--------avg------------")
    //用相同的key的sum和count值相除（用join的技术让相同key的以一行的形式呈现出来）
    sum.join(count).map(e => (e._1, e._2._1 / e._2._2)).foreach(println)


    println("--------avg---combine---------")
    //利用combine一个stage拿到数据的sum和count值

    data.combineByKey(
      /*
      createCombiner: V => C,
      mergeValue: (C, V) => C,
      mergeCombiners: (C, C) => C,

        ("zhangsan", 234),
      ("zhangsan", 5667),
      ("wangwu", 22)
      */
      //createCombine，表示第一条数据，怎样放入hashmap中
      (v) => (v, 1), //(234,1)
      //mergeValue，表示的是第二条及以后的数据怎样放入到hashMap
      //lodData 表示的是第一步返回的结果数据
      (oldData: (Int, Int), newValue: Int) => (oldData._1 + newValue, oldData._2 + 1), //(234+5667,1+1)
      //mergeCombiners，表示合并一些结果到出去
      (c1: (Int, Int), c2: (Int, Int)) => (c1._1 + c2._1, c1._2 + c2._2)
    ).mapValues(e => e._1 / e._2).foreach(println)

    println("--------avg,max,min---combine---------")
    //通过combine一步把，min,max,sum,count,avg所有数据一步全部拿到
    data.combineByKey(
      (v) => (v, 1, v, v),
      (oldData: (Int, Int, Int, Int), v: Int) => (
        (oldData._1 + v, oldData._2 + 1, if (oldData._3 < v) v else oldData._3, if (oldData._4 < v) oldData._4 else v)
        ),
      (v1: (Int, Int, Int, Int), v2: (Int, Int, Int, Int)) => (v1._1 + v2._1, v1._2 + v2._2,
        if (v1._3 < v2._3) v2._3 else v1._3, if (v1._4 < v2._4) v1._4 else v2._4)
    ).foreach(println)

    while (true) {}
  }

}
