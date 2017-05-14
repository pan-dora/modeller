package cool.pandora.modeller.ui;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.form.binding.BindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.layout.TableLayoutBuilder;

import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

/**
 * BagTableFormBuilder
 *
 * @author gov.loc
 */
public class BagTableFormBuilder extends TableFormBuilder {
    /**
     * @param bindingFactory BindingFactory
     */
    public BagTableFormBuilder(final BindingFactory bindingFactory) {
        super(bindingFactory);
    }

    private ComponentFactory componentFactory;

    /**
     * @return componentFactory
     */
    @Override
    protected ComponentFactory getComponentFactory() {
        if (componentFactory == null) {
            componentFactory =
                    (ComponentFactory) ApplicationServicesLocator.services().getService(ComponentFactory.class);
        }
        return componentFactory;
    }

    /**
     * @param isRequired boolean
     * @param label      String
     * @param checkbox   JComponent
     * @return addBinding
     */
    public JComponent[] add(final boolean isRequired, final String label, final JComponent checkbox) {
        final JComponent textField = new JTextField();
        return addBinding(isRequired, label, textField, checkbox);
    }

    /**
     * @param isRequired   boolean
     * @param label        String
     * @param elements     Collection
     * @param defaultValue String
     * @param checkbox     JComponent
     * @return addBinding
     */
    public JComponent[] addList(final boolean isRequired, final String label, final Collection<String> elements,
                                final String defaultValue, final JComponent checkbox) {
        final ArrayList<String> listModel = new ArrayList<>();
        listModel.addAll(elements);

        // Set default value selected from value list
        final JComboBox<String> dropDownTextField = new JComboBox<>(listModel.toArray(new String[listModel.size()]));
        dropDownTextField.setSelectedItem(defaultValue);
        final Object obj = dropDownTextField.getSelectedItem();
        dropDownTextField.setSelectedItem(obj);

        return addBinding(isRequired, label, dropDownTextField, checkbox);
    }

    /**
     * @param isRequired boolean
     * @param label      String
     * @param checkbox   JComponent
     * @return addBinding
     */
    public JComponent[] addTextArea(final boolean isRequired, final String label, final JComponent checkbox) {
        final JComponent textArea = new NoTabTextArea(3, 40);
        // Binding binding = createBinding(fieldName, textArea);
        // TODO: using the JScrollPane component causes the validation 'x' to
        // disappear
        // JComponent wrappedComponent = new JScrollPane(textArea)
        return addBinding(isRequired, label, textArea, checkbox);
    }

    /**
     * @param isRequired   boolean
     * @param labelName    String
     * @param component    JComponent
     * @param removeButton JComponent
     * @return JComponent
     */
    private JComponent[] addBinding(final boolean isRequired, final String labelName, final JComponent component,
                                    final JComponent removeButton) {
        removeButton.setFocusable(false);
        final JLabel label = new JLabel(labelName); // createLabelFor(fieldName,
        // component);
        label.setToolTipText("Double-Click to Edit");
        final TableLayoutBuilder layoutBuilder = getLayoutBuilder();
        if (!layoutBuilder.hasGapToLeft()) {
            layoutBuilder.gapCol();
        }
        layoutBuilder.cell(label, "colSpec=left:pref:noGrow");
        final JComponent reqComp;
    /* */
        if (isRequired) {
            final JButton b = new JButton("R");
            b.setForeground(Color.red);
            b.setOpaque(false);
            b.setBorderPainted(false);
            reqComp = b;
        } else {
            final JButton b = new JButton("");
            b.setOpaque(false);
            b.setBorderPainted(false);
            reqComp = b;
        }
    /* */
        reqComp.setFocusable(false);
        layoutBuilder.cell(reqComp, "colSpec=left:pref:noGrow");
        layoutBuilder.cell(component, "colSpec=fill:pref:grow");
        layoutBuilder.labelGapCol();
        layoutBuilder.cell(removeButton, "colSpec=left:pref:noGrow");
        layoutBuilder.labelGapCol();
        return new JComponent[]{label, reqComp, component, removeButton};
    }
}