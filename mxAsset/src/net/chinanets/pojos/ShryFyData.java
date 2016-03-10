package net.chinanets.pojos;

import java.lang.Long;
import java.util.Date;

/**
 * ShryFyData entity. @author dzj
 */

public class ShryFyData implements java.io.Serializable {

	// Fields

	private Long fyid;
	private String xh;
	private String pp;
	private String zj;
	private String fbzj;
	private String dlhzj;
	private String lgzj;
	private String lggd;
	private String ypzj;
	private String zl;
	private String ypsm;
	private String jqfs;
	private String ypfb;
	private String cl;
	private String clbz;
	private String qj;
	private String wxturl;
	private String xnturl;
	private String zsturl;
	private String syzt;
	private String memo;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryFyData() {
	}

	/** full constructor */
	public ShryFyData(String xh, String pp, String zj, String fbzj,
			String dlhzj, String lgzj, String lggd, String ypzj, String zl,
			String ypsm, String jqfs, String ypfb, String qj, String wxturl,
			String xnturl, String zsturl, String memo, Date inputdate,
			String inputuser, Date updatedate, String updateuser) {
		this.xh = xh;
		this.pp = pp;
		this.zj = zj;
		this.fbzj = fbzj;
		this.dlhzj = dlhzj;
		this.lgzj = lgzj;
		this.lggd = lggd;
		this.ypzj = ypzj;
		this.zl = zl;
		this.ypsm = ypsm;
		this.jqfs = jqfs;
		this.ypfb = ypfb;
		this.qj = qj;
		this.wxturl = wxturl;
		this.xnturl = xnturl;
		this.zsturl = zsturl;
		this.memo = memo;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getFyid() {
		return this.fyid;
	}

	public void setFyid(Long fyid) {
		this.fyid = fyid;
	}

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getPp() {
		return this.pp;
	}

	public void setPp(String pp) {
		this.pp = pp;
	}

	public String getZj() {
		return this.zj;
	}

	public void setZj(String zj) {
		this.zj = zj;
	}

	public String getFbzj() {
		return this.fbzj;
	}

	public void setFbzj(String fbzj) {
		this.fbzj = fbzj;
	}

	public String getDlhzj() {
		return this.dlhzj;
	}

	public void setDlhzj(String dlhzj) {
		this.dlhzj = dlhzj;
	}

	public String getLgzj() {
		return this.lgzj;
	}

	public void setLgzj(String lgzj) {
		this.lgzj = lgzj;
	}

	public String getLggd() {
		return this.lggd;
	}

	public void setLggd(String lggd) {
		this.lggd = lggd;
	}

	public String getYpzj() {
		return this.ypzj;
	}

	public void setYpzj(String ypzj) {
		this.ypzj = ypzj;
	}

	public String getZl() {
		return this.zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
	}

	public String getYpsm() {
		return this.ypsm;
	}

	public void setYpsm(String ypsm) {
		this.ypsm = ypsm;
	}

	public String getJqfs() {
		return this.jqfs;
	}

	public void setJqfs(String jqfs) {
		this.jqfs = jqfs;
	}

	public String getYpfb() {
		return this.ypfb;
	}

	public void setYpfb(String ypfb) {
		this.ypfb = ypfb;
	}

	public String getQj() {
		return this.qj;
	}

	public void setQj(String qj) {
		this.qj = qj;
	}

	public String getCl() {
		return cl;
	}

	public void setCl(String cl) {
		this.cl = cl;
	}

	public String getClbz() {
		return clbz;
	}

	public void setClbz(String clbz) {
		this.clbz = clbz;
	}

	public String getWxturl() {
		return this.wxturl;
	}

	public void setWxturl(String wxturl) {
		this.wxturl = wxturl;
	}

	public String getXnturl() {
		return this.xnturl;
	}

	public void setXnturl(String xnturl) {
		this.xnturl = xnturl;
	}

	public String getZsturl() {
		return this.zsturl;
	}

	public void setZsturl(String zsturl) {
		this.zsturl = zsturl;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSyzt() {
		return syzt;
	}

	public void setSyzt(String syzt) {
		this.syzt = syzt;
	}

	public Date getInputdate() {
		return inputdate;
	}

	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}

	public String getInputuser() {
		return inputuser;
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