package com.wfm

import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.ml.linalg.{Matrices, Vector}
import org.apache.spark.mllib.random.RandomRDDs._

/**
  * @Auther: wangfanming
  * @Date: 2019/8/7 15:08
  * @Description:
  */
object CrossValidatorStudy {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("CrossValidatorStudy")
      .master("local[*]")
      .getOrCreate()

//    val training = spark.createDataFrame(Seq(
//      (0L,"a b c d e spark",1.0),
//      (1L,"b d",0.0),
//      (2L,"spark f g h",1.0),
//      (3L,"hadoop mapreduce",0.0),
//      (5L,"g d a y",0.0),
//      (6L,"spark fly", 1.0),
//      (7L,"was mapreduce", 0.0),
//      (8L,"e spark program", 1.0),
//      (9L," a e c l", 0.0),
//      (10L,"spark compile", 1.0),
//      (11L,"hadoop software", 0.0)
//    )).toDF("id","text","label")
//
//    val tokenizer = new Tokenizer().setInputCol("text").setOutputCol("words")
//
//    val hashingTF = new HashingTF().setInputCol(tokenizer.getOutputCol).setOutputCol("features")
//
//    val lr = new LogisticRegression().setMaxIter(10)
//
//    val pipeline = new Pipeline().setStages(Array(tokenizer,hashingTF,lr))
//
//    val paramGrid = new ParamGridBuilder().addGrid(hashingTF.numFeatures,Array(10,100,1000))
//      .addGrid(lr.regParam,Array(0.1,0.01))
//      .build()
//
//    val cv = new CrossValidator()
//      .setEstimator(pipeline)
//      .setEvaluator(new BinaryClassificationEvaluator())
//      .setEstimatorParamMaps(paramGrid).setNumFolds(2)
//
//    val cvmodel = cv.fit(training)
//
//    val test = spark.createDataFrame(Seq(
//      (4L, "spark i j k"),
//      (5L,"l m n"),
//      (6L,"mapreduce spark"),
//      (7L,"apache hadoop"))).toDF("id","text")
//
//    val Predictions =cvmodel.transform(test)
//    Predictions.select("id","text","probability","prediction")
//      .collect()
//      .foreach {
//        case Row(id: Long, text: String, prob:Vector, prediction: Double)
//        => println(s"($id, $text) --> prob=$prob, prediction=$prediction")
//      }
//
//    val bestModel= cvmodel.bestModel.asInstanceOf[PipelineModel]
//    val lrModel = bestModel.stages(2).asInstanceOf[LogisticRegressionModel]
//    println(lrModel.getRegParam ) // 显示为 0.1
//    println(lrModel.numFeatures)  // 100

    val matrix = Matrices.sparse(3,3,Array(0,2,4,7),Array(0,2,1,2,0,1,2),Array(9,3,6,2,1,4,1))
    println(matrix)

    val u = normalRDD(spark.sparkContext,100L,10)

  }
}
