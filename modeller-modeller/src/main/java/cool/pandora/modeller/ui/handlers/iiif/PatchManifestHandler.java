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

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import javax.swing.AbstractAction;

import org.apache.commons.io.IOUtils;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.ManifestPropertiesImpl;
import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.templates.ManifestScope;
import cool.pandora.modeller.templates.MetadataTemplate;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.jpanel.iiif.PatchManifestFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ModellerClient;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Patch Manifest Handler
 *
 * @author Christopher Johnson
 */
public class PatchManifestHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchManifestHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchManifestHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.manifestpatched");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();

        final URI collectionIdURI = IIIFObjectURI.getCollectionIdURI(map);
        final URI sequenceIdURI = IIIFObjectURI.getSequenceObjectURI(map, "normal");

        final InputStream rdfBody;
        rdfBody = getManifestMetadata(collectionIdURI, sequenceIdURI, map);
        final URI destinationURI = IIIFObjectURI.getManifestResource(map);
        try {
            ModellerClient.doPatch(destinationURI, rdfBody);
            ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
        } catch (final ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
        bagView.getControl().invalidate();
    }

    void openPatchManifestFrame() {
        final DefaultBag bag = bagView.getBag();
        final PatchManifestFrame patchManifestFrame =
                new PatchManifestFrame(bagView, bagView.getPropertyMessage("bag.frame.patch.manifest"));
        patchManifestFrame.setBag(bag);
        patchManifestFrame.setVisible(true);
    }

    private static InputStream getManifestMetadata(final URI collectionIdURI, final URI sequenceIdURI,
                                                   final Map<String, BagInfoField> map) {

        final MetadataTemplate metadataTemplate;
        final List<ManifestScope.Prefix> prefixes = Arrays.asList(new ManifestScope.Prefix(FedoraPrefixes.RDFS),
                new ManifestScope.Prefix(FedoraPrefixes.MODE));

        final String label = getMapValue(map, ManifestPropertiesImpl.FIELD_LABEL);
        final String attribution = getMapValue(map, ManifestPropertiesImpl.FIELD_ATTRIBUTION);
        final String license = getMapValue(map, ManifestPropertiesImpl.FIELD_LICENSE);
        final String rendering = getMapValue(map, ManifestPropertiesImpl.FIELD_RENDERING);
        final String logo = getMapValue(map, ManifestPropertiesImpl.FIELD_INSTITUTION_LOGO_URI);
        final String author = getMapValue(map, ManifestPropertiesImpl.FIELD_AUTHOR);
        final String published = getMapValue(map, ManifestPropertiesImpl.FIELD_PUBLISHED);

        final ManifestScope scope =
                new ManifestScope().fedoraPrefixes(prefixes).collectionURI(collectionIdURI.toString())
                        .sequenceURI(sequenceIdURI.toString()).label(label).attribution(attribution).license(license)
                        .logo(logo).rendering(rendering).author(author).published(published);

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-manifest.mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8);
    }

    private static String getMapValue(final Map<String, BagInfoField> map, final String key) {
        final BagInfoField bagDescriptorKey = map.get(key);
        return bagDescriptorKey != null ? bagDescriptorKey.getValue() : null;
    }
}
