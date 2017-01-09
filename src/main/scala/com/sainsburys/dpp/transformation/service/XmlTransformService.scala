package com.sainsburys.dpp.transformation.service

import com.sainsburys.dpp.transformation.Loggable
import com.sainsburys.dpp.transformation.config.ConfigProperties
import com.sainsburys.dpp.transformation.xml.Xmltransformer
import com.typesafe.config.ConfigFactory

/**
  * XML Transformation Services for different data sources XML formats
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
class XmlTransformService() extends Loggable {

  logger.info("Connecting to Amazon S3")

  def transformXmlFiles(xmlPath :String, xslSource :String, outputPath :String): Unit = {
    val xmltransformer = new Xmltransformer(xmlPath, xslSource, outputPath)
    xmltransformer.transformPath()
  }

  def transformR10XmlToCsv(): Unit = {
    val configProperties = ConfigFactory.load()
    val xmlPath = configProperties.getString("xml.source.path.r10")
    val xslSource = configProperties.getString("xsl.stylesheet.r10")
    val outputPath = configProperties.getString("csv.output.path.r10")

    val xmltransformer = new Xmltransformer(xmlPath, xslSource, outputPath)
    xmltransformer.transformPath()
  }

}

object XmlTransformService {
  def apply(configProperties: ConfigProperties): XmlTransformService = {
      new XmlTransformService()
  }
}
