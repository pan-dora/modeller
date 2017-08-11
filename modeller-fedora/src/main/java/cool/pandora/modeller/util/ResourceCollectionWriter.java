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

import static org.apache.jena.graph.NodeFactory.createBlankNode;
import static org.apache.jena.riot.writer.WriterConst.RDF_First;
import static org.apache.jena.riot.writer.WriterConst.RDF_Nil;
import static org.apache.jena.riot.writer.WriterConst.RDF_Rest;

import cool.pandora.modeller.common.uri.IIIFPredicates;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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



/**
 * ResourceCollectionWriter.
 *
 * @author Christopher Johnson
 */
public class ResourceCollectionWriter {
    /**
     * collection.
     *
     * @return ResourceCollectionBuilder
     */
    public static ResourceCollectionBuilder collection() {
        return new ResourceCollectionBuilder();
    }

    private final ByteArrayOutputStream rdfCollection;

    /**
     * render.
     *
     * @return rdfCollection
     */
    public String render() {
        return this.rdfCollection.toString();
    }

    /**
     * ResourceCollectionWriter.
     *
     * @param idList List
     * @param collectionPredicate String
     * @param resourceTargetMap Map
     */
    ResourceCollectionWriter(final List<String> idList, final String collectionPredicate,
                             final Map<String, String> resourceTargetMap) {

        final Model model = ModelFactory.createDefaultModel();
        final Map<String, Node> bnodeMap = getBNodeKeyMap(idList);

        final Resource s = model.createResource(getIdentitySubject());
        final Property p = model.createProperty(collectionPredicate);
        final Node firstBNode = getSubjNodeForCurrentIndex(0, bnodeMap);
        final Resource o = model.createResource(String.valueOf(firstBNode));
        model.add(s, p, o);

        for (final String id : idList) {
            final int pos = getIDPos(idList, id);
            final String lastId = idList.get(idList.size() - 1);
            final String canvasURI = resourceTargetMap.get(id);
            //singleton list
            if (pos == 0 && (Objects.equals(id, lastId))) {
                final Node subjNode = getSubjNodeForCurrentIndex(pos, bnodeMap);

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
                final Node subjNode = getSubjNodeForCurrentIndex(pos, bnodeMap);
                final Node objNode = getObjNodeForCurrentIndex(pos, bnodeMap);

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
                final Node subjNode = getObjNodeFromPrevIndex(pos, bnodeMap);

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
                final Node subjNode = getObjNodeFromPrevIndex(pos, bnodeMap);
                final Node objNode = getObjNodeForCurrentIndex(pos, bnodeMap);

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
     * getIDPos.
     *
     * @param idList List
     * @param id String
     * @return id position
     */
    private static int getIDPos(final List<String> idList, final String id) {
        return idList.indexOf(id);
    }

    /**
     * getBNodeKeyMap.
     *
     * @param idList List
     * @return bnodeMap
     */
    private static Map<String, Node> getBNodeKeyMap(final List<String> idList) {
        final Map<String, Node> bnodeMap = new HashMap<>();

        for (final String id : idList) {
            final int pos = getIDPos(idList, id);
            final Node sNode = getNewBNode();
            final Node oNode = getNewBNode();
            final String subjKey = String.valueOf(pos) + ":subj";
            bnodeMap.put(subjKey, sNode);
            final String objKey = String.valueOf(pos) + ":obj";
            bnodeMap.put(objKey, oNode);
        }
        return bnodeMap;
    }

    /**
     * getObjNodeFromPrevIndex.
     *
     * @param pos int
     * @param bnodeMap bnodeMap
     * @return object Node
     */
    private static Node getObjNodeFromPrevIndex(final int pos, final Map<String, Node> bnodeMap) {
        final int prevIndex = pos - 1;
        final String objKey = String.valueOf(prevIndex) + ":obj";
        return bnodeMap.get(objKey);
    }

    /**
     * getSubjNodeForCurrentIndex.
     *
     * @param pos int
     * @param bnodeMap Map
     * @return subject Node
     */
    private static Node getSubjNodeForCurrentIndex(final int pos, final Map<String, Node>
            bnodeMap) {
        final String objKey = String.valueOf(pos) + ":subj";
        return bnodeMap.get(objKey);
    }

    /**
     * getObjNodeForCurrentIndex.
     *
     * @param pos int
     * @param bnodeMap Map
     * @return object Node
     */
    private static Node getObjNodeForCurrentIndex(final int pos, final Map<String, Node> bnodeMap) {
        final String objKey = String.valueOf(pos) + ":obj";
        return bnodeMap.get(objKey);
    }

    /**
     * getIdentitySubject.
     *
     * @return identity
     */
    private static String getIdentitySubject() {
        return "";
    }

    /**
     * getNewBNode.
     *
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

    /**
     * ResourceCollectionBuilder.
     */
    public static class ResourceCollectionBuilder {

        private List<String> idList;
        private String collectionPredicate;
        private Map<String, String> resourceTargetMap;

        /**
         * idList.
         *
         * @param idList List
         * @return this
         */
        public ResourceCollectionWriter.ResourceCollectionBuilder idList(final List<String>
                                                                                 idList) {
            this.idList = idList;
            return this;
        }

        /**
         * collectionPredicate.
         *
         * @param collectionPredicate String
         * @return this
         */
        public ResourceCollectionWriter.ResourceCollectionBuilder collectionPredicate(
                final String collectionPredicate) {
            this.collectionPredicate = collectionPredicate;
            return this;
        }

        /**
         * resourceTargetMap.
         *
         * @param resourceTargetMap Map
         * @return this
         */
        public ResourceCollectionWriter.ResourceCollectionBuilder resourceTargetMap(
                final Map<String, String> resourceTargetMap) {
            this.resourceTargetMap = resourceTargetMap;
            return this;
        }

        /**
         * build.
         *
         * @return ResourceCollectionWriter
         */
        public ResourceCollectionWriter build() {
            return new ResourceCollectionWriter(this.idList, this.collectionPredicate, this
                    .resourceTargetMap);
        }

        /**
         * Factory.
         */
        public interface Factory {
            /**
             * create.
             *
             * @return blank node
             */
            BlankNodeAllocator create();
        }
    }

}
