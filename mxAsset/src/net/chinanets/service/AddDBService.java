package net.chinanets.service;

import java.util.List;

public interface AddDBService {
	
	/**
	 * 更新部门数据
	 * @return
	 */
	public String addOrUpdateDept();
	
	/**
	 * 更新用户数据
	 * @return
	 */
	public String addOrUpdateUser();
	
	/**
	 * 更新部门和用户数据
	 * @return
	 */
	public String addOrUpdateData();
}
