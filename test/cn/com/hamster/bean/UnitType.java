package cn.com.hamster.bean;


import java.io.Serializable;

/**
 * 往来单位类别
 */
public class UnitType implements Serializable {
	/**
	 * 类别编号
	 */
	 private int utClient;
	 /**
	  * 类别名称
	  */
	 private String utSupply;

	public UnitType() {
	}

	public UnitType(int utClient, String utSupply) {
		this.utClient = utClient;
		this.utSupply = utSupply;
	}

	public int getUtClient() {
		return utClient;
	}
	public void setUtClient(int utClient) {
		this.utClient = utClient;
	}
	public String getUtSupply() {
		return utSupply;
	}
	public void setUtSupply(String utSupply) {
		this.utSupply = utSupply;
	}
 
}
