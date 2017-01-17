<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:r10Ex="http://www.nrf-arts.org/IXRetail/namespace/">
    <xsl:strip-space elements="*"/>
    <xsl:output method="text" media-type="text/plain"/>
    <xsl:include href="r10_pipeline_flatten.xsl"/>
    <xsl:include href="r10_pipeline_xml2csv.xsl"/>
    <xsl:include href="r10_pipeline_equalizing.xsl"/>

    <doc xmlns="http://www.oxygenxml.com/ns/doc/xsl">
        <desc>Flattern and Convert all Xpath under LoyaltyAccount</desc>
    </doc>
    <!-- Transaction Level Elements -->
    <xsl:variable name="var_Transaction_level">
        <xsl:apply-templates select="//r10Ex:Transaction/*[not(self::r10Ex:ReceiptImage |self::r10Ex:RetailTransaction)]" mode="s3_flatten"/>
    </xsl:variable>

    <!-- RetailTransaction Level Elements -->
    <xsl:variable name="var_RetailTransaction_level">
        <xsl:apply-templates select="//r10Ex:Transaction/r10Ex:RetailTransaction/*[not(child::*)]" mode="s3_flatten"/>
    </xsl:variable>

    <!-- Customer Level Elements -->
    <xsl:variable name="var_Customer_level">
        <xsl:apply-templates select="//r10Ex:Transaction/r10Ex:RetailTransaction/r10Ex:Customer/*" mode="s3_flatten"/>
    </xsl:variable>

    <!-- ReceiptImage Level Elements -->
    <xsl:variable name="var_ReceiptImage_level">
        <xsl:apply-templates select="//r10Ex:Transaction/r10Ex:ReceiptImage/*" mode="s3_flatten"/>
    </xsl:variable>

    <!-- RefundablePromotions Level Elements -->
    <xsl:variable name="var_RefundablePromotions_level">
        <xsl:apply-templates select="//r10Ex:Transaction/r10Ex:RetailTransaction/*:RefundablePromotions/*" mode="s3_flatten"/>
    </xsl:variable>
    
    <!-- /r10Ex:POSLog/r10Ex:Transaction/r10Ex:RetailTransaction/r10Ex:LoyaltyAccount -->
    <!-- Start here /r10Ex:POSLog -->
    <xsl:template match="/">
        <xsl:apply-templates select="//r10Ex:Transaction" mode="s1_user-defined"/>
    </xsl:template>

    <!-- /r10Ex:POSLog/r10Ex:Transaction -->
    <xsl:template match="r10Ex:Transaction" mode="s1_user-defined">
        <xsl:apply-templates select="r10Ex:RetailTransaction" mode="s1_user-defined"/>
    </xsl:template>


    <!-- /r10Ex:POSLog/r10Ex:Transaction/r10Ex:RetailTransaction -->
    <xsl:template match="r10Ex:RetailTransaction" mode="s1_user-defined">
        <!-- STEP-2: s2_joinxml-->
        <xsl:variable name="s2_joinxml">
            <xsl:element name="table">
                <xsl:for-each select="r10Ex:LoyaltyAccount">
                    <xsl:element name="tr">
                        <xsl:copy-of select="$var_Transaction_level/TransactionID"></xsl:copy-of>
                        <xsl:apply-templates select="." mode="s1_user-defined"/>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
        </xsl:variable>

        <!--<xsl:copy-of select="$s2_joinxml"/>-->

        <!-- STEP-3: r10_pipeline_equalizing.xsl -->
        <xsl:variable name="s4_equalizing">
            <xsl:apply-templates mode="s4_equalizing" select="$s2_joinxml"/>
        </xsl:variable>

        <!--<xsl:copy-of select="$s4_equalizing"/>-->

        <!-- STEP-4: r10_pipeline_xml2csv.xsl -->
        <!-- The final transformation of input or modified input xml file to CSV. -->
        <xsl:variable name="s5_xml2csv">
            <xsl:apply-templates mode="s7_xml2csv" select="$s4_equalizing"/>
        </xsl:variable>

        <xsl:value-of select="$s5_xml2csv"/>
    </xsl:template>

    <!-- /r10Ex:POSLog/r10Ex:Transaction/r10Ex:RetailTransaction/r10Ex:LoyaltyAccount -->
    <xsl:template match="r10Ex:LoyaltyAccount" mode="s1_user-defined">
        <xsl:for-each select="r10Ex:LoyaltyProgram">
            <tr>
                <!--<xsl:copy-of select="$var_Transaction_level/TransactionID"></xsl:copy-of>-->
                <xsl:apply-templates select="./*" mode="s3_flatten"/>
            </tr>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>