package org.blume.modeller.ui.handlers;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.BagView;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class UpdateBagHandler extends AbstractAction {
  private static final long serialVersionUID = 1L;
  private BagView bagView;

  public UpdateBagHandler(BagView bagView) {
    super();
    this.bagView = bagView;
  }

  public void setBagView(BagView bagView) {
    this.bagView = bagView;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    updateBag(bagView.getBag());
  }

  public void updateBag(DefaultBag bag) {
    bagView.infoInputPane.bagInfoInputPane.updateForms(bag);
  }
}
