package com.feifei.object_class

object Match {
  def main(args: Array[String]): Unit = {
    val tuple4: (Double, Int, String, Boolean, Int) = (1.0,88,"abc",false,44)
    val proIter: Iterator[Any] = tuple4.productIterator
    val res: Iterator[String] = proIter.map((x) => {
      x match {
        case 2 => println(s"$x...is 1");String.valueOf(x)
        case 88 => println(s"$x ...is 88"); String.valueOf(x)
        case _ => println("wo ye bu zhi dao sha lei xing ");String.valueOf(x)
      }
    })
    /*
    wo ye bu zhi dao sha lei xing
    1.0
    这是第一个元素返回的值，wo ye bu zhi dao sha lei xing 这句是case匹配到后，运行的结果，
    1.0是根据 val res: Iterator[String]的返回值，也就是case匹配到后，运行的最后一条语句执行的结果
    */
    while (res.hasNext)
      println(res.next())
  }
}
