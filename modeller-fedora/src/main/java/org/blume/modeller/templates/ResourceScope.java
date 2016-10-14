package org.blume.modeller.templates;

import java.util.Collections;
import java.util.List;

public class ResourceScope extends Scope {
    private List<ResourceScope.Prefix> prefixes;
    private String filename;
    private String serviceURI;
    private String formatName;
    private int imgWidth;
    private int imgHeight;

    public ResourceScope fedoraPrefixes(List<ResourceScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    public ResourceScope filename(String filename) {
        this.filename = filename;
        return this;
    }

    public ResourceScope serviceURI(String serviceURI) {
        this.serviceURI = serviceURI;
        return this;
    }

    public ResourceScope formatName(String formatName) {
        this.formatName = formatName;
        return this;
    }

    public ResourceScope imgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
        return this;
    }

    public ResourceScope imgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
        return this;
    }

    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.filename, this.serviceURI, this.formatName,
                        this.imgHeight, this.imgWidth)
        );
    }

    static class Item {
        Item(List<Prefix> prefixes, String filename, String serviceURI, String formatName,
             int imgHeight, int imgWidth) {
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

    public static class Prefix {
        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }
}