package com.feifei.object_class

object Trait{
  def main(args: Array[String]): Unit = {
    println("fds")
  }
}
trait God {
  def say()={
    println("mg....say")
  }
}

trait Mg{
  def say(p:String):String
}

trait Person1{
  def say(p:String):String ={
    p
  }
}

class Person(name:String) extends God with Mg with Person1 {
  def hello(): Unit ={
    println(s"$name say hello")
  }
  override def say(): Unit = {
    println("ziji shixian ....")
  }

  override def say(p: String): String ={
    println(s"$p")
    p
  }
}

class Person3{
  def say(): Unit = {
    println("ziji shixian ....")
  }
}

//类不能多继承，只有接口（trait）可以
//class Person4(name:String) extends Person(name:String) with Person3 {
//  def say1(): Unit = {
//    println("ziji shixian ....")
//  }
//}
