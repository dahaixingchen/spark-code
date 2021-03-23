package com.feifei.spark.streaming

import java.util
import java.util.{Map, Properties}
import java.util.regex.Pattern

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRebalanceListener, ConsumerRecord, ConsumerRecords, KafkaConsumer, OffsetAndMetadata}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer

object KafkaConsumer {
  def main(args: Array[String]): Unit = {
    val pro = new Properties()
    pro.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "node01:9092")
    pro.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    pro.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    pro.put(ConsumerConfig.GROUP_ID_CONFIG, "bula2")
    pro.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest") // earliest   latest


    pro.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    val consumer = new KafkaConsumer[String, String](pro)

    //订阅主题
    consumer.subscribe(Pattern.compile("ooxx"), new ConsumerRebalanceListener() {
      override def onPartitionsRevoked(partitions: util.Collection[TopicPartition]): Unit = {

      }

      //归本消费者管理的partitions的详细信息
      override def onPartitionsAssigned(partitions: util.Collection[TopicPartition]): Unit = {
      }
    })

    //拉取数据
    while (true) {

      val records: ConsumerRecords[String, String] = consumer.poll(0)
      if (!records.isEmpty) {
        println(s"------------------------${records.count()}----------------------")
        val iter: util.Iterator[ConsumerRecord[String, String]] = records.iterator()
        while (iter.hasNext) {
          val recode: ConsumerRecord[String, String] = iter.next()
          val topic: String = recode.topic()
          val partition: Int = recode.partition()
          val offset: Long = recode.offset()

          val key: String = recode.key()
          val value: String = recode.value()


          println(s"key: $key  value: $value  partition: $partition  offset: $offset ")
          val offMap: util.HashMap[TopicPartition, OffsetAndMetadata] = new util.HashMap[TopicPartition, OffsetAndMetadata]()
          offMap.put(new TopicPartition("ooxx", recode.partition()),new OffsetAndMetadata(recode.offset()))

          //等业务执行完后再提交offset
          consumer.commitSync(offMap)
        }
      }
    }

  }

}
