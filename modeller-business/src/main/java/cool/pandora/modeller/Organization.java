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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;


/**
 * Organization.
 *
 * @author gov.loc
 */
public class Organization {
    private static final String FIELD_SOURCE_ORGANIZATION = "Source-Organization";
    private static final String FIELD_ORGANIZATION_ADDRESS = "Organization-Address";
    private ProfileField name;
    private ProfileField address;

    /**
     * Organization.
     */
    public Organization() {
        name = new ProfileField();
        name.setFieldName(FIELD_SOURCE_ORGANIZATION);
        address = new ProfileField();
        address.setFieldName(FIELD_ORGANIZATION_ADDRESS);
    }

    /**
     * createOrganization.
     *
     * @param organizationJson JSONObject
     * @return organization
     * @throws JSONException exception
     */
    static Organization createOrganization(final JSONObject organizationJson) throws JSONException {
        final Organization organization = new Organization();
        ProfileField name = null;
        ProfileField address = null;
        if (organizationJson != null) {
            if (organizationJson.has(FIELD_SOURCE_ORGANIZATION)) {
                final JSONObject jsonObjectOrgName =
                        (JSONObject) organizationJson.get(FIELD_SOURCE_ORGANIZATION);
                if (jsonObjectOrgName != null) {
                    name = ProfileField
                            .createProfileField(jsonObjectOrgName, FIELD_SOURCE_ORGANIZATION);
                }
            }

            if (organizationJson.has(FIELD_ORGANIZATION_ADDRESS)) {
                final JSONObject jsonObjectOrgAddr =
                        (JSONObject) organizationJson.get(FIELD_ORGANIZATION_ADDRESS);
                if (jsonObjectOrgAddr != null) {
                    address = ProfileField
                            .createProfileField(jsonObjectOrgAddr, FIELD_ORGANIZATION_ADDRESS);
                }
            }
        }

        if (name == null) {
            name = new ProfileField();
            name.setFieldName(FIELD_SOURCE_ORGANIZATION);
        }
        if (address == null) {
            address = new ProfileField();
            address.setFieldName(FIELD_ORGANIZATION_ADDRESS);
        }
        organization.setName(name);
        organization.setAddress(address);
        return organization;
    }

    /**
     * getName.
     *
     * @return name
     */
    public ProfileField getName() {
        return this.name;
    }

    /**
     * setName.
     *
     * @param n name
     */
    public void setName(final ProfileField n) {
        this.name = n;
    }

    /**
     * getAddress.
     *
     * @return address
     */
    public ProfileField getAddress() {
        return this.address;
    }

    /**
     * setAddress.
     *
     * @param a address
     */
    private void setAddress(final ProfileField a) {
        this.address = a;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * toString.
     *
     * @param inline boolean
     * @return String
     */
    public String toString(final boolean inline) {
        final String delim;
        if (inline) {
            delim = ", ";
        } else {
            delim = "\n";
        }

        return String.valueOf(this.getName()) + delim + this.getAddress();
    }

    String serialize() throws JSONException {
        final StringWriter writer = new StringWriter();
        final JSONWriter orgStringer = new JSONWriter(writer);

        orgStringer.object().key(FIELD_ORGANIZATION_ADDRESS)
                .value(new JSONObject(new JSONTokener(this.getAddress().serialize())));
        orgStringer.key(FIELD_SOURCE_ORGANIZATION)
                .value(new JSONObject(new JSONTokener(getName().serialize())));
        orgStringer.endObject();
        return writer.toString();
    }
}
