package cn.com.hamster.vo;


import cn.com.hamster.bean.OperatorAuthority;
import cn.com.hamster.bean.OperatorImpower;
import cn.com.hamster.bean.Operators;

/**
 * 授权VO
 */
public class OperatorImpowerVO {
	/**
	 * 授权
	 */
	private OperatorImpower operatorImpower;
	/**
	 * 操作权限
	 */
	private OperatorAuthority operatorAuthority;
	/**
	 *操作员 
	 */
	private Operators operators;
	public OperatorAuthority getOperatorAuthority() {
		return operatorAuthority;
	}
	public void setOperatorAuthority(OperatorAuthority operatorAuthority) {
		this.operatorAuthority = operatorAuthority;
	}
	public Operators getOperators() {
		return operators;
	}
	public void setOperators(Operators operators) {
		this.operators = operators;
	}

	public OperatorImpower getOperatorImpower() {
		return operatorImpower;
	}

	public void setOperatorImpower(OperatorImpower operatorImpower) {
		this.operatorImpower = operatorImpower;
	}
}
