package cool.pandora.modeller.ui.handlers.common;


import cool.pandora.modeller.ProfileOptions;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.ui.util.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static cool.pandora.modeller.ui.util.URIResolver.ContainerURIResolverNormal.getMapValue;

/**
 * Text Object URI
 *
 * @author Christopher Johnson
 */
public class TextObjectURI {
    protected static final Logger log = LoggerFactory.getLogger(TextObjectURI.class);

    private TextObjectURI() {
    }

    /**
     * @param map Map
     * @return String
     */
    public static String gethOCRResourceURI(final Map<String, BagInfoField> map) {
        return getMapValue(map, ProfileOptions.TEXT_HOCR_RESOURCE_KEY);
    }

    /**
     * @param map Map
     * @return String
     */
    public static URI getPageContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.TEXT_PAGE_CONTAINER_KEY).pathType(4)
                            .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return String
     */
    public static URI getAreaContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.TEXT_AREA_CONTAINER_KEY).pathType(4)
                            .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return String
     */
    public static URI getLineContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.TEXT_LINE_CONTAINER_KEY).pathType(4)
                            .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return String
     */
    public static URI getWordContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.TEXT_WORD_CONTAINER_KEY).pathType(4)
                            .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return String
     */
    public static URI getPageObjectURI(final Map<String, BagInfoField> map, final String resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.TEXT_PAGE_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return String
     */
    public static URI getAreaObjectURI(final Map<String, BagInfoField> map, final String resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.TEXT_AREA_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return String
     */
    public static URI getLineObjectURI(final Map<String, BagInfoField> map, final String resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.TEXT_LINE_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return String
     */
    public static URI getWordObjectURI(final Map<String, BagInfoField> map, final String resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.TEXT_WORD_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}


