package org.blume.modeller.templates;

public class ResourceTemplate {

    private String renderedTemplate;

    public static ResourceTemplate.ResourceTemplateBuilder template() {
        return new ResourceTemplate.ResourceTemplateBuilder();
    }

    private ResourceTemplate(String template, ResourceScope scope) {
        TemplateBuilder mb = new TemplateBuilder()
                .scope(scope)
                .template(template);
        this.renderedTemplate = mb.build();
    }

    public String render() {
        return this.renderedTemplate;
    }

    public static class ResourceTemplateBuilder {
        private String template;
        private Scope scope;
        private boolean throwExceptionOnFailure;

        ResourceTemplateBuilder() {
        }

        public ResourceTemplate.ResourceTemplateBuilder scope(Scope scope) {
            this.scope = scope;
            return this;
        }

        public ResourceTemplate.ResourceTemplateBuilder template(String template) {
            this.template = template;
            return this;
        }

        public ResourceTemplate build() {
            return new ResourceTemplate(this.template, (ResourceScope) this.scope);
        }

        public ResourceTemplate.ResourceTemplateBuilder throwExceptionOnFailure() {
            this.throwExceptionOnFailure = true;
            return this;
        }
    }

}
