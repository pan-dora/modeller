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
import org.blume.modeller.ui.UploadBagFrame;
import org.blume.modeller.ModellerClient;
import org.fcrepo.client.FcrepoOperationFailedException;

public class UploadBagHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private UploadBagFrame uploadBagFrame;
    DefaultBag bag;
    private BagView bagView;
    List<String> payload = null;
    HashMap<String, BagInfoField> map;
    BagInfoField baseURI, collectionRoot, objektID, IIIFResourceContainer;
    private File tmpRootPath;
    private boolean clearAfterSaving = false;
    private String messages;

    public UploadBagHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
    }

    public void upload(File file) {
        DefaultBag bag = bagView.getBag();
        payload = bag.getPayloadPaths();
        map = bag.getInfo().getFieldMap();
        String basePath = AbstractBagConstants.DATA_DIRECTORY;
        Path rootDir = bagView.getBagRootPath().toPath();
        for (Iterator<String> it = payload.iterator(); it.hasNext();) {
            String filePath = it.next();
            try {
                String normalPath = BaggerFileEntity.removeBasePath(basePath, filePath);
                String resourceContainer = getResourceContainer(map);
                String destinationURI = getDestinationURI(resourceContainer, normalPath);
                Path absoluteFilePath = rootDir.resolve(filePath);
                File bagResource = absoluteFilePath.toFile();
                ModellerClient client = new ModellerClient();
                client.doBinaryPut(destinationURI, bagResource);
            } catch (Exception e) {
                log.error("Failed to remove base path from {}", filePath, e);
            }
        }

        if (file == null) {
            file = bagView.getBagRootPath();
        }
        bag.setName(file.getName());
        bagView.getControl().invalidate();
    }

    public void openUploadBagFrame() {
        bag = bagView.getBag();
        uploadBagFrame = new UploadBagFrame(bagView, bagView.getPropertyMessage("bag.frame.upload"));
        uploadBagFrame.setBag(bag);
        uploadBagFrame.setVisible(true);
    }

    public String getDestinationURI(String resourceContainer, String normalPath) {
        String destinationURI = new StringBuilder(resourceContainer)
                .append(normalPath)
                .toString();
        return destinationURI;
    }

    public String getResourceContainer(HashMap<String, BagInfoField> map) {
        baseURI = map.get("FedoraBaseURI");
        collectionRoot = map.get("CollectionRoot");
        objektID = map.get("ObjektID");
        IIIFResourceContainer = map.get("IIIFResourceContainer");
        String resourceContainer = new StringBuilder(baseURI.getValue())
                .append(collectionRoot.getValue())
                .append(objektID.getValue())
                .append(IIIFResourceContainer.getValue())
                .toString();
        return resourceContainer;
    }
}