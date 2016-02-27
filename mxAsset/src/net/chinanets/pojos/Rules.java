package net.chinanets.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Rules entity. @author dzj
 */

public class Rules implements java.io.Serializable {

	// Fields

	private Long id;
	private String mc;
	private String bz;
	private String yxj;
	private String bmid;
	private String bmmc;
	private String kmcz;
	private Long userId;
	private List userses = new ArrayList(0);
    private List rulesMenuses = new ArrayList(0);
	// Constructors

	/** default constructor */
	public Rules() {
	}

	/** full constructor */
	public Rules(String mc, String bz, String yxj, String bmid,
			String bmmc, String kmcz,Long userId, List userses,List rulesMenuses) {
		this.mc = mc;
		this.bz = bz;
		this.yxj = yxj;
		this.bmid = bmid;
		this.bmmc = bmmc;
		this.kmcz = kmcz;
		this.userId = userId;
		this.userses = userses;
		this.rulesMenuses = rulesMenuses;
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

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getYxj() {
		return this.yxj;
	}

	public void setYxj(String yxj) {
		this.yxj = yxj;
	}

	public String getBmid() {
		return this.bmid;
	}

	public void setBmid(String bmid) {
		this.bmid = bmid;
	}

	public String getBmmc() {
		return this.bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getKmcz() {
		return this.kmcz;
	}

	public void setKmcz(String kmcz) {
		this.kmcz = kmcz;
	}

	public List getUserses() {
		return this.userses;
	}

	public void setUserses(List userses) {
		this.userses = userses;
	}

	public List getRulesMenuses() {
		return rulesMenuses;
	}

	public void setRulesMenuses(List rulesMenuses) {
		this.rulesMenuses = rulesMenuses;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}