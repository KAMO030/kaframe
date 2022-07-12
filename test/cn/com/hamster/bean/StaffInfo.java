package cn.com.hamster.bean;



import java.io.Serializable;

/**
 * 员工信息表
 */
public class StaffInfo  implements Serializable {
	/**
	 * 员工编号
	 */
	private int sId;
	/**
	 * 员工姓名
	 */
	private String sName;
	/**
	 * 职务
	 */
	private String sJob;
	/**
	 * 联系方式
	 */
	private String sContact;
	/**
	 * 备注
	 */
	private String sNote;

	public StaffInfo() {
	}

	public StaffInfo(int sId, String sName, String sJob, String sContact, String sNote) {
		this.sId = sId;
		this.sName = sName;
		this.sJob = sJob;
		this.sContact = sContact;
		this.sNote = sNote;
	}

	public int getsId() {
		return sId;
	}
	public void setsId(int sId) {
		this.sId = sId;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getsJob() {
		return sJob;
	}
	public void setsJob(String sJob) {
		this.sJob = sJob;
	}
	public String getsContact() {
		return sContact;
	}
	public void setsContact(String sContact) {
		this.sContact = sContact;
	}
	public String getsNote() {
		return sNote;
	}
	public void setsNote(String sNote) {
		this.sNote = sNote;
	}


}
