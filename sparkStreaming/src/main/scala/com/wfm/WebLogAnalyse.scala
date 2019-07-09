package com.wfm

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WebLogAnalyse {
  def main(args: Array[String]): Unit = {
    val batch: Int = 10  //设置计算周期
    val sparkConf = new SparkConf().setAppName("WebLogAnalyse").setMaster("local[*]")

    val ssc: StreamingContext = new StreamingContext(sparkConf,Seconds(batch))
    ssc.sparkContext.setLogLevel("WARN")

    val input = "/home/wangfanming/IdeaProjects/StudyProject/sparkStreaming/src/main/resources/nginx/log"
    val lineStream: DStream[String] = ssc.textFileStream(input)

    //计算总PV
    lineStream.count().print()

    //各个IP的PV
    lineStream.map(line =>{(line.split(" ")(0),1)}).reduceByKey(_+_).print()

    //获取搜索引擎信息
    val urls: DStream[String] = lineStream.map(_.split(" ")(3))

    //先输出搜索引擎和查询关键词，避免统计搜索关键词时重复计算
    //输出（host,query_keys）
    val searchEnginInfo: DStream[(String, String)] = urls.map(url=>{
      val searchEngines = Map(
        "www.google.cn" -> "q",
        "www.yahoo.com" -> "p",
        "cn.bing.com" -> "q",
        "www.baidu.com" -> "wd",
        "www.sogou.com" -> "query"
      )
      val temp = url.split("/")
      if(temp.length > 2){
        val host = temp(2)
        if(searchEngines.contains(host)){
          val q = url.split("//?")
          if(q.length > 0){
            val query = q(1)
            val arr_search_q = query.split("&").filter(_.indexOf(searchEngines(host) + "=") == 0)
            if(arr_search_q.length > 0){
              (host,arr_search_q(0).split("=")(1))
            }else{
              (host,"")
            }
          }else{
            ("","")
          }
        }else{
          ("","")
        }
      }else{
        ("","")
      }
    })

    //搜索引擎PV
    searchEnginInfo.filter(_._1.length > 0).map(i =>(i._1,1)).reduceByKey(_+_).print()

    //关键词PV
    searchEnginInfo.filter(_._2.length > 0).map(i => (i._2,1)).reduceByKey(_+_).print()

    //终端PV
    lineStream.map(_.split("\"")(5)).map(agent =>{
      val types: Seq[String] = Seq("iPhone", "Android")
      var r = "Default"
      for(t <- types){
        if(agent.indexOf(t) != -1){
          r = t
        }
      }
      (r,1)
    })
      .reduceByKey(_+_).print()

    //各页面PV
    lineStream.map(line =>(line.split("\"")(1).split(" ")(1),1)).reduceByKey(_+_).print()

    ssc.start()
    ssc.awaitTermination()

  }
}
