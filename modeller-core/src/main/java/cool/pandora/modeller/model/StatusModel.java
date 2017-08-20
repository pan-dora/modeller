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

package cool.pandora.modeller.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * StatusModel.
 *
 * @author gov.loc
 */
public class StatusModel {

    private Status status = Status.UNKNOWN;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * getStatus.
     *
     * @return status
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * setStatus.
     *
     * @param status Status
     */
    public void setStatus(final Status status) {
        final Status old = this.status;
        this.status = status;
        this.pcs.firePropertyChange("status", old, status);
    }

    /**
     * addPropertyChangeListener.
     *
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * removePropertyChangeListener.
     *
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

}
