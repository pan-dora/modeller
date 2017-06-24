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
package cool.pandora.modeller.model;

/**
 * BagStatus
 *
 * @author gov.loc
 */
public class BagStatus {

    private static final BagStatus instance = new BagStatus();

    private StatusModel validationStatus = new StatusModel();
    private StatusModel completenessStatus = new StatusModel();
    private StatusModel profileComplianceStatus = new StatusModel();

    /**
     * @return validationStatus
     */
    public StatusModel getValidationStatus() {
        return validationStatus;
    }

    /**
     * @param validationStatus StatusModel
     */
    public void setValidationStatus(final StatusModel validationStatus) {
        this.validationStatus = validationStatus;
    }

    /**
     * @return completenessStatus
     */
    public StatusModel getCompletenessStatus() {
        return completenessStatus;
    }

    /**
     * @param completenessStatus StatusModel
     */
    public void setCompletenessStatus(final StatusModel completenessStatus) {
        this.completenessStatus = completenessStatus;
    }

    /**
     * @return profileComplianceStatus
     */
    public StatusModel getProfileComplianceStatus() {
        return profileComplianceStatus;
    }

    /**
     * @param profileComplianceStatus StatusModel
     */
    public void setProfileComplianceStatus(final StatusModel profileComplianceStatus) {
        this.profileComplianceStatus = profileComplianceStatus;
    }

    /**
     * @return instance
     */
    public static BagStatus getInstance() {
        return instance;
    }

}
