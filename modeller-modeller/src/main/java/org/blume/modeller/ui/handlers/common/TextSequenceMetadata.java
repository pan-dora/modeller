package org.blume.modeller.ui.handlers.common;


import org.apache.commons.io.IOUtils;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.CollectionScope;
import org.blume.modeller.templates.MetadataTemplate;
import org.blume.modeller.util.ResourceCollectionWriter;
import org.blume.modeller.util.TextCollectionWriter;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;

public class TextSequenceMetadata {
    public static InputStream getTextSequenceMetadata(Map<String, List<String>> resourceIDList, String pageId, String
            canvasRegionURI, String collectionPredicate, URI resourceContainerIRI) {
        ArrayList<String> idList = new ArrayList<>(resourceIDList.get(pageId));
        TextCollectionWriter collectionWriter;
        collectionWriter = TextCollectionWriter.collection()
                .idList(idList)
                .collectionPredicate(collectionPredicate)
                .resourceContainerIRI(resourceContainerIRI.toString())
                .canvasURI(canvasRegionURI)
                .build();

        String collection = collectionWriter.render();
        MetadataTemplate metadataTemplate;
        List<CollectionScope.Prefix> prefixes = Arrays.asList(
                new CollectionScope.Prefix(FedoraPrefixes.RDFS),
                new CollectionScope.Prefix(FedoraPrefixes.MODE));

        CollectionScope scope = new CollectionScope()
                .fedoraPrefixes(prefixes)
                .sequenceGraph(collection);

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-seq.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8);
    }

    public static InputStream getListSequenceMetadata(Map<String, List<String>> resourceIDList, String pageId,
                      Map<String, String> resourceTargetMap, String collectionPredicate) {
        ArrayList<String> idList = new ArrayList<>(resourceIDList.get(pageId));
        ResourceCollectionWriter collectionWriter;
        collectionWriter = ResourceCollectionWriter.collection()
                .idList(idList)
                .collectionPredicate(collectionPredicate)
                .resourceTargetMap(resourceTargetMap)
                .build();

        String collection = collectionWriter.render();
        MetadataTemplate metadataTemplate;
        List<CollectionScope.Prefix> prefixes = Arrays.asList(
                new CollectionScope.Prefix(FedoraPrefixes.RDFS),
                new CollectionScope.Prefix(FedoraPrefixes.MODE));

        CollectionScope scope = new CollectionScope()
                .fedoraPrefixes(prefixes)
                .sequenceGraph(collection);

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-seq.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8);
    }
}
