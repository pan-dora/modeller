package org.blume.modeller.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    protected RDFCollectionWriter(ArrayList<Integer> idList, String collectionPredicate) {
        Model model = ModelFactory.createDefaultModel();
        Map<String, Node> bNodeMap = getBNodeKeyMap(idList);

        Resource s = model.createResource(getIdentitySubject());
        Property p = model.createProperty(collectionPredicate);
        Node firstBNode = getSubjNodeForCurrentIndex(0, bNodeMap);
        Resource o = model.createResource(String.valueOf(firstBNode));
        model.add(s, p, o);

        for (int id : idList) {
            int pos = getIDPos(idList, id);
            int lastId = idList.get(idList.size() - 1);
            if (pos == 0) {
                Node subjNode = getSubjNodeForCurrentIndex(pos, bNodeMap);
                Node objNode = getObjNodeForCurrentIndex(pos, bNodeMap);
                String objectURI = getResourceURI(id);

                Resource s1 = model.createResource(String.valueOf(subjNode));
                Property p1 = model.createProperty(String.valueOf(RDF_First));
                Resource o1 = model.createResource(objectURI);
                model.add(s1, p1, o1);

                Resource s2 = model.createResource(String.valueOf(subjNode));
                Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                Resource o2 = model.createResource(String.valueOf(objNode));
                model.add(s2, p2, o2);
            } else if (id == lastId ) {
                Node subjNode = getObjNodeFromPrevIndex(pos, bNodeMap);
                String objectURI = getResourceURI(id);
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
                String objectURI = getResourceURI(id);

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

    private int getIDPos(ArrayList<Integer> idList, int id) {
        return idList.indexOf(id);
    }

    private Map<String, Node> getBNodeKeyMap(ArrayList<Integer> idList) {
        Map<String, Node> bNodeMap = new HashMap <>();

        for (int id : idList) {
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

    public String getResourceURI(int resourceID) {
        /** TODO Make an ResourceURI Generator Class */
        String baseURI = "http://localhost:8080/fcrepo/rest/collection/AIG/1011/canvas/c";
        return baseURI +
                resourceID;
    }

    public static class RDFCollectionBuilder {

        private ArrayList<Integer> idList;
        private String collectionPredicate;

        public RDFCollectionWriter.RDFCollectionBuilder idList(ArrayList<Integer> idList) {
            this.idList = idList;
            return this;
        }

        public RDFCollectionWriter.RDFCollectionBuilder collectionPredicate(String collectionPredicate) {
            this.collectionPredicate = collectionPredicate;
            return this;
        }

        public RDFCollectionWriter build() {
            return new RDFCollectionWriter(this.idList, this.collectionPredicate);
        }

        public interface Factory {
            BlankNodeAllocator create() ;
        }
    }

}
