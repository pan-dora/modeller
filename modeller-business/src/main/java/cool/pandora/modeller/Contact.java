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

import java.io.Serializable;
import java.io.StringWriter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

/**
 * | (Contact-Name: Edna Janssen )
 * | (Contact-Phone: +1 408-555-1212 )
 * | (Contact-Nmail: ej@spengler.edu )
 *
 * @author Jon Steinbach
 */
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private ProfileField contactName;
    private ProfileField telephone;
    private ProfileField email;

    public static final String FIELD_CONTACT_NAME = "Contact-Name";
    public static final String FIELD_CONTACT_PHONE = "Contact-Phone";
    public static final String FIELD_CONTACT_EMAIL = "Contact-Email";

    public static final String FIELD_TO_CONTACT_NAME = "To-Contact-Name";
    public static final String FIELD_TO_CONTACT_PHONE = "To-Contact-Phone";
    public static final String FIELD_TO_CONTACT_EMAIL = "To-Contact-Email";

    private static final String FIELD_JSON_NAME = "name";
    private static final String FIELD_JSON_PHONE = "phone";
    private static final String FIELD_JSON_EMAIL = "email";

    /**
     * Contact.
     */
    public Contact() {
    }

    /**
     * Contact.
     *
     * @param isSentTo boolean
     */
    public Contact(final boolean isSentTo) {
        final String name = isSentTo ? FIELD_TO_CONTACT_NAME : FIELD_CONTACT_NAME;
        final String phone = isSentTo ? FIELD_TO_CONTACT_PHONE : FIELD_CONTACT_PHONE;
        final String mail = isSentTo ? FIELD_TO_CONTACT_EMAIL : FIELD_CONTACT_EMAIL;

        contactName = new ProfileField();
        contactName.setFieldName(name);

        telephone = new ProfileField();
        telephone.setFieldName(phone);

        email = new ProfileField();
        email.setFieldName(mail);
    }

    /**
     * getContactName.
     *
     * @return contactName
     */
    public ProfileField getContactName() {
        return this.contactName;
    }

    /**
     * setContactName.
     *
     * @param name contactName
     */
    public void setContactName(final ProfileField name) {
        this.contactName = name;
    }

    /**
     * getTelephone.
     *
     * @return telephone
     */
    public ProfileField getTelephone() {
        return this.telephone;
    }

    /**
     * setTelephone.
     *
     * @param telephone telephone
     */
    public void setTelephone(final ProfileField telephone) {
        this.telephone = telephone;
    }

    /**
     * getEmail.
     *
     * @return email
     */
    public ProfileField getEmail() {
        return this.email;
    }

    /**
     * setEmail.
     *
     * @param email email
     */
    public void setEmail(final ProfileField email) {
        this.email = email;
    }

    @Override
    public String toString() {
        // sb.append(this.getContactName());
        // sb.append('\n');
        // sb.append(this.getTelephone());
        // sb.append('\n');
        // sb.append(this.getEmail());
        // sb.append('\n');

        return "";
    }

    /**
     * createContact.
     *
     * @param contactSendToJson JSONObject
     * @param sendTo boolean
     * @return contact
     * @throws JSONException exception
     */
    static Contact createContact(final JSONObject contactSendToJson, final boolean sendTo) throws
            JSONException {
        final Contact contact = new Contact();
        final String name = sendTo ? FIELD_TO_CONTACT_NAME : FIELD_CONTACT_NAME;
        final String phone = sendTo ? FIELD_TO_CONTACT_PHONE : FIELD_CONTACT_PHONE;
        final String email = sendTo ? FIELD_TO_CONTACT_EMAIL : FIELD_CONTACT_EMAIL;

        ProfileField namefield = null;
        ProfileField phonefield = null;
        ProfileField emailfield = null;

        if (contactSendToJson != null) {
            if (contactSendToJson.has(FIELD_JSON_NAME)) {
                final JSONObject nameJson = (JSONObject) contactSendToJson.get(FIELD_JSON_NAME);
                if (nameJson != null) {
                    namefield = ProfileField.createProfileField(nameJson, name);
                }
            }

            if (contactSendToJson.has(FIELD_JSON_PHONE)) {
                final JSONObject phoneJson = (JSONObject) contactSendToJson.get(FIELD_JSON_PHONE);
                if (phoneJson != null) {
                    phonefield = ProfileField.createProfileField(phoneJson, phone);
                }
            }

            if (contactSendToJson.has(FIELD_JSON_EMAIL)) {
                final JSONObject emailJson = (JSONObject) contactSendToJson.get(FIELD_JSON_EMAIL);
                if (emailJson != null) {
                    emailfield = ProfileField.createProfileField(emailJson, email);
                }
            }
        }

        if (namefield == null) {
            namefield = new ProfileField();
            namefield.setFieldName(name);
        }

        if (phonefield == null) {
            phonefield = new ProfileField();
            phonefield.setFieldName(phone);
        }

        if (emailfield == null) {
            emailfield = new ProfileField();
            emailfield.setFieldName(email);
        }

        contact.setContactName(namefield);
        contact.setTelephone(phonefield);
        contact.setEmail(emailfield);

        return contact;
    }

    String serialize() throws JSONException {

        final StringWriter writer = new StringWriter();
        final JSONWriter contactWriter = new JSONWriter(writer);

        contactWriter.object().key(FIELD_JSON_NAME)
                .value(new JSONObject(new JSONTokener(getContactName().serialize())));
        contactWriter.key(FIELD_JSON_PHONE).value(new JSONObject(new JSONTokener(getTelephone()
                .serialize())));
        contactWriter.key(FIELD_JSON_EMAIL).value(new JSONObject(new JSONTokener(getEmail()
                .serialize())));
        contactWriter.endObject();

        return writer.toString();
    }

}
