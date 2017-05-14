package cool.pandora.modeller.templates;

import java.util.Collections;
import java.util.List;

/**
 * ListScope
 *
 * @author Christopher Johnson
 */
public class ListScope extends Scope {
    private List<ListScope.Prefix> prefixes;
    private String sequenceGraph;
    private String serviceNode;

    /**
     * @param prefixes List
     * @return this
     */
    public ListScope fedoraPrefixes(final List<ListScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * @param sequenceGraph String
     * @return this
     */
    public ListScope sequenceGraph(final String sequenceGraph) {
        this.sequenceGraph = sequenceGraph;
        return this;
    }

    /**
     * @param serviceNode String
     * @return this
     */
    public ListScope serviceNode(final String serviceNode) {
        this.serviceNode = serviceNode;
        return this;
    }

    /**
     * @return Item
     */
    List<Item> items() {
        return Collections.singletonList(new Item(this.prefixes, this.sequenceGraph, this.serviceNode));
    }

    /**
     *
     */
    static class Item {
        Item(final List<Prefix> prefixes, final String sequenceGraph, final String serviceNode) {
            this.prefixes = prefixes;
            this.sequenceGraph = sequenceGraph;
            this.serviceNode = serviceNode;
        }

        List<Prefix> prefixes;
        String sequenceGraph;
        String serviceNode;
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