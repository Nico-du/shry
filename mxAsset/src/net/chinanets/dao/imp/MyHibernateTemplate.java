package net.chinanets.dao.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.Assert;

public class MyHibernateTemplate extends org.springframework.orm.hibernate3.HibernateTemplate {
	
	public MyHibernateTemplate(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List findByExample(final Object exampleEntity,
			final int firstResult, final int maxResults)
			throws DataAccessException {
		
		Assert.notNull(exampleEntity, "Example entity must not be null");
		return (List) executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session.createCriteria(
						exampleEntity.getClass()).setCacheable(true);
				executableCriteria.add(Example.create(exampleEntity)
						.enableLike());
				// prepareCriteria(executableCriteria);
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

	public List findByExample(final Object exampleEntity,
			final int firstResult, final int maxResults, final String condition)
			throws DataAccessException {

		Assert.notNull(exampleEntity, "Example entity must not be null");
		return (List) executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria.add(Example.create(exampleEntity)
						.enableLike());
				if (condition != null && !condition.equals("")) {
					String newCondition = condition.replaceAll("`", "'");
					executableCriteria.add(Restrictions
							.sqlRestriction(newCondition));
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

	// QBC����ʽ
	public int getCountByExample(final Object exampleEntity)
			throws DataAccessException {
		List list = (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session.createCriteria(
						exampleEntity.getClass()).setCacheable(true);
				executableCriteria.setProjection(Projections.rowCount()).add(
						Example.create(exampleEntity).enableLike());
				return executableCriteria.list();
			}
		});
		int count = 0;
		if (list.size() > 0) {
			count = ((Integer) list.get(0)).intValue();
		}
		return count;

	}

	public String getMaxByExample(final Object exampleEntity,
			final String maxProperty) throws DataAccessException {
		List list = (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria.setProjection(Projections.max(maxProperty))
						.add(Example.create(exampleEntity).enableLike());
				return executableCriteria.list();
			}
		});
		String max = "";
		if (list.size() > 0) {
			max = ((String) list.get(0));
		}
		return max;

	}

	public int getCountByExample(final Object exampleEntity,
			final String condition) throws DataAccessException {
		List list = (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria.setProjection(Projections.rowCount()).add(
						Example.create(exampleEntity).enableLike());
				if (condition != null && !condition.equals("")) {
					String newCondition = condition.replaceAll("`", "'");
					executableCriteria.add(Restrictions
							.sqlRestriction(newCondition));
				}
				return executableCriteria.list();
			}
		});
		int count = 0;
		if (list.size() > 0) {
			count = ((Integer) list.get(0)).intValue();
		}
		return count;

	}

	public String getMaxByExample(final Object exampleEntity,
			final String maxProperty, final String condition)
			throws DataAccessException {
		List list = (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria.setProjection(Projections.max(maxProperty))
						.add(Example.create(exampleEntity).enableLike());
				if (condition != null && !condition.equals("")) {
					String newCondition = condition.replaceAll("`", "'");
					executableCriteria.add(Restrictions
							.sqlRestriction(newCondition));
				}
				return executableCriteria.list();
			}
		});
		String max = "";
		if (list.size() > 0) {
			max = ((String) list.get(0));
		}
		return max;

	}

	public Map getGroupCountByExample(final Object exampleEntity,
			final String groupByProperty) throws DataAccessException {
		List list = (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria
						.setProjection(
								Projections
										.projectionList()
										.add(Projections.rowCount())
										.add(
												Projections
														.groupProperty(groupByProperty)))
						.add(Example.create(exampleEntity).enableLike());
				return executableCriteria.list();
			}
		});
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			map.put(o[1], o[0]);
		}
		return map;
	}

	public Map getGroupCountByExample(final Object exampleEntity,
			final String groupByProperty, final String condition)
			throws DataAccessException {
		List list = (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria
						.setProjection(
								Projections
										.projectionList()
										.add(Projections.rowCount())
										.add(
												Projections
														.groupProperty(groupByProperty)))
						.add(Example.create(exampleEntity).enableLike());
				if (condition != null && !condition.equals("")) {
					String newCondition = condition.replaceAll("`", "'");

					/*
					 * String[] cons=newCondition.split(";"); for(int i=0;i<cons.length;i++){
					 * String oneCon=cons[i]; String[]
					 * oneCons=oneCon.split("[|]"); String symbol=oneCons[1];
					 * if(symbol.equals("=")){
					 * executableCriteria.add(Restrictions.eq(oneCons[0],
					 * oneCons[2])); }else if(symbol.equals("<")){
					 * executableCriteria.add(Restrictions.lt(oneCons[0],
					 * oneCons[2])); }else if(symbol.equals("<=")){
					 * executableCriteria.add(Restrictions.le(oneCons[0],
					 * oneCons[2])); }else if(symbol.equals(">")){
					 * executableCriteria.add(Restrictions.gt(oneCons[0],
					 * oneCons[2])); }else if(symbol.equals(">=")){
					 * executableCriteria.add(Restrictions.ge(oneCons[0],
					 * oneCons[2])); }else if(symbol.equalsIgnoreCase("sql")){
					 * executableCriteria.add(Restrictions.sqlRestriction(oneCons[2])); } }
					 */
					executableCriteria.add(Restrictions
							.sqlRestriction(newCondition));
				}
				return executableCriteria.list();
			}
		});
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			map.put(o[1], o[0]);
		}
		return map;
	}

	public List findByExample(final Object exampleEntity,
			final int firstResult, final int maxResults,
			final String propertyName, final Object propertyValue,
			final String symbol) throws DataAccessException {

		Assert.notNull(exampleEntity, "Example entity must not be null");
		return (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria.add(Example.create(exampleEntity)
						.enableLike());
				if (propertyName != null && propertyValue != null
						&& symbol != null) {
					if (symbol.equals(">")) {
						executableCriteria.add(Restrictions.gt(propertyName,
								propertyValue));
					}
					if (symbol.equals(">=")) {
						executableCriteria.add(Restrictions.ge(propertyName,
								propertyValue));
					}
					if (symbol.equals("<")) {
						executableCriteria.add(Restrictions.lt(propertyName,
								propertyValue));
					}
					if (symbol.equals("<=")) {
						executableCriteria.add(Restrictions.le(propertyName,
								propertyValue));
					}
				}
				prepareCriteria(executableCriteria);
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

	public int getCountByExample(final Object exampleEntity,
			final String propertyName, final Object propertyValue,
			final String symbol) throws DataAccessException {
		List list = (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria.setProjection(Projections.rowCount()).add(
						Example.create(exampleEntity).enableLike());
				if (propertyName != null && propertyValue != null
						&& symbol != null) {
					if (symbol.equals(">")) {
						executableCriteria.add(Restrictions.gt(propertyName,
								propertyValue));
					}
					if (symbol.equals(">=")) {
						executableCriteria.add(Restrictions.ge(propertyName,
								propertyValue));
					}
					if (symbol.equals("<")) {
						executableCriteria.add(Restrictions.lt(propertyName,
								propertyValue));
					}
					if (symbol.equals("<=")) {
						executableCriteria.add(Restrictions.le(propertyName,
								propertyValue));
					}
				}
				return executableCriteria.list();
			}
		});
		int count = 0;
		if (list.size() > 0) {
			count = ((Integer) list.get(0)).intValue();
		}
		return count;
	}

	public List findByExample(final Object exampleEntity,
			final int firstResult, final int maxResults,
			final String[] orderPropertys, final String[] ascOrDescs)
			throws DataAccessException {

		Assert.notNull(exampleEntity, "Example entity must not be null");
		return (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria.add(Example.create(exampleEntity)
						.enableLike());
				if (orderPropertys != null && ascOrDescs != null) {
					for (int i = 0; i < orderPropertys.length; i++) {
						String ssss = ascOrDescs[i];
						if (ascOrDescs[i].equalsIgnoreCase("asc")) {
							executableCriteria = executableCriteria
									.addOrder(Order.asc(orderPropertys[i]));
						}
						if (ascOrDescs[i].equalsIgnoreCase("desc")) {
							executableCriteria = executableCriteria
									.addOrder(Order.desc(orderPropertys[i]));
						}
					}
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

	public List findByExample(final Object exampleEntity,
			final int firstResult, final int maxResults,
			final String[] orderPropertys, final String[] ascOrDescs,
			final String condition) throws DataAccessException {

		Assert.notNull(exampleEntity, "Example entity must not be null");
		return (List) execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria executableCriteria = session
						.createCriteria(exampleEntity.getClass());
				executableCriteria.add(Example.create(exampleEntity)
						.enableLike());
				if (orderPropertys != null && ascOrDescs != null) {
					for (int i = 0; i < orderPropertys.length; i++) {
						String ssss = ascOrDescs[i];
						if (ascOrDescs[i].equalsIgnoreCase("asc")) {
							executableCriteria = executableCriteria
									.addOrder(Order.asc(orderPropertys[i]));
						}
						if (ascOrDescs[i].equalsIgnoreCase("desc")) {
							executableCriteria = executableCriteria
									.addOrder(Order.desc(orderPropertys[i]));
						}
					}
				}
				if (condition != null && !condition.equals("")) {
					String newCondition = condition.replaceAll("`", "'");
					executableCriteria.add(Restrictions
							.sqlRestriction(newCondition));
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
}
