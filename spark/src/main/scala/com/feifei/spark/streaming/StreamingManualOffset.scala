package com.feifei.spark.streaming

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._
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
      ConsumerConfig.GROUP_ID_CONFIG -> "bula33",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false",
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest"
    )

    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc
      , LocationStrategies.PreferConsistent
      , ConsumerStrategies.Subscribe(List("ooxx")
        ,map))

    kafkaDStream.map(recode =>{
      val k: String = recode.key()
      val v: String = recode.value()
      val o: Long = recode.offset()
      val p: Int = recode.partition()
      (k,(v,o,p))
    }).print()

    //手动维护offset，实现至少一次消费
    kafkaDStream.foreachRDD(rdd => {

      //foreach对应的RDD的实现就是KafkaRDD，KafkaRDD是私有的，就用它对应的接口，实例化这个如下的rdd
      //得到各个分区消费之后的offset值
      val ranges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

      //采用闭包的形式，用到 KafkaUtils.createDirectStream 得到流，它里面 CanCommitOffsets 接口对应的提交offset的方法
      //把这些offset提交给Kafka的主题
      kafkaDStream.asInstanceOf[CanCommitOffsets].commitAsync(ranges)
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
