package org.blume.modeller.templates;

import java.util.Collections;
import java.util.List;

public class ListScope extends Scope {
    private List<ListScope.Prefix> prefixes;
    private String sequenceGraph;
    private String serviceNode;

    public ListScope fedoraPrefixes(List<ListScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    public ListScope sequenceGraph(String sequenceGraph) {
        this.sequenceGraph = sequenceGraph;
        return this;
    }

    public ListScope serviceNode(String serviceNode) {
        this.serviceNode = serviceNode;
        return this;
    }

    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.sequenceGraph, this.serviceNode)
        );
    }

    static class Item {
        Item(List<Prefix> prefixes, String sequenceGraph, String serviceNode) {
            this.prefixes = prefixes;
            this.sequenceGraph = sequenceGraph;
            this.serviceNode = serviceNode;
        }

        List<Prefix> prefixes;
        String sequenceGraph;
        String serviceNode;
    }

    public static class Prefix {
        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }
}