package cn.com.hamster.bean;
//


import java.io.Serializable;

/**
 * 仓库信息表
 */
public class Warehouse   implements Serializable {
	/**
	 * 仓库编号
	 */

	private int wId;
	/**
	 * 仓库名称
	 */

	private String wName;

	/**
	 * 默认仓库
	 */
	private int wDefault;

	public Warehouse(int wId, String wName, int wDefault) {
		this.wId = wId;
		this.wName = wName;
		this.wDefault = wDefault;
	}

	public Warehouse() {
	}

	public int getwId() {
		return wId;
	}
	public void setwId(int wId) {
		this.wId = wId;
	}
	public String getwName() {
		return wName;
	}
	public void setwName(String wName) {
		this.wName = wName;
	}
	public int getwDefault() {
		return wDefault;
	}
	public void setwDefault(int wDefault) {
		this.wDefault = wDefault;
	}

	@Override
	public String toString() {
		return wName;
	}
}
