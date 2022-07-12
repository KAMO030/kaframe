package cn.com.hamster.vo;


import cn.com.hamster.bean.CommodityClasses;
import cn.com.hamster.bean.CommodityInfo;
import cn.com.hamster.bean.CommodityUnit;

import java.io.Serializable;

/**
 * 商品信息vo
 */
public class CommodityInfoVO  implements Serializable {
	/**
	 * 商品
	 */
	private CommodityInfo commodityInfo;
	/**
	 * 商品类别
	 */
	private CommodityClasses commdClasses;
	/**
	 * 商品单位
	 */
	private CommodityUnit commodityUnit;

	public CommodityInfo getCommodityInfo() {
		return commodityInfo;
	}

	public void setCommodityInfo(CommodityInfo commodityInfo) {
		this.commodityInfo = commodityInfo;
	}

	public CommodityClasses getCommdClasses() {
		return commdClasses;
	}

	public void setCommdClasses(CommodityClasses commdClasses) {
		this.commdClasses = commdClasses;
	}

	public CommodityUnit getCommodityUnit() {
		return commodityUnit;
	}

	public void setCommodityUnit(CommodityUnit commodityUnit) {
		this.commodityUnit = commodityUnit;
	}

	@Override
	public String toString() {
		return commodityInfo.toString();
	}
}
