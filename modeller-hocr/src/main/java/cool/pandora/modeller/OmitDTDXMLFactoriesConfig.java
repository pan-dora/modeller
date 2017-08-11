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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xmlbeam.config.DefaultXMLFactoriesConfig;
import org.xmlbeam.config.XMLFactoriesConfig;
/**
 * OmitDTDXMLFactoriesConfig.
 *
 * @author Christopher Johnson
 */
class OmitDTDXMLFactoriesConfig extends DefaultXMLFactoriesConfig implements XMLFactoriesConfig {

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentBuilderFactory createDocumentBuilderFactory() {
        final DocumentBuilderFactory instance = DocumentBuilderFactory.newInstance();
        try {
            instance.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return instance;
    }
}