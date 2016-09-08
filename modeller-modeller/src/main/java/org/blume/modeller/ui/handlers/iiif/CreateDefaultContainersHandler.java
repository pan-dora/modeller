package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.AbstractAction;

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
        client.doPut(collectionIDURI);
        String objektURI = getObjektURI(map);
        client.doPut(objektURI);

        String resourceContainer = getMapValue(map, "IIIFResourceContainer");
        String manifestContainer = getMapValue(map, "IIIFManifestContainer");
        String sequenceContainer = getMapValue(map, "IIIFSequenceContainer");
        String rangeContainer = getMapValue(map, "IIIFRangeContainer");
        String canvasContainer = getMapValue(map, "IIIFCanvasContainer");
        String listContainer = getMapValue(map, "IIIFListContainer");
        String layerContainer = getMapValue(map, "IIIFLayerContainer");
        String[] IIIFContainers = new String[]{resourceContainer, manifestContainer, sequenceContainer,
                rangeContainer, canvasContainer, listContainer, layerContainer};
        for (String container: IIIFContainers) {
            String containerURI = buildContainerURI(objektURI, container);
            client.doPut(containerURI);
            ApplicationContextUtil.addConsoleMessage(message + " " + containerURI);

        }
        bagView.getControl().invalidate();
    }

    public void openCreateDefaultContainersFrame() {
        DefaultBag bag = bagView.getBag();
        CreateDefaultContainersFrame createDefaultContainersFrame = new CreateDefaultContainersFrame(bagView, bagView.getPropertyMessage("bag.frame.put"));
        createDefaultContainersFrame.setBag(bag);
        createDefaultContainersFrame.setVisible(true);
    }

    public String getObjektURI(Map<String, BagInfoField> map) {
        BagInfoField baseURI = map.get("FedoraBaseURI");
        BagInfoField collectionRoot = map.get("CollectionRoot");
        BagInfoField collectionID = map.get("CollectionID");
        BagInfoField objektID = map.get("ObjektID");
        return baseURI.getValue() +
                collectionRoot.getValue() +
                collectionID.getValue() +
                objektID.getValue();
    }

    public String getCollectionIDURI(Map<String, BagInfoField> map) {
        BagInfoField baseURI = map.get("FedoraBaseURI");
        BagInfoField collectionRoot = map.get("CollectionRoot");
        BagInfoField collectionID = map.get("CollectionID");
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