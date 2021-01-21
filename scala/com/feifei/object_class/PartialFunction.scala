package com.feifei.object_class

/*偏函数*/
object PartialFunction {
  def main(args: Array[String]): Unit = {

    def asd(): Unit = {
      //方法里直接写case会报错
      //      case "hello" => "val is hello"
      //      case x:Int => s"$x....is int"
      //      case _ => "none"
      println("haha")
    }

    println(asd)

    def asd1(): Unit = {
      "hello" match {
        case "hello" => println("val is hello")
        //模式匹配的时候不能自定义数据
        //        case x: Int => s"$x....is int"
        case _ => "none"
      }
    }
    println(asd1())

    println("----------------------------偏函数----------------------------------")
    //偏函数，定义的时候一定要先定义函数的类型其中第一个泛型表示的是输入参数，第二个泛型表示的是出参
    //注意：偏函数的定义是没有()的
    def asd2: PartialFunction[Any, String] = {

        case "hello" => "val is hello"
        case x: Int => s"$x....is int"
        case _ => "none"
    }

    println(asd2(55))

  }
}
