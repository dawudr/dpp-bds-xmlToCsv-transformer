<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match="/" mode="s3_flatten">
        <xsl:apply-templates mode="s3_flatten"/>
    </xsl:template>

    <xsl:template match="/*" mode="s3_flatten">
        <xsl:copy>
            <!-- Attributes at top-level is ignored. -->
            <xsl:apply-templates select="node()" mode="s3_flatten"/>
        </xsl:copy>
    </xsl:template>

    <!-- Elements at item level.  -->
    <xsl:template match="/*/*" mode="s3_flatten">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="s3_flatten"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*" mode="s3_flatten">
        <xsl:choose>
            <xsl:when test="*">
                <xsl:apply-templates select="@*|node()" mode="s3_flatten"/>
            </xsl:when>
            <xsl:when test="exists(@ID)">
                <!-- Added Step: Customised flatten Attribute Name with @ID or @TypeCode and Value as an Element -->
                <xsl:variable name="name"
                    select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:variable name="id">
                    <xsl:value-of select="if(contains(@ID, ':')) then substring-after(@ID,':') else @ID"></xsl:value-of>
                </xsl:variable>
                <xsl:element
                    name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '', local-name(.), '.', $id)}">
                    <xsl:value-of select="."/>
                </xsl:element>
                <xsl:apply-templates select="@*" mode="s3_flatten"/>
            </xsl:when>
            <xsl:when test="exists(@Type)">
                <!-- Added Step: Used for LoyaltyProgram -->
                <xsl:variable name="name"
                              select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:variable name="id">
                    <xsl:value-of select="if(contains(@Type, ':')) then substring-after(@Type,':') else @Type"></xsl:value-of>
                </xsl:variable>
                <xsl:element
                        name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '', local-name(.), '.', $id)}">
                    <xsl:value-of select="."/>
                </xsl:element>
                <!--<xsl:apply-templates select="@*" />-->
            </xsl:when>
            <xsl:when test="exists(@Culture)">
                <!-- Added Step: Used for TaxAuthority.Description -->
                <xsl:variable name="name"
                              select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:variable name="id">
                    <xsl:value-of select="@Culture"></xsl:value-of>
                </xsl:variable>
                <xsl:element
                        name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '', local-name(.), '.', $id)}">
                    <xsl:value-of select="."/>
                </xsl:element>
                <!--<xsl:apply-templates select="@*" />-->
            </xsl:when>
            <xsl:when test="exists(@TypeCode)">
                <!-- Added Step: Customised flatten Attribute Name with @ID or @TypeCode and Value as an Element -->
                <xsl:variable name="name"
                              select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:variable name="id">
                    <xsl:value-of select="if(contains(@TypeCode, ':')) then substring-after(@TypeCode,':') else @TypeCode"></xsl:value-of>
                </xsl:variable>
                <xsl:element
                        name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '', local-name(.), '.', $id)}">
                    <xsl:value-of select="."/>
                </xsl:element>
                <xsl:apply-templates select="@*" mode="s3_flatten"/>
            </xsl:when>
            <xsl:when test="exists(@TotalType)">
                <!-- Added Step: Customised flatten Attribute Name with @ID or @TotalType and Value as an Element -->
                <xsl:variable name="name"
                    select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:variable name="id">
                    <xsl:value-of select="if(contains(@TotalType, ':')) then substring-after(@TotalType,':') else @TotalType"></xsl:value-of>
                </xsl:variable>
                <xsl:element
                    name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '', local-name(.), '.', $id)}">
                    <xsl:value-of select="."/>
                </xsl:element>
                <xsl:if test="exists(@CurrencyCode)">
                    <xsl:element
                        name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '', local-name(.), '.', $id, '.', 'CurrencyCode')}">
                        <xsl:value-of select="@CurrencyCode"></xsl:value-of>
                    </xsl:element>
                </xsl:if>
            </xsl:when>    
            <xsl:otherwise>
                <xsl:variable name="name"
                    select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:element
                    name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '',  local-name(.))}">
                    <xsl:value-of select="."/>
                </xsl:element>
                <xsl:apply-templates select="@*" mode="s3_flatten"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
     
    <!-- All attributes except first and second level.  --> 
    <xsl:template match="@*" mode="s3_flatten">
                <xsl:variable name="name"
                    select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:element
                    name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '.' else '',  local-name(.))}">
                    <xsl:value-of select="."/>
                </xsl:element>
    </xsl:template>
    
    <!-- Attributes of item (/*/*) need an extra ancestor level to get element name right. -->
    <xsl:template match="/*/*/@*" mode="s3_flatten">
        <xsl:variable name="name"
            select="if(count(ancestor::*) > 1) then for $a in ancestor::*[count(ancestor::*) > 0] return for $b in $a return local-name($b) else ''"/>
        <xsl:element
            name="{concat(string-join($name, '_'), if(count(ancestor::*) > 1)then '.' else '',  local-name(.))}">
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>
    
    <!-- Comments and processing-instructions are first ignored at STEP-7, s7_xml2csv.xsl. -->
    <!-- We keep them alive until then: They might contain information that can remind us of something. -->
    <xsl:template match="comment()|processing-instruction()" mode="s3_flatten">
        <xsl:copy/>          
    </xsl:template>

</xsl:stylesheet>
