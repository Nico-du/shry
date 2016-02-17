package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class CnstTkdmxData implements Serializable {

	private String tkdmxid;
	private String tkdmxname;
	private String tkdmxbh;
	private String tkdmxtype;
	private String tkdid;
	private String wppp;
	private String wpxh;
	private float tksl;
	private float shsl;
	private float sjsl;
	private String wpdw;
	private float wpdj;
	private float wpje;
	private float ggsl;
	private String ggmc;
	private int usable;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;
    private String gbdm;
    private String pcbh;
    private String wpmc;
    
	public CnstTkdmxData() {
	}

	public CnstTkdmxData(String tkdmxname,String tkdmxbh, String tkdmxtype,String tkdid,
			String wppp, String wpxh,float tksl, float shsl, float sjsl, String wpdw, float wpdj,
			float wpje, float ggsl, String ggmc, int usable, Date ctime,
			String cuser, Date utime, String uuser, String memo, String gbdm,String pcbh,String wpmc) {
		this.tkdmxname = tkdmxname;
		this.tkdmxbh = tkdmxbh;
		this.tkdmxtype = tkdmxtype;
		this.tkdid=tkdid;
		this.wppp = wppp;
		this.wpxh = wpxh;
		this.tksl = tksl;
		this.shsl = shsl;
		this.sjsl = sjsl;
		this.wpdw = wpdw;
		this.wpdj = wpdj;
		this.wpje = wpje;
		this.ggsl = ggsl;
		this.ggmc = ggmc;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
		this.memo = memo;
		this.gbdm = gbdm;
		this.pcbh = pcbh;
		this.wpmc = wpmc;
	}

	public String getTkdmxid() {
		return this.tkdmxid;
	}

	public void setTkdmxid(String tkdmxid) {
		this.tkdmxid = tkdmxid;
	}

	public String getTkdid() {
		return tkdid;
	}

	public void setTkdid(String tkdid) {
		this.tkdid = tkdid;
	}

	public String getTkdmxname() {
		return this.tkdmxname;
	}

	public void setTkdmxname(String tkdmxname) {
		this.tkdmxname = tkdmxname;
	}

	public String getTkdmxbh() {
		return this.tkdmxbh;
	}

	public void setTkdmxbh(String tkdmxbh) {
		this.tkdmxbh = tkdmxbh;
	}

	public String getTkdmxtype() {
		return this.tkdmxtype;
	}

	public void setTkdmxtype(String tkdmxtype) {
		this.tkdmxtype = tkdmxtype;
	}

	public String getWppp() {
		return this.wppp;
	}

	public void setWppp(String wppp) {
		this.wppp = wppp;
	}

	public String getWpxh() {
		return this.wpxh;
	}

	public void setWpxh(String wpxh) {
		this.wpxh = wpxh;
	}

	public float getTksl() {
		return this.tksl;
	}

	public void setTksl(float tksl) {
		this.tksl = tksl;
	}

	public float getShsl() {
		return this.shsl;
	}

	public void setShsl(float shsl) {
		this.shsl = shsl;
	}

	public float getSjsl() {
		return this.sjsl;
	}

	public void setSjsl(float sjsl) {
		this.sjsl = sjsl;
	}

	public String getWpdw() {
		return this.wpdw;
	}

	public void setWpdw(String wpdw) {
		this.wpdw = wpdw;
	}

	public float getWpdj() {
		return this.wpdj;
	}

	public void setWpdj(float wpdj) {
		this.wpdj = wpdj;
	}

	public float getWpje() {
		return this.wpje;
	}

	public void setWpje(float wpje) {
		this.wpje = wpje;
	}

	public float getGgsl() {
		return this.ggsl;
	}

	public void setGgsl(float ggsl) {
		this.ggsl = ggsl;
	}

	public String getGgmc() {
		return this.ggmc;
	}

	public void setGgmc(String ggmc) {
		this.ggmc = ggmc;
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

	public String getGbdm() {
		return gbdm;
	}

	public void setGbdm(String gbdm) {
		this.gbdm = gbdm;
	}

	public String getPcbh() {
		return pcbh;
	}

	public void setPcbh(String pcbh) {
		this.pcbh = pcbh;
	}

	public String getWpmc() {
		return wpmc;
	}

	public void setWpmc(String wpmc) {
		this.wpmc = wpmc;
	}
}