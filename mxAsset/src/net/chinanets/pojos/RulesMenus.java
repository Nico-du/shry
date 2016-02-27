package net.chinanets.pojos;

/**
 * RulesMenus entity. @author dzj
 */

public class RulesMenus implements java.io.Serializable {

	// Fields

	private Long id;
	private Rules rules;
	private Menus menus;
	private String sfAdd;
	private String sfModify;
	private String sfDelete;
	private String sfSearch;
	private String sfImport;
	private String sfExport;
	
	private Long rulesID;
	private Long menusID;


	// Constructors

	

	/** default constructor */
	public RulesMenus() {
	}

	/** minimal constructor */
	public RulesMenus(Rules rules, Menus menus) {
		this.rules = rules;
		this.menus = menus;
	}

	/** full constructor */
	public RulesMenus(Rules rules, Menus menus, String sfAdd, String sfModify,
			String sfDelete, String sfSearch, String sfImport, String sfExport) {
		this.rules = rules;
		this.menus = menus;
		this.sfAdd = sfAdd;
		this.sfModify = sfModify;
		this.sfDelete = sfDelete;
		this.sfSearch = sfSearch;
		this.sfImport = sfImport;
		this.sfExport = sfExport;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Rules getRules() {
		return this.rules;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
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

	public void setRulesID(Long rulesID) {
		this.rulesID = rulesID;
	}

	public Long getRulesID() {
		return rulesID;
	}

	public void setMenusID(Long menusID) {
		this.menusID = menusID;
	}

	public Long getMenusID() {
		return menusID;
	}

}