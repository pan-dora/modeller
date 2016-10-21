package org.blume.modeller.util;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.ByteArrayOutputStream;

public class ServiceNodeWriter {
    public static ServiceNodeBuilder init() {
        return new ServiceNodeBuilder();
    }

    private ByteArrayOutputStream serviceNode;

    public String render() {
        return this.serviceNode.toString();
    }

    protected ServiceNodeWriter(final String serviceURI, final String servicePredicate, final String serviceType) {
        Model model = ModelFactory.createDefaultModel();
        Resource s = model.createResource(getIdentitySubject());
        Property p = model.createProperty(servicePredicate);
        Resource o = model.createResource(serviceURI);
        model.add(s, p, o);

        Dataset dataset = DatasetFactory.create(model);
        dataset.addNamedModel("http://iiif.service", model);
        this.serviceNode = new ByteArrayOutputStream();
        RDFDataMgr.write(this.serviceNode, model, Lang.NTRIPLES);
    }

    public static class ServiceNodeBuilder {
        private String servicePredicate;
        private String serviceURI;
        private String serviceType;

        public ServiceNodeWriter.ServiceNodeBuilder serviceURI(String serviceURI) {
            this.serviceURI = serviceURI;
            return this;
        }

        public ServiceNodeWriter.ServiceNodeBuilder servicePredicate(String servicePredicate) {
            this.servicePredicate = servicePredicate;
            return this;
        }

        public ServiceNodeWriter.ServiceNodeBuilder serviceType(String serviceType) {
            this.serviceType = serviceType;
            return this;
        }

        public ServiceNodeWriter build() {
            return new ServiceNodeWriter(this.serviceURI, this.servicePredicate, this.serviceType);
        }

    }

    private static String getIdentitySubject() {
        return "";
    }
}
