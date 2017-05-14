package cool.pandora.modeller.templates;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.StringWriter;

/**
 * TemplateBuilder
 *
 * @author Christopher Johnson
 */
public class TemplateBuilder {
    private String template;
    private Scope scope;

    TemplateBuilder() {
    }

    /**
     * @param scope Scope
     * @return this
     */
    public TemplateBuilder scope(final Scope scope) {
        this.scope = scope;
        return this;
    }

    /**
     * @param template String
     * @return this
     */
    public TemplateBuilder template(final String template) {
        this.template = template;
        return this;
    }

    /**
     * @return String
     */
    public String build() {
        final MustacheFactory mf = new DefaultMustacheFactory();
        final Mustache template = mf.compile(this.template);

        final StringWriter writer = new StringWriter();
        template.execute(writer, this.scope);
        return writer.toString();
    }

}
