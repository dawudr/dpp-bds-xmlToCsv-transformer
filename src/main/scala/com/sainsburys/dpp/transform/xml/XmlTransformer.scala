package com.sainsburys.dpp.transform.xml

import java.io.{ByteArrayOutputStream, File, FileOutputStream}
import java.util.Date
import javax.xml.transform.stream.{StreamResult, StreamSource}
import javax.xml.transform.{Result, Source, Transformer, TransformerFactory}

import com.sainsburys.dpp.transform.Loggable
import com.sainsburys.dpp.transform.config.ConfigProperties
import com.sainsburys.dpp.transform.util.ArchiveHelper

/**
  * XSLT Transformation engine.
  * Utilises custom XSL stylesheet rules for an XML format to output CSV.
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
class XmlTransformer(private val xmlPath: String,
                     private val xslSource: String,
                     private val csvPath: String) extends Loggable {

  logger.info("Transformer started...")

  /*
  Transform using Directory Path
   */
  def transformPath() = {
    logger.info("Transforming source path: {}", xmlPath)
    val archiveHelper = new ArchiveHelper(xmlPath)
    val archiveFiles = archiveHelper.getListOfFiles()
    if(csvPath !=null) {
      archiveHelper.createOutputDirectory(csvPath)
    }

    val tStart = new Date().getTime
    archiveFiles.foreach( {
      file => {
        if(csvPath != null) {
          val csvFileName = csvPath + File.separator + file.getName.substring(0, file.getName.indexOf(".xml")) + ".csv"
          transformFile(file.getPath, csvFileName)
        } else {
          logger.debug("Writing to console: {}", transformFile(file.getPath, null));
        }
      }
    })
    logger.info("TOTAL XML files processed: {} Time taken: {} ms", archiveFiles.length, new Date().getTime - tStart)
  }


  /*
  Transform XML to CSV file if target file path is specified otherwise return as string
   */
  def transformFile(xmlFile: String, csvFile: String) :String = {
    logger.info("Transforming source XML file: {}", xmlFile)
    if(csvFile != null) {
      // File output
      val fileOutputStream = new FileOutputStream(csvFile)
      val result: Result = new StreamResult(fileOutputStream)
      transformer(xmlFile, xslSource , result)
      logger.info("Written output to target file: {}", csvFile)
      csvFile
    } else {
      // Console output
      val outputStream = new ByteArrayOutputStream()
      val result: Result = new StreamResult(outputStream)
      transformer(xmlFile ,xslSource , result)
      outputStream.toString
    }
  }

  /*
  Run transform XSLT
   */
  def transformer(xmlFile: String, xslFile: String, result: Result) = {
    logger.info("Transforming xml source: {}", xmlFile + " with xsl: "  + xslFile )
    val tStart = new Date().getTime;
    val xmlSource: Source = new StreamSource(xmlFile)
    val xslSource: Source = new StreamSource(xslFile)
    val transformer = buildTransformer(xslSource)
    transformer.transform(xmlSource, result)

    logger.info("Transform successful. Time taken: {}ms", new Date().getTime - tStart)
  }

  /*
  Return transformer instance
   */
  def buildTransformer(xslSource: Source): Transformer = {
    val transformerFactory = TransformerFactory.newInstance()
    transformerFactory.newTransformer(xslSource)
  }
}

object XmlTransformer {
  def apply(configProperties: ConfigProperties): XmlTransformer = {
    new XmlTransformer(configProperties.xmlPath,
      configProperties.xslTemplate,
      configProperties.transformOutputPath)
  }
}