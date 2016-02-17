package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;

@SuppressWarnings("serial")
public class CnstTkdData implements Serializable{
	
	private String tkdid;
	private String tkdbh;
	private Long tkrid;
	private String tkbmid;
	private Date tkrq;
	private int usable;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;
	private String zt;
	
	public CnstTkdData() {
	}

	public CnstTkdData(Long tkrid,String tkbmid, String tkdbh, Date tkrq,
			int usable, Date ctime, String cuser, Date utime, String uuser, String zt) {
		this.tkrid=tkrid;
		this.tkbmid=tkbmid;
		this.tkdbh = tkdbh;
		this.tkrq = tkrq;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
		this.zt = zt;
	}

	public String getTkdid() {
		return this.tkdid;
	}

	public void setTkdid(String tkdid) {
		this.tkdid = tkdid;
	}

	public Long getTkrid() {
		return tkrid;
	}

	public void setTkrid(Long tkrid) {
		this.tkrid = tkrid;
	}

	public String getTkbmid() {
		return tkbmid;
	}

	public void setTkbmid(String tkbmid) {
		this.tkbmid = tkbmid;
	}

	public String getTkdbh() {
		return this.tkdbh;
	}

	public void setTkdbh(String tkdbh) {
		this.tkdbh = tkdbh;
	}

	public Date getTkrq() {
		return this.tkrq;
	}

	public void setTkrq(Date tkrq) {
		this.tkrq = tkrq;
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

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
}