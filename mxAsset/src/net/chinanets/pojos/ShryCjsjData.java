package net.chinanets.pojos;

import java.util.Date;

/**
 * ShryCjsjData entity. @author MyEclipse Persistence Tools
 */

public class ShryCjsjData implements java.io.Serializable {

	// Fields

	private Long id;
	private String th;
	private String zcxh;
	private String cx;
	private String xm;
	private String cjcl;
	private String cpcl;
	private String cpdc;
	private String xj;
	private String pinj;
	private String dwj;
	private String dl;
	private String zddj;
	private String fhdj;
	private String hjgk;
	private String cbl;
	private String dw;
	private String memo;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryCjsjData() {
	}

	/** full constructor */
	public ShryCjsjData(String th, String zcxh, String cx, String xm,
			String cjcl, String cpcl, String cpdc, String xj, String pinj,
			String dwj, String dl, String zddj, String fhdj, String hjgk,
			String cbl, String dw, String memo, Date inputdate,
			String inputuser, Date updatedate, String updateuser) {
		this.th = th;
		this.zcxh = zcxh;
		this.cx = cx;
		this.xm = xm;
		this.cjcl = cjcl;
		this.cpcl = cpcl;
		this.cpdc = cpdc;
		this.xj = xj;
		this.pinj = pinj;
		this.dwj = dwj;
		this.dl = dl;
		this.zddj = zddj;
		this.fhdj = fhdj;
		this.hjgk = hjgk;
		this.cbl = cbl;
		this.dw = dw;
		this.memo = memo;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTh() {
		return this.th;
	}

	public void setTh(String th) {
		this.th = th;
	}

	public String getZcxh() {
		return this.zcxh;
	}

	public void setZcxh(String zcxh) {
		this.zcxh = zcxh;
	}

	public String getCx() {
		return this.cx;
	}

	public void setCx(String cx) {
		this.cx = cx;
	}

	public String getXm() {
		return this.xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getCjcl() {
		return this.cjcl;
	}

	public void setCjcl(String cjcl) {
		this.cjcl = cjcl;
	}

	public String getCpcl() {
		return this.cpcl;
	}

	public void setCpcl(String cpcl) {
		this.cpcl = cpcl;
	}

	public String getCpdc() {
		return this.cpdc;
	}

	public void setCpdc(String cpdc) {
		this.cpdc = cpdc;
	}

	public String getXj() {
		return this.xj;
	}

	public void setXj(String xj) {
		this.xj = xj;
	}

	public String getPinj() {
		return this.pinj;
	}

	public void setPinj(String pinj) {
		this.pinj = pinj;
	}

	public String getDwj() {
		return this.dwj;
	}

	public void setDwj(String dwj) {
		this.dwj = dwj;
	}

	public String getDl() {
		return this.dl;
	}

	public void setDl(String dl) {
		this.dl = dl;
	}

	public String getZddj() {
		return this.zddj;
	}

	public void setZddj(String zddj) {
		this.zddj = zddj;
	}

	public String getFhdj() {
		return this.fhdj;
	}

	public void setFhdj(String fhdj) {
		this.fhdj = fhdj;
	}

	public String getHjgk() {
		return this.hjgk;
	}

	public void setHjgk(String hjgk) {
		this.hjgk = hjgk;
	}

	public String getCbl() {
		return this.cbl;
	}

	public void setCbl(String cbl) {
		this.cbl = cbl;
	}

	public String getDw() {
		return this.dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getInputdate() {
		return this.inputdate;
	}

	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}

	public String getInputuser() {
		return this.inputuser;
	}

	public void setInputuser(String inputuser) {
		this.inputuser = inputuser;
	}

	public Date getUpdatedate() {
		return this.updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getUpdateuser() {
		return this.updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

}