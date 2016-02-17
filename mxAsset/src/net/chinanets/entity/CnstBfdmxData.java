package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class CnstBfdmxData implements Serializable {

	private String bfdmxid;
	private String bfdmxname;
	private String bfdmxbh;
	private String bfdmxtype;
	private String bfdid;
	private String hsfzr;
	private String hsdw;
	private String hsrlx;
	private float sqbfsl;
	private float shbfsl;
	private float sjbfsl;
	private float yghsje;
	private float sjhsje;
	private int usable=1;
	private Date ctime;
	private String cuser;
	private Date utime;
	private String uuser;
	private String memo;
	private String assetTableName;
	private String assetId;
	private String pcbh;
	private String assetIdColumn;
	private String assetBh;
	private String wpmc;
	
	public CnstBfdmxData() {
	}
	

	public CnstBfdmxData(String bfdmxid, String bfdmxname, String bfdmxbh,
			String bfdmxtype, String bfdid, String hsfzr, String hsdw,
			String hsrlx, float sqbfsl, float shbfsl, float sjbfsl,
			float yghsje, float sjhsje, int usable, Date ctime, String cuser,
			Date utime, String uuser, String memo, String assetTableName,
			String assetId, String pcbh, String assetIdColumn,String assetBh,String wpmc) {
		super();
		this.bfdmxid = bfdmxid;
		this.bfdmxname = bfdmxname;
		this.bfdmxbh = bfdmxbh;
		this.bfdmxtype = bfdmxtype;
		this.bfdid = bfdid;
		this.hsfzr = hsfzr;
		this.hsdw = hsdw;
		this.hsrlx = hsrlx;
		this.sqbfsl = sqbfsl;
		this.shbfsl = shbfsl;
		this.sjbfsl = sjbfsl;
		this.yghsje = yghsje;
		this.sjhsje = sjhsje;
		this.usable = usable;
		this.ctime = ctime;
		this.cuser = cuser;
		this.utime = utime;
		this.uuser = uuser;
		this.memo = memo;
		this.assetTableName = assetTableName;
		this.assetId = assetId;
		this.pcbh = pcbh;
		this.assetIdColumn = assetIdColumn;
		this.assetBh = assetBh;
		this.wpmc = wpmc;
	}



	public String getBfdmxid() {
		return this.bfdmxid;
	}

	public void setBfdmxid(String bfdmxid) {
		this.bfdmxid = bfdmxid;
	}

	public String getBfdid() {
		return bfdid;
	}

	public void setBfdid(String bfdid) {
		this.bfdid = bfdid;
	}

	public String getBfdmxname() {
		return this.bfdmxname;
	}

	public void setBfdmxname(String bfdmxname) {
		this.bfdmxname = bfdmxname;
	}

	public String getBfdmxbh() {
		return this.bfdmxbh;
	}

	public void setBfdmxbh(String bfdmxbh) {
		this.bfdmxbh = bfdmxbh;
	}

	public String getBfdmxtype() {
		return this.bfdmxtype;
	}

	public void setBfdmxtype(String bfdmxtype) {
		this.bfdmxtype = bfdmxtype;
	}

	public String getHsfzr() {
		return this.hsfzr;
	}

	public void setHsfzr(String hsfzr) {
		this.hsfzr = hsfzr;
	}

	public String getHsdw() {
		return this.hsdw;
	}

	public void setHsdw(String hsdw) {
		this.hsdw = hsdw;
	}

	public String getHsrlx() {
		return this.hsrlx;
	}

	public void setHsrlx(String hsrlx) {
		this.hsrlx = hsrlx;
	}

	public float getSqbfsl() {
		return this.sqbfsl;
	}

	public void setSqbfsl(float sqbfsl) {
		this.sqbfsl = sqbfsl;
	}

	public float getShbfsl() {
		return this.shbfsl;
	}

	public void setShbfsl(float shbfsl) {
		this.shbfsl = shbfsl;
	}

	public float getSjbfsl() {
		return this.sjbfsl;
	}

	public void setSjbfsl(float sjbfsl) {
		this.sjbfsl = sjbfsl;
	}

	public float getYghsje() {
		return this.yghsje;
	}

	public void setYghsje(float yghsje) {
		this.yghsje = yghsje;
	}

	public float getSjhsje() {
		return this.sjhsje;
	}

	public void setSjhsje(float sjhsje) {
		this.sjhsje = sjhsje;
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

	public String getAssetTableName() {
		return assetTableName;
	}

	public void setAssetTableName(String assetTableName) {
		this.assetTableName = assetTableName;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}



	public String getPcbh() {
		return pcbh;
	}



	public void setPcbh(String pcbh) {
		this.pcbh = pcbh;
	}



	public String getAssetIdColumn() {
		return assetIdColumn;
	}



	public void setAssetIdColumn(String assetIdColumn) {
		this.assetIdColumn = assetIdColumn;
	}


	public String getAssetBh() {
		return assetBh;
	}


	public void setAssetBh(String assetBh) {
		this.assetBh = assetBh;
	}


	public String getWpmc() {
		return wpmc;
	}


	public void setWpmc(String wpmc) {
		this.wpmc = wpmc;
	}

}