package cool.pandora.modeller.ui;

import cool.pandora.modeller.bag.BaggerFileEntity;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.handlers.base.BagTreeTransferHandler;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.DropMode;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Bag Tree
 *
 * @author gov.loc
 */
public class BagTree extends JTree {
    private static final long serialVersionUID = -5361474872106399068L;
    protected static final Logger log = LoggerFactory.getLogger(BagTree.class);
    private final int BAGTREE_WIDTH = 400;
    private int BAGTREE_HEIGHT = 160;
    private int BAGTREE_ROW_MODIFIER = 22;

    private String basePath;
    private DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(AbstractBagConstants.DATA_DIRECTORY);
    private final ArrayList<DefaultMutableTreeNode> srcNodes = new ArrayList<>();

    /**
     * @param bagView BagView
     * @param path    String
     */
    public BagTree(final BagView bagView, final String path) {
        super();
        this.setShowsRootHandles(true);
        basePath = path;
        parentNode = new DefaultMutableTreeNode(basePath);
        initialize();
        initListeners();
        final JTextField nameTextField = new JTextField();
        BAGTREE_ROW_MODIFIER = nameTextField.getFontMetrics(nameTextField.getFont()).getHeight() + 5;
        this.setDragEnabled(true);
        this.setDropMode(DropMode.ON_OR_INSERT);
        this.setTransferHandler(new BagTreeTransferHandler());
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        bagView.registerTreeListener(path, this);
    }

    /**
     *
     */
    private void initialize() {
        setModel(new DefaultTreeModel(parentNode));
        final TreePath rootPath = new TreePath(parentNode.getPath());

        // setCheckingPath(rootPath);
        setAnchorSelectionPath(rootPath);
        makeVisible(rootPath);
        // getCheckingModel().setCheckingMode(TreeCheckingModel.CheckingMode.PROPAGATE);
        setLargeModel(true);
        requestFocus();
        setScrollsOnExpand(true);
    }

    /**
     * @param bag     DefaultBag
     * @param path    String
     * @param rootSrc File
     */
    public void populateNodes(final DefaultBag bag, final String path, final File rootSrc) {
        basePath = path;

        log.debug("BagTree.populateNodes");
        if (bag.getPayload() != null && rootSrc.listFiles() != null) {
            addNodes(rootSrc, true);
        } else {
            log.debug("BagTree.populateNodes listFiles NULL:");
            final List<String> payload;
            if (!bag.isHoley()) {
                log.debug("BagTree.populateNodes getPayloadPaths:");
                payload = bag.getPayloadPaths();
            } else {
                log.debug("BagTree.populateNodes getFetchPayload:");
                payload = bag.getPayloadPaths(); // bag.getFetchPayload();
                // basePath = bag.getFetch().getBaseURL();
            }
            for (final String filePath : payload) {
                try {
                    final String normalPath;
                    if (bag.isHoley()) {
                        normalPath = BaggerFileEntity.removeBasePath("data", filePath);
                    } else {
                        normalPath = BaggerFileEntity.removeBasePath(basePath, filePath);
                    }
                    if (!nodeAlreadyExists(normalPath)) {
                        this.addNode(normalPath);
                    }
                } catch (final Exception e) {
                    if (!nodeAlreadyExists(filePath)) {
                        this.addNode(filePath);
                    }
                    log.error("Failed to remove base path from {}", filePath, e);
                }
            }
            log.debug("BagTree rows: {}", payload.size());
            BAGTREE_HEIGHT = BAGTREE_ROW_MODIFIER * (payload.size() + 1);
            setPreferredSize(getTreeSize());
            invalidate();
        }
    }

    /**
     * @param file     File
     * @param isParent boolean
     * @return boolean
     */
    public boolean addNodes(final File file, final boolean isParent) {
        if (!nodeAlreadyExists(file.getName())) {
            final DefaultMutableTreeNode rootNode = createNodeTree(null, null, file);
            srcNodes.add(rootNode);
            if (isParent) {
                parentNode = rootNode;
            } else {
                parentNode.add(rootNode);
            }
            initialize();
        } else {
            return true;
        }
        return false;
    }

    /**
     * @param path String
     * @return isNodeChild
     */
    private boolean nodeAlreadyExists(final String path) {
        final DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(path);
        final String node = aNode.toString();
        boolean isNodeChild = parentNode.isNodeChild(aNode);
        if (isNodeChild) {
            return true;
        }
        for (int i = 0; i < parentNode.getChildCount(); i++) {
            final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            final String child = childNode.toString();
            if (child.equalsIgnoreCase(node)) {
                isNodeChild = true;
                break;
            }
        }
        return isNodeChild;
    }

    /**
     * @param filePath String
     */
    public void addNode(final String filePath) {
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(filePath);
        srcNodes.add(node);
        parentNode.add(node);
        initialize();
    }

    /**
     * Add nodes from under "dir" into curTop. Highly recursive.
     */
    private static DefaultMutableTreeNode createNodeTree(final DefaultMutableTreeNode curTop,
                                                         final DefaultMutableTreeNode displayTop, final File dir) {
        final String curPath = dir.getPath();
        final String displayPath = dir.getName();
        final DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
        final DefaultMutableTreeNode displayDir = new DefaultMutableTreeNode(displayPath);
        if (curTop != null) { // should only be null at root
            curTop.add(curDir);
            displayTop.add(displayDir);
        }
        final Vector<String> ol = new Vector<>();
        // display("addNodes: " + dir.list());
        final String[] tmp = dir.list();
        if (tmp != null && tmp.length > 0) {
            for (final String aTmp : tmp) {
                ol.addElement(aTmp);
            }
        }

        ol.sort(String.CASE_INSENSITIVE_ORDER);
        File f;
        final Vector<String> files = new Vector<>();
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (int i = 0; i < ol.size(); i++) {
            final String thisObject = ol.elementAt(i);
            final String newPath;
            if (curPath.equals(".")) {
                newPath = thisObject;
            } else {
                newPath = curPath + File.separator + thisObject;
            }
            if ((f = new File(newPath)).isDirectory()) {
                createNodeTree(curDir, displayDir, f);
            } else {
                files.addElement(thisObject);
            }
        }
        // Pass two: for files.
        // display("createBagManagerTree: files.size: " + files.size());
        for (int fnum = 0; fnum < files.size(); fnum++) {
            final String elem = files.elementAt(fnum);
            final DefaultMutableTreeNode elemNode = new DefaultMutableTreeNode(elem);
            curDir.add(elemNode);
            displayDir.add(elemNode);
        }

        // return curDir;
        return displayDir;
    }

    /**
     * @return Dimension
     */
    Dimension getTreeSize() {
        return new Dimension(BAGTREE_WIDTH, BAGTREE_HEIGHT);
    }

    /**
     *
     */
    private void initListeners() {
        addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(final TreeExpansionEvent e) {
                final int rows = BAGTREE_ROW_MODIFIER * getRowCount();
                log.trace("BagTree rows: {}", rows);
                setPreferredSize(new Dimension(BAGTREE_WIDTH, rows));
                invalidate();
            }

            public void treeCollapsed(final TreeExpansionEvent e) {
                final int rows = BAGTREE_ROW_MODIFIER * getRowCount();
                log.trace("BagTree rows: {}", rows);
                setPreferredSize(new Dimension(BAGTREE_WIDTH, rows));
                invalidate();
            }
        });
    }

}
