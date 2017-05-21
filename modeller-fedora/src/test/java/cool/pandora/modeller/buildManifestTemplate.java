package cool.pandora.modeller;

import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.templates.ManifestScope;
import cool.pandora.modeller.templates.MetadataTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class buildManifestTemplate {

    public static void main(String[] args) throws IOException {
        MetadataTemplate metadataTemplate;
        List<ManifestScope.Prefix> prefixes = Arrays.asList(new ManifestScope.Prefix(FedoraPrefixes.RDFS),
                new ManifestScope.Prefix(FedoraPrefixes.MODE));

        ManifestScope scope = new ManifestScope().fedoraPrefixes(prefixes)
                .collectionURI("http://localhost:8080/fcrepo/rest/collection/test/").label("Test")
                .sequenceURI("http://localhost:8080/fcrepo/rest/collection/test/001/sequence/normal")
                .license("http://localhost/static/test/license.html");

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-manifest.mustache").scope(scope)
                .throwExceptionOnFailure().build();

        String template = metadataTemplate.render();
        System.out.println(template);
    }
}

