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
 * ResourceScope.
 *
 * @author Christopher Johnson
 */
public class ResourceScope extends Scope {
    private List<ResourceScope.Prefix> prefixes;
    private String filename;
    private String serviceURI;
    private String formatName;
    private int imgWidth;
    private int imgHeight;

    /**
     * fedoraPrefixes.
     *
     * @param prefixes List
     * @return this
     */
    public ResourceScope fedoraPrefixes(final List<ResourceScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * filename.
     *
     * @param filename String
     * @return this
     */
    public ResourceScope filename(final String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * serviceURI.
     *
     * @param serviceURI String
     * @return this
     */
    public ResourceScope serviceURI(final String serviceURI) {
        this.serviceURI = serviceURI;
        return this;
    }

    /**
     * formatName.
     *
     * @param formatName String
     * @return this
     */
    public ResourceScope formatName(final String formatName) {
        this.formatName = formatName;
        return this;
    }

    /**
     * imgWidth.
     *
     * @param imgWidth int
     * @return this
     */
    public ResourceScope imgWidth(final int imgWidth) {
        this.imgWidth = imgWidth;
        return this;
    }

    /**
     * imgHeight.
     *
     * @param imgHeight int
     * @return this
     */
    public ResourceScope imgHeight(final int imgHeight) {
        this.imgHeight = imgHeight;
        return this;
    }

    /**
     * items.
     *
     * @return Item
     */
    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.filename, this.serviceURI, this.formatName,
                        this.imgHeight, this.imgWidth));
    }

    /**
     * Item.
     */
    static class Item {
        List<Prefix> prefixes;
        String filename;
        String serviceURI;
        String formatName;
        int imgHeight;
        int imgWidth;

        Item(final List<Prefix> prefixes, final String filename, final String serviceURI,
             final String formatName, final int imgHeight, final int imgWidth) {
            this.prefixes = prefixes;
            this.filename = filename;
            this.serviceURI = serviceURI;
            this.formatName = formatName;
            this.imgHeight = imgHeight;
            this.imgWidth = imgWidth;
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