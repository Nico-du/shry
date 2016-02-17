package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;


@SuppressWarnings("serial")
public class CnstLydData implements Serializable {

	private String lydid;
	private String lydbh;
	private Long slrid;
	private String slbmid;
	private Date slsj;
	private String lyzt;
	private String lybz;
	private Long bmldid;
	private Long yldid;
	private Long glyid;
	private int usable=1;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;

	public CnstLydData() {
	}

	public CnstLydData(Long slrid, String slbmid, String lydbh, Date slsj,
			String lyzt, String lybz, int usable, Date ctime, String cuser,
			Date utime, String uuser) {
		this.slrid = slrid;
		this.slbmid = slbmid;
		this.lydbh = lydbh;
		this.slsj = slsj;
		this.lyzt = lyzt;
		this.lybz = lybz;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
	}
	
	public String getLydid() {
		return this.lydid;
	}

	public void setLydid(String lydid) {
		this.lydid = lydid;
	}

	public Long getSlrid() {
		return slrid;
	}

	public void setSlrid(Long slrid) {
		this.slrid = slrid;
	}

	public String getSlbmid() {
		return slbmid;
	}

	public void setSlbmid(String slbmid) {
		this.slbmid = slbmid;
	}

	public String getLydbh() {
		return this.lydbh;
	}

	public void setLydbh(String lydbh) {
		this.lydbh = lydbh;
	}

	public Date getSlsj() {
		return this.slsj;
	}

	public void setSlsj(Date slsj) {
		this.slsj = slsj;
	}

	public String getLyzt() {
		return this.lyzt;
	}

	public void setLyzt(String lyzt) {
		this.lyzt = lyzt;
	}

	public String getLybz() {
		return this.lybz;
	}

	public void setLybz(String lybz) {
		this.lybz = lybz;
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