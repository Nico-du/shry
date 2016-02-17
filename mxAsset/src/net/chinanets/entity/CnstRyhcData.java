package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;

@SuppressWarnings("serial")
public class CnstRyhcData implements Serializable{

	private String ryhcid;
	private String ryhcbh;
	private String ryhcname;
	private Long glrid;
	private String glbmid;
	private int usable=1;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;
	public CnstRyhcData() {
	}

	public CnstRyhcData(Long glrid,String glbmid, String ryhcbh, String ryhcname,int usable,
			Date ctime, String cuser, Date utime, String uuser) {
		this.glrid = glrid;
		this.glbmid = glbmid;
		this.ryhcbh = ryhcbh;
		this.ryhcname = ryhcname;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
	}
	
	public String getRyhcid() {
		return this.ryhcid;
	}

	public void setRyhcid(String ryhcid) {
		this.ryhcid = ryhcid;
	}

	public Long getGlrid() {
		return glrid;
	}

	public void setGlrid(Long glrid) {
		this.glrid = glrid;
	}

	public String getGlbmid() {
		return glbmid;
	}

	public void setGlbmid(String glbmid) {
		this.glbmid = glbmid;
	}

	public String getRyhcbh() {
		return this.ryhcbh;
	}

	public void setRyhcbh(String ryhcbh) {
		this.ryhcbh = ryhcbh;
	}

	public String getRyhcname() {
		return this.ryhcname;
	}

	public void setRyhcname(String ryhcname) {
		this.ryhcname = ryhcname;
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