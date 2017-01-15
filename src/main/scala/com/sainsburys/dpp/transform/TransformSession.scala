package com.sainsburys.dpp.transform

import com.sainsburys.dpp.transform.config.ConfigPropertiesFactory
import com.sainsburys.dpp.transform.service.XmlTransformService
import com.sainsburys.dpp.transform.xml.XmlTransformer

/**
  * Transforms R10 XML format to CSV.
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
object TransformSession extends Loggable {

  def main(args: Array[String]): Unit = {

    logger.info("XML2CSV Transformation started...")
    lazy val configProperties = ConfigPropertiesFactory.load(args)

    // initialise transformer
    val xmlTransformer: XmlTransformer = XmlTransformer(configProperties)

    val service = new XmlTransformService(xmlTransformer)
    service.transformR10XmlToCsv()

    logger.info("XML2CSV Transformation finished the task successfully.")
    System.exit(0)
  }

}
