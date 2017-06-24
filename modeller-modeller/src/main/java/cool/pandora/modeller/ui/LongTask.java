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

import gov.loc.repository.bagit.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.ProgressMonitor;
import java.text.MessageFormat;

/**
 * LongTask
 *
 * @author gov.loc
 */
public class LongTask implements ProgressListener {

    protected static final Logger log = LoggerFactory.getLogger(LongTask.class);

    private boolean done = false;
    private Progress progress;
    private ProgressMonitor progressMonitor;

    private String activityMonitored;

    /**
     *
     */
    public LongTask() {
        // Compute length of task...
        // In a real program, this would figure out
        // the number of bytes to read or whatever.
    }

    /**
     * @param monitor ProgressMonitor
     */
    public void setMonitor(final ProgressMonitor monitor) {
        this.progressMonitor = monitor;
    }

    /**
     * @param progress Progress
     */
    public void setProgress(final Progress progress) {
        this.progress = progress;
    }

    /**
     * Called from ProgressBarDemo to start the task.
     */
    public void go() {
        final SwingWorker worker = new SwingWorker(this) {
            @Override
            public Object construct() {
                done = false;
                longTask.progress.execute();
                return new Object();
            }

            @Override
            public void finished() {
                // update UI
            }
        };
        worker.start();
    }

    /**
     * @return isCanceled
     */
    public boolean hasUserTriedToCancel() {
        return progressMonitor.isCanceled();
    }

    /**
     * Called from ProgressBarDemo to find out if the task has completed.
     */
    public boolean isDone() {
        return done;
    }

    /**
     *
     */
    public void done() {
        this.done = true;
        progressMonitor.close();
    }

    /**
     * should be thread-safe
     *
     * @param activity String
     * @param item     Object
     * @param count    Long
     * @param total    Long
     */
    @Override
    public synchronized void reportProgress(final String activity, final Object item, final Long count,
                                            final Long total) {
        if (count == null || total == null) {
            log.error("reportProgress received null info: count={}, total={}", count, total);
        } else {
            if (activityMonitored == null || activityMonitored.equals(activity)) {
                final String message = MessageFormat.format("{0} ({2} of {3}) {1} ", activity, item, count, total);
                this.progressMonitor.setNote(message);
                this.progressMonitor.setMaximum(total.intValue());
                this.progressMonitor.setProgress(count.intValue());
            }
        }
    }

    /**
     * @param activityMonitored String
     */
    public synchronized void setActivityMonitored(final String activityMonitored) {
        this.activityMonitored = activityMonitored;
    }

}
