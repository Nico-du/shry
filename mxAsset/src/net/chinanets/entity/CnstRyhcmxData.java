package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;

@SuppressWarnings("serial")
public class CnstRyhcmxData implements Serializable {

     private String ryhcmxid;
     private String ryhcmxname;
     private String ryhcmxbh;
     private String ryhcmxtype;
     private String rkdid;
     private String wppp;
     private String wpxh;
     private float jksl;
     private float sssl;
     private String wpdw;
     private float wpdj;
     private float wpje;
     private float ggsl;
     private String ggmc;
     private int usable=1;
     private Date ctime;
     private String cuser;
     private Date utime;
     private String uuser;
     private String memo;
     private String wpyc;
     private String wpcs;
     private String gbdm;
     private String syzt;
     
     private String fphm;
     private String zphm;
     private String fkfs;
     private String xjfpje;
     private String qtfkje;
     private String wpmc;

    public CnstRyhcmxData(String ryhcmxid, String ryhcmxname, String ryhcmxbh,
			String ryhcmxtype, String rkdid, String wppp, String wpxh,
			float jksl, float sssl, String wpdw, float wpdj, float wpje,
			float ggsl, String ggmc, int usable, Date ctime, String cuser,
			Date utime, String uuser, String memo, String wpyc, String wpcs,
			String gbdm, String syzt, String fphm, String zphm, String fkfs,
			String xjfpje, String qtfkje, String wpmc) {
		super();
		this.ryhcmxid = ryhcmxid;
		this.ryhcmxname = ryhcmxname;
		this.ryhcmxbh = ryhcmxbh;
		this.ryhcmxtype = ryhcmxtype;
		this.rkdid = rkdid;
		this.wppp = wppp;
		this.wpxh = wpxh;
		this.jksl = jksl;
		this.sssl = sssl;
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
		this.wpyc = wpyc;
		this.wpcs = wpcs;
		this.gbdm = gbdm;
		this.syzt = syzt;
		this.fphm = fphm;
		this.zphm = zphm;
		this.fkfs = fkfs;
		this.xjfpje = xjfpje;
		this.qtfkje = qtfkje;
		this.wpmc = wpmc;
	}

	public CnstRyhcmxData() {
    }

    public String getRyhcmxid() {
        return this.ryhcmxid;
    }
    
    public void setRyhcmxid(String ryhcmxid) {
        this.ryhcmxid = ryhcmxid;
    }

    public String getRyhcmxname() {
        return this.ryhcmxname;
    }
    
    public void setRyhcmxname(String ryhcmxname) {
        this.ryhcmxname = ryhcmxname;
    }

    public String getRyhcmxbh() {
        return this.ryhcmxbh;
    }
    
    public void setRyhcmxbh(String ryhcmxbh) {
        this.ryhcmxbh = ryhcmxbh;
    }

    public String getRyhcmxtype() {
        return this.ryhcmxtype;
    }
    
    public void setRyhcmxtype(String ryhcmxtype) {
        this.ryhcmxtype = ryhcmxtype;
    }
    
    public String getRkdid() {
		return rkdid;
	}

	public void setRkdid(String rkdid) {
		this.rkdid = rkdid;
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

    public float getJksl() {
        return this.jksl;
    }
    
    public void setJksl(float jksl) {
        this.jksl = jksl;
    }

    public float getSssl() {
        return this.sssl;
    }
    
    public void setSssl(float sssl) {
        this.sssl = sssl;
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

    public String getWpyc() {
        return this.wpyc;
    }
    
    public void setWpyc(String wpyc) {
        this.wpyc = wpyc;
    }

    public String getWpcs() {
        return this.wpcs;
    }
    
    public void setWpcs(String wpcs) {
        this.wpcs = wpcs;
    }


	public String getSyzt() {
		return syzt;
	}

	public void setSyzt(String syzt) {
		this.syzt = syzt;
	}

	public String getGbdm() {
		return gbdm;
	}

	public void setGbdm(String gbdm) {
		this.gbdm = gbdm;
	}

	public String getFphm() {
		return fphm;
	}

	public void setFphm(String fphm) {
		this.fphm = fphm;
	}

	public String getZphm() {
		return zphm;
	}

	public void setZphm(String zphm) {
		this.zphm = zphm;
	}

	public String getFkfs() {
		return fkfs;
	}

	public void setFkfs(String fkfs) {
		this.fkfs = fkfs;
	}

	public String getXjfpje() {
		return xjfpje;
	}

	public void setXjfpje(String xjfpje) {
		this.xjfpje = xjfpje;
	}

	public String getQtfkje() {
		return qtfkje;
	}

	public void setQtfkje(String qtfkje) {
		this.qtfkje = qtfkje;
	}

	public String getWpmc() {
		return wpmc;
	}

	public void setWpmc(String wpmc) {
		this.wpmc = wpmc;
	}
   








}