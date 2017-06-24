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

import java.io.IOException;

/**
 * docManifestBuilderTest
 *
 * @author Christopher Johnson
 */
public class docManifestBuilderTest {

    private docManifestBuilderTest() {
    }

    public static void main(final String[] args) throws IOException {
        final String url = "resource://data/001.hocr";
        final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
        final String resourceURI = "http://localhost:8080/fcrepo/rest/collection/test/004/area/";
        final String rdfseq = DocManifestBuilder.getAreaRDFSequenceForhOCRResource(hocr, resourceURI);
        System.out.println(rdfseq);
        //Map map = getAreaMapForhOCRResource(hocr);
        //System.out.println(map);
    }
}
