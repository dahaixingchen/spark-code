package com.feifei.object_class

import java.util

/*隐式转换 应用场景
*  java的list是没有foreach方法的，如果要让java的list实现 这个方法，可以用这个语法来解决
* */

object ImplicitClass {
  def main(args: Array[String]): Unit = {
    val javaArrayList = new util.ArrayList[String]()
    javaArrayList.add("zhangsan")
    javaArrayList.add("lisi")
    javaArrayList.add("wangwu")

//    javaArrayList.forEach(println)  需要java的list有foreach方法
//    val scalaList = List("zhansan",1,"lisi")
//    scalaList.foreach(println)

    println("-------------------------利用方法直接实现----------------------------------")
    //版本一，定义一个方法，实现输入java的list集合，和一个方法，然后执行这个方法
    //我们希望能list然后通过 点 出方法来
    def foreach(list: util.ArrayList[String],f:()=>Unit)={
      val value: util.Iterator[String] = list.iterator()
      while (value.hasNext) println(value.next())
    }
    foreach(javaArrayList,println)


    println("-------------------------利用类，然后点的形式实现----------------------------------")
    //版本二，通过一个类点出的形式出来，但是换了一个集合对象，
    val javalist = new RealizeClass1[String](javaArrayList)
    javalist.foreach(println)

    println("-------------------------利用Scala中的隐式转换来实现--------------------------------")
    //    list.foreach(println) //必须先承认一件事情：  list有foreach方法吗？  肯定是没有的~！ 在java里这么写肯定报错。。
    //这些代码最终交给的是scala的编译器！
    /*
    1,scala编译器发现 list.foreach(println)  有bug
    2,去寻找有没有implicit  定义的方法，且方法的参数正好是list的类型！！！
    3,编译期：完成你曾经人类：
    //    val xx = new XXX(list)
//    xx.foreach(println)
  *，编译器帮你把代码改写了。。。！
  具体方法的实现逻辑还是需要我们自己用类的方式写好的
  必须要提供一个功能完善的类，类中要有对应的要实现的方法
  然后写一个隐式转换方法，这个方法必须
    1.implicit 开头
    2.参数必须是你需要处理的数据的类型
    3.返回值一定是你之前写好的那个类
     */
    implicit def ooxx[T](list:util.ArrayList[T])={
      val iter: util.Iterator[T] = list.iterator()
      new RealizeClass[T](iter)
    }
    javaArrayList.foreach(println)


    println("-------------------------隐式转换变量--------------------------------")
    implicit val fsdasf:String = "feifei"
    implicit val fdgge:Int = 52
    def ooxsf(implicit name:String)={
      println(name + " ")
    }

    ooxsf("xuxu")
    ooxsf

  }

}


 //java中list类型的父类是iterator
class RealizeClass[T](list:util.Iterator[T]){

  def foreach(f:()=>Unit)={
    while (list.hasNext) println(list.next())
  }
}


class RealizeClass1[T](list:util.ArrayList[T]){

  def foreach(f:()=>Unit)={
    val value: util.Iterator[T] = list.iterator()
    while (value.hasNext) println(value.next())
  }
}