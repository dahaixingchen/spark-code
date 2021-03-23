package com.feifei.spark.streaming

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, ConsumerStrategies, HasOffsetRanges, KafkaUtils, LocationStrategies, LocationStrategy, OffsetRange}
import org.apache.spark.streaming.{Duration, StreamingContext}


object StreamingToKafka {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[8]").setAppName("kafka-streaming")


    conf.set("spark.streaming.backpressure.enabled", "true")

    conf.set("spark.streaming.kafka.maxRatePerPartition", "3") //运行时状态每个分区拉取的条数
//    conf.set("spark.streaming.backpressure.initialRate", "5") //起步状态，在2.3.4版本中不起作用
//    conf.set("spark.streaming.receiver.maxRate", "3") //运行时状态，在2.3.4版本中不起作用

    //如果为true，Spark会StreamingContext在JVM关闭时正常关闭，而不是立即关闭。
    conf.set("spark.streaming.stopGracefullyOnShutdown", "true")

    val ssc: StreamingContext = new StreamingContext(conf, Duration(1000))
    ssc.sparkContext.setLogLevel("error")
    val map: Map[String, Object] = Map[String, Object](
      (ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "node01:9092,node02:9092"),
      (ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer]),
      (ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer]),
      (ConsumerConfig.GROUP_ID_CONFIG, "bula31"),
      (ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    )
    val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent, //preferconsistent：kafka有多少分区，就启动多少个consumer
      ConsumerStrategies.Subscribe[String, String](List("ooxx"), map)
    )
    val dstream: DStream[(String, (String, String, Int, Long))] = kafkaStream.map(record => {
      val t: String = record.topic()
      val p: Int = record.partition()
      val o: Long = record.offset()
      val k: String = record.key()
      val v: String = record.value()


      (k, (v, t, p, o))
    })
    dstream.print()
    kafkaStream.foreachRDD(rdd =>{
      //通过 HasOffsetRanges 可以调用私有的kafkaRdd
      val ranges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

      kafkaStream.asInstanceOf[CanCommitOffsets].commitAsync(ranges)
    } )

    ssc.start()
    ssc.awaitTermination()
  }
}
