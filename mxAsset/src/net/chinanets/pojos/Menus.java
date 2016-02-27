package net.chinanets.pojos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Menus entity. @author dzj
 */

public class Menus implements java.io.Serializable {

	// Fields

	private Long id;
	private Menus menus;
	private String mc;
	private String url;
	private String bz;
	private String pageUrl;
	private String menuImg;
	private List rulesMenuses = new ArrayList(0);
	private List menuses = new ArrayList(0);
	private List usersMenuses = new ArrayList(0);

	// Constructors

	/** default constructor */
	public Menus() {
	}

	/** full constructor */
	public Menus(Menus menus, String mc, String url, String bz,String pageUrl, String menuImg,
			List rulesMenuses, List menuses, List usersMenuses) {
		this.menus = menus;
		this.mc = mc;
		this.url = url;
		this.bz = bz;
		this.pageUrl = pageUrl;
		this.menuImg = menuImg;
		this.rulesMenuses = rulesMenuses;
		this.menuses = menuses;
		this.usersMenuses = usersMenuses;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Menus getMenus() {
		return this.menus;
	}

	public void setMenus(Menus menus) {
		this.menus = menus;
	}

	public String getMc() {
		return this.mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getMenuImg() {
		return this.menuImg;
	}

	public void setMenuImg(String menuImg) {
		this.menuImg = menuImg;
	}

	public List getRulesMenuses() {
		return this.rulesMenuses;
	}

	public void setRulesMenuses(List rulesMenuses) {
		this.rulesMenuses = rulesMenuses;
	}

	public List getMenuses() {
		return this.menuses;
	}

	public void setMenuses(List menuses) {
		this.menuses = menuses;
	}

	public List getUsersMenuses() {
		return usersMenuses;
	}

	public void setUsersMenuses(List usersMenuses) {
		this.usersMenuses = usersMenuses;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

}