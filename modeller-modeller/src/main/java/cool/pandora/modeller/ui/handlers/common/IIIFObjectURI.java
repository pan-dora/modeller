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
 * IIIF Object URI
 *
 * @author Christopher Johnson
 */
public final class IIIFObjectURI {

    private IIIFObjectURI() {
    }

    protected static final Logger log = LoggerFactory.getLogger(TextObjectURI.class);

    /**
     * @param map          Map
     * @param containerKey String
     * @return URI
     */
    public static URI buildContainerURI(final Map<String, BagInfoField> map, final String containerKey) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(containerKey).pathType(4).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
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
     * @param map Map
     * @return URI
     */
    public static URI getSequenceContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.SEQUENCE_CONTAINER_KEY).pathType(4)
                    .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map        Map
     * @param sequenceID String
     * @return URI
     */
    public static URI getSequenceObjectURI(final Map<String, BagInfoField> map, final String sequenceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.SEQUENCE_CONTAINER_KEY)
                    .resource(sequenceID).pathType(5).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return URI
     */
    public static URI getCanvasContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.CANVAS_CONTAINER_KEY).pathType(4)
                    .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map        Map
     * @param resourceID String
     * @return URI
     */
    public static URI getCanvasObjectURI(final Map<String, BagInfoField> map, final String resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.CANVAS_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return URI
     */
    public static URI getListContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.LIST_CONTAINER_KEY).pathType(4).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map        Map
     * @param resourceID String
     * @return URI
     */
    public static URI getListObjectURI(final Map<String, BagInfoField> map, final String resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions.LIST_CONTAINER_KEY).resource(resourceID)
                            .pathType(5).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map Map
     * @return URI
     */
    public static URI getResourceContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.RESOURCE_CONTAINER_KEY).pathType(4)
                    .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * @param map      Map
     * @param filename String
     * @return URI
     */
    public static URI getDestinationURI(final Map<String, BagInfoField> map, final String filename) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.RESOURCE_CONTAINER_KEY)
                    .resource(filename).pathType(5).build();
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
    public static String getListServiceBaseURI(final Map<String, BagInfoField> map) {
        return getMapValue(map, ProfileOptions.LIST_SERVICE_KEY);
    }

}
