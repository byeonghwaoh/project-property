package bh.plex.ao;

import net.java.ao.Preload;
import net.java.ao.Entity;

@Preload
public interface Property extends Entity {

    String getPrjId();
    void setPrjId(String prjId);

    String getPKey();
    void setPKey(String pKey);

    String getPValue();
    void setPValue(String pValue);
}
