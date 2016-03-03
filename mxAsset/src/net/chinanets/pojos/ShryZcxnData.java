package net.chinanets.pojos;

import java.lang.Long;
import java.util.Date;

/**
 * ShryZcxnData entity. @author dzj
 */

public class ShryZcxnData implements java.io.Serializable {

	// Fields

	private Long zcxnid;
	private Long zcid;
	private Long fyid;
	private Long lxdid;
	private String ll;
	private String jyl;
	private String zzs;
	private String fzs;
	private String dy;
	private String dl;
	private String srgl;
	private String xl;
	private String memo;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryZcxnData() {
	}

	/** full constructor */
	public ShryZcxnData(Long zcid, Long lxdid, String ll,
			String jyl, String zzs, String fzs, String dy, String dl,
			String srgl, String xl, String memo, Date inputdate,
			String inputuser, Date updatedate, String updateuser) {
		this.zcid = zcid;
		this.lxdid = lxdid;
		this.ll = ll;
		this.jyl = jyl;
		this.zzs = zzs;
		this.fzs = fzs;
		this.dy = dy;
		this.dl = dl;
		this.srgl = srgl;
		this.xl = xl;
		this.memo = memo;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getZcxnid() {
		return this.zcxnid;
	}

	public void setZcxnid(Long zcxnid) {
		this.zcxnid = zcxnid;
	}

	public Long getZcid() {
		return this.zcid;
	}

	public void setZcid(Long zcid) {
		this.zcid = zcid;
	}

	public Long getFyid() {
		return fyid;
	}

	public void setFyid(Long fyid) {
		this.fyid = fyid;
	}

	public Long getLxdid() {
		return this.lxdid;
	}

	public void setLxdid(Long lxdid) {
		this.lxdid = lxdid;
	}

	public String getLl() {
		return this.ll;
	}

	public void setLl(String ll) {
		this.ll = ll;
	}

	public String getJyl() {
		return this.jyl;
	}

	public void setJyl(String jyl) {
		this.jyl = jyl;
	} 

	public String getZzs() {
		return this.zzs;
	}

	public void setZzs(String zzs) {
		this.zzs = zzs;
	}

	public String getFzs() {
		return this.fzs;
	}

	public void setFzs(String fzs) {
		this.fzs = fzs;
	}

	public String getDy() {
		return this.dy;
	}

	public void setDy(String dy) {
		this.dy = dy;
	}

	public String getDl() {
		return this.dl;
	}

	public void setDl(String dl) {
		this.dl = dl;
	}

	public String getSrgl() {
		return this.srgl;
	}

	public void setSrgl(String srgl) {
		this.srgl = srgl;
	}

	public String getXl() {
		return this.xl;
	}

	public void setXl(String xl) {
		this.xl = xl;
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