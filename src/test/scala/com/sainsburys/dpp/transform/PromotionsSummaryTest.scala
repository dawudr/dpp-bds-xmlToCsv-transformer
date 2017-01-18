package com.sainsburys.dpp.transform

import scala.xml.{NodeSeq, XML}

/**
  * Created by bradley.reed on 17/01/2017.
  */
class PromotionsSummaryTest extends XmlTest {

  override val xslPath = "src/main/resources/domain/purchases/xsl/r10_extract_retailtransaction_promotionssummary.xsl"
  override val outPath = "output/test/promotionssummary"

  "The XML transformer" should "transform Promotions Summary XML" in {
    // TRY
    transformFiles((xml, csv) => {
      // VERIFY
      val csvData = loadCsvTo2dArray(csv)
      val xmlData = XML.loadFile(xmlPath + "/" + xml)

      val proSums = xmlData \\ "PromotionsSummary" \ "PromotionSummary"

      // The amount of rows in the CSV should = the number of summaries
      val csvRows = csvData.length
      val xmlRows = proSums.length
      csvRows should be (xmlRows)

      // Only run the test
      if (csvRows > 0 && xmlRows > 0) {
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
            6 -> rootNode \ "RewardType",
            7 -> rootNode \ "PromotionDescription",
            8 -> rootNode \ "QualifyingSpent",
            9 -> rootNode \ "TriggerTiming",
            10 -> rootNode \ "LoyaltyAccount" \ "LoyaltyProgram" \ "LoyaltyAccountID",
            // Finds Points with Type="PointsEarned"
            11 -> (rootNode \ "LoyaltyAccount" \ "LoyaltyProgram" \ "Points").filter { node =>
              node.attributes.exists(attr => {attr.key == "Type" && attr.value.text == "PointsEarned"})
            }
          )
        )
      }
    })
  }

}
