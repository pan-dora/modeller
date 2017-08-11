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
     * @return XMLWriterBuilder
     */
    public static XMLWriterBuilder write() {
        return new XMLWriterBuilder();
    }

    protected XmlFileWriter() {
    }

    /**
     * @param fileDescriptor ImageFileDescriptor
     * @return marshalled output
     * @throws JAXBException Exception
     */
    private static ByteArrayOutputStream marshal(final ImageFileDescriptor fileDescriptor) throws
            JAXBException {
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
         * @param collectionId String
         * @return this
         */
        public XmlFileWriter.XMLWriterBuilder collectionId(final String collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        /**
         * @param objektId String
         * @return this
         */
        public XmlFileWriter.XMLWriterBuilder objektId(final String objektId) {
            this.objektId = objektId;
            return this;
        }

        /**
         * @param resourceId String
         * @return this
         */
        public XmlFileWriter.XMLWriterBuilder resourceId(final String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        /**
         * @return marshalled output
         * @throws JAXBException Exception
         */
        public ByteArrayOutputStream build() throws JAXBException {
            final String descriptor = this.collectionId + "." + this.objektId + "." + this
                    .resourceId;
            final ImageFileDescriptor fileDescriptor = new ImageFileDescriptor();
            fileDescriptor.setId(descriptor);
            return XmlFileWriter.marshal(fileDescriptor);
        }
    }
}