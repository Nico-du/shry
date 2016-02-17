package net.chinanets.dao;

import java.util.List;

import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Users;


public interface UsersDao extends CommonDao{

	 /**
	  * 根据ID得到User对象
	  * @param id
	  * @return
	  */
	 public Users findUserById(Long id);
	 
	 /***
	  * 删除User对象
	  * @param id
	  */
	 public void deleteUser(Users user);
	 
	 /**
	  * 根据user对象属性值查询对应的用户
	  * @param user
	  * @return
	  */
	 public List getUserByExample(Users user);
	/**
	 * 获取所有的部门 (不包含部门下用户)
	 * @return
	 */
	public List<Dept> getAllDepts();
	/**
	 * 获取所有的角色(不包含角色下用户)
	 * @return
	 */
	public List getAllRules();
}
