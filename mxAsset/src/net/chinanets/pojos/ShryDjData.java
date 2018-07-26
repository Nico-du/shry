package net.chinanets.pojos;

import java.util.Date;

/**
 * ShryDjData entity. @author MyEclipse Persistence Tools
 */

public class ShryDjData implements java.io.Serializable {

	// Fields

	private Long djid;
	private Long zcid;
	private String zcxh;
	private Long fyid;
	private String fyxh;
	private String djxh;
	private String djth;
	private String sjxh;
	private String sjbh;
	private String dy;
	private String dl;
	private String zs;
	private String zj;
	private String srgl;
	private String ysXl;
	private String ysCml;
	private String ysJs;
	private String ysWj;
	private String ysZc;
	private String txpwj;
	private String txdh;
	private String rzXj;
	private String rzZs;
	private String jkcd;
	private String cwcd;
	private String wsKzlx;
	private String wsSrpl;
	private String wsPwmdy;
	private String wsFjxbh;
	private String wsLjqdy;
	private String wsDjzl;
	private String wsDjwxWj;
	private String wsDjwxGd;
	private String wsTxcpWj;
	private String wsWxcpCs;
	private String syzt;
	private String isysdj;
	private String memo;
	private Date inputdate;
	private String inputuser;
	private Date updatedate;
	private String updateuser;

	// Constructors

	/** default constructor */
	public ShryDjData() {
	}

	/** full constructor */
	public ShryDjData(Long zcid, Long fyid, String djxh,
			String djth, String sjxh, String sjbh, String dy, String dl,
			String zs, String zj, String srgl, String ysXl, String ysCml,
			String ysJs, String ysWj, String ysZc, String txpwj, String txdh,
			String rzXj, String rzZs, String jkcd, String cwcd, String wsKzlx,
			String wsSrpl, String wsPwmdy, String wsFjxbh, String wsLjqdy,
			String wsDjzl, String wsDjwxWj, String wsDjwxGd, String wsTxcpWj,
			String wsWxcpCs, String syzt, String memo, Date inputdate,
			String inputuser, Date updatedate, String updateuser) {
		this.zcid = zcid;
		this.fyid = fyid;
		this.djxh = djxh;
		this.djth = djth;
		this.sjxh = sjxh;
		this.sjbh = sjbh;
		this.dy = dy;
		this.dl = dl;
		this.zs = zs;
		this.zj = zj;
		this.srgl = srgl;
		this.ysXl = ysXl;
		this.ysCml = ysCml;
		this.ysJs = ysJs;
		this.ysWj = ysWj;
		this.ysZc = ysZc;
		this.txpwj = txpwj;
		this.txdh = txdh;
		this.rzXj = rzXj;
		this.rzZs = rzZs;
		this.jkcd = jkcd;
		this.cwcd = cwcd;
		this.wsKzlx = wsKzlx;
		this.wsSrpl = wsSrpl;
		this.wsPwmdy = wsPwmdy;
		this.wsFjxbh = wsFjxbh;
		this.wsLjqdy = wsLjqdy;
		this.wsDjzl = wsDjzl;
		this.wsDjwxWj = wsDjwxWj;
		this.wsDjwxGd = wsDjwxGd;
		this.wsTxcpWj = wsTxcpWj;
		this.wsWxcpCs = wsWxcpCs;
		this.syzt = syzt;
		this.memo = memo;
		this.inputdate = inputdate;
		this.inputuser = inputuser;
		this.updatedate = updatedate;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Long getDjid() {
		return this.djid;
	}

	public void setDjid(Long djid) {
		this.djid = djid;
	}

	public Long getZcid() {
		return this.zcid;
	}

	public void setZcid(Long zcid) {
		this.zcid = zcid;
	}

	public Long getFyid() {
		return this.fyid;
	}

	public void setFyid(Long fyid) {
		this.fyid = fyid;
	}

	public String getDjxh() {
		return this.djxh;
	}

	public void setDjxh(String djxh) {
		this.djxh = djxh;
	}

	public String getDjth() {
		return this.djth;
	}

	public void setDjth(String djth) {
		this.djth = djth;
	}

	public String getSjxh() {
		return this.sjxh;
	}

	public void setSjxh(String sjxh) {
		this.sjxh = sjxh;
	}

	public String getSjbh() {
		return this.sjbh;
	}

	public void setSjbh(String sjbh) {
		this.sjbh = sjbh;
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

	public String getZj() {
		return this.zj;
	}

	public void setZj(String zj) {
		this.zj = zj;
	}

	public String getSrgl() {
		return this.srgl;
	}

	public void setSrgl(String srgl) {
		this.srgl = srgl;
	}

	public String getYsXl() {
		return this.ysXl;
	}

	public void setYsXl(String ysXl) {
		this.ysXl = ysXl;
	}

	public String getYsCml() {
		return this.ysCml;
	}

	public void setYsCml(String ysCml) {
		this.ysCml = ysCml;
	}

	public String getYsJs() {
		return this.ysJs;
	}

	public void setYsJs(String ysJs) {
		this.ysJs = ysJs;
	}

	public String getYsWj() {
		return this.ysWj;
	}

	public void setYsWj(String ysWj) {
		this.ysWj = ysWj;
	}

	public String getYsZc() {
		return this.ysZc;
	}

	public void setYsZc(String ysZc) {
		this.ysZc = ysZc;
	}

	public String getTxpwj() {
		return this.txpwj;
	}

	public void setTxpwj(String txpwj) {
		this.txpwj = txpwj;
	}

	public String getTxdh() {
		return this.txdh;
	}

	public void setTxdh(String txdh) {
		this.txdh = txdh;
	}

	public String getRzXj() {
		return this.rzXj;
	}

	public void setRzXj(String rzXj) {
		this.rzXj = rzXj;
	}

	public String getRzZs() {
		return this.rzZs;
	}

	public void setRzZs(String rzZs) {
		this.rzZs = rzZs;
	}

	public String getJkcd() {
		return this.jkcd;
	}

	public void setJkcd(String jkcd) {
		this.jkcd = jkcd;
	}

	public String getCwcd() {
		return this.cwcd;
	}

	public void setCwcd(String cwcd) {
		this.cwcd = cwcd;
	}

	public String getWsKzlx() {
		return this.wsKzlx;
	}

	public void setWsKzlx(String wsKzlx) {
		this.wsKzlx = wsKzlx;
	}

	public String getWsSrpl() {
		return this.wsSrpl;
	}

	public void setWsSrpl(String wsSrpl) {
		this.wsSrpl = wsSrpl;
	}

	public String getWsPwmdy() {
		return this.wsPwmdy;
	}

	public void setWsPwmdy(String wsPwmdy) {
		this.wsPwmdy = wsPwmdy;
	}

	public String getWsFjxbh() {
		return this.wsFjxbh;
	}

	public void setWsFjxbh(String wsFjxbh) {
		this.wsFjxbh = wsFjxbh;
	}

	public String getWsLjqdy() {
		return this.wsLjqdy;
	}

	public void setWsLjqdy(String wsLjqdy) {
		this.wsLjqdy = wsLjqdy;
	}

	public String getWsDjzl() {
		return this.wsDjzl;
	}

	public void setWsDjzl(String wsDjzl) {
		this.wsDjzl = wsDjzl;
	}

	public String getWsDjwxWj() {
		return this.wsDjwxWj;
	}

	public void setWsDjwxWj(String wsDjwxWj) {
		this.wsDjwxWj = wsDjwxWj;
	}

	public String getWsDjwxGd() {
		return this.wsDjwxGd;
	}

	public void setWsDjwxGd(String wsDjwxGd) {
		this.wsDjwxGd = wsDjwxGd;
	}

	public String getWsTxcpWj() {
		return this.wsTxcpWj;
	}

	public void setWsTxcpWj(String wsTxcpWj) {
		this.wsTxcpWj = wsTxcpWj;
	}

	public String getWsWxcpCs() {
		return this.wsWxcpCs;
	}

	public void setWsWxcpCs(String wsWxcpCs) {
		this.wsWxcpCs = wsWxcpCs;
	}

	public String getSyzt() {
		return this.syzt;
	}

	public void setSyzt(String syzt) {
		this.syzt = syzt;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	
	
	public String getIsysdj() {
		return isysdj;
	}

	public void setIsysdj(String isysdj) {
		this.isysdj = isysdj;
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

}