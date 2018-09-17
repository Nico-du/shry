package net.chinanets.pojos;

import java.util.Date;

/**
 * ShryDjxnData entity. @author MyEclipse Persistence Tools
 */

public class ShryDjxnData implements java.io.Serializable {

	// Fields

	private Long djxnid;
	private Long djid;
	private Long lxdid;
	private String data;
	private String input;
	private String torqueNm;
	private String torqueOzIn;
	private String speed;
	private String voltage;
	private String current;
	private String powIn;
	private String powOut;
	private String eff;
	private String remark;
	private String wsPf1;
	private String wsHorsepower;
	private String wsTime;
	private String wsDirection;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryDjxnData() {
	}

	/** full constructor */
	public ShryDjxnData(Long djid, Long lxdid, String data,
			String input, String torqueNm, String torqueOzIn, String speed,
			String voltage, String current, String powIn, String powOut,
			String eff, String remark, String wsPf1, String wsHorsepower,
			String wsTime, String wsDirection, Date inputdate,
			String inputuser, Date updatedate, String updateuser) {
		this.djid = djid;
		this.lxdid = lxdid;
		this.data = data;
		this.input = input;
		this.torqueNm = torqueNm;
		this.torqueOzIn = torqueOzIn;
		this.speed = speed;
		this.voltage = voltage;
		this.current = current;
		this.powIn = powIn;
		this.powOut = powOut;
		this.eff = eff;
		this.remark = remark;
		this.wsPf1 = wsPf1;
		this.wsHorsepower = wsHorsepower;
		this.wsTime = wsTime;
		this.wsDirection = wsDirection;
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

	public Long getLxdid() {
		return this.lxdid;
	}

	public void setLxdid(Long lxdid) {
		this.lxdid = lxdid;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getInput() {
		return this.input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getTorqueNm() {
		return this.torqueNm;
	}

	public void setTorqueNm(String torqueNm) {
		this.torqueNm = torqueNm;
	}

	public String getTorqueOzIn() {
		return this.torqueOzIn;
	}

	public void setTorqueOzIn(String torqueOzIn) {
		this.torqueOzIn = torqueOzIn;
	}

	public String getSpeed() {
		return this.speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getVoltage() {
		return this.voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getCurrent() {
		return this.current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getPowIn() {
		return this.powIn;
	}

	public void setPowIn(String powIn) {
		this.powIn = powIn;
	}

	public String getPowOut() {
		return this.powOut;
	}

	public void setPowOut(String powOut) {
		this.powOut = powOut;
	}

	public String getEff() {
		return this.eff;
	}

	public void setEff(String eff) {
		this.eff = eff;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWsPf1() {
		return this.wsPf1;
	}

	public void setWsPf1(String wsPf1) {
		this.wsPf1 = wsPf1;
	}

	public String getWsHorsepower() {
		return this.wsHorsepower;
	}

	public void setWsHorsepower(String wsHorsepower) {
		this.wsHorsepower = wsHorsepower;
	}

	public String getWsTime() {
		return this.wsTime;
	}

	public void setWsTime(String wsTime) {
		this.wsTime = wsTime;
	}

	public String getWsDirection() {
		return this.wsDirection;
	}

	public void setWsDirection(String wsDirection) {
		this.wsDirection = wsDirection;
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