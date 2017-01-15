package com.sainsburys.dpp.transform.service

import com.sainsburys.dpp.transform.Loggable
import com.sainsburys.dpp.transform.config.ConfigProperties
import com.sainsburys.dpp.transform.xml.{XmlTransformer, XmlTransformer$}
import com.typesafe.config.ConfigFactory

/**
  * XML Transformation Services for different data sources XML formats
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
class XmlTransformService(private val xmlTransformer: XmlTransformer) extends Loggable {

  def transformXmlFiles(xmlPath :String, xslSource :String, transformOutputPath :String): Unit = {
    val xmltransformer = new XmlTransformer(xmlPath, xslSource, transformOutputPath)
    xmltransformer.transformPath()
  }

  def transformR10XmlToCsv(): Unit = {
    xmlTransformer.transformPath()
  }

}
