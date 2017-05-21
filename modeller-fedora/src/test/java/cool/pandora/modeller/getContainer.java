package cool.pandora.modeller;

import org.apache.jena.rdf.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class getContainer {
    public static void main(String[] args) throws IOException {
        try {
            String resource = ModellerClient
                    .doGetContainerResources(URI.create("http://localhost:8080/fcrepo/rest/collection/test/001/res"));
            final Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(resource.getBytes()), null, "TTL");
            ArrayList<String> children = getChilden(model);
            model.write(System.out, "TTL");
            System.out.println(children);
        } catch (ModellerClientFailedException e) {
            System.out.println(getMessage(e));
        }
    }

    private static ArrayList<String> getChilden(Model model) {
        String NS = "http://www.w3.org/ns/ldp#";
        Property ldpcontains = model.getProperty(NS + "contains");
        ArrayList<String> retval = new ArrayList<>();
        StmtIterator it = model.listStatements(new SimpleSelector(null, ldpcontains, (Resource) null));
        while (it.hasNext()) {
            Statement st = it.next();
            retval.add(st.getObject().toString());
        }
        return retval;
    }

}
