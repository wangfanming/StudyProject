package com.wfm.pipelines

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vector,Vectors}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.{Row, SparkSession}

object pipelineTest01 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("pipelineTest01").master("local[*]").getOrCreate()

    val training = spark.createDataFrame(Seq(
      (1.0,Vectors.dense(0.0,1.1,0.1)),
      (0.0,Vectors.dense(2.0,1.0,-1.0)),
      (0.0,Vectors.dense(2.0,1.3,1.0)),
      (1.0,Vectors.dense(0.0,1.2,-0.5))
    )).toDF("label","features")

    //创建一个LogsticRegression实例，该实例是一个估计器
    val lr = new LogisticRegression()

    //通过set方法设置参数
    lr.setMaxIter(10).setRegParam(0.01)

    //使用设置的参数训练模型
    val model1 = lr.fit(training)
    //model是模型（即由估计器生成的转换器），我们可以查看它在fit()中使用的参数
    println("Modle 1 was fit using parematers :" + model1.parent.extractParamMap())


    //这里展示了设置参数的不同方法，后边设置的参数会覆盖前边的设置
    val paramMap = ParamMap(lr.maxIter -> 20).put(lr.maxIter,30).put(lr.regParam -> 0.1,lr.threshold -> 0.55)

    //也可以组合ParamMap
    val paramMap2 = ParamMap(lr.probabilityCol -> "myProbability")
    val paramMapCombined = paramMap ++ paramMap2

    //使用paramMapCombined训练一个新模型
    val model2 = lr.fit(training,paramMapCombined)
    println("Modle 1 was fit using parematers :" + model2.parent.extractParamMap())

    //准备测试数据
    val test = spark.createDataFrame(Seq(
      (1.0,Vectors.dense(-1.0,1.5,1.3)),
      (0.0,Vectors.dense(3.0,2.0,-0.1)),
      (1.0,Vectors.dense(0.0,2.2,-1.5))
    )).toDF("label","features")

    //使用Transformer.transform()方法对测试数据进行预测
    //LogicRegression将仅使用 features列

    model2.transform(test)
      .select("features","label","myProbability","prediction")
      .collect()
      .foreach{ case Row(features:Vector,label: Double,prob:Vector,predicion:Double) =>{
        println(s"($features,$label) -> prob = $prob,prediction = $predicion,")
      }}





























  }
}
