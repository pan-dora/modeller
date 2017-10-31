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

package cool.pandora.modeller.ui.handlers.common;

import static cool.pandora.modeller.ui.util.URIResolver.ContainerURIResolverNormal.getMapValue;

import cool.pandora.modeller.ProfileOptions;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.ui.util.URIResolver;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * IIIF Object URI.
 *
 * @author Christopher Johnson
 */
public final class IIIFObjectURI {

    protected static final Logger log = LoggerFactory.getLogger(TextObjectURI.class);

    private IIIFObjectURI() {
    }

    /**
     * buildContainerURI.
     *
     * @param map          Map
     * @param containerKey String
     * @return URI
     */
    public static URI buildContainerURI(final Map<String, BagInfoField> map,
                                        final String containerKey) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(containerKey).pathType(4).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getCollectionIdURI.
     *
     * @param map Map
     * @return URI
     */
    public static URI getCollectionIdURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).pathType(2).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getObjektURI.
     *
     * @param map Map
     * @return URI
     */
    public static URI getObjektURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).pathType(3).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getManifestResource.
     *
     * @param map Map
     * @return URI
     */
    public static URI getManifestResource(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).pathType(6).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getSequenceContainerURI.
     *
     * @param map Map
     * @return URI
     */
    public static URI getSequenceContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map)
                    .containerKey(ProfileOptions.SEQUENCE_CONTAINER_KEY).pathType(4).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getSequenceObjectURI.
     *
     * @param map        Map
     * @param sequenceID String
     * @return URI
     */
    public static URI getSequenceObjectURI(final Map<String, BagInfoField> map,
                                           final String sequenceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map)
                    .containerKey(ProfileOptions.SEQUENCE_CONTAINER_KEY).resource(sequenceID)
                    .pathType(5).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getCanvasContainerURI.
     *
     * @param map Map
     * @return URI
     */
    public static URI getCanvasContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.CANVAS_CONTAINER_KEY)
                            .pathType(4).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getCanvasObjectURI.
     *
     * @param map        Map
     * @param resourceID String
     * @return URI
     */
    public static URI getCanvasObjectURI(final Map<String, BagInfoField> map,
                                         final String resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.CANVAS_CONTAINER_KEY)
                            .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getListContainerURI.
     *
     * @param map Map
     * @return URI
     */
    public static URI getListContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.LIST_CONTAINER_KEY)
                            .pathType(4).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getListObjectURI.
     *
     * @param map        Map
     * @param resourceID String
     * @return URI
     */
    public static URI getListObjectURI(final Map<String, BagInfoField> map,
                                       final String resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.LIST_CONTAINER_KEY)
                            .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getResourceContainerURI.
     *
     * @param map Map
     * @return URI
     */
    public static URI getResourceContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map)
                    .containerKey(ProfileOptions.RESOURCE_CONTAINER_KEY).pathType(4).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getDestinationURI.
     *
     * @param map      Map
     * @param filename String
     * @return URI
     */
    public static URI getDestinationURI(final Map<String, BagInfoField> map,
                                        final String filename) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map)
                    .containerKey(ProfileOptions.RESOURCE_CONTAINER_KEY).resource(filename)
                    .pathType(5).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getListServiceBaseURI.
     *
     * @param map Map
     * @return String
     */
    public static String getListServiceBaseURI(final Map<String, BagInfoField> map) {
        return getMapValue(map, ProfileOptions.LIST_SERVICE_KEY);
    }

}
