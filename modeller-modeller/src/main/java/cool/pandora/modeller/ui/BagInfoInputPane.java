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

package cool.pandora.modeller.ui;

import cool.pandora.modeller.bag.BaggerProfile;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.bag.impl.DefaultBagInfo;
import cool.pandora.modeller.ui.jpanel.base.BagInfoForm;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.base.OrganizationProfileForm;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.richclient.form.FormModelHelper;

/**
 * BagInfoInputPane.
 *
 * @author gov.loc
 */
public class BagInfoInputPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    protected static final Logger log = LoggerFactory.getLogger(BagInfoInputPane.class);

    private final BagView bagView;
    private DefaultBag defaultBag;
    private BaggerProfile bagProfile;
    private BagInfoForm bagInfoForm = null;
    private OrganizationProfileForm profileForm = null;

    /**
     * BagInfoInputPane.
     *
     * @param bagView BagView
     */
    public BagInfoInputPane(final BagView bagView) {
        this.bagView = bagView;
        this.defaultBag = bagView.getBag();
        populateForms(defaultBag);

        final InputMap im = this.getInputMap();
        im.put(KeyStroke.getKeyStroke("F2"), "tabNext");
        final ActionMap am = this.getActionMap();
        am.put("tabNext", new AbstractAction("tabNext") {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent evt) {
                final int selected = getSelectedIndex();
                final int count = getComponentCount();
                if (selected >= 0 && selected < count - 1) {
                    setSelectedIndex(selected + 1);
                } else {
                    setSelectedIndex(0);
                }
                invalidate();
                repaint();
            }
        });
        this.setActionMap(am);
    }

    /**
     * enableForms.
     *
     * @param b boolean
     */
    public void enableForms(final boolean b) {
        profileForm.setEnabled(b);
        profileForm.getControl().invalidate();
        bagInfoForm.setEnabled(b);
        bagInfoForm.getControl().invalidate();
        this.setEnabled(b);
        this.invalidate();
    }

    /**
     * populateForms.
     *
     * @param bag DefaultBag
     */
    public void populateForms(final DefaultBag bag) {

        defaultBag = bag;
        final DefaultBagInfo bagInfo = bag.getInfo();

        if (bagProfile == null) {
            bagProfile = new BaggerProfile();
        }

        bagProfile.setOrganization(bagInfo.getBagOrganization());
        bagProfile.setToContact(bagInfo.getToContact());

        final HierarchicalFormModel profileFormModel = FormModelHelper.createCompoundFormModel(
                bagProfile);
        profileForm =
                new OrganizationProfileForm(FormModelHelper.createChildPageFormModel(
                        profileFormModel, null), bagView);

        final HierarchicalFormModel infoFormModel = FormModelHelper.createCompoundFormModel(
                bagInfo);
        bagInfoForm = new BagInfoForm(FormModelHelper.createChildPageFormModel(infoFormModel,
                null), bagView,
                bagInfo.getFieldMap());

        createTabbedUiComponentsWithForms();
    }

    /**
     * createTabbedUiComponentsWithForms.
     *
     * <p>Create a tabbed pane for the information forms and checkbox panel
     */
    private void createTabbedUiComponentsWithForms() {
        removeAll();
        // invalidate();
        validate();
        setName("Profile");
        bagInfoForm.getControl().setToolTipText(bagView.getPropertyMessage("infoinputpane.tab"
                + ".details.help"));
        addTab(bagView.getPropertyMessage("infoInputPane.tab.details"), bagInfoForm.getControl());
        profileForm.getControl().setToolTipText("Profile Form");
    }

    /**
     * verifyForms.
     *
     * @param bag DefaultBag
     */
    private void verifyForms(final DefaultBag bag) {

        if (!profileForm.hasErrors()) {
            profileForm.commit();
        } else {
            throw new RuntimeException("Bag-Info has errors");
        }

        if (!bagInfoForm.hasErrors()) {
            bagInfoForm.commit();
        } else {
            throw new RuntimeException("Bag-Info has errors");
        }
        updateBagInfo(bag);

    }

    /**
     * updateForms.
     *
     * @param bag DefaultBag
     */
    public void updateForms(final DefaultBag bag) {

        verifyForms(bag);
        createTabbedUiComponentsWithForms();

        final java.awt.Component[] components = bagInfoForm.getControl().getComponents();
        for (final Component c : components) {
            c.invalidate();
            c.repaint();
        }
        bagInfoForm.getControl().invalidate();
        profileForm.getControl().invalidate();
        invalidate();
        repaint();

    }

    /**
     * updateProject.
     *
     * @param bagView BagView
     */
    public static void updateProject(final BagView bagView) {
        bagView.infoInputPane.updateInfoFormsPane();
    }

    private void updateBagInfo(final DefaultBag bag) {
        final HashMap<String, String> map = bagInfoForm.getBagInfoMap();
        bag.updateBagInfo(map);
    }

    /**
     * requestFocus.
     */
    @Override
    public void requestFocus() {
        bagInfoForm.getControl().requestFocus();
    }
}