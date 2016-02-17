package net.chinanets.service;

/**
 * 工作流数据Service接口
 * @author RLiuyong
 *
 */
public interface WFHandleService extends CommonService {
	
	//查询所有流程步骤数据
	public String GetWFStepByPaging(String tableName,int tempPageSize,int tempPageCurrent,String strcondition);
	
	//保存或修改工作流步骤
	public String SaveOrUpdateWFStep(String WFStepJson);
	
	//删除流程步骤
	public String RemoveWFStep(String wfstepId);
	
	//保存或修改工作流历史操作
	public String SaveOrUpdateWFHistory(String wfHistoryJson);
	
	//删除流程步骤
	public String RemoveWFHistory(String wfHistoryId);
}
