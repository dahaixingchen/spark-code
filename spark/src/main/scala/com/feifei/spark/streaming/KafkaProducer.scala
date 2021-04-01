package com.feifei.spark.streaming

import java.util.Properties
import java.util.concurrent.Future

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer

object KafkaProducer {

  def main(args: Array[String]): Unit = {
    val pro = new Properties()
    pro.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"node01:9092")
    pro.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,classOf[StringSerializer])
    pro.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,classOf[StringSerializer])
    val producer = new KafkaProducer[String,String](pro)

    while (true){
      for (i<- 1 to 3;j<- 1 to 3){
        val record: ProducerRecord[String,String] = new ProducerRecord[String,String]("ooxx",s"item$j",s"action$i")
        val records: Future[RecordMetadata] = producer.send(record)
        val metadata: RecordMetadata = records.get()
        val partition: Int = metadata.partition()
        val offset: Long = metadata.offset()
        println(s"item$j  action$i  partiton: $partition  offset: $offset")
        Thread.sleep(1000*2)
      }
    }
    producer.close()
  }

}
