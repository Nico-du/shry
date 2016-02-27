package net.chinanets.pojos;

import java.lang.Long;
import java.util.Date;

/**
 * ShryFyxnData entity. @author dzj
 */

public class ShryFyxnData implements java.io.Serializable {

	// Fields

	private Long fyxnid;
	private Long fyid;
	private Long sydid;
	private String zzs;
	private String fzs;
	private String zy;
	private String ll;
	private String jyl;
	private String fszs;
	private String nj;
	private String yxgl;
	private String zgl;
	private String xl;
	private String memo;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryFyxnData() {
	}

	/** full constructor */
	public ShryFyxnData(Long fyid, Long sydid, String zzs,
			String fzs, String zy, String ll, String jyl, String fszs,
			String nj, String yxgl, String zgl, String xl, String memo,
			Date inputdate, String inputuser, Date updatedate,
			String updateuser) {
		this.fyid = fyid;
		this.sydid = sydid;
		this.zzs = zzs;
		this.fzs = fzs;
		this.zy = zy;
		this.ll = ll;
		this.jyl = jyl;
		this.fszs = fszs;
		this.nj = nj;
		this.yxgl = yxgl;
		this.zgl = zgl;
		this.xl = xl;
		this.memo = memo;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getFyxnid() {
		return this.fyxnid;
	}

	public void setFyxnid(Long fyxnid) {
		this.fyxnid = fyxnid;
	}

	public Long getFyid() {
		return this.fyid;
	}

	public void setFyid(Long fyid) {
		this.fyid = fyid;
	}

	public Long getSydid() {
		return this.sydid;
	}

	public void setSydid(Long sydid) {
		this.sydid = sydid;
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

	public String getZy() {
		return this.zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
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

	public String getFszs() {
		return this.fszs;
	}

	public void setFszs(String fszs) {
		this.fszs = fszs;
	}

	public String getNj() {
		return this.nj;
	}

	public void setNj(String nj) {
		this.nj = nj;
	}

	public String getYxgl() {
		return this.yxgl;
	}

	public void setYxgl(String yxgl) {
		this.yxgl = yxgl;
	}

	public String getZgl() {
		return this.zgl;
	}

	public void setZgl(String zgl) {
		this.zgl = zgl;
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