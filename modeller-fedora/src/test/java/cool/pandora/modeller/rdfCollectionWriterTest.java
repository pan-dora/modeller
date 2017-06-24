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

import cool.pandora.modeller.util.RDFCollectionWriter;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * rdfCollectionWriterTest
 *
 * @author Christopher Johnson
 */
public class rdfCollectionWriterTest {

    private rdfCollectionWriterTest() {
    }

    public static void main(final String[] args) {
        final RDFCollectionWriter collectionWriter;
        collectionWriter = RDFCollectionWriter.collection().idList(getMockSequence())
                .collectionPredicate("http://iiif.io/api/presentation/2#hasCanvases")
                .resourceContainerIRI("http://localhost:8080/fcrepo/rest/collection/AIG/").build();
        final String collection = collectionWriter.render();
        System.out.println(collection);
    }

    private static ArrayList<String> getMockSequence() {
        final int numOfValues = 2;
        final int[] array = IntStream.range(1, numOfValues + 1).toArray();
        final ArrayList<String> idList = new ArrayList<>(array.length);
        for (final int anArray : array) {
            idList.add(String.valueOf(anArray));
        }
        return idList;
    }
}


