package com.wfm

import org.apache.spark.SparkContext
import org.apache.spark.ml.classification.KNNClassifier
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.mllib.util.MLUtils
/**
 * @Auther: wangfanming
 * @Date: 2019/7/29 16:17
 * @Description:
 */
object KNNTest {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("KNNTest").master("local[*]").getOrCreate()
    val sc: SparkContext = spark.sparkContext

    import spark.implicits._
    //read in raw label and features
    val training = MLUtils.loadLibSVMFile(sc, "E:\\workspaces\\StudyProject\\sparkMLib\\src\\main\\resources\\data\\sample_libsvm_data.txt").toDF()

    val knn = new KNNClassifier()
      .setTopTreeSize(training.count().toInt / 500)
      .setK(10)

    val knnModel = knn.fit(training)

    val predicted = knnModel.transform(training)
  }
}