package com.wfm


import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.sql.SparkSession

object TreeDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("TreeDemo").master("local[*]").getOrCreate()
    //训练数据
    val data1 = spark.sparkContext.textFile("E:\\workspaces\\StudyProject\\sparkMLib\\src\\main\\resources\\data\\tree1")
    //测试数据
    val data2 = spark.sparkContext.textFile("E:\\workspaces\\StudyProject\\sparkMLib\\src\\main\\resources\\data\\tree2")

    //转换为向量
    val tree1 = data1.map(line =>{
      val parts = line.split(",")
      LabeledPoint(parts(0).toDouble,Vectors.dense(parts(1).split(" ").map(_.toDouble)))
    })

    val tree2 = data2.map(line =>{
      val parts = line.split(",")
      LabeledPoint(parts(0).toDouble,Vectors.dense(parts(1).split(" ").map(_.toDouble)))
    })

    val (trainingData,testData) = (tree1,tree2)

    //分类
    val numClasses = 2
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "entropy"

    //最大深度
    val maxDepth = 5

    //最大分支
    val maxBins = 32

    //训练模型
    val model = DecisionTree.trainClassifier(trainingData,numClasses,categoricalFeaturesInfo,impurity,maxDepth,maxBins)

    //模型预测
    val labelAndPreds = testData.map(point =>{
      val prediction = model.predict(point.features)
      (point.label,prediction)
    })

    //测试值与真实值比对
    val print_predit = labelAndPreds.take(15)

    println("label" + "\t" + "prediction")

    for(i <- 0 to print_predit.length - 1){
      println(print_predit(i)._1 + "\t" + print_predit(i)._2)
    }

    //错误率
    val testErr = labelAndPreds.filter(r => r._1 != r._2).count() / testData.count()
    println("Test Error = " + testErr)
    println(model.toDebugString)

  }
}
