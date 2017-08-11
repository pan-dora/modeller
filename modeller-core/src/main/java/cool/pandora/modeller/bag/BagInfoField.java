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

import cool.pandora.modeller.ProfileField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * BagInfoField
 *
 * @author gov.loc
 */
public class BagInfoField implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int TEXTFIELD_COMPONENT = 1;
    public static final int TEXTAREA_COMPONENT = 2;
    public static final int LIST_COMPONENT = 3;
    public static final String TEXTFIELD_CODE = "TF";
    public static final String TEXTAREA_CODE = "TA";
    public static final int MAX_VALUE = 32;

    private String name = "";
    private String label = "";
    private String value = "";
    private List<String> elements = new ArrayList<>();
    private int componentType = TEXTFIELD_COMPONENT;
    private boolean isEnabled = true;
    private boolean isRequired = false;
    private boolean isRequiredvalue = false;
    private boolean isEditable = true;
    private boolean isProfile = false;

    /**
     *
     */
    public BagInfoField() {

    }

    /**
     * @param projectProfile ProfileField
     */
    public BagInfoField(final ProfileField projectProfile) {
        this.setLabel(projectProfile.getFieldName());
        this.setName(this.getLabel());
        this.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
        this.isEnabled(!projectProfile.isReadOnly());
        this.isEditable(!projectProfile.isReadOnly());
        this.isRequiredvalue(projectProfile.getIsValueRequired());
        this.isRequired(projectProfile.getIsRequired());
        this.setValue(projectProfile.getFieldValue());
        // field.setValue("");
        if (projectProfile.isReadOnly()) {
            this.isEnabled(false);
        }
        this.buildElements(projectProfile.getElements());
        if (projectProfile.getFieldType().equalsIgnoreCase(BagInfoField.TEXTFIELD_CODE)) {
            this.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
        } else if (projectProfile.getFieldType().equalsIgnoreCase(BagInfoField.TEXTAREA_CODE)) {
            this.setComponentType(BagInfoField.TEXTAREA_COMPONENT);
        } else if (!(projectProfile.getElements().isEmpty())) {
            this.setComponentType(BagInfoField.LIST_COMPONENT);
        }
    }

    /**
     * @param n String
     */
    public void setName(final String n) {
        this.name = n;
    }

    /**
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param l String
     */
    public void setLabel(final String l) {
        this.label = l;
    }

    /**
     * @return label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @param v String
     */
    public void setValue(final String v) {
        this.value = v;
    }

    /**
     * @return value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param e List
     */
    public void setElements(final List<String> e) {
        this.elements = e;
    }

    /**
     * @return elements
     */
    public List<String> getElements() {
        return this.elements;
    }

    /**
     * @param type int
     */
    public void setComponentType(final int type) {
        this.componentType = type;
    }

    /**
     * @return componentType
     */
    public int getComponentType() {
        return this.componentType;
    }

    /**
     * @param b boolean
     */
    public void isEditable(final boolean b) {
        this.isEditable = b;
    }

    /**
     * @return isEditable
     */
    private boolean isEditable() {
        return this.isEditable;
    }

    /**
     * @param b boolean
     */
    public void isEnabled(final boolean b) {
        this.isEnabled = b;
    }

    /**
     * @return isEnabled
     */
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * @param b boolean
     */
    public void isRequired(final boolean b) {
        this.isRequired = b;
    }

    /**
     * @return isRequired
     */
    public boolean isRequired() {
        return this.isRequired;
    }

    /**
     * @param b boolean
     */
    public void isRequiredvalue(final boolean b) {
        this.isRequiredvalue = b;
    }

    /**
     * @return isRequiredvalue
     */
    private boolean isRequiredvalue() {
        return this.isRequiredvalue;
    }

    /**
     * @param b boolean
     */
    public void isProfile(final boolean b) {
        this.isProfile = b;
    }

    /**
     * @return isProfile
     */
    public boolean isProfile() {
        return this.isProfile;
    }

    /**
     * @param elementList List
     */
    public void buildElements(final List<String> elementList) {

        this.elements = elementList;
    }

    @Override
    public String toString() {
        return "\n" + "Label: " + getLabel() + "\n" + "Name: " + getName() + "\n" + "Value: " +
                getValue() + "\n" +
                "Type: " + getComponentType() + "\n" + "Elements: " + getElements() + "\n" +
                "isRequired: " +
                isRequired() + "\n" + "isRequiredvalue: " + isRequiredvalue() + "\n" +
                "isEnabled: " + isEnabled() +
                "\n" + "isEditable: " + isEditable() + "\n";
    }

}