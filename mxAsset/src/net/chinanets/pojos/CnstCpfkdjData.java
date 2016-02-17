package net.chinanets.pojos;

import java.util.Date;

/**
 * CnstCpfkdjData entity. @author MyEclipse Persistence Tools
 */

public class CnstCpfkdjData implements java.io.Serializable {

	// Fields

	private String fkdjid;
	private String fkbh;
	private String compactid;
	private Date fksj;
	private String fkje;
	private String jbr;
	private String fph;
	private String htwcjd;
	private String bqlxqk;
	private String memo;
	private String cuser;
	private Date ctime;
	private String uuser;
	private Date utime;

	// Constructors

	/** default constructor */
	public CnstCpfkdjData() {
	}

	/** full constructor */
	public CnstCpfkdjData(String fkbh, String compactid, Date fksj,
			String fkje, String jbr, String fph, String htwcjd, String bqlxqk,
			String memo, String cuser, Date ctime, String uuser, Date utime) {
		this.fkbh = fkbh;
		this.compactid = compactid;
		this.fksj = fksj;
		this.fkje = fkje;
		this.jbr = jbr;
		this.fph = fph;
		this.htwcjd = htwcjd;
		this.bqlxqk = bqlxqk;
		this.memo = memo;
		this.cuser = cuser;
		this.ctime = ctime;
		this.uuser = uuser;
		this.utime = utime;
	}

	// Property accessors

	public String getFkdjid() {
		return this.fkdjid;
	}

	public void setFkdjid(String fkdjid) {
		this.fkdjid = fkdjid;
	}

	public String getFkbh() {
		return this.fkbh;
	}

	public void setFkbh(String fkbh) {
		this.fkbh = fkbh;
	}

	public String getCompactid() {
		return this.compactid;
	}

	public void setCompactid(String compactid) {
		this.compactid = compactid;
	}

	public Date getFksj() {
		return this.fksj;
	}

	public void setFksj(Date fksj) {
		this.fksj = fksj;
	}

	public String getFkje() {
		return this.fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getJbr() {
		return this.jbr;
	}

	public void setJbr(String jbr) {
		this.jbr = jbr;
	}

	public String getFph() {
		return this.fph;
	}

	public void setFph(String fph) {
		this.fph = fph;
	}

	public String getHtwcjd() {
		return this.htwcjd;
	}

	public void setHtwcjd(String htwcjd) {
		this.htwcjd = htwcjd;
	}

	public String getBqlxqk() {
		return this.bqlxqk;
	}

	public void setBqlxqk(String bqlxqk) {
		this.bqlxqk = bqlxqk;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCuser() {
		return this.cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public Date getCtime() {
		return this.ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getUuser() {
		return this.uuser;
	}

	public void setUuser(String uuser) {
		this.uuser = uuser;
	}

	public Date getUtime() {
		return this.utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

}