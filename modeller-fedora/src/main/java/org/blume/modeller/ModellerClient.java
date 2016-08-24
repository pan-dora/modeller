package org.blume.modeller;

import org.apache.commons.io.IOUtils;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoOperationFailedException;
import org.fcrepo.client.FcrepoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static java.net.URI.create;

public class ModellerClient {
    protected static final Logger log = LoggerFactory.getLogger(ModellerClient.class);
    public ModellerClient() {}

    public void doBinaryPut(String destinationURI, File bagResource) {
        final URI uri = create(destinationURI);
        FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            FcrepoResponse response = testClient.put(uri)
                    .body(bagResource, "image/jpg")
                    .perform();
            try {
                log.info(IOUtils.toString(response.getBody()));
            } catch (IOException e){
                System.out.println(e);
            }
        } catch (FcrepoOperationFailedException e) {
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doPut(String destinationURI) {
        final URI uri = create(destinationURI);
        FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            FcrepoResponse response = testClient.put(uri)
                    .perform();
            try {
                log.info(IOUtils.toString(response.getBody()));
            } catch (IOException e){
                System.out.println(e);
            }
        } catch (FcrepoOperationFailedException e) {
            System.out.println(e);
        }
    }
}
