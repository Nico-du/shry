package net.chinanets.dao.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import net.chinanets.dao.RulesDao;
import net.chinanets.pojos.Rules;
import net.chinanets.pojos.RulesMenus;
import net.chinanets.pojos.Users;

public class RulesDaoImp extends CommonDaoImp implements RulesDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 查询所有角色
	 * 
	 * @return
	 */
	public List findAllRules() {
		return super.getObjectByExample(new Rules(), " 1=1 order by yxj asc");
	}
	/**
	 * 查询角色
	 * 
	 * @return
	 */
	public List findRulesByObj(Rules rules) {
		return super.getObjectByExample(rules, " 1=1 order by yxj asc");
	}

	/**
	 * 得到最大的优先级
	 * 
	 * @return
	 */
	public int getMaxYxj() {
		String dbSql = "select max(yxj) from rules";
		return jdbcTemplate.queryForInt(dbSql);
	}

	/**
	 * 修改删除2个角色的优先级中间的角色的优先级，使得角色的优先级始终是不间断的顺序
	 * 
	 * @param firstYxj
	 * @param maxYxj
	 * @param yxj
	 */
	public void updateRules(int firstYxj, int maxYxj, int yxj) {
		StringBuffer sb = new StringBuffer();
		if (maxYxj == 0) {
			sb.append("update rules set yxj = yxj - " + yxj + " where yxj > "
					+ firstYxj);
		} else {
			if (firstYxj == 0) {
				sb.append("update rules set yxj = yxj - " + yxj
						+ " where yxj > " + maxYxj);
			} else {
				sb.append("update rules set yxj = yxj - " + yxj
						+ " where yxj > " + firstYxj + " and yxj < " + maxYxj);
			}
		}
		System.out.println(sb.toString());
		jdbcTemplate.execute(sb.toString());
	}

	/**
	 * 查询中间表记录
	 * @param ruleId
	 * @param menuId
	 * @return
	 */
	public RulesMenus getRuleMenuByRMid(Long ruleId, Long menuId) {
		String dbSql = "select * from rules_menus where rule_Id=" + ruleId
				+ " and menu_Id=" + menuId;
		return (RulesMenus) jdbcTemplate.queryForObject(dbSql, new RuleMenuRowMapper());
	}
	
	class RuleMenuRowMapper implements RowMapper{

		public Object mapRow(ResultSet arg0, int arg1) throws SQLException {
			RulesMenus ruleMenu = new RulesMenus();
			ruleMenu.setId(arg0.getLong("ID"));
			ruleMenu.setMenusID(arg0.getLong("MENU_ID"));
			ruleMenu.setRulesID(arg0.getLong("RULE_ID"));
			ruleMenu.setSfAdd(arg0.getString("sf_Add"));
			ruleMenu.setSfDelete(arg0.getString("sf_Delete"));
			ruleMenu.setSfExport(arg0.getString("sf_Export"));
			ruleMenu.setSfImport(arg0.getString("sf_Import"));
			ruleMenu.setSfModify(arg0.getString("sf_Modify"));
			ruleMenu.setSfSearch(arg0.getString("sf_Search"));
			return ruleMenu;
		}
		
	}

	/**
	 * 根据角色ID删除中间表
	 * @param ruleId
	 * @return
	 */
	public void deleteRuleMenuByruleId(Long ruleId) {
		String dbSql = "delete from rules_menus where rule_id = " + ruleId;
		jdbcTemplate.execute(dbSql);
	}
	/**
	 * 根据用户ID删除中间表
	 * @param ruleId
	 * @return
	 */
	public void deleteUserMenuByruleId(Long userId) {
		String dbSql = "delete from users_menus where user_id = " + userId;
		jdbcTemplate.execute(dbSql);
	}
	/**
	 * 更新中间表
	 * @param ruleId
	 * @return
	 */
	public void updteUserMenus(Users user,List userMenuList){
		user.setUsersMenuses(userMenuList);
		super.getSession().refresh(user);
		super.update(user);
	}

}
