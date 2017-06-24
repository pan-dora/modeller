/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cool.pandora.modeller.ui;

import cool.pandora.modeller.ui.jpanel.base.ConsolePane;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.support.AbstractView;

import javax.swing.JComponent;

/**
 * ConsoleView
 *
 * @author gov.loc
 */
public class ConsoleView extends AbstractView {

    private ConsolePane consolePane;

    /**
     *
     */
    public ConsoleView() {
        ((ConfigurableApplicationContext) Application.instance().getApplicationContext()).getBeanFactory()
                .registerSingleton("myConsoleView", this);
    }

    /**
     * @return consolePane
     */
    @Override
    protected JComponent createControl() {
        consolePane = new ConsolePane(getInitialConsoleMsg());
        return consolePane;
    }

    /**
     * @param context PageComponentContext
     */
    @Override
    protected void registerLocalCommandExecutors(final PageComponentContext context) {
        context.register("startCommand", ApplicationContextUtil.getBagView().startExecutor);
        context.register("openCommand", ApplicationContextUtil.getBagView().openExecutor);
        context.register("createBagInPlaceCommand", ApplicationContextUtil.getBagView().createBagInPlaceExecutor);
        context.register("clearCommand", ApplicationContextUtil.getBagView().clearExecutor);
        context.register("validateCommand", ApplicationContextUtil.getBagView().validateExecutor);
        context.register("completeCommand", ApplicationContextUtil.getBagView().completeExecutor);
        context.register("addDataCommand", ApplicationContextUtil.getBagView().addDataExecutor);
        context.register("saveBagCommand", ApplicationContextUtil.getBagView().saveBagExecutor);
        context.register("saveBagAsCommand", ApplicationContextUtil.getBagView().saveBagAsExecutor);
    }

    /**
     * @param messages String
     */
    public void addConsoleMessages(final String messages) {
        consolePane.addConsoleMessages(messages);
    }

    /**
     * @return message
     */
    private String getInitialConsoleMsg() {
        return getMessage("consolepane.msg.help") + "\n\n" + getMessage("consolepane.status.help") + "\n\n";
    }
}
