package com.sainsburys.dpp.transformation.xml

import java.io.{ByteArrayOutputStream, File, FileOutputStream}
import java.util.Date
import javax.xml.transform.stream.{StreamResult, StreamSource}
import javax.xml.transform.{Result, Source, Transformer, TransformerFactory}

import com.sainsburys.dpp.transformation.Loggable
import com.sainsburys.dpp.transformation.directoryreader.DirectoryReader

/**
  * XSLT Transformation engine.
  * Utilises custom XSL stylesheet rules for an XML format to output CSV.
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
class Xmltransformer(private val xmlPath: String,
                     private val xslSource: String,
                     private val csvPath: String) extends Loggable {

  logger.info("XSLT transformer started...")

  /*
  Transform using Directory Path
   */
  def transformPath() = {
    logger.info("XSLT transforming source path: {}", xmlPath)
    val directoryReader = new DirectoryReader(xmlPath)
    val directoryFiles = directoryReader.getListOfFiles()
    if(csvPath !=null) {
      directoryReader.createOutputDirectory(csvPath)
    }

    val tStart = new Date().getTime
    directoryFiles.foreach( {
      file => {
        if(csvPath != null) {
          val csvFileName = csvPath + File.separator + file.getName.substring(0, file.getName.indexOf(".xml")) + ".csv"
          transformFile(file.getPath, csvFileName)
        } else {
          logger.debug("XSLT output to console: {}", transformFile(file.getPath, null));
        }
      }
    })
    logger.info("TOTAL XML files processed: {} Time taken: {} ms", directoryFiles.length, new Date().getTime - tStart)
  }


  /*
  Transform XML to CSV file if target file path is specified otherwise return as string
   */
  def transformFile(xmlFile: String, csvFile: String) :String = {
    logger.info("XSLT transforming source XML file: {} started", xmlFile)
    if(csvFile != null) {
      // File output
      val fileOutputStream = new FileOutputStream(csvFile)
      val result: Result = new StreamResult(fileOutputStream)
      transformer(xmlFile, xslSource , result)
      logger.info("XSLT written output to target file: {}", csvFile)
      csvFile
    } else {
      // Console output
      val outputStream = new ByteArrayOutputStream()
      val result: Result = new StreamResult(outputStream)
      transformer(xmlFile ,xslSource , result)
      outputStream.toString
    }
  }

  /*
  Run transform XSLT
   */
  def transformer(xmlFile: String, xslFile: String, result: Result) = {
    logger.info("XSLT transforming xml source: {}", xmlFile + " with xsl: "  + xslFile )
    val tStart = new Date().getTime;
    val xmlSource: Source = new StreamSource(xmlFile)
    val xslSource: Source = new StreamSource(xslFile)
    val transformer = buildTransformer(xslSource)
    transformer.transform(xmlSource, result)

    logger.info("XSLT transform successful. Time taken: {}ms", new Date().getTime - tStart)
  }

  /*
  Return transformer instance
   */
  def buildTransformer(xslSource: Source): Transformer = {
    val transformerFactory = TransformerFactory.newInstance()
    transformerFactory.newTransformer(xslSource)
  }
}


object Xmltransformer {
  def apply(xmlSourceFileName: String, xslSourceFileName: String, csvTargetFileName: String = null): Xmltransformer = {
    if(csvTargetFileName == null) {
      new Xmltransformer(xmlSourceFileName, xslSourceFileName, null)
    } else {
      new Xmltransformer(xmlSourceFileName, xslSourceFileName, csvTargetFileName)
    }
  }
}


object XmlData {
  val posLog =
    <POSLog xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:r10Ex="http://www.Retalix.com/Extensions" FixVersion="1" MajorVersion="6" MinorVersion="0"
            xmlns="http://www.nrf-arts.org/IXRetail/namespace/">
      <Transaction MajorVersion="6" MinorVersion="0" FixVersion="1">
        <BusinessUnit>
          <UnitID Name="@UnitID">UnitID</UnitID>
        </BusinessUnit>
        <WorkstationID TypeCode="X:KeyboardPOS" WorkstationLocation="@WorkstationLocation">WorkstationID</WorkstationID>
        <SequenceNumber>SequenceNumber</SequenceNumber>
        <TransactionID>TransactionID</TransactionID>
        <OperatorID OperatorName="@OperatorName" WorkerID="@WorkerID"
                    OperatorType="Cashier">OperatorID</OperatorID>
        <RetailTransaction r10Ex:TransactionType="@TransactionType">
          <LineItem EntryMethod="@EntryMethod1">
            <Sale>
              <POSIdentity>
                <POSItemID>POSItemID1</POSItemID>
              </POSIdentity>
              <MerchandiseHierarchy ID="Merchandise" r10Ex:HierarchyPath="@HierarchyPath1"
              >MerchandiseHierarchy1</MerchandiseHierarchy>
              <MerchandiseHierarchy ID="Tax">0.00</MerchandiseHierarchy>
              <ItemID Type="X:EAN">ItemID1</ItemID>
              <Description r10Ex:Culture="en-GB">DescriptionCulture1</Description>
              <Description TypeCode="Long" r10Ex:Culture="en-GB">DescriptionLongCulture1</Description>
              <PromotionDeferredRewards xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                        xmlns="http://www.Retalix.com/Extensions">
                <Type>Member Account</Type>
                <Value>1.95</Value>
                <PromotionID>100000001</PromotionID>
                <Description>GOODS BASE POINTS</Description>
                <SequenceNumber>0</SequenceNumber>
                <DeferredID>4</DeferredID>
              </PromotionDeferredRewards>
              <PromotionDeferredRewards xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                        xmlns="http://www.Retalix.com/Extensions">
                <Type>Member Account</Type>
                <Value>0</Value>
                <PromotionID>100000004</PromotionID>
                <Description>QualifyingSpend</Description>
                <LineItemRewardPromotion>
                  <TriggerQuantity Units="1" UnitOfMeasureCode="EA">1</TriggerQuantity>
                  <ApportionmentAmount Currency="GBP">0</ApportionmentAmount>
                  <RewardSplitAmount Currency="GBP">0</RewardSplitAmount>
                  <RewardLevel>1</RewardLevel>
                </LineItemRewardPromotion>
                <SequenceNumber>0</SequenceNumber>
                <DeferredID>8</DeferredID>
              </PromotionDeferredRewards>
            </Sale>
            <ScanData>0000000323628</ScanData>
            <SequenceNumber>2</SequenceNumber>
            <BeginDateTime>2016-05-16T09:03:03.3011171+03:00</BeginDateTime>
            <EndDateTime>2016-05-16T09:03:03.3011171+03:00</EndDateTime>
          </LineItem>
          <LineItem EntryMethod="@EntryMethod2">
            <Sale>
              <POSIdentity>
                <POSItemID>POSItemID2</POSItemID>
              </POSIdentity>
              <MerchandiseHierarchy ID="Merchandise" r10Ex:HierarchyPath="@HierarchyPath2"
              >MerchandiseHierarchy2</MerchandiseHierarchy>
              <MerchandiseHierarchy ID="Tax">0.00</MerchandiseHierarchy>
              <ItemID Type="X:EAN">ItemID2</ItemID>
              <Description r10Ex:Culture="en-GB">DescriptionCulture2</Description>
              <Description TypeCode="Long" r10Ex:Culture="en-GB">DescriptionLongCulture2</Description>
              <PromotionDeferredRewards xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                        xmlns="http://www.Retalix.com/Extensions">
                <Type>Member Account</Type>
                <Value>1.95</Value>
                <PromotionID>100000001</PromotionID>
                <Description>GOODS BASE POINTS</Description>
                <SequenceNumber>0</SequenceNumber>
                <DeferredID>4</DeferredID>
              </PromotionDeferredRewards>
              <PromotionDeferredRewards xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                        xmlns="http://www.Retalix.com/Extensions">
                <Type>Member Account</Type>
                <Value>0</Value>
                <PromotionID>100000004</PromotionID>
                <Description>QualifyingSpend</Description>
                <LineItemRewardPromotion>
                  <TriggerQuantity Units="1" UnitOfMeasureCode="EA">1</TriggerQuantity>
                  <ApportionmentAmount Currency="GBP">0</ApportionmentAmount>
                  <RewardSplitAmount Currency="GBP">0</RewardSplitAmount>
                  <RewardLevel>1</RewardLevel>
                </LineItemRewardPromotion>
                <SequenceNumber>0</SequenceNumber>
                <DeferredID>8</DeferredID>
              </PromotionDeferredRewards>
            </Sale>
            <ScanData>0000000323628</ScanData>
            <SequenceNumber>2</SequenceNumber>
            <BeginDateTime>2016-05-16T09:03:03.3011171+03:00</BeginDateTime>
            <EndDateTime>2016-05-16T09:03:03.3011171+03:00</EndDateTime>
          </LineItem>
          <Total CurrencyCode="GBP">798.10</Total>
          <Total TotalType="TransactionNetAmount" CurrencyCode="GBP">725.55</Total>
          <Total TotalType="TransactionPurchaseQuantity">238</Total>
          <Total TotalType="TransactionPurchaseQuantity" TypeCode="Return">0</Total>
          <Total TotalType="X:TransactionTaxIncluded" CurrencyCode="GBP">39.17</Total>
          <Total TotalType="X:TransactionTaxSurcharge" CurrencyCode="GBP">0</Total>
          <Total TotalType="X:TransactionTaxFee" CurrencyCode="GBP">0</Total>
          <Total TotalType="X:ItemTaxFee" CurrencyCode="GBP">0</Total>
          <Total TotalType="TransactionTotalSavings" CurrencyCode="GBP">72.55</Total>
          <Total TotalType="TransactionGrandAmount" CurrencyCode="GBP">725.55</Total>
          <Total TotalType="X:TransactionTotalVoidAmount" CurrencyCode="GBP">0</Total>
          <Total TotalType="X:TransactionTotalVoidCount">0</Total>
          <Total TotalType="X:TransactionTotalReturnAmount" CurrencyCode="GBP">0</Total>
          <Total TotalType="TransactionTaxExemptAmount" CurrencyCode="GBP">0</Total>
          <Total TotalType="TransactionTenderApplied" CurrencyCode="GBP">725.55</Total>
          <Total TotalType="X:CashbackTotalAmount" CurrencyCode="GBP">0</Total>
          <Total TotalType="X:TransactionMerchandiseAmount" CurrencyCode="GBP">798.10</Total>
          <Total TotalType="X:TransactionNonMerchandiseAmount" CurrencyCode="GBP">0</Total>
          <Total TotalType="X:NonResettableGrandTotal" CurrencyCode="GBP">4082.35</Total>
          <Total TotalType="X:PreviousNonResettableGrandTotal" CurrencyCode="GBP">490.90</Total>
          <Customer r10Ex:IsAuthenticatedOffline="False" r10Ex:EntryMethod="Keyed">
            <CustomerID>43200713010</CustomerID>
            <Name>
              <Name>RYtrue 9826300043200713010</Name>
              <Name TypeCode="GivenName">9826300043200713010</Name>
              <Name TypeCode="FamilyName">RYtrue</Name>
            </Name>
            <r10Ex:CustomerExternalId>9826300043200713010</r10Ex:CustomerExternalId>
            <r10Ex:AccountNumber>9826300043200713</r10Ex:AccountNumber>
            <r10Ex:IssueNumber>01</r10Ex:IssueNumber>
            <r10Ex:Segment>Nectar</r10Ex:Segment>
            <r10Ex:SequenceNumber>1</r10Ex:SequenceNumber>
            <r10Ex:EndDateTime>2016-05-16T09:03:00.3872411+03:00</r10Ex:EndDateTime>
            <r10Ex:ScanData>9826300043200713010</r10Ex:ScanData>
            <r10Ex:CustomerOrderNumber>110201</r10Ex:CustomerOrderNumber>
          </Customer>
          <LoyaltyAccount>
            <LoyaltyProgram>
              <LoyaltyAccountID>4</LoyaltyAccountID>
              <Points Type="X:OpenBalance">2872.000</Points>
              <Points Type="Balance">3597.000</Points>
              <Points Type="PointsEarned">725</Points>
            </LoyaltyProgram>
            <LoyaltyProgram>
              <LoyaltyAccountID>10</LoyaltyAccountID>
              <Points Type="X:OpenBalance">2872.000</Points>
              <Points Type="Balance">3597.000</Points>
              <Points Type="PointsEarned">725</Points>
            </LoyaltyProgram>
            <LoyaltyProgram>
              <LoyaltyAccountID>1</LoyaltyAccountID>
              <Points Type="X:OpenBalance">3145.000</Points>
              <Points Type="Balance">3870.000</Points>
              <Points Type="PointsEarned">725</Points>
            </LoyaltyProgram>
            <LoyaltyProgram>
              <LoyaltyAccountID>8</LoyaltyAccountID>
              <Points Type="X:OpenBalance">6.000</Points>
              <Points Type="Balance">7.000</Points>
              <Points Type="PointsEarned">1</Points>
            </LoyaltyProgram>
          </LoyaltyAccount>
          <TaxDefinitions xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                          xmlns="http://www.Retalix.com/Extensions">
            <TaxAuthority>
              <AuthorityId>1</AuthorityId>
              <Descriptions>
                <Description Culture="en-GB">VAT</Description>
              </Descriptions>
            </TaxAuthority>
            <TaxRate>
              <RateId>2</RateId>
              <Type>Tax</Type>
              <AuthorityId>1</AuthorityId>
              <Descriptions>
                <Description Culture="en-GB">0.00%</Description>
              </Descriptions>
              <IsIncluded>true</IsIncluded>
              <TaxCalculatedMethodPercent>
                <Value>0.0000</Value>
                <ImpositionId>A</ImpositionId>
              </TaxCalculatedMethodPercent>
              <TaxIndicator>A</TaxIndicator>
              <RoundingType>Standard</RoundingType>
              <CouponReducesTaxationAmount>false</CouponReducesTaxationAmount>
            </TaxRate>
            <TaxRate>
              <RateId>82</RateId>
              <Type>Tax</Type>
              <AuthorityId>1</AuthorityId>
              <Descriptions>
                <Description Culture="en-GB">20.00%</Description>
              </Descriptions>
              <IsIncluded>true</IsIncluded>
              <TaxCalculatedMethodPercent>
                <Value>20.0000</Value>
                <ImpositionId>C</ImpositionId>
              </TaxCalculatedMethodPercent>
              <TaxIndicator>C</TaxIndicator>
              <RoundingType>Standard</RoundingType>
              <CouponReducesTaxationAmount>false</CouponReducesTaxationAmount>
            </TaxRate>
          </TaxDefinitions>
          <PromotionsSummary xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                             xmlns="http://www.Retalix.com/Extensions">
            <PromotionSummary>
              <PromotionID>IssuePPL</PromotionID>
              <RedemptionQuantity>1</RedemptionQuantity>
              <TotalRewardAmount Currency="GBP">0.02</TotalRewardAmount>
              <TriggerType>Spend</TriggerType>
              <IssuedCoupons>
                <IssuesCoupon Identifier="9b7f5569-6f93-48b9-83c0-1179898cd095" SeriesId="PPL"
                              OfferId="4389" RewardValue="0.02" StartDate="2016-05-16" ExpiryDate="2016-06-15">
                  <Description>PPL_2p</Description>
                  <ScanCode>43890201506167</ScanCode>
                </IssuesCoupon>
              </IssuedCoupons>
              <RewardType>Coupon</RewardType>
              <PromotionDescription>IssuePPL</PromotionDescription>
              <QualifyingSpent>139.80</QualifyingSpent>
              <TriggerTiming>UponTotal</TriggerTiming>
            </PromotionSummary>
          </PromotionsSummary>
        </RetailTransaction>
        <BusinessDayDate>2016-05-16</BusinessDayDate>
        <BeginDateTime>2016-05-16T09:03:00.1567871+03:00</BeginDateTime>
        <EndDateTime>2016-05-16T09:03:23.3457326+03:00</EndDateTime>
      </Transaction>
    </POSLog>
}