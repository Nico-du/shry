package net.chinanets.service;


/**
 * 代码表Service接口
 * @author Liuyong
 *
 */
public interface CodeListService extends CommonService {

	//分页查询所有代码表
	public String GetCodeList(String tempObjBeanName,int tempPageSize,int tempPageCurrent,String strcondition);
	
	//根据类型获取代码表
	public String GetCodeListByType(String typeName);
	
	//保存或更改代码表数据
	public String SaveOrUpdateCodeList(String codeListJson);
	
	//删除代码表
	public String RemoveCodeList(String codeListID);
}
