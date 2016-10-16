package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import org.apache.commons.io.IOUtils;
import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.CollectionScope;
import org.blume.modeller.templates.MetadataTemplate;
import org.blume.modeller.ui.handlers.common.IIIFObjectURI;
import org.blume.modeller.ui.jpanel.iiif.PatchSequenceFrame;
import org.blume.modeller.ui.util.URIResolver;
import org.blume.modeller.util.RDFCollectionWriter;
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

public class PatchSequenceHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchSequenceHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchSequenceHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.sequencepatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ResourceIdentifierList idList = new ResourceIdentifierList(bagView);
        ArrayList<String> resourceIDList = idList.getResourceIdentifierList();
        InputStream rdfBody;
        String collectionPredicate = "http://iiif.io/api/presentation/2#hasCanvases";
        URI canvasContainerIRI = IIIFObjectURI.getCanvasContainerURI(map);
        //TODO Lookup IDs from Created Sequences and provide selection box in Frame
        String sequenceID = bag.getSequenceID();
        rdfBody = getSequenceMetadata(resourceIDList, collectionPredicate, canvasContainerIRI);
        // "normal" is static sequence ID value for testing
        URI destinationURI = getDestinationURI(map, "normal");
        ModellerClient client = new ModellerClient();
            try {
                client.doPatch(destinationURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        bagView.getControl().invalidate();
    }

    void openPatchSequenceFrame() {
        DefaultBag bag = bagView.getBag();
        PatchSequenceFrame patchSequencesFrame = new PatchSequenceFrame(bagView,
                bagView.getPropertyMessage("bag.frame.patch.sequence"));
        patchSequencesFrame.setBag(bag);
        patchSequencesFrame.setVisible(true);
    }

    private URI getDestinationURI(Map<String, BagInfoField> map, String sequenceID) {
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

    private InputStream getSequenceMetadata(ArrayList<String> resourceIDList, String collectionPredicate,
                                            URI resourceContainerIRI) {
        RDFCollectionWriter collectionWriter;
        collectionWriter = RDFCollectionWriter.collection()
                .idList(resourceIDList)
                .collectionPredicate(collectionPredicate)
                .resourceContainerIRI(resourceContainerIRI.toString())
                .build();

        String collection = collectionWriter.render();
        MetadataTemplate metadataTemplate;
        List<CollectionScope.Prefix> prefixes = Arrays.asList(
                new CollectionScope.Prefix(FedoraPrefixes.RDFS),
                new CollectionScope.Prefix(FedoraPrefixes.MODE));

        CollectionScope scope = new CollectionScope()
                .fedoraPrefixes(prefixes)
                .sequenceGraph(collection);

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-seq.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8 );
    }

}