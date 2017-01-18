package com.sainsburys.dpp.transform

import scala.xml.{NodeSeq, XML}

/**
  * Created by bradley.reed on 17/01/2017.
  */
class IssuedCouponsTest extends XmlTest {

  override val xmlPath = "src/test/resources/TLOG_4364_63_2016-03-11_22-12-34_147"
  override val xslPath = "src/main/resources/domain/purchases/xsl/r10_extract_retailtransaction_issuedcoupons.xsl"
  override val outPath = "output/test/issuedcoupons"

  "The XML transformer" should "transform Issued Coupons XML for each PromotionSummary" in {
    // TRY
    transformFiles((xmlFile, csvFile) => {
      // VERIFY
      val csvData = loadCsvTo2dArray(csvFile)
      val xmlData = XML.loadFile(xmlPath + "/" + xmlFile)

      val proSums = xmlData \\ "PromotionsSummary" \ "PromotionSummary"

      // The amount of rows in the CSV should = the total number of IssuedCoupons in the XML file
      (csvData length) should be (proSums \ "IssuedCoupons" \ "IssuesCoupon" length)

      proSums.foreach(proSum => {
        val issuedCoupons = proSum \ "IssuedCoupons" \ "IssuesCoupon"
        // Test each coupon
        assertManyFields(csvData, issuedCoupons, (issuedCoupon: NodeSeq) =>
          Map(
            0 -> xmlData \\ "TransactionID",
            1 -> proSum \ "PromotionID",
            2 -> issuedCoupon \ "@Identifier",
            3 -> issuedCoupon \ "@SeriesId",
            4 -> issuedCoupon \ "@OfferId",
            5 -> issuedCoupon \ "@RewardValue",
            6 -> issuedCoupon \ "@StartDate",
            7 -> issuedCoupon \ "@ExpiryDate",
            8 -> issuedCoupon \ "Description",
            9 -> issuedCoupon \ "ScanCode"
          )
        )
      })
    })
  }
}