package cool.pandora.modeller.profile;

import cool.pandora.modeller.Bagger;
import cool.pandora.modeller.Profile;
import cool.pandora.modeller.ProfileField;

import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map.Entry;

/**
 * BaggerProfileStore
 *
 * @author gov.loc
 */
public class BaggerProfileStore {

    private static BaggerProfileStore instance;

    private HashMap<String, Profile> userProfiles = new HashMap<>();
    private LinkedHashMap<String, List<ProfileField>> profileFieldsMap = new LinkedHashMap<>();

    /**
     * @param bagger Bagger
     */
    public BaggerProfileStore(final Bagger bagger) {
        initializeProfile(bagger);
        instance = this;
    }

    /**
     * @param name String
     * @return profile
     */
    public Profile getProfile(final String name) {
        final Profile profile = this.userProfiles.get(name);
        if (profile == null) {
            throw new RuntimeException("Could not get profile [" + name + "]");
        }
        return profile;
    }

    /**
     * @param bagger Bagger
     */
    private void initializeProfile(final Bagger bagger) {
        final Collection<Profile> profiles = bagger.loadProfiles();
        userProfiles = new HashMap<>();
        profileFieldsMap = new LinkedHashMap<>();

        for (final Profile profile : profiles) {
            userProfiles.put(profile.getName(), profile);
            final LinkedHashMap<String, ProfileField> standardFields = profile.getStandardFields();
            final LinkedHashMap<String, ProfileField> customFields = profile.getCustomFields();
            final LinkedHashMap<String, ProfileField> mergedMap = new LinkedHashMap<>();
            mergedMap.putAll(standardFields);
            mergedMap.putAll(customFields);

            for (final ProfileField profileField : mergedMap.values()) {
                final List<ProfileField> list =
                        profileFieldsMap.computeIfAbsent(profile.getName(), k -> new ArrayList<>());
                list.add(profileField);
            }
        }
    }

    /**
     * @return profile value
     */
    public Profile getDefaultProfile() {
        for (final Entry<String, Profile> entry : userProfiles.entrySet()) {
            if (entry.getValue().getIsDefault()) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * @return instance
     */
    public static BaggerProfileStore getInstance() {
        return instance;
    }

    /**
     * @return profile names
     */
    public String[] getProfileNames() {
        return userProfiles.keySet().toArray(new String[0]);
    }

    /**
     * @param profileName String
     * @return profileFieldsMap
     */
    public List<ProfileField> getProfileFields(final String profileName) {
        return profileFieldsMap.get(profileName);
    }

}
