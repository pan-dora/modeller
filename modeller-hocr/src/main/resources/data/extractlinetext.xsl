<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:hocr="http://www.purl.org/hocr#" xmlns:functx="http://www.functx.com"
                xmlns:saxon="http://saxon.sf.net/" xmlns:xs="http://www.w3.org/2001/XMLSchema"
                extension-element-prefixes="saxon" version="2.0"
                xpath-default-namespace="http://www.w3.org/1999/xhtml">
    <xsl:output method="xml" indent="yes"/>
    <xsl:strip-space elements="*"/>
    <xsl:param name="base_uri">http://localhost:8080/fcrepo/rest/collection/test/013/line/
    </xsl:param>
    <xsl:param name="padding">&#160;</xsl:param>
    <xsl:variable name="line" select="0" saxon:assignable="yes"/>
    <xsl:function name="functx:trim" as="xs:string">
        <xsl:param name="arg" as="xs:string?"/>
        <xsl:sequence select="replace(replace($arg, '\s+$', ''), '^\s+', '')"/>
    </xsl:function>
    <xsl:template match="span[@class = 'ocr_line']">
        <xsl:element name="hocr:hasContent">
            <xsl:attribute name="hocr:lineid">
                <xsl:value-of select="substring-after(./@id, 'line_')"/>
            </xsl:attribute>
            <xsl:for-each select="./descendant::span[@class='ocrx_word']">
                <xsl:value-of select="./text()"/><xsl:value-of select="$padding"/>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>