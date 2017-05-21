package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.base.SaveBagFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Save Bag As Handler
 *
 * @author gov.loc
 */
public class SaveBagAsHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    BagView bagView;
    DefaultBag bag;

    /**
     * @param bagView BagView
     */
    public SaveBagAsHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openSaveBagAsFrame();
    }

    void openSaveBagAsFrame() {
        bag = bagView.getBag();
        final SaveBagFrame saveBagFrame = new SaveBagFrame(bagView, bagView.getPropertyMessage("bag.frame.save"));
        saveBagFrame.setBag(bag);
        saveBagFrame.setVisible(true);
    }
}
