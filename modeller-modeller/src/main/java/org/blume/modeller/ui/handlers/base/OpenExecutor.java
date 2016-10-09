package org.blume.modeller.ui.handlers.base;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.base.BagView;

public class OpenExecutor extends AbstractActionCommandExecutor {
  BagView bagView;

  public OpenExecutor(BagView bagView) {
    super();
    setEnabled(true);
    this.bagView = bagView;
  }

  @Override
  public void execute() {
    bagView.openBagHandler.openBag();

  }

}
