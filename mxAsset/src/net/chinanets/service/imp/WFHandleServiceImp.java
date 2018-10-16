package net.chinanets.service.imp;

import net.chinanets.dao.WFHandleDao;
import net.chinanets.entity.CnstWfhistoryData;
import net.chinanets.entity.CnstWfstepData;
import net.chinanets.service.WFHandleService;
import net.chinanets.utils.common.DoResult;
import net.chinanets.utils.common.Errors;
import net.chinanets.utils.helper.CommonHelper;
import net.chinanets.utils.helper.DateHelper;
import net.chinanets.utils.helper.JsonHelper;
import net.chinanets.utils.helper.StringHelper;

/**
 * 工作流service接口实现类
 * @author dzj
 *
 */
public class WFHandleServiceImp extends CommonServiceImp implements WFHandleService {
	// 工作流接口
	private WFHandleDao wfHandleDao;
	
	//查询所有流程步骤数据
	public String GetWFStepByPaging(String tableName,int tempPageSize,int tempPageCurrent,String strcondition){
		return this.wfHandleDao.GetWFStepByPaging(tableName, tempPageSize, tempPageCurrent, strcondition);
	}
	
	//保存或修改工作流步骤
	public String SaveOrUpdateWFStep(String WFStepJson) {
		DoResult doResult = new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstWfstepData wfStepData=(CnstWfstepData)JsonHelper.GetBeanByJsonString(WFStepJson, CnstWfstepData.class);
		if (wfStepData == null) {
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid = wfStepData.getWfstepid();
		if (StringHelper.IsNullOrEmpty(dataid)) {
			String strGUID = CommonHelper.GenGuidEx();
			wfStepData.setWfstepid(strGUID);
			wfStepData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			if (!this.wfHandleDao.SavaOrUpdateData(wfStepData, true)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建流程步骤失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		} else {
			CnstWfstepData tempWFStepData=(CnstWfstepData)this.wfHandleDao.GetDataById(CnstWfstepData.class, dataid);
			JsonHelper.GetBeanByJsonString(WFStepJson, tempWFStepData);
			tempWFStepData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if (!this.wfHandleDao.SavaOrUpdateData(tempWFStepData, false)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新流程步骤失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempWFStepData.getWfstepid());
		}
		return doResult.GetJSONByDoResult();
	}

	//删除流程步骤
	public String RemoveWFStep(String wfstepId) {
		DoResult doResult = new DoResult();
		doResult.setRetCode(Errors.OK);
		if (StringHelper.IsNullOrEmpty(wfstepId)) {
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid = StringHelper.ConvertStrToDBStr(wfstepId);
		//删除流程步骤
		if (!this.wfHandleDao.RemoveData("CNST_WFSTEP_DATA", "WFSTEPID", tempid)) {
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除流程步骤失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}	
	
	//保存或修改工作流历史操作
	public String SaveOrUpdateWFHistory(String wfHistoryJson) {
		DoResult doResult = new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstWfhistoryData wfHistory=(CnstWfhistoryData)JsonHelper.GetBeanByJsonString(wfHistoryJson, CnstWfhistoryData.class);
		if (wfHistory == null) {
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid = wfHistory.getWfhistoryid();
		if (StringHelper.IsNullOrEmpty(dataid)) {
			String strGUID = CommonHelper.GenGuidEx();
			wfHistory.setWfhistoryid(strGUID);
			wfHistory.setUsable(1);
			wfHistory.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			wfHistory.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if (!this.wfHandleDao.SavaOrUpdateData(wfHistory, true)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建工作流操作历史数据失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		} else {
			CnstWfhistoryData tempWFHistoryData=(CnstWfhistoryData)this.wfHandleDao.GetDataById(CnstWfhistoryData.class, dataid);
			JsonHelper.GetBeanByJsonString(wfHistoryJson, tempWFHistoryData);
			tempWFHistoryData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if (!this.wfHandleDao.SavaOrUpdateData(tempWFHistoryData, false)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新工作流操作历史数据失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempWFHistoryData.getWfhistoryid());
		}
		return doResult.GetJSONByDoResult();
	}	
	
	//删除流程步骤
	public String RemoveWFHistory(String wfHistoryId) {
		DoResult doResult = new DoResult();
		doResult.setRetCode(Errors.OK);
		if (StringHelper.IsNullOrEmpty(wfHistoryId)) {
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid = StringHelper.ConvertStrToDBStr(wfHistoryId);
		//删除流程步骤
		if (!this.wfHandleDao.RemoveData("CNST_WFHISTORY_DATA", "WFHISTORYID", tempid)) {
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除流程操作历史数据失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}	
	
	public WFHandleDao getWfHandleDao() {
		return wfHandleDao;
	}

	public void setWfHandleDao(WFHandleDao wfHandleDao) {
		this.wfHandleDao = wfHandleDao;
	}
}
