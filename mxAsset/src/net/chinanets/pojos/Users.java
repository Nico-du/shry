package net.chinanets.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Users entity. @author MyEclipse Persistence Tools
 */

public class Users implements java.io.Serializable {

	// Fields

	private Long id;
	private String mc;
	private String mm;
	private String zsxm;
	private String bmmc;
	private String cjsj;
	private String isleader;
	private String iswfshr;
	private String zwid; //职位ID
	private String zwmc; //职位名称
	private String zwlbmc; //职位类别名称
	private Long showOrder;
	private List depts = new ArrayList(0);
	private List ruleses = new ArrayList(0);
	private List usersMenuses = new ArrayList(0);

	// Constructors

	/** default constructor */
	public Users() {
	}

	/** minimal constructor */
	public Users(String mc, String mm) {
		this.mc = mc;
		this.mm = mm;
	}

	/** full constructor */
	public Users(String mc, String mm, String zsxm, String bmmc, String cjsj,
			String isleader, Long showOrder, List usersDeptses, List ruleses, List usersMenuses) {
		this.mc = mc;
		this.mm = mm;
		this.zsxm = zsxm;
		this.bmmc = bmmc;
		this.cjsj = cjsj;
		this.isleader = isleader;
		this.showOrder = showOrder;
		this.depts = depts;
		this.ruleses = ruleses;
		this.usersMenuses = usersMenuses;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMc() {
		return this.mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getMm() {
		return this.mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	public String getZsxm() {
		return this.zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getBmmc() {
		return this.bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getCjsj() {
		return this.cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getIsleader() {
		return this.isleader;
	}

	public void setIsleader(String isleader) {
		this.isleader = isleader;
	}

	public Long getShowOrder() {
		return this.showOrder;
	}

	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	

	public List getRuleses() {
		return this.ruleses;
	}

	public void setRuleses(List ruleses) {
		this.ruleses = ruleses;
	}

	public List getDepts() {
		return depts;
	}

	public void setDepts(List depts) {
		this.depts = depts;
	}

	public List getUsersMenuses() {
		return usersMenuses;
	}

	public void setUsersMenuses(List usersMenuses) {
		this.usersMenuses = usersMenuses;
	}

	public String getIswfshr() {
		return iswfshr;
	}

	public void setIswfshr(String iswfshr) {
		this.iswfshr = iswfshr;
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


}