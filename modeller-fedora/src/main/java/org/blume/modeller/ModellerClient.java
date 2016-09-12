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
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class ModellerClient {

    protected static final Logger log = LoggerFactory.getLogger(ModellerClient.class);

    public ModellerClient() {}

    public void doBinaryPut(URI destinationURI, File resourceFile, String contentType) throws ModellerClientFailedException {
        FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            FcrepoResponse response = testClient.put(destinationURI)
                    .body(resourceFile, contentType)
                    .perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doPut(URI destinationURI) throws ModellerClientFailedException {
        FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            FcrepoResponse response = testClient.put(destinationURI)
                    .perform();
            try {
                log.info(IOUtils.toString(response.getBody(), UTF_8));
            } catch (IOException e){
                log.info(getMessage(e));
            }
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        }
    }

    public void doPatch(URI destinationURI, InputStream rdfBody) throws ModellerClientFailedException {
        FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            FcrepoResponse response = testClient.patch(destinationURI)
                    .body(rdfBody)
                    .perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        }
    }

    public String doGetContainerResources(URI containerURI) throws ModellerClientFailedException {
        FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try (FcrepoResponse response = testClient.get(containerURI)
                    .accept("text/turtle")
                    .perform()) {
            return IOUtils.toString(response.getBody(), "UTF-8");
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
