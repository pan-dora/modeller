package org.blume.modeller.ui.handlers.iiif;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.apache.commons.io.IOUtils;
import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.BaggerFileEntity;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.ResourceScope;
import org.blume.modeller.templates.MetadataTemplate;
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

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
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
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchResourceFrame() {
        DefaultBag bag = bagView.getBag();
        PatchResourceFrame patchResourcesFrame = new PatchResourceFrame(bagView, bagView.getPropertyMessage("bag.frame.patch"));
        patchResourcesFrame.setBag(bag);
        patchResourcesFrame.setVisible(true);
    }

    private String getDestinationURI(String resourceContainer, String filename) {
        return resourceContainer +
                filename +
                FCRMETADATA;
    }

    public String getResourceContainer(Map<String, BagInfoField> map) {
        ContainerIRIResolver containerIRIResolver;
        containerIRIResolver = ContainerIRIResolver.resolve()
                .map(map)
                .baseURIKey(ProfileOptions.FEDORA_BASE_KEY)
                .collectionRootKey(ProfileOptions.COLLECTION_ROOT_KEY)
                .collectionKey(ProfileOptions.COLLECTION_ID_KEY)
                .objektIDKey(ProfileOptions.OBJEKT_ID_KEY)
                .containerKey(ProfileOptions.RESOURCE_CONTAINER_KEY)
                .build();
        return containerIRIResolver.render();
    }

    private InputStream getResourceMetadata(Map<String, BagInfoField> map, String filename, String formatName,
                                            double imgWidth,
                                            double imgHeight) {
        MetadataTemplate metadataTemplate;
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

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-res.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = metadataTemplate.render();
        return IOUtils.toInputStream(metadata, UTF_8 );
    }

    private String getServiceURI(Map<String, BagInfoField> map, String filename) {
        String serviceURI = getMapValue(map, ProfileOptions.IIIF_SERVICE_KEY);
        String collectionID = substringBeforeLast(getMapValue(map, ProfileOptions.COLLECTION_ID_KEY), "/");
        String objektID = substringBeforeLast(getMapValue(map, ProfileOptions.OBJEKT_ID_KEY), "/");
        return serviceURI + collectionID + "." + objektID + "." +
                BaggerFileEntity.removeFileExtension(filename);
    }

    private String getMapValue(Map<String, BagInfoField> map, String key) {
        BagInfoField IIIFProfileKey = map.get(key);
        return IIIFProfileKey.getValue();
    }

    private static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}