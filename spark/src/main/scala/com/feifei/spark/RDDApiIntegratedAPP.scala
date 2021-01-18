package com.feifei.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.math.Ordering

/*
* 算出相同月份中气温最高的两天
* 1.同一天中要选最高的
* 2.同一个月要选最高的2个
* */
object RDDApiIntegratedAPP {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("integrated")
    val sc = new SparkContext(conf)
    sc.setLogLevel("error")

    //想要别人写的代码sorted方法的实现按照你的方式实现，可以用隐式转换的方式实现
    //    implicit val ooxdfasx = new Ordering[Int] {
    //      override def compare(x: Int, y: Int): Int = -x.compareTo(y)
    //    }


    implicit val sdfsdf = new Ordering[(Int, Int)] {
      override def compare(x: (Int, Int), y: (Int, Int)) = y._2.compareTo(x._2)
    }


    //2019-6-1	39
    val data: RDD[(Int, Int, Int, Int)] = sc.textFile("./data/tqdata", 2).map(line => {
      val str: Array[String] = line.split("\t")
      val date: Array[String] = str(0).split("-")
      (date(0).toInt, date(1).toInt, date(2).toInt, str(1).toInt)
    })
    data.mapPartitionsWithIndex(
      (pindex, piter) => {
        new Iterator[(Int, Int, Int, Int)] {
          override def hasNext: Boolean = {
            piter.hasNext
          }

          override def next(): (Int, Int, Int, Int) = {
            val vale: (Int, Int, Int, Int) = piter.next()
            println(s"--分区：$pindex 的数据是：${vale}")
            vale
          }
        }
      }).foreach((v: (Int, Int, Int, Int)) => Unit)

    //对相同年月日的取最高温
    val sameDayMaxWd: RDD[((Int, Int, Int), Int)] = data.map(e => ((e._1, e._2, e._3), e._4)).reduceByKey((v1, v2) => if (v1 < v2) v2 else v1)

    //相同的月份的所有温度
    val sameMonthGroup: RDD[((Int, Int), Iterable[(Int, Int)])] = sameDayMaxWd.map(e => ((e._1._1, e._1._2), (e._1._3, e._2))).groupByKey()
    //    sameMonthGroup.mapValues(e => e.toList.sorted.take(2)).foreach(println)

    val hashMap = new mutable.HashMap[Int, Int]()

    data.map(e => ((e._1, e._2), (e._3, e._4))).combineByKey(
      //第一条记录怎么放：
      (v1: (Int, Int)) => {
        Array(v1, (0, 0))
      },
      //第二条，以及后续的怎么放：
      (oldv: Array[(Int, Int)], newv: (Int, Int)) => {
        var flag = 0
        //先按照温度排序
        //要有原地排序，不产生新对象的排序方法，因为这个参数每条记录都会调用，会产生大量的对象，导致GC
        scala.util.Sorting.quickSort(oldv)(new Ordering[(Int, Int)] {
          override def compare(x: (Int, Int), y: (Int, Int)): Int = -x._2.compareTo(y._2)
        })
        for (i <- 0 until (oldv.length)) {
          if (oldv(i)._1 == newv._1) {
            //日期相同，温度高的留下
            if (oldv(i)._2 < newv._2) {
              oldv(i) = newv
            }
          } else {
            flag += 1
          }
        }
        //当新来的与所有的日期不同的话，只需要比较最后一个日期的气温，大的留下
        if (flag == oldv.length)
          if (oldv(oldv.length - 1)._2 < newv._2)
            oldv(oldv.length - 1) = newv
        oldv
      },
      //最后是把每个分区中的数据进行合并
      (v1: Array[(Int, Int)], v2: Array[(Int, Int)]) => {
        //这里是按照分区进行合并的，所有数据量不会很大
        //可以直接用数组的形式处理去重和排序的问题
        val unionArray: Array[(Int, Int)] = v1.union(v2)
        for (elem <- unionArray) {
          if (hashMap.contains(elem._1)) {
            if (hashMap.get(elem._1).get < elem._2) {
              hashMap.put(elem._1, elem._2)
            }
          } else
            hashMap.put(elem._1, elem._2)
        }
//        hashMap.toArray.sorted
//
//        hashMap.toArray.sortBy(-_._2)

        val array: Array[(Int, Int)] = hashMap.toArray
        //一定要清空hashMap
        hashMap.clear()
        scala.util.Sorting.quickSort(array)
        array
      }
    ).map(x => (x._1, x._2.toList)).foreach(println)


        while (true) {}
  }

}
