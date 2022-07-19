package basedao_test.service.pojo;

import java.io.Serializable;

/**
 * (Cinfo)实体类
 *
 * @author KAMOsama
 * @since 2022-07-11 21:58:21
 */
public class Cinfo implements Serializable {

    private String cId;

    private String cName;

    private String tId;

    private String uId;

    private String cSpace;

    private String cNo;



    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getcSpace() {
        return cSpace;
    }

    public void setcSpace(String cSpace) {
        this.cSpace = cSpace;
    }

    public String getcNo() {
        return cNo;
    }

    public void setcNo(String cNo) {
        this.cNo = cNo;
    }

    @Override
    public String toString() {
        return "Cinfo{" +
                "cId='" + cId + '\'' +
                ", cName='" + cName + '\'' +
                ", tId='" + tId + '\'' +
                ", uId='" + uId + '\'' +
                ", cSpace='" + cSpace + '\'' +
                ", cNo='" + cNo + '\'' +
                '}';
    }
}

