package cn.com.hamster.bean;




import java.io.Serializable;
import java.sql.Date;

/**
 * 日志
 */
public class LogDaily implements Serializable {
	/**
	 * 日志编号
	 */
	private int ldId;
	/**
	 * 操作员编号
	 */
	private int oId;
	/**
	 * 时间
	 */
	private Date ldDate;
	/**
	 * 操作摘要
	 */
	private String ldResume;
	/**
	 * 操作内容
	 */
	private String ldContent;



	public LogDaily() {
	}

	public LogDaily(int ldId, int oId, Date ldDate, String ldResume, String ldContent) {
		this.ldId = ldId;
		this.oId = oId;
		this.ldDate = ldDate;
		this.ldResume = ldResume;
		this.ldContent = ldContent;
	}

	public int getLdId() {
		return ldId;
	}
	public void setLdId(int ldId) {
		this.ldId = ldId;
	}
	public int getoId() {
		return oId;
	}
	public void setoId(int oId) {
		this.oId = oId;
	}

	public Date getLdDate() {
		return ldDate;
	}

	public void setLdDate(Date ldDate) {
		this.ldDate = ldDate;
	}

	public String getLdResume() {
		return ldResume;
	}
	public void setLdResume(String ldResume) {
		this.ldResume = ldResume;
	}
	public String getLdContent() {
		return ldContent;
	}
	public void setLdContent(String ldContent) {
		this.ldContent = ldContent;
	}
	
}
