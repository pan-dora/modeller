package org.blume.modeller.ui.handlers.base;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.base.BagView;

public class ValidateExecutor extends AbstractActionCommandExecutor {
  BagView bagView;

  public ValidateExecutor(BagView bagView) {
    super();
    this.bagView = bagView;
  }

  @Override
  public void execute() {
    bagView.validateBagHandler.validateBag();
  }

}
