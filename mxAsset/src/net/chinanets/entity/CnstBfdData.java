package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class CnstBfdData implements Serializable{

	private String bfdid;
	private String bfdbh;
	private Long djrid;
	private String djbmid;
	private Date djrq;
	private float bfzje;
	private String bfsqzt;
	private String bfsqbz;
	private Long bmldid;
	private Long yldid;
	private Long glyid;
	private int usable=1;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;

	public CnstBfdData() {
	}
	
	public CnstBfdData(String bfdbh,Long djrid,String djbmid, Date djrq,
			float bfzje, String bfsqzt, String bfsqbz, Long bmldid,
			Long yldid, Long glyid, int usable, Date ctime,
			String cuser, Date utime, String uuser, String memo) {
		this.bfdbh = bfdbh;
		this.djrid=djrid;
		this.djbmid=djbmid;
		this.djrq = djrq;
		this.bfzje = bfzje;
		this.bfsqzt = bfsqzt;
		this.bfsqbz = bfsqbz;
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

	public String getBfdid() {
		return this.bfdid;
	}

	public void setBfdid(String bfdid) {
		this.bfdid = bfdid;
	}

	public Long getDjrid() {
		return djrid;
	}

	public void setDjrid(Long djrid) {
		this.djrid = djrid;
	}

	public String getDjbmid() {
		return djbmid;
	}

	public void setDjbmid(String djbmid) {
		this.djbmid = djbmid;
	}

	public String getBfdbh() {
		return this.bfdbh;
	}

	public void setBfdbh(String bfdbh) {
		this.bfdbh = bfdbh;
	}

	public Date getDjrq() {
		return this.djrq;
	}

	public void setDjrq(Date djrq) {
		this.djrq = djrq;
	}

	public float getBfzje() {
		return this.bfzje;
	}

	public void setBfzje(float bfzje) {
		this.bfzje = bfzje;
	}

	public String getBfsqzt() {
		return this.bfsqzt;
	}

	public void setBfsqzt(String bfsqzt) {
		this.bfsqzt = bfsqzt;
	}

	public String getBfsqbz() {
		return this.bfsqbz;
	}

	public void setBfsqbz(String bfsqbz) {
		this.bfsqbz = bfsqbz;
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