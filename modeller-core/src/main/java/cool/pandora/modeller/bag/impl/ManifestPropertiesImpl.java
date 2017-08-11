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

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.Bag.BagConstants;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.BagHelper;
import gov.loc.repository.bagit.BagInfoTxt;
import gov.loc.repository.bagit.utilities.namevalue.NameValueReader.NameValue;
import gov.loc.repository.bagit.utilities.namevalue.impl.AbstractNameValueMapListBagFile;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * ManifestPropertiesImpl
 *
 * @author loc.gov
 */
public class ManifestPropertiesImpl extends AbstractNameValueMapListBagFile implements BagInfoTxt {

    public static final String FIELD_ATTRIBUTION = "Attribution";
    public static final String FIELD_LICENSE = "License";
    public static final String FIELD_LABEL = "Label";
    static final String FIELD_DESCRIPTION = "Description";
    public static final String FIELD_AUTHOR = "Author";
    public static final String FIELD_PUBLISHED = "Published";
    public static final String FIELD_INSTITUTION_LOGO_URI = "Logo URI";
    public static final String FIELD_RENDERING = "Rendering URI";
    static final String FIELD_RENDERING_LABEL = "Rendering Label";
    static final String FIELD_RENDERING_FORMAT = "Rendering MIME Type";
    private static final String FIELD_SOURCE_ORGANIZATION = "Source-Organization";
    private static final String FIELD_ORGANIZATION_ADDRESS = "Organization-Address";
    private static final String FIELD_CONTACT_NAME = "Contact-Name";
    private static final String FIELD_CONTACT_PHONE = "Contact-Phone";
    private static final String FIELD_CONTACT_EMAIL = "Contact-Email";
    private static final String FIELD_EXTERNAL_DESCRIPTION = "External-Description";
    private static final String FIELD_BAGGING_DATE = "Bagging-Date";
    private static final String FIELD_EXTERNAL_IDENTIFIER = "External-Identifier";
    private static final String FIELD_BAG_SIZE = "Bag-Size";
    private static final String FIELD_PAYLOAD_OXUM = "Payload-Oxum";
    private static final String FIELD_BAG_GROUP_IDENTIFIER = "Bag-Group-Identifier";
    private static final String FIELD_BAG_COUNT = "Bag-Count";
    private static final String FIELD_INTERNAL_SENDER_IDENTIFIER = "Internal-Sender-Identifier";
    private static final String FIELD_INTERNAL_SENDER_DESCRIPTION = "Internal-Sender-Description";
    private static final String STREAM_COUNT_PART = "Stream Count";
    private static final String OCTET_COUNT_PART = "Octet Count";


    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * @param bagFile BagFile
     * @param bagConstants BagConstants
     */
    public ManifestPropertiesImpl(final BagFile bagFile, final BagConstants bagConstants) {
        super(bagConstants.getBagInfoTxt(), bagFile, bagConstants.getBagEncoding());
    }

    /**
     * @param bagConstants BagConstants
     */
    public ManifestPropertiesImpl(final BagConstants bagConstants) {
        super(bagConstants.getBagInfoTxt(), bagConstants.getBagEncoding());

    }

    @Override
    public String getBagCount() {
        return this.getCaseInsensitive(FIELD_BAG_COUNT);
    }

    @Override
    public Integer getTotalBagsInGroup() throws ParseException {
        final String bagCount = this.getBagCount();
        if (bagCount == null) {
            return null;
        }
        final int pos = bagCount.indexOf(" of ");
        if (pos == -1 || pos + 4 >= bagCount.length()) {
            throw new ParseException("Bag-Count not structured N of T", 0);
        }
        final String totalBags = bagCount.substring(pos + 4);
        if ("?".equals(totalBags)) {
            return UNKNOWN_TOTAL_BAGS_IN_GROUP;
        }
        try {
            return Integer.parseInt(totalBags);
        } catch (final NumberFormatException ex) {
            throw new ParseException("Total Bags in Group is not an integer: " + totalBags, 0);
        }
    }

    @Override
    public Integer getBagInGroup() throws ParseException {
        final String bagCount = this.getBagCount();
        if (bagCount == null) {
            return null;
        }
        final int pos = bagCount.indexOf(" of ");
        if (pos == -1 || pos - 1 < 0) {
            throw new ParseException("Bag-Count not structured N of T", 0);
        }

        final String bagInGroup = bagCount.substring(0, pos);
        try {
            return Integer.parseInt(bagInGroup);
        } catch (NumberFormatException ex) {
            throw new ParseException("Bag in Group is not an integer: " + bagInGroup, 0);
        }
    }

    /**
     * @return FIELD_ATTRIBUTION
     */
    public String getAttribution() {
        return this.getCaseInsensitive(FIELD_ATTRIBUTION);
    }

    @Override
    public String getBagGroupIdentifier() {
        return this.getCaseInsensitive(FIELD_BAG_GROUP_IDENTIFIER);
    }

    @Override
    public String getBagSize() {
        return this.getCaseInsensitive(FIELD_BAG_SIZE);
    }

    @Override
    public void generateBagSize(final Bag bag) {
        this.setBagSize(BagHelper.generateBagSize(bag));
    }

    @Override
    public String getBaggingDate() {
        return this.getCaseInsensitive(FIELD_BAGGING_DATE);
    }

    @Override
    public Date getBaggingDateObj() throws ParseException {
        final String baggingDate = this.getBaggingDate();
        if (baggingDate == null) {
            return null;
        }
        return this.dateFormat.parse(baggingDate);
    }

    @Override
    public String getContactEmail() {
        return this.getCaseInsensitive(FIELD_CONTACT_EMAIL);
    }

    @Override
    public String getContactName() {
        return this.getCaseInsensitive(FIELD_CONTACT_NAME);
    }

    @Override
    public String getContactPhone() {
        return this.getCaseInsensitive(FIELD_CONTACT_PHONE);
    }

    @Override
    public String getExternalDescription() {
        return this.getCaseInsensitive(FIELD_EXTERNAL_DESCRIPTION);
    }

    @Override
    public String getExternalIdentifier() {
        return this.getCaseInsensitive(FIELD_EXTERNAL_IDENTIFIER);
    }

    @Override
    public String getInternalSenderDescription() {
        return this.getCaseInsensitive(FIELD_INTERNAL_SENDER_DESCRIPTION);
    }

    @Override
    public String getInternalSenderIdentifier() {
        return this.getCaseInsensitive(FIELD_INTERNAL_SENDER_IDENTIFIER);
    }

    @Override
    public String getOrganizationAddress() {
        return this.getCaseInsensitive(FIELD_ORGANIZATION_ADDRESS);
    }

    @Override
    public String getPayloadOxum() {
        return this.getCaseInsensitive(FIELD_PAYLOAD_OXUM);
    }

    @Override
    public Long getOctetCount() throws ParseException {
        return this.getOxumPart(OCTET_COUNT_PART);
    }

    @Override
    public Long getStreamCount() throws ParseException {
        return this.getOxumPart(STREAM_COUNT_PART);
    }

    /**
     * @param part String
     * @return OxumPar
     * @throws ParseException Exception
     */
    private Long getOxumPart(final String part) throws ParseException {
        final String oxum = this.getPayloadOxum();
        if (oxum == null) {
            return null;
        }
        final String[] split = oxum.split("\\.");
        if (split.length != 2) {
            throw new ParseException("Payload-Oxum is not OctetCount.StreamCount", 0);
        }

        int pos = 0;
        if (STREAM_COUNT_PART.equals(part)) {
            pos = 1;
        }
        try {
            return Long.parseLong(split[pos]);
        } catch (final NumberFormatException ex) {
            throw new ParseException(part + " is not an integer: " + split[pos], 0);
        }

    }

    @Override
    public String getSourceOrganization() {
        return this.getCaseInsensitive(FIELD_SOURCE_ORGANIZATION);
    }

    @Override
    public void setBagCount(final String bagCount) {
        this.put(FIELD_BAG_COUNT, bagCount);
    }

    @Override
    public void setBagCount(final int bagInGroup, final int totalBagsInGroup) {
        String totalBags = Integer.toString(totalBagsInGroup);
        if (totalBagsInGroup == UNKNOWN_TOTAL_BAGS_IN_GROUP) {
            totalBags = UNKNOWN_TOTAL_BAGS_IN_GROUP_MARKER;
        }
        this.setBagCount(MessageFormat.format("{0} of {1}", Integer.toString(bagInGroup),
                totalBags));

    }

    @Override
    public void setBagGroupIdentifier(final String bagGroupIdentifier) {
        this.put(FIELD_BAG_GROUP_IDENTIFIER, bagGroupIdentifier);

    }

    @Override
    public void setBagSize(final String bagSize) {
        this.put(FIELD_BAG_SIZE, bagSize);

    }

    @Override
    public void setBaggingDate(final String baggingDate) {
        this.put(FIELD_BAGGING_DATE, baggingDate);
    }

    @Override
    public void setBaggingDate(final Date date) {
        String dateString = null;
        if (date != null) {
            dateString = this.dateFormat.format(date);
        }
        this.setBaggingDate(dateString);
    }

    @Override
    public void setBaggingDate(final int year, final int month, final int day) {
        try {
            this.setBaggingDate(
                    this.dateFormat.parse(MessageFormat.format("{0}-{1}-{2}", Integer.toString
                            (year), month, day)));

        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setContactEmail(final String contactEmail) {
        this.put(FIELD_CONTACT_EMAIL, contactEmail);

    }

    @Override
    public void setContactName(final String contactName) {
        this.put(FIELD_CONTACT_NAME, contactName);

    }

    @Override
    public void setContactPhone(final String contactPhone) {
        this.put(FIELD_CONTACT_PHONE, contactPhone);

    }

    @Override
    public void setExternalDescription(final String externalDescription) {
        this.put(FIELD_EXTERNAL_DESCRIPTION, externalDescription);

    }

    @Override
    public void setExternalIdentifier(final String externalIdentifier) {
        this.put(FIELD_EXTERNAL_IDENTIFIER, externalIdentifier);

    }

    @Override
    public void setInternalSenderDescription(final String internalSenderDescription) {
        this.put(FIELD_INTERNAL_SENDER_DESCRIPTION, internalSenderDescription);

    }

    @Override
    public void setInternalSenderIdentifier(final String internalSenderIdentifier) {
        this.put(FIELD_INTERNAL_SENDER_IDENTIFIER, internalSenderIdentifier);

    }

    @Override
    public void setOrganizationAddress(final String organizationAddress) {
        this.put(FIELD_ORGANIZATION_ADDRESS, organizationAddress);
    }

    @Override
    public void setPayloadOxum(final String payloadOxsum) {
        this.put(FIELD_PAYLOAD_OXUM, payloadOxsum);
    }

    @Override
    public void setPayloadOxum(final long octetCount, final long streamCount) {
        this.setPayloadOxum(Long.toString(octetCount) + "." + Long.toString(streamCount));

    }

    @Override
    public void generatePayloadOxum(final Bag bag) {
        this.setPayloadOxum(BagHelper.generatePayloadOctetCount(bag), bag.getPayload().size());
    }

    @Override
    public void setSourceOrganization(final String sourceOrganization) {
        this.put(FIELD_SOURCE_ORGANIZATION, sourceOrganization);
    }

    @Override
    public String getType() {
        return BagInfoTxt.TYPE;
    }

    /**
     * @param key String
     * @return boolean
     */
    public boolean containsKeyCaseInsensitive(final String key) {
        return this.getCaseInsensitive(key) != null;
    }

    /**
     * @param key String
     * @return actualKeys
     */
    private Collection<String> getActualKeys(final String key) {
        final List<String> actualKeys = new ArrayList<>();
        for (final String name : this.keySet()) {
            if (name.equalsIgnoreCase(key)) {
                actualKeys.add(name);
            }
        }
        return actualKeys;
    }

    /**
     * @param key String
     * @return key
     */
    public String getCaseInsensitive(final String key) {
        for (final Entry<String, String> entry : this.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return this.get(entry.getKey());
            }
        }
        return null;
    }

    @Override
    public List<String> getListCaseInsensitive(final String key) {
        final List<String> values = new ArrayList<>();
        for (final NameValue nameValue : this.nameValueList) {
            if (nameValue.getName().equalsIgnoreCase(key)) {
                values.add(nameValue.getValue());
            }
        }
        return values;

    }

    @Override
    public List<String> getStandardFields() {
        final List<String> standardFields = new ArrayList<>();
        final Field[] fields = this.getClass().getFields();
        try {
            for (final Field field : fields) {
                if (field.getName().startsWith("FIELD_") && this.containsKeyCaseInsensitive(
                        (String) field.get(this))) {
                    standardFields.addAll(this.getActualKeys((String) field.get(this)));
                }
            }
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
        return standardFields;
    }

    @Override
    public List<String> getNonstandardFields() {
        final List<String> standardFields = this.getStandardFields();
        final List<String> nonstandardFields = new ArrayList<>();
        for (final String key : this.keySet()) {
            if (!standardFields.contains(key)) {
                nonstandardFields.add(key);
            }
        }
        return nonstandardFields;
    }

    @Override
    public void addBagGroupIdentifier(final String bagGroupIdentifier) {
        this.putList(FIELD_BAG_GROUP_IDENTIFIER, bagGroupIdentifier);

    }

    @Override
    public void addContactEmail(final String contactEmail) {
        this.putList(FIELD_CONTACT_EMAIL, contactEmail);
    }

    @Override
    public void addContactName(final String contactName) {
        this.putList(FIELD_CONTACT_NAME, contactName);
    }

    @Override
    public void addContactPhone(final String contactPhone) {
        this.putList(FIELD_CONTACT_PHONE, contactPhone);
    }

    @Override
    public void addExternalDescription(final String externalDescription) {
        this.putList(FIELD_EXTERNAL_DESCRIPTION, externalDescription);
    }

    @Override
    public void addExternalIdentifier(final String externalIdentifier) {
        this.putList(FIELD_EXTERNAL_IDENTIFIER, externalIdentifier);
    }

    @Override
    public void addInternalSenderDescription(final String internalSenderDescription) {
        this.putList(FIELD_INTERNAL_SENDER_DESCRIPTION, internalSenderDescription);
    }

    @Override
    public void addInternalSenderIdentifier(final String internalSenderIdentifier) {
        this.putList(FIELD_INTERNAL_SENDER_IDENTIFIER, internalSenderIdentifier);
    }

    @Override
    public void addOrganizationAddress(final String organizationAddress) {
        this.putList(FIELD_ORGANIZATION_ADDRESS, organizationAddress);
    }

    @Override
    public void addSourceOrganization(final String sourceOrganization) {
        this.putList(FIELD_SOURCE_ORGANIZATION, sourceOrganization);
    }

    @Override
    public List<String> getBagGroupIdentifierList() {
        return this.getListCaseInsensitive(FIELD_BAG_GROUP_IDENTIFIER);
    }

    @Override
    public List<String> getContactEmailList() {
        return this.getListCaseInsensitive(FIELD_CONTACT_EMAIL);
    }

    @Override
    public List<String> getContactNameList() {
        return this.getListCaseInsensitive(FIELD_CONTACT_NAME);
    }

    @Override
    public List<String> getContactPhoneList() {
        return this.getListCaseInsensitive(FIELD_CONTACT_PHONE);
    }

    @Override
    public List<String> getExternalDescriptionList() {
        return this.getListCaseInsensitive(FIELD_EXTERNAL_DESCRIPTION);
    }

    @Override
    public List<String> getExternalIdentifierList() {
        return this.getListCaseInsensitive(FIELD_EXTERNAL_IDENTIFIER);
    }

    @Override
    public List<String> getInternalSenderDescriptionList() {
        return this.getListCaseInsensitive(FIELD_INTERNAL_SENDER_DESCRIPTION);
    }

    @Override
    public List<String> getInternalSenderIdentifierList() {
        return this.getListCaseInsensitive(FIELD_INTERNAL_SENDER_IDENTIFIER);
    }

    @Override
    public List<String> getOrganizationAddressList() {
        return this.getListCaseInsensitive(FIELD_ORGANIZATION_ADDRESS);
    }

    @Override
    public List<String> getSourceOrganizationList() {
        return this.getListCaseInsensitive(FIELD_SOURCE_ORGANIZATION);
    }

}
