package org.blume.modeller.ui.handlers.base;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.BagView;

public class SaveBagAsExecutor extends AbstractActionCommandExecutor {
  BagView bagView;

  public SaveBagAsExecutor(BagView bagView) {
    super();
    this.bagView = bagView;
  }

  @Override
  public void execute() {
    bagView.saveBagAsHandler.openSaveBagAsFrame();
  }

}
