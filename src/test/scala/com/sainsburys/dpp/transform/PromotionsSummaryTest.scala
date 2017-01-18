package com.sainsburys.dpp.transform

import scala.xml.XML

/**
  * Created by bradley.reed on 17/01/2017.
  */
class PromotionsSummaryTest extends XmlTest {

  override val xmlPath = "src/test/resources/xml/full"
  override val xslPath = "src/main/resources/domain/purchases/xsl/r10_extract_retailtransaction_promotionssummary.xsl"
  override val outPath = "output/test/promotionssummary"

  "The XML transformer" should "transform Promotions Summary XML" in {
    // TRY
    transformFile()

    // VERIFY
    val csvData = loadCsvTo2dArray(outPath + "/R10xml.csv")
    val xmlData = XML.loadFile(xmlPath + "/R10xml.xml")

    val proSums = xmlData \\ "PromotionsSummary" \ "PromotionSummary"

    // The amount of rows in the CSV should = the number of summaries
    (csvData length) should be (proSums length)

    // Since the arrays are the same size we can zip them together to one array of tuples
    val zipped = csvData zip proSums

    zipped foreach(iteration => {
      val (row, proSum) = iteration
      assertFields(Map(
        0 -> xmlData \\ "TransactionID",
        1 -> proSum \ "PromotionID",
        2 -> proSum \ "RedemptionQuantity",
        3 -> proSum \ "TotalRewardAmount",
        4 -> proSum \ "TotalRewardAmount" \ "@Currency",
        5 -> proSum \ "TriggerType",
        6 -> proSum \ "LoyaltyAccount" \ "LoyaltyProgram" \ "LoyaltyAccountID",
        // Finds Points with Type="PointsEarned"
        7 -> (proSum \ "LoyaltyAccount" \ "LoyaltyProgram" \ "Points").filter { node =>
          node.attributes.exists(attr => {attr.key == "Type" && attr.value.text == "PointsEarned"})
        },
        8 -> proSum \ "RewardType",
        9 -> proSum \ "PromotionDescription",
        10 -> proSum \ "QualifyingSpent",
        11 -> proSum \ "TriggerTiming"
      ), row)
    })
  }

}
