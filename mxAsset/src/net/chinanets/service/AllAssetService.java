package net.chinanets.service;

import java.util.List;

/**
 * 所有物资Service接口
 * @author Liuyong
 *
 */
public interface AllAssetService extends CommonService {

	/**
	 * 分页查询所有物资
	 * @param tempObjBeanName
	 * @param tempPageSize
	 * @param tempPageCurrent
	 * @param strcondition
	 * @return
	 */
	public String GetAllAsset(String tempObjBeanName,int tempPageSize,int tempPageCurrent,String strcondition);
	
	
	/**
	 * 保存或更改Object
	 * @param ObjectJson 
	 * @param classPath 带包名的路径 (net.chinanets.pojos.DicFunriture)
	 * @param idColumn 主键名
	 * @param objName 
	 * @return
	 */
	public String SaveOrUpdateObject(String ObjectJson,String classPath,String idColumn,String objName);
	
	
	/**
	 * 删除Object
	 * 不考虑存在子表外键数据
	 * @param dataid
	 * @param tableName 表名 数据库中表名 (DIC_FUNRITURE)
	 * @param idColumn 主键名
	 * @param objName
	 * @return
	 */
	public String RemoveObject(String dataid,String tableName,String idColumn,String objName);
	
	/**
	 * 保存或更改日用耗材
	 * @param RYHCJson
	 * @param strMoudel
	 * @return
	 */
	public String SaveOrUpdateRYHC(String RYHCJson,String strMoudel);
	
	/**
	 * 删除日用耗材
	 * @param ryhcid
	 * @return
	 */
	public String RemoveRYHC(String ryhcid);
	
	/**
	 * 保存或更改入库登记
	 * @param RKDJJson
	 * @return
	 */
	public String SaveOrUpdateRKDJ(String RKDJJson);
	
	/**
	 * 删除入库单
	 * @param rkdid
	 * @return
	 */
	public String RemoveRKD(String rkdid);
	
	/**
	 * 保存或更改入库明细
	 * @param RYHCMXJson
	 * @return
	 */
	public String SaveOrUpdateRYHCMX(String RYHCMXJson);
	
	/**
	 * 删除入库明细
	 * @param ryhcmxid
	 * @return
	 */
	public String RemoveRYHCMX(String ryhcmxid);
	
	/**
	 * 得到申请领用流程XML
	 * @param strType
	 * @param userId
	 * @param ruleId
	 * @return
	 */
	public String GetSQLYWFXML(String strType, String userId,String ruleId,Boolean isShr);
	
	/**
	 * 保存或更改领用单
	 * @param LYDJson
	 * @return
	 */
	public String SaveOrUpdateLYD(String LYDJson);
	
	/**
	 * 删除领用单
	 * @param lydid
	 * @return
	 */
	public String RemoveLYD(String lydid);
	
	/**
	 * 保存或更改领用明细
	 * @param LYDMXJson
	 * @return
	 */
	public String SaveOrUpdateLYDMX(String LYDMXJson);
	
	/**
	 * 删除领用明细
	 * @param lydmxid
	 * @return
	 */
	public String RemoveLYDMX(String lydmxid);
	
	
	///退库的CRUD
	
	/**
	 * 保存或更改入库登记
	 * @param TKDJJson
	 * @return
	 */
	public String SaveOrUpdateTKDJ(String TKDJJson);

	/**
	 * 删除退库单
	 * @param tkdid
	 * @return
	 */
	public String RemoveTKD(String tkdid);

	/**
	 * 保存或更改退库明细
	 * @param TKDMXJson
	 * @return
	 */
	public String SaveOrUpdateTKDMX(String TKDMXJson,Boolean isSqPage);

	/**
	 *  删除退库明细
	 * @param tkdmxid
	 * @return
	 */
	public String RemoveTKDMX(String tkdmxid);

	
	///报废的方法
	/**
	 * 保存或更改报废登记
	 * @param BFDJJson
	 * @return
	 */
	public String SaveOrUpdateBFDJ(String BFDJJson);
	
	/**
	 * 删除报废单
	 * @param bfdid
	 * @return
	 */
	public String RemoveBFDJ(String bfdid);
	
	/**
	 * 保存或更改报废明细
	 * @param BFDMXJson
	 * @return
	 */
	public String SaveOrUpdateBFDJMX(String BFDMXJson);
	
	/**
	 * 删除报废明细
	 * @param bfdmxid
	 * @return
	 */
	public String RemoveBFDJMX(String bfdmxid);
	
	///维修的方法
	/**
	 * 保存或更改维修登记
	 * @param WXDJJson
	 * @return
	 */
	 public String SaveOrUpdateWXDJ(String WXDJJson);

	/**
	 * 删除维修单
	 * @param wxdid
	 * @return
	 */
	 public String RemoveWXDJ(String wxdid);
		
	/**
	 * 保存或更改维修明细
	 * @param WXDMXJson
	 * @return
	 */
	 public String SaveOrUpdateWXDJMX(String WXDMXJson);
		
	/**
	 * 删除维修明细
	 * @param wxdmxid
	 * @return
	 */
	 public String RemoveWXDJMX(String wxdmxid);
	 
	 /**
	  * 保存或更改提醒
	  * @param WXDMXJson
	  * @return
	  */
	 public String SaveOrUpdateNotice(String NoticeJson);
	 
	 /**
	  * 删除提醒 (已阅)
	  * @param wxdmxid
	  * @return
	  */
	 public String RemoveNotice(String ntid);
	 /**
		 * 删除用户的所有提醒(全部已阅)
		 * @param wxdmxid
		 * @return
		 */
	public String RemoveUserNotice(String userid);
	
	 /**
	  * 更改流程默认审核人
	  * @param WXDJJson
	  * @return
	  */
	 public String SaveOrUpdateWFStep(String WXDJJson);
	
	
	/**
	 * 获取预定义的流程
	 * @param flowType 流程类型
	 * @return 返回有序的流程Vo List
	 */
	public List<?> getFlowDefine(String flowType);
	
	/**
	 * 转交下一步
	 * @param fwLsjlJson历史记录VO
	 * @param fwJson 流程中步骤 、状态 、下一步操作人 等数据
	 * @return 操作成功的提示信息
	 */
	public String passToNextSetp(String fwLsjlJson,String fwJson);
	
	/**
	 * //查找数据总量
	 * @param strSQL
	 * @return
	 */
	public int GetDataCount(String strSQL);
	
	/**
	 * //根据ID获取流程Obj
	 * @param wfType
	 * @param wfid
	 * @return
	 */
	public Object getWFObjectById(String wfType,String wfid);
	
	/**
	 * //根据ID获取耗材Obj
	 * @param ryhcid
	 * @return
	 */
	public Object getRyhcObjectById(String ryhcid);
	
	/**
	 * 获取当前单号历史记录 
	 * @param wfdId报废单/领用单ID
	 * @return 当前单号的历史记录JSONArray
	 */
	public String getWFHistory(String wfdId);
	/**
	 * //获取用户已审核的记录ID集合
	 * @param userId  用户ID
	 * @param WFType 流程类型
	 * @return
	 */
	public String getWFShHistory(String userId,String WFType);
	
	/**
	 * 验证编号WFBH是否重复
	 * @param wfbh 编号
	 * @param idColumn 表名
	 * @param tableName 表名
	 * @param dataId id
	 * @return true 重复
	 */
	public boolean validateWFBH(String wfbh,String idColumn,String bhColumn,String tableName,String dataId);
	
}
