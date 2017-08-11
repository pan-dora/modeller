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

import cool.pandora.modeller.bag.BaggerFileEntity;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Remove Data Handler.
 *
 * @author gov.loc
 */
public class RemoveDataHandler extends AbstractAction {
    protected static final Logger log = LoggerFactory.getLogger(RemoveDataHandler.class);
    private static final long serialVersionUID = 1L;
    BagView bagView;

    /**
     * RemoveDataHandler.
     *
     * @param bagView BagView
     */
    public RemoveDataHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        removeData();
    }

    private void removeData() {
        String message;
        final DefaultBag bag = bagView.getBag();

        final TreePath[] paths = bagView.bagPayloadTree.getSelectionPaths();

        if (paths != null) {
            final DefaultTreeModel model = (DefaultTreeModel) bagView.bagPayloadTree.getModel();
            for (final TreePath path : paths) {
                final Object node = path.getLastPathComponent();
                log.debug("removeData: {}", path.toString());
                log.debug("removeData pathCount: {}", path.getPathCount());
                File filePath = null;
                String fileName = null;
                if (path.getPathCount() > 0) {
                    filePath = new File("" + path.getPathComponent(0));
                    for (int j = 1; j < path.getPathCount(); j++) {
                        filePath = new File(filePath, "" + path.getPathComponent(j));
                        log.debug("filepath: {}", filePath);
                    }
                }
                if (filePath != null) {
                    fileName = BaggerFileEntity.normalize(filePath.getPath());
                }
                log.debug("removeData filePath: {}", fileName);
                if (fileName != null && !fileName.isEmpty()) {
                    try {
                        bag.removeBagFile(fileName);
                        ApplicationContextUtil.addConsoleMessage("Payload data removed: "
                                + fileName);
                        if (node instanceof MutableTreeNode) {
                            model.removeNodeFromParent((MutableTreeNode) node);
                        } else {
                            final DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(node);
                            model.removeNodeFromParent(aNode);
                        }
                    } catch (final Exception e) {
                        try {
                            bag.removePayloadDirectory(fileName);
                            if (node instanceof MutableTreeNode) {
                                model.removeNodeFromParent((MutableTreeNode) node);
                            } else {
                                final DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(
                                        node);
                                model.removeNodeFromParent(aNode);
                            }
                        } catch (final Exception ex) {
                            message = "Error trying to remove: " + fileName + "\n";
                            BagView.showWarningErrorDialog("Error - file not removed", message
                                    + ex.getMessage());
                        }
                    }
                }
            }

            bagView.bagPayloadTree.removeSelectionPaths(paths);
            bagView.bagPayloadTreePanel.refresh(bagView.bagPayloadTree);
        }
    }

}
