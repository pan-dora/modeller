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
import org.blume.modeller.ui.jpanel.text.PatchLinesFrame;
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
import static org.blume.modeller.DocManifestBuilder.*;
import static org.blume.modeller.ui.handlers.common.NodeMap.getCanvasPageMap;
import static org.blume.modeller.ui.handlers.common.NodeMap.getPageIdMap;

public class PatchLinesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchLinesHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchLinesHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openPatchLinesFrame();
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.linepatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        URI canvasContainerURI = IIIFObjectURI.getCanvasContainerURI(map);
        URI wordContainerIRI = TextObjectURI.getWordContainerURI(map);
        String collectionPredicate = "http://iiif.io/api/text#hasWords";

        String url = bag.gethOCRResource();

        List<String> pageIdList = null;
        List<String> lineIdList;
        Map<String, List<String>> wordIdMap = null;
        Map<String, String> bboxmap = null;
        Map<String, String> canvasPageMap;
        Map<String, String> pageIdMap;

        InputStream rdfBody;

        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            lineIdList = getLineIdList(hocr);
            wordIdMap = NodeMap.getWordIdMap(hocr, lineIdList);
            bboxmap = NodeMap.getBBoxLineMap(hocr, lineIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        canvasPageMap = getCanvasPageMap(pageIdList, canvasContainerURI);
        pageIdMap = getPageIdMap(pageIdList);
        assert wordIdMap != null;
        List<String> lineKeyList = new ArrayList<>(wordIdMap.keySet());

        for (String lineId : lineKeyList) {
            URI lineObjectURI = TextObjectURI.getLineObjectURI(map, lineId);
            assert canvasPageMap != null;
            String canvasURI = canvasPageMap.get(org.apache.commons.lang3.StringUtils.substringBefore(lineId, "_"));
            assert pageIdMap != null;
            String hOCRAreaId = "line_" + lineId;
            String bbox = bboxmap.get(hOCRAreaId);
            String region = Region.region()
                    .bbox(bbox)
                    .build();
            String canvasRegionURI = CanvasRegionURI.regionuri()
                    .region(region)
                    .canvasURI(canvasURI)
                    .build();
            rdfBody = TextSequenceMetadata.getTextSequenceMetadata(wordIdMap, lineId, canvasRegionURI,
                    collectionPredicate,
                    wordContainerIRI);
            try {
                client.doPatch(lineObjectURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + lineObjectURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchLinesFrame() {
        DefaultBag bag = bagView.getBag();
        PatchLinesFrame patchLinesFrame = new PatchLinesFrame(bagView, bagView.getPropertyMessage("bag.frame.patch" +
                ".lines"));
        patchLinesFrame.setBag(bag);
        patchLinesFrame.setVisible(true);
    }
}