package cn.com.hamster.bean;


import java.io.Serializable;
import java.util.Objects;

/**
 * 往来单位信息表
 */
public class DealUnit  implements Serializable {
	/**
	 * 单位编号
	 */
	private int duId;
	/**
	 * 单位名称
	 */
	private String duName;
	/**
	 * 单位类别编号
	 */
	private int utClient;
	/**
	 * 联系电话
	 */
	private String duTelephone;
	/**
	 * 联系地址
	 */
	private String duAddress;
	/**
	 * 禁用状态
	 */
	private int duDefault;
	/**
	 * 默认单位
	 */
	private int duDefunit;
	/**
	 * 备注
	 */
	private String duNote;

	public DealUnit() {
	}

	public DealUnit(int duId, String duName, int utClient, String duTelephone, String duAddress, int duDefault, int duDefunit, String duNote) {
		this.duId = duId;
		this.duName = duName;
		this.utClient = utClient;
		this.duTelephone = duTelephone;
		this.duAddress = duAddress;
		this.duDefault = duDefault;
		this.duDefunit = duDefunit;
		this.duNote = duNote;
	}

	public int getDuId() {
		return duId;
	}
	public void setDuId(int duId) {
		this.duId = duId;
	}
	public String getDuName() {
		return duName;
	}
	public void setDuName(String duName) {
		this.duName = duName;
	}
	public int getUtClient() {
		return utClient;
	}
	public void setUtClient(int utClient) {
		this.utClient = utClient;
	}
	public String getDuTelephone() {
		return duTelephone;
	}

	public void setDuTelephone(String duTelephone) {
		this.duTelephone = duTelephone;
	}
	public String getDuAddress() {
		return duAddress;
	}
	public void setDuAddress(String duAddress) {
		this.duAddress = duAddress;
	}
	public int getDuDefault() {
		return duDefault;
	}
	public void setDuDefault(int duDefault) {
		this.duDefault = duDefault;
	}
	public int getDuDefunit() {
		return duDefunit;
	}
	public void setDuDefunit(int duDefunit) {
		this.duDefunit = duDefunit;
	}
	public String getDuNote() {
		return duNote;
	}
	public void setDuNote(String duNote) {
		this.duNote = duNote;
	}

	@Override
	public String toString() {
		return "DealUnit{" +
				"duName='" + duName + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DealUnit)) {
			return false;
		}
		DealUnit dealUnit = (DealUnit) o;
		return duId == dealUnit.duId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(duId);
	}
}
