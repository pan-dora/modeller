package cool.pandora.modeller;


import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;

public class xmlWriterTest {
    public static void main(String[] args) throws JAXBException {
        ByteArrayOutputStream out =
                XmlFileWriter.write().collectionId("test10").objektId("001").resourceId("004").build();
        System.out.println(out);
    }
}


