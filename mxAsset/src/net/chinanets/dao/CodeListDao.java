package net.chinanets.dao;

import net.sf.json.JSONArray;

/**
 * 所有代码表接口
 * @author Liuyong
 *
 */
public interface CodeListDao extends CommonDao {
	//所有代码表查询数据
	public String GetCodeListByPaging(String tableName,int tempPageSize,int tempPageCurrent,String strcondition);
	
	//根据ID得到对象
	public Object GetDataById(Class<?> tempClass,String strId);
	
	//根据代码表类型查询数据
	public JSONArray GetDataByCodeType(String strType);
	
	//查询数据总量
	public int GetDataCount(String strSQL);
	
	//查找数据总量
	public int GetDataCount(String tableName,String colunmName,String strid);
	
	//保存或更改数据
	public boolean SavaOrUpdateData(Object tempObj,boolean isSave);
	
	//删除数据
	public boolean RemoveData(String strSQL);
	
	//删除数据
	public boolean RemoveData(String tableName,String colunmName,String strid);
}
