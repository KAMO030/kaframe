package cn.com.hamster.vo;


import cn.com.hamster.bean.StorageInfo;
import cn.com.hamster.bean.Warehouse;

import java.io.Serializable;

/**
 * 存储VO
 */
public class StorageInfoVO implements Serializable {
	/**
	 * 存储
	 */
	private StorageInfo storageInfo;
	/**
	 * 仓库
	 */
	private Warehouse warehouse;
	/**
	 * 商品
	 */
	private CommodityInfoVO commdInfoVO;


	public StorageInfo getStorageInfo() {
		return storageInfo;
	}

	public void setStorageInfo(StorageInfo storageInfo) {
		this.storageInfo = storageInfo;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public CommodityInfoVO getCommdInfoVO() {
		return commdInfoVO;
	}

	@Override
	public String toString() {
		return commdInfoVO.toString();
	}

	public void setCommdInfoVO(CommodityInfoVO commdInfoVO) {
		this.commdInfoVO = commdInfoVO;
	}
}
