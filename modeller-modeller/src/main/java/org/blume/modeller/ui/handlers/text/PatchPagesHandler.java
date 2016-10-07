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
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.jpanel.PatchAreasFrame;
import org.blume.modeller.ui.jpanel.PatchPagesFrame;
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
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.blume.modeller.DocManifestBuilder.getAreaIdList;
import static org.blume.modeller.DocManifestBuilder.getAreaIdListforPage;
import static org.blume.modeller.DocManifestBuilder.getPageIdList;

public class PatchPagesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchPagesHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) { openPatchPagesFrame(); }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.pagepatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        URI areaContainerIRI = getAreaContainerURI(map);
        String collectionPredicate = "http://iiif.io/api/text#hasAreas";

        String url = bag.gethOCRResource();

        List<String> pageIdList;
        Map <String, List<String>> nodemap = null;
        InputStream rdfBody;

        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            nodemap = getAreaIdMap(hocr, pageIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert nodemap != null;
        List<String> pageKeyList = new ArrayList<>(nodemap.keySet());

        for (String pageId : pageKeyList) {
            URI pageObjectURI = getPageObjectURI(map, pageId);
            rdfBody = getAreaSequenceMetadata(nodemap, pageId, collectionPredicate, areaContainerIRI);
            try {
                client.doPatch(pageObjectURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + pageObjectURI);
            } catch (ModellerClientFailedException e) {
                    ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    private Map<String, List<String>> getAreaIdMap(hOCRData hocr, List<String> pageIdList) {
        Map<String, List<String>> nodemap = new HashMap<>();
        List<String> areaIdList;
        for (String pageId : pageIdList) {
            areaIdList = getAreaIdListforPage(hocr, pageId);
            for (int i = 0; i < areaIdList.size(); i++) {
                String areaId = StringUtils.substringAfter(areaIdList.get(i), "_");
                areaIdList.set(i, areaId);
            }
            pageId = StringUtils.substringAfter(pageId, "_");
            nodemap.put(pageId, areaIdList);
        }
        return nodemap;
    }

    void openPatchPagesFrame() {
        DefaultBag bag = bagView.getBag();
        PatchPagesFrame patchPagesFrame = new PatchPagesFrame(bagView, bagView.getPropertyMessage("bag.frame.patch.pages"));
        patchPagesFrame.setBag(bag);
        patchPagesFrame.setVisible(true);
    }

    public URI getAreaContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_AREA_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private URI getPageObjectURI(Map<String, BagInfoField> map, String resourceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_PAGE_CONTAINER_KEY)
                    .resource(resourceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private InputStream getAreaSequenceMetadata(Map<String, List<String>> resourceIDList, String pageId, String collectionPredicate,
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