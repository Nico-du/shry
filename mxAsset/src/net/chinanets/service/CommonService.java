package net.chinanets.service;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.chinanets.data.DataEntity;
import net.chinanets.pojos.ShrySydData;
import net.sf.json.JSONArray;

public interface CommonService {
	
	/**
	 * 保存对象
	 * @param obj
	 * @return
	 */
	public Long saveObject(Object obj);
	
	/***
	 * 删除对象
	 * @param obj
	 */
	public void deleteObject(Object obj);
	
	/**
	 * 根据ID查询对象
	 * @param obj
	 * @param id
	 * @return
	 */
	public Object getObjectBySId(Object c,Serializable id);
	/**
	 * 根据ID查询对象
	 * @param obj
	 * @param id
	 * @return
	 */
	public Object getObjectById(Object c,Long id);
	
	/**
	 * 修改对象
	 * @param obj
	 */
	public void updateObject(Object obj);
	
	
	
	public void mergeObject(Object obj);
	
	/**
	 * 保存对象列表
	 * @param list
	 */
	public void mergeObjectList(List list);
	
	/**
	 * 根据对象属性值查询对象
	 * @param obj
	 * @return
	 */
	public List getObjectList(Object obj);
	
	public List getObjectList(Object obj,String condition);
	/**
	 * 根据对象属性值查询对象,并且分页
	 * @param obj
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List getObjectList(Object obj,int pageSize, int pageNumber);
	
	/**
	 * 根据对象属性值查询对象,并且分页
	 * @param obj 分页对象
	 * @param pageSize	页大小
	 * @param pageNumber	第几页
	 * @param condition	sql语句条件
	 * @return
	 */
	public List getObjectList(Object obj,int pageSize, int pageNumber,String condition);
	
	/**
	 * 根据对象属性查询总记录条数
	 * @param obj
	 * @return
	 */
	public int getCountByObject(Object obj);
	
	/***
	 * 根据对象属性查询、条件总记录条数
	 * @param obj
	 * @param condition
	 * @return
	 */
	public int getCountByObject(Object obj,String condition);
	
	/**
	 * 根据对象属性值查询对象,并且分页
	 * @param obj 分页对象
	 * @param pageSize	页大小
	 * @param pageNumber	第几页
	 * @param 
	 * @return 一次性返回结果和行数
	 */
	public List getPageList(Object obj, int pageSize, int pageNumber,String condition);
	
	/**
	 * 自定义hql语句
	 * @param hql
	 * @return 返回结果行的数量
	 */
	public int getObjectItemTotalCount(String hql);
	
	
	/**
	 * 删除对象数组
	 * @param List
	 * 
	 */
	
	public void deleteObjectItem(List lt);

	public void deleteOjbectItem(String type, Long[] id);
	
	public List getAllBm();
	
	/**
	 * 保存对象数组
	 * @param List
	 * 
	 */
	public void saveObjectItem(List lt);
	
	/**
	 * 根据sql语句获得数组
	 * @param sql
	 * 
	 */
	public List getObjectBySql(String sql);
	
	public void updateObjectBySql(String sql);
	
	/**
	 * 更新对象数组
	 * @param List
	 * 
	 */
	public void updateObjectItem(List lt);
	
	/**
	 * 保存或更新数据对象
	 */
	public void saveOrUpdateObject(Object obj);

	public int getOneBysql(String sql);
	
	public void removeObjectList(List list);
	
	public List getAllObjectByHql(String hql);
	
	/**
	 * 原始SQL查询数据
	 * @param strSQL
	 * @param params
	 * @return  List<DataEntity>
	 */
	public List<DataEntity> RunSelectDataEntityBySql(String strSQL,Object[] params);
	/**
	 * 原始SQL查询数据
	 * @param strSQL
	 * @param params
	 * @return JSONArray
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
	 * //原始数据SQL增删改执行方法
	 * @param strSQL :完整的SQL查询语句
	 * Object[] params : 参数
	 * @return
	 */
	public boolean RunUpdateBySQL(String strSQL);
	/**
	 * 解析txt
	 */
	public  List readInputByRow(String path);
	
	/**
	 * 解析property
	 */
	public  String readData(String key);
	/**
	 * 解析文件名
	 * @param strPath
	 * @return
	 * @throws Exception 
	 */
	public List refreshFileList(String strPath) throws Exception;
	
	 public  String tree(Object obj, int level);
	 
	 public int clearxml();
	 public List getImfomat(String[] str);
	 public List<String> geNu(String[] a);
	 public List<String> getFu(String[] a,String txtName);
	 public void Delt();
	 public void deleteByHql(String hql);
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
		    * 总成性能导入，查询是否存在重复记录
		    * param:   试验单号 +试验性质+试验日期
		    */
		public List<ShrySydData> getZCSydByParam(String sydh, String syxz,Date syrq) ;

		/**
		 * 风叶性能导入，查询是否存在重复记录
		 * param：试验单号 + 风叶id
		 */
		public List<ShrySydData> getFYSydByParam(String sydh, Long fyxh) ;
		
		/**
		 * 根据风叶型号查询是否存在重复记录
		 * param:xh
		 * @return
		 */
		public int getCountShryFy(String xh);
		
		
		public String getDictionaryByKey(String dictType,String dictKey);
}
