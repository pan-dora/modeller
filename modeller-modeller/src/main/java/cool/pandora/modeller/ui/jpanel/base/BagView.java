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
package cool.pandora.modeller.ui.jpanel.base;

import cool.pandora.modeller.Bagger;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.domain.BaggerValidationRulesSource;
import cool.pandora.modeller.profile.BaggerProfileStore;
import cool.pandora.modeller.ui.BagTree;
import cool.pandora.modeller.ui.TagManifestPane;
import cool.pandora.modeller.ui.LongTask;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.BagTreePanel;

import cool.pandora.modeller.ui.handlers.base.AddDataHandler;
import cool.pandora.modeller.ui.handlers.base.AddDataExecutor;
import cool.pandora.modeller.ui.handlers.base.AddTagFileHandler;
import cool.pandora.modeller.ui.handlers.base.ClearBagHandler;
import cool.pandora.modeller.ui.handlers.base.ClearBagExecutor;
import cool.pandora.modeller.ui.handlers.base.CompleteBagHandler;
import cool.pandora.modeller.ui.handlers.base.CompleteExecutor;
import cool.pandora.modeller.ui.handlers.base.CreateBagInPlaceHandler;
import cool.pandora.modeller.ui.handlers.base.CreateBagInPlaceExecutor;
import cool.pandora.modeller.ui.handlers.base.StartNewBagHandler;
import cool.pandora.modeller.ui.handlers.base.SaveBagAsExecutor;
import cool.pandora.modeller.ui.handlers.base.SaveBagAsHandler;
import cool.pandora.modeller.ui.handlers.base.ShowTagFilesHandler;
import cool.pandora.modeller.ui.handlers.base.StartExecutor;
import cool.pandora.modeller.ui.handlers.base.OpenBagHandler;
import cool.pandora.modeller.ui.handlers.base.OpenExecutor;
import cool.pandora.modeller.ui.handlers.base.SaveBagHandler;
import cool.pandora.modeller.ui.handlers.base.SaveBagExecutor;
import cool.pandora.modeller.ui.handlers.base.ValidateBagHandler;
import cool.pandora.modeller.ui.handlers.base.ValidateExecutor;
import cool.pandora.modeller.ui.handlers.base.RemoveDataHandler;
import cool.pandora.modeller.ui.handlers.base.RemoveTagFileHandler;

import cool.pandora.modeller.ui.handlers.iiif.CreateDefaultContainersHandler;
import cool.pandora.modeller.ui.handlers.iiif.CreateDefaultContainersExecutor;
import cool.pandora.modeller.ui.handlers.iiif.UploadBagHandler;
import cool.pandora.modeller.ui.handlers.iiif.UploadBagExecutor;
import cool.pandora.modeller.ui.handlers.iiif.PatchResourceHandler;
import cool.pandora.modeller.ui.handlers.iiif.PatchResourceExecutor;
import cool.pandora.modeller.ui.handlers.iiif.PatchManifestHandler;
import cool.pandora.modeller.ui.handlers.iiif.PatchManifestExecutor;
import cool.pandora.modeller.ui.handlers.iiif.CreateListsHandler;
import cool.pandora.modeller.ui.handlers.iiif.CreateListsExecutor;
import cool.pandora.modeller.ui.handlers.iiif.CreateXmlFilesHandler;
import cool.pandora.modeller.ui.handlers.iiif.CreateXmlFilesExecutor;
import cool.pandora.modeller.ui.handlers.iiif.PatchListHandler;
import cool.pandora.modeller.ui.handlers.iiif.PatchListExecutor;
import cool.pandora.modeller.ui.handlers.iiif.CreateCanvasesHandler;
import cool.pandora.modeller.ui.handlers.iiif.CreateCanvasesExecutor;
import cool.pandora.modeller.ui.handlers.iiif.CreateSequencesHandler;
import cool.pandora.modeller.ui.handlers.iiif.CreateSequencesExecutor;
import cool.pandora.modeller.ui.handlers.iiif.PatchSequenceHandler;
import cool.pandora.modeller.ui.handlers.iiif.PatchSequenceExecutor;
import cool.pandora.modeller.ui.handlers.iiif.PatchCanvasHandler;
import cool.pandora.modeller.ui.handlers.iiif.PatchCanvasExecutor;
import cool.pandora.modeller.ui.handlers.iiif.PublishBagExecutor;
import cool.pandora.modeller.ui.handlers.text.PatchPagesHandler;
import cool.pandora.modeller.ui.handlers.text.PatchPagesExecutor;
import cool.pandora.modeller.ui.handlers.text.PatchAreasHandler;
import cool.pandora.modeller.ui.handlers.text.PatchAreasExecutor;
import cool.pandora.modeller.ui.handlers.text.PatchLinesHandler;
import cool.pandora.modeller.ui.handlers.text.PatchLinesExecutor;
import cool.pandora.modeller.ui.handlers.text.PatchWordsHandler;
import cool.pandora.modeller.ui.handlers.text.PatchWordsExecutor;
import cool.pandora.modeller.ui.handlers.text.CreatePagesHandler;
import cool.pandora.modeller.ui.handlers.text.CreatePagesExecutor;
import cool.pandora.modeller.ui.handlers.text.CreateAreasHandler;
import cool.pandora.modeller.ui.handlers.text.CreateAreasExecutor;
import cool.pandora.modeller.ui.handlers.text.CreateLinesHandler;
import cool.pandora.modeller.ui.handlers.text.CreateLinesExecutor;
import cool.pandora.modeller.ui.handlers.text.CreateWordsHandler;
import cool.pandora.modeller.ui.handlers.text.CreateWordsExecutor;
import cool.pandora.modeller.ui.util.LayoutUtil;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Cancellable;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServices;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.support.AbstractView;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.image.ImageSource;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.util.Assert;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.JTree;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;

/**
 * Bag View
 *
 * @author gov.loc
 */
public class BagView extends AbstractView implements ApplicationListener {
    protected static final Logger log = LoggerFactory.getLogger(BagView.class);

    private final static int ONE_SECOND = 1000;
    private final int DEFAULT_WIDTH = 1024;
    private final int DEFAULT_HEIGHT = 768;

    private ProgressMonitor progressMonitor;
    public LongTask task;
    public Cancellable longRunningProcess = null;
    private final Timer timer = new Timer(ONE_SECOND / 10, null);

    private Bagger bagger;
    private DefaultBag bag;
    public BaggerValidationRulesSource baggerRules;
    private BaggerProfileStore profileStore;
    public BagTree bagPayloadTree;
    public BagTree bagTagFileTree;

    private File bagRootPath;

    public TagManifestPane tagManifestPane;
    public InfoFormsPane infoInputPane;
    public BagTreePanel bagPayloadTreePanel;
    public BagTreePanel bagTagFileTreePanel;

    private JPanel bagButtonPanel;
    private JPanel topButtonPanel;

    public StartNewBagHandler startNewBagHandler;
    public StartExecutor startExecutor = new StartExecutor(this);
    public OpenBagHandler openBagHandler;
    public OpenExecutor openExecutor = new OpenExecutor(this);
    public CreateBagInPlaceHandler createBagInPlaceHandler;
    public CreateBagInPlaceExecutor createBagInPlaceExecutor = new CreateBagInPlaceExecutor(this);
    public SaveBagHandler saveBagHandler;
    public SaveBagExecutor saveBagExecutor = new SaveBagExecutor(this);
    public SaveBagAsHandler saveBagAsHandler;
    public SaveBagAsExecutor saveBagAsExecutor = new SaveBagAsExecutor(this);
    public ValidateBagHandler validateBagHandler;
    public ValidateExecutor validateExecutor = new ValidateExecutor(this);
    public CompleteBagHandler completeBagHandler;
    public CompleteExecutor completeExecutor = new CompleteExecutor(this);
    public ClearBagHandler clearBagHandler;
    public ClearBagExecutor clearExecutor = new ClearBagExecutor(this);
    public AddDataHandler addDataHandler;
    public AddDataExecutor addDataExecutor = new AddDataExecutor(this);
    private RemoveDataHandler removeDataHandler;
    private RemoveTagFileHandler removeTagFileHandler;
    private AddTagFileHandler addTagFileHandler;
    public CreateDefaultContainersHandler createDefaultContainersHandler;
    private final CreateDefaultContainersExecutor createDefaultContainersExecutor =
            new CreateDefaultContainersExecutor(this);
    public UploadBagHandler uploadBagHandler;
    private final UploadBagExecutor uploadBagExecutor = new UploadBagExecutor(this);
    public PatchResourceHandler patchResourceHandler;
    private final PatchResourceExecutor patchResourceExecutor = new PatchResourceExecutor(this);
    public CreateListsHandler createListsHandler;
    private final CreateListsExecutor createListsExecutor = new CreateListsExecutor(this);
    public CreateCanvasesHandler createCanvasesHandler;
    private final CreateCanvasesExecutor createCanvasesExecutor = new CreateCanvasesExecutor(this);
    public CreateSequencesHandler createSequencesHandler;
    private final CreateSequencesExecutor createSequencesExecutor = new CreateSequencesExecutor(this);
    public PatchSequenceHandler patchSequenceHandler;
    private final PatchSequenceExecutor patchSequenceExecutor = new PatchSequenceExecutor(this);
    public PatchCanvasHandler patchCanvasHandler;
    private final PatchCanvasExecutor patchCanvasExecutor = new PatchCanvasExecutor(this);
    public PatchManifestHandler patchManifestHandler;
    private final PatchManifestExecutor patchManifestExecutor = new PatchManifestExecutor(this);
    public CreateXmlFilesHandler createXmlFilesHandler;
    private final CreateXmlFilesExecutor createXmlFilesExecutor = new CreateXmlFilesExecutor(this);
    public PatchListHandler patchListHandler;
    private final PatchListExecutor patchListExecutor = new PatchListExecutor(this);
    public CreatePagesHandler createPagesHandler;
    private final CreatePagesExecutor createPagesExecutor = new CreatePagesExecutor(this);
    public CreateAreasHandler createAreasHandler;
    private final CreateAreasExecutor createAreasExecutor = new CreateAreasExecutor(this);
    public CreateLinesHandler createLinesHandler;
    private final CreateLinesExecutor createLinesExecutor = new CreateLinesExecutor(this);
    public CreateWordsHandler createWordsHandler;
    private final CreateWordsExecutor createWordsExecutor = new CreateWordsExecutor(this);
    public PatchPagesHandler patchPagesHandler;
    private final PatchPagesExecutor patchPagesExecutor = new PatchPagesExecutor(this);
    public PatchAreasHandler patchAreasHandler;
    private final PatchAreasExecutor patchAreasExecutor = new PatchAreasExecutor(this);
    public PatchLinesHandler patchLinesHandler;
    private final PatchLinesExecutor patchLinesExecutor = new PatchLinesExecutor(this);
    public PatchWordsHandler patchWordsHandler;
    private final PatchWordsExecutor patchWordsExecutor = new PatchWordsExecutor(this);
    private final PublishBagExecutor publishBagExecutor = new PublishBagExecutor(this);

    private JLabel addDataToolBarAction;
    private JLabel removeDataToolBarAction;
    private JLabel viewTagFilesToolbarAction;
    private JLabel addTagFileToolBarAction;
    private JLabel removeTagFileToolbarAction;

    /**
     *
     */
    public BagView() {
        ((ConfigurableApplicationContext) Application.instance().getApplicationContext()).getBeanFactory()
                .registerSingleton("myBagView", this);
    }

    /**
     * @param bagger Bagger
     */
    public void setBagger(final Bagger bagger) {
        Assert.notNull(bagger, "The cool.pandora.modeller property is required");
        this.bagger = bagger;
    }

    /**
     * @return this.bagger
     */
    public Bagger getBagger() {
        return this.bagger;
    }

    /**
     * @param baggerBag DefaultBag
     */
    public void setBag(final DefaultBag baggerBag) {
        this.bag = baggerBag;
    }

    /**
     * @return this.bag
     */
    public DefaultBag getBag() {
        return this.bag;
    }

    /**
     * @param f File
     */
    public void setBagRootPath(final File f) {
        this.bagRootPath = f;
    }

    /**
     * @return this.bagRootPath
     */
    public File getBagRootPath() {
        return this.bagRootPath;
    }

    /**
     * @return Dimension
     */
    public Dimension getMinimumSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * @return Dimension
     */
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private static void display(final String s) {
        log.info(s);
    }

    ImageIcon getPropertyImage(final String name) {
        final ImageSource source = this.getImageSource();
        final Image image = source.getImage(name);
        return new ImageIcon(image);
    }

    /**
     * @param bagTree BagTree
     */
    public void setBagPayloadTree(final BagTree bagTree) {
        this.bagPayloadTree = bagTree;
    }

    /**
     * @return this.bagPayloadTree
     */
    public BagTree getBagPayloadTree() {
        return this.bagPayloadTree;
    }

    /**
     * @param bagTree BagTree
     */
    public void setBagTagFileTree(final BagTree bagTree) {
        this.bagTagFileTree = bagTree;
    }

    /**
     * @return this.bagTagFileTree
     */
    public BagTree getBagTagFileTree() {
        return this.bagTagFileTree;
    }

    /**
     * This populates the default view descriptor declared as the startingPageId
     * property in the richclient-application-context.xml file.
     *
     * @return bagViewPanel
     */
    @Override
    protected JComponent createControl() {
        bag = new DefaultBag();

        final String userHomeDir = System.getProperty("user.home");
        display("createControl - User Home Path: " + userHomeDir);

        initializeCommands();

        final ApplicationServices services = this.getApplicationServices();
        final Object rulesSource = services.getService(org.springframework.rules.RulesSource.class);
        baggerRules = (BaggerValidationRulesSource) rulesSource;

        final Color bgColor = new Color(20, 20, 100);
        topButtonPanel = createTopButtonPanel();
        topButtonPanel.setBackground(bgColor);

        infoInputPane = new InfoFormsPane(this);
        infoInputPane.bagInfoInputPane.enableForms(false);
        final JSplitPane bagPanel = createBagPanel();

        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints glbc = LayoutUtil
                .buildGridBagConstraints(0, 0, 1, 1, 50, 100, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        layout.setConstraints(bagPanel, glbc);

        final JPanel mainPanel = new JPanel(layout);
        mainPanel.add(bagPanel);

        final JPanel bagViewPanel = new JPanel(new BorderLayout(2, 2));
        bagViewPanel.setBackground(bgColor);
        bagViewPanel.add(mainPanel, BorderLayout.CENTER);
        return bagViewPanel;
    }

    private JPanel createTopButtonPanel() {
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        startNewBagHandler = new StartNewBagHandler(this);
        openBagHandler = new OpenBagHandler(this);
        createBagInPlaceHandler = new CreateBagInPlaceHandler(this);
        saveBagHandler = new SaveBagHandler(this);
        saveBagAsHandler = new SaveBagAsHandler(this);
        completeBagHandler = new CompleteBagHandler(this);
        validateBagHandler = new ValidateBagHandler(this);
        clearBagHandler = new ClearBagHandler(this);
        createDefaultContainersHandler = new CreateDefaultContainersHandler(this);
        uploadBagHandler = new UploadBagHandler(this);
        patchResourceHandler = new PatchResourceHandler(this);
        createListsHandler = new CreateListsHandler(this);
        createCanvasesHandler = new CreateCanvasesHandler(this);
        createSequencesHandler = new CreateSequencesHandler(this);
        patchSequenceHandler = new PatchSequenceHandler(this);
        patchCanvasHandler = new PatchCanvasHandler(this);
        patchManifestHandler = new PatchManifestHandler(this);
        createXmlFilesHandler = new CreateXmlFilesHandler(this);
        patchListHandler = new PatchListHandler(this);
        createPagesHandler = new CreatePagesHandler(this);
        createAreasHandler = new CreateAreasHandler(this);
        createLinesHandler = new CreateLinesHandler(this);
        createWordsHandler = new CreateWordsHandler(this);
        patchPagesHandler = new PatchPagesHandler(this);
        patchAreasHandler = new PatchAreasHandler(this);
        patchLinesHandler = new PatchLinesHandler(this);
        patchWordsHandler = new PatchWordsHandler(this);
        return buttonPanel;
    }

    private JSplitPane createBagPanel() {

        final LineBorder border = new LineBorder(Color.GRAY, 1);

        bagButtonPanel = createBagButtonPanel();

        final JPanel bagTagButtonPanel = createBagTagButtonPanel();

        bagPayloadTree = new BagTree(this, AbstractBagConstants.DATA_DIRECTORY);
        bagPayloadTree.setEnabled(false);

        bagPayloadTreePanel = new BagTreePanel(bagPayloadTree);
        bagPayloadTreePanel.setEnabled(false);
        bagPayloadTreePanel.setBorder(border);
        bagPayloadTreePanel.setToolTipText(getMessage("bagTree.help"));

        bagTagFileTree = new BagTree(this, getMessage("bag.label.noname"));
        bagTagFileTree.setEnabled(false);
        bagTagFileTreePanel = new BagTreePanel(bagTagFileTree);
        bagTagFileTreePanel.setEnabled(false);
        bagTagFileTreePanel.setBorder(border);
        bagTagFileTreePanel.setToolTipText(getMessage("bagTree.help"));

        tagManifestPane = new TagManifestPane(this);
        tagManifestPane.setToolTipText(getMessage("compositePane.tab.help"));

        final JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.5);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        final JPanel payloadPannel = new JPanel();
        splitPane.setLeftComponent(payloadPannel);
        payloadPannel.setLayout(new BorderLayout(0, 0));

        final JPanel payLoadToolBarPanel = new JPanel();
        payloadPannel.add(payLoadToolBarPanel, BorderLayout.NORTH);
        payLoadToolBarPanel.setLayout(new GridLayout(1, 0, 0, 0));

        final JPanel payloadLabelPanel = new JPanel();
        final FlowLayout flowLayout = (FlowLayout) payloadLabelPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        payLoadToolBarPanel.add(payloadLabelPanel);

        final JLabel lblPayloadTree = new JLabel(getMessage("bagView.payloadTree.name"));
        payloadLabelPanel.add(lblPayloadTree);

        payLoadToolBarPanel.add(bagButtonPanel);

        payloadPannel.add(bagPayloadTreePanel, BorderLayout.CENTER);

        final JPanel tagFilePanel = new JPanel();
        splitPane.setRightComponent(tagFilePanel);
        tagFilePanel.setLayout(new BorderLayout(0, 0));

        final JPanel tagFileToolBarPannel = new JPanel();
        tagFilePanel.add(tagFileToolBarPannel, BorderLayout.NORTH);
        tagFileToolBarPannel.setLayout(new GridLayout(0, 2, 0, 0));

        final JPanel TagFileLabelPanel = new JPanel();
        final FlowLayout tagFileToolbarFlowLayout = (FlowLayout) TagFileLabelPanel.getLayout();
        tagFileToolbarFlowLayout.setAlignment(FlowLayout.LEFT);
        tagFileToolBarPannel.add(TagFileLabelPanel);

        final JLabel tagFileTreeLabel = new JLabel(getMessage("bagView.TagFilesTree.name"));
        TagFileLabelPanel.add(tagFileTreeLabel);

        tagFileToolBarPannel.add(bagTagButtonPanel);

        tagFilePanel.add(bagTagFileTreePanel, BorderLayout.CENTER);

        return splitPane;
    }

    private JPanel createBagButtonPanel() {

        addDataHandler = new AddDataHandler(this);
        removeDataHandler = new RemoveDataHandler(this);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));

        addDataToolBarAction = new JLabel("");
        addDataToolBarAction.setEnabled(false);
        addDataToolBarAction.setHorizontalAlignment(SwingConstants.CENTER);
        addDataToolBarAction.setBorder(new LineBorder(addDataToolBarAction.getBackground(), 1));
        addDataToolBarAction.setIcon(getPropertyImage("Bag_Content.add.icon"));
        addDataToolBarAction.setToolTipText(getMessage("bagView.payloadTree.addbutton.tooltip"));

        addDataToolBarAction.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (addDataToolBarAction.isEnabled()) {
                    addDataHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                addDataToolBarAction.setBorder(new LineBorder(addDataToolBarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                if (addDataToolBarAction.isEnabled()) {
                    addDataToolBarAction.setBorder(new LineBorder(Color.GRAY, 1));
                }
            }
        });
        buttonPanel.add(addDataToolBarAction);

        removeDataToolBarAction = new JLabel("");
        removeDataToolBarAction.setEnabled(false);
        removeDataToolBarAction.setHorizontalAlignment(SwingConstants.CENTER);
        removeDataToolBarAction.setBorder(new LineBorder(removeDataToolBarAction.getBackground(), 1));
        removeDataToolBarAction.setIcon(getPropertyImage("Bag_Content.minus.icon"));
        removeDataToolBarAction.setToolTipText(getMessage("bagView.payloadTree.remove.tooltip"));
        buttonPanel.add(removeDataToolBarAction);
        removeDataToolBarAction.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (removeDataToolBarAction.isEnabled()) {
                    removeDataHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                removeDataToolBarAction.setBorder(new LineBorder(removeDataToolBarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                if (removeDataToolBarAction.isEnabled()) {
                    removeDataToolBarAction.setBorder(new LineBorder(Color.GRAY, 1));
                }
            }
        });

        final JLabel spacerLabel = new JLabel("    ");
        buttonPanel.add(spacerLabel);

        addDataHandler = new AddDataHandler(this);
        removeDataHandler = new RemoveDataHandler(this);

        return buttonPanel;
    }

    private JPanel createBagTagButtonPanel() {

        final JPanel buttonPanel = new JPanel();

        final ShowTagFilesHandler showTageFileHandler = new ShowTagFilesHandler(this);
        addTagFileHandler = new AddTagFileHandler(this);
        removeTagFileHandler = new RemoveTagFileHandler(this);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));

        viewTagFilesToolbarAction = new JLabel("");
        viewTagFilesToolbarAction.setEnabled(false);
        viewTagFilesToolbarAction.setHorizontalAlignment(SwingConstants.CENTER);
        viewTagFilesToolbarAction.setBorder(new LineBorder(viewTagFilesToolbarAction.getBackground(), 1));
        viewTagFilesToolbarAction.setIcon(getPropertyImage("Bag_ViewTagFile.icon"));
        viewTagFilesToolbarAction.setToolTipText(getMessage("bagView.TagFilesTree.viewfile.tooltip"));

        viewTagFilesToolbarAction.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (viewTagFilesToolbarAction.isEnabled()) {
                    showTageFileHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                viewTagFilesToolbarAction.setBorder(new LineBorder(viewTagFilesToolbarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                if (viewTagFilesToolbarAction.isEnabled()) {
                    viewTagFilesToolbarAction.setBorder(new LineBorder(Color.GRAY, 1));
                }
            }
        });
        buttonPanel.add(viewTagFilesToolbarAction);

        addTagFileToolBarAction = new JLabel("");
        addTagFileToolBarAction.setEnabled(false);
        addTagFileToolBarAction.setHorizontalAlignment(SwingConstants.CENTER);
        addTagFileToolBarAction.setBorder(new LineBorder(addTagFileToolBarAction.getBackground(), 1));
        addTagFileToolBarAction.setIcon(getPropertyImage("Bag_Content.add.icon"));
        addTagFileToolBarAction.setToolTipText(getMessage("bagView.TagFilesTree.addbutton.tooltip"));

        addTagFileToolBarAction.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (addTagFileToolBarAction.isEnabled()) {
                    addTagFileHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                addTagFileToolBarAction.setBorder(new LineBorder(addTagFileToolBarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                if (addTagFileToolBarAction.isEnabled()) {
                    addTagFileToolBarAction.setBorder(new LineBorder(Color.GRAY, 1));
                }
            }
        });
        buttonPanel.add(addTagFileToolBarAction);

        removeTagFileToolbarAction = new JLabel("");
        removeTagFileToolbarAction.setEnabled(false);
        removeTagFileToolbarAction.setHorizontalAlignment(SwingConstants.CENTER);
        removeTagFileToolbarAction.setBorder(new LineBorder(removeTagFileToolbarAction.getBackground(), 1));
        removeTagFileToolbarAction.setIcon(getPropertyImage("Bag_Content.minus.icon"));
        removeTagFileToolbarAction.setToolTipText(getMessage("bagView.TagFilesTree.remove.tooltip"));

        buttonPanel.add(removeTagFileToolbarAction);
        removeTagFileToolbarAction.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (removeTagFileToolbarAction.isEnabled()) {
                    removeTagFileHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                removeTagFileToolbarAction.setBorder(new LineBorder(removeTagFileToolbarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                if (removeTagFileToolbarAction.isEnabled()) {
                    removeTagFileToolbarAction.setBorder(new LineBorder(Color.GRAY, 1));
                }
            }
        });

        final JLabel spacerLabel = new JLabel("    ");
        buttonPanel.add(spacerLabel);

        addTagFileHandler = new AddTagFileHandler(this);
        removeTagFileHandler = new RemoveTagFileHandler(this);

        return buttonPanel;
    }

    /**
     * @param b boolean
     */
    public void enableBagSettings(final boolean b) {
        bagPayloadTree.setEnabled(b);
        bagPayloadTreePanel.setEnabled(b);
        bagTagFileTree.setEnabled(b);
        bagTagFileTreePanel.setEnabled(b);
        infoInputPane.bagInfoInputPane.setEnabled(b);
    }

    /**
     *
     */
    public void updateBaggerRules() {
        baggerRules.init();
        bag.updateStrategy();

    }

    /**
     * @param title String
     * @param msg   String
     */
    public static void showWarningErrorDialog(final String title, final String msg) {
        final MessageDialog dialog = new MessageDialog(title, msg);
        dialog.showDialog();
    }

    private void initializeCommands() {
        startExecutor.setEnabled(true);
        openExecutor.setEnabled(true);
        createBagInPlaceExecutor.setEnabled(true);
        clearExecutor.setEnabled(false);
        validateExecutor.setEnabled(false);
        completeExecutor.setEnabled(false);
        addDataExecutor.setEnabled(false);
        saveBagExecutor.setEnabled(false);
        saveBagAsExecutor.setEnabled(false);
        createDefaultContainersExecutor.setEnabled(false);
        uploadBagExecutor.setEnabled(false);
        patchResourceExecutor.setEnabled(false);
        createListsExecutor.setEnabled(false);
        createCanvasesExecutor.setEnabled(false);
        createSequencesExecutor.setEnabled(false);
        patchSequenceExecutor.setEnabled(false);
        patchCanvasExecutor.setEnabled(false);
        patchManifestExecutor.setEnabled(false);
        createXmlFilesExecutor.setEnabled(false);
        patchListExecutor.setEnabled(false);
        createPagesExecutor.setEnabled(false);
        createAreasExecutor.setEnabled(false);
        createLinesExecutor.setEnabled(false);
        createWordsExecutor.setEnabled(false);
        patchPagesExecutor.setEnabled(false);
        patchAreasExecutor.setEnabled(false);
        patchLinesExecutor.setEnabled(false);
        patchWordsExecutor.setEnabled(false);
        publishBagExecutor.setEnabled(false);
    }

    /**
     *
     */
    public void updateClearBag() {
        enableBagSettings(false);

        infoInputPane.holeyValue.setText("");
        addDataToolBarAction.setEnabled(false);
        removeDataToolBarAction.setEnabled(false);
        addDataExecutor.setEnabled(false);
        saveBagExecutor.setEnabled(false);
        saveBagAsExecutor.setEnabled(false);
        viewTagFilesToolbarAction.setEnabled(false);
        addTagFileToolBarAction.setEnabled(false);
        removeTagFileToolbarAction.setEnabled(false);
        clearExecutor.setEnabled(false);
        validateExecutor.setEnabled(false);
        completeExecutor.setEnabled(false);
        bagButtonPanel.invalidate();
        topButtonPanel.invalidate();
        createDefaultContainersExecutor.setEnabled(false);
        uploadBagExecutor.setEnabled(false);
        createListsExecutor.setEnabled(false);
        createCanvasesExecutor.setEnabled(false);
        createSequencesExecutor.setEnabled(false);
        patchSequenceExecutor.setEnabled(false);
        patchCanvasExecutor.setEnabled(false);
        patchManifestExecutor.setEnabled(false);
        createXmlFilesExecutor.setEnabled(false);
        patchListExecutor.setEnabled(false);
        createPagesExecutor.setEnabled(false);
        createAreasExecutor.setEnabled(false);
        createLinesExecutor.setEnabled(false);
        createWordsExecutor.setEnabled(false);
        patchPagesExecutor.setEnabled(false);
        patchAreasExecutor.setEnabled(false);
        patchLinesExecutor.setEnabled(false);
        patchWordsExecutor.setEnabled(false);
        publishBagExecutor.setEnabled(false);
    }

    /**
     *
     */
    public void updateNewBag() {
        viewTagFilesToolbarAction.setEnabled(true);
        enableBagSettings(true);
        addDataToolBarAction.setEnabled(true);
        addDataExecutor.setEnabled(true);
        addTagFileToolBarAction.setEnabled(true);
        bagButtonPanel.invalidate();
        createDefaultContainersExecutor.setEnabled(false);
        uploadBagExecutor.setEnabled(false);
        createListsExecutor.setEnabled(false);
        createCanvasesExecutor.setEnabled(false);
        createSequencesExecutor.setEnabled(false);
        patchSequenceExecutor.setEnabled(false);
        patchCanvasExecutor.setEnabled(false);
        patchManifestExecutor.setEnabled(false);
        createXmlFilesExecutor.setEnabled(false);
        patchListExecutor.setEnabled(false);
        createPagesExecutor.setEnabled(false);
        createAreasExecutor.setEnabled(false);
        createLinesExecutor.setEnabled(false);
        createWordsExecutor.setEnabled(false);
        patchPagesExecutor.setEnabled(false);
        patchAreasExecutor.setEnabled(false);
        patchLinesExecutor.setEnabled(false);
        patchWordsExecutor.setEnabled(false);
        publishBagExecutor.setEnabled(false);
    }

    /**
     *
     */
    public void updateOpenBag() {
        addDataToolBarAction.setEnabled(true);
        addDataExecutor.setEnabled(true);
        saveBagExecutor.setEnabled(true);
        addTagFileToolBarAction.setEnabled(true);
        viewTagFilesToolbarAction.setEnabled(true);
        saveBagAsExecutor.setEnabled(true);
        bagButtonPanel.invalidate();
        clearExecutor.setEnabled(true);
        setCompleteExecutor(); // Disables the Is Complete Bag Button for Holey Bags
        setValidateExecutor(); // Disables the Validate Bag Button for Holey Bags
        topButtonPanel.invalidate();
        createDefaultContainersExecutor.setEnabled(true);
        uploadBagExecutor.setEnabled(true);
        patchResourceExecutor.setEnabled(true);
        createListsExecutor.setEnabled(true);
        createCanvasesExecutor.setEnabled(true);
        createSequencesExecutor.setEnabled(true);
        patchSequenceExecutor.setEnabled(true);
        patchCanvasExecutor.setEnabled(true);
        patchManifestExecutor.setEnabled(true);
        createXmlFilesExecutor.setEnabled(true);
        patchListExecutor.setEnabled(true);
        createPagesExecutor.setEnabled(true);
        createAreasExecutor.setEnabled(true);
        createLinesExecutor.setEnabled(true);
        createWordsExecutor.setEnabled(true);
        patchPagesExecutor.setEnabled(true);
        patchAreasExecutor.setEnabled(true);
        patchLinesExecutor.setEnabled(true);
        patchWordsExecutor.setEnabled(true);
        publishBagExecutor.setEnabled(true);
    }

    /**
     *
     */
    public void updateBagInPlace() {
        addDataToolBarAction.setEnabled(true);
        addDataExecutor.setEnabled(true);
        saveBagExecutor.setEnabled(false);
        saveBagAsExecutor.setEnabled(true);
        addTagFileToolBarAction.setEnabled(true);
        viewTagFilesToolbarAction.setEnabled(true);
        bagButtonPanel.invalidate();
        completeExecutor.setEnabled(true);
        validateExecutor.setEnabled(true);
        bagButtonPanel.invalidate();
        topButtonPanel.invalidate();
        createDefaultContainersExecutor.setEnabled(true);
        uploadBagExecutor.setEnabled(true);
        patchResourceExecutor.setEnabled(true);
        createListsExecutor.setEnabled(true);
        createCanvasesExecutor.setEnabled(true);
        createSequencesExecutor.setEnabled(true);
        patchSequenceExecutor.setEnabled(true);
        patchCanvasExecutor.setEnabled(true);
        patchManifestExecutor.setEnabled(true);
        createXmlFilesExecutor.setEnabled(true);
        patchListExecutor.setEnabled(true);
        createPagesExecutor.setEnabled(true);
        createAreasExecutor.setEnabled(true);
        createLinesExecutor.setEnabled(true);
        createWordsExecutor.setEnabled(true);
        patchPagesExecutor.setEnabled(true);
        patchAreasExecutor.setEnabled(true);
        patchLinesExecutor.setEnabled(true);
        patchWordsExecutor.setEnabled(true);
        publishBagExecutor.setEnabled(true);
    }

    /**
     *
     */
    public void updateSaveBag() {
        addDataToolBarAction.setEnabled(true);
        addDataExecutor.setEnabled(true);
        saveBagExecutor.setEnabled(true);
        addTagFileToolBarAction.setEnabled(true);
        viewTagFilesToolbarAction.setEnabled(true);
        createListsExecutor.setEnabled(true);
        bagButtonPanel.invalidate();
        clearExecutor.setEnabled(true);
        setCompleteExecutor(); // Disables the Is Complete Bag Button for Holey Bags
        setValidateExecutor(); // Disables the Validate Bag Button for Holey Bags
        topButtonPanel.invalidate();
        saveBagAsExecutor.setEnabled(true);
        createDefaultContainersExecutor.setEnabled(true);
        uploadBagExecutor.setEnabled(true);
        patchResourceExecutor.setEnabled(true);
        createListsExecutor.setEnabled(true);
        createCanvasesExecutor.setEnabled(true);
        createSequencesExecutor.setEnabled(true);
        patchSequenceExecutor.setEnabled(true);
        patchCanvasExecutor.setEnabled(true);
        patchManifestExecutor.setEnabled(true);
        createXmlFilesExecutor.setEnabled(true);
        patchListExecutor.setEnabled(true);
        createPagesExecutor.setEnabled(true);
        createAreasExecutor.setEnabled(true);
        createLinesExecutor.setEnabled(true);
        createWordsExecutor.setEnabled(true);
        patchPagesExecutor.setEnabled(true);
        patchAreasExecutor.setEnabled(true);
        patchLinesExecutor.setEnabled(true);
        patchWordsExecutor.setEnabled(true);
        publishBagExecutor.setEnabled(true);
    }

    /**
     *
     */
    public void updateAddData() {
        saveBagAsExecutor.setEnabled(true);
        createListsExecutor.setEnabled(true);
        bagButtonPanel.invalidate();
        topButtonPanel.invalidate();
        createDefaultContainersExecutor.setEnabled(true);
        uploadBagExecutor.setEnabled(true);
        patchResourceExecutor.setEnabled(true);
        createListsExecutor.setEnabled(true);
        createCanvasesExecutor.setEnabled(true);
        createSequencesExecutor.setEnabled(true);
        patchSequenceExecutor.setEnabled(true);
        patchCanvasExecutor.setEnabled(true);
        patchManifestExecutor.setEnabled(true);
        createXmlFilesExecutor.setEnabled(true);
        patchListExecutor.setEnabled(true);
        createPagesExecutor.setEnabled(true);
        createAreasExecutor.setEnabled(true);
        createLinesExecutor.setEnabled(true);
        createWordsExecutor.setEnabled(true);
        patchPagesExecutor.setEnabled(true);
        patchAreasExecutor.setEnabled(true);
        patchLinesExecutor.setEnabled(true);
        patchWordsExecutor.setEnabled(true);
        publishBagExecutor.setEnabled(true);
    }

    /**
     *
     */
    public void updateManifestPane() {
        bagTagFileTree = new BagTree(this, bag.getName());
        final Collection<BagFile> tags = bag.getTags();
        for (final BagFile bf : tags) {
            bagTagFileTree.addNode(bf.getFilepath());
        }
        bagTagFileTreePanel.refresh(bagTagFileTree);
    }

    /**
     * @param context PageComponentContext
     */
    @Override
    protected void registerLocalCommandExecutors(final PageComponentContext context) {
        context.register("startCommand", startExecutor);
        context.register("openCommand", openExecutor);
        context.register("createBagInPlaceCommand", createBagInPlaceExecutor);
        context.register("clearCommand", clearExecutor);
        context.register("validateCommand", validateExecutor);
        context.register("completeCommand", completeExecutor);
        context.register("addDataCommand", addDataExecutor);
        context.register("saveBagCommand", saveBagExecutor);
        context.register("saveBagAsCommand", saveBagAsExecutor);
        context.register("createDefaultContainersCommand", createDefaultContainersExecutor);
        context.register("uploadBagCommand", uploadBagExecutor);
        context.register("patchResourceCommand", patchResourceExecutor);
        context.register("createListsCommand", createListsExecutor);
        context.register("createCanvasesCommand", createCanvasesExecutor);
        context.register("createSequencesCommand", createSequencesExecutor);
        context.register("patchSequenceCommand", patchSequenceExecutor);
        context.register("patchCanvasCommand", patchCanvasExecutor);
        context.register("patchManifestCommand", patchManifestExecutor);
        context.register("createXmlFilesCommand", createXmlFilesExecutor);
        context.register("patchListCommand", patchListExecutor);
        context.register("createPagesCommand", createPagesExecutor);
        context.register("createAreasCommand", createAreasExecutor);
        context.register("createLinesCommand", createLinesExecutor);
        context.register("createWordsCommand", createWordsExecutor);
        context.register("patchPagesCommand", patchPagesExecutor);
        context.register("patchAreasCommand", patchAreasExecutor);
        context.register("patchLinesCommand", patchLinesExecutor);
        context.register("patchWordsCommand", patchWordsExecutor);
        context.register("publishBagCommand", publishBagExecutor);
    }

    /**
     * @param e ApplicationEvent
     */
    @Override
    public void onApplicationEvent(final ApplicationEvent e) {
        log.info("BagView.onApplicationEvent: {}", e);
    }

    /**
     * The actionPerformed method in this class
     * is called each time the Timer "goes off".
     */
    class TimerListener implements ActionListener {
        /**
         * @param evt ActionEvent
         */
        @Override
        public void actionPerformed(final ActionEvent evt) {
            // check if task is completed or user has clicked cancel button
            if (task.hasUserTriedToCancel() || task.isDone()) {
                // we are done
                progressMonitor.close();
                Toolkit.getDefaultToolkit().beep();
                timer.stop();
                log.info("Stopped the timer");
                // getting an array of Action Listeners from Timer Listener (will have
                // only one element)
                final ActionListener[] als = timer.getListeners(ActionListener.class);
                // Removing Action Listener from timer
                if (als.length > 0) {
                    timer.removeActionListener(als[0]);
                }
                if (longRunningProcess != null && !task.isDone()) {
                    log.info("Trying to cancel the long running process: {}", longRunningProcess);
                    longRunningProcess.cancel();
                }
            }
        }
    }

    /**
     * @param progress          Progress
     * @param message           String
     * @param activityMonitored String
     */
    public void statusBarBegin(final Progress progress, final String message, final String activityMonitored) {
        BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());
        task = new LongTask();
        task.setActivityMonitored(activityMonitored);
        task.setProgress(progress);

        timer.addActionListener(new TimerListener());

        progressMonitor = new ProgressMonitor(this.getControl(), message, "Preparing the operation...", 0, 1);
        progressMonitor.setMillisToDecideToPopup(ONE_SECOND);
        task.setMonitor(progressMonitor);

        task.go();
        timer.start();
    }

    /**
     *
     */
    public static void statusBarEnd() {
        BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
    }

    /**
     * @param label String
     * @param tree  JTree
     */
    public void registerTreeListener(final String label, final JTree tree) {
        if (AbstractBagConstants.DATA_DIRECTORY.equals(label)) {
            tree.addTreeSelectionListener(e -> {

                final TreePath[] paths = tree.getSelectionPaths();
                if (paths == null || paths.length == 0) {
                    return;
                }

                for (final TreePath path : paths) {
                    if (path.getPathCount() == 1) {
                        removeDataToolBarAction.setEnabled(false);
                        return;
                    }
                }

                removeDataToolBarAction.setEnabled(true);
            });
        } else {
            tree.addTreeSelectionListener(e -> {

                final TreePath[] paths = tree.getSelectionPaths();
                if (paths == null || paths.length == 0) {
                    return;
                }

                for (final TreePath path : paths) {
                    if (path.getPathCount() == 1) {
                        removeTagFileToolbarAction.setEnabled(false);
                        return;
                    }
                }

                removeTagFileToolbarAction.setEnabled(true);
            });
        }
    }

    /**
     * @return profileStore
     */
    public BaggerProfileStore getProfileStore() {
        return profileStore;
    }

    /**
     * @param profileStore BaggerProfileStore
     */
    public void setProfileStore(final BaggerProfileStore profileStore) {
        this.profileStore = profileStore;
    }

    /**
     * @param propertyName String
     * @return message
     */
    public String getPropertyMessage(final String propertyName) {
        return getMessage(propertyName);
    }

    /*
     * Returns true if the Fetch.txt file exists.
     * This would be true in the case of a Holey Bag
     * Returns false for all other types of Bags
     */
    private boolean checkFetchTxtFile() {
        return bag.getFetchTxt() != null;
    }

    /*
     * Disables the Is Complete Bag Button if Fetch.txt file exists.
     * This is true in the case of a Holey Bag
     * The Validate Button is enabled for all other types of Bags
     */
    private void setCompleteExecutor() {
        if (checkFetchTxtFile()) {
            completeExecutor.setEnabled(false);
        }
        completeExecutor.setEnabled(true);
    }

    /*
     * Disables the Validate Bag Button if Fetch.txt file exists.
     * This is true in the case of a Holey Bag
     * The Validate Button is enabled for all other types of Bags
     */
    private void setValidateExecutor() {
        if (checkFetchTxtFile()) {
            validateExecutor.setEnabled(false);
        }
        validateExecutor.setEnabled(true);
    }

}
