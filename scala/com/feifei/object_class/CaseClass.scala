package com.feifei.object_class


object CaseClass {
  def main(args: Array[String]): Unit = {
    val hsq = new Dog("hsq",2)
    val dog = Dog("hsq",2)
    println(hsq.equals(dog))
    println(hsq == dog)
  }

}

/*样例类*/
//如果是class的时候，上面的dog比较是不会相等的，因为比的是两个对象
case class Dog(name:String,age:Int){
  def say():String={
    println(s"$name + $age")
    this.name
  }
}
