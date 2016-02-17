package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;

@SuppressWarnings("serial")
public class CnstRkdData implements Serializable {
	
     private String rkdid;
     private String rkdbh;
     private String jhdw;
     private String fphm;
     private String rkdd;
     private Date rkrq;
     private int usable=1;
     private Date ctime;
     private String cuser;
     private Date utime;
     private String uuser;
     private String memo;

    public CnstRkdData() {
    }

    public CnstRkdData(String rkdbh, Date rkrq, int usable, Date ctime, String cuser, Date utime, String uuser) {
        this.rkdbh = rkdbh;
        this.rkrq = rkrq;
        this.usable = usable;
        this.ctime = ctime;
        this.cuser = cuser;
        this.utime = utime;
        this.uuser = uuser;
    }
    
    public CnstRkdData(String rkdbh, String jhdw, String fphm,String rkdd, Date rkrq, int usable, Date ctime, String cuser, Date utime, String uuser, String memo) {
        this.rkdbh = rkdbh;
        this.jhdw = jhdw;
        this.fphm = fphm;
        this.rkdd = rkdd;
        this.rkrq = rkrq;
        this.usable = usable;
        this.ctime = ctime;
        this.cuser = cuser;
        this.utime = utime;
        this.uuser = uuser;
        this.memo = memo;
    }

    public String getRkdid() {
        return this.rkdid;
    }
    
    public void setRkdid(String rkdid) {
        this.rkdid = rkdid;
    }

    public String getRkdbh() {
        return this.rkdbh;
    }
    
    public void setRkdbh(String rkdbh) {
        this.rkdbh = rkdbh;
    }

    public String getJhdw() {
        return this.jhdw;
    }
    
    public void setJhdw(String jhdw) {
        this.jhdw = jhdw;
    }

    public String getFphm() {
        return this.fphm;
    }
    
    public void setFphm(String fphm) {
        this.fphm = fphm;
    }

    public String getRkdd() {
        return this.rkdd;
    }
    
    public void setRkdd(String rkdd) {
        this.rkdd = rkdd;
    }

    public Date getRkrq() {
        return this.rkrq;
    }
    
    public void setRkrq(Date rkrq) {
        this.rkrq = rkrq;
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