package net.chinanets.pojos;

import java.util.Date;

/**
 * CnstCompactData entity. @author MyEclipse Persistence Tools
 */

public class CnstCompactData implements java.io.Serializable {

	// Fields

	private String compactid;
	private String compactname;
	private String asscompactid;
	private String cptype;
	private Date signdate;
	private Date validitydate;
	private String dfdw;
	private String jbr;
	private String htzje;
	private String htnr;
	private String memo;
	private String htbh;
	private Date htwcrq;
	private String cuser;
	private Date ctime;
	private String uuser;
	private Date utime;

	// Constructors

	/** default constructor */
	public CnstCompactData() {
	}

	/** full constructor */
	public CnstCompactData(String compactname, String asscompactid,
			String cptype, Date signdate, Date validitydate, String dfdw,
			String jbr, String htzje, String htnr, String memo, String htbh,
			Date htwcrq, String cuser, Date ctime, String uuser, Date utime) {
		this.compactname = compactname;
		this.asscompactid = asscompactid;
		this.cptype = cptype;
		this.signdate = signdate;
		this.validitydate = validitydate;
		this.dfdw = dfdw;
		this.jbr = jbr;
		this.htzje = htzje;
		this.htnr = htnr;
		this.memo = memo;
		this.htbh = htbh;
		this.htwcrq = htwcrq;
		this.cuser = cuser;
		this.ctime = ctime;
		this.uuser = uuser;
		this.utime = utime;
	}

	// Property accessors

	public String getCompactid() {
		return this.compactid;
	}

	public void setCompactid(String compactid) {
		this.compactid = compactid;
	}

	public String getCompactname() {
		return this.compactname;
	}

	public void setCompactname(String compactname) {
		this.compactname = compactname;
	}

	public String getAsscompactid() {
		return this.asscompactid;
	}

	public void setAsscompactid(String asscompactid) {
		this.asscompactid = asscompactid;
	}

	public String getCptype() {
		return this.cptype;
	}

	public void setCptype(String cptype) {
		this.cptype = cptype;
	}

	public Date getSigndate() {
		return this.signdate;
	}

	public void setSigndate(Date signdate) {
		this.signdate = signdate;
	}

	public Date getValiditydate() {
		return this.validitydate;
	}

	public void setValiditydate(Date validitydate) {
		this.validitydate = validitydate;
	}

	public String getDfdw() {
		return this.dfdw;
	}

	public void setDfdw(String dfdw) {
		this.dfdw = dfdw;
	}

	public String getJbr() {
		return this.jbr;
	}

	public void setJbr(String jbr) {
		this.jbr = jbr;
	}

	public String getHtzje() {
		return this.htzje;
	}

	public void setHtzje(String htzje) {
		this.htzje = htzje;
	}

	public String getHtnr() {
		return this.htnr;
	}

	public void setHtnr(String htnr) {
		this.htnr = htnr;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getHtbh() {
		return this.htbh;
	}

	public void setHtbh(String htbh) {
		this.htbh = htbh;
	}

	public Date getHtwcrq() {
		return this.htwcrq;
	}

	public void setHtwcrq(Date htwcrq) {
		this.htwcrq = htwcrq;
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