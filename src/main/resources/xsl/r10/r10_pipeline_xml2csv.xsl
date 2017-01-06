<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!--<xsl:output method="text"/>-->
	<xsl:strip-space elements="*"/>
	<!-- delimiter parameter values: ','|';'|':' -->
	<xsl:param name="param_delimiter" select="','"/>
	<!-- emptyfield parameter values: 'quot'|'' -->
	<xsl:param name="param_emptyfield" select="'quot'"/>

	<xsl:template match="*/*[1]" mode="s7_xml2csv">
		<xsl:apply-templates mode="fieldname" select="*"/>
		<xsl:text>&#xA;</xsl:text>
		<xsl:apply-templates mode="s7_xml2csv"/>
	</xsl:template>

	<!-- mode="fieldname" creates the field names in first line -->
	<xsl:template match="*/*/*" mode="fieldname">
		<xsl:value-of select="local-name()"/>
		<xsl:if test="position() != last()">
			<xsl:value-of select="$param_delimiter"/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*/*/*" mode="s7_xml2csv">
		<xsl:choose>
			<xsl:when test="text()">
				<xsl:apply-templates select="text()" mode="s7_xml2csv"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$param_emptyfield = ''"/>
					<xsl:otherwise>""</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="position() != last()">
			<xsl:value-of select="$param_delimiter"/>
		</xsl:if>
		<xsl:if test="position() = last()">
			<xsl:text>&#xA;</xsl:text>
		</xsl:if>
	</xsl:template>

	<!-- matching data. -->
	<xsl:template match="text()" mode="s7_xml2csv">
		<xsl:value-of select="."/>
	</xsl:template>

	<!-- matching data with quot. -->
	<!-- Priority attribute is used to prevent the template from being overridden. -->
	<!-- We could also have put it last. -->
	<xsl:template match="text()[contains(., '&quot;')]" priority="1" mode="s7_xml2csv">
		<xsl:value-of select="concat('&quot;', replace(., '&quot;', '&quot;&quot;'), '&quot;')"/>
	</xsl:template>

	<!-- matching data with comma. -->
	<xsl:template match="text()[contains(., ',')]" mode="s7_xml2csv">
		<xsl:choose>
			<xsl:when test="$param_delimiter = ','">
				<xsl:value-of select="concat('&quot;', ., '&quot;')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- matching data with semicolon. -->
	<xsl:template match="text()[contains(., ';')]" mode="s7_xml2csv">
		<xsl:choose>
			<xsl:when test="$param_delimiter = ';'">
				<xsl:value-of select="concat('&quot;', ., '&quot;')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- matching data with colon. -->
	<xsl:template match="text()[contains(., ':')]" mode="s7_xml2csv">
		<xsl:choose>
			<xsl:when test="$param_delimiter = ':'">
				<xsl:value-of select="concat('&quot;', ., '&quot;')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- matching data with leading/trailing whitespace. -->
	<xsl:template match="text()[starts-with(., ' ') or ends-with(., ' ')]" mode="s7_xml2csv">
		<xsl:value-of select="concat('&quot;', ., '&quot;')"/>
	</xsl:template>

	<!-- matching data with newline. -->
	<xsl:template match="text()[matches(., '\n\r?')]" mode="s7_xml2csv">
		<xsl:value-of select="concat('&quot;', ., '&quot;')"/>
	</xsl:template>

</xsl:stylesheet>
