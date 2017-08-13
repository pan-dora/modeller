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

import static cool.pandora.modeller.DocManifestBuilder.getWordIdList;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

import cool.pandora.modeller.DocManifestBuilder;
import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.ProfileOptions;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.hOCRData;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.text.CreateWordsFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ui.util.URIResolver;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create Words Handler.
 *
 * @author Christopher Johnson
 */
public class CreateWordsHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreateWordsHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * CreateWordsHandler.
     *
     * @param bagView BagView
     */
    public CreateWordsHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openCreateWordsFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.wordcreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final String url = bag.gethOCRResource();
        List<String> wordIdList = null;
        try {
            final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            wordIdList = getWordIdList(hocr);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        assert wordIdList != null;
        for (String resourceID : wordIdList) {
            resourceID = StringUtils.substringAfter(resourceID, "_");
            final URI wordObjectURI = getWordObjectURI(map, resourceID);
            try {
                ModellerClient.doPut(wordObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + wordObjectURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateWordsFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateWordsFrame createWordsFrame =
                new CreateWordsFrame(bagView, bagView.getPropertyMessage("bag.frame" + ".words"));
        createWordsFrame.setBag(bag);
        createWordsFrame.setVisible(true);
    }

    /**
     * getWordContainerURI.
     *
     * @param map Map
     * @return String
     */
    public static URI getWordContainerURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver =
                    URIResolver.resolve().map(map).containerKey(ProfileOptions
                            .TEXT_WORD_CONTAINER_KEY).pathType(4)
                            .build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private static URI getWordObjectURI(final Map<String, BagInfoField> map, final String
            resourceID) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions
                    .TEXT_WORD_CONTAINER_KEY)
                    .resource(resourceID).pathType(5).build();
            return uriResolver.render();
        } catch (final URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}