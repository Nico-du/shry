package net.chinanets.pojos;

import java.lang.Long;
import java.sql.Timestamp;

/**
 * ShryDjxnData entity. @author MyEclipse Persistence Tools
 */

public class ShryDjxnData implements java.io.Serializable {

	// Fields

	private Long djxnid;
	private Long djid;
	private String nj;
	private String dy;
	private String dl;
	private String zs;
	private String srgl;
	private String scgl;
	private String xl;
	private String memo;
	private Timestamp inputdate;
	private String inputuser;
	private Timestamp updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryDjxnData() {
	}

	/** full constructor */
	public ShryDjxnData(Long djid, String nj, String dy, String dl,
			String zs, String srgl, String scgl, String xl, String memo,
			Timestamp inputdate, String inputuser, Timestamp updatedate,
			String updateuser) {
		this.djid = djid;
		this.nj = nj;
		this.dy = dy;
		this.dl = dl;
		this.zs = zs;
		this.srgl = srgl;
		this.scgl = scgl;
		this.xl = xl;
		this.memo = memo;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getDjxnid() {
		return this.djxnid;
	}

	public void setDjxnid(Long djxnid) {
		this.djxnid = djxnid;
	}

	public Long getDjid() {
		return this.djid;
	}

	public void setDjid(Long djid) {
		this.djid = djid;
	}

	public String getNj() {
		return this.nj;
	}

	public void setNj(String nj) {
		this.nj = nj;
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

	public String getZs() {
		return this.zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}

	public String getSrgl() {
		return this.srgl;
	}

	public void setSrgl(String srgl) {
		this.srgl = srgl;
	}

	public String getScgl() {
		return this.scgl;
	}

	public void setScgl(String scgl) {
		this.scgl = scgl;
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

	public Timestamp getInputdate() {
		return this.inputdate;
	}

	public void setInputdate(Timestamp inputdate) {
		this.inputdate = inputdate;
	}

	public String getInputuser() {
		return this.inputuser;
	}

	public void setInputuser(String inputuser) {
		this.inputuser = inputuser;
	}

	public Timestamp getUpdatedate() {
		return this.updatedate;
	}

	public void setUpdatedate(Timestamp updatedate) {
		this.updatedate = updatedate;
	}

	public String getUpdateuser() {
		return this.updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

}