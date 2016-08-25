package org.blume.modeller.templates;

import java.util.Arrays;
import java.util.List;

public class ResourceTemplate {

    private String renderedTemplate;

    public static ResourceTemplate.ResourceTemplateBuilder template() {
        return new ResourceTemplate.ResourceTemplateBuilder();
    }

    public static ResourceTemplate.Scope scope() {
        return new ResourceTemplate.Scope();
    }

    protected ResourceTemplate(String template, Scope scope) {
        TemplateBuilder mb = new TemplateBuilder(template, scope);
        this.renderedTemplate = mb.build();
    }

    public String render() {
        return this.renderedTemplate;
    }

    public static class ResourceTemplateBuilder {
        private String template;
        private Scope scope;
        private boolean throwExceptionOnFailure;

        public ResourceTemplateBuilder() {
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
            return new ResourceTemplate(this.template, this.scope);
        }

        public ResourceTemplate.ResourceTemplateBuilder throwExceptionOnFailure() {
            this.throwExceptionOnFailure = true;
            return this;
        }
    }

    public static class Scope {
        private List prefixes;
        private String serviceURI;

        public ResourceTemplate.Scope fedoraPrefixes(List<Scope.Prefix> prefixes) {
            this.prefixes = prefixes;
            return this;
        }

        public ResourceTemplate.Scope serviceURI(String serviceURI) {
            this.serviceURI = serviceURI;
            return this;
        }

        List<Item> items() {
            return Arrays.asList(
                    new Item(this.prefixes,
                            this.serviceURI)
            );
        }

        static class Item {
            Item(List<Prefix> prefixes, String serviceURI) {
                this.prefixes = prefixes;
                this.serviceURI = serviceURI;
            }

            List<Prefix> prefixes;
            String serviceURI;
        }

        public static class Prefix {
            public Prefix(String prefix) {
                this.prefix = prefix;
            }

            String prefix;
        }
    }

}
