package org.blume.modeller.templates;

public class CollectionTemplate {

    private String renderedTemplate;

    public static CollectionTemplate.CollectionTemplateBuilder template() {
        return new CollectionTemplate.CollectionTemplateBuilder();
    }

    private CollectionTemplate(String template, CollectionScope scope) {
        TemplateBuilder mb = new TemplateBuilder()
                .scope(scope)
                .template(template);
        this.renderedTemplate = mb.build();
    }

    public String render() {
        return this.renderedTemplate;
    }

    public static class CollectionTemplateBuilder {
        private String template;
        private Scope scope;
        private boolean throwExceptionOnFailure;

        CollectionTemplateBuilder() {
        }

        public CollectionTemplate.CollectionTemplateBuilder scope(Scope scope) {
            this.scope = scope;
            return this;
        }

        public CollectionTemplate.CollectionTemplateBuilder template(String template) {
            this.template = template;
            return this;
        }

        public CollectionTemplate build() {
            return new CollectionTemplate(this.template, (CollectionScope) this.scope);
        }

        public CollectionTemplate.CollectionTemplateBuilder throwExceptionOnFailure() {
            this.throwExceptionOnFailure = true;
            return this;
        }
    }

}

