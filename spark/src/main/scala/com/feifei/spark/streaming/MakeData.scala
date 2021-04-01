package com.feifei.spark.streaming

import java.io.{OutputStream, PrintStream}
import java.net.{ServerSocket, Socket}

object MakeData {
  def main(args: Array[String]): Unit = {
    val listen = new ServerSocket(8889)
    println("server start")
    while (true){
      val client: Socket = listen.accept()

      new Thread(){
        override def run(): Unit = {
          var num = 0
          if (client.isConnected){
            val out: OutputStream = client.getOutputStream
            val printer = new PrintStream(out)
            while (client.isConnected){
              num =1
//              printer.println(s"hello ${num}")
//              printer.println(s"hi ${num}")
//              printer.println(s"hi ${num}")

              printer.println(s"hello")
              printer.println(s"hi")
              printer.println(s"hadoop")
              printer.println(s"360高清")
              printer.println(s"标清")
              printer.println(s"蓝光")
              printer.println(s"4K")
//              printer.println(s"hi")
              Thread.sleep(1000)
            }
          }
        }
      }.start()
    }
  }

}
