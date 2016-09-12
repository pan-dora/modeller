package org.blume.modeller.ui.handlers.iiif;

import org.apache.jena.rdf.model.*;
import org.blume.modeller.ModellerClient;
import org.blume.modeller.ModellerClientFailedException;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

class ResourceList {
    private URI resourceContainerURI;
    ResourceList(URI resourceContainerURI) {
        this.resourceContainerURI = resourceContainerURI;
    }

    ArrayList<String> getResourceList() {
        ModellerClient client = new ModellerClient();
        try {
            String resource = client.doGetContainerResources(this.resourceContainerURI);
            final Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(resource.getBytes()), null, "TTL");
            ArrayList<String> resourceList = getChilden(model);
            model.write(System.out, "TTL");
            System.out.println(resourceList);
            Collections.sort(resourceList, String.CASE_INSENSITIVE_ORDER);
            return resourceList;
        } catch (ModellerClientFailedException e) {
            System.out.println(getMessage(e));
        }
        return null;
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

