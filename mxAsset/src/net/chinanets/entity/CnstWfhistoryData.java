package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;

@SuppressWarnings("serial")
public class CnstWfhistoryData implements Serializable {

	private String wfhistoryid;
	private String wfhistoryname;
	private String wfhstepid;
	private String wfdataid;
	private String wfdouserid;
	private String wfdocontent;
	private int usable=1;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;

	public CnstWfhistoryData() {
	}

	public CnstWfhistoryData(String wfhistoryname, String wfhstepid,
			String wfdataid, String wfdouserid, int usable, Date ctime,
			String cuser, Date utime, String uuser) {
		this.wfhistoryname = wfhistoryname;
		this.wfhstepid = wfhstepid;
		this.wfdataid = wfdataid;
		this.wfdouserid = wfdouserid;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
	}

	public CnstWfhistoryData(String wfhistoryname, String wfhstepid,
			String wfdataid, String wfdouserid, String wfdocontent,
			int usable, Date ctime, String cuser, Date utime, String uuser,
			String memo) {
		this.wfhistoryname = wfhistoryname;
		this.wfhstepid = wfhstepid;
		this.wfdataid = wfdataid;
		this.wfdouserid = wfdouserid;
		this.wfdocontent = wfdocontent;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
		this.memo = memo;
	}

	public String getWfhistoryid() {
		return this.wfhistoryid;
	}

	public void setWfhistoryid(String wfhistoryid) {
		this.wfhistoryid = wfhistoryid;
	}

	public String getWfhistoryname() {
		return this.wfhistoryname;
	}

	public void setWfhistoryname(String wfhistoryname) {
		this.wfhistoryname = wfhistoryname;
	}

	public String getWfhstepid() {
		return this.wfhstepid;
	}

	public void setWfhstepid(String wfhstepid) {
		this.wfhstepid = wfhstepid;
	}

	public String getWfdataid() {
		return this.wfdataid;
	}

	public void setWfdataid(String wfdataid) {
		this.wfdataid = wfdataid;
	}

	public String getWfdouserid() {
		return this.wfdouserid;
	}

	public void setWfdouserid(String wfdouserid) {
		this.wfdouserid = wfdouserid;
	}

	public String getWfdocontent() {
		return this.wfdocontent;
	}

	public void setWfdocontent(String wfdocontent) {
		this.wfdocontent = wfdocontent;
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