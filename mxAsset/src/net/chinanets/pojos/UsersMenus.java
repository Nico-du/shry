package net.chinanets.pojos;

/**
 * UsersMenus entity. @author dzj
 */

public class UsersMenus implements java.io.Serializable {

	// Fields

	private Long id;
	private Users users;
	private Menus menus;
	private String sfAdd;
	private String sfModify;
	private String sfDelete;
	private String sfSearch;
	private String sfImport;
	private String sfExport;
	private String searchCondition;
	private String sfOther;

	// Constructors

	/** default constructor */
	public UsersMenus() {
	}

	/** minimal constructor */
	public UsersMenus(Users users, Menus menus) {
		this.users = users;
		this.menus = menus;
	}

	/** full constructor */
	public UsersMenus(Users users, Menus menus, String sfAdd, String sfModify,
			String sfDelete, String sfSearch, String sfImport, String sfExport,
			String searchCondition, String sfOther) {
		this.users = users;
		this.menus = menus;
		this.sfAdd = sfAdd;
		this.sfModify = sfModify;
		this.sfDelete = sfDelete;
		this.sfSearch = sfSearch;
		this.sfImport = sfImport;
		this.sfExport = sfExport;
		this.searchCondition = searchCondition;
		this.sfOther = sfOther;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Menus getMenus() {
		return this.menus;
	}

	public void setMenus(Menus menus) {
		this.menus = menus;
	}

	public String getSfAdd() {
		return this.sfAdd;
	}

	public void setSfAdd(String sfAdd) {
		this.sfAdd = sfAdd;
	}

	public String getSfModify() {
		return this.sfModify;
	}

	public void setSfModify(String sfModify) {
		this.sfModify = sfModify;
	}

	public String getSfDelete() {
		return this.sfDelete;
	}

	public void setSfDelete(String sfDelete) {
		this.sfDelete = sfDelete;
	}

	public String getSfSearch() {
		return this.sfSearch;
	}

	public void setSfSearch(String sfSearch) {
		this.sfSearch = sfSearch;
	}

	public String getSfImport() {
		return this.sfImport;
	}

	public void setSfImport(String sfImport) {
		this.sfImport = sfImport;
	}

	public String getSfExport() {
		return this.sfExport;
	}

	public void setSfExport(String sfExport) {
		this.sfExport = sfExport;
	}

	public String getSearchCondition() {
		return this.searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getSfOther() {
		return this.sfOther;
	}

	public void setSfOther(String sfOther) {
		this.sfOther = sfOther;
	}

}