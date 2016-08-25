package org.blume.modeller.templates;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.StringWriter;

public class TemplateBuilder {
    private String template;
    private ResourceTemplate.Scope scope;

    public TemplateBuilder(String template, ResourceTemplate.Scope scope) {
        this.template = template;
        this.scope = scope;
    }

    public String build() {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache template = mf.compile(this.template);

        StringWriter writer = new StringWriter();
        template.execute(writer, this.scope);
        return writer.toString();
    }

}
