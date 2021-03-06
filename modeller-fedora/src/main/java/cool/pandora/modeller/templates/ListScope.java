/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller.templates;

import java.util.Collections;
import java.util.List;

/**
 * ListScope.
 *
 * @author Christopher Johnson
 */
public class ListScope extends Scope {
    private List<ListScope.Prefix> prefixes;
    private String sequenceGraph;
    private String serviceNode;

    /**
     * fedoraPrefixes.
     *
     * @param prefixes List
     * @return this
     */
    public ListScope fedoraPrefixes(final List<ListScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * sequenceGraph.
     *
     * @param sequenceGraph String
     * @return this
     */
    public ListScope sequenceGraph(final String sequenceGraph) {
        this.sequenceGraph = sequenceGraph;
        return this;
    }

    /**
     * serviceNode.
     *
     * @param serviceNode String
     * @return this
     */
    public ListScope serviceNode(final String serviceNode) {
        this.serviceNode = serviceNode;
        return this;
    }

    /**
     * items.
     *
     * @return Item
     */
    List<Item> items() {
        return Collections
                .singletonList(new Item(this.prefixes, this.sequenceGraph, this.serviceNode));
    }

    /**
     * Item.
     */
    static class Item {
        List<Prefix> prefixes;
        String sequenceGraph;
        String serviceNode;

        Item(final List<Prefix> prefixes, final String sequenceGraph, final String serviceNode) {
            this.prefixes = prefixes;
            this.sequenceGraph = sequenceGraph;
            this.serviceNode = serviceNode;
        }
    }

    /**
     * Prefix.
     */
    public static class Prefix {
        String prefix;

        /**
         * Prefix.
         *
         * @param prefix String
         */
        public Prefix(final String prefix) {
            this.prefix = prefix;
        }
    }
}