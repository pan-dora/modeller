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

package cool.pandora.modeller.bag.impl;

import cool.pandora.modeller.Contact;
import cool.pandora.modeller.Organization;
import cool.pandora.modeller.Profile;
import cool.pandora.modeller.ProfileField;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.BaggerSourceOrganization;
import cool.pandora.modeller.profile.BaggerProfileStore;
import gov.loc.repository.bagit.BagInfoTxt;
import gov.loc.repository.bagit.impl.BagInfoTxtImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.Map.Entry;

/**
 * DefaultBagInfo
 *
 * @author loc.gov
 */
public class DefaultBagInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    protected static final Logger log = LoggerFactory.getLogger(DefaultBagInfo.class);

    public static final String FIELD_LC_PROJECT = "Profile Name";

    private static final String[] ORGANIZATION_CONTACT_FIELDS =
            {BagInfoTxtImpl.FIELD_SOURCE_ORGANIZATION, BagInfoTxtImpl.FIELD_ORGANIZATION_ADDRESS,
                    BagInfoTxtImpl.FIELD_CONTACT_NAME, BagInfoTxtImpl.FIELD_CONTACT_PHONE,
                    BagInfoTxtImpl.FIELD_CONTACT_EMAIL, Contact.FIELD_TO_CONTACT_NAME, Contact
                    .FIELD_TO_CONTACT_PHONE,
                    Contact.FIELD_TO_CONTACT_EMAIL};

    private static final String[] MANIFEST_PROPERTY_FIELDS =
            {ManifestPropertiesImpl.FIELD_ATTRIBUTION, ManifestPropertiesImpl.FIELD_AUTHOR,
                    ManifestPropertiesImpl.FIELD_LABEL, ManifestPropertiesImpl.FIELD_LICENSE,
                    ManifestPropertiesImpl.FIELD_INSTITUTION_LOGO_URI, ManifestPropertiesImpl
                    .FIELD_DESCRIPTION,
                    ManifestPropertiesImpl.FIELD_PUBLISHED, ManifestPropertiesImpl.FIELD_RENDERING,
                    ManifestPropertiesImpl.FIELD_RENDERING_FORMAT, ManifestPropertiesImpl
                    .FIELD_RENDERING_LABEL};

    private static final HashSet<String> ORGANIZATION_CONTACT_FIELD_SET =
            new HashSet<>(Arrays.asList(ORGANIZATION_CONTACT_FIELDS));

    private static final HashSet<String> MANIFEST_PROPERTY_FIELD_SET =
            new HashSet<>(Arrays.asList(MANIFEST_PROPERTY_FIELDS));

    private BaggerSourceOrganization sourceOrganization = new BaggerSourceOrganization();
    private Contact toContact = new Contact(true);
    private LinkedHashMap<String, BagInfoField> fieldMap = new LinkedHashMap<>();

    /**
     * @return sourceOrganization
     */
    public BaggerSourceOrganization getBagOrganization() {
        return this.sourceOrganization;
    }

    /**
     * @return fieldMap
     */
    public HashMap<String, BagInfoField> getFieldMap() {
        return this.fieldMap;
    }

    /**
     * @param field BagInfoField
     */
    void addField(final BagInfoField field) {
        fieldMap.put(field.getName(), field);
    }

    /**
     * @param bagInfoTxt BagInfoTxt
     */
    public void update(final BagInfoTxt bagInfoTxt) {
        updateBagInfoFieldMapFromBilBag(bagInfoTxt);
        sourceOrganization = new BaggerSourceOrganization(bagInfoTxt);
        toContact = new Contact(true);
        toContact.setContactName(ProfileField.createProfileField(Contact.FIELD_TO_CONTACT_NAME,
                bagInfoTxt.getOrDefault(Contact.FIELD_TO_CONTACT_NAME, "")));

        toContact.setTelephone(ProfileField.createProfileField(Contact.FIELD_TO_CONTACT_PHONE,
                bagInfoTxt.getOrDefault(Contact.FIELD_TO_CONTACT_PHONE, "")));

        toContact.setEmail(ProfileField.createProfileField(Contact.FIELD_TO_CONTACT_EMAIL,
                bagInfoTxt.getOrDefault(Contact.FIELD_TO_CONTACT_EMAIL, "")));

        for (final String key : bagInfoTxt.keySet()) {
            final BagInfoField infoField = new BagInfoField();
            infoField.setLabel(key);
            infoField.setName(key);
            infoField.setValue(bagInfoTxt.get(key));
            infoField.isEditable(true);
            infoField.isEnabled(true);
            fieldMap.put(key, infoField);
        }

    }

    /**
     * @param bagInfoTxt BagInfoTxt
     */
    private void updateBagInfoFieldMapFromBilBag(final BagInfoTxt bagInfoTxt) {
        if (fieldMap != null) {
            for (final Entry<String, BagInfoField> entry : fieldMap.entrySet()) {
                final String value = bagInfoTxt.get(entry.getKey());
                entry.getValue().setValue(value);
            }
        }
    }

    /**
     * @param profile Profile
     * @param newBag boolean
     */
    void setProfile(final Profile profile, final boolean newBag) {
        if (newBag) {
            // if this is a new bag, populate organization and contacts with profile
            // info
            Contact person = profile.getSendToContact();
            if (person == null) {
                person = new Contact(true);
            }
            Contact contact = profile.getSendFromContact();
            if (contact == null) {
                contact = new Contact(false);
            }
            sourceOrganization.setContact(contact);
            Organization org = profile.getOrganization();
            if (org == null) {
                org = new Organization();
            }
            sourceOrganization.setOrganizationName(org.getName().getFieldValue());
            sourceOrganization.setOrganizationAddress(org.getAddress().getFieldValue());

            this.toContact = person;
        }

        applyProfileToFieldMap(profile);
    }

    /**
     * @param profile Profile
     */
    private void applyProfileToFieldMap(final Profile profile) {
        if (profile != null) {
            if (profile.isNoProfile()) {
                if (fieldMap.containsKey(DefaultBagInfo.FIELD_LC_PROJECT)) {
                    fieldMap.remove(DefaultBagInfo.FIELD_LC_PROJECT);
                }
            } else {
                final BagInfoField field = new BagInfoField();
                field.setLabel(DefaultBagInfo.FIELD_LC_PROJECT);
                field.setName(DefaultBagInfo.FIELD_LC_PROJECT);
                field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
                field.isEnabled(false);
                field.isEditable(false);
                field.isRequiredvalue(true);
                field.isRequired(true);
                field.setValue(profile.getName());
                field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
                fieldMap.put(field.getLabel(), field);
            }

            final List<ProfileField> list = BaggerProfileStore.getInstance().getProfileFields
                    (profile.getName());
            final LinkedHashMap<String, ProfileField> profileFields = convertToMap(list);

            if (fieldMap.size() > 0) {
                for (final BagInfoField field : fieldMap.values()) {
                    final ProfileField projectProfile = profileFields.get(field.getLabel());
                    if (projectProfile == null) {
                        continue;
                    }

                    field.isEnabled(!projectProfile.isReadOnly());
                    field.isEditable(!projectProfile.isReadOnly());
                    field.isRequiredvalue(projectProfile.getIsValueRequired());
                    field.isRequired(projectProfile.getIsRequired());
                    // field.setValue(projectProfile.getFieldValue());
                    field.buildElements(projectProfile.getElements());
                    if (projectProfile.getFieldType().equalsIgnoreCase(BagInfoField
                            .TEXTFIELD_CODE)) {
                        field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
                    } else if (projectProfile.getFieldType().equalsIgnoreCase(BagInfoField
                            .TEXTAREA_CODE)) {
                        field.setComponentType(BagInfoField.TEXTAREA_COMPONENT);
                    } else if (!(projectProfile.getElements().isEmpty())) {
                        field.setComponentType(BagInfoField.LIST_COMPONENT);
                    }
                }
            }

            final LinkedHashMap<String, ProfileField> exclusiveProfileFields = new
                    LinkedHashMap<>();
            exclusiveProfileFields.putAll(profileFields);
            exclusiveProfileFields.keySet().removeAll(fieldMap.keySet());

            if (exclusiveProfileFields.size() > 0) {
                for (final ProfileField profileField : exclusiveProfileFields.values()) {
                    if (profileField != null) {
                        final BagInfoField field = new BagInfoField();
                        field.setLabel(profileField.getFieldName());
                        field.setName(field.getLabel());
                        field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
                        field.isEnabled(!profileField.isReadOnly());
                        field.isEditable(!profileField.isReadOnly());
                        field.isRequiredvalue(profileField.getIsValueRequired());
                        field.isRequired(profileField.getIsRequired());
                        field.setValue(profileField.getFieldValue());
                        // field.setValue("");
                        if (profileField.isReadOnly()) {
                            field.isEnabled(false);
                        }
                        field.buildElements(profileField.getElements());
                        if (profileField.getFieldType().equalsIgnoreCase(BagInfoField
                                .TEXTFIELD_CODE)) {
                            field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
                        } else if (profileField.getFieldType().equalsIgnoreCase(BagInfoField
                                .TEXTAREA_CODE)) {
                            field.setComponentType(BagInfoField.TEXTAREA_COMPONENT);
                        } else if (!(profileField.getElements().isEmpty())) {
                            field.setComponentType(BagInfoField.LIST_COMPONENT);
                        }
                        fieldMap.put(field.getLabel(), field);
                    }
                }
            }
        }
    }

    /**
     * @param profileFields LinkedHashMap
     * @return filedsToReturn
     */
    private static LinkedHashMap<String, ProfileField> convertToMap(final List<ProfileField>
                                                                            profileFields) {
        final LinkedHashMap<String, ProfileField> filedsToReturn = new LinkedHashMap<>();
        if (profileFields == null) {
            return filedsToReturn;
        }
        for (final ProfileField profileFiled : profileFields) {
            filedsToReturn.put(profileFiled.getFieldName(), profileFiled);
        }
        return filedsToReturn;
    }

    /**
     *
     */
    void clearFields() {
        fieldMap = new LinkedHashMap<>();
    }

    /**
     * @param key String
     */
    void removeField(final String key) {
        fieldMap.remove(key);
    }

    /**
     * @param fieldName String
     * @return boolean
     */
    public static boolean isOrganizationContactField(final String fieldName) {
        return ORGANIZATION_CONTACT_FIELD_SET.contains(fieldName);
    }

    /**
     * @param fieldName String
     * @return boolean
     */
    public static boolean isManifestPropertyField(final String fieldName) {
        return MANIFEST_PROPERTY_FIELD_SET.contains(fieldName);
    }

    /**
     * @param bagInfoTxt BagInfoTxt
     */
    void prepareBilBagInfo(final BagInfoTxt bagInfoTxt) {
        bagInfoTxt.clear();

        for (final Entry<String, BagInfoField> entry : fieldMap.entrySet()) {
            bagInfoTxt.put(entry.getKey(), entry.getValue().getValue());
        }

        updateBagInfoTxtWithOrganizationInformation(bagInfoTxt);
    }

    /**
     * @param bagInfoTxt BagInfoTxt
     */
    private void updateBagInfoTxtWithOrganizationInformation(final BagInfoTxt bagInfoTxt) {
        if (!sourceOrganization.getOrganizationName().trim().isEmpty()) {
            bagInfoTxt.setSourceOrganization(sourceOrganization.getOrganizationName().trim());
        }
        if (!sourceOrganization.getOrganizationAddress().trim().isEmpty()) {
            bagInfoTxt.setOrganizationAddress(sourceOrganization.getOrganizationAddress().trim());
        }
        final Contact contact = sourceOrganization.getContact();
        if (!contact.getContactName().getFieldValue().trim().isEmpty()) {
            bagInfoTxt.setContactName(contact.getContactName().getFieldValue().trim());
        }
        if (!contact.getTelephone().getFieldValue().trim().isEmpty()) {
            bagInfoTxt.setContactPhone(contact.getTelephone().getFieldValue().trim());
        }
        if (!contact.getEmail().getFieldValue().trim().isEmpty()) {
            bagInfoTxt.setContactEmail(contact.getEmail().getFieldValue().trim());
        }
        if (!toContact.getContactName().getFieldValue().trim().isEmpty()) {
            bagInfoTxt.put(Contact.FIELD_TO_CONTACT_NAME, toContact.getContactName()
                    .getFieldValue());
        }
        if (!toContact.getTelephone().getFieldValue().trim().isEmpty()) {
            bagInfoTxt.put(Contact.FIELD_TO_CONTACT_PHONE, toContact.getTelephone().getFieldValue
                    ().trim());
        }
        if (!toContact.getEmail().getFieldValue().trim().isEmpty()) {
            bagInfoTxt.put(Contact.FIELD_TO_CONTACT_EMAIL, toContact.getEmail().getFieldValue()
                    .trim());
        }

    }

    /**
     * @return toContact
     */
    public Contact getToContact() {
        return toContact;
    }

    /**
     * @param toContact Contact
     */
    public void setToContact(final Contact toContact) {
        this.toContact = toContact;
    }

    /**
     * @param map Map
     */
    public void update(final Map<String, String> map) {
        for (final Entry<String, String> entry : map.entrySet()) {
            if (fieldMap.get(entry.getKey()) != null) {
                fieldMap.get(entry.getKey()).setValue(entry.getValue());
            }
        }
    }

}