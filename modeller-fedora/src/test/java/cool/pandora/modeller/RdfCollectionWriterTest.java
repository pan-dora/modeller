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

import static junit.framework.TestCase.assertNotNull;

import cool.pandora.modeller.util.RDFCollectionWriter;

import java.io.IOException;

import org.junit.Test;


/**
 * RdfCollectionWriterTest.
 *
 * @author Christopher Johnson
 */
public class RdfCollectionWriterTest {

    @Test
    public void rdfCollectionWriterTest() throws IOException {
        final RDFCollectionWriter collectionWriter;
        collectionWriter = RDFCollectionWriter.collection().idList(TestUtils.getMockSequence())
                .collectionPredicate("http://iiif.io/api/presentation/2#hasCanvases")
                .resourceContainerIRI("http://localhost:8080/fcrepo/rest/collection/AIG/").build();
        final String collection = collectionWriter.render();
        System.out.println(collection);
        assertNotNull(collection);
    }
}


