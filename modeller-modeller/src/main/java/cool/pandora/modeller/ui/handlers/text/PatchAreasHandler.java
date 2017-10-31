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

import static cool.pandora.modeller.DocManifestBuilder.getAreaIdList;
import static cool.pandora.modeller.DocManifestBuilder.getPageIdList;
import static cool.pandora.modeller.ui.handlers.common.NodeMap.getCanvasPageMap;
import static cool.pandora.modeller.ui.handlers.common.NodeMap.getPageIdMap;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

import cool.pandora.modeller.CanvasRegionURI;
import cool.pandora.modeller.DocManifestBuilder;
import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.Region;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.hOCRData;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.handlers.common.NodeMap;
import cool.pandora.modeller.ui.handlers.common.TextObjectURI;
import cool.pandora.modeller.ui.handlers.common.TextSequenceMetadata;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.text.PatchAreasFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Patch Areas Handler.
 *
 * @author Christopher Johnson
 */
public class PatchAreasHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchAreasHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * PatchAreasHandler.
     *
     * @param bagView BagView
     */
    public PatchAreasHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openPatchAreasFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.areapatched");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final URI canvasContainerURI = IIIFObjectURI.getCanvasContainerURI(map);
        final URI lineContainerIRI = TextObjectURI.getLineContainerURI(map);
        final String collectionPredicate = "http://iiif.io/api/text#hasLines";

        final String url = bag.gethOCRResource();

        List<String> pageIdList = null;
        final List<String> areaIdList;
        Map<String, List<String>> lineIdmap = null;
        Map<String, String> bboxmap = null;
        final Map<String, String> canvasPageMap;
        final Map<String, String> pageIdMap;

        InputStream rdfBody;

        try {
            final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            areaIdList = getAreaIdList(hocr);
            lineIdmap = NodeMap.getLineIdMap(hocr, areaIdList);
            bboxmap = NodeMap.getBBoxAreaMap(hocr, areaIdList);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        canvasPageMap = getCanvasPageMap(pageIdList, canvasContainerURI);
        pageIdMap = getPageIdMap(pageIdList);
        assert lineIdmap != null;
        final List<String> areaKeyList = new ArrayList<>(lineIdmap.keySet());

        for (final String areaId : areaKeyList) {
            final URI areaObjectURI = TextObjectURI.getAreaObjectURI(map, areaId);
            assert canvasPageMap != null;
            final String canvasURI = canvasPageMap.get(StringUtils.substringBefore(areaId, "_"));
            assert pageIdMap != null;
            final String hOCRAreaId = "block_" + areaId;
            final String bbox = bboxmap.get(hOCRAreaId);
            final String region = Region.region().bbox(bbox).build();
            final String canvasRegionURI =
                    CanvasRegionURI.regionuri().region(region).canvasURI(canvasURI).build();

            rdfBody = TextSequenceMetadata
                    .getTextSequenceMetadata(lineIdmap, areaId, canvasRegionURI,
                            collectionPredicate, lineContainerIRI);
            try {
                ModellerClient.doPatch(areaObjectURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + areaObjectURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }


    void openPatchAreasFrame() {
        final DefaultBag bag = bagView.getBag();
        final PatchAreasFrame patchAreasFrame = new PatchAreasFrame(bagView,
                bagView.getPropertyMessage("bag.frame" + ".patch" + ".areas"));
        patchAreasFrame.setBag(bag);
        patchAreasFrame.setVisible(true);
    }

}