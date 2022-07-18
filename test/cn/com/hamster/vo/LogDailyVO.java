package cn.com.hamster.vo;


import cn.com.hamster.bean.Operators;
import cn.com.hamster.bean.LogDaily;

import java.io.Serializable;

/**
 * 日志VO
 */
public class LogDailyVO  implements Serializable {
	/**
	 * 日志
	 */
	private LogDaily logDaily;
	/**
	 * 操作员
	 */
	private Operators operators;

	public LogDaily getLogDaily() {
		return logDaily;
	}

	public void setLogDaily(LogDaily logDaily) {
		this.logDaily = logDaily;
	}

	public Operators getOperators() {
		return operators;
	}

	public void setOperators(Operators operators) {
		this.operators = operators;
	}
}
