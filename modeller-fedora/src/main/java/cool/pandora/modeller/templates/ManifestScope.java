package cool.pandora.modeller.templates;

import java.util.Collections;
import java.util.List;

/**
 * ManifestScope
 *
 * @author Christopher Johnson
 */
public class ManifestScope extends Scope {
    private List<ManifestScope.Prefix> prefixes;
    private String collectionURI;
    private String sequenceURI;
    private String label;
    private String attribution;
    private String license;
    private String logo;
    private String rendering;
    private String author;
    private String published;

    /**
     * @param prefixes List
     * @return this
     */
    public ManifestScope fedoraPrefixes(final List<ManifestScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * @param sequenceURI String
     * @return this
     */
    public ManifestScope sequenceURI(final String sequenceURI) {
        this.sequenceURI = sequenceURI;
        return this;
    }

    /**
     * @param collectionURI String
     * @return this
     */
    public ManifestScope collectionURI(final String collectionURI) {
        this.collectionURI = collectionURI;
        return this;
    }

    /**
     * @param label String
     * @return this
     */
    public ManifestScope label(final String label) {
        this.label = label;
        return this;
    }

    /**
     * @param attribution String
     * @return this
     */
    public ManifestScope attribution(final String attribution) {
        this.attribution = attribution;
        return this;
    }

    /**
     * @param license String
     * @return this
     */
    public ManifestScope license(final String license) {
        this.license = license;
        return this;
    }

    /**
     * @param logo String
     * @return this
     */
    public ManifestScope logo(final String logo) {
        this.logo = logo;
        return this;
    }

    /**
     * @param rendering String
     * @return this
     */
    public ManifestScope rendering(final String rendering) {
        this.rendering = rendering;
        return this;
    }

    /**
     * @param author String
     * @return this
     */
    public ManifestScope author(final String author) {
        this.author = author;
        return this;
    }

    /**
     * @param published String
     * @return this
     */
    public ManifestScope published(final String published) {
        this.published = published;
        return this;
    }

    /**
     * @return Item
     */
    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.collectionURI, this.sequenceURI, this.label, this.attribution,
                        this.license, this.logo, this.rendering, this.author, this.published));
    }

    /**
     *
     */
    private static class Item {
        Item(final List<Prefix> prefixes, final String collectionURI, final String sequenceURI, final String label,
             final String attribution, final String license, final String logo, final String rendering,
             final String author, final String published) {
            this.prefixes = prefixes;
            this.collectionURI = collectionURI;
            this.sequenceURI = sequenceURI;
            this.label = label;
            this.attribution = attribution;
            this.license = license;
            this.logo = logo;
            this.rendering = rendering;
            this.author = author;
            this.published = published;

        }

        List<Prefix> prefixes;
        String collectionURI;
        String sequenceURI;
        String attribution;
        String label;
        String license;
        String logo;
        String rendering;
        String author;
        String published;
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
