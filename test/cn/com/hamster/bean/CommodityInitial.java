package cn.com.hamster.bean;



import java.io.Serializable;

/**
 * 期初建账
 */
public class CommodityInitial implements Serializable {
	/**
	 * 商品编号
	 */
	private int cnId;
	/**
	 * 存放仓库
	 */
	private String cnWareHouse;
	/**
	 * 库存数量
	 */
	private int cnCount;
	/**
	 * 备注
	 */
	private String cnNote;

	public CommodityInitial(int cnId, String cnWareHouse, int cnCount, String cnNote) {
		this.cnId = cnId;
		this.cnWareHouse = cnWareHouse;
		this.cnCount = cnCount;
		this.cnNote = cnNote;
	}

	public CommodityInitial() {
	}

	public int getCnId() {
		return cnId;
	}
	public void setCnId(int cnId) {
		this.cnId = cnId;
	}
	public String getCnWareHouse() {
		return cnWareHouse;
	}
	public void setCnWareHouse(String cnWareHouse) {
		this.cnWareHouse = cnWareHouse;
	}
	public int getCnCount() {
		return cnCount;
	}
	public void setCnCount(int cnCount) {
		this.cnCount = cnCount;
	}
	public String getCnNote() {
		return cnNote;
	}
	public void setCnNote(String cnNote) {
		this.cnNote = cnNote;
	}
	
}
