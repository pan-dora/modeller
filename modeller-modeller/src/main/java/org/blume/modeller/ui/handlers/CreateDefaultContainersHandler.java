package org.blume.modeller.ui.handlers;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.BaggerFileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.CreateDefaultContainersFrame;
import org.blume.modeller.ModellerClient;

public class CreateDefaultContainersHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private CreateDefaultContainersFrame createDefaultContainersFrame;
    DefaultBag bag;
    private BagView bagView;
    List<String> payload = null;
    HashMap<String, BagInfoField> map;
    BagInfoField baseURI, collectionRoot, objektID, IIIFResourceContainer;
    private File tmpRootPath;
    private boolean clearAfterSaving = false;
    private String messages;

    public CreateDefaultContainersHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
    }

    public void create() {
        DefaultBag bag = bagView.getBag();
        payload = bag.getPayloadPaths();
        map = bag.getInfo().getFieldMap();

        ModellerClient client = new ModellerClient();
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
        }
        bagView.getControl().invalidate();
    }

    public void openCreateDefaultContainersFrame() {
        bag = bagView.getBag();
        createDefaultContainersFrame = new CreateDefaultContainersFrame(bagView, bagView.getPropertyMessage("bag.frame.put"));
        createDefaultContainersFrame.setBag(bag);
        createDefaultContainersFrame.setVisible(true);
    }

    public String getObjektURI(HashMap<String, BagInfoField> map) {
        baseURI = map.get("FedoraBaseURI");
        collectionRoot = map.get("CollectionRoot");
        objektID = map.get("ObjektID");
        String objektURI = new StringBuilder(baseURI.getValue())
                .append(collectionRoot.getValue())
                .append(objektID.getValue())
                .toString();
        return objektURI;
    }

    public String getMapValue(HashMap<String, BagInfoField> map, String key) {
        IIIFResourceContainer = map.get(key);
        String resourceContainer = new StringBuilder(IIIFResourceContainer.getValue())
                .toString();
        return resourceContainer;
    }

    public String buildContainerURI(String objektURI, String container) {
        String containerURI = new StringBuilder(objektURI)
                .append(container)
                .toString();
        return containerURI;
    }
}