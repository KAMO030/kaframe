package cn.com.hamster.bean;




import java.io.Serializable;
import java.sql.Date;

/**
 * 出库
 */
public class ExwarehouseOut  implements Serializable {
	/**
	 * 出库单号
	 */
	private int eqId;
	/**
	 * 商品编号
	 */
	private int ciId;
	/**
	 * 出库日期
	 */
	private Date eqProduceDate;
	/**
	 * 单位编号
	 */
	private int duId;
	/**
	 * 出库类型编号
	 */
	private int ehTypeid;
	/**
	 * 仓库编号
	 */
	private int wId;
	/**
	 * 员工编号
	 */
	private int sId;
	/**
	 * 操作员编号
	 */
	private int oId;
	/**
	 * 数量
	 */
	private int eqCount;
	/**
	 * 备注
	 */
	private String eqNote;

	public ExwarehouseOut() {
	}

	public ExwarehouseOut(int eqId, int ciId, Date eqProduceDate, int duId, int ehTypeid, int wId, int sId, int oId, int eqCount, String eqNote) {
		this.eqId = eqId;
		this.ciId = ciId;
		this.eqProduceDate = eqProduceDate;
		this.duId = duId;
		this.ehTypeid = ehTypeid;
		this.wId = wId;
		this.sId = sId;
		this.oId = oId;
		this.eqCount = eqCount;
		this.eqNote = eqNote;
	}

	public int getEqId() {
		return eqId;
	}
	public void setEqId(int eqId) {
		this.eqId = eqId;
	}
	public int getCiId() {
		return ciId;
	}
	public void setCiId(int ciId) {
		this.ciId = ciId;
	}

	public Date getEqProduceDate() {
		return eqProduceDate;
	}

	public void setEqProduceDate(Date eqProduceDate) {
		this.eqProduceDate = eqProduceDate;
	}

	public int getoId() {
		return oId;
	}

	public void setoId(int oId) {
		this.oId = oId;
	}

	public int getDuId() {
		return duId;
	}
	public void setDuId(int duId) {
		this.duId = duId;
	}
	
	public int getEhTypeid() {
		return ehTypeid;
	}
	public void setEhTypeid(int ehTypeid) {
		this.ehTypeid = ehTypeid;
	}
	public int getwId() {
		return wId;
	}
	public void setwId(int wId) {
		this.wId = wId;
	}
	public int getsId() {
		return sId;
	}
	public void setsId(int sId) {
		this.sId = sId;
	}
	public int getEqCount() {
		return eqCount;
	}
	public void setEqCount(int eqCount) {
		this.eqCount = eqCount;
	}
	public String getEqNote() {
		return eqNote;
	}
	public void setEqNote(String eqNote) {
		this.eqNote = eqNote;
	}
	
}
