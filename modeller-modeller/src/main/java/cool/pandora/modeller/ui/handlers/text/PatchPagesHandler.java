/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import cool.pandora.modeller.ui.jpanel.text.PatchPagesFrame;
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

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static cool.pandora.modeller.DocManifestBuilder.getPageIdList;
import static cool.pandora.modeller.ui.handlers.common.NodeMap.getCanvasPageMap;
import static cool.pandora.modeller.ui.handlers.common.NodeMap.getPageIdMap;

/**
 * Patch Pages Handler
 *
 * @author Christopher Johnson
 */
public class PatchPagesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchPagesHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchPagesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openPatchPagesFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.pagepatched");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final URI canvasContainerURI = IIIFObjectURI.getCanvasContainerURI(map);
        final URI areaContainerIRI = TextObjectURI.getAreaContainerURI(map);
        final String collectionPredicate = "http://iiif.io/api/text#hasAreas";

        final String url = bag.gethOCRResource();

        List<String> pageIdList = null;
        Map<String, List<String>> areaIdmap = null;
        Map<String, String> bboxmap = null;
        final Map<String, String> canvasPageMap;
        final Map<String, String> pageIdMap;

        InputStream rdfBody;

        try {
            final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            areaIdmap = NodeMap.getAreaIdMap(hocr, pageIdList);
            bboxmap = NodeMap.getBBoxMap(hocr, pageIdList);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        canvasPageMap = getCanvasPageMap(pageIdList, canvasContainerURI);
        pageIdMap = getPageIdMap(pageIdList);
        assert areaIdmap != null;
        final List<String> pageKeyList = new ArrayList<>(areaIdmap.keySet());

        for (final String pageId : pageKeyList) {
            final URI pageObjectURI = TextObjectURI.getPageObjectURI(map, pageId);
            assert canvasPageMap != null;
            final String canvasURI = canvasPageMap.get(pageId);
            assert pageIdMap != null;
            final String hOCRPageId = pageIdMap.get(pageId);
            final String bbox = bboxmap.get(hOCRPageId);
            final String region = Region.region().bbox(bbox).build();
            final String canvasRegionURI = CanvasRegionURI.regionuri().region(region).canvasURI(canvasURI).build();

            rdfBody = TextSequenceMetadata
                    .getTextSequenceMetadata(areaIdmap, pageId, canvasRegionURI, collectionPredicate, areaContainerIRI);
            try {
                ModellerClient.doPatch(pageObjectURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + pageObjectURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchPagesFrame() {
        final DefaultBag bag = bagView.getBag();
        final PatchPagesFrame patchPagesFrame =
                new PatchPagesFrame(bagView, bagView.getPropertyMessage("bag.frame" + ".patch" + ".pages"));
        patchPagesFrame.setBag(bag);
        patchPagesFrame.setVisible(true);
    }
}