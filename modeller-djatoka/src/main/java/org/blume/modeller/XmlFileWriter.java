package org.blume.modeller;

import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XmlFileWriter extends ByteArrayOutputStream {

    public static XMLWriterBuilder write() {return new XMLWriterBuilder();}

    protected XmlFileWriter() {}

    private static ByteArrayOutputStream marshal(ImageFileDescriptor fileDescriptor) throws JAXBException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JAXBContext jaxbContext = JAXBContext.newInstance(ImageFileDescriptor.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(fileDescriptor, out);
            return out;
    }

    public static class XMLWriterBuilder {
        private String collectionRoot;
        private String collectionId;
        private String objektId;
        private String resourceContainer;
        private String resourceId;
        private String serializationDirectoryPath;

        public XmlFileWriter.XMLWriterBuilder collectionId(String collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        public XmlFileWriter.XMLWriterBuilder objektId(String objektId) {
            this.objektId = objektId;
            return this;
        }

        public XmlFileWriter.XMLWriterBuilder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public XmlFileWriter.XMLWriterBuilder serializationDirectoryPath(String serializationDirectoryPath ) {
            this.serializationDirectoryPath = serializationDirectoryPath;
            return this;
        }


        public ByteArrayOutputStream build() throws JAXBException {
            String descriptor = this.collectionId + "." + this.objektId + "." + this.resourceId;
            ImageFileDescriptor fileDescriptor = new ImageFileDescriptor();
            fileDescriptor.setId(descriptor);
            return XmlFileWriter.marshal(fileDescriptor);
        }
    }
}