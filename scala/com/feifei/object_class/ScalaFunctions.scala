package com.feifei.object_class

import java.util
import java.util.Date

import com.feifei.Constructor

object ScalaFunctions {


  def ssxx()={
    println("hello object")
  }


  def main(args: Array[String]): Unit = {

    println("-------1.普通函数----------")

    def fun(): Unit = {
      val constructor = new com.feifei.Constructor("女")
      println(constructor.sex)
      new util.ArrayList[Int]()
    }
    fun()

    //如果给出了return，就一定要手写返回值
    def fun1(): util.LinkedList[String] = {
      return new util.LinkedList[String]()
    }

    def fun11() = {
      new util.LinkedList[String]()
    }

    println("-------2.递归函数----------")
    //递归函数一定要有个返回值
    def fun2(num:Int):Int={
      if(num == 1) num
      else num * fun2(num - 1)
    }
    val i = fun2(3)
    println(i)


    println("-------3.默认值函数----------")
    def fun3(a:Int = 250,b:String = "张晓旭")={
      println(s"$a * $b = ")
    }
    fun3()
    fun3(b = "旭旭")

    println("-------4.匿名函数----------")
    val fun4: (Double, Float) => Double = (a: Double,b :Float) => {
        a + b
    }
    println(fun4(2.55,6))
    val fun41 = fun4(_:Double,52)
    println(fun41(56))

    //  匿名函数一般都需要定义一个变量去接下，不然往后的代码都不会运行，而且编译器的提示功能也会有点问题
//    (a: Int,b :Int) =>{
//      a+b
//      println(a + b)
//    }
    println("-------5.嵌套函数----------")

    def fun5()={
      val a :Int = 5
      def fun51(a:Int)={
        println(a)
      }
      fun51(a)
    }

    fun5
    println("-------6.偏应用函数----------")
    def fun6(date:Date,tp:String,msg:String)={
      println(s"$date\t$tp\t$msg")
    }
    fun6(new Date(),"info","ok")

    //可以用 _ 号加上原先函数的参数数据类型，可以组成另外一个新的定制函数
    val info = fun6(_:Date,"info",_:String)
    val error = fun6(_:Date,"error",_:String)
    info(new Date(),"ok")
    error(new Date(),"error...")

    println("-------7.可变参函数----------")
    def fun7(a:Int *)={
      for (e <- a){
        print(e+ "\t")
      }
    }
    fun7(1,5,6,5,4,2,4)

    //跟java一样，可变参只可以放在最后
//    def fun71(a:Int *,b:String*)={
//
//    }
    println("-------8.高阶函数----------")

    def computer(a:Int,b:Int,f:(Int,Int) => String)={
      val res = f(a,b)
      println(res)
    }
    def computer8(a:Int,b:Int,f:(Int,Int)=> Int)={
      val value = f(a,b)
      println(value)
    }

    //函数作为参数
    computer(3,8,(x:Int,y:Int) =>{
      String.valueOf(x*y)
    })
    //如果有固定多少个参数，可以用 _ 来替代这些参数
    computer(3,8,_.toString + _.toString)
    computer(3,8,(x:Int,y:Int) =>{
      String.valueOf(x*x*y*y)
    })

    //这里的函数当做参数，函数具体的实现方式交给调用它的匿名方法去实现，
    //在定义的函数里，我们只需要关注这个 作为参数的函数的参数，返回值，
    //也就是说我们利用这个函数中的数据，调用参数函数，然后利用参数函数的返回值进行一些计算就可以了
    def computer1(a:Int,b:Int,c:String,f:(Int,Int,String) => String)={
      println(s"$a+$b c")
      val aa:Int = 110
      val bb = 120
      val cc = "我是函数体中的一个变量"
      val str = f(aa,bb,cc)
      println(str)
    }
    computer1(3,8,"这个是乘法",(a:Int,b:Int,c:String)=>{
      s"$a + $b $c"
    })

    //函数作为返回值
    def factory(i:String):(Int,Int)=> Int ={
      def plus(x:Int,y:Int) ={
        x+y
      }
      if (i.equals("+")){
        plus
      }else{
        (x:Int,y:Int)=>{
          x * y
        }
      }
    }

    computer8(3,8,factory("-"))

    println("-------9.柯里化---------")
    def fun9(a:Int*)(b:String *)={
      println(s"$a\t$b")
      a.foreach(print)
      b.foreach(print)
    }
    fun9(4,8,5,6)("sdfa","我们")

    println("--------*.方法---------")

    //方法不想执行，赋值给一个变量， 方法名+空格+下划线
    val u = fun3()

    val un = fun3 _
    un(52,"sdfa")

    info(new Date(),"ok")

    //方法不想执行，赋值给一个变量， 方法名+空格+下划线
    val fun101 = fun6 _
    fun101(new Date(),"debug","fjsladkfjkl")

    //方法不想执行，赋值给一个变量， 不能给赋值后的函数变量
    //    val fun10 = info _

  }


}
