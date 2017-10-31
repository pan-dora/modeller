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

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;


/**
 * ResourceObjectNode.
 *
 * @author Christopher Johnson
 */
public class ResourceObjectNode {
    private ArrayList<RDFNode> resourceValue;

    /**
     * ResourceObjectNode.
     *
     * @param resourceURI      String
     * @param resourceProperty String
     */
    ResourceObjectNode(final String resourceURI, final String resourceProperty) {

        try {
            final String resource = ModellerClient.doGetContainerResources(new URI(resourceURI));
            final Model model = ModelFactory.createDefaultModel();
            model.read(
                    new ByteArrayInputStream(resource != null ? resource.getBytes() : new byte[0]),
                    null, "TTL");
            this.resourceValue = getValue(model, resourceProperty);
        } catch (final ModellerClientFailedException e) {
            System.out.println(getMessage(e));
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * init.
     *
     * @return ResourceValueBuilder
     */
    public static ResourceObjectNode.ResourceValueBuilder init() {
        return new ResourceObjectNode.ResourceValueBuilder();
    }

    /**
     * getValue.
     *
     * @param model            Model
     * @param resourceProperty String
     * @return retval
     */
    private static ArrayList<RDFNode> getValue(final Model model, final String resourceProperty) {
        final Property property = model.getProperty(resourceProperty);
        final ArrayList<RDFNode> retval = new ArrayList<>();
        final StmtIterator it =
                model.listStatements(new SimpleSelector(null, property, (Resource) null));
        while (it.hasNext()) {
            final Statement st = it.next();
            retval.add(st.getObject());
        }
        return retval;
    }

    /**
     * render.
     *
     * @return resourceValue
     */
    public ArrayList<RDFNode> render() {
        return this.resourceValue;
    }

    /**
     * ResourceValueBuilder.
     */
    public static class ResourceValueBuilder {

        private String resourceURI;
        private String resourceProperty;

        /**
         * resourceURI.
         *
         * @param resourceURI String
         * @return this
         */
        public ResourceObjectNode.ResourceValueBuilder resourceURI(final String resourceURI) {
            this.resourceURI = resourceURI;
            return this;
        }

        /**
         * resourceProperty.
         *
         * @param resourceProperty String
         * @return this
         */
        public ResourceObjectNode.ResourceValueBuilder resourceProperty(
                final String resourceProperty) {
            this.resourceProperty = resourceProperty;
            return this;
        }

        /**
         * build.
         *
         * @return ResourceObjectNode
         */
        public ResourceObjectNode build() {
            return new ResourceObjectNode(this.resourceURI, this.resourceProperty);
        }
    }

}


