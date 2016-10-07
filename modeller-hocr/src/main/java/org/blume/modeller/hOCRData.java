package org.blume.modeller;

import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBRead;
import org.xmlbeam.annotation.XBWrite;

import java.util.ArrayList;
import java.util.List;

public interface hOCRData {
    @XBRead("//*[local-name()='div'][@class='ocr_page']/@id")
    List<String> getPageNodeId();

    @XBRead("//*[local-name()='div'][@class='ocr_carea']/@id")
    List<String> getCAreaNodeId();

    @XBRead("//*[local-name()='div'][@class='ocr_page'][@id='{0}']/descendant::node()[@class='ocr_carea']/@id")
    List<String> getCAreasforPage(String id);

    @XBRead("//*[local-name()='div'][@class='ocr_carea'][@id='{0}']/descendant::node()/@id")
    List<String> getCAreaIdDescIds(String id);

    @XBRead("//*[@id='{0}']/@title")
    List<String> getTitleForId(String id);

    @XBRead("//*[local-name()='span'][@class='ocr_line']/@id")
    List<String> getLineNodeId();

    @XBRead("//*[local-name()='span'][@class='ocrx_word']/@id")
    List<String> getWordNodeId();
}
