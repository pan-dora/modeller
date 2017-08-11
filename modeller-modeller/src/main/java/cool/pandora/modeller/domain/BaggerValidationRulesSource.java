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

package cool.pandora.modeller.domain;

import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;

/**
 * Rule Validation.
 *
 * @author gov.loc
 */
public class BaggerValidationRulesSource extends DefaultRulesSource {

    /**
     * BaggerValidationRulesSource.
     */
    public BaggerValidationRulesSource() {
        super();
    }

    /**
     * init.
     */
    public void init() {
        clear();
    }

    /**
     * clear.
     */
    public void clear() {
        final java.util.List<Rules> empty = new java.util.ArrayList<Rules>();
        setRules(empty);
    }

}
