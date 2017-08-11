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
 * ManifestScope.
 *
 * @author Christopher Johnson
 */
public class ManifestScope extends Scope {
    private List<ManifestScope.Prefix> prefixes;
    private String collectionURI;
    private String sequenceURI;
    private String label;
    private String attribution;
    private String license;
    private String logo;
    private String rendering;
    private String author;
    private String published;

    /**
     * fedoraPrefixes.
     *
     * @param prefixes List
     * @return this
     */
    public ManifestScope fedoraPrefixes(final List<ManifestScope.Prefix> prefixes) {
        this.prefixes = prefixes;
        return this;
    }

    /**
     * sequenceURI.
     *
     * @param sequenceURI String
     * @return this
     */
    public ManifestScope sequenceURI(final String sequenceURI) {
        this.sequenceURI = sequenceURI;
        return this;
    }

    /**
     * collectionURI.
     *
     * @param collectionURI String
     * @return this
     */
    public ManifestScope collectionURI(final String collectionURI) {
        this.collectionURI = collectionURI;
        return this;
    }

    /**
     * label.
     *
     * @param label String
     * @return this
     */
    public ManifestScope label(final String label) {
        this.label = label;
        return this;
    }

    /**
     * attribution.
     *
     * @param attribution String
     * @return this
     */
    public ManifestScope attribution(final String attribution) {
        this.attribution = attribution;
        return this;
    }

    /**
     * license.
     *
     * @param license String
     * @return this
     */
    public ManifestScope license(final String license) {
        this.license = license;
        return this;
    }

    /**
     * logo.
     *
     * @param logo String
     * @return this
     */
    public ManifestScope logo(final String logo) {
        this.logo = logo;
        return this;
    }

    /**
     * rendering.
     *
     * @param rendering String
     * @return this
     */
    public ManifestScope rendering(final String rendering) {
        this.rendering = rendering;
        return this;
    }

    /**
     * author.
     *
     * @param author String
     * @return this
     */
    public ManifestScope author(final String author) {
        this.author = author;
        return this;
    }

    /**
     * published.
     *
     * @param published String
     * @return this
     */
    public ManifestScope published(final String published) {
        this.published = published;
        return this;
    }

    /**
     * items.
     *
     * @return Item
     */
    List<Item> items() {
        return Collections.singletonList(
                new Item(this.prefixes, this.collectionURI, this.sequenceURI, this.label, this
                        .attribution,
                        this.license, this.logo, this.rendering, this.author, this.published));
    }

    /**
     * Item.
     */
    private static class Item {
        Item(final List<Prefix> prefixes, final String collectionURI, final String sequenceURI,
             final String label,
             final String attribution, final String license, final String logo, final String
                     rendering,
             final String author, final String published) {
            this.prefixes = prefixes;
            this.collectionURI = collectionURI;
            this.sequenceURI = sequenceURI;
            this.label = label;
            this.attribution = attribution;
            this.license = license;
            this.logo = logo;
            this.rendering = rendering;
            this.author = author;
            this.published = published;

        }

        List<Prefix> prefixes;
        String collectionURI;
        String sequenceURI;
        String attribution;
        String label;
        String license;
        String logo;
        String rendering;
        String author;
        String published;
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
