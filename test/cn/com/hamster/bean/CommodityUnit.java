package cn.com.hamster.bean;


import java.io.Serializable;

/**
 * 商品单位表
 */
public class CommodityUnit implements Serializable {
	/**
	 * 商品单位编号
	 */
	private int cuId;
	/**
	 * 商品单位名称
	 */
	private String cuHe;

	public CommodityUnit() {
	}

	public CommodityUnit(int cuId, String cuHe) {
		this.cuId = cuId;
		this.cuHe = cuHe;
	}

	public int getCuId() {
		return cuId;
	}

	public void setCuId(int cuId) {
		this.cuId = cuId;
	}

	public String getCuHe() {
		return cuHe;
	}
	public void setCuHe(String cuHe) {
		this.cuHe = cuHe;
	}




}
