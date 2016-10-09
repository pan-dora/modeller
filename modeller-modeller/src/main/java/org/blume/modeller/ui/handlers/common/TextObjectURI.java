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

public class TextObjectURI {
    protected static final Logger log = LoggerFactory.getLogger(TextObjectURI.class);
    public TextObjectURI() {}

    public static String gethOCRResourceURI(Map<String, BagInfoField> map) {
        return getMapValue(map, ProfileOptions.TEXT_HOCR_RESOURCE_KEY);
    }

    public static URI getPageContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_PAGE_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getAreaContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_AREA_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getLineContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_LINE_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getWordContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_WORD_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getPageObjectURI(Map<String, BagInfoField> map, String resourceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_PAGE_CONTAINER_KEY)
                    .resource(resourceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getAreaObjectURI(Map<String, BagInfoField> map, String resourceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_AREA_CONTAINER_KEY)
                    .resource(resourceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static URI getLineObjectURI(Map<String, BagInfoField> map, String resourceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_LINE_CONTAINER_KEY)
                    .resource(resourceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}


