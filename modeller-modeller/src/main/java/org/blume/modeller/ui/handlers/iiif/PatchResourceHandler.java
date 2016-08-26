package org.blume.modeller.ui.handlers.iiif;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.apache.commons.io.IOUtils;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.BaggerFileEntity;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.ResourceTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.jpanel.PatchResourceFrame;
import org.blume.modeller.ModellerClient;
import static org.blume.modeller.common.uri.FedoraResources.FCRMETADATA;
import static java.nio.charset.StandardCharsets.UTF_8;

public class PatchResourceHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchResourceHandler.class);
    private static final long serialVersionUID = 1L;
    private PatchResourceFrame PatchResourcesFrame;
    DefaultBag bag;
    private BagView bagView;
    List<String> payload = null;
    HashMap<String, BagInfoField> map;
    BagInfoField IIIFProfileKey;

    public PatchResourceHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.resourcepatched");
        DefaultBag bag = bagView.getBag();
        payload = bag.getPayloadPaths();
        map = bag.getInfo().getFieldMap();
        String resourceContainer = getResourceContainer(map);
        ModellerClient client = new ModellerClient();
        String basePath = AbstractBagConstants.DATA_DIRECTORY;
        for (Iterator<String> it = payload.iterator(); it.hasNext();) {
            String filePath = it.next();
            String normalPath = BaggerFileEntity.removeBasePath(basePath, filePath);
            String destinationURI = getDestinationURI(resourceContainer, normalPath);
            InputStream rdfBody = getResourceMetadata(map, normalPath);
            client.doPatch(destinationURI, rdfBody);
            ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
        }
        bagView.getControl().invalidate();
    }

    public void openPatchResourceFrame() {
        bag = bagView.getBag();
        PatchResourcesFrame = new PatchResourceFrame(bagView, bagView.getPropertyMessage("bag.frame.upload"));
        PatchResourcesFrame.setBag(bag);
        PatchResourcesFrame.setVisible(true);
    }

    public String getDestinationURI(String resourceContainer, String normalPath) {
        String destinationURI = new StringBuilder(resourceContainer)
                .append(normalPath)
                .append(FCRMETADATA)
                .toString();
        return destinationURI;
    }

    public String getResourceContainer(HashMap<String, BagInfoField> map) {
        String baseURI = getMapValue(map, "FedoraBaseURI");
        String collectionRoot = getMapValue(map, "CollectionRoot");
        String objektID = getMapValue(map, "ObjektID");
        String IIIFResourceContainer = getMapValue(map, "IIIFResourceContainer");
        String resourceContainer = new StringBuilder(baseURI)
                .append(collectionRoot)
                .append(objektID)
                .append(IIIFResourceContainer)
                .toString();
        return resourceContainer;
    }

    public InputStream getResourceMetadata(HashMap<String, BagInfoField> map, String normalPath) {
        String serviceURI = getMapValue(map, "IIIFServiceBaseURI");
        ResourceTemplate resourceTemplate;
        List<ResourceTemplate.Scope.Prefix> prefixes = Arrays.asList(
                new ResourceTemplate.Scope.Prefix(FedoraPrefixes.RDFS),
                new ResourceTemplate.Scope.Prefix(FedoraPrefixes.MODE));

        ResourceTemplate.Scope scope = new ResourceTemplate.Scope()
                .fedoraPrefixes(prefixes)
                .serviceURI(getServiceURI(serviceURI, normalPath));

        resourceTemplate = ResourceTemplate.template()
                .template("template/sparql-update.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = resourceTemplate.render();
        InputStream input = IOUtils.toInputStream(metadata, UTF_8 );
        return input;
    }

    public String getServiceURI(String serviceURI, String normalPath) {
        String serviceURIPath = new StringBuilder(serviceURI)
                .append(normalPath)
                .toString();
        return serviceURIPath;
    }

    public String getMapValue(HashMap<String, BagInfoField> map, String key) {
        IIIFProfileKey = map.get(key);
        String profileKeyValue = new StringBuilder(IIIFProfileKey.getValue())
                .toString();
        return profileKeyValue;
    }

    public Dimension getImageDimensions(Object resourceFile) throws IOException {
        try (ImageInputStream in = ImageIO.createImageInputStream(resourceFile)){
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        }
        return null;
    }
}