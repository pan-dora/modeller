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

package cool.pandora.modeller.ui.util;

import cool.pandora.modeller.ui.ConsoleView;
import cool.pandora.modeller.ui.jpanel.base.BagView;

import java.awt.Image;
import java.util.Locale;
import javax.swing.UIManager;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.image.ImageSource;

/**
 * ApplicationContextUtil.
 *
 * @author loc.gov
 */
public class ApplicationContextUtil {

    private ApplicationContextUtil() {
    }

    static {
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
    }

    /**
     * getMessage.
     *
     * @param propertyName String
     * @return message
     */
    public static String getMessage(final String propertyName) {
        return Application.instance().getApplicationContext()
                .getMessage(propertyName, null, propertyName, Locale.getDefault());
    }

    /**
     * getImage.
     *
     * @param imageName String
     * @return imageName
     */
    public static Image getImage(final String imageName) {
        final ImageSource source = (ImageSource) ApplicationServicesLocator.services().getService(
                ImageSource.class);
        return source.getImage(imageName);
    }

    /**
     * getBagView.
     *
     * @return Application.instance()
     */
    public static BagView getBagView() {
        return (BagView) Application.instance().getApplicationContext().getBean("myBagView");
    }

    /**
     * addConsoleMessage.
     *
     * @param message String
     */
    public static void addConsoleMessage(final String message) {
        final ConsoleView consoleView =
                (ConsoleView) Application.instance().getApplicationContext().getBean(
                        "myConsoleView");
        consoleView.addConsoleMessages(message);
    }

}
