package org.blume.modeller.templates;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.StringWriter;

public class TemplateBuilder {
    private String template;
    private Scope scope;

    public TemplateBuilder() {
    }

    public TemplateBuilder scope(Scope scope) {
        this.scope = scope;
        return this;
    }

    public TemplateBuilder template(String template) {
        this.template = template;
        return this;
    }

    public String build() {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache template = mf.compile(this.template);

        StringWriter writer = new StringWriter();
        template.execute(writer, this.scope);
        return writer.toString();
    }

}
