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

package cool.pandora.modeller.bag;

import cool.pandora.modeller.Contact;
import cool.pandora.modeller.ProfileField;
import gov.loc.repository.bagit.BagInfoTxt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Simple JavaBean domain object representing an organization.
 * <p>
 * | (Source-organization: California Digital Library )
 * | (Organization-address: 415 20th Street, 4th Floor, Oakland, CA. 94612 )
 * | (Contact-name: A. E. Newman )
 * | (Contact-phone: +1 510-555-1234 )
 * | (Contact-email: alfred@ucop.edu )
 *
 * @author Jon Steinbach
 */
public class BaggerSourceOrganization implements Serializable {
    private static final long serialVersionUID = 1L;

    protected static final Logger log = LoggerFactory.getLogger(BaggerSourceOrganization.class);

    private String organizationName = "";

    private String organizationAddress = "";

    private Contact contact = new Contact(false);

    /**
     *
     */
    public BaggerSourceOrganization() {
    }

    /**
     * @param bagInfoTxt BagInfoTxt
     */
    public BaggerSourceOrganization(final BagInfoTxt bagInfoTxt) {
        contact = new Contact(false);
        if (bagInfoTxt.getContactName() != null && !bagInfoTxt.getContactName().trim().isEmpty()) {
            contact.setContactName(
                    ProfileField.createProfileField(Contact.FIELD_CONTACT_NAME, bagInfoTxt
                            .getContactName()));
        } else {
            contact.setContactName(ProfileField.createProfileField(Contact.FIELD_CONTACT_NAME, ""));
        }
        if (bagInfoTxt.getContactPhone() != null && !bagInfoTxt.getContactPhone().trim().isEmpty
                ()) {
            contact.setTelephone(
                    ProfileField.createProfileField(Contact.FIELD_CONTACT_PHONE, bagInfoTxt
                            .getContactPhone()));
        } else {
            contact.setTelephone(ProfileField.createProfileField(Contact.FIELD_CONTACT_PHONE, ""));
        }
        if (bagInfoTxt.getContactEmail() != null && !bagInfoTxt.getContactEmail().trim().isEmpty
                ()) {
            contact.setEmail(
                    ProfileField.createProfileField(Contact.FIELD_CONTACT_EMAIL, bagInfoTxt
                            .getContactEmail()));
        } else {
            contact.setEmail(ProfileField.createProfileField(Contact.FIELD_CONTACT_EMAIL, ""));
        }

        if (bagInfoTxt.getSourceOrganization() != null && !bagInfoTxt.getSourceOrganization()
                .trim().isEmpty()) {
            setOrganizationName(bagInfoTxt.getSourceOrganization());
        } else {
            setOrganizationName("");
        }
        if (bagInfoTxt.getOrganizationAddress() != null && !bagInfoTxt.getOrganizationAddress()
                .trim().isEmpty()) {
            setOrganizationAddress(bagInfoTxt.getOrganizationAddress());
        } else {
            setOrganizationAddress("");
        }
    }

    /**
     * @return organizationName
     */
    public String getOrganizationName() {
        return this.organizationName;
    }

    /**
     * @param name String
     */
    public void setOrganizationName(final String name) {
        this.organizationName = name;
    }

    /**
     * @return organizationAddress
     */
    public String getOrganizationAddress() {
        return this.organizationAddress;
    }

    /**
     * @param address String
     */
    public void setOrganizationAddress(final String address) {
        this.organizationAddress = address;
    }

    /**
     * @return contact
     */
    public Contact getContact() {
        return this.contact;
    }

    /**
     * @param contact Contact
     */
    public void setContact(final Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        log.info("SourceOrganization.toString");

        return this.organizationName + '\n' + this.organizationAddress + '\n' + this.contact
                .getContactName() + '\n' +
                this.contact.getTelephone() + '\n' + this.contact.getEmail() + '\n';
    }

}
