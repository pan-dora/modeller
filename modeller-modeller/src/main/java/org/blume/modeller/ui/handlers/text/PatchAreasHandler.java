package org.blume.modeller.ui.handlers.text;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.CollectionScope;
import org.blume.modeller.templates.MetadataTemplate;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.jpanel.PatchAreasFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.util.URIResolver;
import org.blume.modeller.util.RDFCollectionWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.blume.modeller.DocManifestBuilder.*;

public class PatchAreasHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchAreasHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchAreasHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) { openPatchAreasFrame(); }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.areapatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        URI lineContainerIRI = getLineContainerURI(map);
        String collectionPredicate = "http://iiif.io/api/text#hasLines";

        String url = bag.gethOCRResource();

        List<String> areaIdList;
        Map <String, List<String>> nodemap = null;
        InputStream rdfBody;

        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            areaIdList = getAreaIdList(hocr);
            nodemap = getLineIdMap(hocr, areaIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert nodemap != null;
        List<String> areaKeyList = new ArrayList<>(nodemap.keySet());

        for (String areaId : areaKeyList) {
            URI areaObjectURI = getAreaObjectURI(map, areaId);
            rdfBody = getLineSequenceMetadata(nodemap, areaId, collectionPredicate, lineContainerIRI);
            try {
                client.doPatch(areaObjectURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + areaObjectURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    private Map<String, List<String>> getLineIdMap(hOCRData hocr, List<String> areaIdList) {
        Map<String, List<String>> nodemap = new HashMap<>();
        List<String> lineIdList;
        for (String areaId : areaIdList) {
            lineIdList = getLineIdListforArea(hocr, areaId);
            for (int i = 0; i < lineIdList.size(); i++) {
                String lineId = StringUtils.substringAfter(lineIdList.get(i), "_");
                lineIdList.set(i, lineId);
            }
            areaId = StringUtils.substringAfter(areaId, "_");
            nodemap.put(areaId, lineIdList);
        }
        return nodemap;
    }

    void openPatchAreasFrame() {
        DefaultBag bag = bagView.getBag();
        PatchAreasFrame patchAreasFrame = new PatchAreasFrame(bagView, bagView.getPropertyMessage("bag.frame.patch.areas"));
        patchAreasFrame.setBag(bag);
        patchAreasFrame.setVisible(true);
    }

    public URI getLineContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_LINE_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private URI getAreaObjectURI(Map<String, BagInfoField> map, String resourceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_AREA_CONTAINER_KEY)
                    .resource(resourceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private InputStream getLineSequenceMetadata(Map<String, List<String>> resourceIDList, String pageId, String collectionPredicate,
                                                URI resourceContainerIRI) {
        ArrayList idList = new ArrayList<>(resourceIDList.get(pageId));
        RDFCollectionWriter collectionWriter;
        collectionWriter = RDFCollectionWriter.collection()
                .idList(idList)
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