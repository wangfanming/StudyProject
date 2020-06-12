package com.wfm


import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.java.hadoop.mapreduce.HadoopOutputFormat
import org.apache.flink.api.java.tuple.Tuple2
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.configuration.{ConfigConstants, Configuration}
import org.apache.hadoop.hbase.client.{Mutation, Put}
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job

/**
 *
 * @ClassName Write2Hbase
 * @Descripyion TODO 测试Flink将数据写入HBase
 * @author wangfanming
 * @date 2020/2/6 22:15
 * @version 1.0
 */
object Write2Hbase {
  def main(args: Array[String]): Unit = {
    // table params
    val tableName = "test-table"
    val cf = "someCf".getBytes(ConfigConstants.DEFAULT_CHARSET)
    val column = "someQual".getBytes(ConfigConstants.DEFAULT_CHARSET)

    // set up the execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment

    // create data
    val inputDs: DataSet[String] = env.readTextFile("D:\\Study\\workspace\\StudyProject\\flinkStudy\\src\\main\\resources\\hbaseKv.txt")

    val outputDs: DataSet[Tuple2[Int, String]] = inputDs.map(x => {
      val kvs = x.split(",")
      new Tuple2(kvs(0).toInt, kvs(1))
    })

    // emit result
    val job: Job = Job.getInstance
    job.getConfiguration.set(TableOutputFormat.OUTPUT_TABLE, tableName)

    job.getConfiguration.set("hbase.zookeeper.quorum","centos7");
    job.getConfiguration.set("hbase.zookeeper.property.clientPort","2181");
    job.getConfiguration.set("zookeeper.znode.parent","/hbase");

    outputDs.map(new RichMapFunction[Tuple2[Int, String], Tuple2[Text, Mutation]] {
      @transient private var reuse: Tuple2[Text, Mutation] = null

      @throws(classOf[Exception])
      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        reuse = new Tuple2[Text, Mutation]
      }

      /**
       * 将Tuple2[Int, String]类型的输入数据转成Tuple2[Text, Mutation]类型
       *
       * @param t
       * @return
       */
      @throws(classOf[Exception])
      override def map(t: Tuple2[Int, String]): Tuple2[Text, Mutation] = {
        reuse.f0 = new Text(Bytes.toBytes(t.f0))
        val put = new Put(t.f0.toString.getBytes(ConfigConstants.DEFAULT_CHARSET))
        put.addColumn(cf, column, Bytes.toBytes(t.f1))
        reuse.f1 = put
        reuse
      }
    }).output(new HadoopOutputFormat[Text, Mutation](new TableOutputFormat[Text], job))

    // execute program
    env.execute("write into hbase ...")
  }
}
