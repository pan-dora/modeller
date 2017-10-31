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

import java.net.URI;

/**
 * testFragmentURI.
 *
 * @author Christopher Johnson
 */
public class TestFragmentURI {

    private TestFragmentURI() {
    }

    public static void main(final String[] args) {
        final URI uri = URI.create(
                "http://localhost:8080/fcrepo/rest/collection/test/003/canvas/19#xywh"
                        + "=2999,1542,62,56");
        final String out = uri.toString();
        System.out.println(out);
    }
}
