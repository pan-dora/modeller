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

package cool.pandora.modeller.ui.handlers.common;


import org.apache.commons.io.IOUtils;
import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.common.uri.IIIFPredicates;
import cool.pandora.modeller.common.uri.IIIFPrefixes;
import cool.pandora.modeller.templates.CollectionScope;
import cool.pandora.modeller.templates.ListScope;
import cool.pandora.modeller.templates.MetadataTemplate;
import cool.pandora.modeller.util.ResourceCollectionWriter;
import cool.pandora.modeller.util.ServiceNodeWriter;
import cool.pandora.modeller.util.TextCollectionWriter;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;

/**
 * Text Sequence Metadata
 *
 * @author Christopher Johnson
 */
public class TextSequenceMetadata {

    private TextSequenceMetadata() {
    }

    /**
     * @param resourceIDList       Map
     * @param pageId               String
     * @param canvasRegionURI      String
     * @param collectionPredicate  String
     * @param resourceContainerIRI URI
     * @return InputStream
     */
    public static InputStream getTextSequenceMetadata(final Map<String, List<String>> resourceIDList,
                                                      final String pageId, final String canvasRegionURI,
                                                      final String collectionPredicate,
                                                      final URI resourceContainerIRI) {
        final ArrayList<String> idList = new ArrayList<>(resourceIDList.get(pageId));
        final TextCollectionWriter collectionWriter;
        collectionWriter = TextCollectionWriter.collection().idList(idList).collectionPredicate(collectionPredicate)
                .resourceContainerIRI(resourceContainerIRI.toString()).canvasURI(canvasRegionURI).build();

        final String collection = collectionWriter.render();
        final MetadataTemplate metadataTemplate;
        final List<CollectionScope.Prefix> prefixes = Arrays.asList(new CollectionScope.Prefix(FedoraPrefixes.RDFS),
                new CollectionScope.Prefix(FedoraPrefixes.MODE));

        final CollectionScope scope = new CollectionScope().fedoraPrefixes(prefixes).sequenceGraph(collection);

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-seq.mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8);
    }

    /**
     * @param resourceIDList      Map
     * @param listURI             String
     * @param resourceTargetMap   Map
     * @param collectionPredicate String
     * @param listServiceBaseURI  String
     * @return InputStream
     */
    public static InputStream getListSequenceMetadata(final Map<String, List<String>> resourceIDList,
                                                      final String listURI, final Map<String, String> resourceTargetMap,
                                                      final String collectionPredicate,
                                                      final String listServiceBaseURI) {
        final ArrayList<String> idList = new ArrayList<>(resourceIDList.get(listURI));

        final ResourceCollectionWriter collectionWriter;
        collectionWriter = ResourceCollectionWriter.collection().idList(idList).collectionPredicate(collectionPredicate)
                .resourceTargetMap(resourceTargetMap).build();

        final String collection = collectionWriter.render();

        final String serviceURI = listServiceBaseURI + listURI;
        final ServiceNodeWriter serviceNodeWriter;
        serviceNodeWriter = ServiceNodeWriter.init().serviceURI(serviceURI).servicePredicate(IIIFPredicates.SERVICE)
                .serviceType("http://localhost:3000/listcontext.json").build();
        final String serviceNode = serviceNodeWriter.render();


        final MetadataTemplate metadataTemplate;
        final List<ListScope.Prefix> prefixes =
                Arrays.asList(new ListScope.Prefix(FedoraPrefixes.RDFS), new ListScope.Prefix(FedoraPrefixes.MODE),
                        new ListScope.Prefix(IIIFPrefixes.SVCS), new ListScope.Prefix(IIIFPrefixes.SC));

        final ListScope scope =
                new ListScope().fedoraPrefixes(prefixes).sequenceGraph(collection).serviceNode(serviceNode);

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-list.mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8);
    }
}
