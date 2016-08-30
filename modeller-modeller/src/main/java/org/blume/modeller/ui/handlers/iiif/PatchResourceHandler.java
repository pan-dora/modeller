package org.blume.modeller.ui.handlers.iiif;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageInputStream;
import com.twelvemonkeys.imageio.plugins.tiff.TIFFImageWriterSpi;

import javax.imageio.stream.ImageInputStream;
import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.apache.commons.io.IOUtils;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.BaggerFileEntity;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.ResourceTemplate;
import org.blume.modeller.ui.util.ImageIOUtil;
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
        ImageIOUtil imageioutil = new ImageIOUtil();
        String basePath = AbstractBagConstants.DATA_DIRECTORY;
        Path rootDir = bagView.getBagRootPath().toPath();

        for (String filePath : payload) {
            String filename = BaggerFileEntity.removeBasePath(basePath, filePath);
            String destinationURI = getDestinationURI(resourceContainer, filename);
            Path absoluteFilePath = rootDir.resolve(filePath);
            File resourceFile = absoluteFilePath.toFile();
            String formatName = null;
            Dimension dim = null;
            InputStream rdfBody = null;

            try {
                formatName = imageioutil.getImageMIMEType(resourceFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                dim = imageioutil.getImageDimensions(resourceFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dim != null) {
                double imgWidth = dim.getWidth();
                double imgHeight = dim.getHeight();
                try {
                    rdfBody = getResourceMetadata(map, filename, formatName, imgWidth, imgHeight);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                client.doPatch(destinationURI, rdfBody);
            } finally {
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            }
        }
        bagView.getControl().invalidate();
    }

    public void openPatchResourceFrame() {
        bag = bagView.getBag();
        PatchResourcesFrame = new PatchResourceFrame(bagView, bagView.getPropertyMessage("bag.frame.upload"));
        PatchResourcesFrame.setBag(bag);
        PatchResourcesFrame.setVisible(true);
    }

    public String getDestinationURI(String resourceContainer, String filename) {
        return resourceContainer +
                filename +
                FCRMETADATA;
    }

    public String getResourceContainer(HashMap<String, BagInfoField> map) {
        String baseURI = getMapValue(map, "FedoraBaseURI");
        String collectionRoot = getMapValue(map, "CollectionRoot");
        String objektID = getMapValue(map, "ObjektID");
        String IIIFResourceContainer = getMapValue(map, "IIIFResourceContainer");
        return baseURI +
                collectionRoot +
                objektID +
                IIIFResourceContainer;
    }

    public InputStream getResourceMetadata(HashMap<String, BagInfoField> map, String filename, String formatName,
                                           double imgWidth,
                                           double imgHeight) {
        String serviceURI = getMapValue(map, "IIIFServiceBaseURI");
        ResourceTemplate resourceTemplate;
        List<ResourceTemplate.Scope.Prefix> prefixes = Arrays.asList(
                new ResourceTemplate.Scope.Prefix(FedoraPrefixes.RDFS),
                new ResourceTemplate.Scope.Prefix(FedoraPrefixes.MODE));

        ResourceTemplate.Scope scope = new ResourceTemplate.Scope()
                .fedoraPrefixes(prefixes)
                .filename(filename)
                .serviceURI(getServiceURI(serviceURI, filename))
                .formatName(formatName)
                .imgHeight(imgHeight)
                .imgWidth(imgWidth);

        resourceTemplate = ResourceTemplate.template()
                .template("template/sparql-update.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = resourceTemplate.render();
        return IOUtils.toInputStream(metadata, UTF_8 );
    }

    public String getServiceURI(String serviceURI, String filename) {
        return serviceURI +
                filename;
    }

    public String getMapValue(HashMap<String, BagInfoField> map, String key) {
        IIIFProfileKey = map.get(key);
        return IIIFProfileKey.getValue();
    }

}