<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:r10Ex="http://www.nrf-arts.org/IXRetail/namespace/">
    <xsl:strip-space elements="*"/>
    <xsl:output method="text" media-type="text/plain"/>
    <xsl:include href="r10_pipeline_flatten.xsl"/>
    <xsl:include href="r10_pipeline_xml2csv.xsl"/>
    <xsl:include href="r10_pipeline_equalizing.xsl"/>

    <doc xmlns="http://www.oxygenxml.com/ns/doc/xsl">
        <desc>Flattern and Convert all Xpath under Customer</desc>
    </doc>

    <!-- Transaction Level Elements -->
    <xsl:variable name="var_Transaction_level">
        <xsl:apply-templates select="//r10Ex:Transaction/*[not(self::r10Ex:ReceiptImage |self::r10Ex:RetailTransaction)]" mode="s3_flatten"/>
    </xsl:variable>
    <!-- RetailTransaction Level Elements -->
    <xsl:variable name="var_RetailTransaction_level">
        <xsl:apply-templates select="//r10Ex:Transaction/r10Ex:RetailTransaction/*[not(child::*)]" mode="s3_flatten"/>
    </xsl:variable>

    <!--- START HERE -->

    <!-- /r10Ex:POSLog/r10Ex:Transaction/r10Ex:RetailTransaction/r10Ex:Customer -->
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
                <!-- Create a row for each Customer -->
                <xsl:for-each select="r10Ex:Customer">
                    <xsl:element name="tr">
                        <xsl:copy-of select="$var_Transaction_level/TransactionID" />
                        <xsl:apply-templates select="." mode="s1_user-defined"/>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
        </xsl:variable>

        <!-- STEP-3: r10_pipeline_equalizing.xsl -->
        <xsl:variable name="s4_equalizing">
            <xsl:apply-templates mode="s4_equalizing" select="$s2_joinxml" />
        </xsl:variable>

        <!-- STEP-4: r10_pipeline_xml2csv.xsl -->
        <!-- The final transformation of input or modified input xml file to CSV. -->
        <xsl:variable name="s5_xml2csv">
            <xsl:apply-templates mode="s7_xml2csv" select="$s4_equalizing" />
        </xsl:variable>

        <xsl:value-of select="$s5_xml2csv"/>
    </xsl:template>

    <!-- Put into Customer template -->
    <xsl:template match="r10Ex:Customer" mode="s1_user-defined">
        <!-- For the Customer name, take the first child element of the <Name></Name> -->
        <xsl:element name="customer_name">
            <xsl:value-of select="./r10Ex:Name/r10Ex:Name[1]" />
        </xsl:element>
        <xsl:apply-templates select="./*[not(self::r10Ex:Name)]" mode="s3_flatten" />
    </xsl:template>

</xsl:stylesheet>