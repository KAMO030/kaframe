package cn.com.hamster.bean;



import java.io.Serializable;

/**
 * 出库类型
 */
public class ExwarehouseType implements Serializable {
	/**
	 * 出库类型编号
	 */
	private int ehTypeid;
	/**
	 * 出库类型名称
	 */
	private String ehTypename;

	public ExwarehouseType() {
	}

	public ExwarehouseType(int ehTypeid, String ehTypename) {
		this.ehTypeid = ehTypeid;
		this.ehTypename = ehTypename;
	}

	public int getEhTypeid() {
		return ehTypeid;
	}
	public void setEhTypeid(int ehTypeid) {
		this.ehTypeid = ehTypeid;
	}
	public String getEhTypename() {
		return ehTypename;
	}
	public void setEhTypename(String ehTypename) {
		this.ehTypename = ehTypename;
	}
	
	
	
}
