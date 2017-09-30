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

package cool.pandora.modeller.util;

import static cool.pandora.modeller.common.uri.FedoraResources.IMAGE;

import com.github.jaiimageio.jpeg2000.impl.J2KImageWriterSpi;

import com.twelvemonkeys.imageio.plugins.tiff.TIFFImageWriterSpi;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageInputStream;


/**
 * ImageIOUtil.
 *
 * @author Christopher Johnson
 */
public class ImageIOUtil {

    private ImageIOUtil() {
    }

    /**
     * registerAllServicesProviders.
     */
    private static void registerAllServicesProviders() {
        IIORegistry.getDefaultInstance().registerServiceProvider(new TIFFImageWriterSpi());
        IIORegistry.getDefaultInstance().registerServiceProvider(new J2KImageWriterSpi());
    }

    /**
     * getImageDimensions.
     *
     * @param resourceFile File
     * @return Dimension
     */
    public static Dimension getImageDimensions(final File resourceFile) {
        registerAllServicesProviders();

        try (ImageInputStream in = ImageIO.createImageInputStream(resourceFile)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                final ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getImageMIMEType.
     *
     * @param resourceFile File
     * @return MimeType
     */
    public static String getImageMIMEType(final File resourceFile) {
        registerAllServicesProviders();
        String mimeType = null;
        try (ImageInputStream in = ImageIO.createImageInputStream(resourceFile)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                final ImageReader reader = readers.next();
                try {
                    final String formatName = reader.getFormatName();
                    switch (formatName) {
                        case("tif"):
                           mimeType = IMAGE + "tiff";
                           break;
                        case("jpeg 2000"):
                            mimeType = IMAGE + "jp2";
                            break;
                        default:
                            break;    
                    }
                    reader.setInput(in);
                    return mimeType;
                } finally {
                    reader.dispose();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
