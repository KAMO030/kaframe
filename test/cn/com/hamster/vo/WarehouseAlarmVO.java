package cn.com.hamster.vo;


import cn.com.hamster.bean.WarehouseAlarm;

import java.io.Serializable;

/**
 * 库存报警VO
 */
public class WarehouseAlarmVO  implements Serializable {
	/**
	 * 库存报警
	 */
	private WarehouseAlarm warehouseAlarm;
	/**
	 * 商品
	 */
	private CommodityInfoVO commodityInfoVO;

	public WarehouseAlarm getWarehouseAlarm() {
		return warehouseAlarm;
	}

	public void setWarehouseAlarm(WarehouseAlarm warehouseAlarm) {
		this.warehouseAlarm = warehouseAlarm;
	}

	public CommodityInfoVO getCommodityInfoVO() {
		return commodityInfoVO;
	}

	public void setCommodityInfoVO(CommodityInfoVO commodityInfoVO) {
		this.commodityInfoVO = commodityInfoVO;
	}
}
