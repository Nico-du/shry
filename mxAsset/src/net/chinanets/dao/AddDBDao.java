package net.chinanets.dao;

public interface AddDBDao extends CommonDao{
	/**
	 * 更新人员信息
	 */
	public String addOrUpdateUser();
	/**
	 * 更新部门信息
	 */
    public String addOrUpdateDept();
}
