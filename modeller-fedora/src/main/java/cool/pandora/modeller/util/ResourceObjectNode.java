package cool.pandora.modeller.util;

import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.RDFNode;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * ResourceObjectNode
 *
 * @author Christopher Johnson
 */
public class ResourceObjectNode {
    /**
     * @return ResourceValueBuilder
     */
    public static ResourceObjectNode.ResourceValueBuilder init() {
        return new ResourceObjectNode.ResourceValueBuilder();
    }

    private ArrayList<RDFNode> resourceValue;

    /**
     * @return resourceValue
     */
    public ArrayList<RDFNode> render() {
        return this.resourceValue;
    }

    /**
     * @param resourceURI      String
     * @param resourceProperty String
     */
    ResourceObjectNode(final String resourceURI, final String resourceProperty) {

        try {
            final String resource = ModellerClient.doGetContainerResources(new URI(resourceURI));
            final Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(resource != null ? resource.getBytes() : new byte[0]), null, "TTL");
            this.resourceValue = getValue(model, resourceProperty);
        } catch (final ModellerClientFailedException e) {
            System.out.println(getMessage(e));
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param model            Model
     * @param resourceProperty String
     * @return retval
     */
    private static ArrayList<RDFNode> getValue(final Model model, final String resourceProperty) {
        final Property property = model.getProperty(resourceProperty);
        final ArrayList<RDFNode> retval = new ArrayList<>();
        final StmtIterator it = model.listStatements(new SimpleSelector(null, property, (Resource) null));
        while (it.hasNext()) {
            final Statement st = it.next();
            retval.add(st.getObject());
        }
        return retval;
    }

    /**
     *
     */
    public static class ResourceValueBuilder {

        private String resourceURI;
        private String resourceProperty;

        /**
         * @param resourceURI String
         * @return this
         */
        public ResourceObjectNode.ResourceValueBuilder resourceURI(final String resourceURI) {
            this.resourceURI = resourceURI;
            return this;
        }

        /**
         * @param resourceProperty String
         * @return this
         */
        public ResourceObjectNode.ResourceValueBuilder resourceProperty(final String resourceProperty) {
            this.resourceProperty = resourceProperty;
            return this;
        }

        /**
         * @return ResourceObjectNode
         */
        public ResourceObjectNode build() {
            return new ResourceObjectNode(this.resourceURI, this.resourceProperty);
        }
    }

}


