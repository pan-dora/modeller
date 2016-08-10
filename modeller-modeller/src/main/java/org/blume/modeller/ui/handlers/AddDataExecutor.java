package org.blume.modeller.ui.handlers;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.BagView;

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
