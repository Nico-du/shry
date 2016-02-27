package net.chinanets.entity;

import java.util.Date;


/**
 * CnstNotice entity. @author dzj
 */

public class CnstNotice  implements java.io.Serializable {


    // Fields    

     private String ntid;
     private String wfid;
     private String nttype;
     private String isread;
     private Date ctime;
     private String cuser;
     private Date utime;
     private String uuser;
     private String tzrid;


    // Constructors

    /** default constructor */
    public CnstNotice() {
    }

    
    /** full constructor */
    public CnstNotice(String wfid, String nttype, String isread, Date ctime, String cuser, Date utime, String uuser, String tzrid) {
        this.wfid = wfid;
        this.nttype = nttype;
        this.isread = isread;
        this.ctime = ctime;
        this.cuser = cuser;
        this.utime = utime;
        this.uuser = uuser;
        this.tzrid = tzrid;
    }

   
    // Property accessors

    public String getNtid() {
        return this.ntid;
    }
    
    public void setNtid(String ntid) {
        this.ntid = ntid;
    }

    public String getWfid() {
        return this.wfid;
    }
    
    public void setWfid(String wfid) {
        this.wfid = wfid;
    }

    public String getNttype() {
        return this.nttype;
    }
    
    public void setNttype(String nttype) {
        this.nttype = nttype;
    }

    public String getIsread() {
        return this.isread;
    }
    
    public void setIsread(String isread) {
        this.isread = isread;
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


	public String getTzrid() {
		return tzrid;
	}


	public void setTzrid(String tzrid) {
		this.tzrid = tzrid;
	}
   








}