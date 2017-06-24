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
package cool.pandora.modeller.common.uri;

/**
 * IIIFPredicates
 *
 * @author Christopher Johnson
 */
public class IIIFPredicates {
    private IIIFPredicates() {
    }

    public static final String ON = "http://www.w3.org/ns/oa#hasTarget";
    public static final String HEIGHT = "http://www.w3.org/2003/12/exif/ns#height";
    public static final String WIDTH = "http://www.w3.org/2003/12/exif/ns#width";
    public static final String HAS_BODY = "http://www.w3.org/ns/oa#hasBody";
    public static final String SERVICE = "http://rdfs.org/sioc/services#has_service";
    public static final String OTHER_CONTENT = "http://iiif.io/api/presentation/2#hasAnnotations";
}
