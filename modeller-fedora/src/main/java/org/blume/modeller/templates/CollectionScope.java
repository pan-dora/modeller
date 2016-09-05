package org.blume.modeller.templates;

import java.util.Collections;
import java.util.List;

public class CollectionScope extends Scope {
    private List<CollectionScope.Prefix> prefixes;
    private String sequenceGraph;

    public CollectionScope fedoraPrefixes(List<CollectionScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    public CollectionScope sequenceGraph(String sequenceGraph) {
        this.sequenceGraph = sequenceGraph;
        return this;
    }

    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.sequenceGraph)
        );
    }

    static class Item {
        Item(List<Prefix> prefixes, String sequenceGraph) {
            this.prefixes = prefixes;
            this.sequenceGraph = sequenceGraph;
        }

        List<Prefix> prefixes;
        String sequenceGraph;
    }

    public static class Prefix {
        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }
}