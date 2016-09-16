package org.blume.modeller;

public class xmlWriterTest {
    public static void main(String[] args) {
            XMLWriter.write()
                .collectionRoot("collection")
                .collectionId("test10")
                .objektId("001")
                .resourceContainer("res")
                .resourceId("004")
                .serializationDirectoryPath("/mnt/fcrepo/binaries/")
                .build();
    }
}


