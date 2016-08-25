package org.blume.modeller.templates;

import org.blume.modeller.common.uri.FedoraPrefixes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class buildTemplate {

    public static void main(String[] args) throws IOException {
        ResourceTemplate resourceTemplate;
        List<ResourceTemplate.Scope.Prefix> prefixes = Arrays.asList(
                new ResourceTemplate.Scope.Prefix(FedoraPrefixes.RDFS),
                new ResourceTemplate.Scope.Prefix(FedoraPrefixes.MODE));

        ResourceTemplate.Scope scope = new ResourceTemplate.Scope()
                .fedoraPrefixes(prefixes)
                .serviceURI("http://localhost:8888/iiif/");

        resourceTemplate = ResourceTemplate.template()
                .template("template/sparql-update.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String template = resourceTemplate.render();
        System.out.println(template);
    }
}
