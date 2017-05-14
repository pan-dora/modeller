package cool.pandora.modeller;

import java.util.List;

/**
 * Bagger
 *
 * @author gov.loc
 */
public interface Bagger {
    /**
     * @return profiles
     */
    List<Profile> loadProfiles();

    /**
     * @param profileName String
     */
    void loadProfile(String profileName);

    /**
     * @param profile Profile
     */
    void saveProfile(Profile profile);

    /**
     * @param profile Profile
     */
    void removeProfile(Profile profile);

}
