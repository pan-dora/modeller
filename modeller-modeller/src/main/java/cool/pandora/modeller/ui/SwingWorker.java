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

import javax.swing.SwingUtilities;

/**
 * This is the 3rd version of SwingWorker (also known as
 * SwingWorker 3), an abstract class that you subclass to
 * perform GUI-related work in a dedicated thread. For
 * instructions on and examples of using this class, see:
 *
 * <p>http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 *
 * <p>Note that the API changed slightly in the 3rd version:
 * You must now invoke start() on the SwingWorker after
 * creating it.
 *
 * @author gov.loc
 */
public abstract class SwingWorker {
    private final ThreadVar threadVar;
    LongTask longTask;
    private Object value; // see getValue(), setValue()

    /**
     * SwingWorker.
     *
     * @param longTask LongTask
     */
    SwingWorker(final LongTask longTask) {
        this.longTask = longTask;
        final Runnable doFinished = this::finished;

        final Runnable doConstruct = new Runnable() {
            @Override
            public void run() {
                try {
                    setValue(construct());
                } finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        final Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    /**
     * Get the value produced by the worker thread, or null if it
     * hasn't been constructed yet.
     *
     * @return value Object
     */
    protected synchronized Object getValue() {
        return value;
    }

    /**
     * Set the value produced by worker thread.
     */
    private synchronized void setValue(final Object x) {
        value = x;
    }

    /**
     * Compute the value to be returned by the <code>get</code> method.
     *
     * @return Object
     */
    public abstract Object construct();

    /**
     * Called on the event dispatching thread (not on the worker thread)
     * after the <code>construct</code> method has returned.
     */
    public void finished() {
    }

    /**
     * Return the value created by the <code>construct</code> method.
     * Returns null if either the constructing thread or the current
     * thread was interrupted before a value was produced.
     *
     * @return the value created by the <code>construct</code> method
     */
    public Object get() {
        while (true) {
            final Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }

    /**
     * Start the worker thread.
     */
    public void start() {
        final Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }

    /**
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private static class ThreadVar {
        private Thread thread;

        ThreadVar(final Thread t) {
            thread = t;
        }

        synchronized Thread get() {
            return thread;
        }

        synchronized void clear() {
            thread = null;
        }
    }
}
