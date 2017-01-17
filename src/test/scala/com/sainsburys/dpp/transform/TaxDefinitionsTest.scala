package com.sainsburys.dpp.transform

import scala.xml.{Node, XML}

/**
  * Created by bradley.reed on 17/01/2017.
  */
class TaxDefinitionsTest extends XmlTest {

  override val xslPath = "src/main/resources/domain/purchases/xsl/r10_extract_retailtransaction_taxdefinitions.xsl"
  override val outPath = "output/test/taxdefinitions"

  "The XML transformer" should "transform Tax Definitions XML" in {
    // TRY
    transformFile()

    // VERIFY
    val csvData = loadCsvTo2dArray(outPath + "/R10xml.csv")
    val xmlData = XML.loadFile(xmlPath + "/R10xml.xml")

    val taxDefinitionData = xmlData \\ "TaxDefinitions"
    val taxAuthority = taxDefinitionData \ "TaxAuthority"
    val taxRates = taxDefinitionData \ "TaxRate"

    // The amount of rows in the CSV should = the number of Tax Rates
    (csvData length) should be (taxRates length)

    // Since the arrays are the same size we can zip them together to one array of tuples
    val zipped = csvData zip taxRates

    zipped foreach(iteration => {
      val (row, taxRate) = iteration
      assertFields(Map(
        0 -> xmlData \\ "TransactionID",
        1 -> taxAuthority \ "AuthorityId",
        2 -> (taxAuthority \ "Descriptions" \ "Description").head,
        3 -> taxRate \ "RateId",
        4 -> taxRate \ "Type",
        5 -> taxRate \ "AuthorityId",
        6 -> (taxRate \ "Descriptions" \ "Description").head,
        7 -> taxRate \ "IsIncluded",
        8 -> taxRate \ "TaxCalculatedMethodPercent" \ "Value",
        9 -> taxRate \ "TaxCalculatedMethodPercent" \ "ImpositionId",
        10 -> taxRate \ "TaxIndicator",
        11 -> taxRate \ "RoundingType",
        12 -> taxRate \ "CouponReducesTaxationAmount"
      ), row)
    })
  }

}
