package cool.pandora.modeller.bag;

import gov.loc.repository.bagit.utilities.FilenameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.MessageFormat;

/**
 * Bagger needs to know where the file came from so that it can be retrieved:
 * rootSrc,
 * and also where the file belongs within the bag: bagSrc.
 * Once the logical location of bagSrc becomes a physical copy isInBag=true
 * If the file is unselected from the BagTree, it is marked to be removed from
 * the bag
 * by setting isIncluded=false.
 * <p>
 * In order to create a bag, all data files to be included are copied to the bag
 * data dir.
 * If they already exist they are written over, or deleted if marked as
 * isIncluded=false.
 * <p>
 * If the file comes from a pre-existing bag, then the rootSrc and bagSrc will
 * be the same,
 * and isInBag=true, otherwise it comes from somewhere else and needs to be
 * placed in the
 * current bag.
 *
 * @author Jon Steinbach
 */
public class BaggerFileEntity {
    protected static final Logger log = LoggerFactory.getLogger(BaggerFileEntity.class);

    private File rootParent; // c:\\user\my documents\
    private File rootSrc; // c:\\user\my documents\datadir\dir1\file1
    private File bagSrc; // c:\\user\my documents\bag\data\datadir\dir1\file
    private String normalizedName; // datadir\dir1\file1
    private boolean isInBag = false;
    private boolean isIncluded = true;

    @Override
    public String toString() {
        return this.getNormalizedName();
    }

    /**
     * @param name String
     */
    public void setNormalizedName(final String name) {
        this.normalizedName = name;
    }

    /**
     * @return normalizedName
     */
    private String getNormalizedName() {
        return this.normalizedName;
    }

    /**
     * @param file File
     */
    public void setRootParent(final File file) {
        this.rootParent = file;
    }

    /**
     * @return rootParent
     */
    public File getRootParent() {
        return this.rootParent;
    }

    /**
     * @param file File
     */
    public void setRootSrc(final File file) {
        this.rootSrc = file;
    }

    /**
     * @return rootSrc
     */
    public File getRootSrc() {
        return this.rootSrc;
    }

    /**
     * @param bagDir File
     * @param src    File
     */
    public void setBagSrc(final File bagDir, final File src) {
        // TODO given the bag location, create the location the src file will exist
        // within the bag data directory, e.g. strip off parent of src and replace
        // it with the bag data dir
        this.bagSrc = new File(bagDir, src.getPath());
    }

    /**
     * @param file File
     */
    public void setBagSrc(final File file) {
        this.bagSrc = file;
    }

    /**
     * @return bagSrc
     */
    public File getBagSrc() {
        return this.bagSrc;
    }

    /**
     * @param b boolean
     */
    public void setIsInBag(final boolean b) {
        this.isInBag = b;
    }

    /**
     * @return isInBag
     */
    public boolean getIsInBag() {
        return this.isInBag;
    }

    /**
     * @param b boolean
     */
    public void setIsIncluded(final boolean b) {
        this.isIncluded = b;
    }

    /**
     * @return isIncluded
     */
    public boolean getIsIncluded() {
        return this.isIncluded;
    }

    /**
     * @return success
     */
    public boolean copyRootToBag() {
        final boolean success = false;

        // TODO perform the copy
        this.isInBag = true;
        return success;
    }

    /**
     * @param basePath String
     * @param filename String
     * @return filenameWithoutBasePath
     * @throws RuntimeException RuntimeException
     */
    public static String removeBasePath(final String basePath, final String filename) throws RuntimeException {
        if (filename == null) {
            throw new RuntimeException("Cannot remove basePath from null");
        }
        final String normBasePath = normalize(basePath);
        final String normFilename = normalize(filename);
        final String filenameWithoutBasePath;
        if (basePath == null) {
            filenameWithoutBasePath = normFilename;
        } else {
            if (!normFilename.startsWith(normBasePath)) {
                throw new RuntimeException(
                        MessageFormat.format("Cannot remove basePath {0} from {1}", basePath, filename));
            }
            if (normBasePath.equals(normFilename)) {
                filenameWithoutBasePath = "";
            } else {
                final int delta;
                if (normBasePath.endsWith("/") || normBasePath.endsWith("\\")) {
                    delta = 0;
                } else {
                    delta = 1;
                }
                filenameWithoutBasePath = normFilename.substring(normBasePath.length() + delta);
                log.trace("filenamewithoutbasepath: {}", filenameWithoutBasePath);
            }
        }
        log.debug(MessageFormat
                .format("Removing {0} from {1} resulted in {2}", basePath, filename, filenameWithoutBasePath));
        return filenameWithoutBasePath;
    }

    /**
     * @param filename String
     * @return filename (no extension)
     * @throws RuntimeException RuntimeException
     */
    public static String removeFileExtension(final String filename) throws RuntimeException {
        if (filename == null) {
            throw new RuntimeException("Cannot remove file extension from null");
        }
        return filename.split("\\.[^\\.]*$")[0];
    }

    /**
     * @param filename String
     * @return normalizedFilename
     */
    public static String normalize(final String filename) {
        return FilenameHelper.normalizePathSeparators(filename);
    }
}
