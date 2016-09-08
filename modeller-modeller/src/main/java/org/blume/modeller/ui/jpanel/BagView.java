package org.blume.modeller.ui.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ProgressMonitor;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.blume.modeller.ui.*;
import org.blume.modeller.ui.handlers.iiif.*;
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

import org.blume.modeller.Bagger;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.domain.BaggerValidationRulesSource;
import org.blume.modeller.profile.BaggerProfileStore;
import org.blume.modeller.ui.handlers.base.AddDataExecutor;
import org.blume.modeller.ui.handlers.base.AddDataHandler;
import org.blume.modeller.ui.handlers.base.AddTagFileHandler;
import org.blume.modeller.ui.handlers.base.ClearBagExecutor;
import org.blume.modeller.ui.handlers.base.ClearBagHandler;
import org.blume.modeller.ui.handlers.base.CompleteBagHandler;
import org.blume.modeller.ui.handlers.base.CompleteExecutor;
import org.blume.modeller.ui.handlers.base.CreateBagInPlaceExecutor;
import org.blume.modeller.ui.handlers.base.CreateBagInPlaceHandler;
import org.blume.modeller.ui.handlers.base.OpenBagHandler;
import org.blume.modeller.ui.handlers.base.OpenExecutor;
import org.blume.modeller.ui.handlers.base.RemoveDataHandler;
import org.blume.modeller.ui.handlers.base.RemoveTagFileHandler;
import org.blume.modeller.ui.handlers.base.SaveBagAsExecutor;
import org.blume.modeller.ui.handlers.base.SaveBagAsHandler;
import org.blume.modeller.ui.handlers.base.SaveBagExecutor;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.handlers.base.ShowTagFilesHandler;
import org.blume.modeller.ui.handlers.base.StartExecutor;
import org.blume.modeller.ui.handlers.base.StartNewBagHandler;
import org.blume.modeller.ui.handlers.base.ValidateBagHandler;
import org.blume.modeller.ui.handlers.base.ValidateExecutor;
import org.blume.modeller.ui.util.LayoutUtil;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Cancellable;
import gov.loc.repository.bagit.impl.AbstractBagConstants;

public class BagView extends AbstractView implements ApplicationListener {
    protected static final Logger log = LoggerFactory.getLogger(BagView.class);

    private final static int ONE_SECOND = 1000;
    private int DEFAULT_WIDTH = 1024;
    private int DEFAULT_HEIGHT = 768;

    public ProgressMonitor progressMonitor;
    public LongTask task;
    public Cancellable longRunningProcess = null;
    private final Timer timer = new Timer(ONE_SECOND / 10, null);

    private Bagger bagger;
    private DefaultBag bag;
    public BaggerValidationRulesSource baggerRules;
    public BaggerProfileStore profileStore;
    public BagTree bagPayloadTree;
    public BagTree bagTagFileTree;

    private File bagRootPath;
    private String userHomeDir;

    public TagManifestPane tagManifestPane;
    public InfoFormsPane infoInputPane;
    public BagTreePanel bagPayloadTreePanel;
    public BagTreePanel bagTagFileTreePanel;

    private JPanel bagButtonPanel;
    private JPanel bagTagButtonPanel;
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
    public RemoveDataHandler removeDataHandler;
    public RemoveTagFileHandler removeTagFileHandler;
    public AddTagFileHandler addTagFileHandler;
    public CreateDefaultContainersHandler createDefaultContainersHandler;
    public CreateDefaultContainersExecutor createDefaultContainersExecutor = new CreateDefaultContainersExecutor(this);
    public UploadBagHandler uploadBagHandler;
    public UploadBagExecutor uploadBagExecutor = new UploadBagExecutor(this);
    public PatchResourceHandler patchResourceHandler;
    public PatchResourceExecutor patchResourceExecutor = new PatchResourceExecutor(this);
    public CreateListsHandler createListsHandler;
    public CreateListsExecutor createListsExecutor = new CreateListsExecutor(this);
    public CreateCanvasesHandler createCanvasesHandler;
    public CreateCanvasesExecutor createCanvasesExecutor = new CreateCanvasesExecutor(this);
    public CreateSequencesHandler createSequencesHandler;
    public CreateSequencesExecutor createSequencesExecutor = new CreateSequencesExecutor(this);
    public PatchSequenceHandler patchSequenceHandler;
    public PatchSequenceExecutor patchSequenceExecutor = new PatchSequenceExecutor(this);

    private JLabel addDataToolBarAction;
    private JLabel removeDataToolBarAction;
    private JLabel viewTagFilesToolbarAction;
    private JLabel addTagFileToolBarAction;
    private JLabel removeTagFileToolbarAction;

    public BagView() {
        ((ConfigurableApplicationContext) Application.instance().getApplicationContext()).getBeanFactory().registerSingleton("myBagView", this);
    }

    public void setBagger(Bagger bagger) {
        Assert.notNull(bagger, "The org.blume.modeller property is required");
        this.bagger = bagger;
    }

    public Bagger getBagger() {
        return this.bagger;
    }

    public void setBag(DefaultBag baggerBag) {
        this.bag = baggerBag;
    }

    public DefaultBag getBag() {
        return this.bag;
    }

    public void setBagRootPath(File f) {
        this.bagRootPath = f;
    }

    public File getBagRootPath() {
        return this.bagRootPath;
    }

    public Dimension getMinimumSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void display(String s) {
        log.info(s);
    }

    public ImageIcon getPropertyImage(String name) {
        ImageSource source = this.getImageSource();
        Image image = source.getImage(name);
        return new ImageIcon(image);
    }

    public void setBagPayloadTree(BagTree bagTree) {
        this.bagPayloadTree = bagTree;
    }

    public BagTree getBagPayloadTree() {
        return this.bagPayloadTree;
    }

    public void setBagTagFileTree(BagTree bagTree) {
        this.bagTagFileTree = bagTree;
    }

    public BagTree getBagTagFileTree() {
        return this.bagTagFileTree;
    }

    @Override
    // This populates the default view descriptor declared as the startingPageId
    // property in the richclient-application-context.xml file.
    protected JComponent createControl() {
        bag = new DefaultBag();

        this.userHomeDir = System.getProperty("user.home");
        display("createControl - User Home Path: " + userHomeDir);

        initializeCommands();

        ApplicationServices services = this.getApplicationServices();
        Object rulesSource = services.getService(org.springframework.rules.RulesSource.class);
        baggerRules = (BaggerValidationRulesSource) rulesSource;

        Color bgColor = new Color(20, 20, 100);
        topButtonPanel = createTopButtonPanel();
        topButtonPanel.setBackground(bgColor);

        infoInputPane = new InfoFormsPane(this);
        infoInputPane.bagInfoInputPane.enableForms(false);
        JSplitPane bagPanel = createBagPanel();

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints glbc = LayoutUtil.buildGridBagConstraints(0, 0, 1, 1, 50, 100, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

        layout.setConstraints(bagPanel, glbc);

        JPanel mainPanel = new JPanel(layout);
        mainPanel.add(bagPanel);

        JPanel bagViewPanel = new JPanel(new BorderLayout(2, 2));
        bagViewPanel.setBackground(bgColor);
        bagViewPanel.add(mainPanel, BorderLayout.CENTER);
        return bagViewPanel;
    }

    private JPanel createTopButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
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
        return buttonPanel;
    }

    private JSplitPane createBagPanel() {

        LineBorder border = new LineBorder(Color.GRAY, 1);

        bagButtonPanel = createBagButtonPanel();

        bagTagButtonPanel = createBagTagButtonPanel();

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

        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.5);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        JPanel payloadPannel = new JPanel();
        splitPane.setLeftComponent(payloadPannel);
        payloadPannel.setLayout(new BorderLayout(0, 0));

        JPanel payLoadToolBarPanel = new JPanel();
        payloadPannel.add(payLoadToolBarPanel, BorderLayout.NORTH);
        payLoadToolBarPanel.setLayout(new GridLayout(1, 0, 0, 0));

        JPanel payloadLabelPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) payloadLabelPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        payLoadToolBarPanel.add(payloadLabelPanel);

        JLabel lblPayloadTree = new JLabel(getMessage("bagView.payloadTree.name"));
        payloadLabelPanel.add(lblPayloadTree);

        payLoadToolBarPanel.add(bagButtonPanel);

        payloadPannel.add(bagPayloadTreePanel, BorderLayout.CENTER);

        JPanel tagFilePanel = new JPanel();
        splitPane.setRightComponent(tagFilePanel);
        tagFilePanel.setLayout(new BorderLayout(0, 0));

        JPanel tagFileToolBarPannel = new JPanel();
        tagFilePanel.add(tagFileToolBarPannel, BorderLayout.NORTH);
        tagFileToolBarPannel.setLayout(new GridLayout(0, 2, 0, 0));

        JPanel TagFileLabelPanel = new JPanel();
        FlowLayout tagFileToolbarFlowLayout = (FlowLayout) TagFileLabelPanel.getLayout();
        tagFileToolbarFlowLayout.setAlignment(FlowLayout.LEFT);
        tagFileToolBarPannel.add(TagFileLabelPanel);

        JLabel tagFileTreeLabel = new JLabel(getMessage("bagView.TagFilesTree.name"));
        TagFileLabelPanel.add(tagFileTreeLabel);

        tagFileToolBarPannel.add(bagTagButtonPanel);

        tagFilePanel.add(bagTagFileTreePanel, BorderLayout.CENTER);

        return splitPane;
    }

    private JPanel createBagButtonPanel() {

        addDataHandler = new AddDataHandler(this);
        removeDataHandler = new RemoveDataHandler(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));

        addDataToolBarAction = new JLabel("");
        addDataToolBarAction.setEnabled(false);
        addDataToolBarAction.setHorizontalAlignment(SwingConstants.CENTER);
        addDataToolBarAction.setBorder(new LineBorder(addDataToolBarAction.getBackground(), 1));
        addDataToolBarAction.setIcon(getPropertyImage("Bag_Content.add.icon"));
        addDataToolBarAction.setToolTipText(getMessage("bagView.payloadTree.addbutton.tooltip"));

        addDataToolBarAction.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (addDataToolBarAction.isEnabled()) {
                    addDataHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addDataToolBarAction.setBorder(new LineBorder(addDataToolBarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
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
            public void mousePressed(MouseEvent e) {
                if (removeDataToolBarAction.isEnabled()) {
                    removeDataHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                removeDataToolBarAction.setBorder(new LineBorder(removeDataToolBarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
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

        JPanel buttonPanel = new JPanel();

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
            public void mousePressed(MouseEvent e) {
                if (viewTagFilesToolbarAction.isEnabled()) {
                    showTageFileHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                viewTagFilesToolbarAction.setBorder(new LineBorder(viewTagFilesToolbarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
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
            public void mousePressed(MouseEvent e) {
                if (addTagFileToolBarAction.isEnabled()) {
                    addTagFileHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addTagFileToolBarAction.setBorder(new LineBorder(addTagFileToolBarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
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
            public void mousePressed(MouseEvent e) {
                if (removeTagFileToolbarAction.isEnabled()) {
                    removeTagFileHandler.actionPerformed(null);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                removeTagFileToolbarAction.setBorder(new LineBorder(removeTagFileToolbarAction.getBackground(), 1));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
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

    public void enableBagSettings(boolean b) {
        bagPayloadTree.setEnabled(b);
        bagPayloadTreePanel.setEnabled(b);
        bagTagFileTree.setEnabled(b);
        bagTagFileTreePanel.setEnabled(b);
        infoInputPane.bagInfoInputPane.setEnabled(b);
    }

    public String updateBaggerRules() {
        baggerRules.init();
        String messages = "";
        bag.updateStrategy();

        return messages;
    }

    public void showWarningErrorDialog(String title, String msg) {
        MessageDialog dialog = new MessageDialog(title, msg);
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
    }

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
    }

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
    }

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
    }

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
    }

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
    }

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
    }

    public void updateManifestPane() {
        bagTagFileTree = new BagTree(this, bag.getName());
        Collection<BagFile> tags = bag.getTags();
        for (BagFile bf : tags) {
            bagTagFileTree.addNode(bf.getFilepath());
        }
        bagTagFileTreePanel.refresh(bagTagFileTree);
    }

    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context) {
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
    }

    @Override
    public void onApplicationEvent(ApplicationEvent e) {
        log.info("BagView.onApplicationEvent: {}", e);
    }

    /**
     * The actionPerformed method in this class
     * is called each time the Timer "goes off".
     */
    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            // check if task is completed or user has clicked cancel button
            if (task.hasUserTriedToCancel() || task.isDone()) {
                // we are done
                progressMonitor.close();
                Toolkit.getDefaultToolkit().beep();
                timer.stop();
                log.info("Stopped the timer");
                // getting an array of Action Listeners from Timer Listener (will have
                // only one element)
                ActionListener[] als = timer.getListeners(ActionListener.class);
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

    public void statusBarBegin(Progress progress, String message, String activityMonitored) {
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

    public void statusBarEnd() {
        BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
    }

    public void registerTreeListener(String label, final JTree tree) {
        if (AbstractBagConstants.DATA_DIRECTORY.equals(label)) {
            tree.addTreeSelectionListener(e -> {

                TreePath[] paths = tree.getSelectionPaths();
                if (paths == null || paths.length == 0) {
                    return;
                }

                for (TreePath path : paths) {
                    if (path.getPathCount() == 1) {
                        removeDataToolBarAction.setEnabled(false);
                        return;
                    }
                }

                removeDataToolBarAction.setEnabled(true);
            });
        } else {
            tree.addTreeSelectionListener(e -> {

                TreePath[] paths = tree.getSelectionPaths();
                if (paths == null || paths.length == 0) {
                    return;
                }

                for (TreePath path : paths) {
                    if (path.getPathCount() == 1) {
                        removeTagFileToolbarAction.setEnabled(false);
                        return;
                    }
                }

                removeTagFileToolbarAction.setEnabled(true);
            });
        }
    }

    public BaggerProfileStore getProfileStore() {
        return profileStore;
    }

    public void setProfileStore(BaggerProfileStore profileStore) {
        this.profileStore = profileStore;
    }

    public String getPropertyMessage(String propertyName) {
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
