package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.BaggerFileEntity;
import org.blume.modeller.ui.util.ContainerIRIResolver;
import org.blume.modeller.util.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.jpanel.UploadBagFrame;
import org.blume.modeller.ModellerClient;


public class UploadBagHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(UploadBagHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public UploadBagHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.fileuploaded");
        DefaultBag bag = bagView.getBag();
        List<String> payload = bag.getPayloadPaths();
        HashMap<String, BagInfoField> map = bag.getInfo().getFieldMap();
        String resourceContainer = getResourceContainer(map);
        ModellerClient client = new ModellerClient();
        ImageIOUtil imageioutil = new ImageIOUtil();
        String basePath = AbstractBagConstants.DATA_DIRECTORY;
        Path rootDir = bagView.getBagRootPath().toPath();
        for (String filePath : payload) {
            String filename = BaggerFileEntity.removeBasePath(basePath, filePath);
            String destinationURI = getDestinationURI(resourceContainer, filename);
            Path absoluteFilePath = rootDir.resolve(filePath);
            File resourceFile = absoluteFilePath.toFile();
            String contentType = imageioutil.getImageMIMEType(resourceFile);
            try {
                client.doBinaryPut(destinationURI, resourceFile, contentType);
            } finally {
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            }
        }
        bagView.getControl().invalidate();
    }

    void openUploadBagFrame() {
        DefaultBag bag = bagView.getBag();
        UploadBagFrame uploadBagFrame = new UploadBagFrame(bagView, bagView.getPropertyMessage("bag.frame.upload"));
        uploadBagFrame.setBag(bag);
        uploadBagFrame.setVisible(true);
    }

    private String getDestinationURI(String resourceContainer, String filename) {
        return resourceContainer +
                filename;
    }

    public String getResourceContainer(HashMap<String, BagInfoField> map) {
        ContainerIRIResolver containerIRIResolver;
        containerIRIResolver = ContainerIRIResolver.resolve()
                .map(map)
                .baseURIKey("FedoraBaseURI")
                .collectionRootKey("CollectionRoot")
                .collectionKey("CollectionID")
                .objektIDKey("ObjektID")
                .containerKey("IIIFResourceContainer")
                .build();
        return containerIRIResolver.render();
    }
}