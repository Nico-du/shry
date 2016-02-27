package net.chinanets.dao;

import java.io.Serializable;
import java.util.List;

import net.chinanets.data.DataEntity;

/**
 * 查询所有信息接口
 * @author Liuyong
 *
 */
public interface AllAssetDao extends CommonDao {
	//分页查询数据
	public String GetAllAssetListByPaging(String tableName,int tempPageSize,int tempPageCurrent,String strcondition);
	
	//根据ID得到对象
	public Object GetDataById(Class<?> tempClass,Serializable strId);
	
	//根据SQL查询DataEntity
	public List<DataEntity> GetDataEntityBySQL(String strSQL);
	
	//查询数据总量
	public int GetDataCount(String strSQL);
	
	//查找数据总量
	public int GetDataCount(String tableName,String colunmName,String strRKDID);
	
	//保存或更改数据
	public boolean SavaOrUpdateData(Object tempObj,boolean isSave);
	
	//根据SQL语句删除数据
	public boolean RemoveData(String strSQL);
	
	//根据相关参数删除数据
	public boolean RemoveData(String tableName,String colunmName,String strid);
	//查询流程定义数据
	public List<?> getFlowDefine(String flowType);
}
