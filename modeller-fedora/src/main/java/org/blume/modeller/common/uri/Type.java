package org.blume.modeller.common.uri;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class Type implements Serializable {
    private int value;
    private transient String path;

    protected Type(int value, String path) {
        this.value = value;
        this.path = path;
        storeType(this);
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return path;
    }

    private static final Hashtable<Object, Hashtable<Integer, Type>> types = new Hashtable<>();

    private void storeType(Type type) {
        String className = type.getClass().getName();

        Hashtable<Integer, Type> values;

        synchronized (types) {
            values = types.get(className);

            if (values == null) {
                values = new Hashtable<>();
                types.put(className, values);
            }
        }

        values.put(type.getValue(), type);
    }

    public static Type getByValue(Class classRef, int value) {
        Type type = null;
        String className = classRef.getName();
        Hashtable values = (Hashtable) types.get(className);

        if (values != null) {
            type = (Type) values.get(value);
        }

        return (type);
    }

    public static Enumeration elements(Class classRef) {
        String className = classRef.getName();

        Hashtable values = (Hashtable) types.get(className);
        if (values != null) {
            return (values.elements());
        } else {
            return null;
        }
    }
}
