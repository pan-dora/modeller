package cool.pandora.modeller.ui.handlers.base;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bag Tree Transfer Handler
 *
 * @author gov.loc
 */
public class BagTreeTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 1L;

    protected static final Logger log = LoggerFactory.getLogger(BagTreeTransferHandler.class);
    private static final DataFlavor uriListFlavor = new DataFlavor("text/uri-list;class=java.lang.String", null);
    private DataFlavor nodesFlavor;
    private final DataFlavor[] flavors = new DataFlavor[1];
    private DefaultMutableTreeNode[] nodesToRemove;

    /**
     *
     */
    public BagTreeTransferHandler() {
        super();
        try {
            final String mimeType =
                    DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + DefaultMutableTreeNode[].class.getName() +
                            "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch (final ClassNotFoundException e) {
            log.error("Failed to construct a nodeFlavor", e);
        }
    }

    /**
     * @param s String
     */
    private static void display(final String s) {
        final String msg = "BagTreeTransferHandler." + s;
        log.info(msg);
    }

    @Override
    public int getSourceActions(final JComponent c) {
        return TransferHandler.COPY;
    }

    /**
     * @param comp            JComponent
     * @param transferFlavors DataFlavor
     * @return boolean
     */
    @Override
    public boolean canImport(final JComponent comp, final DataFlavor[] transferFlavors) {
        for (final DataFlavor transferFlavor : transferFlavors) {
            final Class<?> representationclass = transferFlavor.getRepresentationClass();
            // URL from Explorer or Firefox, KDE
            if (representationclass != null && URL.class.isAssignableFrom(representationclass)) {
                return true;
            }
            // Drop from Windows Explorer
            if (DataFlavor.javaFileListFlavor.equals(transferFlavor)) {
                return true;
            }
            // Drop from GNOME
            if (DataFlavor.stringFlavor.equals(transferFlavor)) {
                return true;
            }
            if (uriListFlavor.equals(transferFlavor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param c JComponent
     * @return NodesTransferable
     */
    @Override
    protected Transferable createTransferable(final JComponent c) {
        final JTree tree = (JTree) c;
        final TreePath[] paths = tree.getSelectionPaths();
        if (paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            final List<DefaultMutableTreeNode> copies = new ArrayList<>();
            final List<DefaultMutableTreeNode> toRemove = new ArrayList<>();
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[0].getLastPathComponent();
            final DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);
            for (int i = 1; i < paths.length; i++) {
                final DefaultMutableTreeNode next = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.
                if (next.getLevel() < node.getLevel()) {
                    break;
                } else if (next.getLevel() > node.getLevel()) { // child node
                    copy.add(copy(next));
                    // node already contains child
                } else { // sibling
                    copies.add(copy(next));
                    toRemove.add(next);
                }
            }
            final DefaultMutableTreeNode[] nodes = copies.toArray(new DefaultMutableTreeNode[copies.size()]);
            nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }

    /**
     * Defensive copy used in createTransferable.
     */
    private static DefaultMutableTreeNode copy(final TreeNode node) {
        return new DefaultMutableTreeNode(node);
    }

    /**
     * @param source JComponent
     * @param data   Transferable
     * @param action int
     */
    @Override
    protected void exportDone(final JComponent source, final Transferable data, final int action) {
        final JTree tree = (JTree) source;
        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        // Remove nodes saved in nodesToRemove in createTransferable.
        for (final DefaultMutableTreeNode aNodesToRemove : nodesToRemove) {
            display("exportDonevnodesToRemove: " + aNodesToRemove);
            model.removeNodeFromParent(aNodesToRemove);
        }
    }

    /**
     *
     */
    public class NodesTransferable implements Transferable {
        DefaultMutableTreeNode[] nodes;

        NodesTransferable(final DefaultMutableTreeNode[] nodes) {
            this.nodes = nodes.clone();
        }

        /**
         * @param flavor DataFlavor
         * @return nodes
         * @throws UnsupportedFlavorException Exception
         */
        @Override
        public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }

            return nodes.clone();
        }

        /**
         * @return flavors
         */
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        /**
         * @param flavor DataFlavor
         * @return boolean
         */
        @Override
        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}
