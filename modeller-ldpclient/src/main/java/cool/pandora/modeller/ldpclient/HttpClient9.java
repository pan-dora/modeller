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

package cool.pandora.modeller.ldpclient;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static jdk.incubator.http.HttpClient.Version.HTTP_2;
import static jdk.incubator.http.HttpResponse.BodyHandler.asByteArray;
import static jdk.incubator.http.HttpResponse.BodyHandler.asString;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import org.apache.commons.rdf.api.IRI;
import org.apache.jena.riot.WebContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient9.
 *
 * @author christopher-johnson
 */
public class HttpClient9 {
    private static final Logger log = LoggerFactory.getLogger(HttpClient9.class);
    private HttpClient client = null;
    private static ExecutorService exec;
    static SSLContext sslContext;
    private static final String NON_NULL_IDENTIFIER = "Identifier may not be null!";

    private HttpClient9(final HttpClient client) {
        requireNonNull(client, "HTTP client may not be null!");
        this.client = client;
    }

    public HttpClient9() {
        this(getClient());
    }

    private static HttpClient getClient() {
        exec = Executors.newCachedThreadPool();
        return HttpClient.newBuilder().executor(exec)
                //.sslContext(sslContext)
                .version(HTTP_2).build();
    }

    public void putStream(final IRI identifier, final InputStream stream,
                          final Map<String, String> metadata) throws URISyntaxException,
            IOException, InterruptedException {
        requireNonNull(identifier, NON_NULL_IDENTIFIER);
        MediaType contentType = ofNullable(metadata.get(HttpHeaders.CONTENT_TYPE)).map(MediaType::valueOf)
                .orElse(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        HttpRequest req = HttpRequest.newBuilder(new URI(identifier.getIRIString()))
                .headers(HttpHeaders.CONTENT_TYPE, contentType.toString())
                .PUT(HttpRequest.BodyProcessor.fromInputStream(() -> stream)).build();
        HttpResponse<String> res = client.send(req, asString());
        log.info(
                "HTTP PUT request to {} returned {}", identifier, String.valueOf(res.statusCode()));
    }

    /**
     * doPost.
     *
     * @param parentURI URI
     * @throws IOException Throwable
     */
    public void doCreateDirectContainer(final URI parentURI, final String slug)
            throws IOException, InterruptedException {
        String path = parentURI.getPath();
        String membershipObj = "http://trellis:8080" + path;
        final String contentType = "text/turtle";
        final String entity = "@prefix ldp: <http://www.w3.org/ns/ldp#>\n"
                + "<> ldp:hasMemberRelation <http://purl.org/dc/terms/isPartOf> ;\n"
                + "ldp:membershipResource " + "<" + membershipObj + ">";
        HttpRequest req = HttpRequest.newBuilder(parentURI)
                .headers(
                        HttpHeaders.CONTENT_TYPE, contentType, "Slug", slug, "Link",
                        "<http://www.w3.org/ns/ldp#DirectContainer>; rel=\"type\"")
                .POST(HttpRequest.BodyProcessor.fromString(entity)).build();
        HttpResponse<String> res = client.send(req, asString());
        log.info(
                "HTTP POST request to {} returned {}", parentURI, String.valueOf(res.statusCode()));
    }

    public void syncPut(final String is, String toURI)
            throws ExecutionException, InterruptedException, URISyntaxException, IOException {
        HttpRequest req = HttpRequest.newBuilder(new URI(toURI))
                .headers("Content-Type", "text/n3; charset=UTF-8")
                .PUT(HttpRequest.BodyProcessor.fromString(is)).build();
        HttpResponse<String> response = client.send(req, asString());
        int statusCode = response.statusCode();
        log.info(String.valueOf(statusCode));
    }

    public void asyncPut(final String is, String toURI)
            throws ExecutionException, InterruptedException, URISyntaxException, IOException {
        HttpRequest req =
                HttpRequest.newBuilder(new URI(toURI)).headers("Content-Type", WebContent.contentTypeNTriples)
                        .PUT(HttpRequest.BodyProcessor.fromString(is)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, asString());
    }

    public byte[] syncGetQuery(final String query, String accept, boolean optimized)
            throws ExecutionException, InterruptedException, URISyntaxException, IOException {
        HttpRequest req = HttpRequest.newBuilder(new URI(query))
                .headers("Content-Type", WebContent.contentTypeSPARQLQuery, "Accept", accept).GET().build();
        HttpResponse<byte[]> response = client.send(req, asByteArray());

        log.info(String.valueOf(response.version()));
        log.info(String.valueOf(response.statusCode()));
        return response.body();
    }

    public String syncGetQuery(String query, String accept)
            throws ExecutionException, InterruptedException, URISyntaxException, IOException {
        HttpRequest req = HttpRequest.newBuilder(new URI(query))
                .headers("Content-Type", WebContent.contentTypeSPARQLQuery, "Accept", accept).GET().build();
        HttpResponse<String> response = client.send(req, asString());

        log.info(String.valueOf(response.version()));
        log.info(String.valueOf(response.statusCode()));
        return response.body();
    }

    public byte[] asyncGetQuery(final String query, String accept, boolean optimized)
            throws ExecutionException, InterruptedException, URISyntaxException, IOException {
        HttpRequest req = HttpRequest.newBuilder().uri(new URI(query))
                .headers("Content-Type", WebContent.contentTypeSPARQLQuery, "Accept", accept).GET().build();
        CompletableFuture<HttpResponse<byte[]>> response = client.sendAsync(req, asByteArray());
        log.info(String.valueOf(response.get().version()));
        log.info(String.valueOf(response.get().statusCode()));
        return response.get().body();
    }

    public String asyncGetQuery(final String query, String accept)
            throws ExecutionException, InterruptedException, URISyntaxException, IOException {
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(query))
                .headers("Content-Type", WebContent.contentTypeSPARQLQuery, "Accept", accept).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, asString());
        log.info(String.valueOf(response.get().version()));
        log.info(String.valueOf(response.get().statusCode()));
        return response.get().body();
    }

    public String syncUpdate(final String query)
            throws ExecutionException, InterruptedException, URISyntaxException, IOException {
        String formdata = "update=" + query;
        HttpRequest req =
                HttpRequest.newBuilder(new URI("http://localhost:3030/fuseki/annotations"))
                        .headers("Content-Type", WebContent.contentTypeSPARQLQuery)
                        .POST(HttpRequest.BodyProcessor.fromString(formdata)).build();
        HttpResponse<String> response = client.send(req, asString());
        log.info(String.valueOf(response.version()));
        log.info(String.valueOf(response.statusCode()));
        return response.body();
    }
}
