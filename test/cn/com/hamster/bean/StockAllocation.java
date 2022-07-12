package cn.com.hamster.bean;



import java.io.Serializable;
import java.sql.Date;

/**
 * 库存调拨
 */
public class StockAllocation  implements Serializable {
	/**
	 * 单号
	 */
	private int saId;
	/**
	 * 调拨日期
	 */
	private Date saDate;
	/**
	 调出仓库编号
	 */
	private int wIdOut;
	/**
	 * 调入仓库编号
	 */
	private int wIdIn;
	/**
	 * 商品编号
	 */
	private int ciId;
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
	private int saCount;
	/**
	 * 备注
	 */
	private String saNote;

	public StockAllocation(int saId, Date saString, int wIdOut, int wIdIn, int ciId, int sId, int oId, int saCount, String saNote) {
		this.saId = saId;
		this.saDate = saString;
		this.wIdOut = wIdOut;
		this.wIdIn = wIdIn;
		this.ciId = ciId;
		this.sId = sId;
		this.oId = oId;
		this.saCount = saCount;
		this.saNote = saNote;
	}

	public StockAllocation() {
	}

	public int getSaId() {
		return saId;
	}

	public void setSaId(int saId) {
		this.saId = saId;
	}

	public Date getSaDate() {
		return saDate;
	}

	public void setSaDate(Date saDate) {
		this.saDate = saDate;
	}

	public int getwIdOut() {
		return wIdOut;
	}

	public void setwIdOut(int wIdOut) {
		this.wIdOut = wIdOut;
	}

	public int getwIdIn() {
		return wIdIn;
	}

	public void setwIdIn(int wIdIn) {
		this.wIdIn = wIdIn;
	}

	public int getoId() {
		return oId;
	}

	public void setoId(int oId) {
		this.oId = oId;
	}

	public int getCiId() {
		return ciId;
	}

	public void setCiId(int ciId) {
		this.ciId = ciId;
	}

	public int getsId() {
		return sId;
	}

	public void setsId(int sId) {
		this.sId = sId;
	}

	public int getSaCount() {
		return saCount;
	}

	public void setSaCount(int saCount) {
		this.saCount = saCount;
	}

	public String getSaNote() {
		return saNote;
	}

	public void setSaNote(String saNote) {
		this.saNote = saNote;
	}
}
