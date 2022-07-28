package mapper_test.test.vo;


import mapper_test.test.pojo.Cinfo;
import mapper_test.test.pojo.Ctype;
import mapper_test.test.pojo.Cunit;

public class CinfoVO {
    private String cId;
    private String cName;
    private String tName;
    private String tId;
    private String uName;
    private String uId;
    private String cSpace;//规格
    private String cNo;//备注

    public CinfoVO() {
    }

    public CinfoVO(Cinfo cinfo, Ctype ctype, Cunit cunit) {
        this.cId = cinfo.getcId();
        this.cName = cinfo.getcName();
        this.tName = ctype.gettName();
        this.uName = cunit.getuName();
        this.cSpace = cinfo.getcSpace();
        this.cNo = cinfo.getcNo();
        this.tId = cinfo.gettId();
        this.uId = cinfo.getuId();
    }

    public String gettId() {
        return tId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
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


    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
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
        return "CinfoVO{" +
                "cId='" + cId + '\'' +
                ", cName='" + cName + '\'' +
                ", tName='" + tName + '\'' +
                ", tId='" + tId + '\'' +
                ", uName='" + uName + '\'' +
                ", uId='" + uId + '\'' +
                ", cSpace='" + cSpace + '\'' +
                ", cNo='" + cNo + '\'' +
                '}';
    }
}
