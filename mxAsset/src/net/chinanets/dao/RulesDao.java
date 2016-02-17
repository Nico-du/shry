package net.chinanets.dao;

import java.util.List;

import net.chinanets.pojos.Rules;
import net.chinanets.pojos.RulesMenus;
import net.chinanets.pojos.Users;

public interface RulesDao extends CommonDao{
	
	/**
	 * 查询所有角色
	 * @return
	 */
	public List findAllRules();
	/**
	 * 查询角色
	 * 
	 * @return
	 */
	public List findRulesByObj(Rules rules);
	/**
	 * 得到最大的优先级
	 * 
	 * @return
	 */
	public int getMaxYxj();
	
	/**
	 * 修改删除2个角色的优先级中间的角色的优先级，使得角色的优先级始终是不间断的顺序
	 * @param firstYxj
	 * @param maxYxj
	 * @param yxj
	 */
	public void updateRules(int firstYxj,int maxYxj,int yxj);
	
	/**
	 * 根据角色ID删除中间表
	 * @param ruleId
	 * @return
	 */
	public void deleteRuleMenuByruleId(Long ruleId);
	
	/**
	 * 查询中间表记录
	 * @param ruleId
	 * @param menuId
	 * @return
	 */
	public RulesMenus getRuleMenuByRMid(Long ruleId, Long menuId);
	/**
	 * 根据用户ID删除中间表
	 * @param ruleId
	 * @return
	 */
	public void deleteUserMenuByruleId(Long userId);
	/**
	 * 更新中间表
	 * @param ruleId
	 * @return
	 */
	public void updteUserMenus(Users user,List userMenuList);
}
