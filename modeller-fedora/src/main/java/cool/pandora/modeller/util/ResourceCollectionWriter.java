package cool.pandora.modeller.util;

import cool.pandora.modeller.common.uri.IIIFPredicates;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.BlankNodeAllocator;
import org.apache.jena.riot.lang.BlankNodeAllocatorHash;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.jena.graph.NodeFactory.createBlankNode;
import static org.apache.jena.riot.writer.WriterConst.RDF_First;
import static org.apache.jena.riot.writer.WriterConst.RDF_Rest;
import static org.apache.jena.riot.writer.WriterConst.RDF_Nil;

/**
 * ResourceCollectionWriter
 *
 * @author Christopher Johnson
 */
public class ResourceCollectionWriter {
    /**
     * @return ResourceCollectionBuilder
     */
    public static ResourceCollectionBuilder collection() {
        return new ResourceCollectionBuilder();
    }

    private final ByteArrayOutputStream rdfCollection;

    /**
     * @return rdfCollection
     */
    public String render() {
        return this.rdfCollection.toString();
    }

    /**
     * @param idList              List
     * @param collectionPredicate String
     * @param resourceTargetMap   Map
     */
    ResourceCollectionWriter(final List<String> idList, final String collectionPredicate,
                             final Map<String, String> resourceTargetMap) {

        final Model model = ModelFactory.createDefaultModel();
        final Map<String, Node> bNodeMap = getBNodeKeyMap(idList);

        final Resource s = model.createResource(getIdentitySubject());
        final Property p = model.createProperty(collectionPredicate);
        final Node firstBNode = getSubjNodeForCurrentIndex(0, bNodeMap);
        final Resource o = model.createResource(String.valueOf(firstBNode));
        model.add(s, p, o);

        for (final String id : idList) {
            final int pos = getIDPos(idList, id);
            final String lastId = idList.get(idList.size() - 1);
            final String canvasURI = resourceTargetMap.get(id);
            //singleton list
            if (pos == 0 && (Objects.equals(id, lastId))) {
                final Node subjNode = getSubjNodeForCurrentIndex(pos, bNodeMap);

                final Resource s0 = model.createResource(String.valueOf(subjNode));
                final Property p0 = model.createProperty(String.valueOf(RDF_First));
                final Resource o0 = model.createResource();
                model.add(s0, p0, o0);

                final Property p1 = model.createProperty(IIIFPredicates.ON);
                final Resource o1 = model.createResource(canvasURI);
                model.add(o0, p1, o1);

                final Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                final Resource o2 = model.createResource(String.valueOf(RDF_Nil));
                model.add(s0, p2, o2);

                final Property p3 = model.createProperty(IIIFPredicates.HAS_BODY);
                final Resource o3 = model.createResource(id);
                model.add(o0, p3, o3);
            } else if (pos == 0) {
                final Node subjNode = getSubjNodeForCurrentIndex(pos, bNodeMap);
                final Node objNode = getObjNodeForCurrentIndex(pos, bNodeMap);

                final Resource s0 = model.createResource(String.valueOf(subjNode));
                final Property p0 = model.createProperty(String.valueOf(RDF_First));
                final Resource o0 = model.createResource();
                model.add(s0, p0, o0);

                final Property p1 = model.createProperty(IIIFPredicates.ON);
                final Resource o1 = model.createResource(canvasURI);
                model.add(o0, p1, o1);

                final Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                final Resource o2 = model.createResource(String.valueOf(objNode));
                model.add(s0, p2, o2);

                final Property p3 = model.createProperty(IIIFPredicates.HAS_BODY);
                final Resource o3 = model.createResource(id);
                model.add(o0, p3, o3);
            } else if (Objects.equals(id, lastId)) {
                final Node subjNode = getObjNodeFromPrevIndex(pos, bNodeMap);

                final Resource s0 = model.createResource(String.valueOf(subjNode));
                final Property p0 = model.createProperty(String.valueOf(RDF_First));
                final Resource o0 = model.createResource();
                model.add(s0, p0, o0);

                final Property p1 = model.createProperty(IIIFPredicates.ON);
                final Resource o1 = model.createResource(canvasURI);
                model.add(o0, p1, o1);

                final Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                final Resource o2 = model.createResource(String.valueOf(RDF_Nil));
                model.add(s0, p2, o2);

                final Property p3 = model.createProperty(IIIFPredicates.HAS_BODY);
                final Resource o3 = model.createResource(id);
                model.add(o0, p3, o3);
            } else {
                final Node subjNode = getObjNodeFromPrevIndex(pos, bNodeMap);
                final Node objNode = getObjNodeForCurrentIndex(pos, bNodeMap);

                final Resource s0 = model.createResource(String.valueOf(subjNode));
                final Property p0 = model.createProperty(String.valueOf(RDF_First));
                final Resource o0 = model.createResource();
                model.add(s0, p0, o0);

                final Property p1 = model.createProperty(IIIFPredicates.ON);
                final Resource o1 = model.createResource(canvasURI);
                model.add(o0, p1, o1);

                final Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                final Resource o2 = model.createResource(String.valueOf(objNode));
                model.add(s0, p2, o2);

                final Property p3 = model.createProperty(IIIFPredicates.HAS_BODY);
                final Resource o3 = model.createResource(id);
                model.add(o0, p3, o3);
            }
        }

        final Dataset dataset = DatasetFactory.create(model);
        dataset.addNamedModel("http://iiif.sequence", model);
        this.rdfCollection = new ByteArrayOutputStream();
        RDFDataMgr.write(this.rdfCollection, model, Lang.NTRIPLES);
    }

    /**
     * @param idList List
     * @param id     String
     * @return id position
     */
    private static int getIDPos(final List<String> idList, final String id) {
        return idList.indexOf(id);
    }

    /**
     * @param idList List
     * @return bNodeMap
     */
    private static Map<String, Node> getBNodeKeyMap(final List<String> idList) {
        final Map<String, Node> bNodeMap = new HashMap<>();

        for (final String id : idList) {
            final int pos = getIDPos(idList, id);
            final Node sNode = getNewBNode();
            final Node oNode = getNewBNode();
            final String subjKey = String.valueOf(pos) + ":subj";
            bNodeMap.put(subjKey, sNode);
            final String objKey = String.valueOf(pos) + ":obj";
            bNodeMap.put(objKey, oNode);
        }
        return bNodeMap;
    }

    /**
     * @param pos      int
     * @param bNodeMap bNodeMap
     * @return object Node
     */
    private static Node getObjNodeFromPrevIndex(final int pos, final Map<String, Node> bNodeMap) {
        final int prevIndex = pos - 1;
        final String objKey = String.valueOf(prevIndex) + ":obj";
        return bNodeMap.get(objKey);
    }

    /**
     * @param pos      int
     * @param bNodeMap Map
     * @return subject Node
     */
    private static Node getSubjNodeForCurrentIndex(final int pos, final Map<String, Node> bNodeMap) {
        final String objKey = String.valueOf(pos) + ":subj";
        return bNodeMap.get(objKey);
    }

    /**
     * @param pos      int
     * @param bNodeMap Map
     * @return object Node
     */
    private static Node getObjNodeForCurrentIndex(final int pos, final Map<String, Node> bNodeMap) {
        final String objKey = String.valueOf(pos) + ":obj";
        return bNodeMap.get(objKey);
    }

    /**
     * @return identity
     */
    private static String getIdentitySubject() {
        return "";
    }

    /**
     * @return bNode Label
     */
    private static Node getNewBNode() {
        final String bNodeLabelStart = "_:";
        final ResourceCollectionBuilder.Factory fSeededHashAlloc = BlankNodeAllocatorHash::new;
        final BlankNodeAllocator alloc = fSeededHashAlloc.create();
        final Node n = alloc.create();
        final String bnodeLabel = bNodeLabelStart + String.valueOf(n);
        return createBlankNode(bnodeLabel);
    }

    public static class ResourceCollectionBuilder {

        private List<String> idList;
        private String collectionPredicate;
        private Map<String, String> resourceTargetMap;

        /**
         * @param idList List
         * @return this
         */
        public ResourceCollectionWriter.ResourceCollectionBuilder idList(final List<String> idList) {
            this.idList = idList;
            return this;
        }

        /**
         * @param collectionPredicate String
         * @return this
         */
        public ResourceCollectionWriter.ResourceCollectionBuilder collectionPredicate(
                final String collectionPredicate) {
            this.collectionPredicate = collectionPredicate;
            return this;
        }

        /**
         * @param resourceTargetMap Map
         * @return this
         */
        public ResourceCollectionWriter.ResourceCollectionBuilder resourceTargetMap(
                final Map<String, String> resourceTargetMap) {
            this.resourceTargetMap = resourceTargetMap;
            return this;
        }

        /**
         * @return ResourceCollectionWriter
         */
        public ResourceCollectionWriter build() {
            return new ResourceCollectionWriter(this.idList, this.collectionPredicate, this.resourceTargetMap);
        }

        public interface Factory {
            /**
             * @return blank node
             */
            BlankNodeAllocator create();
        }
    }

}
