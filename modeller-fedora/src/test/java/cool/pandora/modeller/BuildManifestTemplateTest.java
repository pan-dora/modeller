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

import static junit.framework.TestCase.assertEquals;

import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.templates.ManifestScope;
import cool.pandora.modeller.templates.MetadataTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * buildManifestTemplateTest.
 *
 * @author Christopher Johnson
 */
public class BuildManifestTemplateTest {

    @Test
    public void buildManifestTemplateTest() throws IOException {
        final MetadataTemplate metadataTemplate;
        final List<ManifestScope.Prefix> prefixes = Arrays.asList(new ManifestScope.Prefix(
                        FedoraPrefixes.RDFS),
                new ManifestScope.Prefix(FedoraPrefixes.MODE));

        final ManifestScope scope = new ManifestScope().fedoraPrefixes(prefixes)
                .collectionURI("http://localhost:8080/fcrepo/rest/collection/test/").label("Test")
                .sequenceURI("http://localhost:8080/fcrepo/rest/collection/test/001/sequence"
                        + "/normal")
                .license("http://localhost/static/test/license.html");

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-manifest"
                + ".mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String template = metadataTemplate.render();
        System.out.println(template);
        InputStream is = getClass().getResourceAsStream("/manifest_template_out.txt");
        String out = TestUtils.streamToString(is);
        assertEquals(template, out);
    }
}

