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
import cool.pandora.modeller.ui.jpanel.iiif.CreateSequencesFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.Map;

import javax.swing.AbstractAction;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Create Sequences Handler
 *
 * @author Christopher Johnson
 */
public class CreateSequencesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateSequencesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openCreateSequencesFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.sequencecreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();

        final String sequenceID = bag.getSequenceID();
        final URI sequenceObjectURI = IIIFObjectURI.getSequenceObjectURI(map, sequenceID);
        try {
            ModellerClient.doPut(sequenceObjectURI);
            ApplicationContextUtil.addConsoleMessage(message + " " + sequenceObjectURI);
        } catch (final ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }

        bagView.getControl().invalidate();
    }

    void openCreateSequencesFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateSequencesFrame createSequencesFrame =
                new CreateSequencesFrame(bagView, bagView.getPropertyMessage("bag.frame.sequence"));
        createSequencesFrame.setBag(bag);
        createSequencesFrame.setVisible(true);
    }
}
