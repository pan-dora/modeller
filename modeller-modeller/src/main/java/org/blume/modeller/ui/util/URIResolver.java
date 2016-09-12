package org.blume.modeller.ui.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.common.uri.IIIFPathTemplate;
import org.blume.modeller.common.uri.Type;

public class URIResolver {
    private URI resolvedURI;

    public URIResolver(URI resolvedURI) {
        this.resolvedURI = resolvedURI;
    }

    public static URIResolver.ContainerURIResolverNormal resolve() {
        return new URIResolver.ContainerURIResolverNormal();
    }

    public URI render() {
        return this.resolvedURI;
    }

    public static class ContainerURIResolverNormal {
        private Map<String, BagInfoField> map;
        private String containerKey;
        private String resource;
        private int pathType;

        ContainerURIResolverNormal() {}

        public URIResolver.ContainerURIResolverNormal map(Map<String, BagInfoField> map) {
            this.map = map;
            return this;
        }

        public URIResolver.ContainerURIResolverNormal pathType(int pathType) {
            this.pathType = pathType;
            return this;
        }

        public URIResolver.ContainerURIResolverNormal containerKey(String containerKey) {
            this.containerKey = containerKey;
            return this;
        }

        public URIResolver.ContainerURIResolverNormal resource(String resource) {
            this.resource = resource;
            return this;
        }

        private static String getMapValue(Map<String, BagInfoField> map, String key) {
            BagInfoField IIIFProfileKey = map.get(key);
            return IIIFProfileKey.getValue();
        }

        public static Type getObjectIDPath(int value)  {
            Type templates = IIIFPathTemplate.BASE_URI_PATH;
            return Type.getByValue(IIIFPathTemplate.class, value);
        }

        static URI resolveURI(Map<String, BagInfoField> map, String containerKey, String resource, int pathType) {
            String hostname = getMapValue(map, ProfileOptions.FEDORA_HOSTNAME_KEY);
            int port = Integer.parseInt(getMapValue(map, ProfileOptions.PORT_KEY));
            String appKey =  getMapValue(map, ProfileOptions.FEDORA_APP_KEY);
            String restKey = getMapValue(map, ProfileOptions.REST_SERVLET_KEY);
            String collectionRoot = getMapValue(map, ProfileOptions.COLLECTION_ROOT_KEY);
            String collection = getMapValue(map, ProfileOptions.COLLECTION_ID_KEY);
            String objektID = getMapValue(map, ProfileOptions.OBJEKT_ID_KEY);
            Type path = getObjectIDPath(pathType);
            UriBuilder builder = UriBuilder.fromPath(path.toString());
            switch (pathType) {
                case (0):
                    return builder
                            .scheme("http")
                            .host(hostname)
                            .port(port)
                            .build(appKey, restKey);
                case(1):
                    return builder
                            .scheme("http")
                            .host(hostname)
                            .port(port)
                            .build(appKey, restKey, collectionRoot);
                case(2):
                    return builder
                            .scheme("http")
                            .host(hostname)
                            .port(port)
                            .build(appKey, restKey, collectionRoot, collection);
                case(3):
                    return builder
                            .scheme("http")
                            .host(hostname)
                            .port(port)
                            .build(appKey, restKey, collectionRoot, collection, objektID);
                case(4):
                    String container = getMapValue(map, containerKey);
                    return builder
                            .scheme("http")
                            .host(hostname)
                            .port(port)
                            .build(appKey, restKey, collectionRoot, collection, objektID, container);
                case(5):
                    container = getMapValue(map, containerKey);
                    return builder
                            .scheme("http")
                            .host(hostname)
                            .port(port)
                            .build(appKey, restKey, collectionRoot, collection, objektID, container, resource);
            }
          return null;
        }

        public URIResolver build() throws URISyntaxException {
            URI resolvedURI = resolveURI(this.map, this.containerKey, this.resource, this.pathType);
            return new URIResolver(resolvedURI);
        }
    }


}
