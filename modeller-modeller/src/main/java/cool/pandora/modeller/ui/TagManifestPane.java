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

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import gov.loc.repository.bagit.BagFile;

import java.awt.Dimension;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Tag Manifest Pane.
 *
 * @author gov.loc
 */
public class TagManifestPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    protected static final Logger log = LoggerFactory.getLogger(TagManifestPane.class);
    private final BagView parentView;
    private DefaultBag defaultBag;
    private final Dimension preferredDimension = new Dimension(600, 480);
    private final Color selectedColor = Color.lightGray; // new Color(180, 180, 200);
    private final Color unselectedColor = Color.black; // new Color(180, 180, 160);

    /**
     * TagManifestPane.
     *
     * @param bagView BagView
     */
    public TagManifestPane(final BagView bagView) {
        super();
        this.parentView = bagView;
        this.defaultBag = bagView.getBag();
        populateBagPane();
    }

    /**
     * init.
     */
    private void init() {
        this.setPreferredSize(preferredDimension);
        final ChangeListener changeListener = changeEvent -> {
            final JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            final int count = sourceTabbedPane.getTabCount();
            final int selected = sourceTabbedPane.getSelectedIndex();
            for (int i = 0; i < count; ++i) {
                final Color c = (i == selected) ? unselectedColor : selectedColor;
                sourceTabbedPane.setBackgroundAt(i, c);
                sourceTabbedPane.setForegroundAt(i, c);
            }
        };
        this.addChangeListener(changeListener);
    }

    /**
     * setBag.
     *
     * @param bag DefaultBag
     */
    public void setBag(final DefaultBag bag) {
        this.defaultBag = bag;
    }

    /**
     * getBag.
     *
     * @return defaultBag
     */
    public DefaultBag getBag() {
        return this.defaultBag;
    }

    /**
     * populateBagPane.
     */
    private void populateBagPane() {
        final Collection<BagFile> list = defaultBag.getTags();
        final List<BagTextPane> manifestPaneList = new ArrayList<>();
        final List<JScrollPane> manifestScrollPaneList = new ArrayList<>();
        log.info("TagManifestPane.populateBagPane getTags: {}", list.size());
        for (final BagFile bf : list) {
            final String content;
            if (bf.getFilepath().contains("manifest")) {
                content = bf.toString();
            } else {
                content = tokenFormat(bf.toString());
            }
            log.debug("BagFile: {}::{}", bf.getFilepath(), content);
            final BagTextPane manifestPane = new BagTextPane(content);
            manifestPaneList.add(manifestPane);
            final JScrollPane manifestScrollPane = new JScrollPane();
            manifestScrollPane.setViewportView(manifestPane);
            manifestScrollPane.setToolTipText(parentView.getPropertyMessage("compositePane.tab" +
                    ".manifest.help"));
            manifestScrollPane.setForeground(selectedColor);
            manifestScrollPaneList.add(manifestScrollPane);
            final String tabName = bf.getFilepath();
            log.debug("manifestName: {}", tabName);
            addTab(tabName, manifestScrollPane);
        }

        final JScrollPane dataScrollPane = new JScrollPane();
        final String dataContent = defaultBag.getDataContent();
        final BagTextPane dataPane = new BagTextPane(dataContent);
        dataScrollPane.setViewportView(dataPane);
        dataScrollPane.setToolTipText(parentView.getPropertyMessage("compositePane.tab.data.help"));
        dataScrollPane.setForeground(selectedColor);
        if (!this.defaultBag.isHoley()) {
            addTab(parentView.getPropertyMessage("compositePane.tab.data"), dataScrollPane);
        }
        init();
    }

    /**
     * updateCompositePaneTabs.
     *
     * setBag must be called before updateTabs is called
     *
     * @param defaultBag DefaultBag
     */
    public void updateCompositePaneTabs(final DefaultBag defaultBag) {
        setBag(defaultBag);
        if (this.getComponentCount() > 0) {
            this.removeAll();
            this.invalidate();
        }
        populateBagPane();
        final int count = this.getTabCount();
        final int selected = this.getSelectedIndex();
        for (int i = 0; i < count; ++i) {
            final Color c = (i == selected) ? unselectedColor : selectedColor;
            this.setBackgroundAt(i, c);
            this.setForegroundAt(i, c);
        }
        this.invalidate();
    }

    /**
     * tokenFormat.
     *
     * @param content String
     * @return buffer
     */
    private static String tokenFormat(final String content) {
        final StringBuilder buffer = new StringBuilder();

        final StringTokenizer st = new StringTokenizer(content, ",", false);
        while (st.hasMoreTokens()) {
            final String s = st.nextToken();
            buffer.append(s);
            buffer.append('\n');
        }
        return buffer.toString();
    }
}
