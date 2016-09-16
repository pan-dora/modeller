package org.blume.modeller;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XMLWriter {

    public static XMLWriterBuilder write() {return new XMLWriterBuilder();}

    protected XMLWriter(final String descriptor, final String outputPath) {

        ImageFileDescriptor fileDescriptor = new ImageFileDescriptor();
        fileDescriptor.setId(descriptor);

        try {

            File file = new File(outputPath);
            JAXBContext jaxbContext = JAXBContext.newInstance(ImageFileDescriptor.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(fileDescriptor, file);
            jaxbMarshaller.marshal(fileDescriptor, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public static class XMLWriterBuilder {
        private String collectionRoot;
        private String collectionId;
        private String objektId;
        private String resourceContainer;
        private String resourceId;
        private String serializationDirectoryPath;


        public XMLWriter.XMLWriterBuilder collectionRoot(String collectionRoot) {
            this.collectionRoot = collectionRoot;
            return this;
        }

        public XMLWriter.XMLWriterBuilder resourceContainer(String resourceContainer) {
            this.resourceContainer = resourceContainer;
            return this;
        }

        public XMLWriter.XMLWriterBuilder collectionId(String collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        public XMLWriter.XMLWriterBuilder objektId(String objektId) {
            this.objektId = objektId;
            return this;
        }

        public XMLWriter.XMLWriterBuilder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public XMLWriter.XMLWriterBuilder serializationDirectoryPath(String serializationDirectoryPath ) {
            this.serializationDirectoryPath = serializationDirectoryPath;
            return this;
        }

        public XMLWriter build() {
            String descriptor = this.collectionId + "." + this.objektId + "." + this.resourceId;
            String outputPath = this.serializationDirectoryPath + this.collectionRoot + "/" + this.collectionId + "/"
                    + this.objektId + "/" + this.resourceContainer + "/" + this.resourceId + ".xml";
            return new XMLWriter(descriptor, outputPath);
        }
    }
}