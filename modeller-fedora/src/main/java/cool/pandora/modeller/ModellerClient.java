package cool.pandora.modeller;

import org.apache.commons.io.IOUtils;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoOperationFailedException;
import org.fcrepo.client.FcrepoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * ModellerClient
 *
 * @author Christopher Johnson
 */
public class ModellerClient {

    protected static final Logger log = LoggerFactory.getLogger(ModellerClient.class);

    /**
     *
     */
    private ModellerClient() {
    }

    /**
     * @param destinationURI URI
     * @param resourceFile   File
     * @param contentType    String
     * @throws ModellerClientFailedException Throwable
     */
    public static void doBinaryPut(final URI destinationURI, final File resourceFile, final String contentType)
            throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            final FcrepoResponse response = testClient.put(destinationURI).body(resourceFile, contentType).perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param destinationURI URI
     * @param resourceFile   ByteArrayInputStream
     * @param contentType    String
     * @throws ModellerClientFailedException Throwable
     */
    public static void doStreamPut(final URI destinationURI, final ByteArrayInputStream resourceFile,
                                   final String contentType) throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            final FcrepoResponse response = testClient.put(destinationURI).body(resourceFile, contentType).perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        }
    }

    /**
     * @param destinationURI URI
     * @throws ModellerClientFailedException Throwable
     */
    public static void doPut(final URI destinationURI) throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            final FcrepoResponse response = testClient.put(destinationURI).perform();
            try {
                log.info(IOUtils.toString(response.getBody(), UTF_8));
            } catch (IOException e) {
                log.info(getMessage(e));
            }
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        }
    }

    /**
     * @param destinationURI URI
     * @param rdfBody        InputStream
     * @throws ModellerClientFailedException Throwable
     */
    public static void doPatch(final URI destinationURI, final InputStream rdfBody)
            throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            final FcrepoResponse response = testClient.patch(destinationURI).body(rdfBody).perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        }
    }

    /**
     * @param containerURI URI
     * @return resources
     * @throws ModellerClientFailedException Throwable
     */
    public static String doGetContainerResources(final URI containerURI) throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try (FcrepoResponse response = testClient.get(containerURI).accept("text/turtle").perform()) {
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
