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


import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;

/**
 * xmlWriterTest
 *
 * @author Christopher Johnson
 */
public class xmlWriterTest {

    private xmlWriterTest() {
    }

    public static void main(final String[] args) throws JAXBException {
        final ByteArrayOutputStream out =
                XmlFileWriter.write().collectionId("test10").objektId("001").resourceId("004").build();
        System.out.println(out);
    }
}


