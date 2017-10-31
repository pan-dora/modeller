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

package cool.pandora.modeller.domain;

import cool.pandora.modeller.Bagger;
import cool.pandora.modeller.Profile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JSonBagger.
 *
 * <p>Provides JSONBagger business object.
 * Leverages saving and loading cool.pandora.modeller.profiles in JSON format.
 *
 * @author gov.loc
 */
public class JSonBagger implements Bagger {
    protected static final Logger log = LoggerFactory.getLogger(JSonBagger.class);
    private static final String BAGGER_PROFILES_HOME_PROPERTY = "BAGGER_PROFILES_HOME";
    private static final String RESOURCE_DIR = "profiles";
    private static final String[] DEFAULT_PROFILES = new String[]{"fedora-iiif-profile.json"};
    private File profilesFolder;

    /**
     * JSonBagger.
     */
    public JSonBagger() {
        String homeDir = System.getProperty("user.home");
        if (System.getProperties().containsKey(BAGGER_PROFILES_HOME_PROPERTY)) {
            homeDir = System.getProperty(BAGGER_PROFILES_HOME_PROPERTY);
        }

        final String profilesPath = homeDir + File.separator + "cool.pandora.modeller";
        log.info("Using cool.pandora.modeller.profiles from {}", profilesPath);

        profilesFolder = new File(profilesPath);
        copyDefautprofilesToUserFolder(profilesFolder);
    }

    /**
     * closeStream.
     *
     * @param stream InputStream
     */
    private static void closeStream(final InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * loadProfile.
     *
     * @param reader       Reader
     * @param jsonFileName String
     * @return profile
     * @throws JSONException RuntimeException
     */
    private static Profile loadProfile(final Reader reader, final String jsonFileName)
            throws JSONException {
        final JSONTokener tokenizer = new JSONTokener(reader);
        final JSONObject jsonObject = new JSONObject(tokenizer);

        return Profile.createProfile(jsonObject, getprofileName(jsonFileName));
    }

    @Override
    public void loadProfile(final String profileName) {
    }

    /**
     * getprofileName.
     *
     * <p>Returns Profile Name from a JSON File Name.
     *
     * @param jsonFileName A JSON file name
     */
    private static String getprofileName(final String jsonFileName) {
        return jsonFileName.substring(0, jsonFileName.indexOf("-profile.json"));
    }

    /**
     * getJsonFileName.
     *
     * @param name String
     * @return name
     */
    private static String getJsonFileName(final String name) {
        return name + "-profile.json";
    }

    /**
     * copyDefautprofilesToUserFolder.
     *
     * @param folder File
     */
    private void copyDefautprofilesToUserFolder(final File folder) {
        if (!folder.exists()) {
            final boolean madeDirs = folder.mkdirs();
            log.debug("Made directories {} ? {}", folder, madeDirs);
        }

        for (final String profile : DEFAULT_PROFILES) {
            InputStream inputStream = null;

            try {
                final String resource = RESOURCE_DIR + "/" + profile;
                log.debug("Getting profile {} from jar", resource);
                inputStream = this.getClass().getClassLoader().getResourceAsStream(resource);
                if (inputStream == null) {
                    log.error("Tried to read {} from jar file but failed!", resource);
                }

                log.debug("Checking if {} exists", profile);
                final File target = new File(folder, profile);
                if (!target.exists()) {
                    log.debug("Profile {} does not already exist on the filesystem. Copying it "
                            + "from jar", profile);
                    Files.copy(inputStream, target.toPath());
                }
            } catch (Exception e) {
                log.error("Failed to copy profile {}", profile, e);
                break;
            } finally {
                closeStream(inputStream);
            }

        }

    }

    @Override
    public List<Profile> loadProfiles() {
        final File[] profilesFiles = profilesFolder.listFiles();
        final List<Profile> profilesToReturn = new ArrayList<>();
        if (profilesFiles != null) {
            for (final File file : profilesFiles) {
                try {
                    final InputStreamReader reader =
                            new InputStreamReader(new FileInputStream(file), "UTF-8");
                    final Profile profile = loadProfile(reader, file.getName());
                    profilesToReturn.add(profile);
                } catch (final FileNotFoundException e) {
                    log.error("Could not find profile file[{}]!", file, e);
                } catch (JSONException e) {
                    log.error("Error parsing json profile[{}]!", file, e);
                } catch (UnsupportedEncodingException e) {
                    log.error("Expected UTF-8 encoded file for {}", file, e);
                }
            }
        }

        // <no profile>
        final Profile profile = new Profile();
        profile.setName(Profile.NO_PROFILE_NAME);
        profile.setIsDefault();
        profilesToReturn.add(profile);
        return profilesToReturn;
    }

    @Override
    public void saveProfile(final Profile profile) {
        if (profile.getName().equals("<no profile>")) {
            return;
        }

        try {
            final String fileName = getJsonFileName(profile.getName());
            final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(
                    profilesFolder.getAbsolutePath() + File.separator + fileName),
                    Charset.forName("UTF-8"));
            final StringWriter stringWriter = new StringWriter();
            final JSONWriter jsonWriter = new JSONWriter(stringWriter);
            profile.serialize(jsonWriter);
            final JSONObject jsonObject = new JSONObject(new JSONTokener(stringWriter.toString()));
            writer.write(jsonObject.toString(4));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error("Failed to write profile {}", profile.getName(), e);
        } catch (JSONException e) {
            log.error("Failed to write JSON", e);
        }
    }

    @Override
    public void removeProfile(final Profile profile) {
        final String homeDir = System.getProperty("user.home");
        final String profilesPath = homeDir + File.separator + "cool.pandora.modeller";
        profilesFolder = new File(profilesPath);
        final String profileFielName = getJsonFileName(profile.getName());
        final File file = new File(profilesFolder, profileFielName);
        if (file.exists()) {
            final boolean wasDeleted = file.delete();
            log.debug("File {} was deleted ? {}", file, wasDeleted);
        }
    }

}