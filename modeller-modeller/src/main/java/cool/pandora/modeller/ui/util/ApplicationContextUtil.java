package cool.pandora.modeller.ui.util;

import cool.pandora.modeller.ui.ConsoleView;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.image.ImageSource;

import javax.swing.UIManager;
import java.awt.Image;
import java.util.Locale;

/**
 * ApplicationContextUtil
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
     * @param propertyName String
     * @return message
     */
    public static String getMessage(final String propertyName) {
        return Application.instance().getApplicationContext()
                .getMessage(propertyName, null, propertyName, Locale.getDefault());
    }

    /**
     * @param imageName String
     * @return imageName
     */
    public static Image getImage(final String imageName) {
        final ImageSource source = (ImageSource) ApplicationServicesLocator.services().getService(ImageSource.class);
        return source.getImage(imageName);
    }

    /**
     * @return Application.instance()
     */
    public static BagView getBagView() {
        return (BagView) Application.instance().getApplicationContext().getBean("myBagView");
    }

    /**
     * @param message String
     */
    public static void addConsoleMessage(final String message) {
        final ConsoleView consoleView =
                (ConsoleView) Application.instance().getApplicationContext().getBean("myConsoleView");
        consoleView.addConsoleMessages(message);
    }

}
