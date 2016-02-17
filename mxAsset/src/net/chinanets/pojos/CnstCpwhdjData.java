package net.chinanets.pojos;

import java.util.Date;

/**
 * CnstCpwhdjData entity. @author MyEclipse Persistence Tools
 */

public class CnstCpwhdjData implements java.io.Serializable {

	// Fields

	private String whdjid;
	private String whbh;
	private String compactid;
	private String whr;
	private Date whsj;
	private String lxfs;
	private String whnr;
	private String memo;
	private String whlx;
	private String cuser;
	private Date ctime;
	private String uuser;
	private Date utime;

	// Constructors

	/** default constructor */
	public CnstCpwhdjData() {
	}

	/** full constructor */
	public CnstCpwhdjData(String whbh, String compactid, String whr, Date whsj,
			String lxfs, String whnr, String memo, String whlx, String cuser,
			Date ctime, String uuser, Date utime) {
		this.whbh = whbh;
		this.compactid = compactid;
		this.whr = whr;
		this.whsj = whsj;
		this.lxfs = lxfs;
		this.whnr = whnr;
		this.memo = memo;
		this.whlx = whlx;
		this.cuser = cuser;
		this.ctime = ctime;
		this.uuser = uuser;
		this.utime = utime;
	}

	// Property accessors

	public String getWhdjid() {
		return this.whdjid;
	}

	public void setWhdjid(String whdjid) {
		this.whdjid = whdjid;
	}

	public String getWhbh() {
		return this.whbh;
	}

	public void setWhbh(String whbh) {
		this.whbh = whbh;
	}

	public String getCompactid() {
		return this.compactid;
	}

	public void setCompactid(String compactid) {
		this.compactid = compactid;
	}

	public String getWhr() {
		return this.whr;
	}

	public void setWhr(String whr) {
		this.whr = whr;
	}

	public Date getWhsj() {
		return this.whsj;
	}

	public void setWhsj(Date whsj) {
		this.whsj = whsj;
	}

	public String getLxfs() {
		return this.lxfs;
	}

	public void setLxfs(String lxfs) {
		this.lxfs = lxfs;
	}

	public String getWhnr() {
		return this.whnr;
	}

	public void setWhnr(String whnr) {
		this.whnr = whnr;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getWhlx() {
		return this.whlx;
	}

	public void setWhlx(String whlx) {
		this.whlx = whlx;
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