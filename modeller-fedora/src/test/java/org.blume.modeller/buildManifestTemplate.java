package org.blume.modeller;

import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.ManifestScope;
import org.blume.modeller.templates.MetadataTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class buildManifestTemplate {

    public static void main(String[] args) throws IOException {
        MetadataTemplate metadataTemplate;
        List<ManifestScope.Prefix> prefixes = Arrays.asList(
                new ManifestScope.Prefix(FedoraPrefixes.RDFS),
                new ManifestScope.Prefix(FedoraPrefixes.MODE));

        ManifestScope scope = new ManifestScope()
                .fedoraPrefixes(prefixes)
                .collectionURI("http://localhost:8080/fcrepo/rest/collection/test/")
                .label("Test")
                .sequenceURI("http://localhost:8080/fcrepo/rest/collection/test/001/sequence/normal")
                .license("http://localhost/static/test/license.html")
                .format("http://example.org/iiif/book1.pdf");

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-manifest.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String template = metadataTemplate.render();
        System.out.println(template);
    }
}

