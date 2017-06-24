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

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * modellerSparqlUpdateReaderTest
 *
 * @author Christopher Johnson
 */
public class modellerSparqlUpdateReaderTest {

    private modellerSparqlUpdateReaderTest() {
    }

    public static void main(final String[] args) {
        //Model model = RDFDataMgr.loadModel("data/res.n3") ;

        final InputStream requestBodyStream =
                modellerSparqlUpdateReaderTest.class.getResourceAsStream("/data/res.update");

        try {
            final String requestBody = IOUtils.toString(requestBodyStream, UTF_8);
            System.out.println();
            System.out.println("#### ---- Write as application/sparql-update");
            System.out.println(requestBody);
        } catch (final IOException ex) {
        }
    }

}