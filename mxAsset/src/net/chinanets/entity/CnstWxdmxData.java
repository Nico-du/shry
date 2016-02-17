package net.chinanets.entity;

import java.io.Serializable;
import java.util.Date;

import net.chinanets.utils.helper.DateHelper;

@SuppressWarnings("serial")
public class CnstWxdmxData implements Serializable {
	
	private String wxdmxid;
	private String wxdmxname;
	private String wxdmxbh;
	private String wxdmxtype;
	private String wxdid;
	private String wxfzr;
	private String wxdw;
	private String lxfs;
	private float sqwxsl;
	private float shwxsl;
	private float sjwxsl;
	private float wxje;
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

	public CnstWxdmxData() {}

	public CnstWxdmxData(String wxdmxid, String wxdmxname, String wxdmxbh,
			String wxdmxtype, String wxdid, String wxfzr, String wxdw,
			String lxfs, float sqwxsl, float shwxsl, float sjwxsl, float wxje,
			int usable, Date ctime, String cuser, Date utime, String uuser,
			String memo, String assetTableName, String assetId, String pcbh,
			String assetIdColumn,String assetBh,String wpmc) {
		super();
		this.wxdmxid = wxdmxid;
		this.wxdmxname = wxdmxname;
		this.wxdmxbh = wxdmxbh;
		this.wxdmxtype = wxdmxtype;
		this.wxdid = wxdid;
		this.wxfzr = wxfzr;
		this.wxdw = wxdw;
		this.lxfs = lxfs;
		this.sqwxsl = sqwxsl;
		this.shwxsl = shwxsl;
		this.sjwxsl = sjwxsl;
		this.wxje = wxje;
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


	public String getWxdmxid() {
		return this.wxdmxid;
	}

	public void setWxdmxid(String wxdmxid) {
		this.wxdmxid = wxdmxid;
	}

	public String getWxdid() {
		return wxdid;
	}

	public void setWxdid(String wxdid) {
		this.wxdid = wxdid;
	}

	public String getWxdmxname() {
		return this.wxdmxname;
	}

	public void setWxdmxname(String wxdmxname) {
		this.wxdmxname = wxdmxname;
	}

	public String getWxdmxbh() {
		return this.wxdmxbh;
	}

	public void setWxdmxbh(String wxdmxbh) {
		this.wxdmxbh = wxdmxbh;
	}

	public String getWxdmxtype() {
		return this.wxdmxtype;
	}

	public void setWxdmxtype(String wxdmxtype) {
		this.wxdmxtype = wxdmxtype;
	}

	public String getWxfzr() {
		return this.wxfzr;
	}

	public void setWxfzr(String wxfzr) {
		this.wxfzr = wxfzr;
	}

	public String getWxdw() {
		return this.wxdw;
	}

	public void setWxdw(String wxdw) {
		this.wxdw = wxdw;
	}

	public String getLxfs() {
		return this.lxfs;
	}

	public void setLxfs(String lxfs) {
		this.lxfs = lxfs;
	}

	public float getSqwxsl() {
		return this.sqwxsl;
	}

	public void setSqwxsl(float sqwxsl) {
		this.sqwxsl = sqwxsl;
	}

	public float getShwxsl() {
		return this.shwxsl;
	}

	public void setShwxsl(float shwxsl) {
		this.shwxsl = shwxsl;
	}

	public float getSjwxsl() {
		return this.sjwxsl;
	}

	public void setSjwxsl(float sjwxsl) {
		this.sjwxsl = sjwxsl;
	}

	public float getWxje() {
		return this.wxje;
	}

	public void setWxje(float wxje) {
		this.wxje = wxje;
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