package net.chinanets.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.chinanets.data.DataEntity;
import net.chinanets.pojos.ShryFyData;
import net.chinanets.pojos.ShrySydData;
import net.sf.json.JSONArray;

/**
 * 通用DAO接口
 * 
 * @author 唐
 */
public interface CommonDao {
	/**
	 * 保持对象
	 * 
	 * @param obj(保持的对象pojo类)
	 */
	public Serializable save(Object obj);

	/**
	 * 删除持久对象
	 * 
	 * @param obj
	 */
	public void delete(Object obj);
	
	/**
	 * 根据hql删除数据
	 * @param hql
	 */
	public void deleteByHql(String hql);

	/**
	 * 更新持久对象
	 * 
	 * @param obj
	 */
	public void update(Object obj);
	
	/**
	 * 更新持久对象
	 * 
	 * @param obj
	 */
	
	public void merge(Object obj);

	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @param c
	 * @return
	 */
	public Object getObjectById(Class c, Serializable id);

	/**
	 * 根据属性查询具体对象
	 * 
	 * @param obj
	 *            有需要查询的字段属性值
	 * @param maxResults
	 *            最大记录数
	 * @param firstResult
	 *            起始记录
	 */
	public List getObjectByExample(Object obj, int maxResults, int firstResult);
	
	/***
	 * 根据对象属性查询总记录条数
	 * @param obj
	 * @return
	 */
	public int getCountByExample(Object obj);

	/**
	 * 根据属性查询出对象，不分页
	 * @param obj
	 * @return
	 */
	public List getObjectByExample(Object obj);

	
	public List getObjectByExample(Object obj,String condition);
	/**
	 * 根据对象属性值查询对象,并且分页
	 * @param obj 有需要查询的字段属性值
	 * @param maxResults	 最大记录数
	 * @param firstResult	起始记录
	 * @param condition	sql语句条件
	 * @return
	 */
	public List getObjectByExample(Object obj,int maxResults, int firstResult,String condition);
	
	/***
	 * 根据对象属性查询、条件总记录条数
	 * @param obj
	 * @param condition
	 * @return
	 */
	public int getCountByExample(Object obj,String condition);
	
	/**
	 * 根据hql语句查询数据的总行数
	 * 
	 * @param hql
	 *            String型的hql语句
	 */
	public int getObjectItemTotalCount(String hql);
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
	public List getObjectItemPage(String hql,int maxResults,int firstResult);
	
	/**
	 * 获得所有的对象
	 * @param hql
	 *           自定义hql语句
	 * @return
	 */
	public List getAllObjectByHql(String hql);
	
	
	public Object getOneColumnValueByHql(String hql);
	
	public int getSequence(String sql);
	
	public List getObjectBySql(String sql);
	
	public void updateObjectBySql(String sql);
	
	/**
	 * 保存或更新数据对象
	 */
	public void saveOrUpdateObject(Object obj);
	
	//根据搜索面板得到SQL语句
	public String FindHqlBySearchForm(String tableName,String strcondition);
	
	///原始SQL方法
	
	//原始数据SQL查询数据
	@SuppressWarnings("deprecation")
	public List<DataEntity> RunSelectDataEntityBySql(String strSQL,Object[] params);
	/**
	 * 原始SQL语句查询数据
	 */
	public JSONArray RunSelectJSONArrayBySql(String strSQL,Object[] params);	
	/**
	 * //原始SQL语句查询数据
	 * @param strSQL :完整的SQL查询语句
	 * @param tempClassPath : 类路径,包含包名
	 * @return
	 */
	public List<?> RunSelectClassBySql(String strSQL,String tempClassPath);
	/**
	 * 原始SQL语句查询数据
	 */
	public List<?> RunSelectClassBySql(String strSQL,Object[] params,Class<?> tempClass);
	
	/**
	 * 原始数据SQL增删改执行方法
	 **/
	public boolean RunUpdateBySQL(String strSQL,Object[] params);
	
	/**
	 * hibernate QBC 查询
	 * 通过型号查询
	 */
	public  List getInfoById (String xh,Object obj);
	/**
	 * 使用hql查询
	 */
	
	public List getInfoByHql(String sql);
	
	/**
	 * 风叶性能导入，查询是否存在重复记录
	 * param：试验单号 + 风叶id 
	 */
	public List<ShrySydData>getFYSydByParam(String sydh,Long fyid);
	
	 /**
	    * 总成性能导入，查询是否存在重复记录
	    * param:   试验单号 +试验性质+试验日期
	    */
	public List<ShrySydData>  getZCSydByParam(String sydh,String syxz,Date syrq);

	/**
	 * 根据风叶型号查询是否存在重复记录
	 * param:xh
	 * @return
	 */
	public 	List<ShryFyData> getShryFybyParam(String xh);
	
}
