package cool.pandora.modeller.common.uri;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Type
 *
 * @author Christopher Johnson
 */
public class Type implements Serializable {
    private final int value;
    private final transient String path;

    /**
     * @param value int
     * @param path  String
     */
    protected Type(final int value, final String path) {
        this.value = value;
        this.path = path;
        storeType(this);
    }

    /**
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * @return path
     */
    public String toString() {
        return path;
    }

    /**
     *
     */
    private static final Hashtable<Object, Hashtable<Integer, Type>> types = new Hashtable<>();

    /**
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
     * @param classRef Class
     * @param value    int
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
