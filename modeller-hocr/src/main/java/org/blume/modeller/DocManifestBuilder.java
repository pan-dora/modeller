package org.blume.modeller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xmlbeam.XBProjector;

public class DocManifestBuilder {

    public static void main(String[] args) throws IOException {
        String url = "resource://data/001.hocr";
        hOCRData hocr = gethOCRProjectionFromURL(url);
        String pageid = hocr.getPageNodeId();
        List<String> careaid = hocr.getCAreaNodeId();
        List<String> lineid = hocr.getLineNodeId();
        List<String> wordid = hocr.getWordNodeId();
        System.out.println(pageid);
        System.out.println(careaid);
        System.out.println(lineid);
        System.out.println(wordid);
    }

    private static hOCRData gethOCRProjectionFromURL(String url) throws IOException {
        return new XBProjector().io().url(url).read(hOCRData.class);
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