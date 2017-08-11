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

/**
 * MetadataTemplate.
 *
 * @author Christopher Johnson
 */
public class MetadataTemplate {

    private final String renderedTemplate;

    /**
     * template.
     *
     * @return MetadataTemplateBuilder
     */
    public static MetadataTemplate.MetadataTemplateBuilder template() {
        return new MetadataTemplate.MetadataTemplateBuilder();
    }

    /**
     * MetadataTemplate.
     *
     * @param template String
     * @param scope Scope
     */
    private MetadataTemplate(final String template, final Scope scope) {
        final TemplateBuilder mb = new TemplateBuilder().scope(scope).template(template);
        this.renderedTemplate = mb.build();
    }

    /**
     * render.
     *
     * @return renderedTemplate
     */
    public String render() {
        return this.renderedTemplate;
    }

    /**
     * MetadataTemplateBuilder.
     */
    public static class MetadataTemplateBuilder {
        private String template;
        private Scope scope;
        private boolean throwExceptionOnFailure;

        MetadataTemplateBuilder() {
        }

        /**
         * scope.
         *
         * @param scope Scope
         * @return this
         */
        public MetadataTemplate.MetadataTemplateBuilder scope(final Scope scope) {
            this.scope = scope;
            return this;
        }

        /**
         * template.
         *
         * @param template String
         * @return this
         */
        public MetadataTemplate.MetadataTemplateBuilder template(final String template) {
            this.template = template;
            return this;
        }

        /**
         * build.
         *
         * @return MetadataTemplate
         */
        public MetadataTemplate build() {
            return new MetadataTemplate(this.template, this.scope);
        }

        /**
         * throwExceptionOnFailure.
         *
         * @return this
         */
        public MetadataTemplate.MetadataTemplateBuilder throwExceptionOnFailure() {
            this.throwExceptionOnFailure = true;
            return this;
        }
    }

}
