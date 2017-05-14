package cool.pandora.modeller;

import java.io.IOException;

public class docManifestBuilderTest {

    public static void main(String[] args) throws IOException {
        String url = "resource://data/001.hocr";
        hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
        String resourceURI = "http://localhost:8080/fcrepo/rest/collection/test/004/area/";
        String rdfseq = DocManifestBuilder.getAreaRDFSequenceForhOCRResource(hocr, resourceURI);
        System.out.println(rdfseq);
        //Map map = getAreaMapForhOCRResource(hocr);
        //System.out.println(map);
    }
}
