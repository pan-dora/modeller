package cool.pandora.modeller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class DefaultContainerBuilderTest {

    @Test
    public void testCreateDirectContainer() {
        try {
            URI uri = new URI("http://localhost:8080/repository/12356");
            URI parent = new URI(StringUtils.substringBeforeLast(uri.toString(),"/"));
            String slug = new File(Objects.requireNonNull(uri).getPath()).getName();
            ModellerClient.doCreateDirectContainer(parent, slug);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }
}
