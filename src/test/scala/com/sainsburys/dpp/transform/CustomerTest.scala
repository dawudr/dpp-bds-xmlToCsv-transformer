package com.sainsburys.dpp.transform

import scala.xml._

/**
  * Created by bradley.reed on 17/01/2017.
  */
class CustomerTest extends XmlTest {

  override val xslPath = "src/main/resources/domain/purchases/xsl/r10_extract_retailtransaction_customer.xsl"
  override val outPath = "output/test/customer"

  "The XML transformer" should "transform Customer XML" in {
    // TRY
    transformFile()

    // VERIFY
    val csvData = loadCsvTo2dArray(outPath + "/R10xml.csv")
    val xmlData = XML.loadFile(xmlPath + "/R10xml.xml")
    val custData = xmlData \\ "Customer"

    // Map of fields and their positions in the CSV output
    val fields = Map(
      0 -> xmlData \\ "TransactionID",
      1 -> (custData \ "Name" \ "Name").head,
      2 -> custData \ "CustomerID",
      3 -> custData \ "CustomerExternalId",
      4 -> custData \ "AccountNumber",
      5 -> custData \ "IssueNumber",
      6 -> custData \ "Segment",
      7 -> custData \ "SequenceNumber",
      8 -> custData \ "EndDateTime",
      9 -> custData \ "ScanData",
      10 -> custData \ "CustomerOrderNumber"
    )

    // Verify the fields using this function
    csvData.foreach(assertFields(fields, _))
  }
}
