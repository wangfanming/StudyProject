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

    val rawdata = spark.read.format("csv").option("header", true).load("D:\\Study\\dataSets\\Bike-Sharing-Dataset\\hour.csv")

    //    rawdata.show(4)

    //    rawdata.printSchema()

//    rawdata.describe("dteday", "holiday", "weekday", "temp").show()

    val data1 = rawdata.select(
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
      rawdata("cnt").cast("Double").alias("label")
    )


    val featuresArray = Array("season", "yr", "mnth", "hr", "holiday", "weekday", "workingday", "weathersit", "temp", "atemp", "hum", "windspeed")

    //把原数据组合成特征向量
    val assembler = new VectorAssembler()
      .setInputCols(featuresArray)
      .setOutputCol("features")

    //对类别特征进行索引化或数值化
    val featuresIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures").setMaxCategories(24)

    //对类别特征使用独热编码
    val data2 = new OneHotEncoder().setInputCol("season").setOutputCol("seasonVec")
    val data3 = new OneHotEncoder().setInputCol("yr").setOutputCol("yrVec")
    val data4 = new OneHotEncoder().setInputCol("mnth").setOutputCol("mnthVec")
    val data5 = new OneHotEncoder().setInputCol("hr").setOutputCol("hrVec")
    val data6 = new OneHotEncoder().setInputCol("holiday").setOutputCol("holidayVec")
    val data7 = new OneHotEncoder().setInputCol("weekday").setOutputCol("weekdayVec")
    val data8 = new OneHotEncoder().setInputCol("workingday").setOutputCol("workingdayVec")
    val data9 = new OneHotEncoder().setInputCol("weathersit").setOutputCol("weathersitVec")

    //OneHotEncoder不是Estimator,需要对采用回归算法的数据进行处理
    val pipeline_en = new Pipeline().setStages(Array(data2, data3, data4, data5, data6, data7, data8, data9))

    val data_lr = pipeline_en.fit(data1).transform(data1)

    //将原来的4个以及8个二元特征向量，拼接成一个feature向量
    val assembler_lr = new VectorAssembler()
      .setInputCols(Array("seasonVec", "yrVec", "mnthVec", "hrVec", "holidayVec", "weekdayVec", "workingdayVec", "weathersitVec", "temp", "atemp", "hum", "windspeed"))
      .setOutputCol("features_lr")

    //将data1数据集随即划分，用于决策模型
    val Array(traingData, testData) = data1.randomSplit(Array(0.7, 0.3), 12)

    //对data2数据进行随机划分，用于回归模型
    val Array(traingData_lr, testData_lr) = data_lr.randomSplit(Array(0.7, 0.3), 12)

    //设置决策树回归模型的参数
    val dt = new DecisionTreeRegressor()
      .setFeaturesCol("indexedFeatures")
      .setLabelCol("label")
      .setMaxBins(64)
      .setMaxDepth(15)

    //设置线性回归模型的参数
    val lr = new LinearRegression()
      .setFeaturesCol("features_lr")
      .setLabelCol("label")
      .setFitIntercept(true)
      .setMaxIter(20)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    //把决策树回归模型涉及到的特征转换及模型训练组装在一个流水线上
    val pipeline = new Pipeline().setStages(Array(assembler,featuresIndexer,dt))

    //把线性回归模型涉及的特征转换、模型训练组装再一个流水线上
    val pipeline_lr = new Pipeline().setStages(Array(assembler_lr,lr))

    //训练决策树回归模型
    val model = pipeline.fit(traingData)
    //训练线性回归模型
//    val lrmodel = pipeline_lr.fit(traingData_lr)

    //做出预测
    val predictions = model.transform(testData)

//    val predictions_lr = lrmodel.transform(testData_lr)

    val evaluator = new RegressionEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("rmse")

    val rmse = evaluator.evaluate(predictions)

//    val rmse_lr = evaluator.evaluate(predictions_lr)

    println("rmse: " + rmse)

//    println("rmse_lr:  " + rmse_lr)
































  }
}
