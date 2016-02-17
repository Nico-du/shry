package net.chinanets.vo;

public class UserVo {

	private Long id;
	private String deptId;
	private String deptName;
	private String userName;//真实姓名
	private String ruleYxj;//角色优先级
	private String ruleId;//角色ID组,逗号分隔
	private String ruleName;//角色名称组
	private String mc;	//帐号
	private String mm;	//密码
	private String isLeader;
	private String isShr;
	private String zwid; //职位ID
	private String zwmc; //职位名称
	private String zwlbmc; //职位类别名称
	private String bmid;//可以查看部门ID组，逗号分隔
	private String bmmc;//可以查看部门名称组,逗号分隔

	public UserVo() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(String isLeader) {
		this.isLeader = isLeader;
	}

	public String getBmid() {
		return bmid;
	}

	public void setBmid(String bmid) {
		this.bmid = bmid;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getZwid() {
		return zwid;
	}

	public void setZwid(String zwid) {
		this.zwid = zwid;
	}

	public String getZwmc() {
		return zwmc;
	}

	public void setZwmc(String zwmc) {
		this.zwmc = zwmc;
	}

	public String getZwlbmc() {
		return zwlbmc;
	}

	public void setZwlbmc(String zwlbmc) {
		this.zwlbmc = zwlbmc;
	}

	public String getIsShr() {
		return isShr;
	}

	public void setIsShr(String isShr) {
		this.isShr = isShr;
	}

	public String getRuleYxj() {
		return ruleYxj;
	}

	public void setRuleYxj(String ruleYxj) {
		this.ruleYxj = ruleYxj;
	}
	

}
