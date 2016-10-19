package org.blume.modeller.ui.jpanel.base;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.blume.modeller.bag.impl.ManifestPropertiesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBagInfo;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.util.LayoutUtil;

public class AddFieldPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    protected static final Logger log = LoggerFactory.getLogger(AddFieldPanel.class);

    private JCheckBox standardCheckBox;
    private JComboBox<String> standardFieldsComboBox;
    private JTextField customFieldTextField;
    private JTextField valueField;

    public AddFieldPanel() {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setLayout(new GridBagLayout());
        int row = 0;
        int col = 0;

        // standard field checkbox
        standardCheckBox = new JCheckBox("Standard");
        standardCheckBox.setSelected(true);
        standardCheckBox.addActionListener(new StandardFieldCheckBoxAction());
        GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST);
        add(standardCheckBox, gbc);

        // standard field dropdown menu
        List<String> listModel = getStandardBagFields();
        standardFieldsComboBox = new JComboBox<>(listModel.toArray(new String[listModel.size()]));
        standardFieldsComboBox.setName(ApplicationContextUtil.getMessage("baginfo.field.fieldlist"));
        standardFieldsComboBox.setSelectedItem("");
        standardFieldsComboBox.setToolTipText(ApplicationContextUtil.getMessage("baginfo.field.fieldlist.help"));
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        add(standardFieldsComboBox, gbc);

        // custom field name
        customFieldTextField = new JTextField(17);
        customFieldTextField.setToolTipText(ApplicationContextUtil.getMessage("baginfo.field.name.help"));
        customFieldTextField.setVisible(false);
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        add(customFieldTextField, gbc);

        // field value
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        add(new JLabel(" : "), gbc);

        valueField = new JTextField();
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);
        add(valueField, gbc);

        // add field button
        JButton addFieldButton = new JButton("Add");
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints
                .WEST);
        add(addFieldButton, gbc);
        addFieldButton.addActionListener(new AddFieldAction());
    }

    private class StandardFieldCheckBoxAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox checkbox = (JCheckBox) e.getSource();
            boolean standardFieldSelected = checkbox.isSelected();
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

    private List<String> getStandardBagFields() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("");

        // Standard Fields from BagInfoTxt
        // TODO fix it when BIL has the functionality
        Field[] fields = ManifestPropertiesImpl.class.getFields();
        for (Field field : fields) {
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
                        (!DefaultBagInfo.isManifestPropertyField(field.getName()))
                        && standardFieldName.compareTo("Bag-Size") != 0 && standardFieldName.compareTo
                        ("Payload-Oxum") != 0) {
                    log.debug("adding standard field: {}", standardFieldName);
                    list.add(standardFieldName);
                }
            }
        }
        return list;
    }

    @Override
    public void setEnabled(boolean enabled) {
        Component[] components = getComponents();
        for (Component component : components) {
            component.setEnabled(enabled);
        }
    }

    private class AddFieldAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            BagView bagView = ApplicationContextUtil.getBagView();

            BagInfoField field = createBagInfoField();

            if (field != null) {
                bagView.getBag().addField(field);
                // TODO use observer pattern
                bagView.infoInputPane.updateInfoFormsPane();
                bagView.infoInputPane.bagInfoInputPane.requestFocus();
            }
        }
    }

    private BagInfoField createBagInfoField() {
        BagView bagView = ApplicationContextUtil.getBagView();

        BagInfoField field = new BagInfoField();

        String fieldName = null;
        if (isStandardField()) {
            fieldName = (String) standardFieldsComboBox.getSelectedItem();
        } else {
            fieldName = customFieldTextField.getText();
        }

        if (fieldName.trim().isEmpty()) {
            bagView.showWarningErrorDialog("New Field Dialog", "Field name must be specified!");
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
