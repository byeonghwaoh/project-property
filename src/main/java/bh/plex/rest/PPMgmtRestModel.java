package bh.plex.rest;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class PPMgmtRestModel {

    @XmlElement
    private Integer id;

    @XmlElement
    private String prjId;

    @XmlElement
    private String pKey;

    @XmlElement
    private String pValue;


    public PPMgmtRestModel() {
    }

    public PPMgmtRestModel(Integer id, String prjId, String pKey, String pValue) {
        this.id = id;
        this.prjId = prjId;
        this.pKey = pKey;
        this.pValue = pValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrjId() {
        return prjId;
    }

    public void setPrjId(String prjId) {
        this.prjId = prjId;
    }

    public String getpKey() {
        return pKey;
    }

    public void setpKey(String pKey) {
        this.pKey = pKey;
    }

    public String getpValue() {
        return pValue;
    }

    public void setpValue(String pValue) {
        this.pValue = pValue;
    }
}