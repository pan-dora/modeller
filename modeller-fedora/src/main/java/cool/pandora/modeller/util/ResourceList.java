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
import java.util.ArrayList;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * ResourceList
 *
 * @author Christopher Johnson
 */
public class ResourceList {
    private final URI resourceContainerURI;

    /**
     * @param resourceContainerURI URI
     */
    public ResourceList(final URI resourceContainerURI) {
        this.resourceContainerURI = resourceContainerURI;
    }

    /**
     * @return resourceList
     */
    public ArrayList<String> getResourceList() {
        try {
            final String resource = ModellerClient.doGetContainerResources(this.resourceContainerURI);
            final Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(resource != null ? resource.getBytes() : new byte[0]), null, "TTL");
            final ArrayList<String> resourceList = getChilden(model);
            resourceList.sort(String.CASE_INSENSITIVE_ORDER);
            return resourceList;
        } catch (final ModellerClientFailedException e) {
            System.out.println(getMessage(e));
        }
        return null;
    }

    /**
     * @param model Model
     * @return retval
     */
    private static ArrayList<String> getChilden(final Model model) {
        final String NS = "http://www.w3.org/ns/ldp#";
        final Property ldpcontains = model.getProperty(NS + "contains");
        final ArrayList<String> retval = new ArrayList<>();
        final StmtIterator it = model.listStatements(new SimpleSelector(null, ldpcontains, (Resource) null));
        while (it.hasNext()) {
            final Statement st = it.next();
            retval.add(st.getObject().toString());
        }
        return retval;
    }

}

