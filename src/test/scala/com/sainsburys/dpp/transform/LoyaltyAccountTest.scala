package com.sainsburys.dpp.transform

import scala.xml.XML

/**
  * Created by bradley.reed on 17/01/2017.
  */
class LoyaltyAccountTest extends XmlTest {

  override val xslPath = "src/main/resources/domain/purchases/xsl/r10_extract_retailtransaction_loyaltyaccount.xsl"
  override val outPath = "output/test/loyaltyaccount"

  "The XML transformer" should "transform Loyalty Account XML" in {
    // TRY
    transformFile()

    // VERIFY
    val csvData = loadCsvTo2dArray(outPath + "/R10xml.csv")
    val xmlData = XML.loadFile(xmlPath + "/R10xml.xml")
    val loyaltyAccountData = xmlData \\ "LoyaltyAccount"
    val loyaltyProgramData = loyaltyAccountData \ "LoyaltyProgram"

    // The amount of nodes in loyaltyProgramData should be equal
    // to the number of rows in the CSV output
    (csvData length) should be (loyaltyProgramData length)

    var i = 0
    csvData foreach(row => {
      // If there is an out of bounds exception, the test should fail
      val thisLoyaltyProgram = loyaltyProgramData(i)

      // Get points for x:OpenBalance
      val pointsNodes = thisLoyaltyProgram \ "Points"
      val openBalance = pointsNodes filter { node =>
        node.attributes.exists(_.value.text == "X:OpenBalance")
      }
      val balance = pointsNodes filter { node =>
        node.attributes.exists(_.value.text == "Balance")
      }
      val pointsEarned = pointsNodes filter { node =>
        node.attributes.exists(_.value.text == "PointsEarned")
      }

      // Transaction ID
      row(0) should be (xmlData \\ "TransactionID" text)

      // Loyalty account ID
      row(1) should be (thisLoyaltyProgram \ "LoyaltyAccountID" text)

      row(2) should be (openBalance text)
      row(3) should be (balance text)
      row(4) should be (pointsEarned text)

      // Increment the index counter
      i += 1
    })
  }

}
