package cn.com.hamster.vo;


import cn.com.hamster.bean.*;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 出库VO
 */
public class ExwarehouseOutVO  implements Serializable {
	/**
	 * 出库
	 */
	private ExwarehouseOut exwarehouseOut;
	/**
	 * 商品VO
	 */
	private CommodityInfoVO commodityInfoVO;
	/**
	 * 单位VO
	 */
	private DealUnitVO dealUnitVO;
	/**
	 * 出库类型
	 */
	private WarehouseType warehouseType;
	/**
	 * 仓库
	 */
	private Warehouse warehouse;
	/**
	 * 员工
	 */
	private StaffInfo staffInfo;
	private Operators operators;

	public ExwarehouseOut getExwarehouseOut() {
		return exwarehouseOut;
	}

	public void setExwarehouseOut(ExwarehouseOut exwarehouseOut) {
		this.exwarehouseOut = exwarehouseOut;
	}

	public Operators getOperators() {
		return operators;
	}

	public void setOperators(Operators operators) {
		this.operators = operators;
	}


	public CommodityInfoVO getCommodityInfoVO() {
		return commodityInfoVO;
	}

	public void setCommodityInfoVO(CommodityInfoVO commodityInfoVO) {
		this.commodityInfoVO = commodityInfoVO;
	}

	public DealUnitVO getDealUnitVO() {
		return dealUnitVO;
	}

	public void setDealUnitVO(DealUnitVO dealUnitVO) {
		this.dealUnitVO = dealUnitVO;
	}

	public WarehouseType getWarehouseType() {
		return warehouseType;
	}
	public void setWarehouseType(WarehouseType warehouseType) {
		this.warehouseType = warehouseType;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	public StaffInfo getStaffInfo() {
		return staffInfo;
	}
	public void setStaffInfo(StaffInfo staffInfo) {
		this.staffInfo = staffInfo;
	}

	@Override
	public String toString() {
		return "CK"+ new SimpleDateFormat("YYYYMMDD").format( new Date(exwarehouseOut.getEqProduceDate().getTime()))+exwarehouseOut.getEqId();
	}
}
