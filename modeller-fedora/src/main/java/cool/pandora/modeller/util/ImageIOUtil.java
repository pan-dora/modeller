package cool.pandora.modeller.util;


import com.twelvemonkeys.imageio.plugins.tiff.TIFFImageWriterSpi;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageInputStream;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static cool.pandora.modeller.common.uri.FedoraResources.IMAGE;

/**
 * ImageIOUtil
 *
 * @author Christopher Johnson
 */
public class ImageIOUtil {

    private ImageIOUtil() {
    }

    /**
     *
     */
    private static void registerAllServicesProviders() {
        IIORegistry.getDefaultInstance().registerServiceProvider(new TIFFImageWriterSpi());
    }

    /**
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
     * @param resourceFile File
     * @return MimeType
     */
    public static String getImageMIMEType(final File resourceFile) {
        registerAllServicesProviders();

        try (ImageInputStream in = ImageIO.createImageInputStream(resourceFile)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                final ImageReader reader = readers.next();
                try {
                    final String formatName = reader.getFormatName();
                    reader.setInput(in);
                    return IMAGE + formatName;
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
