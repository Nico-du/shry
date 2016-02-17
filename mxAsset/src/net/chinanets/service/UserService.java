package net.chinanets.service;

import java.util.List;

import net.chinanets.pojos.Menus;
import net.chinanets.pojos.Users;
import net.chinanets.utils.PassWordErrorException;
import net.chinanets.utils.UserNameNotFoundException;
import net.chinanets.vo.MenuVo;
import net.chinanets.vo.RuleVo;
import net.chinanets.vo.UserVo;

public interface UserService extends CommonService {
	/**
	 * 查询用户
	 * @return
	 */
	public List findUsers(int pageSize, int pageNumber,String condition);

	/**
	 * 查询用户总数
	 * @param condition
	 * @return
	 */
	public int getCountByUsers(String condition);

	/**
	 * 添加帐号
	 * @param userVo
	 * @return
	 */
	public Long saveUser(UserVo userVo);
	
	/**
	 * 修改帐号
	 * @param userVo
	 */
	public void updateUser(UserVo userVo);
	
	/**
	 * 登录
	 * 
	 * @param mc
	 * @param mm
	 * @return
	 */
	public UserVo validateUser(String mc, String mm) throws PassWordErrorException,UserNameNotFoundException;
	/**
	 * 获取用户的权限信息
	 * @param userid
	 * @return
	 */
	public UserVo getUserVoById(Long userid);
	/**
	 * 验证用户名是否存在
	 * 
	 * @param mc
	 * @return
	 */
	public boolean validateUserByMc(String mc);
	/**
	 * 单点登录验证
	 * @param userId
	 * @return
	 */
	public UserVo validateSingleLogin(String userId);
	/**
	 * CA登录验证
	 * @param userId
	 * @return
	 */
	public UserVo validateCALogin(String zybm);
	/**
	 * 用户登录后 根据用户ID得到用户所有菜单权限
	 * 
	 * @param userId
	 * @return
	 */
	public String getUserMenu(Long userId);
	/**
	 * 获取用户菜单 不包含菜单结构
	 */
	public String getUserAuthority(Long userId);
	/**
	 * 获取用户菜单 不包含无权限菜单 ,整理结构
	 * @param wdType 菜单类型(menu 字段bz)
	 */
	public String getUserStructureMenu(Long userId,String wdType);
	/**
	 * 根据ID 删除用户
	 * 
	 * @param id
	 */
	public void deleteUser(Long[] id);
	
	/**
	 * 根据ID查询用户
	 * @param id
	 */
	public UserVo findUserById(Long id);
	
	/**
	 * 修改密码
	 * @param newPw
	 * @param userId
	 */
	public void modifyPw(String newPw,Long userId);
	
	/**
	 * 查询用户的权限
	 * @return
	 */
	public RuleVo getRule(Long ruleId);
	
	public int backDB();
	
	public List selectDBback();
	
	/**
	 * 根据菜单得到用户，也就是得到拥有此菜单的所有用户
	 * @return
	 */
	public List getUserByMenu(String url);
	/**
	 * 获取所有部门(不包含用户)
	 * @return
	 */
	public List getAllDepts();
	/**
	 * 获取所有角色(不包含用户)
	 * @return
	 */
	public List getAllRules();
	
}
