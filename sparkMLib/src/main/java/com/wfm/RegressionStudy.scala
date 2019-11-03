package com.wfm

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.{OneHotEncoder, VectorAssembler, VectorIndexer}
import org.apache.spark.ml.regression.{DecisionTreeRegressor, LinearRegression}
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}
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

    val data2 = new OneHotEncoder().setInputCol("season").setOutputCol("seasonVec")
    val data3 = new OneHotEncoder().setInputCol("yr").setOutputCol("yrVec")
    val data4 = new OneHotEncoder().setInputCol("mnth").setOutputCol("mnthVec")
    val data5 = new OneHotEncoder().setInputCol("hr").setOutputCol("hrVec")
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
      .setLabelCol("label")
      .setMaxBins(64)
      .setMaxDepth(15)

    val lr = new LinearRegression()
      .setFeaturesCol("features_lr")
      .setLabelCol("label")
      .setFitIntercept(true)
      .setMaxIter(20)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    val pipeline = new Pipeline().setStages(Array(assembler, featuresIndexer, dt))

    val pipeline_lr = new Pipeline().setStages(Array(assembler_lr, lr))

    //训练决策树回归模型
    val model = pipeline.fit(traingData)
//    //训练线性回归模型
//    val lrmodel = pipeline_lr.fit(traingData_lr)
//
//    //做出预测
//    val predictions = lrModel1.transform(testData)
//
//    val predictions_lr = lrmodel.transform(testData_lr)
//
//    val evaluator = new RegressionEvaluator()
//      .setLabelCol("label")
//      .setPredictionCol("prediction")
//      .setMetricName("rmse")
//
//    val rmse = evaluator.evaluate(predictions)
//
//    val rmse_lr = evaluator.evaluate(predictions_lr)
//
//    println("rmse: " + rmse)
//
//    println("rmse_lr:  " + rmse_lr)


    // 模型优化

    //对线性回归模型进行特征过滤，temp和atemp线性相关，且atemp的贡献度较小，所以 过滤掉该特征
    val assembler_lr1 = new VectorAssembler()
      .setInputCols(Array("seasonVec", "yrVec", "mnthVec", "hrVec", "holidayVec", "weekdayVec", "workingdayVec", "weathersitVec", "temp", "hum", "windspeed"))
      .setOutputCol("features_lr1")

    import org.apache.spark.ml.feature.SQLTransformer

    //对特征label进行SQRT运行
    val sqlTrans = new SQLTransformer().setStatement("SELECT *,POWER(label,2) as label1 FROM __THIS__")

    //建立模型，预测label1的值，设置线性回归参数
    val lr1 = new LinearRegression()
      .setFeaturesCol("features_lr1")
      .setLabelCol("label1")
      .setFitIntercept(true)

    //设置流水线
    val pipeline_lr1 = new Pipeline().setStages(Array(assembler_lr1,sqlTrans,lr1))

    //建立参数网格
    val paramGrid = new ParamGridBuilder()
      .addGrid(lr1.elasticNetParam,Array(0.0,0.8,1.0))
      .addGrid(lr1.regParam,Array(0.1,0.3,0.5))
      .addGrid(lr1.maxIter,Array(20,30))
      .build()

    //选择（prediction，label1），计算测试误差
    val evaluator_lr1 = new RegressionEvaluator()
      .setLabelCol("label1")
      .setPredictionCol("prediction")
      .setMetricName("r2")

    //采用交叉验证方法
    val trainValidationSplit = new TrainValidationSplit()
      .setEstimator(pipeline_lr1)
      .setEvaluator(evaluator_lr1)
      .setEstimatorParamMaps(paramGrid)
      .setTrainRatio(0.8)

    //训练模型并自动选择最优参数
    val lrModel1 = trainValidationSplit.fit(testData_lr)

    //查看模型全部参数
    lrModel1.getEstimatorParamMaps.foreach(println)  //参数组合

    lrModel1.getEvaluator.extractParamMap()  // 查看评估参数

    lrModel1.getEvaluator.isLargerBetter

    // 用最好的参数组合，做出预测
    val predictions_lr1 = lrModel1.transform(testData_lr)
    val rmse_lr1 = evaluator_lr1.evaluate(predictions_lr1)

    println("rmse_lr1  :" + rmse_lr1)

    predictions_lr1.select("features_lr1","label","label1","prediction").show(5)



  }
}
