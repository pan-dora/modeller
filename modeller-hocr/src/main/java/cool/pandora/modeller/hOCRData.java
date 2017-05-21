package cool.pandora.modeller;

import org.xmlbeam.annotation.XBRead;

import java.util.List;

/**
 * hOCRData
 *
 * @author Christopher Johnson
 */
public interface hOCRData {
    /**
     *
     * @return PageNodeId
     */
    @XBRead("//*[local-name()='div'][@class='ocr_page']/@id")
    List<String> getPageNodeId();

    /**
     *
     * @return CAreaNodeId
     */
    @XBRead("//*[local-name()='div'][@class='ocr_carea']/@id")
    List<String> getCAreaNodeId();

    /**
     *
     * @return LineNodeId
     */
    @XBRead("//*[local-name()='span'][@class='ocr_line']/@id")
    List<String> getLineNodeId();

    /**
     *
     * @return WordNodeId
     */
    @XBRead("//*[local-name()='span'][@class='ocrx_word']/@id")
    List<String> getWordNodeId();

    /**
     *
     * @param id String
     * @return CAreasforPage
     */
    @XBRead("//*[local-name()='div'][@class='ocr_page'][@id='{0}']/descendant::node()[@class='ocr_carea']/@id")
    List<String> getCAreasforPage(String id);

    /**
     *
     * @param id String
     * @return LinesforArea
     */
    @XBRead("//*[local-name()='div'][@class='ocr_carea'][@id='{0}']/descendant::node()[@class='ocr_line']/@id")
    List<String> getLinesforArea(String id);

    /**
     *
     * @param id String
     * @return WordsforLine
     */
    @XBRead("//*[local-name()='span'][@class='ocr_line'][@id='{0}']/descendant::node()[@class='ocrx_word']/@id")
    List<String> getWordsforLine(String id);

    /**
     *
     * @param id String
     * @return WordsforPage
     */
    @XBRead("//*[local-name()='div'][@class='ocr_page'][@id='{0}']/descendant::node()[@class='ocrx_word']/@id")
    List<String> getWordsforPage(String id);

    /**
     *
     * @param id String
     * @return CAreaIdDescIds
     */
    @XBRead("//*[local-name()='div'][@class='ocr_carea'][@id='{0}']/descendant::node()/@id")
    List<String> getCAreaIdDescIds(String id);

    /**
     *
     * @param id String
     * @return TitleForId
     */
    @XBRead("//*[@id='{0}']/@title")
    String getTitleForId(String id);

    /**
     *
     * @param id String
     * @return CharsForId
     */
    @XBRead("//*[@id='{0}']//text()")
    String getCharsForId(String id);

}
