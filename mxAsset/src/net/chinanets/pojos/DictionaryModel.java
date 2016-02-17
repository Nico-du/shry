package net.chinanets.pojos;

import java.util.List;

public class DictionaryModel {
	private Long id;
	private String name;
	private Long pId;
	private String pName;
	private String remarks;
	private Long status;
	private List<DictionaryModel> children;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getpId() {
		return pId;
	}
	public void setpId(Long pId) {
		this.pId = pId;
	}
	
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public List<DictionaryModel> getChildren() {
		return children;
	}
	public void setChildren(List<DictionaryModel> children) {
		this.children = children;
	}
	
}
