package cool.pandora.modeller.ui.handlers.text;

import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.DocManifestBuilder;
import cool.pandora.modeller.hOCRData;
import cool.pandora.modeller.CanvasRegionURI;
import cool.pandora.modeller.Region;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.handlers.common.NodeMap;
import cool.pandora.modeller.ui.handlers.common.TextObjectURI;
import cool.pandora.modeller.ui.handlers.common.TextSequenceMetadata;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.text.PatchLinesFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static org.apache.commons.lang.StringUtils.substringBefore;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static cool.pandora.modeller.DocManifestBuilder.getPageIdList;
import static cool.pandora.modeller.DocManifestBuilder.getLineIdList;
import static cool.pandora.modeller.ui.handlers.common.NodeMap.getCanvasPageMap;
import static cool.pandora.modeller.ui.handlers.common.NodeMap.getPageIdMap;

/**
 * Patch Lines Handler
 *
 * @author Christopher Johnson
 */
public class PatchLinesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchLinesHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchLinesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openPatchLinesFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.linepatched");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final URI canvasContainerURI = IIIFObjectURI.getCanvasContainerURI(map);
        final URI wordContainerIRI = TextObjectURI.getWordContainerURI(map);
        final String collectionPredicate = "http://iiif.io/api/text#hasWords";

        final String url = bag.gethOCRResource();

        List<String> pageIdList = null;
        final List<String> lineIdList;
        Map<String, List<String>> wordIdMap = null;
        Map<String, String> bboxmap = null;
        final Map<String, String> canvasPageMap;
        final Map<String, String> pageIdMap;

        InputStream rdfBody;

        try {
            final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            lineIdList = getLineIdList(hocr);
            wordIdMap = NodeMap.getWordIdMap(hocr, lineIdList);
            bboxmap = NodeMap.getBBoxLineMap(hocr, lineIdList);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        canvasPageMap = getCanvasPageMap(pageIdList, canvasContainerURI);
        pageIdMap = getPageIdMap(pageIdList);
        assert wordIdMap != null;
        final List<String> lineKeyList = new ArrayList<>(wordIdMap.keySet());

        for (final String lineId : lineKeyList) {
            final URI lineObjectURI = TextObjectURI.getLineObjectURI(map, lineId);
            assert canvasPageMap != null;
            final String canvasURI = canvasPageMap.get(substringBefore(lineId, "_"));
            assert pageIdMap != null;
            final String hOCRAreaId = "line_" + lineId;
            final String bbox = bboxmap.get(hOCRAreaId);
            final String region = Region.region().bbox(bbox).build();
            final String canvasRegionURI = CanvasRegionURI.regionuri().region(region).canvasURI(canvasURI).build();
            rdfBody = TextSequenceMetadata
                    .getTextSequenceMetadata(wordIdMap, lineId, canvasRegionURI, collectionPredicate, wordContainerIRI);
            try {
                ModellerClient.doPatch(lineObjectURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + lineObjectURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchLinesFrame() {
        final DefaultBag bag = bagView.getBag();
        final PatchLinesFrame patchLinesFrame =
                new PatchLinesFrame(bagView, bagView.getPropertyMessage("bag.frame" + ".patch" + ".lines"));
        patchLinesFrame.setBag(bag);
        patchLinesFrame.setVisible(true);
    }
}