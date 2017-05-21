package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import gov.loc.repository.bagit.verify.impl.ParallelManifestChecksumVerifier;
import gov.loc.repository.bagit.verify.impl.ValidVerifierImpl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

/**
 * Validate Bag Handler
 *
 * @author gov.loc
 */
public class ValidateBagHandler extends AbstractAction implements Progress {
    private static final long serialVersionUID = 1L;
    private final BagView bagView;
    private String messages;

    /**
     * @param bagView BagView
     */
    public ValidateBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        validateBag();
    }

    void validateBag() {
        bagView.statusBarBegin(this, "Validating bag...", "verifying file checksum");
    }

    @Override
    public void execute() {
        final DefaultBag bag = bagView.getBag();
        try {
            final CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();

            final ParallelManifestChecksumVerifier manifestVerifier = new ParallelManifestChecksumVerifier();

            final ValidVerifierImpl validVerifier = new ValidVerifierImpl(completeVerifier, manifestVerifier);
            validVerifier.addProgressListener(bagView.task);
            bagView.longRunningProcess = validVerifier;
      /* */
            messages = bag.validateBag(validVerifier);

            if (messages != null && !messages.trim().isEmpty()) {
                BagView.showWarningErrorDialog("Warning - validation failed", "Validation result: " + messages);
            } else {
                BagView.showWarningErrorDialog("Validation Dialog", "Validation successful.");
            }

            SwingUtilities.invokeLater(() -> ApplicationContextUtil.addConsoleMessage(messages));
        } catch (final Exception e) {
            e.printStackTrace();
            if (bagView.longRunningProcess.isCancelled()) {
                BagView.showWarningErrorDialog("Validation cancelled", "Validation cancelled.");
            } else {
                BagView.showWarningErrorDialog("Warning - validation interrupted",
                        "Error trying validate bag: " + e.getMessage());
            }
        } finally {
            bagView.task.done();
            BagView.statusBarEnd();
        }
    }
}
