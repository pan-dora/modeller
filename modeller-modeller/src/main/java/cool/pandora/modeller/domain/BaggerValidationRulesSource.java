package cool.pandora.modeller.domain;

import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;

/**
 * Rule Validation
 *
 * @author gov.loc
 */
public class BaggerValidationRulesSource extends DefaultRulesSource {

    /**
     *
     */
    public BaggerValidationRulesSource() {
        super();
    }

    /**
     *
     */
    public void init() {
        clear();
    }

    /**
     *
     */
    public void clear() {
        final java.util.List<Rules> empty = new java.util.ArrayList<Rules>();
        setRules(empty);
    }

}
