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
package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

/**
 * Complete Bag Handler
 *
 * @author gov.loc
 */
public class CompleteBagHandler extends AbstractAction implements Progress {
    private static final long serialVersionUID = 1L;
    private final BagView bagView;
    private String messages;

    /**
     * @param bagView BagView
     */
    public CompleteBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    /**
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        completeBag();
    }

    void completeBag() {
        bagView.statusBarBegin(this, "Checking if complete...", null);
    }

    @Override
    public void execute() {
        final DefaultBag bag = bagView.getBag();
        try {
            final CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
            completeVerifier.addProgressListener(bagView.task);
            bagView.longRunningProcess = completeVerifier;

            messages = bag.completeBag(completeVerifier);

            if (messages != null && !messages.trim().isEmpty()) {
                BagView.showWarningErrorDialog("Warning - incomplete", "Is complete result: " + messages);
            } else {
                BagView.showWarningErrorDialog("Is Complete Dialog", "Bag is complete.");
            }

            SwingUtilities.invokeLater(() -> ApplicationContextUtil.addConsoleMessage(messages));

        } catch (final Exception e) {
            e.printStackTrace();

            if (bagView.longRunningProcess.isCancelled()) {
                BagView.showWarningErrorDialog("Check cancelled", "Completion check cancelled.");
            } else {
                BagView.showWarningErrorDialog("Warning - complete check interrupted",
                        "Error checking bag " + "completeness: " + e.getMessage());
            }
        } finally {
            bagView.task.done();
            BagView.statusBarEnd();
        }
    }
}
