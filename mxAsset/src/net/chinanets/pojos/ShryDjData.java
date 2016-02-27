package net.chinanets.pojos;

import java.lang.Long;
import java.util.Date;

/**
 * ShryDjData entity. @author dzj
 */

public class ShryDjData implements java.io.Serializable {

	// Fields

	private Long djid;
	private String xh;
	private String pp;
	private String gd;
	private String zj;
	private String zl;
	private String syzt;
	private String memo;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryDjData() {
	}

	/** full constructor */
	public ShryDjData(String xh, String pp, String gd, String zj, String zl,
			String memo, Date inputdate, String inputuser,
			Date updatedate, String updateuser) {
		this.xh = xh;
		this.pp = pp;
		this.gd = gd;
		this.zj = zj;
		this.zl = zl;
		this.memo = memo;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

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

	public String getGd() {
		return this.gd;
	}

	public void setGd(String gd) {
		this.gd = gd;
	}

	public String getZj() {
		return this.zj;
	}

	public void setZj(String zj) {
		this.zj = zj;
	}

	public String getZl() {
		return this.zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
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