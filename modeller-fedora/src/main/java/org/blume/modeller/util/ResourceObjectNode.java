package org.blume.modeller.util;

import org.apache.jena.rdf.model.*;
import org.blume.modeller.ModellerClient;
import org.blume.modeller.ModellerClientFailedException;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class ResourceObjectNode {

    public static ResourceObjectNode.ResourceValueBuilder init() {
        return new ResourceObjectNode.ResourceValueBuilder();
    }

    private ArrayList<RDFNode> resourceValue;

    public ArrayList<RDFNode> render() {
        return this.resourceValue;
    }

    protected ResourceObjectNode(final String resourceURI, final String resourceProperty) {

        ModellerClient client = new ModellerClient();
        try {
            String resource = client.doGetContainerResources(new URI(resourceURI));
            final Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(resource.getBytes()), null, "TTL");
            this.resourceValue = getValue(model, resourceProperty);
        } catch (ModellerClientFailedException e) {
            System.out.println(getMessage(e));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<RDFNode> getValue(Model model, String resourceProperty) {
        Property property = model.getProperty(resourceProperty);
        ArrayList<RDFNode> retval = new ArrayList<>();
        StmtIterator it = model.listStatements(new SimpleSelector(null, property,(Resource)null));
        while (it.hasNext()) {
            Statement st = it.next();
            retval.add(st.getObject());
        }
        return retval;
    }

    public static class ResourceValueBuilder {

        private String resourceURI;
        private String resourceProperty;


        public ResourceObjectNode.ResourceValueBuilder resourceURI(String resourceURI) {
            this.resourceURI = resourceURI;
            return this;
        }

        public ResourceObjectNode.ResourceValueBuilder resourceProperty(String resourceProperty) {
            this.resourceProperty = resourceProperty;
            return this;
        }

        public ResourceObjectNode build() {
            return new ResourceObjectNode(this.resourceURI, this.resourceProperty);
        }
    }

}


