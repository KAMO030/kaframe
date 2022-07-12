package cn.com.hamster.bean;



import java.io.Serializable;

/**
 * 库存报警
 */
public class WarehouseAlarm implements Serializable {
	/**
	 * 编号
	 */
	private int whId;
	/**
	 * 商品编号
	 */
	private int ciId;

	public WarehouseAlarm() {
	}

	public WarehouseAlarm(int whId, int ciId) {
		this.whId = whId;
		this.ciId = ciId;
	}

	public int getWhId() {
		return whId;
	}
	public void setWhId(int whId) {
		this.whId = whId;
	}
	public int getCiId() {
		return ciId;
	}
	public void setCiId(int ciId) {
		this.ciId = ciId;
	}
	
}
