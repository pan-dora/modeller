package org.blume.modeller.ui.handlers;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.BagView;

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
