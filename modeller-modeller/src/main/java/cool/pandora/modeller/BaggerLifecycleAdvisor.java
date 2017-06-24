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
package cool.pandora.modeller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.config.ApplicationWindowConfigurer;
import org.springframework.richclient.application.config.DefaultApplicationLifecycleAdvisor;
import org.springframework.richclient.application.setup.SetupWizard;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;


/**
 * Custom application lifecycle implementation that configures the app at well
 * defined points within
 * its lifecycle.
 *
 * @author Jon Steinbach
 */
public class BaggerLifecycleAdvisor extends DefaultApplicationLifecycleAdvisor {
    protected static final Logger log = LoggerFactory.getLogger(BaggerLifecycleAdvisor.class);

    /**
     * Show a setup wizard before actual applicationWindow is created. This should
     * happen only on Application
     * startup and only once. (note: for this to happen only once, a state should
     * be preserved, which is not
     * the case with this sample)
     */
    @Override
    public void onPreStartup() {
        log.debug("BaggerLifeCycleAdvisor.onPreStartup");
        final boolean useWizard = false;
        if (useWizard && getApplication().getApplicationContext().containsBean("setupWizard")) {
            final SetupWizard setupWizard =
                    (SetupWizard) getApplication().getApplicationContext().getBean("setupWizard", SetupWizard.class);
            setupWizard.execute();
        }
    }

    /**
     * Additional window configuration before it is created.
     */
    @Override
    public void onPreWindowOpen(final ApplicationWindowConfigurer configurer) {
        super.onPreWindowOpen(configurer);
        // comment out to hide the menubar, or reduce window size...
        // configurer.setShowMenuBar(false);
        final Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        configurer.setInitialSize(new Dimension(rect.width, rect.height));
        //configurer.setInitialSize(new Dimension(1024, 768));
    }

    /**
     * When commands are created, lookup the login command and execute it.
     */
    @Override
    public void onCommandsCreated(final ApplicationWindow window) {
        // ActionCommand command = (ActionCommand)
        // window.getCommandManager().getCommand("loginCommand",
        // ActionCommand.class);
        // TODO: implement login and logout if db is on remote server
        // command.execute();
    }
}