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

import javax.swing.JOptionPane;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessException;
import org.springframework.richclient.application.ApplicationLauncher;

/**
 * Main driver that starts the Bagger spring rich client application.
 *
 * @author gov.loc
 */
final class ModellerApplication {
    protected static final Logger log = LoggerFactory.getLogger(ModellerApplication.class);

    private ModellerApplication() {
    }

    public static void main(final String[] args) {
        final String rootContextDirectoryClassPath = "cool/pandora/modeller/ctx";

        final String startupContextPath = rootContextDirectoryClassPath
                + "/common/richclient-startup-context.xml";

        final String richclientApplicationContextPath =
                rootContextDirectoryClassPath + "/common/richclient-application-context.xml";

        final String businessLayerContextPath = rootContextDirectoryClassPath
                + "/common/business-layer-context.xml";

        try {
            new ApplicationLauncher(startupContextPath,
                    new String[]{richclientApplicationContextPath, businessLayerContextPath});
        } catch (final IllegalStateException ex1) {
            log.error("IllegalStateException during startup", ex1);
            JOptionPane.showMessageDialog(new JFrame(), "An illegal state error occured.\n",
                    "Bagger startup error!",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (final PropertyAccessException ex) {
            log.error("PropertyAccessException during startup", ex);
            JOptionPane.showMessageDialog(new JFrame(), "An error occured loading properties.\n",
                    "Bagger startup " + "error!", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (final RuntimeException e) {
            log.error("RuntimeException during startup", e);
            final String msg = e.getMessage();
            if (msg.contains("SAXParseException")) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "An error occured parsing application context.  You may " + "have no "
                                + "internet access.\n",
                        "Bagger startup error!", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "An error occured during startup.\n",
                        "Bagger startup " + "error!", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(1);
        }
    }

}