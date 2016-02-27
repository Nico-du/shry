package net.chinanets.pojos;

import java.lang.Long;
import java.util.Date;

/**
 * ShrySydData entity. @author dzj
 */

public class ShrySydData implements java.io.Serializable {

	// Fields

	private Long sydid;
	private Long zcid;
	private Date syrq;
	private String lxdh;
	private String fjxs;
	private String syfd;
	private String ckmj;
	private String syfs;
	private String fyzj;
	private String fjxh;
	private String yps;
	private String ypbh;
	private String dqy;
	private String syxz;
	private String kqwd;
	private String sply;
	private String xdsd;
	private String syry;
	private String skqbz;
	private String bz;
	private String zt;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShrySydData() {
	}

	/** full constructor */
	public ShrySydData(Long zcid, Date syrq, String lxdh,
			String fjxs, String syfd, String ckmj, String syfs, String fyzj,
			String fjxh, String yps, String ypbh, String dqy, String syxz,
			String kqwd, String sply, String xdsd, String syry, String skqbz,
			String bz, String zt, Date inputdate, String inputuser,
			Date updatedate, String updateuser) {
		this.zcid = zcid;
		this.syrq = syrq;
		this.lxdh = lxdh;
		this.fjxs = fjxs;
		this.syfd = syfd;
		this.ckmj = ckmj;
		this.syfs = syfs;
		this.fyzj = fyzj;
		this.fjxh = fjxh;
		this.yps = yps;
		this.ypbh = ypbh;
		this.dqy = dqy;
		this.syxz = syxz;
		this.kqwd = kqwd;
		this.sply = sply;
		this.xdsd = xdsd;
		this.syry = syry;
		this.skqbz = skqbz;
		this.bz = bz;
		this.zt = zt;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getSydid() {
		return this.sydid;
	}

	public void setSydid(Long sydid) {
		this.sydid = sydid;
	}

	public Long getZcid() {
		return this.zcid;
	}

	public void setZcid(Long zcid) {
		this.zcid = zcid;
	}

	public Date getSyrq() {
		return this.syrq;
	}

	public void setSyrq(Date syrq) {
		this.syrq = syrq;
	}

	public String getLxdh() {
		return this.lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getFjxs() {
		return this.fjxs;
	}

	public void setFjxs(String fjxs) {
		this.fjxs = fjxs;
	}

	public String getSyfd() {
		return this.syfd;
	}

	public void setSyfd(String syfd) {
		this.syfd = syfd;
	}

	public String getCkmj() {
		return this.ckmj;
	}

	public void setCkmj(String ckmj) {
		this.ckmj = ckmj;
	}

	public String getSyfs() {
		return this.syfs;
	}

	public void setSyfs(String syfs) {
		this.syfs = syfs;
	}

	public String getFyzj() {
		return this.fyzj;
	}

	public void setFyzj(String fyzj) {
		this.fyzj = fyzj;
	}

	public String getFjxh() {
		return this.fjxh;
	}

	public void setFjxh(String fjxh) {
		this.fjxh = fjxh;
	}

	public String getYps() {
		return this.yps;
	}

	public void setYps(String yps) {
		this.yps = yps;
	}

	public String getYpbh() {
		return this.ypbh;
	}

	public void setYpbh(String ypbh) {
		this.ypbh = ypbh;
	}

	public String getDqy() {
		return this.dqy;
	}

	public void setDqy(String dqy) {
		this.dqy = dqy;
	}

	public String getSyxz() {
		return this.syxz;
	}

	public void setSyxz(String syxz) {
		this.syxz = syxz;
	}

	public String getKqwd() {
		return this.kqwd;
	}

	public void setKqwd(String kqwd) {
		this.kqwd = kqwd;
	}

	public String getSply() {
		return this.sply;
	}

	public void setSply(String sply) {
		this.sply = sply;
	}

	public String getXdsd() {
		return this.xdsd;
	}

	public void setXdsd(String xdsd) {
		this.xdsd = xdsd;
	}

	public String getSyry() {
		return this.syry;
	}

	public void setSyry(String syry) {
		this.syry = syry;
	}

	public String getSkqbz() {
		return this.skqbz;
	}

	public void setSkqbz(String skqbz) {
		this.skqbz = skqbz;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getZt() {
		return this.zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
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