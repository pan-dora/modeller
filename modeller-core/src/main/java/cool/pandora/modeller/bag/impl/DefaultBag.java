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

import cool.pandora.modeller.Profile;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.BaggerFetch;
import cool.pandora.modeller.model.BagStatus;
import cool.pandora.modeller.model.Status;
import cool.pandora.modeller.profile.BaggerProfileStore;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagInfoTxt;
import gov.loc.repository.bagit.PreBag;
import gov.loc.repository.bagit.FetchTxt;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagItTxt;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.BagFactory.Version;
import gov.loc.repository.bagit.FetchTxt.FilenameSizeUrl;
import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.transformer.HolePuncher;
import gov.loc.repository.bagit.transformer.impl.DefaultCompleter;
import gov.loc.repository.bagit.transformer.impl.HolePuncherImpl;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.Verifier;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import gov.loc.repository.bagit.verify.impl.RequiredBagInfoTxtFieldsVerifier;
import gov.loc.repository.bagit.verify.impl.ValidVerifierImpl;
import gov.loc.repository.bagit.writer.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * DefaultBag
 *
 * @author loc.gov
 */
public class DefaultBag {
    protected static final Logger log = LoggerFactory.getLogger(DefaultBag.class);
    public static final long KB = 1024;
    public static final long MB = 1048576;
    public static final long GB = 1073741824;
    public static final long MAX_SIZE = 104857600; // 100 MB
    public static final short NO_MODE = 0;
    public static final short ZIP_MODE = 1;
    private static final short TAR_MODE = 2;
    private static final short TAR_GZ_MODE = 3;
    private static final short TAR_BZ2_MODE = 4;
    public static final String NO_LABEL = "none";
    public static final String ZIP_LABEL = "zip";
    private static final String TAR_LABEL = "tar";
    public static final String GZ_LABEL = "gz";
    private static final String TAR_GZ_LABEL = "tar.gz";
    private static final String TAR_BZ2_LABEL = "tar.bz2";

    // Bag option flags
    private boolean isHoley = false;
    private boolean isSerial = true;
    private boolean isAddKeepFilesToEmptyFolders = false;

    // bag building (saving) options
    private boolean isBuildTagManifest = true;
    private boolean isBuildPayloadManifest = true;
    private String tagManifestAlgorithm;
    private String payloadManifestAlgorithm;
    private short serialMode = NO_MODE;

    // Bag state flags
    private boolean isValidateOnSave = false;
    private boolean isSerialized = false;

    private boolean dirty = false;

    private File rootDir = null;
    private String name = "bag_";
    private long size;
    private long totalSize = 0;

    private Bag bilBag;
    private DefaultBagInfo bagInfo = null;
    private Verifier bagStrategy;
    private BaggerFetch fetch;
    private Profile profile;
    private String versionString = null;
    private File bagFile = null;

    private boolean hasText = true;
    private String sequenceID = "normal";
    private String hOCRResource;
    private String listServiceURI;

    /**
     *
     */
    public DefaultBag() {
        this(null, Version.V0_96.versionString);
    }

    /**
     * @param rootDir File
     * @param version String
     */
    public DefaultBag(final File rootDir, final String version) {
        this.versionString = version;
        init(rootDir);
    }

    /**
     * @param s String
     */
    private void display(final String s) {
        log.info(this.getClass().getName() + ": " + s);
    }

    /**
     * @param dir File
     */
    private void init(final File dir) {
        final boolean newBag = dir == null;
        resetStatus();
        this.rootDir = dir;

        display("DefaultBag.init file: " + dir + ", version: " + versionString);
        final BagFactory bagFactory = new BagFactory();
        if (!newBag) {
            bilBag = bagFactory.createBag(this.rootDir);
            versionString = bilBag.getVersion().versionString;
        } else if (versionString != null) {
            final Version version = Version.valueOfString(versionString);
            bilBag = bagFactory.createBag(version);
        } else {
            bilBag = bagFactory.createBag();
        }
        initializeBilBag();

        bagInfo = new DefaultBagInfo();

        final FetchTxt fetchTxt = bilBag.getFetchTxt();
        if (fetchTxt != null && !fetchTxt.isEmpty()) {
            final String url = getBaseUrl(fetchTxt);
            if (url != null && !url.isEmpty()) {
                isHoley(true);
                final BaggerFetch localFetch = this.getFetch();
                localFetch.setBaseURL(url);
                this.fetch = localFetch;
            } else {
                isHoley(false);
            }
        }

        this.payloadManifestAlgorithm = Manifest.Algorithm.MD5.bagItAlgorithm;
        this.tagManifestAlgorithm = Manifest.Algorithm.MD5.bagItAlgorithm;

        this.bagInfo.update(bilBag.getBagInfoTxt());

        // set profile
        final String lcProject = bilBag.getBagInfoTxt().get(DefaultBagInfo.FIELD_LC_PROJECT);
        if (lcProject != null && !lcProject.isEmpty()) {
            log.debug("Getting [{}] profile", lcProject);
            final Profile localProfile = BaggerProfileStore.getInstance().getProfile(lcProject);
            setProfile(localProfile, newBag);
        } else {
            clearProfile();
        }
    }

    /**
     *
     */
    private void initializeBilBag() {
        BagInfoTxt bagInfoTxt = bilBag.getBagInfoTxt();
        if (bagInfoTxt == null) {
            bagInfoTxt = bilBag.getBagPartFactory().createBagInfoTxt();
      /* */
            final Set<String> keys = bagInfoTxt.keySet();
            for (final String key : keys) {
                bagInfoTxt.remove(key);
            }
      /* */
            bilBag.putBagFile(bagInfoTxt);
        }

        BagItTxt bagIt = bilBag.getBagItTxt();
        if (bagIt == null) {
            bagIt = bilBag.getBagPartFactory().createBagItTxt();
            bilBag.putBagFile(bagIt);
        }
    }

    /**
     * @param data File
     */
    public void createPreBag(final File data) {
        final BagFactory bagFactory = new BagFactory();
        final PreBag preBag = bagFactory.createPreBag(data);
        bilBag = preBag.makeBagInPlace(BagFactory.LATEST, false);
    }

    /**
     * Makes BIL API call to create Bag in place and
     * adding .keep files in empty Pay load folders
     */
    public void createPreBagAddKeepFilesToEmptyFolders(final File data) {
        final BagFactory bagFactory = new BagFactory();
        final PreBag preBag = bagFactory.createPreBag(data);
        bilBag = preBag.makeBagInPlace(BagFactory.LATEST, false, true);
    }

    /**
     * @return bagFile
     */
    public File getBagFile() {
        return bagFile;
    }

    /**
     * @param fname File
     */
    private void setBagFile(final File fname) {
        this.bagFile = fname;
    }

    /**
     * @return dataDirectory
     */
    public String getDataDirectory() {
        return bilBag.getBagConstants().getDataDirectory();
    }

    /**
     *
     */
    private static void resetStatus() {
        isComplete(Status.UNKNOWN);
        isValid(Status.UNKNOWN);
        isValidMetadata(Status.UNKNOWN);
    }

    /**
     * @param v String
     */
    public void setVersion(final String v) {
        this.versionString = v;
    }

    /**
     * @return versionString
     */
    public String getVersion() {
        return this.versionString;
    }

    /**
     * @param name String
     */
    public void setName(final String name) {
        final String[] list = name.split("\\.");
        String split_name = null;
        if (list.length > 0) {
            split_name = list[0];
        }
        this.name = split_name;
    }

    /**
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param b boolean
     */
    public void hasText(final boolean b) {
        this.hasText = b;
    }

    /**
     * @return hasText
     */
    public boolean hasText() {
        return this.hasText;
    }

    /**
     * @param sequenceID String
     */
    public void setSequenceID(final String sequenceID) {
        this.sequenceID = sequenceID;
    }

    /**
     * @return sequenceID
     */
    public String getSequenceID() {
        return this.sequenceID;
    }

    /**
     * @param hOCRResource String
     */
    public void sethOCRResource(final String hOCRResource) {
        this.hOCRResource = hOCRResource;
    }

    /**
     * @return hOCRResource
     */
    public String gethOCRResource() {
        return this.hOCRResource;
    }

    /**
     * @param listServiceURI String
     */
    public void setListServiceBaseURI(final String listServiceURI) {
        this.listServiceURI = listServiceURI;
    }

    /**
     * @return listServiceURI
     */
    public String getListServiceBaseURI() {
        return this.listServiceURI;
    }

    /**
     * @param size long
     */
    public void setSize(final long size) {
        this.size = size;
    }

    /**
     * @return size
     */
    public long getSize() {
        return this.size;
    }

    /**
     * This directory contains either the bag directory or serialized bag file
     *
     * @param rootDir File
     */
    public void setRootDir(final File rootDir) {
        this.rootDir = rootDir;
    }

    /**
     * @return rootDir
     */
    public File getRootDir() {
        return this.rootDir;
    }

    /**
     * @param b boolean
     */
    public void isHoley(final boolean b) {
        this.isHoley = b;
    }

    /**
     * @return isHoley
     */
    public boolean isHoley() {
        return this.isHoley;
    }

    /**
     * @param b boolean
     */
    public void isSerial(final boolean b) {
        this.isSerial = b;
    }

    /**
     * @return isSerial
     */
    public boolean isSerial() {
        return this.isSerial;
    }

    /**
     * @param m short
     */
    public void setSerialMode(final short m) {
        this.serialMode = m;
    }

    /**
     * @return serialMode
     */
    public short getSerialMode() {
        return this.serialMode;
    }

    /**
     * @return isNoProfile
     */
    private boolean isNoProject() {
        return profile.isNoProfile();
    }

    /**
     * @param b boolean
     */
    public void isBuildTagManifest(final boolean b) {
        this.isBuildTagManifest = b;
    }

    /**
     * @return isBuildTagManifest
     */
    public boolean isBuildTagManifest() {
        return this.isBuildTagManifest;
    }

    /**
     * @param b boolean
     */
    public void isBuildPayloadManifest(final boolean b) {
        this.isBuildPayloadManifest = b;
    }

    /**
     * @return isBuildPayloadManifest
     */
    public boolean isBuildPayloadManifest() {
        return this.isBuildPayloadManifest;
    }

    /**
     * @param s String
     */
    public void setTagManifestAlgorithm(final String s) {
        this.tagManifestAlgorithm = s;
    }

    /**
     * @return tagManifestAlgorithm
     */
    public String getTagManifestAlgorithm() {
        return this.tagManifestAlgorithm;
    }

    /**
     * @param s String
     */
    public void setPayloadManifestAlgorithm(final String s) {
        this.payloadManifestAlgorithm = s;
    }

    /**
     * @return payloadManifestAlgorithm
     */
    public String getPayloadManifestAlgorithm() {
        return this.payloadManifestAlgorithm;
    }


    /**
     * Setter Method
     * for the passed value associated with the ".keep Files in Empty Folder(s):"
     * Check Box
     *
     * @param b boolean
     */
    public void isAddKeepFilesToEmptyFolders(final boolean b) {
        this.isAddKeepFilesToEmptyFolders = b;
    }

    /**
     * Getter Method
     * for the value return value associated with the
     * "Add .keep Files To Empty Folder" Check Box
     */
    public boolean isAddKeepFilesToEmptyFolders() {
        return this.isAddKeepFilesToEmptyFolders;
    }

    /**
     * @param b boolean
     */
    public void isValidateOnSave(final boolean b) {
        this.isValidateOnSave = b;
    }

    /**
     * @return isValidateOnSave
     */
    public boolean isValidateOnSave() {
        return this.isValidateOnSave;
    }

    /**
     * @param status Status
     */
    private static void isComplete(final Status status) {
        BagStatus.getInstance().getCompletenessStatus().setStatus(status);
    }

    /**
     * @param status Status
     */
    private static void isValid(final Status status) {
        BagStatus.getInstance().getValidationStatus().setStatus(status);
    }

    /**
     * @param status Status
     */
    private static void isValidMetadata(final Status status) {
        BagStatus.getInstance().getProfileComplianceStatus().setStatus(status);
    }

    /**
     * @param b boolean
     */
    private void isSerialized(final boolean b) {
        this.isSerialized = b;
    }

    /**
     * @return isSerialized
     */
    public boolean isSerialized() {
        return this.isSerialized;
    }

    /**
     * @param map Map
     */
    public void updateBagInfo(final Map<String, String> map) {
        changeToDirty();
        isValidMetadata(Status.UNKNOWN);
        bagInfo.update(map);
    }

    /**
     * @return bagInfo
     */
    public DefaultBagInfo getInfo() {
        return this.bagInfo;
    }

    /**
     * @return bicontent
     */
    public String getBagInfoContent() {
        String bicontent = "";
        if (this.bagInfo != null) {
            bicontent = this.bagInfo.toString();
        }
        return bicontent;
    }

    /**
     * @param fetchTxt FetchTxt
     * @return baseUrl
     */
    private static String getBaseUrl(final FetchTxt fetchTxt) {
        final String httpToken = "http:";
        final String delimToken = "bagit";
        String baseUrl = "";
        try {
            if (fetchTxt != null && !fetchTxt.isEmpty()) {
                final FilenameSizeUrl fsu = fetchTxt.get(0);
                if (fsu != null) {
                    final String url = fsu.getUrl();
                    baseUrl = url;
                    final String[] list = url.split(delimToken);
                    for (final String s : list) {
                        if (s.trim().startsWith(httpToken)) {
                            baseUrl = s;
                        }
                    }
                }
            }
        } catch (final Exception e) {
            log.error("Failed to get base URL", e);
        }
        return baseUrl;
    }

    /**
     * @param fetch BaggerFetch
     */
    public void setFetch(final BaggerFetch fetch) {
        this.fetch = fetch;
    }

    /**
     * @return fetch
     */
    public BaggerFetch getFetch() {
        if (this.fetch == null) {
            this.fetch = new BaggerFetch();
        }
        return this.fetch;
    }

    /**
     * @return list
     */
    public List<String> getFetchPayload() {
        final List<String> list = new ArrayList<>();

        final FetchTxt fetchTxt = this.bilBag.getFetchTxt();
        if (fetchTxt == null) {
            return list;
        }

        for (final FilenameSizeUrl localFetch : fetchTxt) {
            final String s = localFetch.getFilename();
            display("DefaultBag.getFetchPayload: " + localFetch.toString());
            list.add(s);
        }
        return list;
    }

    /**
     * @return dataContent
     */
    public String getDataContent() {
        totalSize = 0;
        final StringBuilder dcontent = new StringBuilder();
        dcontent.append(this.getDataDirectory()).append("/");
        dcontent.append('\n');
        final Collection<BagFile> files = this.bilBag.getPayload();
        if (files != null) {
            for (final BagFile file : files) {
                try {
                    if (file != null) {
                        totalSize += file.getSize();
                        dcontent.append(file.getFilepath());
                        dcontent.append('\n');
                    }
                } catch (final Exception e) {
                    log.error("Failed to get data content", e);
                }
            }
        }
        this.setSize(totalSize);
        return dcontent.toString();
    }

    /**
     * @return totalSize
     */
    public long getDataSize() {
        return this.totalSize;
    }

    /**
     * @return size
     */
    public int getDataNumber() {
        return this.bilBag.getPayload().size();
    }

    /**
     *
     */
    private void clearProfile() {
        final Profile noProfile = new Profile();
        noProfile.setName(Profile.NO_PROFILE_NAME);
        noProfile.setIsDefault();
        setProfile(noProfile, false);
    }

    /**
     * @param profile Profile
     * @param newBag boolean
     */
    public void setProfile(final Profile profile, final boolean newBag) {
        this.profile = profile;
        bagInfo.setProfile(profile, newBag);
    }

    /**
     * @return profile
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     * @return pathList
     */
    public List<String> getPayloadPaths() {
        final ArrayList<String> pathList = new ArrayList<String>();
        final Collection<BagFile> payload = this.bilBag.getPayload();
        if (payload != null) {
            for (final BagFile bf : payload) {
                pathList.add(bf.getFilepath());
            }
        }
        return pathList;
    }

    /**
     * @param f File
     */
    public void addTagFile(final File f) {
        changeToDirty();
        isComplete(Status.UNKNOWN);

        String message = null;
        if (f != null) {
            try {
                bilBag.addFileAsTag(f);
            } catch (Exception e) {
                message = "Error adding file: " + f + " due to: " + e.getMessage();
            }
        }
    }

    /**
     * @param bw Writer
     * @return messages
     */
    public String write(final Writer bw) {
        prepareBilBagInfoIfDirty();

        generateManifestFiles();

        if (this.isHoley && this.getFetch().getBaseURL() != null) {
            final BagInfoTxt bagInfoTxt = bilBag.getBagInfoTxt();

            final List<Manifest> manifests = bilBag.getPayloadManifests();
            final List<Manifest> tags = bilBag.getTagManifests();

            final HolePuncher puncher = new HolePuncherImpl(new BagFactory());
            bilBag = puncher.makeHoley(bilBag, this.getFetch().getBaseURL(), true, true, false);
            // makeHoley deletes baginfo so put back
            bilBag.putBagFile(bagInfoTxt);
            if (manifests != null) {
                for (final Manifest manifest : manifests) {
                    bilBag.putBagFile(manifest);
                }
            }
            if (tags != null) {
                for (final Manifest tag : tags) {
                    bilBag.putBagFile(tag);
                }
            }
        }

        final String messages = writeBag(bw);

        if (bw.isCancelled()) {
            return "Save cancelled.";
        }
        return messages;
    }

    /**
     * @param completeVerifier CompleteVerifierImpl
     * @return messages
     */
    public String completeBag(final CompleteVerifierImpl completeVerifier) {
        prepareBilBagInfoIfDirty();

        String messages = "";
        final SimpleResult result = completeVerifier.verify(bilBag);

        if (completeVerifier.isCancelled()) {
            DefaultBag.isComplete(Status.UNKNOWN);
            return "Completeness check cancelled.";
        }

        if (!result.isSuccess()) {
            messages = "Bag is not complete:\n";
            messages += result.toString();
        }
        DefaultBag.isComplete(result.isSuccess() ? Status.PASS : Status.FAILURE);
        if (!isNoProject()) {
            try {
                final String msgs = validateMetadata();
                if (msgs != null) {
                    messages += msgs;
                }
            } catch (final Exception ex) {
                ex.printStackTrace();
                final String msgs = "ERROR validating bag: \n" + ex.getMessage() + "\n";
                messages += msgs;
            }
        }
        return messages;
    }

    /**
     * @return messages
     */
    public String validateMetadata() {
        prepareBilBagInfoIfDirty();

        String messages = null;
        updateStrategy();
        final SimpleResult result = this.bilBag.verify(bagStrategy);
        if (result.toString() != null && !result.isSuccess()) {
            messages = "Bag-info fields are not all present for the project selected.\n";
            messages += result.toString();
        }
        DefaultBag.isValidMetadata(result.isSuccess() ? Status.PASS : Status.FAILURE);
        return messages;
    }

    /**
     * @param validVerifier ValidVerifierImpl
     * @return messages
     */
    public String validateBag(final ValidVerifierImpl validVerifier) {
        prepareBilBagInfoIfDirty();

        String messages = "";
        final SimpleResult result = validVerifier.verify(bilBag);

        if (validVerifier.isCancelled()) {
            isValid(Status.UNKNOWN);
            return "Validation check cancelled.";
        }

        if (!result.isSuccess()) {
            messages = "Bag is not valid:\n";
            messages += result.toString();
        }
        DefaultBag.isValid(result.isSuccess() ? Status.PASS : Status.FAILURE);
        if (result.isSuccess()) {
            isComplete(Status.PASS);
        }
        if (!isNoProject()) {
            final String msgs = validateMetadata();
            if (msgs != null) {
                messages += msgs;
            }
        }
        return messages;
    }

    /**
     * @param filename String
     * @return String
     */
    private static String fileStripSuffix(final String filename) {
        final StringTokenizer st = new StringTokenizer(filename, ".");
        return st.nextToken();
    }

    /**
     * @param bw Writer
     * @return null
     */
    private String writeBag(final Writer bw) {
        final String messages = null;
        String bagName;
        File localBagFile = null;
        final File parentDir;
        bagName = fileStripSuffix(getRootDir().getName());
        parentDir = getRootDir().getParentFile();
        log.debug("DefaultBag.writeBag parentDir: {}, bagName: {}", parentDir, bagName);

        this.setName(bagName);
        if (this.serialMode == NO_MODE) {
            localBagFile = new File(parentDir, this.getName());
        } else if (this.serialMode == ZIP_MODE) {
            final String s = bagName;
            final int i = s.lastIndexOf('.');
            if (i > 0 && i < s.length() - 1) {
                final String sub = s.substring(i + 1);
                if (!sub.equalsIgnoreCase(ZIP_LABEL)) {
                    bagName += "." + ZIP_LABEL;
                }
            } else {
                bagName += "." + ZIP_LABEL;
            }
            localBagFile = new File(parentDir, bagName);
      /*
       * long zipSize = this.getSize() / MB;
       * if (zipSize > 100) {
       * messages =
       * "WARNING: You may not be able to network transfer files > 100 MB!\n";
       * }
       */
        } else if (this.serialMode == TAR_MODE) {
            final String s = bagName;
            final int i = s.lastIndexOf('.');
            if (i > 0 && i < s.length() - 1) {
                if (!s.substring(i + 1).toLowerCase().equals(TAR_LABEL)) {
                    bagName += "." + TAR_LABEL;
                }
            } else {
                bagName += "." + TAR_LABEL;
            }
            localBagFile = new File(parentDir, bagName);
      /*
       * long zipSize = this.getSize() / MB;
       * if (zipSize > 100) {
       * messages =
       * "WARNING: You may not be able to network transfer files > 100 MB!\n";
       * }
       */
        } else if (this.serialMode == TAR_GZ_MODE) {
            final String s = bagName;
            final int i = s.lastIndexOf('.');
            if (i > 0 && i < s.length() - 1) {
                if (!s.substring(i + 1).toLowerCase().equals(TAR_GZ_LABEL)) {
                    bagName += "." + TAR_GZ_LABEL;
                }
            } else {
                bagName += "." + TAR_GZ_LABEL;
            }
            localBagFile = new File(parentDir, bagName);
      /*
       * long zipSize = this.getSize() / MB;
       * if (zipSize > 100) {
       * messages =
       * "WARNING: You may not be able to network transfer files > 100 MB!\n";
       * }
       */
        } else if (this.serialMode == TAR_BZ2_MODE) {
            final String s = bagName;
            final int i = s.lastIndexOf('.');
            if (i > 0 && i < s.length() - 1) {
                if (!s.substring(i + 1).toLowerCase().equals(TAR_BZ2_LABEL)) {
                    bagName += "." + TAR_BZ2_LABEL;
                }
            } else {
                bagName += "." + TAR_BZ2_LABEL;
            }
            localBagFile = new File(parentDir, bagName);
      /*
       * long zipSize = this.getSize() / MB;
       * if (zipSize > 100) {
       * messages =
       * "WARNING: You may not be able to network transfer files > 100 MB!\n";
       * }
       */
        }
        setBagFile(localBagFile);

        log.info("Bag-Info to write: {}", bilBag.getBagInfoTxt());

        this.isSerialized(false);
        final Bag newBag = bw.write(bilBag, localBagFile);
        if (newBag != null) {
            bilBag = newBag;
            // write successful
            this.isSerialized(true);
        }
        return null;
    }

    /**
     *
     */
    public void updateStrategy() {
        bagStrategy = getBagInfoStrategy();
    }

    /**
     * @return RequiredBagInfoTxtFieldsVerifier
     */
    private Verifier getBagInfoStrategy() {
        final List<String> rulesList = new ArrayList<>();
        final HashMap<String, BagInfoField> fieldMap = this.getInfo().getFieldMap();
        if (fieldMap != null) {

            for (final Entry<String, BagInfoField> entry : fieldMap.entrySet()) {
                if (entry.getValue().isRequired()) {
                    rulesList.add(entry.getKey());
                }
            }
        }
        final String[] rules = rulesList.toArray(new String[rulesList.size()]);

        return new RequiredBagInfoTxtFieldsVerifier(rules);
    }

    /**
     *
     */
    private void generateManifestFiles() {
        final DefaultCompleter completer = new DefaultCompleter(new BagFactory());
        if (this.isBuildPayloadManifest) {
            if (this.payloadManifestAlgorithm.equalsIgnoreCase(Manifest.Algorithm
                    .MD5.bagItAlgorithm)) {
                completer.setPayloadManifestAlgorithm(Algorithm.MD5);
            } else if (this.payloadManifestAlgorithm.equalsIgnoreCase(Manifest.Algorithm
                    .SHA1.bagItAlgorithm)) {
                completer.setPayloadManifestAlgorithm(Algorithm.SHA1);
            } else if (this.payloadManifestAlgorithm.equalsIgnoreCase(Manifest.Algorithm
                    .SHA256.bagItAlgorithm)) {
                completer.setPayloadManifestAlgorithm(Algorithm.SHA256);
            } else if (this.payloadManifestAlgorithm.equalsIgnoreCase(Manifest.Algorithm
                    .SHA512.bagItAlgorithm)) {
                completer.setPayloadManifestAlgorithm(Algorithm.SHA512);
            } else {
                completer.setPayloadManifestAlgorithm(Algorithm.MD5);
            }
            completer.setClearExistingPayloadManifests(true);
        }
        if (this.isBuildTagManifest) {
            completer.setClearExistingTagManifests(true);
            completer.setGenerateTagManifest(true);
            if (this.tagManifestAlgorithm.equalsIgnoreCase(Manifest.Algorithm.MD5.bagItAlgorithm)) {
                completer.setTagManifestAlgorithm(Algorithm.MD5);
            } else if (this.tagManifestAlgorithm.equalsIgnoreCase(Manifest.Algorithm
                    .SHA1.bagItAlgorithm)) {
                completer.setTagManifestAlgorithm(Algorithm.SHA1);
            } else if (this.tagManifestAlgorithm.equalsIgnoreCase(Manifest.Algorithm
                    .SHA256.bagItAlgorithm)) {
                completer.setTagManifestAlgorithm(Algorithm.SHA256);
            } else if (this.tagManifestAlgorithm.equalsIgnoreCase(Manifest.Algorithm
                    .SHA512.bagItAlgorithm)) {
                completer.setTagManifestAlgorithm(Algorithm.SHA512);
            } else {
                completer.setTagManifestAlgorithm(Algorithm.MD5);
            }
        }
        if (bilBag.getBagInfoTxt() != null) {
            completer.setGenerateBagInfoTxt(true);
        }
        bilBag = completer.complete(bilBag);
    }

    /**
     *
     */
    public void clear() {
        clearProfile();
        bagInfo.clearFields();

    }

    /**
     * @param field BagInfoField
     */
    public void addField(final BagInfoField field) {
        changeToDirty();
        isValidMetadata(Status.UNKNOWN);

        bagInfo.addField(field);
    }

    /**
     * @param key String
     */
    public void removeBagInfoField(final String key) {
        changeToDirty();
        isValidMetadata(Status.UNKNOWN);

        bagInfo.removeField(key);
    }

    /**
     * @param file File
     */
    public void addFileToPayload(final File file) {
        changeToDirty();
        isComplete(Status.UNKNOWN);

        bilBag.addFileToPayload(file);
    }

    /**
     * @return tags
     */
    public Collection<BagFile> getTags() {
        return bilBag.getTags();
    }

    /**
     * @param fileName String
     */
    public void removeBagFile(final String fileName) {
        changeToDirty();
        isComplete(Status.UNKNOWN);

        bilBag.removeBagFile(fileName);
    }

    /**
     * @param fileName String
     */
    public void removePayloadDirectory(final String fileName) {
        changeToDirty();
        isComplete(Status.UNKNOWN);
        bilBag.removePayloadDirectory(fileName);
    }

    /**
     * @return payload
     */
    public Collection<BagFile> getPayload() {
        return bilBag.getPayload();
    }

    /**
     * @return fetchtext
     */
    public FetchTxt getFetchTxt() {
        return bilBag.getFetchTxt();
    }

    /**
     *
     */
    private void changeToDirty() {
        this.dirty = true;
        isValid(Status.UNKNOWN);
    }

    /**
     *
     */
    private void prepareBilBagInfoIfDirty() {
        if (dirty) {
            bagInfo.prepareBilBagInfo(bilBag.getBagInfoTxt());
        }
    }


}
