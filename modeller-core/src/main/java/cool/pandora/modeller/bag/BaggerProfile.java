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
public class BaggerProfile {
    private BaggerSourceOrganization sourceOrganization = new BaggerSourceOrganization();
    private Contact toContact = new Contact(true);

    /**
     * @return sourceOrganization
     */
    public BaggerSourceOrganization getOrganization() {
        return this.sourceOrganization;
    }

    /**
     * @param organization BaggerSourceOrganization
     */
    public void setOrganization(final BaggerSourceOrganization organization) {
        this.sourceOrganization = organization;
    }

    /**
     * @return contact
     */
    public Contact getSourceContact() {
        return this.sourceOrganization.getContact();
    }

    /**
     * @param contact Contact
     */
    public void setSourceContact(final Contact contact) {
        this.sourceOrganization.setContact(contact);
    }

    /**
     * @return toContact
     */
    public Contact getToContact() {
        return this.toContact;
    }

    /**
     * @param contact Contact
     */
    public void setToContact(final Contact contact) {
        this.toContact = contact;
    }

    /**
     * @return organizationName
     */
    public String getSourceOrganization() {
        return this.sourceOrganization.getOrganizationName();
    }

    /**
     * @param name String
     */
    public void setSourceOrganization(final String name) {
        this.sourceOrganization.setOrganizationName(name);
    }

    /**
     * @return organizationAddress
     */
    public String getOrganizationAddress() {
        return this.sourceOrganization.getOrganizationAddress();
    }

    /**
     * @param address String
     */
    public void setOrganizationAddress(final String address) {
        this.sourceOrganization.setOrganizationAddress(address);
    }

    /**
     * @return contactName
     */
    public String getSrcContactName() {
        return this.sourceOrganization.getContact().getContactName().getFieldValue();
    }

    /**
     * @param name String
     */
    public void setSrcContactName(final String name) {
        this.sourceOrganization.getContact().getContactName().setFieldValue(name);
    }

    /**
     * @return contactPhone
     */
    public String getSrcContactPhone() {
        return this.sourceOrganization.getContact().getTelephone().getFieldValue();
    }

    /**
     * @param phone String
     */
    public void setSrcContactPhone(final String phone) {
        this.sourceOrganization.getContact().getTelephone().setFieldValue(phone);
    }

    /**
     * @return contactEmail
     */
    public String getSrcContactEmail() {
        return this.sourceOrganization.getContact().getEmail().getFieldValue();
    }

    /**
     * @param email String
     */
    public void setSrcContactEmail(final String email) {
        this.sourceOrganization.getContact().getEmail().setFieldValue(email);
    }

    /**
     * @return contactName
     */
    public String getToContactName() {
        return this.toContact.getContactName().getFieldValue();
    }

    /**
     * @param name String
     */
    public void setToContactName(final String name) {
        this.toContact.getContactName().setFieldValue(name);
    }

    /**
     * @return contactPhone
     */
    public String getToContactPhone() {
        return this.toContact.getTelephone().getFieldValue();
    }

    /**
     * @param phone String
     */
    public void setToContactPhone(final String phone) {
        this.toContact.getTelephone().setFieldValue(phone);
    }

    /**
     * @return contactEmail
     */
    public String getToContactEmail() {
        return this.toContact.getEmail().getFieldValue();
    }

    /**
     * @param email String
     */
    public void setToContactEmail(final String email) {
        this.toContact.getEmail().setFieldValue(email);
    }
}
