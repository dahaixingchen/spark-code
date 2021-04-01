package com.feifei.spark.streaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

/**
 * @todo: 自定义 receiver
 * @return
 * @date: 2021/4/1 14:07
 */
class CustormReceiver(host:String,port:Int) extends Receiver[String](StorageLevel.MEMORY_AND_DISK) {
  override def onStart(): Unit = {
    //转化socket发送过来的数据

    val server = new Socket(host,port)
    val reader = new BufferedReader(new InputStreamReader(server.getInputStream))
    var line: String = reader.readLine()

    while (!isStopped() && line != null){
      //把receiver中的数据存下
      store(line)
      line = reader.readLine()
    }

  }

  override def onStop(): Unit = ???
}
