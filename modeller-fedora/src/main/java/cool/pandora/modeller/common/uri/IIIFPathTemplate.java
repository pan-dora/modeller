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
 * IIIFPathTemplate
 *
 * @author Christopher Johnson
 */
public final class IIIFPathTemplate extends Type {
    private IIIFPathTemplate(final int value, final String path) {
        super(value, path);
    }

    public static final IIIFPathTemplate BASE_URI_PATH = new IIIFPathTemplate(0, "/{FedoraAppRoot}/{RestServletURI}/");
    public static final IIIFPathTemplate COLLECTION_ROOT_PATH =
            new IIIFPathTemplate(1, "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/");
    public static final IIIFPathTemplate COLLECTION_ID_PATH =
            new IIIFPathTemplate(2, "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/");
    public static final IIIFPathTemplate OBJECT_ID_PATH =
            new IIIFPathTemplate(3, "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/{objectID}/");
    public static final IIIFPathTemplate CONTAINER_PATH = new IIIFPathTemplate(4,
            "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/{objectID}/{container}/");
    public static final IIIFPathTemplate RESOURCE_PATH = new IIIFPathTemplate(5,
            "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/{objectID}/{container}/{resource}");
    public static final IIIFPathTemplate MANIFEST_PATH = new IIIFPathTemplate(6,
            "/{FedoraAppRoot}/{RestServletURI}/{collectionRoot}/{collectionID}/{objectID}/{manifest}");
}
