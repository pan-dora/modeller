package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.AbstractAction;

import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.jpanel.CreateDefaultContainersFrame;
import org.blume.modeller.ModellerClient;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class CreateDefaultContainersHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreateDefaultContainersHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.containercreated");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();

        ModellerClient client = new ModellerClient();
        String collectionIDURI = getCollectionIDURI(map);
        String objektURI = getObjektURI(map);

        try {
        client.doPut(collectionIDURI);
        } catch (ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
        try {
        client.doPut(objektURI);
        } catch (ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }

        String resourceContainer = getMapValue(map, ProfileOptions.RESOURCE_CONTAINER_KEY);
        String manifestContainer = getMapValue(map, ProfileOptions.MANIFEST_CONTAINER_KEY);
        String sequenceContainer = getMapValue(map, ProfileOptions.SEQUENCE_CONTAINER_KEY);
        String rangeContainer = getMapValue(map, ProfileOptions.RANGE_CONTAINER_KEY);
        String canvasContainer = getMapValue(map, ProfileOptions.CANVAS_CONTAINER_KEY);
        String listContainer = getMapValue(map, ProfileOptions.LIST_CONTAINER_KEY);
        String layerContainer = getMapValue(map, ProfileOptions.LAYER_CONTAINER_KEY);
        String[] IIIFContainers = new String[]{resourceContainer, manifestContainer, sequenceContainer,
                rangeContainer, canvasContainer, listContainer, layerContainer};
        for (String container: IIIFContainers) {
            String containerURI = buildContainerURI(objektURI, container);
            try {
                client.doPut(containerURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + containerURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateDefaultContainersFrame() {
        DefaultBag bag = bagView.getBag();
        CreateDefaultContainersFrame createDefaultContainersFrame = new CreateDefaultContainersFrame(bagView, bagView.getPropertyMessage("bag.frame.put"));
        createDefaultContainersFrame.setBag(bag);
        createDefaultContainersFrame.setVisible(true);
    }

    public String getObjektURI(Map<String, BagInfoField> map) {
        BagInfoField baseURI = map.get(ProfileOptions.FEDORA_BASE_KEY);
        BagInfoField collectionRoot = map.get(ProfileOptions.COLLECTION_ROOT_KEY);
        BagInfoField collectionID = map.get(ProfileOptions.COLLECTION_ID_KEY);
        BagInfoField objektID = map.get(ProfileOptions.OBJEKT_ID_KEY);
        return baseURI.getValue() +
                collectionRoot.getValue() +
                collectionID.getValue() +
                objektID.getValue();
    }

    private String getCollectionIDURI(Map<String, BagInfoField> map) {
        BagInfoField baseURI = map.get(ProfileOptions.FEDORA_BASE_KEY);
        BagInfoField collectionRoot = map.get(ProfileOptions.COLLECTION_ROOT_KEY);
        BagInfoField collectionID = map.get(ProfileOptions.COLLECTION_ID_KEY);
        return baseURI.getValue() +
                collectionRoot.getValue() +
                collectionID.getValue();
    }

    private String getMapValue(Map<String, BagInfoField> map, String key) {
        BagInfoField IIIFResourceContainer = map.get(key);
        return IIIFResourceContainer.getValue();
    }

    private String buildContainerURI(String objektURI, String container) {
        return objektURI +
                container;
    }
}