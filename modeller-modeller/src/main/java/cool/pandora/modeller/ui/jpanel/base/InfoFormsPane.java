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
package cool.pandora.modeller.ui.jpanel.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.BagInfoInputPane;
import cool.pandora.modeller.ui.handlers.base.UpdateBagHandler;
import cool.pandora.modeller.ui.util.LayoutUtil;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * InfoFormsPane
 *
 * @author gov.loc
 */
public class InfoFormsPane extends JScrollPane {
    private static final long serialVersionUID = -5988111446773491301L;
    private final BagView bagView;
    private final DefaultBag bag;
    private JPanel infoPanel;
    protected JPanel serializeGroupPanel;

    public BagInfoInputPane bagInfoInputPane;
    public UpdateBagHandler updateBagHandler;

    private JLabel bagNameValue;
    public JButton removeProjectButton;
    public JLabel bagVersionValue;
    private JLabel bagProfileValue;
    public JLabel holeyValue;
    public JLabel serializeLabel;
    public JLabel serializeValue;
    public JCheckBox defaultProject;
    public JRadioButton noneButton;
    public JRadioButton zipButton;
    public JRadioButton tarButton;
    public JRadioButton tarGzButton;
    public JRadioButton tarBz2Button;

    /**
     * @param bagView BagView
     */
    InfoFormsPane(final BagView bagView) {
        super();
        this.bagView = bagView;
        this.bag = bagView.getBag();
        createUiComponent();
        updateBagHandler = new UpdateBagHandler(bagView);
    }

    /**
     * @param profileName String
     */
    public void setProfile(final String profileName) {
        bagProfileValue.setText(profileName);
    }

    /**
     *
     */
    private void createUiComponent() {
        final JPanel bagSettingsPanel = createSettingsPanel();

        infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setToolTipText(bagView.getPropertyMessage("bagView.bagInfoInputPane.help"));
        final Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        infoPanel.setBorder(emptyBorder);

        GridBagConstraints gbc = LayoutUtil
                .buildGridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        infoPanel.add(bagSettingsPanel, gbc);

        bagInfoInputPane = new BagInfoInputPane(bagView);
        bagInfoInputPane.setToolTipText(bagView.getPropertyMessage("bagView.bagInfoInputPane.help"));
        bagInfoInputPane.setEnabled(false);

        gbc = LayoutUtil.buildGridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        infoPanel.add(bagInfoInputPane, gbc);
        this.setViewportView(infoPanel);
    }

    /**
     * @return contentPane
     */
    private JPanel createSettingsPanel() {
        final JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(5, 5));

        final JPanel mainPanel = new JPanel();
        contentPane.add(mainPanel, BorderLayout.NORTH);
        mainPanel.setLayout(new BorderLayout(0, 0));

        // Bag settings
        mainPanel.add(createBagSettingsPanel(), BorderLayout.CENTER);

        return contentPane;
    }

    /**
     * @return pane
     */
    private JPanel createBagSettingsPanel() {
        final JPanel pane = new JPanel();

        pane.setLayout(new GridBagLayout());

        // bag name
        int row = 0;
        final JLabel lblBagName = new JLabel(bagView.getPropertyMessage("bag.label.name"));
        GridBagConstraints gbc = LayoutUtil
                .buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(lblBagName, gbc);

        bagNameValue = new JLabel(bagView.getPropertyMessage("bag.label.noname"));
        gbc = LayoutUtil
                .buildGridBagConstraints(1, row, 3, 1, 3, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(bagNameValue, gbc);

        // bag profile
        row++;
        final JLabel bagProfileLabel = new JLabel("Profile:");
        gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(bagProfileLabel, gbc);

        bagProfileValue = new JLabel("");
        gbc = LayoutUtil
                .buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(bagProfileValue, gbc);

        // bag version
        final JLabel bagVersionLabel = new JLabel(bagView.getPropertyMessage("bag.label.version"));
        bagVersionLabel.setToolTipText(bagView.getPropertyMessage("bag.versionlist.help"));
        gbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(bagVersionLabel, gbc);

        bagVersionValue = new JLabel("");
        gbc = LayoutUtil
                .buildGridBagConstraints(3, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(bagVersionValue, gbc);

        // is Holey bag?
        row++;
        final JLabel holeyLabel = new JLabel(bagView.getPropertyMessage("bag.label.isholey"));
        holeyLabel.setToolTipText(bagView.getPropertyMessage("bag.isholey.help"));
        gbc = LayoutUtil.buildGridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(holeyLabel, gbc);

        holeyValue = new JLabel("");
        gbc = LayoutUtil
                .buildGridBagConstraints(1, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(holeyValue, gbc);

        // is packed?
        final JLabel serializeLabel = new JLabel(bagView.getPropertyMessage("bag.label.ispackage"));
        serializeLabel.setToolTipText(bagView.getPropertyMessage("bag.serializetype.help"));
        gbc = LayoutUtil.buildGridBagConstraints(2, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(serializeLabel, gbc);

        serializeValue = new JLabel("");
        gbc = LayoutUtil
                .buildGridBagConstraints(3, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        gbc.insets = new Insets(0, 0, 5, 5);
        pane.add(serializeValue, gbc);
        return pane;
    }

    /**
     * @param value String
     */
    public void setBagVersion(final String value) {
        bagVersionValue.setText(value);
    }

    /**
     * @return bagVersionValue
     */
    public String getBagVersion() {
        return bagVersionValue.getText();
    }

    /**
     * @param value String
     */
    public void setHoley(final String value) {
        holeyValue.setText(value);
    }

    /**
     * @param name String
     */
    public void setBagName(final String name) {
        if (name == null || name.length() < 1) {
            return;
        }
        bagNameValue.setText(name);
    }

    /**
     * @return bagNameValue
     */
    public String getBagName() {
        return bagNameValue.getText();
    }

    /**
     *
     */
    public void updateInfoForms() {
        bagInfoInputPane.populateForms(bag);
        bagInfoInputPane.enableForms(false);
        bagInfoInputPane.invalidate();
    }

    /**
     *
     */
    public void updateInfoFormsPane() {
        // need to remove something?
        infoPanel.remove(bagInfoInputPane);
        infoPanel.validate();

        bagInfoInputPane = new BagInfoInputPane(bagView);
        bagInfoInputPane.setToolTipText(bagView.getPropertyMessage("bagView.bagInfoInputPane.help"));

        final GridBagConstraints gbc =
                LayoutUtil.buildGridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        infoPanel.add(bagInfoInputPane, gbc);

        this.validate();
    }

    /**
     * @param i int
     */
    public void showTabPane(final int i) {
        bagInfoInputPane.setSelectedIndex(i);
        bagInfoInputPane.invalidate();
    }

}
