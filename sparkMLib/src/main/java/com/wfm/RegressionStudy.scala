package com.wfm

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.{OneHotEncoder, VectorAssembler, VectorIndexer}
import org.apache.spark.ml.regression.{DecisionTreeRegressor, LinearRegression}
import org.apache.spark.sql.SparkSession

/**
  * @Auther: wangfanming
  * @Date: 2019/8/8 11:54
  * @Description:
  */
object RegressionStudy {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("RegressionStudy")
      .master("local[*]")
      .getOrCreate()

    val rawdata = spark.read.format("csv").option("header", true).load("E:\\workspaces\\StudyProject\\sparkMLib\\src\\main\\resources\\data\\Bike-Sharing-Dataset\\hour.csv")

    //    rawdata.show(4)

    //    rawdata.printSchema()

//    rawdata.describe("dteday", "holiday", "weekday", "temp").show()

    val datal = rawdata.select(
      rawdata("season").cast("Double"),
      rawdata("yr").cast("Double"),
      rawdata("mnth").cast("Double"),
      rawdata("hr").cast("Double"),
      rawdata("holiday").cast("Double"),
      rawdata("weekday").cast("Double"),
      rawdata("workingday").cast("Double"),
      rawdata("weathersit").cast("Double"),
      rawdata("temp").cast("Double"),
      rawdata("atemp").cast("Double"),
      rawdata("hum").cast("Double"),
      rawdata("windspeed").cast("Double"),
      rawdata("cnt").cast("Double").alias("label"))


    val featuresArray = Array("season", "yr", "mnth", "hr", "holiday", "weekday", "workingday", "weathersit", "temp", "atemp", "hum", "windspeed")

    val assembler = new VectorAssembler()
      .setInputCols(featuresArray)
      .setOutputCol("features")

    val featuresIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures").setMaxCategories(24)

    val Array(traingData, testData) = datal.randomSplit(Array(0.7, 0.3), 12)

    val data2 = new OneHotEncoder().setInputCol("season").setOutputCol("seasonVee")
    val data3 = new OneHotEncoder().setInputCol("yr").setOutputCol("yrVee")
    val data4 = new OneHotEncoder().setInputCol("mnth").setOutputCol("mnthVec")
    val data5 = new OneHotEncoder().setInputCol("hr").setOutputCol("hrVee")
    val data6 = new OneHotEncoder().setInputCol("holiday").setOutputCol("holidayVec")
    val data7 = new OneHotEncoder().setInputCol("weekday").setOutputCol("weekdayVec")
    val data8 = new OneHotEncoder().setInputCol("workingday").setOutputCol("workingdayVec")
    val data9 = new OneHotEncoder().setInputCol("weathersit").setOutputCol("weathersitVec")

    val pipeline_en = new Pipeline().setStages(Array(data2, data3, data4, data5, data6, data7, data8, data9))

    val data_lr = pipeline_en.fit(datal).transform(datal)

    val assembler_lr = new VectorAssembler()
      .setInputCols(Array("seasonVec", "yrVec", "mnthVec", "hrVec", "holidayVec", "weekdayVec", "workingdayVec", "weathersitVec", "temp", "atemp", "hum", "windspeed"))
      .setOutputCol("features_lr")

    val Array(traingData_lr, testData_lr) = data_lr.randomSplit(Array(0.7, 0.3), 12)

    val dt = new DecisionTreeRegressor()
      .setFeaturesCol("indexedFeatures")
      .setVarianceCol("label")
      .setMaxBins(64)
      .setMaxDepth(15)

    val lr = new LinearRegression()
      .setFeaturesCol("features_lr")
      .setLabelCol("label")
      .setFitIntercept(true)
      .setMaxIter(20)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    val pipeline = new Pipeline().setStages(Array(assembler,featuresIndexer,dt))

    val pipeline_lr = new Pipeline().setStages(Array(assembler_lr,lr))

    //训练决策树回归模型
    val model = pipeline.fit(traingData)
    //训练线性回归模型
    val lrmodel = pipeline_lr.fit(traingData_lr)

    //做出预测
    val predictions = model.transform(testData)

    val predictions_lr = lrmodel.transform(testData_lr)

    val evaluator = new RegressionEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("rmse")

    val rmse = evaluator.evaluate(predictions)

    val rmse_lr = evaluator.evaluate(predictions_lr)

    println("rmse: " + rmse)

    println("rmse_lr:  " + rmse_lr)
































  }
}
