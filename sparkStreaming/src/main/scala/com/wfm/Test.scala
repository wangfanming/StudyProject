package com.wfm

import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.sql.SparkSession

object Test {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().appName("").master("local[*]").getOrCreate()
    val data = sparkSession.read.format("libsvm").load("")
    val Array(trainingData,testData) = data.randomSplit(Array(0.7,0.3),seed = 1234L)


    val categoricalFeaturesInfo = Map[Int,Int]()
    val impu = "entropy"
    val maxdepth = 5
    val maxbins = 32

//    DecisionTree.trainRegressor(trainingData,categoricalFeaturesInfo,impu,maxdepth,maxbins)

//    DecisionTreeClassifier·



  }
}
