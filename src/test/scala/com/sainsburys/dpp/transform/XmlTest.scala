package com.sainsburys.dpp.transform

import com.sainsburys.dpp.transform.service.XmlTransformService
import com.sainsburys.dpp.transform.xml.XmlTransformer
import org.scalatest.exceptions.TestFailedException
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer
import scala.xml.NodeSeq

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

  /**
    * Loops over each tuple in `fields`,
    * checking if the Node's text equals the
    * text in the `csvRow` at that index
    *
    * @param fields Map of Nodes to their index in the CSV row
    * @param csvRow Array of strings read from one row in the CSV
    */
  def assertFields(fields: Map[Int, NodeSeq], csvRow: Array[String]): Unit = {
    fields.foreach(field => {
      val column = csvRow(field._1)
      val node = field._2
      // If the node does not exist, is is actually a blank string ("") in the CSV
      // Eg, in PromotionSummary, <TotalRewardAmount> sometimes has Currency="GBP" but not always
      val value = if (node.length > 0) node text else "\"\""

//      try {
        column should be (value)
//      } catch {
//        case _: TestFailedException => println(field._1 + " not equal to " + (field._2 text))
//      }
    })
  }

}
