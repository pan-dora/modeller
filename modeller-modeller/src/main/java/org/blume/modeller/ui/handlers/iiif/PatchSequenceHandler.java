package org.blume.modeller.ui.handlers.iiif;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.apache.commons.io.IOUtils;
import org.apache.jena.iri.IRI;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.CollectionScope;
import org.blume.modeller.templates.CollectionTemplate;
import org.blume.modeller.ui.jpanel.PatchSequenceFrame;
import org.blume.modeller.ui.util.ContainerIRIResolver;
import org.blume.modeller.util.RDFCollectionWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ModellerClient;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static org.blume.modeller.common.uri.FedoraResources.FCRMETADATA;
import static java.nio.charset.StandardCharsets.UTF_8;

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
        InputStream rdfBody = null;
        String collectionPredicate = "http://iiif.io/api/presentation/2#hasCanvases";
        String sequenceContainerIRI = getSequenceContainer(map);
        String canvasContainerIRI = getCanvasContainer(map);
        String sequenceID = bag.getSequenceID();
        rdfBody = getSequenceMetadata(resourceIDList, collectionPredicate, canvasContainerIRI);
        String destinationURI = getDestinationURI(sequenceContainerIRI, "normal");
        ModellerClient client = new ModellerClient();
            try {
                client.doPatch(destinationURI, rdfBody);
            } finally {
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            }
        bagView.getControl().invalidate();
    }

    public void openPatchSequenceFrame() {
        DefaultBag bag = bagView.getBag();
        PatchSequenceFrame patchSequencesFrame = new PatchSequenceFrame(bagView, bagView.getPropertyMessage("bag.frame.patch.sequence"));
        patchSequencesFrame.setBag(bag);
        patchSequencesFrame.setVisible(true);
    }

    public String getDestinationURI(String sequenceContainer, String sequenceID) {
        return sequenceContainer +
                sequenceID;
    }

    public String getSequenceContainer(Map<String, BagInfoField> map) {
        ContainerIRIResolver containerIRIResolver;
        containerIRIResolver = ContainerIRIResolver.resolve()
                .map(map)
                .baseURIKey("FedoraBaseURI")
                .collectionRootKey("CollectionRoot")
                .collectionKey("CollectionID")
                .objektIDKey("ObjektID")
                .containerKey("IIIFSequenceContainer")
                .build();
        return containerIRIResolver.render();
    }

    public String getCanvasContainer(Map<String, BagInfoField> map) {
        ContainerIRIResolver containerIRIResolver;
        containerIRIResolver = ContainerIRIResolver.resolve()
                .map(map)
                .baseURIKey("FedoraBaseURI")
                .collectionRootKey("CollectionRoot")
                .collectionKey("CollectionID")
                .objektIDKey("ObjektID")
                .containerKey("IIIFCanvasContainer")
                .build();
        return containerIRIResolver.render();
    }

    public InputStream getSequenceMetadata(ArrayList<String> resourceIDList, String collectionPredicate,
                                           String resourceContainerIRI) {
        RDFCollectionWriter collectionWriter;
        collectionWriter = RDFCollectionWriter.collection()
                .idList(resourceIDList)
                .collectionPredicate(collectionPredicate)
                .resourceContainerIRI(resourceContainerIRI)
                .build();

        String collection = collectionWriter.render();
        CollectionTemplate collectionTemplate;
        List<CollectionScope.Prefix> prefixes = Arrays.asList(
                new CollectionScope.Prefix(FedoraPrefixes.RDFS),
                new CollectionScope.Prefix(FedoraPrefixes.MODE));

        CollectionScope scope = new CollectionScope()
                .fedoraPrefixes(prefixes)
                .sequenceGraph(collection);

        collectionTemplate = CollectionTemplate.template()
                .template("template/sparql-update-seq.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = unescapeXml(collectionTemplate.render());
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