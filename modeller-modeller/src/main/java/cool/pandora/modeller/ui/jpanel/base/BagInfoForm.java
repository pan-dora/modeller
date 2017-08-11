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
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.BagTableFormBuilder;
import cool.pandora.modeller.ui.NoTabTextArea;
import cool.pandora.modeller.ui.util.LayoutUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;


import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.AbstractAction;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * BagInfoForm
 *
 * @author gov.loc
 */
public class BagInfoForm extends AbstractForm implements FocusListener {
    protected static final Logger log = LoggerFactory.getLogger(BagInfoForm.class);

    private static final String INFO_FORM_PAGE = "infoPage";

    private JComponent focusField;
    private BagView bagView;
    private final HashMap<String, BagInfoField> fieldMap;
    private JComponent form;
    private AddFieldPanel addFieldPannel;

    /**
     * @param formModel FormModel
     * @param bagView BagView
     * @param map HashMap
     */
    public BagInfoForm(final FormModel formModel, final BagView bagView, final HashMap<String,
            BagInfoField> map) {
        super(formModel, INFO_FORM_PAGE);
        this.bagView = bagView;
        this.fieldMap = map;
    }

    /**
     * @param bagView BagView
     */
    public void setBagView(final BagView bagView) {
        this.bagView = bagView;
    }

    @Override
    protected JComponent createFormControl() {
        // add field panel
        final JPanel contentPanel = new JPanel(new GridBagLayout());
        int row = 0;
        final int col = 0;
        GridBagConstraints gbc = LayoutUtil
                .buildGridBagConstraints(col, row++, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST);
        addFieldPannel = new AddFieldPanel();
        contentPanel.add(addFieldPannel, gbc);

        gbc = LayoutUtil.buildGridBagConstraints(col, row++, 1, 1, 0, 0, GridBagConstraints
                        .HORIZONTAL,
                GridBagConstraints.CENTER);
        contentPanel.add(new JSeparator(), gbc);

        // bag-info input form
        form = createFormFields();
        gbc = LayoutUtil
                .buildGridBagConstraints(col, row++, 1, 1, 1, 1, GridBagConstraints.BOTH,
                        GridBagConstraints.WEST);
        contentPanel.add(form, gbc);
        return contentPanel;
    }

    private JComponent createFormFields() {
        final BagTableFormBuilder formBuilder = new BagTableFormBuilder(getBindingFactory());
        formBuilder.row();
        if (fieldMap != null && !fieldMap.isEmpty()) {
            createFormFieldsFromMap(formBuilder);//TODO
        }
        final JComponent fieldForm = formBuilder.getForm();
        fieldForm.invalidate();
        return fieldForm;
    }

    private void createFormFieldsFromMap(final BagTableFormBuilder formBuilder) {
        int rowCount = 0;
        final int index = 2;

        final Set<String> keys = fieldMap.keySet();
        for (final BagInfoField field : fieldMap.values()) {
            formBuilder.row();
            rowCount++;
            final ImageIcon imageIcon = bagView.getPropertyImage("bag.delete.image");
            JButton removeButton = new JButton(imageIcon);
            final Dimension dimension = removeButton.getPreferredSize();
            dimension.width = imageIcon.getIconWidth();
            removeButton.setMaximumSize(dimension);
            removeButton.setOpaque(false);
            removeButton.setBorderPainted(false);
            removeButton.setContentAreaFilled(false);
            removeButton.addActionListener(new RemoveFieldHandler());
            logger.debug("OrganizationInfoForm add: " + field);
            if (field.getValue() != null && field.getValue().length() > 60) {
                field.setComponentType(BagInfoField.TEXTAREA_COMPONENT);
            }
            if (field.isRequired()) {
                removeButton = new JButton();
                removeButton.setOpaque(false);
                removeButton.setBorderPainted(false);
                removeButton.setContentAreaFilled(false);
            }
            switch (field.getComponentType()) {
                case BagInfoField.TEXTAREA_COMPONENT:
                    final JComponent[] tlist =
                            formBuilder.addTextArea(field.isRequired(), field.getLabel(),
                                    removeButton);
                    final JComponent textarea = tlist[index];
                    textarea.setEnabled(field.isEnabled());
                    textarea.addFocusListener(this);
                    ((NoTabTextArea) textarea).setText(field.getValue());
                    textarea.setBorder(new EmptyBorder(1, 1, 1, 1));
                    ((NoTabTextArea) textarea).setLineWrap(true);
                    if (rowCount == 1) {
                        focusField = textarea;
                    }
                    break;
                case BagInfoField.TEXTFIELD_COMPONENT:
                    final JComponent[] flist = formBuilder.add(field.isRequired(), field.getLabel
                            (), removeButton);
                    final JComponent comp = flist[index];
                    comp.setEnabled(field.isEnabled());
                    comp.addFocusListener(this);
                    ((JTextField) comp).setText(field.getValue());
                    if (rowCount == 1) {
                        focusField = comp;
                    }
                    break;
                case BagInfoField.LIST_COMPONENT:
                    final List<String> elements = field.getElements();
                    final JComponent[] llist = formBuilder
                            .addList(field.isRequired(), field.getLabel(), elements, field
                                    .getValue(), removeButton);
                    final JComponent lcomp = llist[index];
                    lcomp.setEnabled(field.isEnabled());
                    lcomp.addFocusListener(this);
                    if (field.getValue() != null) {
                        ((JComboBox<?>) lcomp).setSelectedItem(field.getValue().trim());
                    }
                    if (rowCount == 1) {
                        focusField = lcomp;
                    }
                    break;
                default:
            }
        }
        if (focusField != null) {
            focusField.requestFocus();
        }

    }

    @Override
    public void focusGained(final FocusEvent evt) {
    }

    @Override
    public void focusLost(final FocusEvent evt) {
        final DefaultBag defaultBag = bagView.getBag();
        bagView.infoInputPane.updateBagHandler.updateBag(defaultBag);
        bagView.infoInputPane.bagInfoInputPane.setSelectedIndex(0);
    }

    private class RemoveFieldHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final Component selected = (Component) e.getSource();
            String key = "";
            final Component[] components = getFieldComponents();
            for (int i = 0; i < components.length; i++) {
                Component c;
                // See BagTableFormBuilder.addBinding for component info
                // Field label
                c = components[i];
                if (c instanceof JLabel) {
                    final JLabel label = (JLabel) c;
                    key = label.getText();
                }
                i++;
                // Required button
                c = components[i];
                i++;
                // Input text field
                c = components[i];
                i++;
                // Remove button
                c = components[i];
                if (c instanceof JButton && c == selected) {
                    final BagInfoField field = getField(key);
                    if (field != null) {
                        // remove field
                        bagView.getBag().removeBagInfoField(key);
                    }
                }
            }
            bagView.infoInputPane.updateInfoFormsPane();
        }
    }

    /**
     * @param key String
     * @return field
     */
    private BagInfoField getField(final String key) {
        final BagInfoField field;
        final Set<String> keys = fieldMap.keySet();
        for (final String keySet : keys) {
            if (keySet.equalsIgnoreCase(key)) {
                field = fieldMap.get(key);
                return field;
            }
        }
        return null;
    }

    /**
     * @return map
     */
    public HashMap<String, String> getBagInfoMap() {
        final HashMap<String, String> map = new HashMap<>();
        String key = "";
        String value = "";
        final Component[] components = getFieldComponents();
        for (int i = 0; i < components.length; i++) {
            Component c;
            c = components[i];
            if (c instanceof JLabel) {
                final JLabel label = (JLabel) c;
                key = label.getText();
            }
            i++;
            // Is required component
            c = components[i];
            i++;
            c = components[i];
            if (c instanceof JTextField) {
                final JTextField tf = (JTextField) c;
                value = tf.getText();
            } else if (c instanceof JTextArea) {
                final JTextArea ta = (JTextArea) c;
                value = ta.getText();
            } else if (c instanceof JComboBox) {
                final JComboBox<?> tb = (JComboBox<?>) c;
                value = (String) tb.getSelectedItem();
            }
            map.put(key, value);
            i++;
            c = components[i];
        }
        return map;
    }

    private Component[] getFieldComponents() {
        return form.getComponents();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        addFieldPannel.setEnabled(enabled);
    }

}
