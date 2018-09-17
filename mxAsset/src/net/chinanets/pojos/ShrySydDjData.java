package net.chinanets.pojos;

import java.util.Date;

/**
 * ShrySydDjData entity. @author MyEclipse Persistence Tools
 */

public class ShrySydDjData implements java.io.Serializable {

	// Fields

	private Long lxdid;
	private Long djid;
	private String isysdj;
	private String project;
	private String motor;
	private Date datetime;
	private String operator;
	private String testRef;
	private String motorRef;
	private String direction;
	private String winding;
	private String sof;
	private String voltage;
	private String simulationRes;
	private String hardwareRes;
	private String nlcMeasuredSpeed;
	private String nlcMeasuredCurrent;
	private String nlcCalculatedSpeed;
	private String nlcCalculatedCurrent;
	private String scTorque;
	private String scCurrent;
	private String mpcTorque;
	private String mpcSpeed;
	private String mpcCurrent;
	private String mpcPowOut;
	private String mpcEfficiency;
	private String mecTorque;
	private String mecSpeed;
	private String mecCurrent;
	private String mecPowOut;
	private String mecEfficiency;
	private String mcTorqueConstant;
	private String mcDynamicResistance;
	private String mcMotorRegulation;
	private String memo;
	private String syzt;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShrySydDjData() {
	}

	/** full constructor */
	public ShrySydDjData(Long djid, String isysdj, String project,
			String motor, Date datetime, String operator, String testRef,
			String motorRef, String direction, String winding, String sof,
			String voltage, String simulationRes, String hardwareRes,
			String nlcMeasuredSpeed, String nlcMeasuredCurrent,
			String nlcCalculatedSpeed, String nlcCalculatedCurrent,
			String scTorque, String scCurrent, String mpcTorque,
			String mpcSpeed, String mpcCurrent, String mpcPowOut,
			String mpcEfficiency, String mecTorque, String mecSpeed,
			String mecCurrent, String mecPowOut, String mecEfficiency,
			String mcTorqueConstant, String mcDynamicResistance,
			String mcMotorRegulation, String memo, String syzt,
			Date inputdate, String inputuser, Date updatedate,
			String updateuser) {
		this.djid = djid;
		this.isysdj = isysdj;
		this.project = project;
		this.motor = motor;
		this.datetime = datetime;
		this.operator = operator;
		this.testRef = testRef;
		this.motorRef = motorRef;
		this.direction = direction;
		this.winding = winding;
		this.sof = sof;
		this.voltage = voltage;
		this.simulationRes = simulationRes;
		this.hardwareRes = hardwareRes;
		this.nlcMeasuredSpeed = nlcMeasuredSpeed;
		this.nlcMeasuredCurrent = nlcMeasuredCurrent;
		this.nlcCalculatedSpeed = nlcCalculatedSpeed;
		this.nlcCalculatedCurrent = nlcCalculatedCurrent;
		this.scTorque = scTorque;
		this.scCurrent = scCurrent;
		this.mpcTorque = mpcTorque;
		this.mpcSpeed = mpcSpeed;
		this.mpcCurrent = mpcCurrent;
		this.mpcPowOut = mpcPowOut;
		this.mpcEfficiency = mpcEfficiency;
		this.mecTorque = mecTorque;
		this.mecSpeed = mecSpeed;
		this.mecCurrent = mecCurrent;
		this.mecPowOut = mecPowOut;
		this.mecEfficiency = mecEfficiency;
		this.mcTorqueConstant = mcTorqueConstant;
		this.mcDynamicResistance = mcDynamicResistance;
		this.mcMotorRegulation = mcMotorRegulation;
		this.memo = memo;
		this.syzt = syzt;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getLxdid() {
		return this.lxdid;
	}

	public void setLxdid(Long lxdid) {
		this.lxdid = lxdid;
	}

	public Long getDjid() {
		return this.djid;
	}

	public void setDjid(Long djid) {
		this.djid = djid;
	}

	public String getIsysdj() {
		return this.isysdj;
	}

	public void setIsysdj(String isysdj) {
		this.isysdj = isysdj;
	}

	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getMotor() {
		return this.motor;
	}

	public void setMotor(String motor) {
		this.motor = motor;
	}

	public Date getDatetime() {
		return this.datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTestRef() {
		return this.testRef;
	}

	public void setTestRef(String testRef) {
		this.testRef = testRef;
	}

	public String getMotorRef() {
		return this.motorRef;
	}

	public void setMotorRef(String motorRef) {
		this.motorRef = motorRef;
	}

	public String getDirection() {
		return this.direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getWinding() {
		return this.winding;
	}

	public void setWinding(String winding) {
		this.winding = winding;
	}

	public String getSof() {
		return this.sof;
	}

	public void setSof(String sof) {
		this.sof = sof;
	}

	public String getVoltage() {
		return this.voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getSimulationRes() {
		return this.simulationRes;
	}

	public void setSimulationRes(String simulationRes) {
		this.simulationRes = simulationRes;
	}

	public String getHardwareRes() {
		return this.hardwareRes;
	}

	public void setHardwareRes(String hardwareRes) {
		this.hardwareRes = hardwareRes;
	}

	public String getNlcMeasuredSpeed() {
		return this.nlcMeasuredSpeed;
	}

	public void setNlcMeasuredSpeed(String nlcMeasuredSpeed) {
		this.nlcMeasuredSpeed = nlcMeasuredSpeed;
	}

	public String getNlcMeasuredCurrent() {
		return this.nlcMeasuredCurrent;
	}

	public void setNlcMeasuredCurrent(String nlcMeasuredCurrent) {
		this.nlcMeasuredCurrent = nlcMeasuredCurrent;
	}

	public String getNlcCalculatedSpeed() {
		return this.nlcCalculatedSpeed;
	}

	public void setNlcCalculatedSpeed(String nlcCalculatedSpeed) {
		this.nlcCalculatedSpeed = nlcCalculatedSpeed;
	}

	public String getNlcCalculatedCurrent() {
		return this.nlcCalculatedCurrent;
	}

	public void setNlcCalculatedCurrent(String nlcCalculatedCurrent) {
		this.nlcCalculatedCurrent = nlcCalculatedCurrent;
	}

	public String getScTorque() {
		return this.scTorque;
	}

	public void setScTorque(String scTorque) {
		this.scTorque = scTorque;
	}

	public String getScCurrent() {
		return this.scCurrent;
	}

	public void setScCurrent(String scCurrent) {
		this.scCurrent = scCurrent;
	}

	public String getMpcTorque() {
		return this.mpcTorque;
	}

	public void setMpcTorque(String mpcTorque) {
		this.mpcTorque = mpcTorque;
	}

	public String getMpcSpeed() {
		return this.mpcSpeed;
	}

	public void setMpcSpeed(String mpcSpeed) {
		this.mpcSpeed = mpcSpeed;
	}

	public String getMpcCurrent() {
		return this.mpcCurrent;
	}

	public void setMpcCurrent(String mpcCurrent) {
		this.mpcCurrent = mpcCurrent;
	}

	public String getMpcPowOut() {
		return this.mpcPowOut;
	}

	public void setMpcPowOut(String mpcPowOut) {
		this.mpcPowOut = mpcPowOut;
	}

	public String getMpcEfficiency() {
		return this.mpcEfficiency;
	}

	public void setMpcEfficiency(String mpcEfficiency) {
		this.mpcEfficiency = mpcEfficiency;
	}

	public String getMecTorque() {
		return this.mecTorque;
	}

	public void setMecTorque(String mecTorque) {
		this.mecTorque = mecTorque;
	}

	public String getMecSpeed() {
		return this.mecSpeed;
	}

	public void setMecSpeed(String mecSpeed) {
		this.mecSpeed = mecSpeed;
	}

	public String getMecCurrent() {
		return this.mecCurrent;
	}

	public void setMecCurrent(String mecCurrent) {
		this.mecCurrent = mecCurrent;
	}

	public String getMecPowOut() {
		return this.mecPowOut;
	}

	public void setMecPowOut(String mecPowOut) {
		this.mecPowOut = mecPowOut;
	}

	public String getMecEfficiency() {
		return this.mecEfficiency;
	}

	public void setMecEfficiency(String mecEfficiency) {
		this.mecEfficiency = mecEfficiency;
	}

	public String getMcTorqueConstant() {
		return this.mcTorqueConstant;
	}

	public void setMcTorqueConstant(String mcTorqueConstant) {
		this.mcTorqueConstant = mcTorqueConstant;
	}

	public String getMcDynamicResistance() {
		return this.mcDynamicResistance;
	}

	public void setMcDynamicResistance(String mcDynamicResistance) {
		this.mcDynamicResistance = mcDynamicResistance;
	}

	public String getMcMotorRegulation() {
		return this.mcMotorRegulation;
	}

	public void setMcMotorRegulation(String mcMotorRegulation) {
		this.mcMotorRegulation = mcMotorRegulation;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSyzt() {
		return this.syzt;
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