package com.feifei.object_class

object Aa{
  def main(args: Array[String]): Unit = {
    val control = new Control()
//    control.f1(10)

    val inclusive = 1 to 10
    inclusive.foreach(println)

    val range = 1 until 10
    for (elem <- range if (elem %2 == 0)) {
      println(elem)
    }
    println("---------乘法口诀-------------")
    for (i <- 1 to 9){
      for (j <- 1 to 9){
        if(j <= i) print(s"$j * $i = ${i*j} \t")
        if(j == i) println()
      }
    }
    println("---------乘法口诀升级版-------------")
    for(i <- 1 to 9 ;j <- 1 to 9 if(j <= i)){
      //这里当j> i的时候依然依然还会执行，所以要用一个守护方法if(j <= i)
      if(j <= i)print(s"$j * $i = ${i*j}\t")
      if(i == j)println()
    }

    println("---------yield-------------")
    //for是无返回值的，想要得到for处理过的数据集得用yield
    val unit = for (i <-1 to 10){
      var x = 8
      i + x
    }

    val ints = for (i <- 1 to 10) yield{
      var x = 8
      i + x
    }
    ints.foreach(println)




  }
}

// 控制类
class Control {
  private val array = new Array[Int](9)

  def f1(a:Int): Unit ={
    for (elem <- array) {
      println(elem)
    }
  }

  1 to 10
}

