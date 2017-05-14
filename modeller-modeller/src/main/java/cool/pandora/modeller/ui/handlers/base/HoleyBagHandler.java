package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;

/**
 * @author gov.loc
 */
public class HoleyBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    BagView bagView;
    DefaultBag bag;

    /**
     * @param bagView BagView
     */
    public HoleyBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        this.bag = bagView.getBag();

        final JCheckBox cb = (JCheckBox) e.getSource();

        // Determine status
        final boolean isSelected = cb.isSelected();
        if (isSelected) {
            bag.isHoley(true);
        } else {
            bag.isHoley(false);
        }
    /*
     * String messages = "";
     * bagView.updateBaggerRules();
     * 
     * bagView.bagInfoInputPane.populateForms(bag, true);
     * messages = bagView.bagInfoInputPane.updateForms(bag);
     * bagView.updateBagInfoInputPaneMessages(messages);
     * bagView.bagInfoInputPane.update(bag);
     * 
     * bagView.bagInfoInputPane.updateSelected(bag);
     * bagView.compositePane.updateCompositePaneTabs(bag, messages);
     * bagView.tagManifestPane.updateCompositePaneTabs(bag);
     * bagView.setBag(bag);
     */
    }
}
