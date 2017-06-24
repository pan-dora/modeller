/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cool.pandora.modeller.util;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.ByteArrayOutputStream;

/**
 * ServiceNodeWriter
 *
 * @author Christopher Johnson
 */
public class ServiceNodeWriter {
    /**
     * @return ServiceNodeBuilder
     */
    public static ServiceNodeBuilder init() {
        return new ServiceNodeBuilder();
    }

    private final ByteArrayOutputStream serviceNode;

    /**
     * @return serviceNode
     */
    public String render() {
        return this.serviceNode.toString();
    }

    /**
     * @param serviceURI       String
     * @param servicePredicate String
     * @param serviceType      String
     */
    ServiceNodeWriter(final String serviceURI, final String servicePredicate, final String serviceType) {
        final Model model = ModelFactory.createDefaultModel();
        final Resource s = model.createResource(getIdentitySubject());
        final Property p = model.createProperty(servicePredicate);
        final Resource o = model.createResource(serviceURI);
        model.add(s, p, o);

        final Dataset dataset = DatasetFactory.create(model);
        dataset.addNamedModel("http://iiif.service", model);
        this.serviceNode = new ByteArrayOutputStream();
        RDFDataMgr.write(this.serviceNode, model, Lang.NTRIPLES);
    }

    /**
     *
     */
    public static class ServiceNodeBuilder {
        private String servicePredicate;
        private String serviceURI;
        private String serviceType;

        /**
         * @param serviceURI String
         * @return this
         */
        public ServiceNodeWriter.ServiceNodeBuilder serviceURI(final String serviceURI) {
            this.serviceURI = serviceURI;
            return this;
        }

        /**
         * @param servicePredicate String
         * @return this
         */
        public ServiceNodeWriter.ServiceNodeBuilder servicePredicate(final String servicePredicate) {
            this.servicePredicate = servicePredicate;
            return this;
        }

        /**
         * @param serviceType String
         * @return this
         */
        public ServiceNodeWriter.ServiceNodeBuilder serviceType(final String serviceType) {
            this.serviceType = serviceType;
            return this;
        }

        /**
         * @return ServiceNodeWriter
         */
        public ServiceNodeWriter build() {
            return new ServiceNodeWriter(this.serviceURI, this.servicePredicate, this.serviceType);
        }

    }

    /**
     * @return identity
     */
    private static String getIdentitySubject() {
        return "";
    }
}
