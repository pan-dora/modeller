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

package cool.pandora.modeller.ui.handlers.iiif;

import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.base.SaveBagHandler;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.iiif.CreateListsFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractAction;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Create Lists Handler
 *
 * @author Christopher Johnson
 */
public class CreateListsHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateListsHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openCreateListsFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.listcreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final ResourceIdentifierList idList = new ResourceIdentifierList(bagView);
        final ArrayList<String> resourceIDList = idList.getResourceIdentifierList();
        for (final String resourceID : resourceIDList) {
            final URI listObjectURI = IIIFObjectURI.getListObjectURI(map, resourceID);
            try {
                ModellerClient.doPut(listObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + listObjectURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateListsFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateListsFrame createListsFrame =
                new CreateListsFrame(bagView, bagView.getPropertyMessage("bag.frame" + ".list"));
        createListsFrame.setBag(bag);
        createListsFrame.setVisible(true);
    }
}

