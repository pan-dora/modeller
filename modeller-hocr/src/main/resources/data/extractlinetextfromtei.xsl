<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tei="http://www.tei-c.org/ns/1.0"
                xmlns:functx="http://www.functx.com" xmlns:saxon="http://saxon.sf.net/"
                xmlns:xs="http://www.w3.org/2001/XMLSchema" extension-element-prefixes="saxon" version="2.0"
                xpath-default-namespace="http://www.w3.org/1999/xhtml">
    <xsl:output method="xml" indent="yes"/>
    <xsl:strip-space elements="*"/>
    <xsl:param name="base_uri">http://localhost:8080/fcrepo/rest/collection/test/004/doc/
    </xsl:param>
    <xsl:param name="padding">&#160;</xsl:param>
    <xsl:param name="break">
        <xsl:text>&#xa;</xsl:text>
    </xsl:param>
    <xsl:variable name="line" select="0" saxon:assignable="yes"/>
    <xsl:function name="functx:trim" as="xs:string">
        <xsl:param name="arg" as="xs:string?"/>
        <xsl:sequence select="replace(replace($arg, '\s+$', ''), '^\s+', '')"/>
    </xsl:function>
    <xsl:template match="tei:p">
        <xsl:apply-templates/>
        <xsl:for-each select="./descendant::*">
            <xsl:value-of select="./text()"/>
        </xsl:for-each>
    </xsl:template>
    <xsl:template match="tei:lb">
        <xsl:value-of select="$break"/>
    </xsl:template>
</xsl:stylesheet>