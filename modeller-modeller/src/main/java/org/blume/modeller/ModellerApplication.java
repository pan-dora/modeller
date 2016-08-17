package org.blume.modeller;

import org.springframework.beans.PropertyAccessException;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessException;
import org.springframework.richclient.application.ApplicationLauncher;

/**
 * Main driver that starts the Bagger spring rich client application.
 */
public class ModellerApplication {
    protected static final Logger log = LoggerFactory.getLogger(ModellerApplication.class);

    public static void main(String[] args) {
        String rootContextDirectoryClassPath = "org/blume/modeller/ctx";

        String startupContextPath = rootContextDirectoryClassPath + "/common/richclient-startup-context.xml";

        String richclientApplicationContextPath = rootContextDirectoryClassPath + "/common/richclient-application-context.xml";

        String businessLayerContextPath = rootContextDirectoryClassPath + "/common/business-layer-context.xml";

        try {
            new ApplicationLauncher(startupContextPath, new String[]{richclientApplicationContextPath, businessLayerContextPath});
        } catch (IllegalStateException ex1) {
            log.error("IllegalStateException during startup", ex1);
            JOptionPane.showMessageDialog(new JFrame(), "An illegal state error occured.\n", "Bagger startup error!", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (PropertyAccessException ex) {
            log.error("PropertyAccessException during startup", ex);
            JOptionPane.showMessageDialog(new JFrame(), "An error occured loading properties.\n", "Bagger startup error!", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (RuntimeException e) {
            log.error("RuntimeException during startup", e);
            String msg = e.getMessage();
            if (msg.contains("SAXParseException")) {
                JOptionPane.showMessageDialog(new JFrame(), "An error occured parsing application context.  You may have no internet access.\n",
                        "Bagger startup error!", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "An error occured during startup.\n", "Bagger startup error!", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(1);
        }
    }

}