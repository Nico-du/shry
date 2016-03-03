package net.chinanets.pojos;

import java.lang.Long;
import java.util.Date;

/**
 * ShrySydData entity. @author dzj
 */

public class ShrySydData implements java.io.Serializable {

	// Fields

	private Long lxdid;
	private Long zcid;
	private Long fyid;
	private Long djid;
	private String sydx;
	private Date syrq;
	private String lxdh;
	private String fjxs;
	private String syfd;
	private String ckmj;
	private String syfs;
	private String fyzj;
	private String zcxh;
	private String yps;
	private String fyxh;
	private String dqy;
	private String symd;
	private String kqwd;
	private String sply;
	private String xdsd;
	private String syry;
	private String skqbz;
	private String memo;
	private String syzt;
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
			String zcxh, String yps, String fyxh, String dqy, String symd,
			String kqwd, String sply, String xdsd, String syry, String skqbz,
			String memo, String syzt, Date inputdate, String inputuser,
			Date updatedate, String updateuser) {
		this.zcid = zcid;
		this.syrq = syrq;
		this.lxdh = lxdh;
		this.fjxs = fjxs;
		this.syfd = syfd;
		this.ckmj = ckmj;
		this.syfs = syfs;
		this.fyzj = fyzj;
		this.zcxh = zcxh;
		this.yps = yps;
		this.fyxh = fyxh;
		this.dqy = dqy;
		this.symd = symd;
		this.kqwd = kqwd;
		this.sply = sply;
		this.xdsd = xdsd;
		this.syry = syry;
		this.skqbz = skqbz;
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

	public Long getDjid() {
		return djid;
	}

	public void setDjid(Long djid) {
		this.djid = djid;
	}

	public String getSydx() {
		return sydx;
	}

	public void setSydx(String sydx) {
		this.sydx = sydx;
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
	public String getYps() {
		return this.yps;
	}

	public void setYps(String yps) {
		this.yps = yps;
	}

	public String getZcxh() {
		return zcxh;
	}

	public void setZcxh(String zcxh) {
		this.zcxh = zcxh;
	}

	public String getFyxh() {
		return fyxh;
	}

	public void setFyxh(String fyxh) {
		this.fyxh = fyxh;
	}

	public String getDqy() {
		return this.dqy;
	}

	public void setDqy(String dqy) {
		this.dqy = dqy;
	}

	public String getSymd() {
		return this.symd;
	}

	public void setSymd(String symd) {
		this.symd = symd;
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


	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSyzt() {
		return syzt;
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