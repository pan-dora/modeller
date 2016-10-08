package org.blume.modeller.ui.handlers.common;


import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;

import java.util.Map;

import static org.blume.modeller.ui.util.URIResolver.ContainerURIResolverNormal.getMapValue;

public class TextObjectURI {
    public TextObjectURI() {}

    public static String gethOCRResourceURI(Map<String, BagInfoField> map) {
        return getMapValue(map, ProfileOptions.TEXT_HOCR_RESOURCE_KEY);
    }
}


