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

import cool.pandora.modeller.common.uri.IIIFPathTemplate;
import cool.pandora.modeller.common.uri.Type;

import java.util.Enumeration;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * getTypeTest
 *
 * @author Christopher Johnson
 */

public class GetTypeTest {

    @Test
    public void testType() throws Exception {
        final Type object_id_path = IIIFPathTemplate.OBJECT_ID_PATH;
        final Enumeration e = Type.elements(IIIFPathTemplate.class);
        final Type path = Type.getByValue(IIIFPathTemplate.class, 0);
        System.out.println(path);
        String out = "/{FedoraAppRoot}/{RestServletURI}/";
        assertEquals(path.toString(), out);
    }
}
