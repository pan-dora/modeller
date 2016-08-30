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
        private String filename;
        private String serviceURI;
        private String formatName;
        private double imgWidth;
        private double imgHeight;

        public ResourceTemplate.Scope fedoraPrefixes(List<Scope.Prefix> prefixes) {
            this.prefixes = prefixes;
            return this;
        }

        public ResourceTemplate.Scope filename(String filename) {
            this.filename = filename;
            return this;
        }

        public ResourceTemplate.Scope serviceURI(String serviceURI) {
            this.serviceURI = serviceURI;
            return this;
        }

        public ResourceTemplate.Scope formatName(String formatName) {
            this.formatName = formatName;
            return this;
        }

        public ResourceTemplate.Scope imgWidth(double imgWidth) {
            this.imgWidth = imgWidth;
            return this;
        }

        public ResourceTemplate.Scope imgHeight(double imgHeight) {
            this.imgHeight = imgHeight;
            return this;
        }

        List<Item> items() {
            return Arrays.asList(
                    new Item(this.prefixes, this.filename, this.serviceURI, this.formatName,
                            this.imgHeight, this.imgWidth)
            );
        }

        static class Item {
            Item(List<Prefix> prefixes, String filename, String serviceURI, String formatName,
                 double imgHeight, double imgWidth) {
                this.prefixes = prefixes;
                this.filename = filename;
                this.serviceURI = serviceURI;
                this.formatName = formatName;
                this.imgHeight = imgHeight;
                this.imgWidth = imgWidth;
            }

            List<Prefix> prefixes;
            String filename;
            String serviceURI;
            String formatName;
            double imgHeight;
            double imgWidth;
        }

        public static class Prefix {
            public Prefix(String prefix) {
                this.prefix = prefix;
            }

            String prefix;
        }
    }

}
