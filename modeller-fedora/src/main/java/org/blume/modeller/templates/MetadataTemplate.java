package org.blume.modeller.templates;

public class MetadataTemplate {

    private String renderedTemplate;

    public static MetadataTemplate.MetadataTemplateBuilder template() {
        return new MetadataTemplate.MetadataTemplateBuilder();
    }

    private MetadataTemplate(String template, Scope scope) {
        TemplateBuilder mb = new TemplateBuilder()
                .scope(scope)
                .template(template);
        this.renderedTemplate = mb.build();
    }

    public String render() {
        return this.renderedTemplate;
    }

    public static class MetadataTemplateBuilder {
        private String template;
        private Scope scope;
        private boolean throwExceptionOnFailure;

        MetadataTemplateBuilder() {
        }

        public MetadataTemplate.MetadataTemplateBuilder scope(Scope scope) {
            this.scope = scope;
            return this;
        }

        public MetadataTemplate.MetadataTemplateBuilder template(String template) {
            this.template = template;
            return this;
        }

        public MetadataTemplate build() {
            return new MetadataTemplate(this.template, this.scope);
        }

        public MetadataTemplate.MetadataTemplateBuilder throwExceptionOnFailure() {
            this.throwExceptionOnFailure = true;
            return this;
        }
    }

}
