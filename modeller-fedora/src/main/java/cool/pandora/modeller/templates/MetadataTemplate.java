package cool.pandora.modeller.templates;

/**
 * MetadataTemplate
 *
 * @author Christopher Johnson
 */
public class MetadataTemplate {

    private final String renderedTemplate;

    /**
     * @return MetadataTemplateBuilder
     */
    public static MetadataTemplate.MetadataTemplateBuilder template() {
        return new MetadataTemplate.MetadataTemplateBuilder();
    }

    /**
     * @param template String
     * @param scope    Scope
     */
    private MetadataTemplate(final String template, final Scope scope) {
        final TemplateBuilder mb = new TemplateBuilder().scope(scope).template(template);
        this.renderedTemplate = mb.build();
    }

    /**
     * @return renderedTemplate
     */
    public String render() {
        return this.renderedTemplate;
    }

    /**
     *
     */
    public static class MetadataTemplateBuilder {
        private String template;
        private Scope scope;
        private boolean throwExceptionOnFailure;

        MetadataTemplateBuilder() {
        }

        /**
         * @param scope Scope
         * @return this
         */
        public MetadataTemplate.MetadataTemplateBuilder scope(final Scope scope) {
            this.scope = scope;
            return this;
        }

        /**
         * @param template String
         * @return this
         */
        public MetadataTemplate.MetadataTemplateBuilder template(final String template) {
            this.template = template;
            return this;
        }

        /**
         * @return MetadataTemplate
         */
        public MetadataTemplate build() {
            return new MetadataTemplate(this.template, this.scope);
        }

        /**
         * @return this
         */
        public MetadataTemplate.MetadataTemplateBuilder throwExceptionOnFailure() {
            this.throwExceptionOnFailure = true;
            return this;
        }
    }

}
