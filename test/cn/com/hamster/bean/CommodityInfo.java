package cn.com.hamster.bean;



import java.io.Serializable;

/**
 * 商品信息表
 */
public class CommodityInfo  implements Serializable {
	/**
	 * 商品编号
	 */

   private int ciId;
   /**
	* 商品名称
	*/
   private String ciName;
   /**
	* 商品类别编号
	*/
   private int clId;
   /**
	* 商品条码
	*/
   private String ciBarCode;
   /**
	* 商品单位编号
	*/
   private int cuId;
   /**
	* 规格
	*/
   private String ciSpecs;
   /**
	* 禁用状态
	*/
   private int ciDisable;
   /**
	* 库存上限
	*/
   private int ciTopLimit;
   /**
	* 库存下限
	*/
   private int ciLowerLimit;
   /**
	* 备注
	*/
   private String ciNote;

	public CommodityInfo() {
	}

	public CommodityInfo(int ciId, String ciName, int clId, String ciBarCode, int duId, String ciSpecs, int ciDisable, int ciTopLimit, int ciLowerLimit, String ciNote) {
		this.ciId = ciId;
		this.ciName = ciName;
		this.clId = clId;
		this.ciBarCode = ciBarCode;
		this.cuId = duId;
		this.ciSpecs = ciSpecs;
		this.ciDisable = ciDisable;
		this.ciTopLimit = ciTopLimit;
		this.ciLowerLimit = ciLowerLimit;
		this.ciNote = ciNote;
	}

	public int getCiId() {
		return ciId;
	}
	public void setCiId(int ciId) {
		this.ciId = ciId;
	}
	public String getCiName() {
		return ciName;
	}
	public void setCiName(String ciName) {
		this.ciName = ciName;
	}
	public int getClId() {
		return clId;
	}
	public void setClId(int clId) {
		this.clId = clId;
	}
	public String getCiBarCode() {
		return ciBarCode;
	}
	public void setCiBarCode(String ciBarCode) {
		this.ciBarCode = ciBarCode;
	}
	public int getCuId() {
		return cuId;
	}
	public void setCuId(int cuId) {
		this.cuId = cuId;
	}
	public String getCiSpecs() {
		return ciSpecs;
	}
	public void setCiSpecs(String ciSpecs) {
		this.ciSpecs = ciSpecs;
	}

	public int getCiDisable() {
		return ciDisable;
	}

	public void setCiDisable(int ciDisable) {
		this.ciDisable = ciDisable;
	}

	public int getCiTopLimit() {
		return ciTopLimit;
	}

	public void setCiTopLimit(int ciTopLimit) {
		this.ciTopLimit = ciTopLimit;
	}

	public int getCiLowerLimit() {
		return ciLowerLimit;
	}

	public void setCiLowerLimit(int ciLowerLimit) {
		this.ciLowerLimit = ciLowerLimit;
	}

	public String getCiNote() {
		return ciNote;
	}
	public void setCiNote(String ciNote) {
		this.ciNote = ciNote;
	}
	   
	
   
}

