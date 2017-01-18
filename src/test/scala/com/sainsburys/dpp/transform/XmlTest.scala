package com.sainsburys.dpp.transform

import com.sainsburys.dpp.transform.service.XmlTransformService
import com.sainsburys.dpp.transform.xml.XmlTransformer
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

      column should be (value)
    })
  }

  /**
    * This function takes the CSV data, a NodeSeq of root nodes, and a callback function to return a Map
    * It then loops each Node, passing it to the callback which returns a Map of column indices of the data in the CSV
    * and a Node object where the data can be found in the CSV
    * These are then passed to `assertFields`, which loops each field and tests that the correct data appears in the CSV
    *
    * @param csvData     2D array of data read from the CSV
    * @param nodes       Array of root nodes for the data in the XML, for each row in the CSV
    * @param getFieldMap Callback which should return a Map of column indices in the CSV to an XML node
    */
  def assertManyFields(csvData: List[Array[String]], nodes: NodeSeq, getFieldMap: (NodeSeq) => Map[Int, NodeSeq]): Unit = {
    // Since the arrays are the same size we can zip them together to one array of tuples
    val zipped = csvData zip nodes

    // For each tuple of (csvRow, node), call the callback to get the mapping
    // Then call assertFields, passing the mapping and the row to do the tests
    zipped foreach(iteration => {
      val (row, node) = iteration
      assertFields(getFieldMap(node), row)
    })
  }

}
