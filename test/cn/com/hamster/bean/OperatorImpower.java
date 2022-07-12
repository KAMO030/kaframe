package cn.com.hamster.bean;


import java.io.Serializable;

/**
 * 授权
 */
public class OperatorImpower implements Serializable {
	/**
	 *授权编号 
	 */

	private int opId;
	/**
	 * 操作员编号
	 */
	private int oId;
	/**
	 * 操作权限编号
	 */
	private int oaId;

	public OperatorImpower(int opId, int oId, int oaId) {
		this.opId = opId;
		this.oId = oId;
		this.oaId = oaId;
	}

	public OperatorImpower() {
	}

	public int getOpId() {
		return opId;
	}
	public void setOpId(int opId) {
		this.opId = opId;
	}
	public int getoId() {
		return oId;
	}
	public void setoId(int oId) {
		this.oId = oId;
	}
	public int getOaId() {
		return oaId;
	}
	public void setOaId(int oaId) {
		this.oaId = oaId;
	}
	
}
