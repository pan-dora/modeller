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
