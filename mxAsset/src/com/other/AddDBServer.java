package com.other;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Users;
import net.chinanets.pojos.Users;
import net.chinanets.service.imp.CommonServiceImp;
import java.util.Iterator;

import com.other.MyConnection;
@SuppressWarnings("unchecked")
public class AddDBServer extends CommonServiceImp {
	// 获得远程数据库的所有部门
	private List getAllDept(Long id) {
		Connection con = null;
		List lt = new ArrayList();
		try {
			con = MyConnection.getDAConnection();
			Statement st = con.createStatement();
			String sql = "select organize_id,organize_name from v_organize where organize_id in (select r.child_id from v_organize_relation r where r.organize_id="
					+ id + ")";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Dept dp = new Dept();
				dp.setId(rs.getString("ORGANIZE_ID"));
				dp.setMc(rs.getString("ORGANIZE_NAME"));
			//	dp.setParentid(id);
				lt.add(dp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyConnection.closeConn(con);
		}
		return lt;
	}

	// 获取远程员工对象
	private List getAllUsers() {
		Connection con = null;
		List lt = new ArrayList();
		try {
			con = MyConnection.getDAConnection();
			Statement st = con.createStatement();
			String sql = "select user_id,user_name,login_name from v_user where user_id in (select r.child_id from v_organize_relation r where r.organize_id=403 or r.organize_id=402)";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Users emp = new Users();
				emp.setId(rs.getLong("USER_ID"));
				emp.setZsxm(rs.getString("USER_NAME"));
				emp.setMc(rs.getString("LOGIN_NAME"));
				Dept dept = new Dept();
				dept.setId(rs.getString("USER_ID"));
				List deptSet = new ArrayList();
				deptSet.add(dept);
				emp.setDepts(deptSet);
				lt.add(emp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyConnection.closeConn(con);
		}
		return lt;
	}

	// 更新员工
	public void addOrUpdateUsers() {
		List lt = getAllUsers();
		if (lt.size() > 0) {
			for (Iterator iterator = lt.iterator(); iterator.hasNext();) {
				Users emp = (Users) iterator.next();
				Users count = (Users)super.getObjectById(new Users(),emp.getId());
				if (count != null) {
					super.saveObject(emp);
				} else {
					super.mergeObject(emp);
				}
			}
		}
	}

	// 更新部门
	public void addOrUpdateDept() {
		/*
		List juList = getAllDept(Long.parseLong("402"));
		List guanList = getAllDept(Long.parseLong("403"));
		juList.addAll(guanList);
		*/
		
		List juList = new ArrayList();
		Dept p1 = new Dept();p1.setId("408");p1.setMc("机关党委");
		Dept p2 = new Dept();p2.setId("409");p2.setMc("综合规划处");
		Dept p3 = new Dept();p3.setId("407");p3.setMc("组织人事处");
		juList.add(p1);juList.add(p2);juList.add(p3);

		if(juList.size()>0){
		for (Iterator iterator = juList.iterator(); iterator.hasNext();) {
			Dept dept = (Dept) iterator.next();
			Dept count = (Dept)super.getObjectBySId(new Dept(),dept.getId());
			if (count != null) {
				super.saveObject(dept);
			} else {
				super.mergeObject(dept);
			}
		}
		}
	}
	

		private class myUser{
			public String kk(){
				return "";
			}
		}
	
}
