package org.blume.modeller.ui.handlers.base;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.BagView;

public class StartExecutor extends AbstractActionCommandExecutor {
  BagView bagView;

  public StartExecutor(BagView bagView) {
    super();
    setEnabled(true);
    this.bagView = bagView;
  }

  @Override
  public void execute() {
    bagView.startNewBagHandler.newBag();
  }

}
