package net.chinanets.service;

import java.util.List;

import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Rules;

public interface RuleService extends CommonService{
	
	/**
	 * 查询角色列表
	 * @return
	 */
	public List findAllRules();
	
	/**
	 * 添加角色
	 * @param ruleVo
	 */
	public Long addRules(Rules ruleVo);
	
	/**
	 * 删除角色
	 * @param id
	 */
	public void deleteRules(Long[] id);
	
	/**
	 * 根据用户ID得到所属角色
	 * @param userId
	 * @return
	 */
	public List findRulesByUserId(Long userId);
	
	/**
	 * 添加角色时，查询所有菜单树
	 */
	public String getMenuTree(Long id);
	/**
	 * 编辑用户权限时，查询所有菜单树
	 */
	public String getUserMenuTree(Long userid);
	/**
	 * 获取用户登录成功后的菜单XML
	 * @param userid 用户ID
	 * @return
	 */
	public String getUserLoginTree(Long userid);
	/**
	 * 得到最大的优先级
	 * @return
	 */
	public int getMaxYxj();
	
	/**
	 * 验证角色名称是否存在
	 */
	public boolean validateRuleMc(String mc);
	
	/**
	 * 根据ID查询角色
	 * @param id
	 */
	public Rules findRuleById(Long id);
	
	/**
	 * 修改角色
	 * @param ruleVo
	 */
	public void updateRule(Rules ruleVo);
	
	/**
	 * 根据角色ID查询所对应的菜单
	 * @param id
	 */
	public List getMenuByRuleId(Long id);
	
	/**
	 * 保存角色菜单中间表
	 * @param ruleMenuList
	 * @param ruleId
	 */
	public void saveRuleMenu(List ruleMenuList,Long ruleId,String deptId,String deptName);
	/**
	 * 保存用户菜单中间表
	 */
	public void saveUserMenu(List userMenuList, Long userId,String deptId,String deptName);
	/**
	 * 保存角色权限
	 * 同时更新属于这个角色的所有用户的权限
	 * @param ruleMenuList
	 * @param ruleId
	 */
	public void saveRuleUserMenu(List ruleMenuList, Long ruleId,String deptId,String deptName);
	/**
	 * 获取所处该角色下所有用户
	 * @return
	 */
	public List getUserByRule(Rules ruleVo);
	
	/**
	 * 查询用户的科目权限
	 * @param id
	 * @return
	 */
	public Rules findKmRuleById(Long id);
	
	/**
	 * 根据ruleVo属性查询ruleVo列表
	 * @param ruleVo
	 * @return
	 */
	public List getRuleVoList(Rules ruleVo);
	
	/**
	 * add by songmz
	 * 根据多个rule查询user
	 * @param list
	 * @return
	 */
	public List getUserByRuleList(List<Rules> list);
	/**
	 * 获取所处该角色下所有用户
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getUserByDept(Dept ruleVo);
}
