package net.chinanets.pojos;

import java.util.Date;


/**
 * CnstAssetbgjlData entity. @author dzj
 */

public class CnstAssetbgjlData  implements java.io.Serializable {


    // Fields    

     private String assetbgjlid;
     private String sqr;
     private String sqbm;
     private Date sqrq;
     private String bglx;
     private String asetetid;
     private String assetidcolumn;
     private String assettabnename;
     private String sqdbh;
     private String sqdbhcolumn;
     private String sqdtablename;
     private Date bgrq;
     private String memo;
     private String wpmc;
     private String sqdid;
     private String zt;


    // Constructors

    /** default constructor */
    public CnstAssetbgjlData() {
    }

    
    /** full constructor */
    public CnstAssetbgjlData(String sqr, String sqbm, Date sqrq, String bglx, String asetetid, String assetidcolumn, String assettabnename, String sqdbh, String sqdbhcolumn, String sqdtablename, Date bgrq, String memo, String wpmc, String sqdid, String zt) {
        this.sqr = sqr;
        this.sqbm = sqbm;
        this.sqrq = sqrq;
        this.bglx = bglx;
        this.asetetid = asetetid;
        this.assetidcolumn = assetidcolumn;
        this.assettabnename = assettabnename;
        this.sqdbh = sqdbh;
        this.sqdbhcolumn = sqdbhcolumn;
        this.sqdtablename = sqdtablename;
        this.bgrq = bgrq;
        this.memo = memo;
        this.wpmc = wpmc;
        this.sqdid = sqdid;
        this.zt = zt;
    }

   
    // Property accessors

    public String getAssetbgjlid() {
        return this.assetbgjlid;
    }
    
    public void setAssetbgjlid(String assetbgjlid) {
        this.assetbgjlid = assetbgjlid;
    }

    public String getSqr() {
        return this.sqr;
    }
    
    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getSqbm() {
        return this.sqbm;
    }
    
    public void setSqbm(String sqbm) {
        this.sqbm = sqbm;
    }

    public Date getSqrq() {
        return this.sqrq;
    }
    
    public void setSqrq(Date sqrq) {
        this.sqrq = sqrq;
    }

    public String getBglx() {
        return this.bglx;
    }
    
    public void setBglx(String bglx) {
        this.bglx = bglx;
    }

    public String getAsetetid() {
        return this.asetetid;
    }
    
    public void setAsetetid(String asetetid) {
        this.asetetid = asetetid;
    }

    public String getAssetidcolumn() {
        return this.assetidcolumn;
    }
    
    public void setAssetidcolumn(String assetidcolumn) {
        this.assetidcolumn = assetidcolumn;
    }

    public String getAssettabnename() {
        return this.assettabnename;
    }
    
    public void setAssettabnename(String assettabnename) {
        this.assettabnename = assettabnename;
    }

    public String getSqdbh() {
        return this.sqdbh;
    }
    
    public void setSqdbh(String sqdbh) {
        this.sqdbh = sqdbh;
    }

    public String getSqdbhcolumn() {
        return this.sqdbhcolumn;
    }
    
    public void setSqdbhcolumn(String sqdbhcolumn) {
        this.sqdbhcolumn = sqdbhcolumn;
    }

    public String getSqdtablename() {
        return this.sqdtablename;
    }
    
    public void setSqdtablename(String sqdtablename) {
        this.sqdtablename = sqdtablename;
    }

    public Date getBgrq() {
        return this.bgrq;
    }
    
    public void setBgrq(Date bgrq) {
        this.bgrq = bgrq;
    }

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getWpmc() {
        return this.wpmc;
    }
    
    public void setWpmc(String wpmc) {
        this.wpmc = wpmc;
    }

    public String getSqdid() {
        return this.sqdid;
    }
    
    public void setSqdid(String sqdid) {
        this.sqdid = sqdid;
    }

    public String getZt() {
        return this.zt;
    }
    
    public void setZt(String zt) {
        this.zt = zt;
    }
   








}