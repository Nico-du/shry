package net.chinanets.service.imp;

import net.chinanets.service.AddDBService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.chinanets.dao.AddDBDao;
import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Employee;
import net.chinanets.service.imp.CommonServiceImp;
import net.chinanets.utils.UserNameNotFoundException;

import java.util.Iterator;

import org.springframework.aop.ThrowsAdvice;

import com.other.MyConnection;
@SuppressWarnings("unchecked")
public class AddDBServiceImp extends CommonServiceImp implements AddDBService {
	
	private AddDBDao addDBDao;
	
	public String addOrUpdateData() {
		//if("success".equals(addOrUpdateDept())){
		if(true){
			return addOrUpdateUser();
		}
		return "failed";
	}
	public String addOrUpdateUser() {
		return addDBDao.addOrUpdateUser();
	}
	public String addOrUpdateDept() {
		return addDBDao.addOrUpdateDept();
	}
	
	// 更新员工
		public void addOrUpdateEmployee() {
			/*List lt = getAllEmployee();
			if (lt.size() > 0) {
				for (Iterator iterator = lt.iterator(); iterator.hasNext();) {
					Employee emp = (Employee) iterator.next();
					Dept dept = (Dept) super.getObjectById(new Dept(), emp
							.getDept().getId());
					if (dept == null) {
	                    continue;
					} else {
						Employee count = (Employee) super.getObjectById(new Employee(),
								emp.getId());
						if (count == null) {
							super.saveObject(emp);
						} else {
							super.mergeObject(emp);
						}
					}
				}
			}*/
		}

		// 更新部门
		public void addOrUpdateDeptOld() {
			/*
			 * List juList = getAllDept(Long.parseLong("402")); List guanList =
			 * getAllDept(Long.parseLong("403")); juList.addAll(guanList);
			 */
			/*List juList = getAllDept();
			if (juList.size() > 0) {
				for (Iterator iterator = juList.iterator(); iterator.hasNext();) {
					Dept dept = (Dept) iterator.next();
					Dept count = (Dept) super.getObjectById(new Dept(), dept.getId());
					System.out.println(dept.getMc()+"mc");
					if (count == null) {
						super.saveObject(dept);
					} else {
//						super.saveOrUpdateObject(dept);
//						super.updateObject(dept);
						super.mergeObject(dept);
					}
				}
//				super.deleteByHql("delete from Dept");
//				super.saveObjectItem(juList);
			}*/
		}
	
	
	
	// 获得远程数据库的所有部门
	public List getAllDept() {
		Connection con = null;

		List lt = new ArrayList();
		try {
			con = MyConnection.getDAConnection();
			if (con == null) {
				return null;
			}
			Statement st = con.createStatement();
			// String sql =
			// "select organize_id,organize_name from v_organize where organize_id in (select r.child_id from v_organize_relation r where r.organize_id="
			// + id + ")";
			String sql = "select o.organize_id,o.organize_name,r.organize_id,o.show_order  from  v_organize o,v_organize_relation r where r.child_id=o.organize_id and r.organize_id in (403,402,703)";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Dept dp = new Dept();
				dp.setId(rs.getString(1));
				dp.setMc(rs.getString(2));
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

	// 获取远程员工对象
	public List getAllEmployee() {
		Connection con = null;
		List lt = new ArrayList();
		try {
			con = MyConnection.getDAConnection();
			if (con == null) {
				return null;
			}
			Statement st = con.createStatement();
			String sql = "select * from zc_user";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Employee emp = new Employee();
				emp.setId(rs.getLong("USER_ID"));
				emp.setXm(rs.getString("USER_NAME"));
				emp.setLoginName(rs.getString("LOGIN_NAME"));
				Dept dept = new Dept();
				dept.setId(rs.getString("ORGANIZE_ID"));
				emp.setDept(dept);
				lt.add(emp);
			}
			System.out.println("查询用户数据成功！！");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyConnection.closeConn(con);
		}
		return lt;
	}


	public AddDBDao getAddDBDao() {
		return addDBDao;
	}

	public void setAddDBDao(AddDBDao addDBDao) {
		this.addDBDao = addDBDao;
	}
}
