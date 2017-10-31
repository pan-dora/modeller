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

package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.TagFilesFrame;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;


/**
 * Show Tag Files Handler.
 *
 * @author gov.loc
 */
public class ShowTagFilesHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    BagView bagView;
    DefaultBag bag;

    /**
     * ShowTagFilesHandler.
     *
     * @param bagView BagView
     */
    public ShowTagFilesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        showTagFiles();
    }

    private void showTagFiles() {
        bag = bagView.getBag();
        bagView.tagManifestPane.updateCompositePaneTabs(bag);
        final TagFilesFrame tagFilesFrame =
                new TagFilesFrame(bagView.getPropertyMessage("bagView" + ".tagFrame.title"));
        tagFilesFrame.addComponents(bagView.tagManifestPane);
        tagFilesFrame.addComponents(bagView.tagManifestPane);
        tagFilesFrame.setVisible(true);
        tagFilesFrame.setAlwaysOnTop(true);
    }
}
