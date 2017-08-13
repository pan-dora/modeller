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

package cool.pandora.modeller;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;

import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.common.uri.IIIFPrefixes;
import cool.pandora.modeller.templates.MetadataTemplate;
import cool.pandora.modeller.templates.WordScope;

import java.util.Arrays;
import java.util.List;


/**
 * wordScopeTest.
 *
 * @author Christopher Johnson
 */
public class WordScopeTest {

    private WordScopeTest() {
    }

    public static void main(final String[] args) {
        final MetadataTemplate metadataTemplate;
        final String canvasRegionURI =
                "http://localhost:8080/fcrepo/rest/collection/test/021/canvas/007#xywh=445%2C1431"
                        + "" + "%2C154%2C40";
        final String wordContainerURI = "http://test/word";
        final String chars = "blah&quot;blah&quot;blah";

        final List<WordScope.Prefix> prefixes =
                Arrays.asList(new WordScope.Prefix(FedoraPrefixes.RDFS), new WordScope.Prefix(
                                FedoraPrefixes.MODE),
                        new WordScope.Prefix(IIIFPrefixes.OA), new WordScope.Prefix(IIIFPrefixes
                                .CNT),
                        new WordScope.Prefix(IIIFPrefixes.SC), new WordScope.Prefix(IIIFPrefixes
                                .DCTYPES));

        final WordScope scope = new WordScope().fedoraPrefixes(prefixes).canvasURI(canvasRegionURI)
                .resourceContainerURI(wordContainerURI).chars(chars.replace("&quot;", "\\\""));

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-word"
                + ".mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String metadata = unescapeXml(metadataTemplate.render());
        System.out.println(metadata);
    }
}
