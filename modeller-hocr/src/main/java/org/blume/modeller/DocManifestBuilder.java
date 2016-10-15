package org.blume.modeller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.blume.modeller.util.RDFCollectionWriter;
import org.xmlbeam.XBProjector;

public class DocManifestBuilder {

    public DocManifestBuilder() {
    }

    public static hOCRData gethOCRProjectionFromURL(String url) throws IOException {
        return new XBProjector(new OmitDTDXMLFactoriesConfig()).io().url(url).read(hOCRData.class);
    }

    private static Map<String, Object> buildValueMap(List<String> descList, hOCRData hocr) {
        Map<String, Object> valueMap = new HashMap<>();
        for (String descId : descList) {
            Object oNode = hocr.getTitleForId(descId);
            valueMap.put(descId, oNode);
        }
        return valueMap;
    }

    public static Map getAreaMapForhOCRResource(hOCRData hocr) {
        List<String> cAreaIdList = hocr.getCAreaNodeId();
        Map<String, Object> areaMap = new HashMap<>();
        for (String cAreaId : cAreaIdList) {
            List<String> descList = hocr.getCAreaIdDescIds(cAreaId);
            areaMap.put(cAreaId, buildValueMap(descList, hocr));
        }
        return areaMap;
    }

    public static String getAreaRDFSequenceForhOCRResource(hOCRData hocr, String resourceURI) throws IOException {
        List<String> cAreaIdList = hocr.getCAreaNodeId();
        RDFCollectionWriter collectionWriter;
        String collectionPredicate = "http://iiif.io/api/text#hasAreas";
        collectionWriter = RDFCollectionWriter.collection()
                .idList(cAreaIdList)
                .collectionPredicate(collectionPredicate)
                .resourceContainerIRI(resourceURI)
                .build();
        return collectionWriter.render();
    }

    public static List<String> getPageIdList(hOCRData hocr) {
        return hocr.getPageNodeId();
    }

    public static List<String> getAreaIdList(hOCRData hocr) {
        return hocr.getCAreaNodeId();
    }

    public static List<String> getLineIdList(hOCRData hocr) {
        return hocr.getLineNodeId();
    }

    public static List<String> getWordIdList(hOCRData hocr) {
        return hocr.getWordNodeId();
    }

    public static List<String> getAreaIdListforPage(hOCRData hocr, String id) {
        return hocr.getCAreasforPage(id);
    }

    public static List<String> getLineIdListforArea(hOCRData hocr, String id) { return hocr.getLinesforArea(id); }

    public static String getBboxForId(hOCRData hocr, String id) {
        return StringUtils.substringBefore(StringUtils.substringAfter(hocr.getTitleForId(id), "bbox "), ";");
    }

    public static String getCharsForId(hOCRData hocr, String id) {
        return hocr.getCharsForId(id);
    }

    public static List<String> getWordIdListforLine(hOCRData hocr, String id) {
        return hocr.getWordsforLine(id);
    }

    public static List<String> getWordIdListforPage(hOCRData hocr, String id) {
        return hocr.getWordsforPage(id);
    }

    private static ByteArrayOutputStream marshal(File hocr) throws JAXBException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JAXBContext jaxbContext = JAXBContext.newInstance(File.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(hocr, out);
        return out;
    }
}