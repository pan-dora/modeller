package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Update Bag Handler
 *
 * @author gov.loc
 */
public class UpdateBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    /**
     * @param bagView BagView
     */
    public UpdateBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    /**
     * @param bagView BagView
     */
    public void setBagView(final BagView bagView) {
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        updateBag(bagView.getBag());
    }

    /**
     * @param bag DefaultBag
     */
    public void updateBag(final DefaultBag bag) {
        bagView.infoInputPane.bagInfoInputPane.updateForms(bag);
    }
}
