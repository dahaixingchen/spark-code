package com.feifei.object_class

object ConstructorTest{
  def main(args: Array[String]): Unit = {
    new ConstructorTest(44)
  }
}

class ConstructorTest(age:Int) {
  def this(){
    this(55)
    println(s"dfa+$age")
  }
}
