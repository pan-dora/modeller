package org.blume.modeller;

import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.ResourceScope;
import org.blume.modeller.templates.ResourceTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class buildTemplate {

    public static void main(String[] args) throws IOException {
        ResourceTemplate resourceTemplate;
        List<ResourceScope.Prefix> prefixes = Arrays.asList(
                new ResourceScope.Prefix(FedoraPrefixes.RDFS),
                new ResourceScope.Prefix(FedoraPrefixes.MODE));

        ResourceScope scope = new ResourceScope()
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
