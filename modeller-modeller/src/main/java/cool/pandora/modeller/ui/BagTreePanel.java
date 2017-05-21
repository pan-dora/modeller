package cool.pandora.modeller.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JScrollPane;

/**
 * Bag Tree Panel
 *
 * @author gov.loc
 */
public class BagTreePanel extends JScrollPane {
    private static final long serialVersionUID = 5134745573017768256L;
    protected static final Logger log = LoggerFactory.getLogger(BagTreePanel.class);
    private BagTree bagTree;

    /**
     * @param bagTree BagTree
     */
    public BagTreePanel(final BagTree bagTree) {
        super(bagTree);
        this.bagTree = bagTree;
        init();
    }

    /**
     *
     */
    private void init() {
        log.debug("BagTreePanel.init");
        setViewportView(bagTree);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setPreferredSize(bagTree.getTreeSize());
    }

    /**
     * @param tree BagTree
     */
    public void refresh(final BagTree tree) {
        this.bagTree = tree;
        if (getComponentCount() > 0 && bagTree != null && bagTree.isShowing()) {
            bagTree.invalidate();
        }
        init();
        invalidate();
        repaint();
    }
}
