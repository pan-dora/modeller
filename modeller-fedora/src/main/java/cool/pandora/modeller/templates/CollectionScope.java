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
 * CollectionScope.
 *
 * @author Christopher Johnson
 */
public class CollectionScope extends Scope {
    private List<CollectionScope.Prefix> prefixes;
    private String sequenceGraph;

    /**
     * fedoraPrefixes.
     *
     * @param prefixes List
     * @return this
     */
    public CollectionScope fedoraPrefixes(final List<CollectionScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * sequenceGraph.
     *
     * @param sequenceGraph String
     * @return this
     */
    public CollectionScope sequenceGraph(final String sequenceGraph) {
        this.sequenceGraph = sequenceGraph;
        return this;
    }

    /**
     * items.
     *
     * @return List
     */
    List<Item> items() {
        return Collections.singletonList(new Item(this.prefixes, this.sequenceGraph));
    }

    /**
     * Item.
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
     * Prefix.
     */
    public static class Prefix {
        /**
         * Prefix.
         *
         * @param prefix String
         */
        public Prefix(final String prefix) {
            this.prefix = prefix;
        }

        String prefix;
    }
}