package cn.com.hamster.vo;


import cn.com.hamster.bean.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 入库VO
 */
public class WarehouseInVO  implements Serializable {

	/**
	 * 入库
	 */
	private WarehouseIn warehouseIn;
	/**
	 * 单位VO
	 */
	private DealUnitVO dealUnitVO;
	/**
	 * 商品VO
	 */
	private CommodityInfoVO commodityInfoVO;
	/**
	 * 仓库
	 */
	private Warehouse warehouse;
	/**
	 * 入库类型
	 */
	private WarehouseType warehouseType;
	/**
	 * 员工
	 */
	private StaffInfo staffInfo;
	/**
	 * 操作员
	 */
	private Operators operators;

	public WarehouseIn getWarehouseIn() {
		return warehouseIn;
	}

	public void setWarehouseIn(WarehouseIn warehouseIn) {
		this.warehouseIn = warehouseIn;
	}

	public DealUnitVO getDealUnitVO() {
		return dealUnitVO;
	}

	public Operators getOperators() {
		return operators;
	}

	public void setOperators(Operators operators) {
		this.operators = operators;
	}

	public void setDealUnitVO(DealUnitVO dealUnitVO) {
		this.dealUnitVO = dealUnitVO;
	}

	public CommodityInfoVO getCommodityInfoVO() {
		return commodityInfoVO;
	}

	public void setCommodityInfoVO(CommodityInfoVO commodityInfoVO) {
		this.commodityInfoVO = commodityInfoVO;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public WarehouseType getWarehouseType() {
		return warehouseType;
	}

	public void setWarehouseType(WarehouseType warehouseType) {
		this.warehouseType = warehouseType;
	}

	public StaffInfo getStaffInfo() {
		return staffInfo;
	}

	@Override
	public String toString() {
		return   "RK"+ new SimpleDateFormat("YYYYMMDD").format( new Date(warehouseIn.getWqDate().getTime()))+warehouseIn.getWqId()+"";
	}

	public void setStaffInfo(StaffInfo staffInfo) {
		this.staffInfo = staffInfo;
	}
}
