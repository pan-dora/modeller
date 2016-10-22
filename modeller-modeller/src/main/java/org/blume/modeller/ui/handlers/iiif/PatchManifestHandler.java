package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

import javax.swing.AbstractAction;

import org.apache.commons.io.IOUtils;
import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.ManifestPropertiesImpl;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.ManifestScope;
import org.blume.modeller.templates.MetadataTemplate;
import org.blume.modeller.ui.handlers.common.IIIFObjectURI;
import org.blume.modeller.ui.jpanel.iiif.PatchManifestFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ModellerClient;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class PatchManifestHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchManifestHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchManifestHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.manifestpatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();

        URI collectionIdURI = IIIFObjectURI.getCollectionIdURI(map);
        URI sequenceIdURI = IIIFObjectURI.getSequenceObjectURI(map, "normal");

        InputStream rdfBody;
        rdfBody = getManifestMetadata(collectionIdURI, sequenceIdURI, map);
        URI destinationURI = IIIFObjectURI.getManifestResource(map);
        ModellerClient client = new ModellerClient();
        try {
            client.doPatch(destinationURI, rdfBody);
            ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
        } catch (ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
        bagView.getControl().invalidate();
    }

    void openPatchManifestFrame() {
        DefaultBag bag = bagView.getBag();
        PatchManifestFrame patchManifestFrame = new PatchManifestFrame(bagView,
                bagView.getPropertyMessage("bag.frame.patch.manifest"));
        patchManifestFrame.setBag(bag);
        patchManifestFrame.setVisible(true);
    }

    private InputStream getManifestMetadata(URI collectionIdURI, URI sequenceIdURI, Map<String, BagInfoField> map) {

        MetadataTemplate metadataTemplate;
        List<ManifestScope.Prefix> prefixes = Arrays.asList(
                new ManifestScope.Prefix(FedoraPrefixes.RDFS),
                new ManifestScope.Prefix(FedoraPrefixes.MODE));

        String label = getMapValue(map, ManifestPropertiesImpl.FIELD_LABEL);
        String attribution = getMapValue(map, ManifestPropertiesImpl.FIELD_ATTRIBUTION);
        String license = getMapValue(map, ManifestPropertiesImpl.FIELD_LICENSE);
        String rendering = getMapValue(map, ManifestPropertiesImpl.FIELD_RENDERING);
        String logo = getMapValue(map, ManifestPropertiesImpl.FIELD_INSTITUTION_LOGO_URI);
        String author = getMapValue(map, ManifestPropertiesImpl.FIELD_AUTHOR);
        String published = getMapValue(map, ManifestPropertiesImpl.FIELD_PUBLISHED);

        ManifestScope scope = new ManifestScope()
                .fedoraPrefixes(prefixes)
                .collectionURI(collectionIdURI.toString())
                .sequenceURI(sequenceIdURI.toString())
                .label(label)
                .attribution(attribution)
                .license(license)
                .logo(logo)
                .rendering(rendering)
                .author(author)
                .published(published);

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-manifest.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8 );
    }

    private static String getMapValue(Map<String, BagInfoField> map, String key) {
        BagInfoField bagDescriptorKey = map.get(key);
        return bagDescriptorKey != null ? bagDescriptorKey.getValue() : null;
    }
}
