package mapper_test.test.pojo;

import java.io.Serializable;

/**
 * (Cunit)实体类
 *
 * @author KAMOsama
 * @since 2022-07-11 22:06:59
 */
public class Cunit implements Serializable {

    private String uId;
    
    private String uName;


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

}

