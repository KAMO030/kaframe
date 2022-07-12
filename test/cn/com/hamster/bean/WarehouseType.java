package cn.com.hamster.bean;


import java.io.Serializable;

/**
 * 入库类型
 */
public class WarehouseType  implements Serializable {
	/**
	 * 入库类型编号
	 */

	private int whTypeid;
	/**
	 * 入库类型名称
	 */

	private String whTypename;

	public WarehouseType(int whTypeid, String whTypename) {
		this.whTypeid = whTypeid;
		this.whTypename = whTypename;
	}

	public WarehouseType() {
	}

	public int getWhTypeid() {
		return whTypeid;
	}
	public void setWhTypeid(int whTypeid) {
		this.whTypeid = whTypeid;
	}
	public String getWhTypename() {
		return whTypename;
	}
	public void setWhTypename(String whTypename) {
		this.whTypename = whTypename;
	}
	
	
	
}
