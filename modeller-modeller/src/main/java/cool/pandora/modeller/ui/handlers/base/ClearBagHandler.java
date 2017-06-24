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
import cool.pandora.modeller.ui.BagTree;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.impl.AbstractBagConstants;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;

/**
 * Clear Bag Handler
 *
 * @author gov.loc
 */
public class ClearBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    BagView bagView;
    private boolean confirmSaveFlag = false;

    /**
     * @param bagView BagView
     */
    public ClearBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    /**
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        closeExistingBag();
    }

    void closeExistingBag() {
        // Closes Bag without popping up the Save Dialog Box for a Holey and
        // Serialized Bag
        // For all other types of Bags the Save Dialog Box pops up
        if (bagView.getBag().isHoley() || bagView.getBag().isSerial()) {
            clearExistingBag();
        } else {
            confirmCloseBag();
        }
        if (isConfirmSaveFlag()) {
            bagView.saveBagHandler.setClearAfterSaving(true);
            bagView.saveBagAsHandler.openSaveBagAsFrame();
            setConfirmSaveFlag(false);
        }

    }

    private void confirmCloseBag() {
        final ConfirmationDialog dialog = new ConfirmationDialog() {
            @Override
            protected void onConfirm() {
                setConfirmSaveFlag(true);
            }

            @Override
            protected void onCancel() {
                super.onCancel();
                clearExistingBag();
            }
        };
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(bagView.getPropertyMessage("bag.dialog.title.close"));
        dialog.setConfirmationMessage(bagView.getPropertyMessage("bag.dialog.message.close"));
        dialog.showDialog();
    }

    void clearExistingBag() {
        newDefaultBag(null);
        final DefaultBag bag = bagView.getBag();
        bag.clear();
        bagView.baggerRules.clear();
        bagView.bagPayloadTree = new BagTree(bagView, AbstractBagConstants.DATA_DIRECTORY);
        bagView.bagPayloadTreePanel.refresh(bagView.bagPayloadTree);
        bagView.bagTagFileTree = new BagTree(bagView, ApplicationContextUtil.getMessage("bag.label.noname"));
        bagView.bagTagFileTreePanel.refresh(bagView.bagTagFileTree);
        bagView.infoInputPane.setBagName(bag.getName());
        bagView.infoInputPane.updateInfoForms();
        bagView.updateClearBag();
    }

    void newDefaultBag(final File f) {
        DefaultBag bag;
        final String bagName;
        try {
            bag = new DefaultBag(f, bagView.infoInputPane.getBagVersion());
        } catch (final Exception e) {
            bag = new DefaultBag(f, null);
        }
        if (f == null) {
            bagName = bagView.getPropertyMessage("bag.label.noname");
        } else {
            bagName = f.getName();
            final String fileName = f.getAbsolutePath();
            bagView.infoInputPane.setBagName(fileName);
        }
        bag.setName(bagName);
        bagView.setBag(bag);
    }

    private void setConfirmSaveFlag(final boolean confirmSaveFlag) {
        this.confirmSaveFlag = confirmSaveFlag;
    }

    private boolean isConfirmSaveFlag() {
        return confirmSaveFlag;
    }
}
