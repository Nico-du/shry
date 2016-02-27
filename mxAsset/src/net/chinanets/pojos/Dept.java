package net.chinanets.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Dept entity. @author dzj
 */

public class Dept implements java.io.Serializable {

	// Fields

	private String id;
	private String mc;
	private Dept parentid;
	private Long showOrder;
	private String bz;
	private List userses = new ArrayList(0);

	// Constructors

	/** default constructor */
	public Dept() {
	}

	/** full constructor */
	public Dept(String mc, Dept parentid, Long showOrder,String bz,
			List userses) {
		this.mc = mc;
		this.parentid = parentid;
		this.showOrder = showOrder;
		this.bz = bz;
		this.userses = userses;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMc() {
		return this.mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public Dept getParentid() {
		return this.parentid;
	}

	public void setParentid(Dept parentid) {
		this.parentid = parentid;
	}

	public Long getShowOrder() {
		return this.showOrder;
	}

	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	public List getUserses() {
		return userses;
	}

	public void setUserses(List userses) {
		this.userses = userses;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

}