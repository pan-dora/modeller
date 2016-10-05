package org.blume.modeller.ui.handlers.iiif;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.blume.modeller.bag.BaggerFileEntity;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.BagView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceIdentifierList {
    private BagView bagView;

    public ResourceIdentifierList(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    public ArrayList<String> getResourceIdentifierList() {
        DefaultBag bag = bagView.getBag();
        List<String> payload = bag.getPayloadPaths();
        ArrayList<String> identifierList = new ArrayList<>();
        String basePath = AbstractBagConstants.DATA_DIRECTORY;
        for (String filePath : payload) {
            String filename = BaggerFileEntity.removeBasePath(basePath, filePath);
            String resourceID = BaggerFileEntity.removeFileExtension(filename);
            identifierList.add(resourceID);
        }
        Collections.sort(identifierList, String.CASE_INSENSITIVE_ORDER);
        return identifierList;
    }

}
