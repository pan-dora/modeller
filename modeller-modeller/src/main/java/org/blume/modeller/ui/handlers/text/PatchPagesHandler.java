package org.blume.modeller.ui.handlers.text;

import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.common.IIIFObjectURI;
import org.blume.modeller.ui.handlers.common.NodeMap;
import org.blume.modeller.ui.handlers.common.TextObjectURI;
import org.blume.modeller.ui.handlers.common.TextSequenceMetadata;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.text.PatchPagesFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.blume.modeller.DocManifestBuilder.getPageIdList;
import static org.blume.modeller.ui.handlers.common.NodeMap.getCanvasPageMap;
import static org.blume.modeller.ui.handlers.common.NodeMap.getPageIdMap;

public class PatchPagesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchPagesHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchPagesHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openPatchPagesFrame();
    }

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
        Map<String, List<String>> areaIdmap = null;
        Map<String, String> bboxmap = null;
        Map<String, String> canvasPageMap;
        Map<String, String> pageIdMap;

        InputStream rdfBody;

        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            areaIdmap = NodeMap.getAreaIdMap(hocr, pageIdList);
            bboxmap = NodeMap.getBBoxMap(hocr, pageIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        canvasPageMap = getCanvasPageMap(pageIdList, canvasContainerURI);
        pageIdMap = getPageIdMap(pageIdList);
        assert areaIdmap != null;
        List<String> pageKeyList = new ArrayList<>(areaIdmap.keySet());

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

            rdfBody = TextSequenceMetadata.getTextSequenceMetadata(areaIdmap, pageId, canvasRegionURI,
                    collectionPredicate, areaContainerIRI);
            try {
                client.doPatch(pageObjectURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + pageObjectURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchPagesFrame() {
        DefaultBag bag = bagView.getBag();
        PatchPagesFrame patchPagesFrame = new PatchPagesFrame(bagView, bagView.getPropertyMessage("bag.frame.patch" +
                ".pages"));
        patchPagesFrame.setBag(bag);
        patchPagesFrame.setVisible(true);
    }
}