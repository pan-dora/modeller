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
 * CanvasScope.
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
     * fedoraPrefixes.
     *
     * @param prefixes List
     * @return scope
     */
    public CanvasScope fedoraPrefixes(final List<CanvasScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * resourceURI.
     *
     * @param resourceURI String
     * @return scope
     */
    public CanvasScope resourceURI(final String resourceURI) {
        this.resourceURI = resourceURI;
        return this;
    }

    /**
     * listURI.
     *
     * @param listURI String
     * @return scope
     */
    public CanvasScope listURI(final String listURI) {
        this.listURI = listURI;
        return this;
    }

    /**
     * canvasLabel.
     *
     * @param canvasLabel String
     * @return scope
     */
    public CanvasScope canvasLabel(final String canvasLabel) {
        this.canvasLabel = canvasLabel;
        return this;
    }

    /**
     * canvasHeight.
     *
     * @param canvasHeight int
     * @return scope
     */
    public CanvasScope canvasHeight(final int canvasHeight) {
        this.canvasHeight = canvasHeight;
        return this;
    }

    /**
     * canvasWidth.
     *
     * @param canvasWidth int
     * @return scope
     */
    public CanvasScope canvasWidth(final int canvasWidth) {
        this.canvasWidth = canvasWidth;
        return this;
    }

    /**
     * items.
     *
     * @return Item
     */
    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.resourceURI, this.listURI, this.canvasLabel, this
                        .canvasHeight,
                        this.canvasWidth));
    }

    /**
     * Item.
     */
    static class Item {
        Item(final List<Prefix> prefixes, final String resourceURI, final String listURI, final
        String canvasLabel,
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