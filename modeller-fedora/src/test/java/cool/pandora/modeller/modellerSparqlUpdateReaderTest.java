package cool.pandora.modeller;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class modellerSparqlUpdateReaderTest {
    public static void main(String[] args) {
        //Model model = RDFDataMgr.loadModel("data/res.n3") ;

        InputStream requestBodyStream = modellerSparqlUpdateReaderTest.class.getResourceAsStream("/data/res.update");

        try {
            final String requestBody = IOUtils.toString(requestBodyStream, UTF_8);
            System.out.println();
            System.out.println("#### ---- Write as application/sparql-update");
            System.out.println(requestBody);
        } catch (final IOException ex) {
        }
    }

}