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

import cool.pandora.modeller.Profile;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.BagInfoInputPane;
import cool.pandora.modeller.ui.BagTree;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.base.NewBagFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * StartNewBagHandler.
 *
 * @author gov.loc
 */
public class StartNewBagHandler extends AbstractAction {
    protected static final Logger log = LoggerFactory.getLogger(StartNewBagHandler.class);
    private static final long serialVersionUID = 1L;
    BagView bagView;

    /**
     * StartNewBagHandler.
     *
     * @param bagView BagView
     */
    public StartNewBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        newBag();
    }

    void newBag() {
        final NewBagFrame newBagFrame =
                new NewBagFrame(bagView, bagView.getPropertyMessage("bag" + ".frame.new"));
        newBagFrame.setVisible(true);
    }

    /**
     * createNewBag.
     *
     * @param profileName String
     */
    public void createNewBag(final String profileName) {
        log.info("Creating a new bag with version: {}, profile: {}",
                BagFactory.LATEST.versionString, profileName);

        bagView.clearBagHandler.clearExistingBag();
        final DefaultBag bag = bagView.getBag();
        bagView.infoInputPane.bagInfoInputPane.enableForms(true);

        final String bagName = bagView.getPropertyMessage("bag.label.noname");
        bag.setName(bagName);
        bagView.infoInputPane.setBagName(bagName);

        bagView.bagTagFileTree = new BagTree(bagView, bag.getName());
        final Collection<BagFile> tags = bag.getTags();
        for (final BagFile bf : tags) {
            bagView.bagTagFileTree.addNode(bf.getFilepath());
        }
        bagView.bagTagFileTreePanel.refresh(bagView.bagTagFileTree);
        bagView.updateBaggerRules();
        bag.setRootDir(bagView.getBagRootPath());

        bagView.infoInputPane.bagInfoInputPane.populateForms(bag);
        ApplicationContextUtil.addConsoleMessage("A new bag has been created in memory.");
        bagView.updateNewBag();

        // set bagItVersion
        bagView.infoInputPane.bagVersionValue.setText(BagFactory.LATEST.versionString);

        // change profile
        changeProfile(profileName);
    }

    // TODO refactor
    private void changeProfile(final String selected) {
        final Profile profile = bagView.getProfileStore().getProfile(selected);
        log.info("bagProject: {}", profile.getName());
        final DefaultBag bag = bagView.getBag();
        bag.setProfile(profile, true);
        BagInfoInputPane.updateProject(bagView);

        bagView.infoInputPane.setProfile(bag.getProfile().getName());
    }
}
