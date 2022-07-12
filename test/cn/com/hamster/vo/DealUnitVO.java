package cn.com.hamster.vo;


import cn.com.hamster.bean.DealUnit;
import cn.com.hamster.bean.UnitType;

import java.io.Serializable;

/**
 * 往来单位VO
 */
public class DealUnitVO  implements Serializable {
public static Long serialVersionUID = 3213L;

/**
 * 往来单位
 */
	private DealUnit dealUnit;

	/**
	 * 单位类别
	 */
	private UnitType unitType;

	public DealUnit getDealUnit() {
		return dealUnit;
	}

	public void setDealUnit(DealUnit dealUnit) {
		this.dealUnit = dealUnit;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	@Override
	public String toString() {
		return dealUnit.toString();
	}
}
