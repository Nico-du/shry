package net.chinanets.dao.imp;

import java.sql.Connection;
import java.sql.ResultSet;import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.chinanets.dao.IQueryDAO;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class QueryDAOImpl extends HibernateDaoSupport implements IQueryDAO {

	public List getObjectByExample(final Object obj) {

		return (List) this.getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria executableCriteria = session
								.createCriteria(obj.getClass());
						executableCriteria.add(Example.create(obj).enableLike()
								.excludeZeroes());
						return executableCriteria.list();
					}
				});
	}

	public List getObjectByExample(final Object obj, final int maxResults,
			final int firstResult) {

		return (List) this.getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria executableCriteria = session
								.createCriteria(obj.getClass());
						executableCriteria.add(Example.create(obj).enableLike()
								.excludeZeroes());
						if (firstResult >= 0) {
							executableCriteria.setFirstResult(firstResult);
						}
						if (maxResults > 0) {
							executableCriteria.setMaxResults(maxResults);
						}
						return executableCriteria.list();
					}
				});
	}

	public int getCountByExample(final Object obj) {

		List list = (List) this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria executableCriteria = session
								.createCriteria(obj.getClass());
						executableCriteria.add(Example.create(obj).enableLike()
								.excludeZeroes());
						return executableCriteria.list();
					}
				});
		
		return list.size();
	}

	public int getCountByExample(final Object obj, final String condition) {
		List list = (List) this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria executableCriteria = session
								.createCriteria(obj.getClass());
						executableCriteria.add(Example.create(obj).enableLike()
								.excludeZeroes());
						if (condition != null && !"".equals(condition)) {
							executableCriteria.add(Restrictions
									.sqlRestriction(condition));
						}
						return executableCriteria.list();
					}
				});
		return list.size();
	}

	public List getObjectByExample(final Object obj, final int maxResults,
			final int firstResult, final String condition) {
		
		
		return (List) this.getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria executableCriteria = session
								.createCriteria(obj.getClass());
						executableCriteria.add(Example.create(obj).enableLike().excludeZeroes());
						if (!"".equals(condition) && (condition != null)) {
							executableCriteria.add(Restrictions
									.sqlRestriction(condition));
						}
						if (firstResult >= 0) {
							executableCriteria.setFirstResult(firstResult);
						}
						if (maxResults > 0) {
							executableCriteria.setMaxResults(maxResults);
						}
						return executableCriteria.list();
					}
				});
	}

	public List executeJdbcSql(final String jdbcSql, final int columns) {
		return (List) this.getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Connection conn = session.connection();
						Statement stmt = null;
						ResultSet rs = null;
						List list = null;
						try {
							stmt = conn.createStatement();
							rs = stmt.executeQuery(jdbcSql);
							String[] data = null;
							list = new ArrayList();
							while (rs.next()) {
								data = new String[columns];
								for (int i = 0; i < columns; i++) {
									data[i] = rs.getString(i + 1);
								}
								list.add(data);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							if (rs != null)
								rs.close();
							if (stmt != null)
								stmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return list;
					}
				});
	}

	public List getObjectByExample(final Object obj, final String condition) {
		// TODO Auto-generated method stub
		return (List) this.getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria executableCriteria = session
								.createCriteria(obj.getClass());
						executableCriteria.add(
								Example.create(obj).enableLike()
										.excludeZeroes());
						if(condition!=null&&!"".equals(condition))
							executableCriteria.add(Restrictions.sqlRestriction(condition));
						return executableCriteria.list();
					}
				});
	}

	public List getDistinctDept() {
		return (List)this.getHibernateTemplate().executeWithNativeSession(
			new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					//String hql = "select distinct user.bmmc from Users user";
					return session.createQuery("select distinct user.bmmc from Users user").list();
				}
			});
	}

	@SuppressWarnings("unchecked")
	public List executerHsql(final String sql,final int columns) {
		return null;
		/*return (List) this.getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						List<CaseComputer> cclist = (List<CaseComputer>) session.createQuery(sql);
						Connection conn = session.connection();
						List list = null;
						try {
							String[] data = null;
							list = new ArrayList();
							for (CaseComputer cc:cclist) {
								data = new String[columns];
								for (int i = 0; i < columns; i++) {
									data[i] = list.get(i).toString();
								}
								list.add(data);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally{
							session.close();
						}
						return list;
					}
				});*/
	}
}
