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

/**
 * Update Bag Handler.
 *
 * @author gov.loc
 */
public class UpdateBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    /**
     * UpdateBagHandler.
     *
     * @param bagView BagView
     */
    public UpdateBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    /**
     * setBagView.
     *
     * @param bagView BagView
     */
    public void setBagView(final BagView bagView) {
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        updateBag(bagView.getBag());
    }

    /**
     * updateBag.
     *
     * @param bag DefaultBag
     */
    public void updateBag(final DefaultBag bag) {
        bagView.infoInputPane.bagInfoInputPane.updateForms(bag);
    }
}
