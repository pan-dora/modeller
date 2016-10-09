package org.blume.modeller.ui.handlers.base;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.base.BagView;

public class AddDataExecutor extends AbstractActionCommandExecutor {
  BagView bagView;

  public AddDataExecutor(BagView bagView) {
    super();
    this.bagView = bagView;
  }

  @Override
  public void execute() {
    bagView.addDataHandler.addData();
  }

}
