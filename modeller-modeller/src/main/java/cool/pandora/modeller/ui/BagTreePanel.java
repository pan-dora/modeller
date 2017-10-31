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

package cool.pandora.modeller.ui;

import javax.swing.JScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bag Tree Panel.
 *
 * @author gov.loc
 */
public class BagTreePanel extends JScrollPane {
    protected static final Logger log = LoggerFactory.getLogger(BagTreePanel.class);
    private static final long serialVersionUID = 5134745573017768256L;
    private BagTree bagTree;

    /**
     * BagTreePanel.
     *
     * @param bagTree BagTree
     */
    public BagTreePanel(final BagTree bagTree) {
        super(bagTree);
        this.bagTree = bagTree;
        init();
    }

    /**
     * init.
     */
    private void init() {
        log.debug("BagTreePanel.init");
        setViewportView(bagTree);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setPreferredSize(bagTree.getTreeSize());
    }

    /**
     * refresh.
     *
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
