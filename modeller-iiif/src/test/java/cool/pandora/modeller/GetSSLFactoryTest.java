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

import static cool.pandora.modeller.ModellerClient.getSSLFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.junit.Test;

public class GetSSLFactoryTest {
    @Test
    public void getSSLFactoryTest()
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            KeyManagementException, IOException {
        SSLConnectionSocketFactory sslsf;
        sslsf = getSSLFactory();
    }
}
