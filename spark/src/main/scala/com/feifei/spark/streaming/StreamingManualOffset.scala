package com.feifei.spark.streaming

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Duration, StreamingContext}

object StreamingManualOffset {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[3]").setAppName("manualOffset")
    //开启内部的被压机制，根据spark处理的速度有节奏的拉取Kafka的数据
    conf.set("spark.streaming.backpressure.enabled", "true")

    conf.set("spark.streaming.kafka.maxRatePerPartition", "300") //运行时状态每个分区拉取的条数
    conf.set("spark.streaming.receiver.maxRate", "3") //运行时状态，在2.3.4版本中不起作用

    //如果为true，Spark会StreamingContext在JVM关闭时正常关闭，而不是立即关闭。
    conf.set("spark.streaming.stopGracefullyOnShutdown", "true")

    val ssc = new StreamingContext(conf, Duration(1000))
    ssc.sparkContext.setLogLevel("error")

    var map: Map[String, Object] = Map(
      (ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"node01:9092"),
      ConsumerConfig.GROUP_ID_CONFIG -> "bula11",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest"
    )

    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent
      , ConsumerStrategies.Subscribe(List("ooxx"), map))
    kafkaDStream.map(recode =>{
      val k: String = recode.key()
      val v: String = recode.value()
      val o: Long = recode.offset()
      val p: Int = recode.partition()
      (k,(v,o,p))
    }).print()


    ssc.start()
    ssc.awaitTermination()
  }

}
