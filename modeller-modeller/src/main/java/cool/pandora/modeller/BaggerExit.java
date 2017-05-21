package cool.pandora.modeller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.command.support.ExitCommand;

/**
 * Bagger Exit
 *
 * @author gov.loc
 */
public class BaggerExit extends ExitCommand {
    protected static final Logger log = LoggerFactory.getLogger(BaggerExit.class);

    /**
     * Closes the single {@link org.springframework.richclient.application.Application} instance.
     *
     * @see org.springframework.richclient.application.Application#close()
     */
    @Override
    public void doExecuteCommand() {
        super.doExecuteCommand();
        log.debug("BaggerExit.doExecuteCommand");
        System.exit(0);
    }
}
