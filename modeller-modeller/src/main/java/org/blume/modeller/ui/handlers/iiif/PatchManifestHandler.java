package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import org.apache.commons.io.IOUtils;
import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.ManifestScope;
import org.blume.modeller.templates.MetadataTemplate;
import org.blume.modeller.ui.jpanel.iiif.PatchManifestFrame;
import org.blume.modeller.ui.util.URIResolver;
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
        URI collectionIdURI = getCollectionIdURI(map);
        URI sequenceIdURI = getSequenceIdURI(map, "normal");
        String label = "Test";
        InputStream rdfBody;
        rdfBody = getManifestMetadata(collectionIdURI, label, sequenceIdURI);
        URI destinationURI = getManifestResource(map);
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

    public URI getManifestResource(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .pathType(6)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private URI getCollectionIdURI(Map<String, BagInfoField> map)  {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .pathType(2)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private URI getSequenceIdURI(Map<String, BagInfoField> map, String sequenceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.SEQUENCE_CONTAINER_KEY)
                    .resource(sequenceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private InputStream getManifestMetadata(URI collectionIdURI, String label, URI sequenceIdURI) {

        MetadataTemplate metadataTemplate;
        List<ManifestScope.Prefix> prefixes = Arrays.asList(
                new ManifestScope.Prefix(FedoraPrefixes.RDFS),
                new ManifestScope.Prefix(FedoraPrefixes.MODE));

        ManifestScope scope = new ManifestScope()
                .fedoraPrefixes(prefixes)
                .collectionURI(collectionIdURI.toString())
                .label(label)
                .sequenceURI(sequenceIdURI.toString())
                .license("http://localhost/static/test/license.html")
                .format("http://example.org/iiif/book1.pdf");

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-manifest.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8 );
    }

}
