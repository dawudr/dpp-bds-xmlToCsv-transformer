package com.sainsburys.dpp.transformation

import com.sainsburys.dpp.transformation.config.{ConfigProperties, ConfigPropertiesFactory}
import com.sainsburys.dpp.transformation.service.XmlTransformService
import com.typesafe.config.ConfigFactory

/**
  * Transforms R10 XML format to CSV.
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
object Xml2CsvTransformer extends Loggable {

  def main(args: Array[String]): Unit = {

    logger.info("XML2CSV Transformation started...")
    val configProperties = ConfigFactory.load()
    val xmlPath = configProperties.getString("xml.source.path.r10")
    val xslSource = configProperties.getString("xsl.stylesheet.r10")
    val outputPath = configProperties.getString("csv.output.path.r10")

    val service = new XmlTransformService()
//    service.transformXmlFiles(xmlPath, xslSource, outputPath)
//    service.transformXmlFiles(xmlPath, xslSource, null)
    service.transformR10XmlToCsv()

    logger.info("XML2CSV Transformation finished the task successfully.")
    System.exit(0)
  }

}
