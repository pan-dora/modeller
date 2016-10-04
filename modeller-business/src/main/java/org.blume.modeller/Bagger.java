package org.blume.modeller;

import java.util.List;

public interface Bagger {

  List<Profile> loadProfiles();

  void loadProfile(String profileName);

  void saveProfile(Profile profile);

  void removeProfile(Profile profile);

}
