package net.chinanets.pojos;

import java.io.Serializable;
import java.util.Date;

/**
 * 资料目录
 * @author dzj
 *
 */
@SuppressWarnings("serial")
public class CnstDirectoryData implements Serializable {
	
	private String directoryid;
	private String directoryname;
	private String lastdirectoryid;
	private String directorypath;
	private int usable=1;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;

	public CnstDirectoryData() {
	}
	public CnstDirectoryData(String directoryname, String lastdirectoryid, String directorypath,
			int usable, Date ctime, String cuser, Date utime, String uuser,
			String memo) {
		this.directoryname = directoryname;
		this.lastdirectoryid = lastdirectoryid;
		this.directorypath=directorypath;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
		this.memo = memo;
	}
	public String getDirectoryid() {
		return this.directoryid;
	}

	public void setDirectoryid(String directoryid) {
		this.directoryid = directoryid;
	}

	public String getDirectoryname() {
		return this.directoryname;
	}

	public void setDirectoryname(String directoryname) {
		this.directoryname = directoryname;
	}

	public String getLastdirectoryid() {
		return this.lastdirectoryid;
	}

	public void setLastdirectoryid(String lastdirectoryid) {
		this.lastdirectoryid = lastdirectoryid;
	}

	public String getDirectorypath() {
		return directorypath;
	}
	public void setDirectorypath(String directorypath) {
		this.directorypath = directorypath;
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
    		this.utime=new Date();
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