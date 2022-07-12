package test.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (Stu)实体类
 *
 * @author KAMOsama
 * @since 2022-07-07 20:31:14
 */
public class Stu implements Serializable {
    
    private String sid;
    
    private String sno;
    
    private String sname;
    
    private Integer ssex;
    
    private Date sbrith;

    @Override
    public String toString() {
        return "Stu{" +
                "sid='" + sid + '\'' +
                ", sno='" + sno + '\'' +
                ", sname='" + sname + '\'' +
                ", ssex=" + ssex +
                ", sbrith=" + sbrith +
                '}';
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getSsex() {
        return ssex;
    }

    public void setSsex(Integer ssex) {
        this.ssex = ssex;
    }

    public Date getSbrith() {
        return sbrith;
    }

    public void setSbrith(Date sbrith) {
        this.sbrith = sbrith;
    }

}

