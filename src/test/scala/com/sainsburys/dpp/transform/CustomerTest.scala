package com.sainsburys.dpp.transform

import scala.xml._

/**
  * Created by bradley.reed on 17/01/2017.
  */
class CustomerTest extends XmlTest {

  override val xmlPath = "src/test/resources/TLOG_4364_63_2016-03-11_22-12-34_147"
  override val xslPath = "src/main/resources/domain/purchases/xsl/r10_extract_retailtransaction_customer.xsl"
  override val outPath = "output/test/customer"

  "The XML transformer" should "transform Customer XML" in {
    // TRY
    transformFiles((xmlFile, csvFile) => {
      // VERIFY
      val csvData = loadCsvTo2dArray(csvFile)
      val xmlData = XML.loadFile(xmlPath + "/" + xmlFile)
      val custData = xmlData \\ "Customer"

      // Some XML files do not have customer details, instead just <Name />
      // This if statement will only run the test if the Customer Name is present
      // If the name is present, the rest of the fields are presumed to be
      val custName = custData \ "Name" \ "Name"
      if (custName.length > 0) {
        // Map of fields and their positions in the CSV output
        val fields = Map(
          0 -> xmlData \\ "TransactionID",
          1 -> custName.head,
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

    })
  }
}
