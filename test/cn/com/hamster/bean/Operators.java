package cn.com.hamster.bean;



import java.io.Serializable;

/**
 * 操作员信息表
 */
public class Operators implements Serializable {
	/**
	 * 操作员编号
	 */
	private int oId;
	/**
	 * 操作员用户名
	 */
	private String oName;
	/**
	 * 操作员密码
	 */
	private String oPassword;

	public Operators(int oId, String oName, String oPassword) {
		this.oId = oId;
		this.oName = oName;
		this.oPassword = oPassword;
	}

	public Operators() {
	}

	public int getoId() {
		return oId;
	}
	public void setoId(int oId) {
		this.oId = oId;
	}
	public String getoName() {
		return oName;
	}
	public void setoName(String oName) {
		this.oName = oName;
	}
	public String getoPassword() {
		return oPassword;
	}
	public void setoPassword(String oPassword) {
		this.oPassword = oPassword;
	}
	
}
