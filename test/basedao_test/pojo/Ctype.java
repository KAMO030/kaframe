package basedao_test.pojo;

import java.io.Serializable;

/**
 * (Ctype)实体类
 *
 * @author KAMOsama
 * @since 2022-07-11 22:00:06
 */
public class Ctype implements Serializable {

    private String tId;
    
    private String tName;


    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

}

