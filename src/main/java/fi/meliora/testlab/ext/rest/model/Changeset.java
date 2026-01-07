package fi.meliora.testlab.ext.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A VCS Changeset in Testlab project.
 *
 * @author Marko Kanala, Meliora Ltd (marko.kanala@meliora.fi)
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Changeset extends ModelObject {
    public static final int TYPE_GIT = 1;
    public static final int TYPE_HG = 2;

    /**
     * Identifying hash for the Changeset.
     */
    private String identifier;

    /**
     * Changeset type, TYPE_GIT or TYPE_HG.
     */
    private int type;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
