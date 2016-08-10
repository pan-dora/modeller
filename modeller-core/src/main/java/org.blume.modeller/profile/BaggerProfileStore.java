package org.blume.modeller.profile;

import org.blume.modeller.Bagger;
import org.blume.modeller.Profile;
import org.blume.modeller.ProfileField;

import java.util.*;
import java.util.Map.Entry;

public class BaggerProfileStore {

  private static BaggerProfileStore instance;

  private HashMap<String, Profile> userProfiles = new HashMap<String, Profile>();
  private LinkedHashMap<String, List<ProfileField>> profileFieldsMap = new LinkedHashMap<String, List<ProfileField>>();

  public BaggerProfileStore(Bagger bagger) {
    initializeProfile(bagger);
    instance = this;
  }

  public Profile getProfile(String name) {
    Profile profile = this.userProfiles.get(name);
    if(profile == null){
      throw new RuntimeException("Could not get profile [" + name + "]");
    }
    return profile;
  }

  private void initializeProfile(Bagger bagger) {
    Collection<Profile> profiles = bagger.loadProfiles();
    userProfiles = new HashMap<String, Profile>();
    profileFieldsMap = new LinkedHashMap<String, List<ProfileField>>();

    for (Profile profile : profiles) {
      userProfiles.put(profile.getName(), profile);
      LinkedHashMap<String, ProfileField> standardFields = profile.getStandardFields();
      LinkedHashMap<String, ProfileField> customFields = profile.getCustomFields();
      LinkedHashMap<String, ProfileField> mergedMap = new LinkedHashMap<String, ProfileField>();
      mergedMap.putAll(standardFields);
      mergedMap.putAll(customFields);

      for (ProfileField profileField : mergedMap.values()) {
        List<ProfileField> list = profileFieldsMap.get(profile.getName());
        if (list == null) {
          list = new ArrayList<ProfileField>();
          profileFieldsMap.put(profile.getName(), list);
        }
        list.add(profileField);
      }
    }
  }

  public Profile getDefaultProfile() {
    for(Entry<String, Profile> entry: userProfiles.entrySet()){
      if(entry.getValue().getIsDefault()){
        return entry.getValue();
      }
    }
    return null;
  }

  public static BaggerProfileStore getInstance() {
    return instance;
  }

  public String[] getProfileNames() {
    return userProfiles.keySet().toArray(new String[0]);
  }

  public List<ProfileField> getProfileFields(String profileName) {
    return profileFieldsMap.get(profileName);
  }

}
