package com.wfm

import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.regression.DecisionTreeRegressor
import org.apache.spark.mllib.evaluation.RegressionMetrics
import org.apache.spark.sql.SparkSession
import scala.collection.JavaConversions._

object DecisionTreeStudy {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().appName("DecisionTreeStudy").master("local[*]").getOrCreate()

    val data = sparkSession.read.format("libsvm").load("E:\\workspaces\\StudyProject\\sparkMLib\\src\\main\\resources\\data\\sample_libsvm_data.txt")
    val Array(trainingData,testData) = data.randomSplit(Array(0.7,0.3),seed = 1234L)

    val decisionTreeRegressor = new DecisionTreeRegressor()
    val decisionTreeRegressionModel = decisionTreeRegressor.fit(trainingData)

    val predictions = decisionTreeRegressionModel.transform(testData)
    predictions.show()

//    计算 MSE MAE RMSE
    val evaluator= new BinaryClassificationEvaluator()
      .setLabelCol ("label").setRawPredictionCol("prediction")
    val accuracy = evaluator.evaluate(predictions)
    println(accuracy)
    val rm2 = new RegressionMetrics (predictions. select("prediction","label")
              .rdd.map(x =>(x(0).asInstanceOf[Double],x(1).asInstanceOf[Double])))

    println( "MSE:"+ rm2.meanSquaredError)
    println ("MAE: " + rm2.meanAbsoluteError)
    println ("RMSE Squared:" + rm2.rootMeanSquaredError)

  }

}
