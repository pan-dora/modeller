package cool.pandora.modeller;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ImageFileDescriptor
 *
 * @author Christopher Johnson
 */
@XmlRootElement
public class ImageFileDescriptor {

    private String id;

    /**
     *
     * @param id String
     */
    @XmlElement
    void setId(final String id) {
        this.id = id;
    }

    /**
     *
     * @return id
     */
    public String getId() {
        return id;
    }

}