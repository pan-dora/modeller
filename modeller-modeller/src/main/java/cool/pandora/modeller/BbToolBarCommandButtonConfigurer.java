package cool.pandora.modeller;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.command.config.ToolBarCommandButtonConfigurer;
import org.springframework.richclient.util.Assert;

import javax.swing.AbstractButton;

/**
 * Custom <code>CommandButtonConfigurer</code> for buttons on the toolbar.
 * <p>
 * Allows using large icons for toolbar.
 *
 * @author gov.loc
 */
public class BbToolBarCommandButtonConfigurer extends ToolBarCommandButtonConfigurer {

    /**
     * Indicates if large icons should be used.
     */
    private Boolean useLargeIcons;

    /**
     * Creates this command button configurer.
     */
    public BbToolBarCommandButtonConfigurer() {

        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(final AbstractButton button, final AbstractCommand command,
                          final CommandFaceDescriptor faceDescriptor) {

        super.configure(button, command, faceDescriptor);
        faceDescriptor.configureIconInfo(button, this.getUseLargeIcons());
    }

    /**
     * Gets the useLargeIcons.
     *
     * @return the useLargeIcons
     */
    private Boolean getUseLargeIcons() {

        if (this.useLargeIcons == null) {
            this.setUseLargeIcons(Boolean.TRUE);
        }

        return this.useLargeIcons;
    }

    /**
     * Sets the useLargeIcons.
     *
     * @param useLargeIcons the useLargeIcons to set
     */
    public void setUseLargeIcons(final Boolean useLargeIcons) {
        Assert.notNull(useLargeIcons, "useLargeIcons");
        this.useLargeIcons = useLargeIcons;
    }
}