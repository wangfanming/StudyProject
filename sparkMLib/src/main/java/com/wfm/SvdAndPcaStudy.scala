package com.wfm

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.sql.SparkSession

object SvdAndPcaStudy {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().appName("SvdAndPcaStudy").master("local[*]").getOrCreate()

    val data = Array(Vectors.dense(1,2,3,4,5,6,7,8,9),
          Vectors.dense(3,2,4,8,5,6,7,8,9),
          Vectors.dense(1,2,0,4,5,9,7,1,0),
          Vectors.dense(6,2,3,4,5,9,7,8,4),
          Vectors.dense(4,5,7,1,4,0,2,1,8))

    val dataRDD = sparkSession.sparkContext.parallelize(data,2)

    val matrix = new RowMatrix(dataRDD)

    val svd = matrix.computeSVD(3,computeU = true)
    val U = svd.U  //左奇异矩阵
    val s = svd.s     // 从大到小的奇异值向量



  }
}
