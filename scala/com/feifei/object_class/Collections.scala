package com.feifei.object_class

import java.util

import scala.collection.mutable.ListBuffer

object Collections {
  def main(args: Array[String]): Unit = {
    val value = new util.ArrayList[Int]()
    value.add(55)

    val array = Array[String]("xuxu","feifei","haha")
    array(1)="张晓旭"
    array.foreach(println)

    println("--------------list-------------")

    //这个list是不可以添加元素的,直接在它的后面进行初始化
    val list = List(2.36,4,5,6,3.99,4.654)

//    list(1) = 110
    for (elem <- list) {
      println(elem)
    }

    //可以动态添加元素，后面不可以直接初始化数据
    val listBuffer = new ListBuffer[String]()
    //todo ++ ++: 元素都是没有添加到集合里，只有=号了才加进去
    //todo += +=: 元素都会加到集合中，其中: 号的作用是把当前的元素放在最前面
    //todo ++= : 有两个 + 号的是直接加集合的拼接的
    listBuffer.+=("我是+=1")
    listBuffer.+=("我是+=2")
    listBuffer.+=("我是+=3")
    listBuffer.+=:("我是 +=：")
    listBuffer.+=("我是 +=4")
//    listBuffer.++=:("wo hsidf ++=:")

    val listBuffer2 = new ListBuffer[String]
    listBuffer2.+=("wo shi +=")
    listBuffer.++=(listBuffer2)
    listBuffer.++(listBuffer2)
//    listBuffer.++=(listBuffer2)
    listBuffer.++:("我是++：")
    listBuffer.foreach(println)


    println("--------------Set-------------")

    val set = Set[Int](1,5,6,1,3,5)
    set.foreach(print)

    import scala.collection.mutable.Set
    val set02 = Set(11,22,33,44,55,66)
    set02.add(77)
    set02.+=(88)
    set02.foreach(println)

    //immutable 表示不可变的，并且如果上面已经import了mutable包的话，这里还想用同名的Set集合，要写全名，不能直接import的方式
    val immutableSet = scala.collection.immutable.Set(111,555,666,222,333)
    immutableSet.foreach(println)

    println("--------------tuple-------------")

    val tuple2 = new Tuple2("sdf",56)
    val tuple22 = Tuple2(12,"ffege")
    val tuple222 = (2.6,'s')
    println(s"$tuple2 + $tuple22 + $tuple222")

    tuple222.productIterator.foreach(print(_))

    println("--------------map-------------")
    val map = Map(("a",6),"6"->7,8->9.6,list->set02)
    println(map)
    val keys = map.keys

    val map1 = Map( ("a",33) ,  "b"->22  ,("c",3434),("a",3333)  )
    val keys1 = map1.keys
    println(keys)

    val muMap = scala.collection.mutable.Map[Any,Any](("a",33) ,  "b"->22  ,("c",3434),("a",3333) )

    println("--------------艺术-------------")
    val list10 = List(1,5,6,2,7,5)
    val doublesList: List[Double] = list.map((x)=>{x+10})
    val doubles: List[Double] = doublesList.map(_*10)

    println("--------------艺术-升华-------------")
    val listStr = List(
      "hello world",
      "hello msb",
      "good idea"
    )
    listStr.foreach(println)
    val flatMapList: List[String] = listStr.flatMap(_.split(" "))
    flatMapList.foreach(println)
    val tuplesWord: List[(String, Int)] = flatMapList.map((_,1))
    //这里得到的是一个新的数据集，操作不是指针，是数据本身，数据会以对象的形式存放在内存中，所有你打印的时候，每次打印都是有真实的数据给到你
    tuplesWord.foreach(println)
    tuplesWord.foreach(println)

    println("--------------艺术-再升华-------------")

    //利用迭代器模式去拿数据，它操作的是指针，迭代器本身不存数据，所以它能节省很多的存储，适合用在大数据的场景下
    val iter: Iterator[String] = listStr.iterator
    val tuples: Iterator[(String, Int)] = iter.flatMap(_.split(" ")).map((_, 1))
    tuples.foreach(println)
    tuples.foreach(println)
    //上面操作已经把迭代器的指针指向了最后，所有在此打印的话，就只能打印就不行了
    val tuplesIter: Iterator[(String, Int)] = tuples
    while (tuplesIter.hasNext){
      val tuple: (String, Int) = tuplesIter.next()
      println(tuple)
    }
  }

}
