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

import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBagInfo;
import cool.pandora.modeller.bag.impl.ManifestPropertiesImpl;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ui.util.LayoutUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * AddFieldPanel
 *
 * @author gov.loc
 */
public class AddFieldPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    protected static final Logger log = LoggerFactory.getLogger(AddFieldPanel.class);

    private final JCheckBox standardCheckBox;
    private final JComboBox<String> standardFieldsComboBox;
    private final JTextField customFieldTextField;
    private final JTextField valueField;

    AddFieldPanel() {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setLayout(new GridBagLayout());
        final int row = 0;
        int col = 0;

        // standard field checkbox
        standardCheckBox = new JCheckBox("Standard");
        standardCheckBox.setSelected(true);
        standardCheckBox.addActionListener(new StandardFieldCheckBoxAction());
        GridBagConstraints gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE,
                        GridBagConstraints.WEST);
        add(standardCheckBox, gbc);

        // standard field dropdown menu
        final List<String> listModel = getStandardBagFields();
        standardFieldsComboBox = new JComboBox<>(listModel.toArray(new String[listModel.size()]));
        standardFieldsComboBox.setName(ApplicationContextUtil.getMessage("baginfo.field" +
                ".fieldlist"));
        standardFieldsComboBox.setSelectedItem("");
        standardFieldsComboBox.setToolTipText(ApplicationContextUtil.getMessage("baginfo.field" +
                ".fieldlist.help"));
        gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE,
                        GridBagConstraints.WEST);
        add(standardFieldsComboBox, gbc);

        // custom field name
        customFieldTextField = new JTextField(17);
        customFieldTextField.setToolTipText(ApplicationContextUtil.getMessage("baginfo.field.name" +
                ".help"));
        customFieldTextField.setVisible(false);
        gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE,
                        GridBagConstraints.WEST);
        add(customFieldTextField, gbc);

        // field value
        gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE,
                        GridBagConstraints.WEST);
        add(new JLabel(" : "), gbc);

        valueField = new JTextField();
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints
                        .HORIZONTAL,
                GridBagConstraints.WEST);
        add(valueField, gbc);

        // add field button
        final JButton addFieldButton = new JButton("Add");
        gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE,
                        GridBagConstraints.WEST);
        add(addFieldButton, gbc);
        addFieldButton.addActionListener(new AddFieldAction());
    }

    private class StandardFieldCheckBoxAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JCheckBox checkbox = (JCheckBox) e.getSource();
            final boolean standardFieldSelected = checkbox.isSelected();
            if (standardFieldSelected) {
                standardFieldsComboBox.setVisible(true);
                standardFieldsComboBox.requestFocus();
                customFieldTextField.setVisible(false);
            } else {
                standardFieldsComboBox.setVisible(false);
                customFieldTextField.setVisible(true);
                customFieldTextField.requestFocus();
            }
        }
    }

    private static List<String> getStandardBagFields() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("");

        // Standard Fields from BagInfoTxt
        // TODO fix it when BIL has the functionality
        final Field[] fields = ManifestPropertiesImpl.class.getFields();
        for (final Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && field.getName().startsWith("FIELD_")) {
                String standardFieldName = null;
                try {
                    standardFieldName = (String) field.get(ManifestPropertiesImpl.class);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                // Removes BagInfo.txt default values from the Standard item drop down
                // list on the Bag-Info tab.
                // This would prevent the BagInfo.txt values generated by Bagger to be
                // overwritten
                if (!DefaultBagInfo.isOrganizationContactField(field.getName()) &&
                        (!DefaultBagInfo.isManifestPropertyField(field.getName())) &&
                        standardFieldName.compareTo("Bag-Size") != 0 &&
                        standardFieldName.compareTo("Payload-Oxum") != 0) {
                    log.debug("adding standard field: {}", standardFieldName);
                    list.add(standardFieldName);
                }
            }
        }
        return list;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        final Component[] components = getComponents();
        for (final Component component : components) {
            component.setEnabled(enabled);
        }
    }

    private class AddFieldAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final BagView bagView = ApplicationContextUtil.getBagView();

            final BagInfoField field = createBagInfoField();

            if (field != null) {
                bagView.getBag().addField(field);
                // TODO use observer pattern
                bagView.infoInputPane.updateInfoFormsPane();
                bagView.infoInputPane.bagInfoInputPane.requestFocus();
            }
        }
    }

    private BagInfoField createBagInfoField() {
        final BagView bagView = ApplicationContextUtil.getBagView();

        final BagInfoField field = new BagInfoField();

        final String fieldName;
        if (isStandardField()) {
            fieldName = (String) standardFieldsComboBox.getSelectedItem();
        } else {
            fieldName = customFieldTextField.getText();
        }

        if (fieldName.trim().isEmpty()) {
            BagView.showWarningErrorDialog("New Field Dialog", "Field name must be specified!");
            return null;
        }

        field.setName(fieldName);
        field.setLabel(fieldName);
        field.setValue(valueField.getText().trim());
        field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);

        return field;
    }

    private boolean isStandardField() {
        return standardCheckBox.isSelected();
    }

}
