package net.chinanets.entity;

/**
 * CnstDictionaryData entity. @author MyEclipse Persistence Tools
 */

public class CnstDictionaryData implements java.io.Serializable {

	// Fields

	private String dicid;
	private String dicname;
	private String parentid;
	private String parentname;
	private String rootid;
	private String rootname;

	// Constructors

	/** default constructor */
	public CnstDictionaryData() {
	}

	/** full constructor */
	public CnstDictionaryData(String dicid,String dicname, String parentid,
			String parentname, String rootid, String rootname) {
		this.dicid = dicid;
		this.dicname = dicname;
		this.parentid = parentid;
		this.parentname = parentname;
		this.rootid = rootid;
		this.rootname = rootname;
	}

	// Property accessors

	public String getDicid() {
		return this.dicid;
	}

	public void setDicid(String dicid) {
		this.dicid = dicid;
	}

	public String getDicname() {
		return this.dicname;
	}

	public void setDicname(String dicname) {
		this.dicname = dicname;
	}

	public String getParentid() {
		return this.parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getParentname() {
		return this.parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname;
	}

	public String getRootid() {
		return this.rootid;
	}

	public void setRootid(String rootid) {
		this.rootid = rootid;
	}

	public String getRootname() {
		return this.rootname;
	}

	public void setRootname(String rootname) {
		this.rootname = rootname;
	}

}