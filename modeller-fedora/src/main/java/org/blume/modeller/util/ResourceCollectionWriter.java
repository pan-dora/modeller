package org.blume.modeller.util;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.BlankNodeAllocator;
import org.apache.jena.riot.lang.BlankNodeAllocatorHash;
import org.blume.modeller.common.uri.IIIFPredicates;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.jena.graph.NodeFactory.createBlankNode;
import static org.apache.jena.riot.writer.WriterConst.*;

public class ResourceCollectionWriter {

    public static ResourceCollectionBuilder collection() {
        return new ResourceCollectionBuilder();
    }

    private ByteArrayOutputStream rdfCollection;

    public String render() {
        return this.rdfCollection.toString();
    }

    protected ResourceCollectionWriter(final List<String> idList, final String collectionPredicate,
                                       final Map<String, String> resourceTargetMap) {

        Model model = ModelFactory.createDefaultModel();
        Map<String, Node> bNodeMap = getBNodeKeyMap(idList);

        Resource s = model.createResource(getIdentitySubject());
        Property p = model.createProperty(collectionPredicate);
        Node firstBNode = getSubjNodeForCurrentIndex(0, bNodeMap);
        Resource o = model.createResource(String.valueOf(firstBNode));
        model.add(s, p, o);

        for (String id : idList) {
            int pos = getIDPos(idList, id);
            String lastId = idList.get(idList.size() - 1);
            String canvasURI = resourceTargetMap.get(id);
            //singleton list
            if (pos == 0  && (Objects.equals(id, lastId))) {
                Node subjNode = getSubjNodeForCurrentIndex(pos, bNodeMap);

                Resource s0 = model.createResource(String.valueOf(subjNode));
                Property p0 = model.createProperty(String.valueOf(RDF_First));
                Resource o0 = model.createResource();
                model.add(s0, p0, o0);

                Property p1 = model.createProperty(IIIFPredicates.ON);
                Resource o1 = model.createResource(canvasURI);
                model.add(o0, p1, o1);

                Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                Resource o2 = model.createResource(String.valueOf(RDF_Nil));
                model.add(s0, p2, o2);

                Property p3 = model.createProperty(IIIFPredicates.HAS_BODY);
                Resource o3 = model.createResource(id);
                model.add(o0, p3, o3);
            } else  if (pos == 0) {
                Node subjNode = getSubjNodeForCurrentIndex(pos, bNodeMap);
                Node objNode = getObjNodeForCurrentIndex(pos, bNodeMap);

                Resource s0 = model.createResource(String.valueOf(subjNode));
                Property p0 = model.createProperty(String.valueOf(RDF_First));
                Resource o0 = model.createResource();
                model.add(s0, p0, o0);

                Property p1 = model.createProperty(IIIFPredicates.ON);
                Resource o1 = model.createResource(canvasURI);
                model.add(o0, p1, o1);

                Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                Resource o2 = model.createResource(String.valueOf(objNode));
                model.add(s0, p2, o2);

                Property p3 = model.createProperty(IIIFPredicates.HAS_BODY);
                Resource o3 = model.createResource(id);
                model.add(o0, p3, o3);
            } else if (Objects.equals(id, lastId)) {
                Node subjNode = getObjNodeFromPrevIndex(pos, bNodeMap);

                Resource s0 = model.createResource(String.valueOf(subjNode));
                Property p0 = model.createProperty(String.valueOf(RDF_First));
                Resource o0 = model.createResource();
                model.add(s0, p0, o0);

                Property p1 = model.createProperty(IIIFPredicates.ON);
                Resource o1 = model.createResource(canvasURI);
                model.add(o0, p1, o1);

                Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                Resource o2 = model.createResource(String.valueOf(RDF_Nil));
                model.add(s0, p2, o2);

                Property p3 = model.createProperty(IIIFPredicates.HAS_BODY);
                Resource o3 = model.createResource(id);
                model.add(o0, p3, o3);
            } else {
                Node subjNode = getObjNodeFromPrevIndex(pos, bNodeMap);
                Node objNode = getObjNodeForCurrentIndex(pos, bNodeMap);

                Resource s0 = model.createResource(String.valueOf(subjNode));
                Property p0 = model.createProperty(String.valueOf(RDF_First));
                Resource o0 = model.createResource();
                model.add(s0, p0, o0);

                Property p1 = model.createProperty(IIIFPredicates.ON);
                Resource o1 = model.createResource(canvasURI);
                model.add(o0, p1, o1);

                Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                Resource o2 = model.createResource(String.valueOf(objNode));
                model.add(s0, p2, o2);

                Property p3 = model.createProperty(IIIFPredicates.HAS_BODY);
                Resource o3 = model.createResource(id);
                model.add(o0, p3, o3);
            }
        }

        Dataset dataset = DatasetFactory.create(model);
        dataset.addNamedModel("http://iiif.sequence", model);
        this.rdfCollection = new ByteArrayOutputStream();
        RDFDataMgr.write(this.rdfCollection, model, Lang.NTRIPLES);
    }

    private int getIDPos(List<String> idList, String id) {
        return idList.indexOf(id);
    }

    private Map<String, Node> getBNodeKeyMap(List<String> idList) {
        Map<String, Node> bNodeMap = new HashMap <>();

        for (String id : idList) {
            int pos = getIDPos(idList, id);
            Node sNode = getNewBNode();
            Node oNode = getNewBNode();
            String subjKey = String.valueOf(pos) +
                    ":subj";
            bNodeMap.put(subjKey, sNode);
            String objKey = String.valueOf(pos) +
                    ":obj";
            bNodeMap.put(objKey, oNode);
        }
        return bNodeMap;
    }

    private Node getObjNodeFromPrevIndex(int pos, Map<String, Node> bNodeMap) {
        int prevIndex = pos -1;
        String objKey = String.valueOf(prevIndex) + ":obj";
        return bNodeMap.get(objKey);
    }

    private Node getSubjNodeForCurrentIndex(int pos, Map<String, Node> bNodeMap) {
        String objKey = String.valueOf(pos) + ":subj";
        return bNodeMap.get(objKey);
    }

    private Node getObjNodeForCurrentIndex(int pos, Map<String, Node> bNodeMap) {
        String objKey = String.valueOf(pos) + ":obj";
        return bNodeMap.get(objKey);
    }

    private static String getIdentitySubject() {
        return "";
    }

    private static Node getNewBNode() {
        final String bNodeLabelStart = "_:" ;
        ResourceCollectionBuilder.Factory fSeededHashAlloc = BlankNodeAllocatorHash::new;
        BlankNodeAllocator alloc = fSeededHashAlloc.create();
        Node n = alloc.create();
        String bnodeLabel = bNodeLabelStart + String.valueOf(n);
        return createBlankNode(bnodeLabel);
    }

    public static class ResourceCollectionBuilder {

        private List<String> idList;
        private String collectionPredicate;
        private Map<String, String> resourceTargetMap;

        public ResourceCollectionWriter.ResourceCollectionBuilder idList(List<String> idList) {
            this.idList = idList;
            return this;
        }

        public ResourceCollectionWriter.ResourceCollectionBuilder collectionPredicate(String collectionPredicate) {
            this.collectionPredicate = collectionPredicate;
            return this;
        }

        public ResourceCollectionWriter.ResourceCollectionBuilder resourceTargetMap(Map<String, String> resourceTargetMap) {
            this.resourceTargetMap = resourceTargetMap;
            return this;
        }

        public ResourceCollectionWriter build() {
            return new ResourceCollectionWriter(this.idList, this.collectionPredicate, this.resourceTargetMap);
        }

        public interface Factory {
            BlankNodeAllocator create() ;
        }
    }

}
