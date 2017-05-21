package cool.pandora.modeller.templates;

import java.util.Collections;
import java.util.List;

/**
 * ResourceScope
 *
 * @author Christopher Johnson
 */
public class ResourceScope extends Scope {
    private List<ResourceScope.Prefix> prefixes;
    private String filename;
    private String serviceURI;
    private String formatName;
    private int imgWidth;
    private int imgHeight;

    /**
     * @param prefixes List
     * @return this
     */
    public ResourceScope fedoraPrefixes(final List<ResourceScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * @param filename String
     * @return this
     */
    public ResourceScope filename(final String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * @param serviceURI String
     * @return this
     */
    public ResourceScope serviceURI(final String serviceURI) {
        this.serviceURI = serviceURI;
        return this;
    }

    /**
     * @param formatName String
     * @return this
     */
    public ResourceScope formatName(final String formatName) {
        this.formatName = formatName;
        return this;
    }

    /**
     * @param imgWidth int
     * @return this
     */
    public ResourceScope imgWidth(final int imgWidth) {
        this.imgWidth = imgWidth;
        return this;
    }

    /**
     * @param imgHeight int
     * @return this
     */
    public ResourceScope imgHeight(final int imgHeight) {
        this.imgHeight = imgHeight;
        return this;
    }

    /**
     * @return Item
     */
    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.filename, this.serviceURI, this.formatName, this.imgHeight,
                        this.imgWidth));
    }

    /**
     *
     */
    static class Item {
        Item(final List<Prefix> prefixes, final String filename, final String serviceURI, final String formatName,
             final int imgHeight, final int imgWidth) {
            this.prefixes = prefixes;
            this.filename = filename;
            this.serviceURI = serviceURI;
            this.formatName = formatName;
            this.imgHeight = imgHeight;
            this.imgWidth = imgWidth;
        }

        List<Prefix> prefixes;
        String filename;
        String serviceURI;
        String formatName;
        int imgHeight;
        int imgWidth;
    }

    /**
     *
     */
    public static class Prefix {
        /**
         * @param prefix String
         */
        public Prefix(final String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }
}