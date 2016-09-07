package org.blume.modeller.ui.handlers.iiif;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.apache.commons.io.IOUtils;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.BaggerFileEntity;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.ResourceScope;
import org.blume.modeller.templates.ResourceTemplate;
import org.blume.modeller.ui.util.ContainerIRIResolver;
import org.blume.modeller.util.ImageIOUtil;
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
    private BagView bagView;

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
        List<String> payload = bag.getPayloadPaths();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
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
        DefaultBag bag = bagView.getBag();
        PatchResourceFrame patchResourcesFrame = new PatchResourceFrame(bagView, bagView.getPropertyMessage("bag.frame.patch"));
        patchResourcesFrame.setBag(bag);
        patchResourcesFrame.setVisible(true);
    }

    public String getDestinationURI(String resourceContainer, String filename) {
        return resourceContainer +
                filename +
                FCRMETADATA;
    }

    public String getResourceContainer(Map<String, BagInfoField> map) {
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

    public InputStream getResourceMetadata(Map<String, BagInfoField> map, String filename, String formatName,
                                           double imgWidth,
                                           double imgHeight) {
        ResourceTemplate resourceTemplate;
        List<ResourceScope.Prefix> prefixes = Arrays.asList(
                new ResourceScope.Prefix(FedoraPrefixes.RDFS),
                new ResourceScope.Prefix(FedoraPrefixes.MODE));

        ResourceScope scope = new ResourceScope()
                .fedoraPrefixes(prefixes)
                .filename(filename)
                .serviceURI(getServiceURI(map, filename))
                .formatName(formatName)
                .imgHeight(imgHeight)
                .imgWidth(imgWidth);

        resourceTemplate = ResourceTemplate.template()
                .template("template/sparql-update-res.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = resourceTemplate.render();
        return IOUtils.toInputStream(metadata, UTF_8 );
    }

    public String getServiceURI(Map<String, BagInfoField> map, String filename) {
        String serviceURI = getMapValue(map, "IIIFServiceBaseURI");
        String collectionID = substringBeforeLast(getMapValue(map, "CollectionID"), "/");
        String objektID = substringBeforeLast(getMapValue(map, "ObjektID"), "/");
        return serviceURI + collectionID + "." + objektID + "." +
                filename;
    }

    public String getMapValue(Map<String, BagInfoField> map, String key) {
        BagInfoField IIIFProfileKey = map.get(key);
        return IIIFProfileKey.getValue();
    }

    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}