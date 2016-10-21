package org.blume.modeller.ui.handlers.common;

import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.ui.util.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.blume.modeller.ui.util.URIResolver.ContainerURIResolverNormal.getMapValue;

public class IIIFObjectURI {
    protected static final Logger log = LoggerFactory.getLogger(TextObjectURI.class);
    public IIIFObjectURI() {}

    public static URI buildContainerURI(Map<String, BagInfoField> map, String containerKey) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(containerKey)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getCollectionIdURI(Map<String, BagInfoField> map)  {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .pathType(2)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }


    public static URI getObjektURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .pathType(3)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getManifestResource(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .pathType(6)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getSequenceContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.SEQUENCE_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getSequenceObjectURI(Map<String, BagInfoField> map, String sequenceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.SEQUENCE_CONTAINER_KEY)
                    .resource(sequenceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getCanvasContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.CANVAS_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getCanvasObjectURI(Map<String, BagInfoField> map, String resourceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.CANVAS_CONTAINER_KEY)
                    .resource(resourceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getListContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.LIST_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }


    public static URI getListObjectURI(Map<String, BagInfoField> map, String resourceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.LIST_CONTAINER_KEY)
                    .resource(resourceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getResourceContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.RESOURCE_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getDestinationURI(Map<String, BagInfoField> map, String filename) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.RESOURCE_CONTAINER_KEY)
                    .resource(filename)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static String getListServiceBaseURI(Map<String, BagInfoField> map) {
        return getMapValue(map, ProfileOptions.LIST_SERVICE_KEY);
    }

}
