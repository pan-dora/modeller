package org.blume.modeller.ui.handlers.base;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.BagView;

public class CreateBagInPlaceExecutor extends AbstractActionCommandExecutor {
  BagView bagView;

  public CreateBagInPlaceExecutor(BagView bagView) {
    super();
    setEnabled(true);
    this.bagView = bagView;
  }

  @Override
  public void execute() {
    bagView.createBagInPlaceHandler.createBagInPlace();
  }

}
