package net.chinanets.dao.imp;

import java.util.ArrayList;
import java.util.List;

import net.chinanets.dao.UsersDao;
import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Rules;
import net.chinanets.pojos.Users;
import net.chinanets.utils.helper.StringHelper;
 

public class UserDaoImp extends CommonDaoImp implements UsersDao {

	/**
	 * 获取所有的部门 (不包含部门下用户)
	 * @return
	 */
	public List<Dept> getAllDepts(){
		List<Dept> list = (List<Dept>) super.RunSelectClassBySql("select * from dept", null, Dept.class);
		return list;
	}
	
	/**
	 * 获取所有的角色(不包含角色下用户)
	 * @return
	 */
	public List getAllRules(){
		List<Rules> list = (List<Rules>) super.RunSelectClassBySql("select * from rules", null, Rules.class);
			
			
		return list;
	}
	
	
	/**
	 * 根据id获取Users对象
	 * 
	 * @param id
	 * @return
	 */
	public Users findUserById(Long id) {

		return (Users) super.getObjectById(Users.class, id);
	}


	/***
	 * 根据User的ID删除User对象
	 * 
	 * @param id
	 */
	public void deleteUser(Users user) {
		Object []objs = {};
	//	String strSql=StringHelper.Format("delete from UsersMenus  where USER_ID=%1$s",user.getId());
		try{
		super.RunUpdateBySQL("delete from Users_Menus  where USER_ID="  +user.getId(),objs);
		super.RunUpdateBySQL("delete from USERS_RULES  where USER_ID=" + user.getId(),objs);
		super.RunUpdateBySQL("delete from USERS_DEPTS  where USER_ID=" + user.getId(),objs);
		super.deleteByHql("delete from Users us where us.id=" + user.getId());
	//	super.RunUpdateBySQL("delete from USERS  where ID=" + user.getId(),objs);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 根据user对象属性值查询对应的用户
	 * 
	 * @param user
	 * @return
	 */
	public List getUserByExample(Users user) {
		return super.getObjectByExample(user);
	}

	
}
