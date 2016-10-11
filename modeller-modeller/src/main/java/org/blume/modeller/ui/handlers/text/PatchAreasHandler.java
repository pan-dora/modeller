package org.blume.modeller.ui.handlers.text;

import org.apache.commons.lang3.StringUtils;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.common.IIIFObjectURI;
import org.blume.modeller.ui.handlers.common.NodeMap;
import org.blume.modeller.ui.handlers.common.TextObjectURI;
import org.blume.modeller.ui.handlers.common.TextSequenceMetadata;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.text.PatchAreasFrame;
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

public class PatchAreasHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchAreasHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchAreasHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openPatchAreasFrame();
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.areapatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        URI canvasContainerURI = IIIFObjectURI.getCanvasContainerURI(map);
        URI lineContainerIRI = TextObjectURI.getLineContainerURI(map);
        String collectionPredicate = "http://iiif.io/api/text#hasLines";

        String url = bag.gethOCRResource();

        List<String> pageIdList = null;
        List<String> areaIdList;
        Map<String, List<String>> lineIdmap = null;
        Map<String, String> bboxmap = null;
        Map<String, String> canvasPageMap;
        Map<String, String> pageIdMap;

        InputStream rdfBody;

        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            areaIdList = getAreaIdList(hocr);
            lineIdmap = NodeMap.getLineIdMap(hocr, areaIdList);
            bboxmap = NodeMap.getBBoxAreaMap(hocr, areaIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        canvasPageMap = getCanvasPageMap(pageIdList, canvasContainerURI);
        pageIdMap = getPageIdMap(pageIdList);
        assert lineIdmap != null;
        List<String> areaKeyList = new ArrayList<>(lineIdmap.keySet());

        for (String areaId : areaKeyList) {
            URI areaObjectURI = TextObjectURI.getAreaObjectURI(map, areaId);
            assert canvasPageMap != null;
            String canvasURI = canvasPageMap.get(StringUtils.substringBefore(areaId, "_"));
            assert pageIdMap != null;
            String hOCRAreaId = "block_" + areaId;
            String bbox = bboxmap.get(hOCRAreaId);
            String region = Region.region()
                    .bbox(bbox)
                    .build();
            String canvasRegionURI = CanvasRegionURI.regionuri()
                    .region(region)
                    .canvasURI(canvasURI)
                    .build();

            rdfBody = TextSequenceMetadata.getTextSequenceMetadata(lineIdmap, areaId, canvasRegionURI, collectionPredicate,
                    lineContainerIRI);
            try {
                client.doPatch(areaObjectURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + areaObjectURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }


    void openPatchAreasFrame() {
        DefaultBag bag = bagView.getBag();
        PatchAreasFrame patchAreasFrame = new PatchAreasFrame(bagView, bagView.getPropertyMessage("bag.frame.patch" +
                ".areas"));
        patchAreasFrame.setBag(bag);
        patchAreasFrame.setVisible(true);
    }

}