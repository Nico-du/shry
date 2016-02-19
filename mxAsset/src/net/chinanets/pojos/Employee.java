package net.chinanets.pojos;

import java.util.HashSet;
import java.util.Set;

/**
 * Employee generated by MyEclipse Persistence Tools
 */

public class Employee implements java.io.Serializable {

	// Fields

	private Long id;
	private Dept dept;
	private String xm;
	private String loginName;


	// Constructors

	/** default constructor */
	public Employee() {
	}

	/** minimal constructor */
	public Employee(Dept dept) {
		this.dept = dept;
	}

	/** full constructor */
	public Employee(Dept dept, String xm) {
		this.dept = dept;
		this.xm = xm;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Dept getDept() {
		return this.dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public String getXm() {
		return this.xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}
}