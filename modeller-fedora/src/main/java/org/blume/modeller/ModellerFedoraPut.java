package org.blume.modeller;

import org.apache.commons.io.IOUtils;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoResponse;

import static java.net.URI.create;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.fcrepo.client.FcrepoOperationFailedException;

import static java.net.URI.create;

public class ModellerFedoraPut {
    public static final String baseUrl = "http://localhost:8080/fcrepo/rest/collection/AIG/760/res/007.tif";
    public static void main(String[] args) {

        final URI uri = create(baseUrl);
        final File pictureFile = new File("/tmp/test1/data/007.tif");
        FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            FcrepoResponse response = testClient.put(uri)
                    .body(pictureFile, "image/jpg")
                    .perform();
            try {
                System.out.println(IOUtils.toString(response.getBody()));
            } catch (IOException e){
                System.out.println(e);
            }
        } catch (FcrepoOperationFailedException e) {
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
};
