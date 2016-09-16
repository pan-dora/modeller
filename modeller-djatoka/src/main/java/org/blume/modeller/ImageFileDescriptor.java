package org.blume.modeller;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImageFileDescriptor {

    private String id;

    @XmlElement
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}