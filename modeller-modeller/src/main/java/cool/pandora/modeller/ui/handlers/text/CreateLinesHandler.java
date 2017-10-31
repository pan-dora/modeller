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

import static cool.pandora.modeller.DocManifestBuilder.getLineIdList;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

import cool.pandora.modeller.DocManifestBuilder;
import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.hOCRData;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.common.TextObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.text.CreateLinesFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create Lines Handler.
 *
 * @author Christopher Johnson
 */
public class CreateLinesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreateLinesHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * CreateLinesHandler.
     *
     * @param bagView BagView
     */
    public CreateLinesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openCreateLinesFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.linecreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final String url = bag.gethOCRResource();
        List<String> lineIdList = null;
        try {
            final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            lineIdList = getLineIdList(hocr);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        assert lineIdList != null;
        for (String resourceID : lineIdList) {
            resourceID = StringUtils.substringAfter(resourceID, "_");
            final URI lineObjectURI = TextObjectURI.getLineObjectURI(map, resourceID);
            try {
                ModellerClient.doPut(lineObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + lineObjectURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateLinesFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateLinesFrame createLinesFrame =
                new CreateLinesFrame(bagView, bagView.getPropertyMessage("bag.frame" + ".lines"));
        createLinesFrame.setBag(bag);
        createLinesFrame.setVisible(true);
    }
}