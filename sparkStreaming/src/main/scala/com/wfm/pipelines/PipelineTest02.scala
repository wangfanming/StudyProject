package com.wfm.pipelines

import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.ml.linalg.{Matrices, Matrix, Vector}
import org.apache.spark.sql.{Row, SparkSession}

object PipelineTest02 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("PipelineTest02").master("local[*]").getOrCreate()
    val training = spark.createDataFrame(Seq(
      (0L,"a b c d e spark",1.0),
      (1L,"b d",0.0),
      (2L,"spark f g h",1.0),
      (3L,"hadoop mapreduce",0.0)
    )).toDF("id","text","label")

    val tokenizer = new Tokenizer()
      .setInputCol("text")
      .setOutputCol("workds")

    val hashingTF = new HashingTF()
      .setNumFeatures(1000)
      .setInputCol(tokenizer.getOutputCol)
      .setOutputCol("features")

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.001)

    val pipeline = new Pipeline()
      .setStages(Array(tokenizer,hashingTF,lr))

    //在训练集上使用Pipeline
    val model = pipeline.fit(training)

    //可以保存测试好的模型到磁盘
    model.write.overwrite().save("D:\\Study\\workspace\\StudyProject\\sparkStreaming\\src\\main\\resources\\saprk-logsticRegression-model")

    //也可以保存未安装好的Pipeline到磁盘
    pipeline.write.overwrite().save("D:\\Study\\workspace\\StudyProject\\sparkStreaming\\src\\main\\resources\\unfit-lr-model")

    //装载模型
    val sameModel = PipelineModel.load("D:\\Study\\workspace\\StudyProject\\sparkStreaming\\src\\main\\resources\\saprk-logsticRegression-model")

    val  test = spark.createDataFrame(Seq(
      (4L,"spark i j k"),
      (5L,"l m n"),
      (6L,"saprk hadoop,spark"),
      (7L,"apache hadoop")
    )).toDF("id","text")

    model.transform(test)
      .select("id","text","Probability","prediction")
      .collect()
      .foreach{ case Row(id:Long,text: String,prob:Vector,predicion:Double) =>{
        println(s"($id,$text) --> prob = $prob,prediction = $predicion,")
      }}

    val sm: Matrix = Matrices.sparse(3, 2, Array(0, 1, 3), Array(0, 2, 1), Array(9, 6, 8))


  }
}
