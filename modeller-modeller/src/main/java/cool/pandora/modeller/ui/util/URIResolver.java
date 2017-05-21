package cool.pandora.modeller.ui.util;

import cool.pandora.modeller.ProfileOptions;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.common.uri.IIIFPathTemplate;
import cool.pandora.modeller.common.uri.Type;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * URIResolver
 *
 * @author Christopher Johnson
 */
public class URIResolver {
    private final URI resolvedURI;

    URIResolver(final URI resolvedURI) {
        this.resolvedURI = resolvedURI;
    }

    /**
     * @return resolvedURI
     */
    public static URIResolver.ContainerURIResolverNormal resolve() {
        return new URIResolver.ContainerURIResolverNormal();
    }

    /**
     * @return resolvedURI
     */
    public URI render() {
        return this.resolvedURI;
    }

    public static class ContainerURIResolverNormal {
        private Map<String, BagInfoField> map;
        private String containerKey;
        private String resource;
        private int pathType;

        ContainerURIResolverNormal() {
        }

        /**
         * @param map Map
         * @return this
         */
        public URIResolver.ContainerURIResolverNormal map(final Map<String, BagInfoField> map) {
            this.map = map;
            return this;
        }

        /**
         * @param pathType int
         * @return pathType
         */
        public URIResolver.ContainerURIResolverNormal pathType(final int pathType) {
            this.pathType = pathType;
            return this;
        }

        /**
         * @param containerKey String
         * @return this
         */
        public URIResolver.ContainerURIResolverNormal containerKey(final String containerKey) {
            this.containerKey = containerKey;
            return this;
        }

        /**
         * @param resource String
         * @return this
         */
        public URIResolver.ContainerURIResolverNormal resource(final String resource) {
            this.resource = resource;
            return this;
        }

        /**
         * @param map Map
         * @param key String
         * @return IIIFProfileKey
         */
        public static String getMapValue(final Map<String, BagInfoField> map, final String key) {
            final BagInfoField IIIFProfileKey = map.get(key);
            return IIIFProfileKey.getValue();
        }

        /**
         * @param value int
         * @return Type
         */
        static Type getObjectIDPath(final int value) {
            final Type templates = IIIFPathTemplate.BASE_URI_PATH;
            return Type.getByValue(IIIFPathTemplate.class, value);
        }

        /**
         * @param map          Map
         * @param containerKey String
         * @param resource     String
         * @param pathType     int
         * @return URI
         */
        static URI resolveURI(final Map<String, BagInfoField> map, final String containerKey, final String resource,
                              final int pathType) {
            final String hostname = getMapValue(map, ProfileOptions.FEDORA_HOSTNAME_KEY);
            Integer port = Integer.parseInt(getMapValue(map, ProfileOptions.PORT_KEY));
            if (port == 80) {
                port = -1;
            }
            final String appKey = getMapValue(map, ProfileOptions.FEDORA_APP_KEY);
            final String restKey = getMapValue(map, ProfileOptions.REST_SERVLET_KEY);
            final String collectionRoot = getMapValue(map, ProfileOptions.COLLECTION_ROOT_KEY);
            final String collection = getMapValue(map, ProfileOptions.COLLECTION_ID_KEY);
            final String objektID = getMapValue(map, ProfileOptions.OBJEKT_ID_KEY);
            final String manifestLabel = getMapValue(map, ProfileOptions.MANIFEST_RESOURCE_LABEL);
            final Type path = getObjectIDPath(pathType);
            final UriBuilder builder = UriBuilder.fromPath(path.toString());
            switch (pathType) {
                case (0):
                    return builder.scheme("http").host(hostname).port(port).build(appKey, restKey);
                case (1):
                    return builder.scheme("http").host(hostname).port(port).build(appKey, restKey, collectionRoot);
                case (2):
                    return builder.scheme("http").host(hostname).port(port)
                            .build(appKey, restKey, collectionRoot, collection);
                case (3):
                    return builder.scheme("http").host(hostname).port(port)
                            .build(appKey, restKey, collectionRoot, collection, objektID);
                case (4):
                    final String container4 = getMapValue(map, containerKey);
                    return builder.scheme("http").host(hostname).port(port)
                            .build(appKey, restKey, collectionRoot, collection, objektID, container4);
                case (5):
                    final String container5 = getMapValue(map, containerKey);
                    return builder.scheme("http").host(hostname).port(port)
                            .build(appKey, restKey, collectionRoot, collection, objektID, container5, resource);
                case (6):
                    return builder.scheme("http").host(hostname).port(port)
                            .build(appKey, restKey, collectionRoot, collection, objektID, manifestLabel);
            }
            return null;
        }

        /**
         * @return URIResolver
         * @throws URISyntaxException exception
         */
        public URIResolver build() throws URISyntaxException {
            final URI resolvedURI = resolveURI(this.map, this.containerKey, this.resource, this.pathType);
            return new URIResolver(resolvedURI);
        }
    }


}
