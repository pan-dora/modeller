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
 * WordScope
 *
 * @author Christopher Johnson
 */
public class WordScope extends Scope {
    private List<WordScope.Prefix> prefixes;
    private String canvasURI;
    private String resourceContainerURI;
    private String chars;

    /**
     * @param prefixes List
     * @return this
     */
    public WordScope fedoraPrefixes(final List<WordScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * @param canvasURI String
     * @return this
     */
    public WordScope canvasURI(final String canvasURI) {
        this.canvasURI = canvasURI;
        return this;
    }

    /**
     * @param resourceContainerURI String
     * @return this
     */
    public WordScope resourceContainerURI(final String resourceContainerURI) {
        this.resourceContainerURI = resourceContainerURI;
        return this;
    }

    /**
     * @param chars String
     * @return this
     */
    public WordScope chars(final String chars) {
        this.chars = chars;
        return this;
    }

    /**
     * @return Item
     */
    List<Item> items() {
        return Collections
                .singletonList(new Item(this.prefixes, this.canvasURI, this.resourceContainerURI, this.chars));
    }

    /**
     *
     */
    static class Item {
        Item(final List<Prefix> prefixes, final String canvasURI, final String resourceContainerURI,
             final String chars) {
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