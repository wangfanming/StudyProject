package com.wfm.operator

import java.util.Properties
import scala.language.dynamics
/**
 *
 * @ClassName DynamicProps
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 15:19
 * @version 1.0
 */
class DynamicProps(val props:Properties) extends Dynamic {
  def selectDynamic(name:String) = props.getProperty(name)
  def updateDynamic(name:String)(value:String) = props.setProperty(name,value)
  def applyDynamic(name:String)(args:Any*): Unit ={

  }
  def applyDynamicNamed(name:String)(args:(String,String)*): Unit ={
    if (name != "add") throw new IllegalArgumentException
    for ((k,v) <- args){
      props.setProperty(k,v)
    }
  }
}
