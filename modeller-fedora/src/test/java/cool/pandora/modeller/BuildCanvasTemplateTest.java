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

import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.templates.CanvasScope;
import cool.pandora.modeller.templates.MetadataTemplate;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * buildCanvasTemplateTest
 *
 * @author Christopher Johnson
 */
public class BuildCanvasTemplateTest {

    @Test
    public void buildTemplateTest() throws IOException {
        final MetadataTemplate metadataTemplate;
        final List<CanvasScope.Prefix> prefixes =
                Arrays.asList(new CanvasScope.Prefix(FedoraPrefixes.RDFS), new CanvasScope.Prefix
                        (FedoraPrefixes.MODE));

        final CanvasScope scope = new CanvasScope().fedoraPrefixes(prefixes)
                .resourceURI("http://localhost:8080/fcrepo/rest/collection/test/001/res/001.tif")
                .listURI("http://localhost:8080/fcrepo/rest/collection/test/001/list/001")
                .canvasLabel("te&quot;st")
                .canvasHeight(3000).canvasWidth(2000);

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-canvas" +
                ".mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String template = metadataTemplate.render();
        System.out.println(template);
        InputStream is = getClass().getResourceAsStream("/canvas_template_out.txt");
        String out = TestUtils.StreamToString(is);
        assertEquals(template, out);
    }
}
