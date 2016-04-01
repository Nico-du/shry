package net.chinanets.pojos;

import java.lang.Long;
import java.sql.Timestamp;

/**
 * ShryFyZsData entity. @author MyEclipse Persistence Tools
 */

public class ShryFyZsData implements java.io.Serializable {

	// Fields

	private Long zsid;
	private Long fyid;
	private String csbz;
	private String speed;
	private String noise;
	private String memo;
	private Timestamp inputdate;
	private String inputuser;
	private Timestamp updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryFyZsData() {
	}

	/** minimal constructor */
	public ShryFyZsData(Long fyid) {
		this.fyid = fyid;
	}

	/** full constructor */
	public ShryFyZsData(Long fyid, String csbz, String speed,
			String noise, String memo, Timestamp inputdate, String inputuser,
			Timestamp updatedate, String updateuser) {
		this.fyid = fyid;
		this.csbz = csbz;
		this.speed = speed;
		this.noise = noise;
		this.memo = memo;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getZsid() {
		return this.zsid;
	}

	public void setZsid(Long zsid) {
		this.zsid = zsid;
	}

	public Long getFyid() {
		return this.fyid;
	}

	public void setFyid(Long fyid) {
		this.fyid = fyid;
	}

	public String getCsbz() {
		return this.csbz;
	}

	public void setCsbz(String csbz) {
		this.csbz = csbz;
	}

	public String getSpeed() {
		return this.speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getNoise() {
		return this.noise;
	}

	public void setNoise(String noise) {
		this.noise = noise;
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