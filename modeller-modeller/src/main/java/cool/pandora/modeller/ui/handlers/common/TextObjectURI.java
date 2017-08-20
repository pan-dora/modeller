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
 * Text Object URI.
 *
 * @author Christopher Johnson
 */
public class TextObjectURI {
    protected static final Logger log = LoggerFactory.getLogger(TextObjectURI.class);

    private TextObjectURI() {
    }

    /**
     * gethOCRResourceURI.
     *
     * @param map Map
     * @return String
     */
    public static String gethOCRResourceURI(final Map<String, BagInfoField> map) {
        return getMapValue(map, ProfileOptions.TEXT_HOCR_RESOURCE_KEY);
    }

    /**
     * getPageContainerURI.
     *
     * @param map Map
     * @return String
     */
    public static URI getPageContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions
                            .TEXT_PAGE_CONTAINER_KEY).pathType(4)
                            .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getAreaContainerURI.
     *
     * @param map Map
     * @return String
     */
    public static URI getAreaContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions
                            .TEXT_AREA_CONTAINER_KEY).pathType(4)
                            .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getLineContainerURI.
     *
     * @param map Map
     * @return String
     */
    public static URI getLineContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions
                            .TEXT_LINE_CONTAINER_KEY).pathType(4)
                            .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getWordContainerURI.
     *
     * @param map Map
     * @return String
     */
    public static URI getWordContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions
                            .TEXT_WORD_CONTAINER_KEY).pathType(4)
                            .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getPageObjectURI.
     *
     * @param map Map
     * @param resourceID String
     * @return String
     */
    public static URI getPageObjectURI(final Map<String, BagInfoField> map, final String
            resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions
                    .TEXT_PAGE_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getAreaObjectURI.
     *
     * @param map Map
     * @param resourceID String
     * @return String
     */
    public static URI getAreaObjectURI(final Map<String, BagInfoField> map, final String
            resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions
                    .TEXT_AREA_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getLineObjectURI.
     *
     * @param resourceID String
     * @param map Map
     * @return String
     */
    public static URI getLineObjectURI(final Map<String, BagInfoField> map, final String
            resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions
                    .TEXT_LINE_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * getWordObjectURI.
     *
     * @param resourceID String
     * @param map Map
     * @return String
     */
    public static URI getWordObjectURI(final Map<String, BagInfoField> map, final String
            resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions
                    .TEXT_WORD_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}


