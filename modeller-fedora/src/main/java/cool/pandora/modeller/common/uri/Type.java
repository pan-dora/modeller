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

package cool.pandora.modeller.common.uri;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Type.
 *
 * @author Christopher Johnson
 */
public class Type implements Serializable {
    private final int value;
    private final transient String path;

    /**
     * Type.
     *
     * @param value int
     * @param path String
     */
    protected Type(final int value, final String path) {
        this.value = value;
        this.path = path;
        storeType(this);
    }

    /**
     * getValue.
     *
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * toString.
     *
     * @return path
     */
    public String toString() {
        return path;
    }

    /**
     * types.
     */
    private static final Hashtable<Object, Hashtable<Integer, Type>> types = new Hashtable<>();

    /**
     * storeType.
     *
     * @param type Type
     */
    private static void storeType(final Type type) {
        final String className = type.getClass().getName();

        final Hashtable<Integer, Type> values;

        synchronized (types) {
            values = types.computeIfAbsent(className, k -> new Hashtable<>());

        }

        values.put(type.getValue(), type);
    }

    /**
     * getByValue.
     *
     * @param classRef Class
     * @param value int
     * @return type
     */
    public static Type getByValue(final Class classRef, final int value) {
        Type type = null;
        final String className = classRef.getName();
        final Hashtable values = types.get(className);

        if (values != null) {
            type = (Type) values.get(value);
        }

        return (type);
    }

    /**
     * elements.
     *
     * @param classRef Class
     * @return values
     */
    public static Enumeration elements(final Class classRef) {
        final String className = classRef.getName();

        final Hashtable values = types.get(className);
        if (values != null) {
            return (values.elements());
        } else {
            return null;
        }
    }
}
