package net.chinanets.pojos;

import java.lang.Long;
import java.util.Date;

/**
 * ShryZcData entity. @author dzj
 */

public class ShryZcData implements java.io.Serializable {

	// Fields

	private Long zcid;
	private Long fyid;
	private Long djid;
	private String xh;
	private String pp;
	private String jqfs;
	private String wxcc;
	private String zl;
	private String fslx;
	private String sycx;
	private String syzt;
	private String memo;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryZcData() {
	}

	/** full constructor */
	public ShryZcData(Long fyid, Long djid, String xh, String pp,
			String jqfs, String wxcc, String zl, String fslx, String sycx,
			String memo, Date inputdate, String inputuser,
			Date updatedate, String updateuser) {
		this.fyid = fyid;
		this.djid = djid;
		this.xh = xh;
		this.pp = pp;
		this.jqfs = jqfs;
		this.wxcc = wxcc;
		this.zl = zl;
		this.fslx = fslx;
		this.sycx = sycx;
		this.memo = memo;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getZcid() {
		return this.zcid;
	}

	public void setZcid(Long zcid) {
		this.zcid = zcid;
	}

	public Long getFyid() {
		return this.fyid;
	}

	public void setFyid(Long fyid) {
		this.fyid = fyid;
	}

	public Long getDjid() {
		return this.djid;
	}

	public void setDjid(Long djid) {
		this.djid = djid;
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

	public String getJqfs() {
		return this.jqfs;
	}

	public void setJqfs(String jqfs) {
		this.jqfs = jqfs;
	}

	public String getWxcc() {
		return this.wxcc;
	}

	public void setWxcc(String wxcc) {
		this.wxcc = wxcc;
	}

	public String getZl() {
		return this.zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
	}

	public String getFslx() {
		return this.fslx;
	}

	public void setFslx(String fslx) {
		this.fslx = fslx;
	}

	public String getSycx() {
		return this.sycx;
	}

	public void setSycx(String sycx) {
		this.sycx = sycx;
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