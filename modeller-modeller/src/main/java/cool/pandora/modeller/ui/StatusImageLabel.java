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

import cool.pandora.modeller.model.Status;
import cool.pandora.modeller.model.StatusModel;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Status Image Label.
 *
 * @author gov.loc
 */
public class StatusImageLabel extends JLabel implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    private static final String PASS_STATUS_ICON = "status.success.icon";
    private static final String FAILURE_STATUS_ICON = "status.fail.icon";
    private static final String UNKNOWN_STATUS_ICON = "status.unknown.icon";

    /**
     * StatusImageLabel.
     *
     * @param model StatusModel
     */
    public StatusImageLabel(final StatusModel model) {
        super("");
        changeIcon(model.getStatus());
        model.addPropertyChangeListener(this);
    }

    /**
     * propertyChange.
     *
     * @param evt PropertyChangeEvent
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final Status newValue = (Status) evt.getNewValue();
        changeIcon(newValue);
    }

    /**
     * changeIcon.
     *
     * @param status Status
     */
    private void changeIcon(final Status status) {
        final ImageIcon icon;
        if (status == Status.PASS) {
            icon = new ImageIcon(ApplicationContextUtil.getImage(PASS_STATUS_ICON));
            setToolTipText(ApplicationContextUtil.getMessage("consolepane.status.pass.help"));
        } else if (status == Status.FAILURE) {
            icon = new ImageIcon(ApplicationContextUtil.getImage(FAILURE_STATUS_ICON));
            setToolTipText(ApplicationContextUtil.getMessage("consolepane.status.fail.help"));
        } else {
            icon = new ImageIcon(ApplicationContextUtil.getImage(UNKNOWN_STATUS_ICON));
            setToolTipText(ApplicationContextUtil.getMessage("consolepane.status.unknown.help"));
        }
        this.setIcon(icon);

    }
}
