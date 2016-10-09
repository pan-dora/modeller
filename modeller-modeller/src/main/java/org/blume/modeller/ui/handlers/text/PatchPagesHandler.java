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
import org.blume.modeller.ui.handlers.common.IIIFObjectURI;
import org.blume.modeller.ui.handlers.common.TextObjectURI;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.text.PatchPagesFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.util.TextCollectionWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.blume.modeller.DocManifestBuilder.getAreaIdListforPage;
import static org.blume.modeller.DocManifestBuilder.getPageIdList;

public class PatchPagesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchPagesHandler.class);
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
        URI canvasContainerURI = IIIFObjectURI.getCanvasContainerURI(map);
        URI areaContainerIRI = TextObjectURI.getAreaContainerURI(map);
        String collectionPredicate = "http://iiif.io/api/text#hasAreas";

        String url = bag.gethOCRResource();

        List<String> pageIdList = null;
        Map <String, List<String>> nodemap = null;
        Map <String, String> bboxmap = null;
        Map<String, String> canvasPageMap;
        Map<String, String> pageIdMap;

        InputStream rdfBody;

        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            nodemap = getAreaIdMap(hocr, pageIdList);
            bboxmap = getBBoxMap(hocr, pageIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        canvasPageMap = getCanvasPageMap(pageIdList,canvasContainerURI);
        pageIdMap = getPageIdMap(pageIdList);
        assert nodemap != null;
        List<String> pageKeyList = new ArrayList<>(nodemap.keySet());

        for (String pageId : pageKeyList) {
            URI pageObjectURI = TextObjectURI.getPageObjectURI(map, pageId);
            assert canvasPageMap != null;
            String canvasURI = canvasPageMap.get(pageId);
            assert pageIdMap != null;
            String hOCRPageId = pageIdMap.get(pageId);
            String bbox = bboxmap.get(hOCRPageId);
            String region = Region.region()
                    .bbox(bbox)
                    .build();
            String canvasRegionURI = CanvasRegionURI.regionuri()
                    .region(region)
                    .canvasURI(canvasURI)
                    .build();

            rdfBody = getAreaSequenceMetadata(nodemap, pageId, canvasRegionURI, collectionPredicate, areaContainerIRI);
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

    private Map<String, String> getBBoxMap(hOCRData hocr, List<String> pageIdList) {
        Map<String, String> bboxMap = new HashMap<>();
         for (String pageId : pageIdList) {
             String bbox = DocManifestBuilder.getBboxForPage(hocr, pageId);
             bboxMap.put(pageId, bbox);
        }
        return bboxMap;
    }

    public static Map<String,String> getCanvasPageMap(List<String> pageIdList, URI canvasContainerURI) {
        try {
            CanvasPageMap canvasPageMap;
            canvasPageMap = CanvasPageMap.init()
                    .canvasContainerURI(canvasContainerURI)
                    .pageIdList(pageIdList)
                    .build();
            return canvasPageMap.render();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,String> getPageIdMap(List<String> pageIdList) {
        try {
            PageIdMap pageIdMap;
            pageIdMap = PageIdMap.init()
                     .pageIdList(pageIdList)
                    .build();
            return pageIdMap.render();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void openPatchPagesFrame() {
        DefaultBag bag = bagView.getBag();
        PatchPagesFrame patchPagesFrame = new PatchPagesFrame(bagView, bagView.getPropertyMessage("bag.frame.patch.pages"));
        patchPagesFrame.setBag(bag);
        patchPagesFrame.setVisible(true);
    }

    private InputStream getAreaSequenceMetadata(Map<String, List<String>> resourceIDList, String pageId,
                                              String canvasRegionURI, String collectionPredicate, URI resourceContainerIRI) {
        ArrayList<String> idList = new ArrayList<>(resourceIDList.get(pageId));
        TextCollectionWriter collectionWriter;
        collectionWriter = TextCollectionWriter.collection()
                .idList(idList)
                .collectionPredicate(collectionPredicate)
                .resourceContainerIRI(resourceContainerIRI.toString())
                .canvasURI(canvasRegionURI)
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