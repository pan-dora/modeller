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
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * ResourceIntegerValue
 *
 * @author Christopher Johnson
 */
public class ResourceIntegerValue {
    /**
     * @return ResourceValueBuilder
     */
    public static ResourceIntegerValue.ResourceValueBuilder init() {
        return new ResourceIntegerValue.ResourceValueBuilder();
    }

    private List<Integer> resourceValue;

    /**
     * @return resourceValue
     */
    public List<Integer> render() {
        return this.resourceValue;
    }

    /**
     * @param resourceURI      String
     * @param resourceProperty String
     */
    ResourceIntegerValue(final String resourceURI, final String resourceProperty) {

        try {
            final String resource = ModellerClient.doGetContainerResources(new URI(resourceURI));
            final Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(resource != null ? resource.getBytes() : new byte[0]), null, "TTL");
            this.resourceValue = getValue(model, resourceProperty);
        } catch (ModellerClientFailedException e) {
            System.out.println(getMessage(e));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param model            Model
     * @param resourceProperty String
     * @return retval
     */
    private static ArrayList<Integer> getValue(final Model model, final String resourceProperty) {
        final Property property = model.getProperty(resourceProperty);
        final ArrayList<Integer> retval = new ArrayList<>();
        final StmtIterator it = model.listStatements(new SimpleSelector(null, property, (Resource) null));
        while (it.hasNext()) {
            final Statement st = it.next();
            retval.add(st.getInt());
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
        public ResourceIntegerValue.ResourceValueBuilder resourceURI(final String resourceURI) {
            this.resourceURI = resourceURI;
            return this;
        }

        /**
         * @param resourceProperty String
         * @return this
         */
        public ResourceIntegerValue.ResourceValueBuilder resourceProperty(final String resourceProperty) {
            this.resourceProperty = resourceProperty;
            return this;
        }

        /**
         * @return ResourceIntegerValue
         */
        public ResourceIntegerValue build() {
            return new ResourceIntegerValue(this.resourceURI, this.resourceProperty);
        }
    }

}


