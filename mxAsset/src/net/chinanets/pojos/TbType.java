package net.chinanets.pojos;



/**
 * TbType entity. @author MyEclipse Persistence Tools
 */

public class TbType implements java.io.Serializable {

	// Fields

	private Long typeId;
	private String typeName;
	private String typeMark;
	private String typeBack;
	private long  usable;
	private String gzNumber;
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeMark() {
		return typeMark;
	}
	public void setTypeMark(String typeMark) {
		this.typeMark = typeMark;
	}
	public String getTypeBack() {
		return typeBack;
	}
	public void setTypeBack(String typeBack) {
		this.typeBack = typeBack;
	}
	public long getUsable() {
		return usable;
	}
	public void setUsable(long usable) {
		this.usable = usable;
	}
	public String getGzNumber() {
		return gzNumber;
	}
	public void setGzNumber(String gzNumber) {
		this.gzNumber = gzNumber;
	}
	

	

}