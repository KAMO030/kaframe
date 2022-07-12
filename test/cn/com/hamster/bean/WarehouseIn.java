package cn.com.hamster.bean;




import java.io.Serializable;
import java.sql.Date;

/**
 * 入库
 */
public class WarehouseIn implements Serializable {
    /**
     * 入库单号
     */

    private int wqId;
    /**
     * 入库时间
     */
    private Date wqDate;
    /**
     * 往来单位编号
     */
    private int duId;
    /**
     * 商品编号
     */
    private int ciId;
    /**
     * 仓库编号
     */
    private int wId;
    /**
     * 数量
     */
    private int wqCount;
    /**
     * 入库类型编号
     */
    private int whTypeid;
    /**
     * 员工编号
     */
    private int sId;
    /**
     * 操作员编号
     */
    private int oId;
    /**
     * 备注
     */
    private String wqNote;

    public WarehouseIn() {
    }

    public WarehouseIn(int wqId, Date wqDate, int duId, int ciId, int wId, int wqCount, int whTypeid, int sId, int oId, String wqNote) {
        this.wqId = wqId;
        this.wqDate = wqDate;
        this.duId = duId;
        this.ciId = ciId;
        this.wId = wId;
        this.wqCount = wqCount;
        this.whTypeid = whTypeid;
        this.sId = sId;
        this.oId = oId;
        this.wqNote = wqNote;
    }

    public int getWqId() {
        return wqId;
    }

    public void setWqId(int wqId) {
        this.wqId = wqId;
    }

    public Date getWqDate() {
        return wqDate;
    }

    public void setWqDate(Date wqDate) {
        this.wqDate = wqDate;
    }

    public int getDuId() {
        return duId;
    }

    public void setDuId(int duId) {
        this.duId = duId;
    }

    public int getCiId() {
        return ciId;
    }

    public void setCiId(int ciId) {
        this.ciId = ciId;
    }

    public int getwId() {
        return wId;
    }

    public void setwId(int wId) {
        this.wId = wId;
    }

    public int getWqCount() {
        return wqCount;
    }

    public void setWqCount(int wqCount) {
        this.wqCount = wqCount;
    }

    public int getWhTypeid() {
        return whTypeid;
    }

    public void setWhTypeId(int whTypeid) {
        this.whTypeid = whTypeid;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public String getWqNote() {
        return wqNote;
    }

    public void setWqNote(String wqNote) {
        this.wqNote = wqNote;
    }

    public void setWhTypeid(int whTypeid) {
        this.whTypeid = whTypeid;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }
}
