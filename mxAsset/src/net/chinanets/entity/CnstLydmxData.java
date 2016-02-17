package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;

@SuppressWarnings("serial")
public class CnstLydmxData implements Serializable{

     private String lydmxid;
     private String lydmxname;
     private String lydmxbh;
     private String lydmxtype;
     private String lydid;
     private String wppp;
     private String wpxh;
     private float sbsl;
     private float shsl;
     private float sfsl;
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

    public CnstLydmxData() {
    }


    public CnstLydmxData(String lydmxid, String lydmxname, String lydmxbh,
			String lydmxtype, String lydid, String wppp, String wpxh,
			float sbsl, float shsl, float sfsl, String wpdw, float wpdj,
			float wpje, float ggsl, String ggmc, int usable, Date ctime,
			String cuser, Date utime, String uuser, String memo,
			 String gbdm, String pcbh,String wpmc) {
		super();
		this.lydmxid = lydmxid;
		this.lydmxname = lydmxname;
		this.lydmxbh = lydmxbh;
		this.lydmxtype = lydmxtype;
		this.lydid = lydid;
		this.wppp = wppp;
		this.wpxh = wpxh;
		this.sbsl = sbsl;
		this.shsl = shsl;
		this.sfsl = sfsl;
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


	public String getLydmxid() {
        return this.lydmxid;
    }
    
    public void setLydmxid(String lydmxid) {
        this.lydmxid = lydmxid;
    }
    
    public String getLydid() {
		return lydid;
	}

	public void setLydid(String lydid) {
		this.lydid = lydid;
	}
	
    public String getLydmxname() {
        return this.lydmxname;
    }
    
    public void setLydmxname(String lydmxname) {
        this.lydmxname = lydmxname;
    }

    public String getLydmxbh() {
        return this.lydmxbh;
    }
    
    public void setLydmxbh(String lydmxbh) {
        this.lydmxbh = lydmxbh;
    }

    public String getLydmxtype() {
        return this.lydmxtype;
    }
    
    public void setLydmxtype(String lydmxtype) {
        this.lydmxtype = lydmxtype;
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

    public float getSbsl() {
        return this.sbsl;
    }
    
    public void setSbsl(float sbsl) {
        this.sbsl = sbsl;
    }

    public float getShsl() {
        return this.shsl;
    }
    
    public void setShsl(float shsl) {
        this.shsl = shsl;
    }

    public float getSfsl() {
        return this.sfsl;
    }
    
    public void setSfsl(float sfsl) {
        this.sfsl = sfsl;
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