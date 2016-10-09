package org.blume.modeller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.blume.modeller.DocManifestBuilder.getPageIdList;

public class canvasPageMapTest {

    public static void main(String[] args) throws IOException {
        List<String> pageIdList;
        Map<String, String> canvasPageMap;
        String url = "resource://data/test_007.hocr";
        hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
        pageIdList = getPageIdList(hocr);
        canvasPageMap = CanvasPageMap.init()
                .canvasContainerURI(URI.create("http://localhost:8080/fcrepo/rest/collection/test/007/canvas"))
                .pageIdList(pageIdList)
                .build();
        System.out.println(canvasPageMap);
    }
}
