package org.blume.modeller;

import org.blume.modeller.common.uri.IIIFPathTemplate;
import org.blume.modeller.common.uri.Type;

import java.util.Enumeration;

public class getTypeTest {

    public static void main(String[] args) {
        Type object_id_path = IIIFPathTemplate.OBJECT_ID_PATH;
        Enumeration e = Type.elements(IIIFPathTemplate.class);
        Type path = Type.getByValue(IIIFPathTemplate.class, 0);
        System.out.println(path);
    }
}
