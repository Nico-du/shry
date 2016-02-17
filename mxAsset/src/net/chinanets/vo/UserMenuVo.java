package net.chinanets.vo;

public class UserMenuVo {
	private Long id;
	private String sfAdd;
	private String sfModify;
	private String sfDelete;
	private String sfSearch;
	private String sfImport;
	private String sfExport;
	private String searchCondition;
	private String sfOther;
	private Long userID;
	private Long menuID;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSfAdd() {
		return sfAdd;
	}

	public void setSfAdd(String sfAdd) {
		this.sfAdd = sfAdd;
	}

	public String getSfModify() {
		return sfModify;
	}

	public void setSfModify(String sfModify) {
		this.sfModify = sfModify;
	}

	public String getSfDelete() {
		return sfDelete;
	}

	public void setSfDelete(String sfDelete) {
		this.sfDelete = sfDelete;
	}

	public String getSfSearch() {
		return sfSearch;
	}

	public void setSfSearch(String sfSearch) {
		this.sfSearch = sfSearch;
	}

	public String getSfImport() {
		return sfImport;
	}

	public void setSfImport(String sfImport) {
		this.sfImport = sfImport;
	}

	public String getSfExport() {
		return sfExport;
	}

	public void setSfExport(String sfExport) {
		this.sfExport = sfExport;
	}

	 
	public Long getMenuID() {
		return menuID;
	}

	public void setMenuID(Long menuID) {
		this.menuID = menuID;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getSfOther() {
		return sfOther;
	}

	public void setSfOther(String sfOther) {
		this.sfOther = sfOther;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

}
