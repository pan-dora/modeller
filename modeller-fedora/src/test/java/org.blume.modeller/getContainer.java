package org.blume.modeller;

import org.apache.jena.rdf.model.*;
import org.blume.modeller.ui.util.ApplicationContextUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class getContainer {
    public static void main(String[] args) throws IOException {
        ModellerClient client = new ModellerClient();
        try {
            String resource = client.doGetContainerResources("http://localhost:8080/fcrepo/rest/collection/test/001/res");
            final Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(resource.getBytes()), null, "TTL");
            ArrayList<String> children = getChilden(model);
            model.write(System.out, "TTL");
            System.out.println(children);
        } catch (ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
    }

    private static ArrayList<String> getChilden(Model model) {
        String NS = "http://www.w3.org/ns/ldp#";
        Property ldpcontains = model.getProperty(NS + "contains");
        ArrayList<String> retval = new ArrayList<>();
        StmtIterator it = model.listStatements(new SimpleSelector(null, ldpcontains,(Resource)null));
        while (it.hasNext()) {
            Statement st = it.next();
            retval.add(st.getObject().toString());
        }
        return retval;
    }

}
