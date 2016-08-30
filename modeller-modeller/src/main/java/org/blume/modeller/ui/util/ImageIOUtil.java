package org.blume.modeller.ui.util;


import com.twelvemonkeys.imageio.plugins.tiff.TIFFImageWriterSpi;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import static org.blume.modeller.common.uri.FedoraResources.IMAGE;

public class ImageIOUtil {


    public static void registerAllServicesProviders() {
        IIORegistry.getDefaultInstance().registerServiceProvider(new TIFFImageWriterSpi());
    }

    public Dimension getImageDimensions(File resourceFile) {
        registerAllServicesProviders();

        try (ImageInputStream in = ImageIO.createImageInputStream(resourceFile)){
            Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getImageMIMEType(File resourceFile) {
        registerAllServicesProviders();

        try (ImageInputStream in = ImageIO.createImageInputStream(resourceFile)){
            Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    String formatName = reader.getFormatName();
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
