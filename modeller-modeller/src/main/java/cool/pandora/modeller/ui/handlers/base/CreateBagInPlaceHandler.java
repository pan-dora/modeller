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
import cool.pandora.modeller.bag.impl.DefaultBagInfo;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.base.NewBagInPlaceFrame;

import gov.loc.repository.bagit.BagFactory;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Create Bag in Place Handler.
 *
 * @author gov.loc
 */
public class CreateBagInPlaceHandler extends AbstractAction implements Progress {
    private static final long serialVersionUID = 1L;
    protected static final Logger log = LoggerFactory.getLogger(StartNewBagHandler.class);
    private final BagView bagView;

    /**
     * CreateBagInPlaceHandler.
     *
     * @param bagView BagView
     */
    public CreateBagInPlaceHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        createBagInPlace();
    }

    @Override
    public void execute() {
        BagView.statusBarEnd();
    }

    void createBagInPlace() {
        final NewBagInPlaceFrame newBagInPlaceFrame =
                new NewBagInPlaceFrame(bagView, bagView.getPropertyMessage("bag" + ".frame" + ""
                        + ".newbaginplace"));
        newBagInPlaceFrame.setBag(bagView.getBag());
        newBagInPlaceFrame.setVisible(true);
    }

    /**
     * createPreBag.
     *
     * @param dataFile File
     * @param profileName String
     */
    public void createPreBag(final File dataFile, final String profileName) {
        if (dataFile != null && profileName != null) {
            log.info("Creating a new bag in place with data: {}, version: {}, profile: {}",
                    dataFile.getName(),
                    BagFactory.LATEST.versionString, profileName);
            bagView.clearBagHandler.clearExistingBag();
            try {
                bagView.getBag().createPreBag(dataFile);
            } catch (final Exception e) {
                BagView.showWarningErrorDialog("Error - bagging in place", e.getMessage());
                return;
            }
            final DefaultBag bag = bagView.getBag();

            final String bagFileName = dataFile.getName();
            bag.setName(bagFileName);
            bagView.infoInputPane.setBagName(bagFileName);

            setProfile(profileName);

            bagView.saveBagHandler.save(dataFile);
        } else {
            log.warn("datafile is null? {} profileName is null? {}", dataFile == null,
                    profileName == null);
        }
    }

    /**
     * Prepares the call to Create Bag in Place and
     * adding .keep files in Empty Pay load Folder(s)
     *
     * @param dataFile File
     * @param profileName String
     */
    public void createPreBagAddKeepFilesToEmptyFolders(final File dataFile, final String
            profileName) {
        if (dataFile != null && profileName != null) {
            log.info("Creating a new bag in place with data: {}, version: {}, profile: {}",
                    dataFile.getName(),
                    BagFactory.LATEST.versionString, profileName);
            bagView.clearBagHandler.clearExistingBag();
            try {
                bagView.getBag().createPreBagAddKeepFilesToEmptyFolders(dataFile);
            } catch (final Exception e) {
                BagView.showWarningErrorDialog("Error - bagging in place",
                        "No file or directory selection was " + "made!\n");
                return;
            }
            final DefaultBag bag = bagView.getBag();

            final String bagFileName = dataFile.getName();
            bag.setName(bagFileName);
            bagView.infoInputPane.setBagName(bagFileName);

            setProfile(profileName);

            bagView.saveBagHandler.save(dataFile);
        } else {
            log.warn("datafile is null? {} profileName is null? {}", dataFile == null,
                    profileName == null);
        }
    }

    private void setProfile(final String selected) {
        final Profile profile = bagView.getProfileStore().getProfile(selected);
        log.info("bagProject: {}", profile.getName());

        final DefaultBag bag = bagView.getBag();
        bag.setProfile(profile, true);

        final Map<String, String> map = new HashMap<>();
        map.put(DefaultBagInfo.FIELD_LC_PROJECT, profile.getName());
        bagView.getBag().updateBagInfo(map);
    }

}
