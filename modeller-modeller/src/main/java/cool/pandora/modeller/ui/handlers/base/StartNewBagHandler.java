package cool.pandora.modeller.ui.handlers.base;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.AbstractAction;

import cool.pandora.modeller.ui.BagInfoInputPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cool.pandora.modeller.Profile;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.BagTree;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.base.NewBagFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;

/**
 * @author gov.loc
 */
public class StartNewBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    protected static final Logger log = LoggerFactory.getLogger(StartNewBagHandler.class);

    BagView bagView;

    /**
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
        final NewBagFrame newBagFrame = new NewBagFrame(bagView, bagView.getPropertyMessage("bag.frame.new"));
        newBagFrame.setVisible(true);
    }

    /**
     * @param profileName String
     */
    public void createNewBag(final String profileName) {
        log.info("Creating a new bag with version: {}, profile: {}", BagFactory.LATEST.versionString, profileName);

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
