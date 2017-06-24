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
 * getBboxTest
 *
 * @author Christopher Johnson
 */
public class getBboxTest {

    private getBboxTest() {
    }

    public static void main(final String[] args) throws IOException {
        final String url = "resource://data/test_007.hocr";
        final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);

        final String bbox = DocManifestBuilder.getBboxForId(hocr, "block_1_4");
        final String region = Region.region().bbox(bbox).build();
        System.out.println(region);
    }
}
