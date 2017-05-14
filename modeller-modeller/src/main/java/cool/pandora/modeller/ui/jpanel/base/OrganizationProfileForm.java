package cool.pandora.modeller.ui.jpanel.base;

import cool.pandora.modeller.Contact;
import cool.pandora.modeller.Organization;
import cool.pandora.modeller.ui.BagTableFormBuilder;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;

import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * OrganizationProfileForm
 *
 * @author gov.loc
 */
public class OrganizationProfileForm extends AbstractForm implements FocusListener {
    private static final String PROFILE_FORM_PAGE = "profilePage";

    private JComponent contactName;
    private final BagView bagView;

    /**
     * @param formModel FormModel
     * @param bagView   BagView
     */
    public OrganizationProfileForm(final FormModel formModel, final BagView bagView) {
        super(formModel, PROFILE_FORM_PAGE);
        this.bagView = bagView;
    }

    @Override
    protected JComponent createFormControl() {
        final JComponent form = new JPanel();
        form.setLayout(new BorderLayout());
        final JComponent formFields = createFormFields();
        form.add(formFields, BorderLayout.CENTER);

        return form;
    }

    /**
     * @return fieldForm
     */
    private JComponent createFormFields() {
        final JComponent fieldForm;
        final BagTableFormBuilder formBuilder = new BagTableFormBuilder(getBindingFactory());
        formBuilder.row();
        formBuilder.addSeparator("Send from Organization");
        formBuilder.row();
        JComponent field = formBuilder.add("sourceOrganization")[1];
        final Organization organization = bagView.getBag().getProfile().getOrganization();
        if (organization != null && organization.getName().isReadOnly()) {
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        final JComponent orgAddress = formBuilder.add("organizationAddress")[1];
        if (organization != null && organization.getAddress().isReadOnly()) {
            field.setEnabled(false);
        }

        orgAddress.addFocusListener(this);

        final Contact fromContact = bagView.getBag().getProfile().getSendFromContact();

        formBuilder.row();
        formBuilder.addSeparator("Send from Contact");
        formBuilder.row();

        this.contactName = formBuilder.add("srcContactName")[1];

        if (fromContact != null && fromContact.getContactName().isReadOnly()) {
            field.setEnabled(false);
        }

        this.contactName.addFocusListener(this);
        formBuilder.row();
        field = formBuilder.add("srcContactPhone")[1];
        if (fromContact != null && fromContact.getTelephone().isReadOnly()) {
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        field = formBuilder.add("srcContactEmail")[1];

        if (fromContact != null && fromContact.getEmail().isReadOnly()) {
            field.setEnabled(false);
        }

        field.addFocusListener(this);
        formBuilder.row();
        formBuilder.addSeparator("Send to Contact");
        formBuilder.row();
        field = formBuilder.add("toContactName")[1];
        final Contact contact = bagView.getBag().getProfile().getSendToContact();
        if (contact != null && contact.getContactName().isReadOnly()) {
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        field = formBuilder.add("toContactPhone")[1];

        if (contact != null && contact.getTelephone().isReadOnly()) {
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        field = formBuilder.add("toContactEmail")[1];
        if (contact != null && contact.getEmail().isReadOnly()) {
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        this.contactName.requestFocus();
        fieldForm = formBuilder.getForm();
        return fieldForm;
    }

    /**
     * @return contactName
     */
    public boolean requestFocusInWindow() {
        return contactName.requestFocusInWindow();
    }

    @Override
    public void focusGained(final FocusEvent evt) {
    }

    @Override
    public void focusLost(final FocusEvent evt) {
        if (bagView != null && !this.hasErrors() && this.isDirty()) {
            bagView.infoInputPane.updateBagHandler.updateBag(bagView.getBag());
            bagView.infoInputPane.bagInfoInputPane.setSelectedIndex(1);
        }
    }
}