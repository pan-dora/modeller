package cool.pandora.modeller;

import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.templates.CanvasScope;
import cool.pandora.modeller.templates.MetadataTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class buildCanvasTemplate {

    public static void main(String[] args) throws IOException {
        MetadataTemplate metadataTemplate;
        List<CanvasScope.Prefix> prefixes =
                Arrays.asList(new CanvasScope.Prefix(FedoraPrefixes.RDFS), new CanvasScope.Prefix(FedoraPrefixes.MODE));

        CanvasScope scope = new CanvasScope().fedoraPrefixes(prefixes)
                .resourceURI("http://localhost:8080/fcrepo/rest/collection/test/001/res/001.tif")
                .listURI("http://localhost:8080/fcrepo/rest/collection/test/001/list/001").canvasLabel("te&quot;st")
                .canvasHeight(3000).canvasWidth(2000);

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-canvas.mustache").scope(scope)
                .throwExceptionOnFailure().build();

        String template = metadataTemplate.render();
        System.out.println(template);
    }
}
