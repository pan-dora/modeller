package org.blume.modeller;

import java.io.IOException;

public class getBboxTest {

    public static void main(String[] args) throws IOException {
        String url = "resource://data/test_007.hocr";
        hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);

        String bbox = DocManifestBuilder.getBboxForId(hocr, "block_1_4");
        String region = Region.region()
                .bbox(bbox)
                .build();
        System.out.println(region);
    }
}
