package org.blume.modeller.ui.handlers;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.BagView;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class EditTagFilesHandler extends AbstractAction {
  private static final long serialVersionUID = 1L;
  BagView bagView;
  DefaultBag bag;

  public EditTagFilesHandler(BagView bagView) {
    super();
    this.bagView = bagView;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.bag = bagView.getBag();
  }
}
