package org.blume.modeller.util;

import java.io.ByteArrayOutputStream;
import java.util.*;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.BlankNodeAllocator;
import org.apache.jena.riot.lang.BlankNodeAllocatorHash;
import static org.apache.jena.riot.writer.WriterConst.RDF_First ;
import static org.apache.jena.riot.writer.WriterConst.RDF_Nil ;
import static org.apache.jena.riot.writer.WriterConst.RDF_Rest ;

import static org.apache.jena.graph.NodeFactory.createBlankNode;

public class RDFCollectionWriter {

    public static RDFCollectionBuilder collection() {
        return new RDFCollectionBuilder();
    }

    private ByteArrayOutputStream rdfCollection;

    public String render() {
        return this.rdfCollection.toString();
    }

    protected RDFCollectionWriter(final List<String> idList, final String collectionPredicate,
                                  final String resourceContainerIRI) {

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
            //singleton list
            if (pos == 0  && (Objects.equals(id, lastId))) {
                Node subjNode = getSubjNodeForCurrentIndex(pos, bNodeMap);
                String objectURI = getResourceURI(resourceContainerIRI, id);

                Resource s1 = model.createResource(String.valueOf(subjNode));
                Property p1 = model.createProperty(String.valueOf(RDF_First));
                Resource o1 = model.createResource(objectURI);
                model.add(s1, p1, o1);

                Resource s2 = model.createResource(String.valueOf(subjNode));
                Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                Resource o2 = model.createResource(String.valueOf(RDF_Nil));
                model.add(s2, p2, o2);
            } else  if (pos == 0) {
                Node subjNode = getSubjNodeForCurrentIndex(pos, bNodeMap);
                Node objNode = getObjNodeForCurrentIndex(pos, bNodeMap);
                String objectURI = getResourceURI(resourceContainerIRI, id);

                Resource s1 = model.createResource(String.valueOf(subjNode));
                Property p1 = model.createProperty(String.valueOf(RDF_First));
                Resource o1 = model.createResource(objectURI);
                model.add(s1, p1, o1);

                Resource s2 = model.createResource(String.valueOf(subjNode));
                Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                Resource o2 = model.createResource(String.valueOf(objNode));
                model.add(s2, p2, o2);
            } else if (Objects.equals(id, lastId)) {
                Node subjNode = getObjNodeFromPrevIndex(pos, bNodeMap);
                String objectURI = getResourceURI(resourceContainerIRI, id);

                Resource s1 = model.createResource(String.valueOf(subjNode));
                Property p1 = model.createProperty(String.valueOf(RDF_First));
                Resource o1 = model.createResource(objectURI);
                model.add(s1, p1, o1);

                Resource s2 = model.createResource(String.valueOf(subjNode));
                Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                Resource o2 = model.createResource(String.valueOf(RDF_Nil));
                model.add(s2, p2, o2);
            } else {
                Node subjNode = getObjNodeFromPrevIndex(pos, bNodeMap);
                Node objNode = getObjNodeForCurrentIndex(pos, bNodeMap);
                String objectURI = getResourceURI(resourceContainerIRI, id);

                Resource s1 = model.createResource(String.valueOf(subjNode));
                Property p1 = model.createProperty(String.valueOf(RDF_First));
                Resource o1 = model.createResource(objectURI);
                model.add(s1, p1, o1);

                Resource s2 = model.createResource(String.valueOf(subjNode));
                Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                Resource o2 = model.createResource(String.valueOf(objNode));
                model.add(s2, p2, o2);
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
        RDFCollectionBuilder.Factory fSeededHashAlloc = BlankNodeAllocatorHash::new;
        BlankNodeAllocator alloc = fSeededHashAlloc.create();
        Node n = alloc.create();
        String bnodeLabel = bNodeLabelStart + String.valueOf(n);
        return createBlankNode(bnodeLabel);
    }

    private String getResourceURI(String resourceContainerIRI, String resourceID) {
        return resourceContainerIRI +
                resourceID;
    }

    public static class RDFCollectionBuilder {

        private List<String> idList;
        private String collectionPredicate;
        private String resourceContainerIRI;

        public RDFCollectionWriter.RDFCollectionBuilder idList(List<String> idList) {
            this.idList = idList;
            return this;
        }

        public RDFCollectionWriter.RDFCollectionBuilder collectionPredicate(String collectionPredicate) {
            this.collectionPredicate = collectionPredicate;
            return this;
        }

        public RDFCollectionWriter.RDFCollectionBuilder resourceContainerIRI(String resourceContainerIRI ) {
            this.resourceContainerIRI = resourceContainerIRI;
            return this;
        }

        public RDFCollectionWriter build() {
            return new RDFCollectionWriter(this.idList, this.collectionPredicate, this.resourceContainerIRI);
        }

        public interface Factory {
            BlankNodeAllocator create() ;
        }
    }

}
