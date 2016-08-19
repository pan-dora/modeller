package org.blume.modeller;

import org.blume.modeller.ModellerClient;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class test {
    public static void main(String[] args) {
        String destinationURI = "http://localhost:8080/fcrepo/rest/collection/AIG/777/001.tif";
        Path bagResourcePath = Paths.get("/tmp/test1/data/001.tif");
        File bagResource = bagResourcePath.toFile();
        ModellerClient client = new ModellerClient();
        client.doBinaryPut(destinationURI, bagResource);
    }
}
