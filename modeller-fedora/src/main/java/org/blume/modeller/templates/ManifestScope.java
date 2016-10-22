package org.blume.modeller.templates;

import java.util.Collections;
import java.util.List;

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

    public ManifestScope fedoraPrefixes(List<ManifestScope.Prefix> prefixes) {
        this.prefixes = prefixes;
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

    public ManifestScope attribution(String attribution) {
        this.attribution = attribution;
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

    public ManifestScope rendering(String rendering) {
        this.rendering = rendering;
        return this;
    }

    public ManifestScope author(String author) {
        this.author = author;
        return this;
    }

    public ManifestScope published(String published) {
        this.published = published;
        return this;
    }

    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.collectionURI, this.sequenceURI, this.label, this.attribution, this
                        .license, this.logo, this.rendering,this.author, this.published));
    }

    private static class Item {
        Item(List<Prefix> prefixes, String collectionURI, String sequenceURI, String label, String attribution,
             String license, String logo, String rendering, String author, String published) {
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

    public static class Prefix {
        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }

}
