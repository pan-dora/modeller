package org.blume.modeller;

import org.apache.commons.io.IOUtils;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoOperationFailedException;
import org.fcrepo.client.FcrepoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.net.URI.create;

public class ModellerClient {
    protected static final Logger log = LoggerFactory.getLogger(ModellerClient.class);
    public ModellerClient() {}

    public void doBinaryPut(String destinationURI, File resourceFile, String contentType) {
        final URI uri = create(destinationURI);
        FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            FcrepoResponse response = testClient.put(uri)
                    .body(resourceFile, contentType)
                    .perform();
            log.info(String.valueOf(response.getStatusCode()));
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
                log.info(IOUtils.toString(response.getBody(), UTF_8));
            } catch (IOException e){
                System.out.println(e);
            }
        } catch (FcrepoOperationFailedException e) {
            System.out.println(e);
        }
    }

    public void doPatch(String destinationURI, InputStream rdfBody) throws ModellerClientFailedException {
        final URI uri = create(destinationURI);
        FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            FcrepoResponse response = testClient.patch(uri)
                    .body(rdfBody)
                    .perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            System.out.println(e);
            throw new ModellerClientFailedException(e);
        }
    }
}
