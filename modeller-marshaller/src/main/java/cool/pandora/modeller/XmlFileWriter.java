package cool.pandora.modeller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

/**
 * XmlFileWriter
 *
 * @author Christopher Johnson
 */
public class XmlFileWriter extends ByteArrayOutputStream {
    /**
     *
     * @return XMLWriterBuilder
     */
    public static XMLWriterBuilder write() {
        return new XMLWriterBuilder();
    }

    protected XmlFileWriter() {
    }

    /**
     *
     * @param fileDescriptor ImageFileDescriptor
     * @return marshalled output
     * @throws JAXBException Exception
     */
    private static ByteArrayOutputStream marshal(final ImageFileDescriptor fileDescriptor) throws JAXBException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final JAXBContext jaxbContext = JAXBContext.newInstance(ImageFileDescriptor.class);
        final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(fileDescriptor, out);
        return out;
    }

    /**
     *
     */
    public static class XMLWriterBuilder {
        private String collectionId;
        private String objektId;
        private String resourceId;

        /**
         *
         * @param collectionId String
         * @return this
         */
        public XmlFileWriter.XMLWriterBuilder collectionId(final String collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        /**
         *
         * @param objektId String
         * @return this
         */
        public XmlFileWriter.XMLWriterBuilder objektId(final String objektId) {
            this.objektId = objektId;
            return this;
        }

        /**
         *
         * @param resourceId String
         * @return this
         */
        public XmlFileWriter.XMLWriterBuilder resourceId(final String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        /**
         *
         * @return marshalled output
         * @throws JAXBException Exception
         */
        public ByteArrayOutputStream build() throws JAXBException {
            final String descriptor = this.collectionId + "." + this.objektId + "." + this.resourceId;
            final ImageFileDescriptor fileDescriptor = new ImageFileDescriptor();
            fileDescriptor.setId(descriptor);
            return XmlFileWriter.marshal(fileDescriptor);
        }
    }
}