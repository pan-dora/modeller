package org.blume.modeller.templates;

import java.util.Collections;
import java.util.List;

public class ManifestScope extends Scope {
    private List<ManifestScope.Prefix> prefixes;
    private String collectionURI;
    private String label;
    private String sequenceURI;
    private String attributionLabel;
    private String license;
    private String logo;
    private String format;

    public ManifestScope fedoraPrefixes(List<ManifestScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    public ManifestScope attributionLabel(String attributionLabel) {
        this.attributionLabel = attributionLabel;
        return this;
    }

    public ManifestScope sequenceURI(String sequenceURI) {
        this.sequenceURI = sequenceURI;
        return this;
    }

    public ManifestScope collectionURI(String collectionURI) {
        this.collectionURI = collectionURI;
        return this;
    }

    public ManifestScope label(String label) {
        this.label = label;
        return this;
    }

    public ManifestScope license(String license) {
        this.license = license;
        return this;
    }

    public ManifestScope logo(String logo) {
        this.logo = logo;
        return this;
    }

    public ManifestScope format(String format) {
        this.format = format;
        return this;
    }

    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.collectionURI, this.label, this.sequenceURI,
                        this.attributionLabel, this.license, this.logo, this.format)
        );
    }

    private static class Item {
        Item(List<Prefix> prefixes, String collectionURI, String label, String sequenceURI,
             String attributionLabel, String license, String logo, String format) {
            this.prefixes = prefixes;
            this.collectionURI = collectionURI;
            this.label = label;
            this.sequenceURI = sequenceURI;
            this.attributionLabel = attributionLabel;
            this.license = license;
            this.logo = logo;
            this.format = format;
        }

        List<Prefix> prefixes;
        String collectionURI;
        String label;
        String sequenceURI;
        String attributionLabel;
        String license;
        String logo;
        String format;
    }

    public static class Prefix {
        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }

}
