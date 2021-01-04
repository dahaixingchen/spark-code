package com.feifei.object_class

/**
 * @todo: 类的构造函数中只可以有一个文件只可以一个类写构造函数
  * @param null
 * @return
 * @date: 2020/12/31 17:00
 */
object Constructor{
  def main(args: Array[String]): Unit = {
    new Constructor("sdf").f1()
    new Constructor1().f1()
    new Constructor2().f1()
    new Constructor3()
  }
}
/**
 * @todo: 带参数的构造
  * @param null
 * @return
 * @date: 2020/12/31 17:14
 */
class Constructor(sex:String) {
  println("-----------------带默认参数的构造-------------------------")
  var age:Int = 100;

  def f1(): Unit ={
    println(sex)
    println(age)
  }
}



/**
 * @todo:
  * @param null
 * @return
 * @date: 2020/12/31 16:57
 */
class Constructor1(address:String) {

  var age: Int = 200
  //个性化的构造方法是一定要先对默认的构造方法进行初始化，才行
  def this(){
    this("dsf")
    this.age = 1024
  }
  def f1(): Unit = {
    println(address)
    println(age)
  }
}


class Constructor2(name:String,a:Int){

  var age: Int = 200
  //个性化的构造方法是一定要先对默认的构造方法进行初始化，才行
  def this(){
    this("zhangsan",1025)
    this.age = 1024 * 1024
  }
  def f1(): Unit = {
    println(age)
  }
}


class Constructor3{
  var age: String = "我们的哎"

  def f1(): Unit = {
    println(age)
  }
}