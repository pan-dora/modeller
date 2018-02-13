/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoOperationFailedException;
import org.fcrepo.client.FcrepoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ModellerClient.
 *
 * @author Christopher Johnson
 */
public class ModellerClient {

    protected static final Logger log = LoggerFactory.getLogger(ModellerClient.class);

    /**
     * ModellerClient.
     */
    private ModellerClient() {
    }

    /**
     * doBinaryPut.
     *
     * @param destinationURI URI
     * @param resourceFile File
     * @param contentType String
     * @throws ModellerClientFailedException Throwable
     */
    public static void doBinaryPut(final URI destinationURI, final File resourceFile,
                                   final String contentType) throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            final FcrepoResponse response =
                    testClient.put(destinationURI).body(resourceFile, contentType).perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * doStreamPut.
     *
     * @param destinationURI URI
     * @param resourceFile ByteArrayInputStream
     * @param contentType String
     * @throws ModellerClientFailedException Throwable
     */
    public static void doStreamPut(final URI destinationURI, final InputStream resourceFile,
                                   final String contentType) throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            final FcrepoResponse response =
                    testClient.put(destinationURI).body(resourceFile, contentType).perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        }
    }

    /**
     * doPut.
     *
     * @param destinationURI URI
     * @throws ModellerClientFailedException Throwable
     */
    public static void doPut(final URI destinationURI) throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            final InputStream resourceFile = getFile("data/emptyFile.txt");
            final String contentType = "text/turtle";
            final FcrepoResponse response =
                    testClient.put(destinationURI).body(resourceFile, contentType).perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        }
    }

    /**
     * doPost.
     *
     * @param parentURI URI
     * @throws IOException Throwable
     */
    public static void doCreateDirectContainer(final URI parentURI, final String slug)
            throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String path = parentURI.getPath();
        String membershipObj = "http://trellis:8080" + path;
        try {
            HttpPost post = new HttpPost(parentURI);
            final String contentType = "text/turtle";
            final String entity = "@prefix ldp: <http://www.w3.org/ns/ldp#>\n"
                    + "<> ldp:hasMemberRelation <http://purl.org/dc/terms/isPartOf> ;\n"
                    + "ldp:membershipResource " + "<" + membershipObj + ">";
            post.setEntity(new StringEntity(entity));
            post.setHeader("Content-Type", contentType);
            post.setHeader("Slug", slug);
            post.setHeader("Link", "<http://www.w3.org/ns/ldp#DirectContainer>; rel=\"type\"");
            HttpResponse response = client.execute(post);
            log.info(String.valueOf(response.getStatusLine()));
        } catch (IOException e) {
            log.info(getMessage(e));
            throw new IOException(e);
        }
    }

    /**
     * doPatch.
     *
     * @param destinationURI URI
     * @param rdfBody InputStream
     * @throws ModellerClientFailedException Throwable
     */
    public static void doPatch(final URI destinationURI, final InputStream rdfBody)
            throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try {
            final FcrepoResponse response =
                    testClient.patch(destinationURI).body(rdfBody).perform();
            log.info(String.valueOf(response.getStatusCode()));
        } catch (FcrepoOperationFailedException e) {
            log.info(getMessage(e));
            throw new ModellerClientFailedException(e);
        }
    }

    /**
     * doGetContainerResources.
     *
     * @param containerURI URI
     * @return resources
     * @throws ModellerClientFailedException Throwable
     */
    public static String doGetContainerResources(final URI containerURI)
            throws ModellerClientFailedException {
        final FcrepoClient testClient;
        testClient = FcrepoClient.client().throwExceptionOnFailure().build();
        try (FcrepoResponse response = testClient.get(containerURI).accept("text/turtle")
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

    public static SSLConnectionSocketFactory getSSLFactory()
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
            KeyManagementException {
        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(
                new File(ModellerClient.class.getResource("/modeller.jks").getFile()),
                "changeme".toCharArray(), new TrustSelfSignedStrategy()).build();
        SSLConnectionSocketFactory sslsf =
                new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                        SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        return sslsf;
    }

    public static InputStream getFile(final String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(fileName);
    }
}
