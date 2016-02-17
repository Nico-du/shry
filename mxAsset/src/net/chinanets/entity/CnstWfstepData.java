package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;

@SuppressWarnings("serial")
public class CnstWfstepData implements Serializable {

	private String wfstepid;
	private String wfstepname;
	private String wfsteptype;
	private String stepbtnname;
	private int steporder;
	private String nextstep;
	private String defaultuser;
	private int usable=1;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;
	public CnstWfstepData() {
	}

	public CnstWfstepData(String wfstepname, String wfsteptype,
			String stepbtnname, int steporder, int usable, Date ctime,
			String cuser, Date utime, String uuser) {
		this.wfstepname = wfstepname;
		this.wfsteptype = wfsteptype;
		this.stepbtnname = stepbtnname;
		this.steporder = steporder;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
	}

	public String getWfstepid() {
		return this.wfstepid;
	}

	public void setWfstepid(String wfstepid) {
		this.wfstepid = wfstepid;
	}

	public String getWfstepname() {
		return this.wfstepname;
	}

	public void setWfstepname(String wfstepname) {
		this.wfstepname = wfstepname;
	}

	public String getWfsteptype() {
		return this.wfsteptype;
	}

	public void setWfsteptype(String wfsteptype) {
		this.wfsteptype = wfsteptype;
	}

	public String getStepbtnname() {
		return this.stepbtnname;
	}

	public void setStepbtnname(String stepbtnname) {
		this.stepbtnname = stepbtnname;
	}

	public int getSteporder() {
		return this.steporder;
	}

	public void setSteporder(int steporder) {
		this.steporder = steporder;
	}

	public String getNextstep() {
		return this.nextstep;
	}

	public void setNextstep(String nextstep) {
		this.nextstep = nextstep;
	}

	public String getDefaultuser() {
		return this.defaultuser;
	}

	public void setDefaultuser(String defaultuser) {
		this.defaultuser = defaultuser;
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