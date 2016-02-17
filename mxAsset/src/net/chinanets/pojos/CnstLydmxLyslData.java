package net.chinanets.pojos;

import java.util.Date;

/**
 * CnstLydmxLyslData entity. @author MyEclipse Persistence Tools
 */

public class CnstLydmxLyslData implements java.io.Serializable {

	// Fields

	private String lydmxlyslid;
	private String sqdmxid;
	private String sqdmxbh;
	private String sqdid;
	private Date sqrq;
	private String sqr;
	private String pcbh;
	private String sqbm;
	private Date bgrq;
	private String bglx;
	private String zt;
	private String wpmc;
	private String memo;
	private String ryhcmxid;
	private String sqsl;
	private String gbdm;

	// Constructors

	/** default constructor */
	public CnstLydmxLyslData() {
	}

	/** minimal constructor */
	public CnstLydmxLyslData(String sqdmxid, String sqdid, Date sqrq, String sqr) {
		this.sqdmxid = sqdmxid;
		this.sqdid = sqdid;
		this.sqrq = sqrq;
		this.sqr = sqr;
	}

	/** full constructor */
	public CnstLydmxLyslData(String sqdmxid, String sqdmxbh, String sqdid,
			Date sqrq, String sqr, String pcbh, String sqbm, Date bgrq,
			String bglx, String zt, String wpmc, String memo, String ryhcmxid,
			String sqsl, String gbdm) {
		this.sqdmxid = sqdmxid;
		this.sqdmxbh = sqdmxbh;
		this.sqdid = sqdid;
		this.sqrq = sqrq;
		this.sqr = sqr;
		this.pcbh = pcbh;
		this.sqbm = sqbm;
		this.bgrq = bgrq;
		this.bglx = bglx;
		this.zt = zt;
		this.wpmc = wpmc;
		this.memo = memo;
		this.ryhcmxid = ryhcmxid;
		this.sqsl = sqsl;
		this.gbdm = gbdm;
	}

	// Property accessors

	public String getLydmxlyslid() {
		return this.lydmxlyslid;
	}

	public void setLydmxlyslid(String lydmxlyslid) {
		this.lydmxlyslid = lydmxlyslid;
	}

	public String getSqdmxid() {
		return this.sqdmxid;
	}

	public void setSqdmxid(String sqdmxid) {
		this.sqdmxid = sqdmxid;
	}

	public String getSqdmxbh() {
		return this.sqdmxbh;
	}

	public void setSqdmxbh(String sqdmxbh) {
		this.sqdmxbh = sqdmxbh;
	}

	public String getSqdid() {
		return this.sqdid;
	}

	public void setSqdid(String sqdid) {
		this.sqdid = sqdid;
	}

	public Date getSqrq() {
		return this.sqrq;
	}

	public void setSqrq(Date sqrq) {
		this.sqrq = sqrq;
	}

	public String getSqr() {
		return this.sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getPcbh() {
		return this.pcbh;
	}

	public void setPcbh(String pcbh) {
		this.pcbh = pcbh;
	}

	public String getSqbm() {
		return this.sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public Date getBgrq() {
		return this.bgrq;
	}

	public void setBgrq(Date bgrq) {
		this.bgrq = bgrq;
	}

	public String getBglx() {
		return this.bglx;
	}

	public void setBglx(String bglx) {
		this.bglx = bglx;
	}

	public String getZt() {
		return this.zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getWpmc() {
		return this.wpmc;
	}

	public void setWpmc(String wpmc) {
		this.wpmc = wpmc;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRyhcmxid() {
		return this.ryhcmxid;
	}

	public void setRyhcmxid(String ryhcmxid) {
		this.ryhcmxid = ryhcmxid;
	}

	public String getSqsl() {
		return this.sqsl;
	}

	public void setSqsl(String sqsl) {
		this.sqsl = sqsl;
	}

	public String getGbdm() {
		return this.gbdm;
	}

	public void setGbdm(String gbdm) {
		this.gbdm = gbdm;
	}

}