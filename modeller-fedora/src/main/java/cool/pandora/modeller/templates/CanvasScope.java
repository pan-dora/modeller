package cool.pandora.modeller.templates;

import java.util.Collections;
import java.util.List;

/**
 * CanvasScope
 *
 * @author Christopher Johnson
 */
public class CanvasScope extends Scope {
    private List<CanvasScope.Prefix> prefixes;
    private String resourceURI;
    private String listURI;
    private String canvasLabel;
    private int canvasHeight;
    private int canvasWidth;

    /**
     * @param prefixes List
     * @return scope
     */
    public CanvasScope fedoraPrefixes(final List<CanvasScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * @param resourceURI String
     * @return scope
     */
    public CanvasScope resourceURI(final String resourceURI) {
        this.resourceURI = resourceURI;
        return this;
    }

    /**
     * @param listURI String
     * @return scope
     */
    public CanvasScope listURI(final String listURI) {
        this.listURI = listURI;
        return this;
    }

    /**
     * @param canvasLabel String
     * @return scope
     */
    public CanvasScope canvasLabel(final String canvasLabel) {
        this.canvasLabel = canvasLabel;
        return this;
    }

    /**
     * @param canvasHeight int
     * @return scope
     */
    public CanvasScope canvasHeight(final int canvasHeight) {
        this.canvasHeight = canvasHeight;
        return this;
    }

    /**
     * @param canvasWidth int
     * @return scope
     */
    public CanvasScope canvasWidth(final int canvasWidth) {
        this.canvasWidth = canvasWidth;
        return this;
    }

    /**
     * @return Item
     */
    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.resourceURI, this.listURI, this.canvasLabel, this.canvasHeight,
                        this.canvasWidth));
    }

    /**
     *
     */
    static class Item {
        Item(final List<Prefix> prefixes, final String resourceURI, final String listURI, final String canvasLabel,
             final int canvasHeight, final int canvasWidth) {
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