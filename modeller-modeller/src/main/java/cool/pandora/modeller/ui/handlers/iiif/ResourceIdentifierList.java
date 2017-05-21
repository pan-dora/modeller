package cool.pandora.modeller.ui.handlers.iiif;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import cool.pandora.modeller.bag.BaggerFileEntity;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;

import java.util.ArrayList;
import java.util.List;

/**
 * Resource Identifier List
 *
 * @author Christopher Johnson
 */
class ResourceIdentifierList {
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    ResourceIdentifierList(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    ArrayList<String> getResourceIdentifierList() {
        final DefaultBag bag = bagView.getBag();
        final List<String> payload = bag.getPayloadPaths();
        final ArrayList<String> identifierList = new ArrayList<>();
        final String basePath = AbstractBagConstants.DATA_DIRECTORY;
        for (final String filePath : payload) {
            final String filename = BaggerFileEntity.removeBasePath(basePath, filePath);
            final String resourceID = BaggerFileEntity.removeFileExtension(filename);
            identifierList.add(resourceID);
        }
        identifierList.sort(String.CASE_INSENSITIVE_ORDER);
        return identifierList;
    }

}
