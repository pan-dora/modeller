package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Edit Tag Files Handler
 *
 * @author gov.loc
 */
public class EditTagFilesHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    BagView bagView;
    DefaultBag bag;

    /**
     * @param bagView BagView
     */
    public EditTagFilesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        this.bag = bagView.getBag();
    }
}
