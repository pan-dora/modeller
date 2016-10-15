package org.blume.modeller.templates;

import java.util.Collections;
import java.util.List;

public class ListScope extends Scope {
    private List<ListScope.Prefix> prefixes;
    private String resourceURI;
    private String listURI;
    private String canvasLabel;
    private int canvasHeight;
    private int canvasWidth;

    public ListScope fedoraPrefixes(List<ListScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    public ListScope resourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
        return this;
    }

    public ListScope listURI(String listURI) {
        this.listURI = listURI;
        return this;
    }

    public ListScope canvasLabel(String canvasLabel) {
        this.canvasLabel = canvasLabel;
        return this;
    }

    public ListScope canvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
        return this;
    }

    public ListScope canvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
        return this;
    }

    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.resourceURI, this.listURI, this.canvasLabel,
                        this.canvasHeight, this.canvasWidth)
        );
    }

    static class Item {
        Item(List<Prefix> prefixes, String resourceURI, String listURI, String canvasLabel,
             int canvasHeight, int canvasWidth) {
            this.prefixes = prefixes;
            this.resourceURI = resourceURI;
            this.listURI = listURI;
            this.canvasLabel = canvasLabel;
            this.canvasHeight = canvasHeight;
            this.canvasWidth = canvasWidth;
        }

        List<Prefix> prefixes;
        String resourceURI;
        String listURI;
        String canvasLabel;
        int canvasHeight;
        int canvasWidth;
    }

    public static class Prefix {
        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }
}