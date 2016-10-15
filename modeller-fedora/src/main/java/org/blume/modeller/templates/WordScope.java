package org.blume.modeller.templates;

import java.util.Collections;
import java.util.List;

public class WordScope extends Scope {
    private List<WordScope.Prefix> prefixes;
    private String canvasURI;
    private String resourceContainerURI;
    private String chars;

    public WordScope fedoraPrefixes(List<WordScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    public WordScope canvasURI(String canvasURI) {
        this.canvasURI = canvasURI;
        return this;
    }

    public WordScope resourceContainerURI(String resourceContainerURI) {
        this.resourceContainerURI = resourceContainerURI;
        return this;
    }

    public WordScope chars(String chars) {
        this.chars = chars;
        return this;
    }

    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.canvasURI, this.resourceContainerURI, this.chars)
        );
    }

    static class Item {
        Item(List<Prefix> prefixes, String canvasURI, String resourceContainerURI, String chars) {
            this.prefixes = prefixes;
            this.canvasURI = canvasURI;
            this.resourceContainerURI = resourceContainerURI;
            this.chars = chars;
        }

        List<Prefix> prefixes;
        String canvasURI;
        String resourceContainerURI;
        String chars;
    }

    public static class Prefix {
        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }
}