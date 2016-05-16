package net.chinanets.dao.imp;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.chinanets.dao.CommonDao;
import net.chinanets.data.DataEntity;
import net.chinanets.pojos.ShrySydData;
import net.chinanets.utils.helper.ConditionHelper;
import net.chinanets.utils.helper.DateHelper;
import net.chinanets.utils.helper.JsonHelper;
import net.chinanets.utils.helper.StringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 通用DAO接口实现类
 * 
 * @author 唐
 * 
 */
public class CommonDaoImp extends HibernateDaoSupport implements CommonDao,Serializable {
	private static Log log = LogFactory.getLog(CommonDaoImp.class);

	/**
	 * 保存对象
	 * 
	 * @param obj
	 *            普通对象
	 */
	public Serializable save(Object obj) {
		return this.getHibernateTemplate().save(obj);
	}

	/**
	 * 删除对象
	 * 
	 * @param obj
	 *            对象
	 */
	public void delete(Object obj) {
		this.getHibernateTemplate().delete(obj);
	}

	/**
	 * 修改对象
	 * 
	 * @param obj
	 */
	public void update(Object obj) {
		this.getHibernateTemplate().update(obj);
	}

	/**
	 * 修改对象
	 * 
	 * @param obj
	 */
	public void merge(Object obj) {

		this.getHibernateTemplate().merge(obj);
	}

	/**
	 * 根据ID查询对象
	 * 
	 * @param c
	 *            类类型
	 * @param id
	 *            id
	 */
	public Object getObjectById(Class c, Serializable id) {
		// TODO Auto-generated method stub
		Object obj = this.getHibernateTemplate().get(c, id);
		return obj;
	}

	/**
	 * 根据属性查询具体对象
	 * 
	 * @param obj
	 *            有需要查询的字段属性值
	 * @param maxResult
	 *            每页显示页数
	 * @param firstResult
	 *            起始页
	 */

	public List getObjectByExample(final Object obj, final int maxResults,
			final int firstResult) {
		// TODO Auto-generated method stub

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

	/**
	 * 根据属性查询出对象，不分页
	 * 
	 * @param obj
	 * @return
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	public List getObjectByExample(final Object obj, final String condition) {
		return (List) this.getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
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
	}

	/***************************************************************************
	 * 根据对象属性查询总记录条数
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	/**
	 * 根据对象属性值查询对象,并且分页
	 * 
	 * @param obj
	 *            有需要查询的字段属性值
	 * @param maxResults
	 *            最大记录数
	 * @param firstResult
	 *            起始记录
	 * @param condition
	 *            sql语句条件
	 * @return
	 */
	public List getObjectByExample(final Object obj, final int maxResults,
			final int firstResult, final String condition) {
		return (List) this.getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria executableCriteria = session
								.createCriteria(obj.getClass());
						executableCriteria.add(Example.create(obj).ignoreCase()
								.enableLike().excludeZeroes());
						if (condition != null && !"".equals(condition)) {
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

	/***************************************************************************
	 * 根据对象属性查询、条件总记录条数
	 * 
	 * @param obj
	 * @param condition
	 * @return
	 */
	public int getCountByExample(final Object obj, final String condition) {
		// TODO Auto-generated method stub
		List list = (List) this.getHibernateTemplate()
				.executeWithNativeSession(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria executableCriteria = session
								.createCriteria(obj.getClass());
						executableCriteria.add(Example.create(obj).ignoreCase()
								.enableLike().excludeZeroes());
						if (condition != null && !"".equals(condition)) {
							executableCriteria.add(Restrictions
									.sqlRestriction(condition));
						}
						return executableCriteria.list();
					}
				});
		return list.size();
	}

	/**
	 * 根据hql语句查询数据的总行数
	 * 
	 * @param hql
	 *            String型的hql语句
	 */

	public int getObjectItemTotalCount(String hql) {
		int result = 0;
		try {
			Session session = this.getSession();
			Query query = session.createQuery(hql);
			if (query.uniqueResult() != null)
				result = Integer.parseInt(query.uniqueResult().toString());
			else
				result = 0;
		} catch (Exception ex) {
			logger.error("获取数据总行数失败！！");
		}
		return result;
	}

	/**
	 * 根据自定义hql语句查询具体对象集合
	 * 
	 * @param hql
	 *            自定义hql语句
	 * @param maxResult
	 *            每页显示页数
	 * @param firstResult
	 *            起始页
	 */
	public List getObjectItemPage(String hql, int maxResults, int firstResult) {
		try {
			Session session = this.getSession();
			Query query = session.createQuery(hql);
			int index = (firstResult - 1) * maxResults;
			query.setFirstResult(index);
			query.setMaxResults(maxResults);
			return query.list();
		} catch (Exception ex) {
			logger.error("获取数据失败！！", ex);
			return null;
		}
	}

	/**
	 * 获得所有的对象
	 * 
	 * @param hql
	 *            自定义hql语句
	 * @return
	 */
	public List getAllObjectByHql(String hql) {
		try {
			Session session = this.getSession();
			Query query = session.createQuery(hql);
			return query.list();
		} catch (Exception ex) {
			logger.error("获取数据失败！！", ex);
			return null;
		}
	}

	public Object getOneColumnValueByHql(String hql) {
		try {
			Session session = this.getSession();
			Query query = session.createQuery(hql);
			return query.uniqueResult();
		} catch (Exception ex) {
			logger.error("获取数据列失败！！", ex);
			return null;
		}
	}

	public int getSequence(String sql) {
		try {
			Session session = this.getSession();
			SQLQuery query = session.createSQLQuery(sql);
			return Integer.parseInt(query.uniqueResult().toString());
		} catch (Exception ex) {
			logger.error("获取数据列失败！！", ex);
			return 0;
		}
	}

	public List getObjectBySql(String sql) {
		try {
			Session session = this.getSession();
			SQLQuery query = session.createSQLQuery(sql);
			return query.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void updateObjectBySql(String sql) {
		Session session = this.getSession(); 
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			session.createQuery(sql).executeUpdate();
			transaction.commit();
		} catch (Exception ex) {
			transaction.rollback();
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}

	/**
	 * 保存或更新数据对象
	 */
	public void saveOrUpdateObject(Object obj) {
		this.getHibernateTemplate().saveOrUpdate(obj);
	}

	public void deleteByHql(String hql) {
		this.getSession().createQuery(hql).executeUpdate();
	}

	// BaseDaoEx的相关操作方法

	// 将实体对象的添加或修改同步到数据库中
	public boolean SaveEx(Object po) {
		Session session=this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tr=null;
		try {
			tr = session.beginTransaction();
			session.save(po);
			tr.commit();
			return true;
		} catch (Exception e) {
			tr.rollback();
			log.error(e.getMessage());
			return false;
		}finally{
			session.close();
		}
	}

	// 根据实体对象更改数据
	public boolean UpdateEx(Object po) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tr =null;
		try {
			tr= session.beginTransaction();
			session.update(po);
			tr.commit();
			return true;
		} catch (Exception e) {
			tr.rollback();
			log.error(e.getMessage());
			return false;
		}finally{
			session.close();
		}
	}

	// 根据HQL语句更改数据
	public boolean UpdateByHqlEx(String strHQL) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tr=null;
		try {
			tr = session.beginTransaction();
			int result=session.createQuery(strHQL).executeUpdate();
			tr.commit();
			if(result<1){
				return false;
			}
			return true;
		} catch (Exception e) {
			tr.rollback();
			log.error(e.getMessage());
			return false;
		}finally{
			session.close();
		}
	}

	// 根据实体对象删除数据
	public boolean DeleteEx(Object po) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tr=null;
		try {
			tr= session.beginTransaction();
			session.delete(po);
			tr.commit();
			return true;
		} catch (Exception e) {
			tr.rollback();
			log.error(e.getMessage());
			return false;
		}finally{
			session.close();
		}
	}

	// 根据Id删除数据
	@SuppressWarnings("rawtypes")
	public boolean DeleteByIdEx(Class classs, String strId) {
		Object po = FindByIdEx(classs, strId);
		if (DeleteEx(po)) {
			return true;
		}
		return false;
	}

	// 根据条件删除数据
	public boolean DeleteByHql(String hql, Object[] params) {
		Session session =this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tr = session.beginTransaction();
		boolean result = false;
		try {
			Query query = session.createQuery(hql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i, params[i]);
				}
			}
			int num = query.executeUpdate();
			tr.commit();
			if (num > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			tr.rollback();
		}finally{
			session.close();
		}
		return result;
	}

	// 根据Id查找对象
	@SuppressWarnings("rawtypes")
	public Object FindByIdEx(Class classs, Serializable strId) {
		Session session = this.getSession();
		try {
			return session.get(classs.getName(), strId);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	// 查询对象所有数据
	@SuppressWarnings("rawtypes")
	public List<?> FindObjectsEx(Class classs) {
		List<?> poList = null;
		Session session = this.getSession();
		try {
			String hql = "from " + classs.getName();
			poList = session.createQuery(hql).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return poList;
	}

	// 根据条件查询对象集合
	public List<?> FindListByHQLEx(String hql, Object[] params) {
		Session session = this.getSession();
		List<?> poList = null;
		try {
			Query query = session.createQuery(hql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i, params[i]);
				}
			}
			poList = query.list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
		return poList;
	}

	/**
	 * 多条件查询指定行数
	 * 
	 * @param hql
	 *            hql语句
	 * @param params
	 *            条件数组
	 * @param num
	 *            查询指定行数
	 * @return
	 */
	public List<?> FindListByHQLEx(String hql, Object[] params, int count) {
		Session session = this.getSession();
		List<?> poList = null;
		try {
			Query query = session.createQuery(hql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i, params[i]);
				}
			}
			if (count > 0) {
				query.setMaxResults(count);
			}
			poList = query.list();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return poList;
	}

	// 根据条件查询一个对象
	public Object FindPoByHQLEx(String hql, Object[] params) {
		Session session = this.getSession();
		Object po = null;
		try {
			Query query = session.createQuery(hql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i, params[i]);
				}
			}
			query.setMaxResults(1);
			po = (Object) query.uniqueResult();

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return po;
	}

	//根据搜索面板得到SQL语句
	public String FindHqlBySearchForm(String tableName,String strcondition){
		String strSQL=StringHelper.Format("from %1$s TMPT where 1>0",tableName);
		if(StringHelper.IsNullOrEmpty(strcondition)){
			return strSQL;
		}
		String orderStr = strcondition.substring(strcondition.indexOf("}")+1);
		JSONObject jsonMap=JSONObject.fromObject(strcondition);
		if(jsonMap ==null){
			return strSQL;
		}
		Iterator<?> tempIterator=jsonMap.keys();
		String strKey=null;
		String tempConditionStr="";
		while(tempIterator.hasNext()){
			strKey=tempIterator.next().toString();
			tempConditionStr=ConditionHelper.GetConditionString(strKey, jsonMap.get(strKey), true);
			if(StringHelper.IsNullOrEmpty(tempConditionStr)){
				continue;
			}
			strSQL+=" and ("+tempConditionStr+")";
		}
		return strSQL +" "+ orderStr;
	}
	
	//根据搜索面板得到HQL语句
	public String FindHqlBySearchForm(Class<?> tempClass,String strcondition){
		String strHql=StringHelper.Format("from %1$s where 1>0",tempClass.getSimpleName());
		if(StringHelper.IsNullOrEmpty(strcondition)){
			return strHql;
		}
		JSONObject jsonMap=JSONObject.fromObject(strcondition);
		if(jsonMap ==null){
			return strHql;
		}
		Iterator<?> tempIterator=jsonMap.keys();
		String strKey=null;
		String tempConditionStr="";
		while(tempIterator.hasNext()){
			strKey=tempIterator.next().toString();
			tempConditionStr=ConditionHelper.GetConditionString(strKey,jsonMap.get(strKey),tempClass,true);
			if(StringHelper.IsNullOrEmpty(tempConditionStr)){
				continue;
			}
			strHql+=" and "+tempConditionStr;
		}
		return strHql;
	}
	
	//搜索面板处理分页
	public List<?> FindPoPageByCondition(String strHql,int tempPageSize,int tempPageCurrent) {
		int datastart=(tempPageCurrent-1)*tempPageSize;
		return FindPoPageEx(strHql,null,datastart<0?0:datastart,tempPageSize);
	}		
	
	// HQL,多条件查询分页
	public List<?> FindPoPageEx(String hql, Object[] params, int start, int limit) {
		Session session = this.getSession();
		List<?> poList = null;
		try {
			Query query = session.createQuery(hql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i, params[i]);
				}
			}
			query.setFirstResult(start);
			query.setMaxResults(limit);
			poList = query.list();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return poList;
	}
	
	//原始数据SQL分页查询
	public List<DataEntity> RunSelectDataEntityBySql(String strSQL,int pagesize,int currentpage){
		if(StringHelper.IsNullOrEmpty(strSQL)){
			return null;
		}
		int start=(currentpage-1)*pagesize;
		int end=start+pagesize;
		String newStrSQL=StringHelper.Format("SELECT * FROM (%1$s) T where RN>%2$s AND RN<=%3$s",strSQL,start,end);
		return RunSelectDataEntityBySql(newStrSQL,null);
	}
	
	//原始数据SQL分页查询
	public JSONArray RunSelectJSONArrayBySql(String strSQL,int pagesize,int currentpage){
		if(StringHelper.IsNullOrEmpty(strSQL)){
			return null;
		}
		int start=(currentpage-1)*pagesize;
		int end=start+pagesize;
		String newStrSQL=StringHelper.Format(" %1$s LIMIT %2$s , %3$s",strSQL,start,end);
		return RunSelectJSONArrayBySql(newStrSQL, null);
	}
	
	/* oracle的分页方式 2
	 //原始数据SQL分页查询
	public JSONArray RunSelectJSONArrayBySql(String strSQL,int pagesize,int currentpage){
		if(StringHelper.IsNullOrEmpty(strSQL)){
			return null;
		}
		int start=(currentpage-1)*pagesize;
		int end=start+pagesize;
		String newStrSQL=StringHelper.Format("SELECT * FROM (%1$s) T where RN>%2$s AND RN<=%3$s",strSQL,start,end);
		return RunSelectJSONArrayBySql(newStrSQL, null);
	}
	 */
	
	//原始SQL查询总数量
	@SuppressWarnings("deprecation")
	public int RunSelectCountBySql(String strSQL,Object[]params){
		Session session =this.getHibernateTemplate().getSessionFactory().openSession();
		try{
			Connection sqlconnet=session.connection();
			if(sqlconnet==null){
				return 0;
			}
			PreparedStatement pStatement=sqlconnet.prepareStatement(strSQL);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pStatement.setObject(i+1, params[i]);
				}
			}
			ResultSet result= pStatement.executeQuery();
			if(result==null){
				return 0;
			}
			if(result.next()){
				return result.getInt(1);
			}
		}catch(Exception ex){
			log.error(ex.getMessage());
		}finally{
			session.close();
		}
		return 0;
	}
	
	//原始数据SQL查询数据
	@SuppressWarnings("deprecation")
	public List<DataEntity> RunSelectDataEntityBySql(String strSQL,Object[] params){
		Session session =this.getHibernateTemplate().getSessionFactory().openSession();
		List<DataEntity> dataList=null;
		try{
			Connection sqlconnet=session.connection();
			if(sqlconnet==null){
				return dataList;
			}
			PreparedStatement pStatement=sqlconnet.prepareStatement(strSQL);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pStatement.setObject(i+1, params[i]);
				}
			}
			ResultSet result= pStatement.executeQuery();
			if(result==null){
				return dataList;
			}
			ResultSetMetaData resultMeta=result.getMetaData();
			int columCount=resultMeta.getColumnCount();
			dataList=new ArrayList<DataEntity>();
			DataEntity dataEntity=null;
			while(result.next()){
				dataEntity=new DataEntity();
				for(int i=1;i<=columCount;i++){
					String columnName=resultMeta.getColumnName(i);
					String columnType=resultMeta.getColumnTypeName(i);
					Object columnValue=null;
					if(StringHelper.Compare(columnType, "DATE", true)==0){
						columnValue=result.getTimestamp(columnName);
						columnValue=DateHelper.GetStrDateByDateClass(columnValue, null);
					}else{
						columnValue=result.getObject(columnName);
					}
					dataEntity.SetValue(columnName, columnValue);
				}
				dataList.add(dataEntity);
			}
		}catch(Exception ex){
			log.error(ex.getMessage());
		}finally{
			session.close();
		}
		return dataList;
	}
	
	//原始SQL语句查询数据
	@SuppressWarnings("deprecation")
	public JSONArray RunSelectJSONArrayBySql(String strSQL,Object[] params){
		Session session =this.getHibernateTemplate().getSessionFactory().openSession();
		JSONArray dataJsonArray=null;
		try{
			Connection sqlconnet=session.connection();
			if(sqlconnet==null){
				return dataJsonArray;
			}
			PreparedStatement pStatement=sqlconnet.prepareStatement(strSQL);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pStatement.setObject(i+1, params[i]);
				}
			}
			ResultSet result= pStatement.executeQuery();
			if(result==null){
				return dataJsonArray;
			}
			ResultSetMetaData resultMeta=result.getMetaData();
			int columCount=resultMeta.getColumnCount();
			dataJsonArray=new JSONArray();
			JSONObject tempJson=null;
			while(result.next()){
				tempJson=new JSONObject();
				for(int i=1;i<=columCount;i++){
					String columnName=resultMeta.getColumnName(i);
					String columnType=resultMeta.getColumnTypeName(i);
					Object columnValue=null;
					if(StringHelper.Compare(columnType, "DATE", true)==0 || StringHelper.Compare(columnType, "DATETIME", true)==0){
						columnValue=result.getTimestamp(columnName);
						columnValue=DateHelper.GetStrDateTimeByDateClass(columnValue, null);
					}else{
						columnValue=result.getObject(columnName);
					}
					tempJson.put(columnName.toLowerCase(), columnValue);
				}
				dataJsonArray.add(tempJson);
			}
		}catch(Exception ex){
			log.error(ex.getMessage());
		}finally{
			session.close();
		}
		return dataJsonArray;
	}
	/**
	 * //原始SQL语句查询数据
	 * @param strSQL :完整的SQL查询语句
	 * @param tempClassPath : 类路径,包含包名
	 * @return
	 */
	public List<?> RunSelectClassBySql(String strSQL,String tempClassPath){
		Class clazz = null;
		try {
			clazz = Class.forName(tempClassPath);
			if(clazz == null) return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return this.RunSelectClassBySql(strSQL,null,clazz);
	}
	//原始SQL语句查询数据
	@SuppressWarnings("deprecation")
	public List<?> RunSelectClassBySql(String strSQL,Object[] params,Class<?> tempClass){
		Session session =this.getHibernateTemplate().getSessionFactory().openSession();
		List<Object> dataList=null;
		try{
			Connection sqlconnet=session.connection();
			if(sqlconnet==null){
				return dataList;
			}
			PreparedStatement pStatement=sqlconnet.prepareStatement(strSQL);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pStatement.setObject(i+1, params[i]);
				}
			}
			ResultSet result= pStatement.executeQuery();
			if(result==null){
				return dataList;
			}
			ResultSetMetaData resultMeta=result.getMetaData();
			int columCount=resultMeta.getColumnCount();
			dataList=new ArrayList<Object>();
			JSONObject tempJson=null;
			while(result.next()){
				tempJson=new JSONObject();
				for(int i=1;i<=columCount;i++){
					String columnName=resultMeta.getColumnName(i);
					String columnType=resultMeta.getColumnTypeName(i);
					Object columnValue=null;
					if(StringHelper.Compare(columnType, "DATE", true)==0){
						columnValue=result.getTimestamp(columnName);
						columnValue=DateHelper.GetStrDateByDateClass(columnValue, null);
					}else{
						columnValue=result.getObject(columnName);
					}
					tempJson.put(columnName.toLowerCase(), columnValue);
				}
				Object tempObj=JsonHelper.GetBeanByJsonString(tempJson.toString(), tempClass);
				dataList.add(tempObj);
			}
		}catch(Exception ex){
			log.error(ex.getMessage());
		}finally{
			session.close();
		}
		return dataList;
	}
	
	//原始数据SQL增删改执行方法
	@SuppressWarnings("deprecation")
	public boolean RunUpdateBySQL(String strSQL,Object[] params){
		Session session =this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction dbTransaction = session.beginTransaction();
		try{
			Connection sqlconnet=session.connection();
			if(sqlconnet==null){
				return false;
			}
			PreparedStatement pStatement=sqlconnet.prepareStatement(strSQL);
			if(params!=null){
				for(int i=0;i<params.length;i++){
					pStatement.setObject(i+1, params[i]);
				 }
			}
			int resultCount=pStatement.executeUpdate();
			if(resultCount<1){
				dbTransaction.rollback();
				return false;
			}else{
				dbTransaction.commit();
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			dbTransaction.rollback();
			return false;
		}finally{
			session.close();
		}
	}
	
	
	/**
	 * hibernate QBC 查询
	 * 通过型号查询
	 */
	
	public  List getInfoById (String xh,Object obj){
		Session session =this.getSessionFactory().getCurrentSession();
		Transaction ts = session.beginTransaction();
		 List<Object>list = new ArrayList<Object>();
		try {
			String className = obj.getClass().getName();
			Class  name = Class.forName(className);
			DetachedCriteria dc = DetachedCriteria.forClass(name);
			if(xh!=null&&!"".equals(xh)){
				dc.add(Restrictions.eq("xh", xh));
			}
			list =this.getHibernateTemplate().findByCriteria(dc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return     list;
	}
	
	/**
	 * 使用hql查询
	 */
	
	public List getInfoByHql(String sql){
		
		Session session =this.getSessionFactory().getCurrentSession();
		 Transaction ts = session.beginTransaction();
		 List<Object>list = new ArrayList<Object>();
		 try {
		    Query query = session.createQuery(sql);
		    list = query.list();
		   ts.commit();
		  } catch (HibernateException ex) {
	        ts.rollback();
	        ex.printStackTrace();
		 }finally{
			session.close();
		 }
		 return list;
	}
	
	   /**
	    * 总成性能导入，查询是否存在重复记录
	    * param:   试验单号 +试验性质+试验日期
	    */
		public List<ShrySydData>  getZCSydByParam(Long syId,String syxz,Date syrq){
			DetachedCriteria dc = DetachedCriteria.forClass(ShrySydData.class);
			if(syId!=null){
				dc.add(Restrictions.eq("lxdid", syId));
			}
			if(StringUtils.isNotBlank(syxz)){
				dc.add(Restrictions.eq("symd", syxz));
			}
			if(syrq!=null){
				dc.add(Restrictions.eq("syrq", syrq));
			}
			return getHibernateTemplate().findByCriteria(dc);
		}
		
		/**
		 * 风叶性能导入，查询是否存在重复记录
		 * param：试验单号 + 风叶id 
		 */
		public List<ShrySydData>getFYSydByParam(Long syId,Long fyid){
			
			DetachedCriteria dc = DetachedCriteria.forClass(ShrySydData.class);
			if(syId!=null){
				dc.add(Restrictions.eq("lxdid", syId));
			}
			if(fyid!=null){
				dc.add(Restrictions.eq("fyid", fyid));
			}
			return getHibernateTemplate().findByCriteria(dc);
		}

}
