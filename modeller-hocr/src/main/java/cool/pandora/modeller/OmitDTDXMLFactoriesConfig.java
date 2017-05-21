package cool.pandora.modeller;


import org.xmlbeam.config.DefaultXMLFactoriesConfig;
import org.xmlbeam.config.XMLFactoriesConfig;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * OmitDTDXMLFactoriesConfig
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
            instance.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return instance;
    }
}