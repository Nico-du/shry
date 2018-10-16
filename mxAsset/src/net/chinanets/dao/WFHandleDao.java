package net.chinanets.dao;

import java.util.List;

import net.chinanets.entity.CnstWfstepData;

/**
 * 工作流相关操作DAO层
 * @author dzj
 *
 */
public interface WFHandleDao extends CommonDao {
	//查询所有流程步骤数据
	public String GetWFStepByPaging(String tableName,int tempPageSize,int tempPageCurrent,String strcondition);
	
	//根据类型查找对应的流程步骤
	public List<CnstWfstepData> GetWFStepByType(String strType);
	
	//根据数据ID查询流程历史步骤
	public String GetWFHistoryByDataId(String strDataId);
	
	//根据ID得到对象
	public Object GetDataById(Class<?> tempClass,String strId);
	
	//保存或更改数据
	public boolean SavaOrUpdateData(Object tempObj,boolean isSave);
	
	//删除数据
	public boolean RemoveData(String strSQL);
	
	//删除数据
	public boolean RemoveData(String tableName,String colunmName,String strid);
}
