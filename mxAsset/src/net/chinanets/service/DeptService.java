package net.chinanets.service;

import java.util.Map;

import net.chinanets.pojos.Dept;

public interface DeptService extends CommonService {
	
	
	/**
	 * 添加设备清单 部门
	 */
	public String getSyBm();
	
	/**
	 * 返回部门组织结构树
	 * @return
	 */
	public String getBmzz();
	/**
	 * 返回部门组织结构树 和 部门组织结构List
	 * @return
	 */
	public Map getBmzzMap();
	
	public String getBmUser();
	/**
	 * 修改部门
	 *  如果修改后的优先级与已有部门优先级相同则交换两个部门的优先级
	 * @param deptVo
	 */
	public void updateDept(Dept deptVo) ;
	/**
	 * 添加部门
	 *  如果添加部门的优先级与已有部门优先级相同则交换两个部门的优先级
	 * @param deptVo
	 */
	public void addDept(Dept deptVo);
}
