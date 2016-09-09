package org.blume.modeller.templates;

import java.util.Collections;
import java.util.List;

public class CanvasScope extends Scope {
    private List<CanvasScope.Prefix> prefixes;
    private String resourceURI;
    private String listURI;
    private String canvasLabel;
    private int canvasHeight;
    private int canvasWidth;

    public CanvasScope fedoraPrefixes(List<CanvasScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    public CanvasScope resourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
        return this;
    }

    public CanvasScope listURI(String listURI) {
        this.listURI = listURI;
        return this;
    }

    public CanvasScope canvasLabel(String canvasLabel) {
        this.canvasLabel = canvasLabel;
        return this;
    }

    public CanvasScope canvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
        return this;
    }

    public CanvasScope canvasWidth(int canvasWidth) {
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
        double canvasHeight;
        double canvasWidth;
    }

    public static class Prefix {
        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }
}