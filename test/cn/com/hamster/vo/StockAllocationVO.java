package cn.com.hamster.vo;


import cn.com.hamster.bean.Operators;
import cn.com.hamster.bean.StaffInfo;
import cn.com.hamster.bean.StockAllocation;
import cn.com.hamster.bean.Warehouse;

import java.io.Serializable;

/**
 *库存调拨VO
 */
public class StockAllocationVO  implements Serializable {
	/**
	 *库存调拨
	 */
	private StockAllocation stockAllocation;
	/**
	 *调入仓库 
	 */
	private Warehouse warehouseIn;
	/**
	 * 调出仓库
	 */
	private Warehouse warehouseOut;
	/**
	 * 商品
	 */
	private CommodityInfoVO commodityInfoVO;
	/**
	 * 员工
	 */
	private StaffInfo staffInfo;
	private Operators operators;

	public StockAllocation getStockAllocation() {
		return stockAllocation;
	}

	public void setStockAllocation(StockAllocation stockAllocation) {
		this.stockAllocation = stockAllocation;
	}

	public Warehouse getWarehouseIn() {
		return warehouseIn;
	}

	public void setWarehouseIn(Warehouse warehouseIn) {
		this.warehouseIn = warehouseIn;
	}

	public Warehouse getWarehouseOut() {
		return warehouseOut;
	}

	public void setWarehouseOut(Warehouse warehouseOut) {
		this.warehouseOut = warehouseOut;
	}

	public CommodityInfoVO getCommodityInfoVO() {
		return commodityInfoVO;
	}

	public void setCommodityInfoVO(CommodityInfoVO commodityInfoVO) {
		this.commodityInfoVO = commodityInfoVO;
	}

	public StaffInfo getStaffInfo() {
		return staffInfo;
	}

	public void setStaffInfo(StaffInfo staffInfo) {
		this.staffInfo = staffInfo;
	}

	public Operators getOperators() {
		return operators;
	}

	public void setOperators(Operators operators) {
		this.operators = operators;
	}
}
