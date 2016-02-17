package net.chinanets.pojos;

import java.io.Serializable;
import java.util.Date;

/**
 * 相关资料实体
 * 
 * @author RLiuyong
 * 
 */
@SuppressWarnings("serial")
public class CnstDatumData implements Serializable {

	private String datumid;
	private String datumname;
	private String datumnameex;
	private String directoryid;
	private int filesize;
	private String filepath;
	private int usable = 1;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;

	public CnstDatumData() {
	}

	public CnstDatumData(String directoryid, String datumname,
			String datumnameex, int filesize, String filepath, int usable,
			Date ctime, String cuser, Date utime, String uuser, String memo) {
		this.directoryid = directoryid;
		this.datumname = datumname;
		this.datumnameex = datumnameex;
		this.filesize = filesize;
		this.filepath = filepath;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
		this.memo = memo;
	}

	public String getDatumid() {
		return this.datumid;
	}

	public void setDatumid(String datumid) {
		this.datumid = datumid;
	}

	public String getDirectoryid() {
		return this.directoryid;
	}

	public void setDirectoryid(String directoryid) {
		this.directoryid = directoryid;
	}

	public String getDatumname() {
		return this.datumname;
	}

	public void setDatumname(String datumname) {
		this.datumname = datumname;
	}

	public String getDatumnameex() {
		return this.datumnameex;
	}

	public void setDatumnameex(String datumnameex) {
		this.datumnameex = datumnameex;
	}

	public int getFilesize() {
		return this.filesize;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	public String getFilepath() {
		return this.filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
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
		if (this.utime == null) {
			this.utime = new Date();
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