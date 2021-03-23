package com.feifei.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Encoders, Row, SaveMode, SparkSession}

import scala.beans.BeanProperty

class Person  extends  Serializable {
  @BeanProperty
  var name :String = ""
  @BeanProperty
  var age:Int  =  0
}

object SqlApi {

  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder()
      .appName("api")
      .master("local")
      .getOrCreate()
    session.sparkContext.setLogLevel("error")

    import  session.implicits._
    val ds: Dataset[String] = List(
      "hello world",
      "hello world",
      "hello msb",
      "hello world",
      "hello world",
      "hello spark",
      "hello world",
      "hello spark"
    ).toDS()
    ds.createTempView("ooxx")
    //    val dfTab: DataFrame = session.sql("select * from ooxx")
//    dfTab.show()
//    dfTab.printSchema()
//
//    println("-------------------------------")
//    session.sql(" select word, count(*) " +
//      "from   (select explode(split(value,' ')) as word from ooxx) as tt   " +
//      "group by tt.word  ").show()
//    println("-------------------------------")
//
//    println("--------------explode-----------------")
//    session.sql(" select explode(split(value,' ')) as word from ooxx").show()
//    println("-------------------------------")
//
//    /**
//     *因为这种API的操作可以跳过SQL的字符串解析，所有要高效一点
//     */
//    println("--------------df的API操作-----------------")
//    val res: DataFrame = ds.selectExpr("explode(split(value,' ')) as word")
//      .groupBy("word")
//      .count()
//    res.show()
    val res1: DataFrame = ds.select("value")
    res1.write.mode(SaveMode.Append).text("./data/out/ooxx")
    val df1 = session.read.textFile("./data/out/ooxx")
    df1.show()
    df1.printSchema()
    val df2: Dataset[String] = session.read.textFile("./data/person.txt")

    val dsMap: Dataset[(String, Int)] = df2.map(line => {
      val str: Array[String] = line.split(" ")
      (str(0), str(1).toInt)
    })//(Encoders.tuple(Encoders.STRING,Encoders.scalaInt))


    //把数据和元数据结合起来，动态封装成dataFrame,然后用SQL操作

    //数据
    val rdd: RDD[String] = session.sparkContext.textFile("./data/person.txt")
    val userSchema = Array(
      "name string",
      "age int",
      "sex int"
    )

    def toDataType(vv:(String,Int))={
      userSchema(vv._2).split(" ")(1) match {
        case "string" => vv._1.toString
        case "int" => vv._1.toInt
      }
    }
    val rddRow: RDD[Row] = rdd.map(_.split(" "))
      .map(arr => Row.apply(arr(0), arr(1).toInt))

    rddRow
    //      .map(x => Row.apply(Array(x._1,x._2))))
    //元数据
    val fields = Array(
      StructField.apply("name", DataTypes.StringType, true),
      StructField.apply("age", DataTypes.IntegerType)
    )
    val schema: StructType = StructType.apply(fields)
    val df3: DataFrame = session.createDataFrame(rddRow,schema)

    df3.show()
    df3.printSchema()

    df3.createTempView("person")
    session.sql("select * from person where name ='lisi'").show()

    session.catalog.listTables().show()


  }
}
