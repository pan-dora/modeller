package org.blume.modeller.util;

import org.apache.jena.rdf.model.*;
import org.blume.modeller.ModellerClient;
import org.blume.modeller.ModellerClientFailedException;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class ResourceIntegerValue {

    public static ResourceIntegerValue.ResourceValueBuilder init() {
        return new ResourceIntegerValue.ResourceValueBuilder();
    }

    private List<Integer> resourceValue;

    public List<Integer> render() {
        return this.resourceValue;
    }

    protected ResourceIntegerValue(final String resourceURI, final String resourceProperty) {

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

    private ArrayList<Integer> getValue(Model model, String resourceProperty) {
        Property property = model.getProperty(resourceProperty);
        ArrayList<Integer> retval = new ArrayList<>();
        StmtIterator it = model.listStatements(new SimpleSelector(null, property,(Resource)null));
        while (it.hasNext()) {
            Statement st = it.next();
            retval.add(st.getInt());
        }
        return retval;
    }

    public static class ResourceValueBuilder {

        private String resourceURI;
        private String resourceProperty;


        public ResourceIntegerValue.ResourceValueBuilder resourceURI(String resourceURI) {
            this.resourceURI = resourceURI;
            return this;
        }

        public ResourceIntegerValue.ResourceValueBuilder resourceProperty(String resourceProperty) {
            this.resourceProperty = resourceProperty;
            return this;
        }

        public ResourceIntegerValue build() {
            return new ResourceIntegerValue(this.resourceURI, this.resourceProperty);
        }
    }

}


