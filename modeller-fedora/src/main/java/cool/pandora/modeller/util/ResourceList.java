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
import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;



/**
 * ResourceList.
 *
 * @author Christopher Johnson
 */
public class ResourceList {
    private final URI resourceContainerURI;

    /**
     * ResourceList.
     *
     * @param resourceContainerURI URI
     */
    public ResourceList(final URI resourceContainerURI) {
        this.resourceContainerURI = resourceContainerURI;
    }

    /**
     * getResourceList.
     *
     * @return resourceList
     */
    public ArrayList<String> getResourceList() {
        try {
            final String resource = ModellerClient.doGetContainerResources(this
                    .resourceContainerURI);
            final Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(resource != null ? resource.getBytes() : new
                    byte[0]), null, "TTL");
            final ArrayList<String> resourceList = getChilden(model);
            resourceList.sort(String.CASE_INSENSITIVE_ORDER);
            return resourceList;
        } catch (final ModellerClientFailedException e) {
            System.out.println(getMessage(e));
        }
        return null;
    }

    /**
     * getChilden.
     *
     * @param model Model
     * @return retval
     */
    private static ArrayList<String> getChilden(final Model model) {
        final String NS = "http://www.w3.org/ns/ldp#";
        final Property ldpcontains = model.getProperty(NS + "contains");
        final ArrayList<String> retval = new ArrayList<>();
        final StmtIterator it = model.listStatements(new SimpleSelector(null, ldpcontains,
                (Resource) null));
        while (it.hasNext()) {
            final Statement st = it.next();
            retval.add(st.getObject().toString());
        }
        return retval;
    }

}

