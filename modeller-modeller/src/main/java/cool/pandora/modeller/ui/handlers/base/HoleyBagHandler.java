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
import cool.pandora.modeller.ui.jpanel.base.BagView;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;

/**
 * @author gov.loc
 */
public class HoleyBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    BagView bagView;
    DefaultBag bag;

    /**
     * @param bagView BagView
     */
    public HoleyBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        this.bag = bagView.getBag();

        final JCheckBox cb = (JCheckBox) e.getSource();

        // Determine status
        final boolean isSelected = cb.isSelected();
        if (isSelected) {
            bag.isHoley(true);
        } else {
            bag.isHoley(false);
        }
    /*
     * String messages = "";
     * bagView.updateBaggerRules();
     * 
     * bagView.bagInfoInputPane.populateForms(bag, true);
     * messages = bagView.bagInfoInputPane.updateForms(bag);
     * bagView.updateBagInfoInputPaneMessages(messages);
     * bagView.bagInfoInputPane.update(bag);
     * 
     * bagView.bagInfoInputPane.updateSelected(bag);
     * bagView.compositePane.updateCompositePaneTabs(bag, messages);
     * bagView.tagManifestPane.updateCompositePaneTabs(bag);
     * bagView.setBag(bag);
     */
    }
}
