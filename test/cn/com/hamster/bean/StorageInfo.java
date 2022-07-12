package cn.com.hamster.bean;



import java.io.Serializable;

/**
 * 存储
 */
public class StorageInfo implements Serializable {
	/**
	 * 存储编号
	 */
	private int sId;
	/**
	 * 仓库编号
	 */
	private int wId;
	/**
	 * 商品编号
	 */
	private int ciId;
	/**
	 * 出库数量
	 */
	private int sComout;
	/**
	 * 入库数量
	 */
	private int sComin;

	public StorageInfo(int sId, int wId, int ciId, int sComout, int sComin) {
		this.sId = sId;
		this.wId = wId;
		this.ciId = ciId;
		this.sComout = sComout;
		this.sComin = sComin;
	}

	public StorageInfo() {
	}

	public int getsId() {
		return sId;
	}
	public void setsId(int sId) {
		this.sId = sId;
	}
	public int getwId() {
		return wId;
	}
	public void setwId(int wId) {
		this.wId = wId;
	}
	public int getCiId() {
		return ciId;
	}
	public void setCiId(int ciId) {
		this.ciId = ciId;
	}
	public int getsComout() {
		return sComout;
	}
	public void setsComout(int sComout) {
		this.sComout = sComout;
	}
	public int getsComin() {
		return sComin;
	}
	public void setsComin(int sComin) {
		this.sComin = sComin;
	}
	
}
