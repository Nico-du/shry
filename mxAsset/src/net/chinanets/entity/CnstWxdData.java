package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;

@SuppressWarnings("serial")
public class CnstWxdData implements Serializable {

	private String wxdid;
	private String wxdbh;
	private Long sqrid;
	private String sqbmid;
	private Date sqrq;
	private String wxfpbh;
	private String wxsqzt;
	private String wxsqbz;
	private Long bmldid;
	private Long yldid;
	private Long glyid;
	private int usable=1;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;

	public CnstWxdData() {
	}

	public CnstWxdData(String wxdbh,Long sqrid,String sqbmid, Date sqrq,
			String wxfpbh, String wxsqzt, String wxsqbz,
			Long bmldid, Long yldid, Long glyid, int usable,
			Date ctime, String cuser, Date utime, String uuser, String memo){
		this.wxdbh = wxdbh;
		this.sqrid=sqrid;
		this.sqbmid=sqbmid;
		this.sqrq = sqrq;
		this.wxfpbh = wxfpbh;
		this.wxsqzt = wxsqzt;
		this.wxsqbz = wxsqbz;
		this.bmldid = bmldid;
		this.yldid = yldid;
		this.glyid = glyid;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
		this.memo = memo;
	}

	public String getWxdid() {
		return this.wxdid;
	}

	public void setWxdid(String wxdid) {
		this.wxdid = wxdid;
	}

	public String getWxdbh() {
		return this.wxdbh;
	}

	public void setWxdbh(String wxdbh) {
		this.wxdbh = wxdbh;
	}

	public Long getSqrid() {
		return sqrid;
	}

	public void setSqrid(Long sqrid) {
		this.sqrid = sqrid;
	}

	public String getSqbmid() {
		return sqbmid;
	}

	public void setSqbmid(String sqbmid) {
		this.sqbmid = sqbmid;
	}

	public Date getSqrq() {
		return this.sqrq;
	}

	public void setSqrq(Date sqrq) {
		this.sqrq = sqrq;
	}

	public String getWxfpbh() {
		return this.wxfpbh;
	}

	public void setWxfpbh(String wxfpbh) {
		this.wxfpbh = wxfpbh;
	}

	public String getWxsqzt() {
		return this.wxsqzt;
	}

	public void setWxsqzt(String wxsqzt) {
		this.wxsqzt = wxsqzt;
	}

	public String getWxsqbz() {
		return this.wxsqbz;
	}

	public void setWxsqbz(String wxsqbz) {
		this.wxsqbz = wxsqbz;
	}

	public Long getBmldid() {
		return this.bmldid;
	}

	public void setBmldid(Long bmldid) {
		this.bmldid = bmldid;
	}

	public Long getYldid() {
		return this.yldid;
	}

	public void setYldid(Long yldid) {
		this.yldid = yldid;
	}

	public Long getGlyid() {
		return this.glyid;
	}

	public void setGlyid(Long glyid) {
		this.glyid = glyid;
	}

	public int getUsable() {
		return this.usable;
	}

	public void setUsable(int usable) {
		this.usable = usable;
	}

	public Date getCtime() {
		return this.ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getCuser() {
		return this.cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public Date getUtime() {
		if(this.utime==null){
			this.utime=DateHelper.GetCurrentUtilDateTimeClass();
		}
		return this.utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public String getUuser() {
		return this.uuser;
	}

	public void setUuser(String uuser) {
		this.uuser = uuser;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}