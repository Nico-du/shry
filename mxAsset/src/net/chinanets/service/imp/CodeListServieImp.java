package net.chinanets.service.imp;

import java.util.List;

import net.chinanets.dao.CodeListDao;
import net.chinanets.entity.CnstCodelistData;
import net.chinanets.service.CodeListService;
import net.chinanets.utils.common.DoResult;
import net.chinanets.utils.common.Errors;
import net.chinanets.utils.helper.CommonHelper;
import net.chinanets.utils.helper.JsonHelper;
import net.chinanets.utils.helper.StringHelper;
import net.sf.json.JSONArray;

/**
 * 代码表Service实现类
 * @author Liuyong
 *
 */
public class CodeListServieImp extends CommonServiceImp implements CodeListService {

	//代码表DAO层
	private CodeListDao codeListDao;

	//分页查询所有代码表
	public String GetCodeList(String tempObjBeanName,int tempPageSize,int tempPageCurrent,String strcondition){
		return codeListDao.GetCodeListByPaging(tempObjBeanName,tempPageSize, tempPageCurrent, strcondition);
	}
	
	//根据类型获取代码表
	public String GetCodeListByType(String typeName){
		JSONArray tempJsonArray=this.codeListDao.GetDataByCodeType(typeName);
		if(tempJsonArray!=null){
			return tempJsonArray.toString();
		}
		return "";
	}
	
	//保存或更改代码表数据
	public String SaveOrUpdateCodeList(String codeListJson){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstCodelistData codelistdata=(CnstCodelistData)JsonHelper.GetBeanByJsonString(codeListJson, CnstCodelistData.class);
		if(codelistdata==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid=codelistdata.getCodeid();
		if(StringHelper.IsNullOrEmpty(dataid)){
			String strGUID=CommonHelper.GenGuidEx();
			codelistdata.setCodeid(strGUID);
			if(!this.codeListDao.SavaOrUpdateData(codelistdata, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建代码表数据失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		}else{
			CnstCodelistData tempCodeListData=(CnstCodelistData)this.codeListDao.GetDataById(CnstCodelistData.class, dataid);
			JsonHelper.GetBeanByJsonString(codeListJson, tempCodeListData);
			if(!this.codeListDao.SavaOrUpdateData(tempCodeListData, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新代码表数据失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempCodeListData.getCodeid());
		}
		return doResult.GetJSONByDoResult();
	}
	
	//删除代码表
	public String RemoveCodeList(String codeListID){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(codeListID)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(codeListID);
		//检测代码表
		String strCountSql=StringHelper.Format("SELECT COUNT(0) FROM CNST_CODELIST_DATA " +
				"WHERE CODETYPE IN (SELECT CODEBS FROM CNST_CODELIST_DATA WHERE CODEID in (%1$s)",tempid);
		int count=this.codeListDao.GetDataCount(strCountSql);
		if(count>0){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("当前代码表存在明细记录无法删除,请首先删除明细记录!");
			return doResult.GetJSONByDoResult();
		}
		
		//删除代码表
		if(!this.codeListDao.RemoveData("CNST_CODELIST_DATA","CODEID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除代码表数据失败");
			return doResult.GetJSONByDoResult();
		}
		
		return doResult.GetJSONByDoResult();
	}
	
	public CodeListDao getCodeListDao() {
		return codeListDao;
	}

	public void setCodeListDao(CodeListDao codeListDao) {
		this.codeListDao = codeListDao;
	}
}
