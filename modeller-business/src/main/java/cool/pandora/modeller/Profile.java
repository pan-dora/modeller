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

package cool.pandora.modeller;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;


/**
 * Profile.
 *
 * @author gov.loc
 */
public class Profile {
    public static final String NO_PROFILE_NAME = "<no profile>";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_ORGANIZATION = "Organization";
    private static final String FIELD_SENDTO = "Send-To";
    private static final String FIELD_SENDFROM = "Send-From";
    private static final String FIELD_CUSTOM_INFO = "Custom-info";
    private static final String FIELD_STANDARD_INFO = "Standard-info";

    private Contact sendToContact = new Contact(true);
    private Contact sendFromContact = new Contact(false);
    private Organization organization = new Organization();
    private String name;
    private boolean isDefault = false;
    private LinkedHashMap<String, ProfileField> customFields = new LinkedHashMap<>();
    private LinkedHashMap<String, ProfileField> standardFields = new LinkedHashMap<>();

    /**
     * createProfile.
     *
     * @param profileJson JSONObject
     * @param profileName String
     * @return profile
     * @throws JSONException exception
     */
    public static Profile createProfile(final JSONObject profileJson, final String profileName)
            throws JSONException {
        final Profile profile = new Profile();
        profile.setName(profileName);

        JSONObject organizationJson = null;
        if (profileJson.has(Profile.FIELD_ORGANIZATION)) {
            organizationJson = (JSONObject) profileJson.get(Profile.FIELD_ORGANIZATION);
        }

        final Organization organization = Organization.createOrganization(organizationJson);
        profile.setOrganization(organization);

        JSONObject contactSendToJson = null;
        if (profileJson.has(Profile.FIELD_SENDTO)) {
            contactSendToJson = (JSONObject) profileJson.get(Profile.FIELD_SENDTO);
        }

        final Contact sendToContact = Contact.createContact(contactSendToJson, true);
        profile.setSendToContact(sendToContact);

        JSONObject contactSendFromJson = null;
        if (profileJson.has(Profile.FIELD_SENDFROM)) {
            contactSendFromJson = (JSONObject) profileJson.get(Profile.FIELD_SENDFROM);
        }

        final Contact sendFromContact = Contact.createContact(contactSendFromJson, false);
        profile.setSendFromContact(sendFromContact);

        JSONObject customInfoJson = null;
        if (profileJson.has(Profile.FIELD_CUSTOM_INFO)) {
            customInfoJson = (JSONObject) profileJson.get(Profile.FIELD_CUSTOM_INFO);
        }
        final LinkedHashMap<String, ProfileField> fields = getFields(customInfoJson);
        profile.setCustomFields(fields);

        final LinkedHashMap<String, ProfileField> profileFields = getFields(profileJson);
        profile.setStandardFields(profileFields);

        return profile;
    }

    /**
     * getFields.
     *
     * @param fieldsJson JSONObject
     * @return profileFields
     * @throws JSONException exception
     */
    private static LinkedHashMap<String, ProfileField> getFields(final JSONObject fieldsJson)
            throws JSONException {
        final LinkedHashMap<String, ProfileField> profileFields = new LinkedHashMap<>();
        if (fieldsJson != null) {
            final String[] names = JSONObject.getNames(fieldsJson);
            if (names == null) {
                return profileFields;
            }

            if (fieldsJson.has("ordered")) {
                final JSONArray orderedFields = (JSONArray) fieldsJson.get("ordered");
                for (final Object obj : orderedFields) {
                    final JSONObject field = (JSONObject) obj;
                    profileFields.putAll(getFields(field));
                }
            } else {
                for (final String name : names) {
                    final JSONObject jsonObject = (JSONObject) fieldsJson.get(name);
                    final ProfileField profileField =
                            ProfileField.createProfileField(jsonObject, name);
                    profileFields.put(profileField.getFieldName(), profileField);
                }
            }
        }
        return profileFields;
    }

    /**
     * seralizeFields.
     *
     * @param profileFields Collection
     * @return String
     * @throws JSONException exception
     */
    private static String seralizeFields(final Collection<ProfileField> profileFields)
            throws JSONException {
        final StringWriter writer = new StringWriter();
        final JSONWriter filedWriter = new JSONWriter(writer);
        filedWriter.object();
        for (final ProfileField field : profileFields) {
            final String fieldStringer = field.serialize();
            filedWriter.key(field.getFieldName())
                    .value(new JSONObject(new JSONTokener(fieldStringer)));
        }
        filedWriter.endObject();
        return writer.toString();
    }

    /**
     * getSendToContact.
     *
     * @return sendToContact
     */
    public Contact getSendToContact() {
        return sendToContact;
    }

    /**
     * setSendToContact.
     *
     * @param sendToContact Contact
     */
    private void setSendToContact(final Contact sendToContact) {
        this.sendToContact = sendToContact;
    }

    /**
     * getSendFromContact.
     *
     * @return sendFromContact
     */
    public Contact getSendFromContact() {
        return sendFromContact;
    }

    /**
     * setSendFromContact.
     *
     * @param sendFromContact Contact
     */
    private void setSendFromContact(final Contact sendFromContact) {
        this.sendFromContact = sendFromContact;
    }

    /**
     * getOrganization.
     *
     * @return organization
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * setOrganization.
     *
     * @param organization Organization
     */
    private void setOrganization(final Organization organization) {
        this.organization = organization;
    }

    /**
     * getName.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * setName.
     *
     * @param profileName String
     */
    public void setName(final String profileName) {
        this.name = profileName;
    }

    /**
     * getCustomFields.
     *
     * @return customFields
     */
    public LinkedHashMap<String, ProfileField> getCustomFields() {
        return customFields;
    }

    /**
     * setCustomFields.
     *
     * @param fields LinkedHashMap
     */
    private void setCustomFields(final LinkedHashMap<String, ProfileField> fields) {
        this.customFields = fields;
    }

    @Override
    public String toString() {
        return "";
    }

    /**
     * getStandardFields.
     *
     * @return standardFields
     */
    public LinkedHashMap<String, ProfileField> getStandardFields() {
        return standardFields;
    }

    /**
     * setStandardFields.
     *
     * @param standardFields LinkedHashMap
     */
    private void setStandardFields(final LinkedHashMap<String, ProfileField> standardFields) {
        this.standardFields = standardFields;
    }

    /**
     * setIsDefault.
     */
    public void setIsDefault() {
        this.isDefault = true;
    }

    /**
     * getIsDefault.
     *
     * @return isDefault
     */
    public boolean getIsDefault() {
        return isDefault;
    }

    /**
     * serialize.
     *
     * @param jsonWriter JSONWriter
     * @throws JSONException exception
     */
    public void serialize(final JSONWriter jsonWriter) throws JSONException {

        final JSONWriter writer = jsonWriter.object().key(Profile.FIELD_NAME).value(getName());
        final String orgStringer = getOrganization().serialize();
        final String fromContact = getSendFromContact().serialize();
        final String toContact = getSendToContact().serialize();
        final String localCustomFields = seralizeFields(this.getCustomFields().values());
        final String localStandardFields = seralizeFields(this.getStandardFields().values());
        writer.key(FIELD_ORGANIZATION).value(new JSONObject(new JSONTokener(orgStringer)));
        writer.key(FIELD_SENDFROM).value(new JSONObject(new JSONTokener(fromContact)));
        writer.key(FIELD_SENDTO).value(new JSONObject(new JSONTokener(toContact)));
        writer.key(FIELD_CUSTOM_INFO).value(new JSONObject(new JSONTokener(localCustomFields)));
        writer.key(FIELD_STANDARD_INFO).value(new JSONObject(new JSONTokener(localStandardFields)));
        writer.endObject();
    }

    /**
     * isNoProfile.
     *
     * @return boolean
     */
    public boolean isNoProfile() {
        return NO_PROFILE_NAME.equals(getName());
    }

    /**
     * getProfileFields.
     *
     * @return fields
     */
    public List<ProfileField> getProfileFields() {
        final ArrayList<ProfileField> fields = new ArrayList<>();

        for (final Map.Entry<String, ProfileField> entry : standardFields.entrySet()) {
            fields.add(entry.getValue());
        }

        for (final Map.Entry<String, ProfileField> entry : customFields.entrySet()) {
            fields.add(entry.getValue());
        }

        return fields;
    }
}
