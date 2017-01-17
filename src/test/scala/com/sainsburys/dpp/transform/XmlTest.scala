package com.sainsburys.dpp.transform

import com.sainsburys.dpp.transform.service.XmlTransformService
import com.sainsburys.dpp.transform.xml.XmlTransformer
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer
import scala.xml.{Node, NodeSeq}

/**
  * Created by bradley.reed on 17/01/2017.
  */
class XmlTest extends FlatSpec with Matchers {

  val xmlPath = "src/test/resources/xml"
  val xslPath = "src/main/resources/domain/purchases/xsl/r10_pipeline.xsl"
  val outPath = "output/test"

  /**
    * Takes an XML file and transforms it according to the stylesheet
    * Then saves it to `outPath`
    * @param xmlFile The path to the XML file
    * @param xslFile The path to the stylesheet
    */
  def transformFile(xmlFile: String = xmlPath, xslFile: String = xslPath): Unit = {
    val xmlTransformer = new XmlTransformer(xmlFile, xslFile, outPath)

    val service = new XmlTransformService(xmlTransformer)
    service.transformR10XmlToCsv()
  }

  /**
    * Loads the CSV file into a list of arrays of strings
    * Each row is an array of strings for each column
    *
    * @return List of arrays of strings
    */
  def loadCsvTo2dArray(file: String): List[Array[String]] = {
    val returnArray = ListBuffer[Array[String]]()

    val bufferedSource = io.Source.fromFile(file)
    for (line <- bufferedSource.getLines) {
      val cols = line.split(",").map(_.trim)
      returnArray.append(cols)
    }

    // Only return the data, not the header row
    returnArray.toList.tail
  }

  def assertFields(fields: Map[Int, NodeSeq], csvRow: Array[String]): Unit = {
    fields.foreach(field => {
      csvRow(field._1) should be (field._2 text)
    })
  }

}
