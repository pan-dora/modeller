/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller.ui.handlers.iiif;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.apache.commons.io.IOUtils;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.ProfileOptions;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.BaggerFileEntity;
import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.common.uri.IIIFPrefixes;
import cool.pandora.modeller.templates.ResourceScope;
import cool.pandora.modeller.templates.MetadataTemplate;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.util.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ui.jpanel.iiif.PatchResourceFrame;
import cool.pandora.modeller.ModellerClient;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static cool.pandora.modeller.common.uri.FedoraResources.FCRMETADATA;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Patch Resource Handler
 *
 * @author Christopher Johnson
 */
public class PatchResourceHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchResourceHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchResourceHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.resourcepatched");
        final DefaultBag bag = bagView.getBag();
        final List<String> payload = bag.getPayloadPaths();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final URI resourceContainer = IIIFObjectURI.getResourceContainerURI(map);
        final String basePath = AbstractBagConstants.DATA_DIRECTORY;
        final Path rootDir = bagView.getBagRootPath().toPath();

        for (final String filePath : payload) {
            final String filename = BaggerFileEntity.removeBasePath(basePath, filePath);
            final String destinationURI = getDestinationURI(resourceContainer, filename);
            final Path absoluteFilePath = rootDir.resolve(filePath);
            final File resourceFile = absoluteFilePath.toFile();
            String formatName = null;
            Dimension dim = null;
            InputStream rdfBody = null;
            final URI uri = URI.create(destinationURI);
            try {
                formatName = ImageIOUtil.getImageMIMEType(resourceFile);
            } catch (final Exception e) {
                e.printStackTrace();
            }

            try {
                dim = ImageIOUtil.getImageDimensions(resourceFile);
            } catch (final Exception e) {
                e.printStackTrace();
            }

            if (dim != null) {
                final double imgWidth = dim.getWidth();
                final int iw = (int) imgWidth;
                final double imgHeight = dim.getHeight();
                final int ih = (int) imgHeight;
                try {
                    rdfBody = getResourceMetadata(map, filename, formatName, iw, ih);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                ModellerClient.doPatch(uri, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchResourceFrame() {
        final DefaultBag bag = bagView.getBag();
        final PatchResourceFrame patchResourcesFrame =
                new PatchResourceFrame(bagView, bagView.getPropertyMessage("bag" + ".frame.patch"));
        patchResourcesFrame.setBag(bag);
        patchResourcesFrame.setVisible(true);
    }

    private static String getDestinationURI(final URI resourceContainer, final String filename) {
        return resourceContainer.toString() + filename + FCRMETADATA;
    }

    private static InputStream getResourceMetadata(final Map<String, BagInfoField> map, final
    String filename,
                                                   final String formatName, final int iw, final
                                                   int ih) {
        final MetadataTemplate metadataTemplate;
        final List<ResourceScope.Prefix> prefixes = Arrays.asList(new ResourceScope.Prefix
                        (FedoraPrefixes.RDFS),
                new ResourceScope.Prefix(FedoraPrefixes.MODE), new ResourceScope.Prefix
                        (IIIFPrefixes.DC),
                new ResourceScope.Prefix(IIIFPrefixes.SVCS), new ResourceScope.Prefix
                        (IIIFPrefixes.EXIF),
                new ResourceScope.Prefix(IIIFPrefixes.DCTYPES));

        final ResourceScope scope =
                new ResourceScope().fedoraPrefixes(prefixes).filename(filename).serviceURI
                        (getServiceURI(map, filename))
                        .formatName(formatName).imgHeight(ih).imgWidth(iw);

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-res" +
                ".mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String metadata = metadataTemplate.render();
        return IOUtils.toInputStream(metadata, UTF_8);
    }

    private static String getServiceURI(final Map<String, BagInfoField> map, final String
            filename) {
        final String separator = "_";
        final String serviceURI = getMapValue(map, ProfileOptions.IIIF_SERVICE_KEY);
        final String path = getMapValue(map, ProfileOptions.COLLECTION_ROOT_KEY) + separator +
                getMapValue(map, ProfileOptions.COLLECTION_ID_KEY) + separator +
                getMapValue(map, ProfileOptions.OBJEKT_ID_KEY) + separator +
                getMapValue(map, ProfileOptions.RESOURCE_CONTAINER_KEY) + separator + filename;
        return serviceURI + path.replace("tif", "jp2");
    }

    private static String getMapValue(final Map<String, BagInfoField> map, final String key) {
        final BagInfoField IIIFProfileKey = map.get(key);
        return IIIFProfileKey.getValue();
    }

}