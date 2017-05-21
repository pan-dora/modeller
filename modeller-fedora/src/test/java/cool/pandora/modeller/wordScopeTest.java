package cool.pandora.modeller;

import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.common.uri.IIIFPrefixes;
import cool.pandora.modeller.templates.MetadataTemplate;
import cool.pandora.modeller.templates.WordScope;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;

public class wordScopeTest {
    public static void main(String[] args) {
        MetadataTemplate metadataTemplate;
        String canvasRegionURI =
                "http://localhost:8080/fcrepo/rest/collection/test/021/canvas/007#xywh=445%2C1431" + "%2C154%2C40";
        String wordContainerURI = "http://test/word";
        String chars = "blah&quot;blah&quot;blah";

        List<WordScope.Prefix> prefixes =
                Arrays.asList(new WordScope.Prefix(FedoraPrefixes.RDFS), new WordScope.Prefix(FedoraPrefixes.MODE),
                        new WordScope.Prefix(IIIFPrefixes.OA), new WordScope.Prefix(IIIFPrefixes.CNT),
                        new WordScope.Prefix(IIIFPrefixes.SC), new WordScope.Prefix(IIIFPrefixes.DCTYPES));

        WordScope scope = new WordScope().fedoraPrefixes(prefixes).canvasURI(canvasRegionURI)
                .resourceContainerURI(wordContainerURI).chars(chars.replace("&quot;", "\\\""));

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-word.mustache").scope(scope)
                .throwExceptionOnFailure().build();

        String metadata = unescapeXml(metadataTemplate.render());
        System.out.println(metadata);
    }
}
