package cool.pandora.modeller.templates;

import java.util.Collections;
import java.util.List;

/**
 * CollectionScope
 *
 * @author Christopher Johnson
 */
public class CollectionScope extends Scope {
    private List<CollectionScope.Prefix> prefixes;
    private String sequenceGraph;

    /**
     * @param prefixes List
     * @return this
     */
    public CollectionScope fedoraPrefixes(final List<CollectionScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * @param sequenceGraph String
     * @return this
     */
    public CollectionScope sequenceGraph(final String sequenceGraph) {
        this.sequenceGraph = sequenceGraph;
        return this;
    }

    /**
     * @return List
     */
    List<Item> items() {
        return Collections.singletonList(new Item(this.prefixes, this.sequenceGraph));
    }

    /**
     *
     */
    static class Item {
        Item(final List<Prefix> prefixes, final String sequenceGraph) {
            this.prefixes = prefixes;
            this.sequenceGraph = sequenceGraph;
        }

        List<Prefix> prefixes;
        String sequenceGraph;
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