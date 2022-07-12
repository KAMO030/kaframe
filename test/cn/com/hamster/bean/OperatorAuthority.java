package cn.com.hamster.bean;


import java.io.Serializable;

/**
 * 操作权限表
 */
public class OperatorAuthority implements Serializable {
	/**
	 * 操作权限编号
	 */
	private int oaId;
	/**
	 * 操作权限名称
	 */
	private String oaName;

	public OperatorAuthority(int oaId, String oaName) {
		this.oaId = oaId;
		this.oaName = oaName;
	}

	public OperatorAuthority() {
	}

	public int getOaId() {
		return oaId;
	}
	public void setOaId(int oaId) {
		this.oaId = oaId;
	}
	public String getOaName() {
		return oaName;
	}
	public void setOaName(String oaName) {
		this.oaName = oaName;
	}
	
}
