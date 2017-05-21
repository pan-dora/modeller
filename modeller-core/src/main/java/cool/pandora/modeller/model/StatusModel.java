package cool.pandora.modeller.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * StatusModel
 *
 * @author gov.loc
 */
public class StatusModel {

    private Status status = Status.UNKNOWN;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * @return status
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * @param status Status
     */
    public void setStatus(final Status status) {
        final Status old = this.status;
        this.status = status;
        this.pcs.firePropertyChange("status", old, status);
    }

    /**
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

}
