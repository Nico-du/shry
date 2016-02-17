package net.chinanets.dao.imp;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.other.MyConnection;

import net.chinanets.dao.AddDBDao;
import net.chinanets.data.DataEntity;
import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Employee;
import net.chinanets.pojos.Rules;
import net.chinanets.pojos.Users;
import net.chinanets.utils.CommonMethods;
import net.chinanets.vo.UserVo;
import net.sf.json.JSONArray;

public class AddDBDaoImp extends CommonDaoImp implements AddDBDao{
	//数据同步
	// 获取远程员工对象
		public List getAllUser() {
			Connection con = null;
			List lt = new ArrayList();
			try {
				con = MyConnection.getDAConnection();
				if (con == null) {
					return null;
				}
				Statement st = con.createStatement();
				String sql = "select * from V_TOCZ_RYXX";
				ResultSet rs = st.executeQuery(sql);
				
				while (rs.next()) {
					UserVo user = new UserVo();
					user.setId(rs.getLong("ZYBM"));
					user.setUserName(rs.getString("XM"));
					user.setDeptId(rs.getString("BM"));
					user.setDeptName(rs.getString("BM_MC"));
					user.setZwid(rs.getString("XZZW"));
					user.setZwmc(rs.getString("XZZW_MC"));
					user.setZwlbmc(rs.getString("ZWLB_MC"));
					
					/*Dept dept = this.getHibernateTemplate().load(Dept.class, rs.getString("BM"));
					if(dept == null){
					dept = new Dept();
					dept.setId(rs.getString("BM"));
					}
					Rules rule = this.getHibernateTemplate().load(Rules.class, 45L);;
				//	rule.setId(45L);//普通用户
					user.getDepts().add(dept);
					user.getRuleses().add(rule);*/
					lt.add(user);
				}
				System.out.println("查询用户数据成功！！");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				MyConnection.closeConn(con);
			}
			return lt;
		}
		
		public List getAllDept(){
			Connection con = null;
			List lt = new ArrayList();
			try {
				con = MyConnection.getDAConnection();
				if (con == null) {
					return null;
				}
				Statement st = con.createStatement();
				String sql = "select bm.FY,bm.BM,bm.MC,bm.PX from v_tocz_bmxx bm";
				ResultSet rs = st.executeQuery(sql);
				while (rs.next()) {
					Dept dp = new Dept();
					dp.setId(rs.getString(2));
					dp.setMc(rs.getString(3));
			//		dp.setParentid(rs.getLong(3));
					dp.setShowOrder(rs.getLong(4));
					lt.add(dp);
				}
				System.out.println("查询部门成功！！");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				MyConnection.closeConn(con);
			}
			return lt;
		}
		
	/**
	 * 更新用户信息
	 */
	public String addOrUpdateUser() {
		try{
		List lt = getAllUser();
		if(lt == null || lt.isEmpty()){return "failed";}
		SimpleDateFormat myDateFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (lt.size() > 0) {
			for (Iterator iterator = lt.iterator(); iterator.hasNext();) {
				this.getSession().clear();
				UserVo user = (UserVo) iterator.next();
				/*Dept dept = (Dept) super.getObjectById(Dept.class, ((Dept) user.getDepts().get(0)).getId());
				if (dept == null) {
                    continue;
				} else*/
				{
					Users count = (Users) super.getObjectById(Users.class,user.getId());
					String userSql,userRuleSql,userDeptSql;
					userSql = userRuleSql = userDeptSql = "";
					if (count == null) {
						userSql = "insert into users (id,zsxm,bmmc,cjsj,zwid,zwmc,zwlbmc) values ("+user.getId()+",'"+user.getUserName()+"','"+user.getDeptName()+"','"+myDateFormate.format(new Date())+"','"+user.getZwid()+"','"+user.getZwmc()+"','"+user.getZwlbmc()+"')";
					} else {
						userSql = "update users set zsxm='"+user.getUserName()+"',bmmc='"+user.getDeptName()+"',zwid='"+user.getZwid()+"',zwmc='"+user.getZwmc()+"',zwlbmc='"+user.getZwlbmc()+"',cjsj='"+myDateFormate.format(new Date())+"' where id="+user.getId();
					}
					//设定默认角色 计算方式需要对需求
					List rstList  = super.RunSelectDataEntityBySql("select id from rules where yxj in (select max(yxj) from rules)", null);
					String ruleId = ((DataEntity)rstList.get(0)).GetStringValue("ID","45");
					
					userRuleSql = "insert into users_rules(user_id,rule_id) values("+user.getId()+","+ruleId+")";
					userDeptSql = "insert into users_depts(user_id,dept_id) values("+user.getId()+",'"+user.getDeptId()+"')";
					
					super.RunUpdateBySQL(userSql,null);
					super.RunUpdateBySQL("delete from users_rules where user_id="+user.getId(),null);
					super.RunUpdateBySQL(userRuleSql,null);
					super.RunUpdateBySQL("delete from users_depts where user_id="+user.getId(),null);
					super.RunUpdateBySQL(userDeptSql,null);
				}
			}
		}
	}catch(Exception e){
		return "failed";
	} 
		return "success";
	}
	
	/**
	 * 更新部门信息
	 */
	public String addOrUpdateDept() {
		try{
		List juList = getAllDept();
		if (juList.size() > 0) {
			for (Iterator iterator = juList.iterator(); iterator.hasNext();) {
				Dept dept = (Dept) iterator.next();
				Dept count = (Dept) super.getObjectById(Dept.class, dept.getId());
				System.out.println(dept.getMc()+"mc");
				if (count == null) {
					super.save(dept);
				} else {
//					super.saveOrUpdateObject(dept);
//					super.updateObject(dept);
					super.merge(dept);
				}
			}
//			super.deleteByHql("delete from Dept");
//			super.saveObjectItem(juList);
		}
		}catch(Exception e){
			return "failed";
		}
		return "success";
	}

	 


}
