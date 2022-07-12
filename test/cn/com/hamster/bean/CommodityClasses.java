package cn.com.hamster.bean;



import java.io.Serializable;
import java.util.Objects;

/**
 * 商品类别表
 */
public class CommodityClasses implements Serializable {
    /**
	 * 商品类别编号
	 */
	private int clId;
	/**
	 * 商品类别名称
	 */
	private String clName;

	public CommodityClasses() {
	}

	public CommodityClasses(int clId, String clName) {
		this.clId = clId;
		this.clName = clName;
	}

	public int getClId() {
		return clId;
	}
	public void setClId(int clId) {
		this.clId = clId;
	}
	public String getClName() {
		return clName;
	}
	public void setClName(String clName) {
		this.clName = clName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CommodityClasses)) {
			return false;
		}
		CommodityClasses classes = (CommodityClasses) o;
		return clId == classes.clId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clId);
	}
}
