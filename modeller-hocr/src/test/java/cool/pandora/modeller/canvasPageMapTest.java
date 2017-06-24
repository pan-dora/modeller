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
import java.net.URI;
import java.util.List;

import static cool.pandora.modeller.DocManifestBuilder.getPageIdList;

/**
 * canvasPageMapTest
 *
 * @author Christopher Johnson
 */
public class canvasPageMapTest {

    private canvasPageMapTest() {
    }

    public static void main(final String[] args) throws IOException {
        final List<String> pageIdList;
        final String url = "resource://data/test_007.hocr";
        final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
        pageIdList = getPageIdList(hocr);
        final CanvasPageMap canvasPageMap = CanvasPageMap.init()
                .canvasContainerURI(URI.create("http://localhost:8080/fcrepo/rest/collection/test/007/canvas"))
                .pageIdList(pageIdList).build();
        System.out.println(canvasPageMap);
    }
}
