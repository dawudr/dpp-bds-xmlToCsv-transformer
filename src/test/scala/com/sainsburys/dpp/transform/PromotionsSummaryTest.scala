package com.sainsburys.dpp.transform

import scala.xml.{NodeSeq, XML}

/**
  * Created by bradley.reed on 17/01/2017.
  */
class PromotionsSummaryTest extends XmlTest {

  override val xmlPath = "src/test/resources/fullxml"
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

    // This takes the CSV data 2D array, and the list of Nodes,
    // and calls the function for each row to get the mapping
    // Then it calls assertFields to test each row in the data
    assertManyFields(csvData, proSums, (rootNode: NodeSeq) =>
      Map(
        0 -> xmlData \\ "TransactionID",
        1 -> rootNode \ "PromotionID",
        2 -> rootNode \ "RedemptionQuantity",
        3 -> rootNode \ "TotalRewardAmount",
        4 -> rootNode \ "TotalRewardAmount" \ "@Currency",
        5 -> rootNode \ "TriggerType",
        6 -> rootNode \ "LoyaltyAccount" \ "LoyaltyProgram" \ "LoyaltyAccountID",
        // Finds Points with Type="PointsEarned"
        7 -> (rootNode \ "LoyaltyAccount" \ "LoyaltyProgram" \ "Points").filter { node =>
          node.attributes.exists(attr => {attr.key == "Type" && attr.value.text == "PointsEarned"})
        },
        8 -> rootNode \ "RewardType",
        9 -> rootNode \ "PromotionDescription",
        10 -> rootNode \ "QualifyingSpent",
        11 -> rootNode \ "TriggerTiming"
      )
    )
  }

}
