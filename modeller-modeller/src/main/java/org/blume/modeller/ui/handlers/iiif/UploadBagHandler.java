package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.BaggerFileEntity;
import org.blume.modeller.ui.util.URIResolver;
import org.blume.modeller.util.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.jpanel.base.UploadBagFrame;
import org.blume.modeller.ModellerClient;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;


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
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        ImageIOUtil imageioutil = new ImageIOUtil();
        String basePath = AbstractBagConstants.DATA_DIRECTORY;
        Path rootDir = bagView.getBagRootPath().toPath();
        for (String filePath : payload) {
            String filename = BaggerFileEntity.removeBasePath(basePath, filePath);
            URI destinationURI = getDestinationURI(map, filename);
            Path absoluteFilePath = rootDir.resolve(filePath);
            File resourceFile = absoluteFilePath.toFile();
            String contentType = imageioutil.getImageMIMEType(resourceFile);
            try {
                client.doBinaryPut(destinationURI, resourceFile, contentType);
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
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

    public URI getResourceContainerURI(Map<String, BagInfoField> map) {
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

    private URI getDestinationURI(Map<String, BagInfoField> map, String filename) {
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
}