package net.chinanets.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CnstCodelistData implements Serializable {

	private String codeid;
	private String codetype;
	private String codebs;
	private String codename;
	private String codememo;

	public CnstCodelistData() {
	}

	public CnstCodelistData(String codetype, String codebs, String codename) {
		this.codetype = codetype;
		this.codebs = codebs;
		this.codename = codename;
	}

	public CnstCodelistData(String codetype, String codebs, String codename,
			String codememo) {
		this.codetype = codetype;
		this.codebs = codebs;
		this.codename = codename;
		this.codememo = codememo;
	}

	public String getCodeid() {
		return this.codeid;
	}

	public void setCodeid(String codeid) {
		this.codeid = codeid;
	}

	public String getCodetype() {
		return this.codetype;
	}

	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}

	public String getCodebs() {
		return this.codebs;
	}

	public void setCodebs(String codebs) {
		this.codebs = codebs;
	}

	public String getCodename() {
		return this.codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public String getCodememo() {
		return this.codememo;
	}

	public void setCodememo(String codememo) {
		this.codememo = codememo;
	}
}