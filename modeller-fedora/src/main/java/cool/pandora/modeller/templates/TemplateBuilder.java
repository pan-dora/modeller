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

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.StringWriter;

/**
 * TemplateBuilder.
 *
 * @author Christopher Johnson
 */
public class TemplateBuilder {
    private String template;
    private Scope scope;

    TemplateBuilder() {
    }

    /**
     * scope.
     *
     * @param scope Scope
     * @return this
     */
    public TemplateBuilder scope(final Scope scope) {
        this.scope = scope;
        return this;
    }

    /**
     * template.
     *
     * @param template String
     * @return this
     */
    public TemplateBuilder template(final String template) {
        this.template = template;
        return this;
    }

    /**
     * build.
     *
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
