package net.chinanets.service.imp;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.chinanets.dao.AllAssetDao;
import net.chinanets.dao.WFHandleDao;
import net.chinanets.data.DataEntity;
import net.chinanets.entity.CnstBfdData;
import net.chinanets.entity.CnstBfdmxData;
import net.chinanets.entity.CnstCodelistData;
import net.chinanets.entity.CnstLydData;
import net.chinanets.entity.CnstLydmxData;
import net.chinanets.entity.CnstNotice;
import net.chinanets.entity.CnstRkdData;
import net.chinanets.entity.CnstRyhcData;
import net.chinanets.entity.CnstRyhcmxData;
import net.chinanets.entity.CnstTkdData;
import net.chinanets.entity.CnstTkdmxData;
import net.chinanets.entity.CnstWfhistoryData;
import net.chinanets.entity.CnstWfstepData;
import net.chinanets.entity.CnstWxdData;
import net.chinanets.entity.CnstWxdmxData;
import net.chinanets.pojos.CnstAssetbgjlData;
import net.chinanets.pojos.Rules;
import net.chinanets.pojos.Users;
import net.chinanets.service.AllAssetService;
import net.chinanets.service.UserService;
import net.chinanets.utils.CommonMethods;
import net.chinanets.utils.common.DoResult;
import net.chinanets.utils.common.Errors;
import net.chinanets.utils.helper.CommonHelper;
import net.chinanets.utils.helper.DateHelper;
import net.chinanets.utils.helper.JsonHelper;
import net.chinanets.utils.helper.StringHelper;
import net.chinanets.utils.helper.WFHelper;
import net.chinanets.vo.UserVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

/**
 * 所有物资接口实现类
 * @author Liuyong
 *
 */
public class AllAssetServiceImp extends CommonServiceImp implements AllAssetService,ApplicationContextAware {
	
	//所有物资DAO层
	private AllAssetDao allAssetDao;
	
	//工作流接口
	private WFHandleDao wfHandleDao;
	
	//Spring上下文
	private ApplicationContext apc;
	
	//分页查询所有物资
	public String GetAllAsset(String tempObjBeanName,int tempPageSize,int tempPageCurrent,String strcondition){
		return allAssetDao.GetAllAssetListByPaging(tempObjBeanName,tempPageSize, tempPageCurrent, strcondition);
	}
	//查找数据总量
	public int GetDataCount(String strSQL){
		return allAssetDao.GetDataCount(strSQL);
	}
	//根据ID获取流程Obj
	public Object getWFObjectById(String wfType,String wfid){
		if("领用".equals(wfType))return allAssetDao.GetDataById(CnstLydData.class, wfid);
		else if("报废".equals(wfType))return allAssetDao.GetDataById(CnstBfdData.class, wfid);
		else if("退库".equals(wfType))return allAssetDao.GetDataById(CnstTkdData.class, wfid);
		else return null;
	}
	//根据ID获取耗材Obj
	public Object getRyhcObjectById(String ryhcid){
		return allAssetDao.GetDataById(CnstRyhcData.class, ryhcid);
	}
	
	//保存或更改Object
	public String SaveOrUpdateObject(String ObjectJson,String classPath,String idColumn,String objName){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.INTERNALERROR);
		try {
			Class clazz = Class.forName(classPath);
			if(clazz == null){return "";}
			Object objData= JsonHelper.GetBeanByJsonString(ObjectJson, clazz);
			if(objData==null){
				doResult.setRetCode(Errors.INVALIDDATA);
				doResult.setErrorInfo("请求数据无效");
				return doResult.GetJSONByDoResult();
			}
			String dataid = "";
			Field fields[] = clazz.getDeclaredFields();
			Field idField,ctimeField,utimeField;
			idField = ctimeField = utimeField = null;

			for(Field field : fields){
				field.setAccessible(true);
				String filedName = field.getName();
				if(idColumn.toLowerCase().equals(filedName.toLowerCase())){
					Object objValue = field.get(objData);
					dataid = ( objValue == null  || ( objValue.getClass() ==  Long.class  && (Long)objValue == 0L )  ? null : objValue.toString());
					idField = field;
				}
				if("inputdate".equals(field.getName().toLowerCase())){
					ctimeField = field;
				}
				if("updatedate".equals(field.getName().toLowerCase())){
					utimeField = field;
				}
				
			}
			if(idField == null /*|| ctimeField==null || utimeField == null*/){return "";}
			
			if(StringHelper.IsNullOrEmpty(dataid)){
				doResult.setErrorInfo("创建"+objName+"失败");
				if(idField.getType() == String.class){
					String strGUID=CommonHelper.GenGuidEx();
					idField.set(objData,strGUID);
				}
				if(ctimeField != null){ctimeField.set(objData, DateHelper.GetCurrentUtilDateTimeClass());}
				if(utimeField != null){utimeField.set(objData, DateHelper.GetCurrentUtilDateTimeClass());}
				if(!this.allAssetDao.SavaOrUpdateData(objData, true)){
					doResult.setRetCode(Errors.INTERNALERROR);
					return doResult.GetJSONByDoResult();
				}
				doResult.setRetCode(Errors.OK);
				doResult.setErrorInfo(idField.get(objData).toString());
			}else{
				doResult.setErrorInfo("更新"+objName+"失败");
				Object tempObjData=this.allAssetDao.GetDataById(clazz, (Serializable)idField.get(objData) );
				if(tempObjData == null){doResult.setErrorInfo("未找到主键相关数据"); return doResult.GetJSONByDoResult(); }
				JsonHelper.GetBeanByJsonString(ObjectJson, tempObjData);
				if(utimeField != null){utimeField.set(tempObjData, DateHelper.GetCurrentUtilDateTimeClass());}
				if(!this.allAssetDao.SavaOrUpdateData(tempObjData, false)){
					return doResult.GetJSONByDoResult();
				}
				doResult.setRetCode(Errors.OK);
				doResult.setErrorInfo(idField.get(tempObjData).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doResult.GetJSONByDoResult();
	}
		
	//删除Object
	public String RemoveObject(String dataid,String tableName,String idColumn,String objName){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.INTERNALERROR);
		if(StringHelper.IsNullOrEmpty(dataid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(dataid);
		
		//删除Object
		if(!this.allAssetDao.RemoveData(tableName,idColumn,tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除"+objName+"数据失败");
			return doResult.GetJSONByDoResult();
		}
		doResult.setRetCode(Errors.OK);
		return doResult.GetJSONByDoResult();
	}
	
	
	
	
	
	//保存或更改日用耗材
	public String SaveOrUpdateRYHC(String RYHCJson,String strMoudel){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstRyhcData ryhcData=(CnstRyhcData)JsonHelper.GetBeanByJsonString(RYHCJson, CnstRyhcData.class);
		if(ryhcData==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid="";
		if(StringHelper.Compare(strMoudel, "DBMSAVE", true)==0){
			String assetType=ryhcData.getRyhcbh();
			String strSQL=StringHelper.Format("SELECT RYHCID FROM CNST_RYHC_DATA WHERE RYHCBH='%1$s'",assetType);
			List<DataEntity> dataList=this.allAssetDao.GetDataEntityBySQL(strSQL);
			if(dataList!=null && dataList.size()>0){
				dataid=dataList.get(0).GetStringValue("RYHCID", "");
			}
		}else if(StringHelper.Compare(strMoudel, "CURSSESAVE", true)==0){
			dataid=ryhcData.getRyhcid();
		}
		if(StringHelper.IsNullOrEmpty(dataid)){
			String strGUID=CommonHelper.GenGuidEx();
			ryhcData.setRyhcid(strGUID);
			ryhcData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(ryhcData, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建日用耗材信息失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		}else{
			CnstRyhcData tempRyhcData=(CnstRyhcData)this.allAssetDao.GetDataById(CnstRyhcData.class, dataid);
			JsonHelper.GetBeanByJsonString(RYHCJson, tempRyhcData);
			tempRyhcData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(tempRyhcData, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新日用耗材信息失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempRyhcData.getRyhcid());
		}
		return doResult.GetJSONByDoResult();
	}	
	
	//删除日用耗材
	public String RemoveRYHC(String ryhcid){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(ryhcid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(ryhcid);
		//检测日用耗材
		String strCountSql=StringHelper.Format("SELECT COUNT(0) FROM CNST_RYHCMX_DATA WHERE RYHCMXBH IN " +
				"(SELECT RYHCBH FROM CNST_RYHC_DATA WHERE RYHCID IN (%1$s))",tempid);
		int count=this.allAssetDao.GetDataCount(strCountSql);
		if(count>0){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("当前日用耗材存在明细记录无法删除,请首先删除明细记录!");
			return doResult.GetJSONByDoResult();
		}
		//删除日用耗材
		if(!this.allAssetDao.RemoveData("CNST_RYHC_DATA","RYHCID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除日用耗材数据失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}	
	
	//保存或更改入库登记
	public String SaveOrUpdateRKDJ(String RKDJJson){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstRkdData rkdData=(CnstRkdData)JsonHelper.GetBeanByJsonString(RKDJJson, CnstRkdData.class);
		if(rkdData==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid=rkdData.getRkdid();
		if(StringHelper.IsNullOrEmpty(dataid)){
			String strGUID=CommonHelper.GenGuidEx();
			rkdData.setRkdid(strGUID);
			rkdData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(rkdData, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建入库单失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		}else{
			CnstRkdData tempRkdData=(CnstRkdData)this.allAssetDao.GetDataById(CnstRkdData.class, dataid);
			JsonHelper.GetBeanByJsonString(RKDJJson, tempRkdData);
			tempRkdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(tempRkdData, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新入库单失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempRkdData.getRkdid());
		}
		return doResult.GetJSONByDoResult();
	}
	
	//删除入库单
	public String RemoveRKD(String rkdid){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(rkdid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(rkdid);
		//检测入库单
		int count=this.allAssetDao.GetDataCount("CNST_RYHCMX_DATA", "RKDID",tempid);
		if(count>0){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("当前入库单存在明细记录无法删除,请首先删除明细记录!");
			return doResult.GetJSONByDoResult();
		}
		//删除入库单
		if(!this.allAssetDao.RemoveData("CNST_RKD_DATA","RKDID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除入库单数据失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}
	
	//保存或更改入库明细
	public String SaveOrUpdateRYHCMX(String RYHCMXJson){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstRyhcmxData ryhcmxData=(CnstRyhcmxData)JsonHelper.GetBeanByJsonString(RYHCMXJson, CnstRyhcmxData.class);
		if(ryhcmxData==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid=ryhcmxData.getRyhcmxid();
		if(StringHelper.IsNullOrEmpty(dataid)){
			String strGUID=CommonHelper.GenGuidEx();
			ryhcmxData.setRyhcmxid(strGUID);
			ryhcmxData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			ryhcmxData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(ryhcmxData, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建入库明细失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		}else{
			CnstRyhcmxData tempRyhcData=(CnstRyhcmxData)this.allAssetDao.GetDataById(CnstRyhcmxData.class, dataid);
			JsonHelper.GetBeanByJsonString(RYHCMXJson, tempRyhcData);
			tempRyhcData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(tempRyhcData, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新入库明细失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempRyhcData.getRyhcmxid());
		}
		return doResult.GetJSONByDoResult();
	}
	
	//删除入库明细
	public String RemoveRYHCMX(String ryhcmxid){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(ryhcmxid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(ryhcmxid);
		//删除入库明细
		if(!this.allAssetDao.RemoveData("CNST_RYHCMX_DATA","RYHCMXID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除入库明细失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}
	
	// 得到申请领用流程XML	
	public String GetSQLYWFXML(String strType, String userId,String ruleId,Boolean isShr) {
		if (StringHelper.IsNullOrEmpty(strType)	|| StringHelper.IsNullOrEmpty(userId) || StringHelper.IsNullOrEmpty(ruleId)){
			return "";
		}
		Document document = DocumentHelper.createDocument();
		Element rootElement = WFHelper.GetWFTreeRootElement();
//		((Element)(((Element)(rootElement.elements().get(0))).elements().get(0))).addAttribute("nextstep", startStep.getNextstep());
		
		if(isShr || "41".equals(ruleId)){
		Element stepElement = DocumentHelper.createElement("node");
		rootElement.add(stepElement);
		stepElement.addAttribute("id", "myhandle");
		stepElement.addAttribute("label", "我的处理");
//		stepElement.addAttribute("color", "FF0000");
		Element tempElement;
		/*for (CnstWfstepData tempStep : stepList) {
			String strWFStepId = tempStep.getWfstepid();
			int nowStepDataCount = GetSHCountByStep(strWFStepId, userId);
			if (nowStepDataCount < 1) {
				continue;
			}
			String strWFStepName = tempStep.getWfstepname();
			strWFStepName += "(" + nowStepDataCount + ")";
			tempElement = DocumentHelper.createElement("node");
			tempElement.addAttribute("id", strWFStepId);
			tempElement.addAttribute("label", strWFStepName);
			tempElement.addAttribute("color", "FF0000");
			tempElement.addAttribute("wftype", "step");
			tempElement.addAttribute("nextstep", tempStep.getNextstep());
			stepElement.add(tempElement);
			stepCount++;
		}*/
		//改为只显示一个待办理
		int nowStepDataCount;
		String strSQL;
		if("41".equals(ruleId)){
			strSQL = StringHelper.Format("SELECT COUNT(0) FROM CNST_LYD_DATA WHERE LYZT NOT IN('初稿','已结束') ");
		}else{
			strSQL = StringHelper.Format("SELECT COUNT(0) FROM CNST_LYD_DATA WHERE  LYZT NOT IN('初稿','已结束') AND BMLDID=%1$s",
					 Integer.parseInt(userId));
		}
		nowStepDataCount = this.allAssetDao.GetDataCount(strSQL);
		 
		String strWFStepName = "待办理";
		strWFStepName += "(" + nowStepDataCount + ")";
		tempElement = DocumentHelper.createElement("node");
		tempElement.addAttribute("id", "DBL");
		tempElement.addAttribute("label", strWFStepName);
		if(nowStepDataCount > 0){tempElement.addAttribute("color", "FF0000");}
		tempElement.addAttribute("wftype", "step");
//		tempElement.addAttribute("nextstep", tempStep.getNextstep());
		stepElement.add(tempElement);
		
		tempElement = DocumentHelper.createElement("node");
		tempElement.addAttribute("id", "YBL");
		tempElement.addAttribute("label", "已办理");
		tempElement.addAttribute("wftype", "step");
//		tempElement.addAttribute("nextstep", tempStep.getNextstep());
		stepElement.add(tempElement);
		}
		
		document.add(rootElement);
		return document.asXML();
	}


	// 保存或更改领用单
	public String SaveOrUpdateLYD(String LYDJson) {
		DoResult doResult = new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstLydData lydData = (CnstLydData) JsonHelper.GetBeanByJsonString(
				LYDJson, CnstLydData.class);
		if (lydData == null) {
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		
		
		//领用结束时做验证
		//固定资产 验证 是否已选择发放物品
		//耗材验证库存数量是否充足  : 1.获取所有类型2.获取所有类型的数量3.每个类型做数量验证
		ArrayList<String> lyhcType = new ArrayList<String>();
		ArrayList<CnstLydmxData> lydmxData = new ArrayList<CnstLydmxData>();
		if(lydData.getLyzt() != null && lydData.getLyzt().contains("结束")){
		
		CnstLydmxData mxdata = new CnstLydmxData();
		mxdata.setUtime(null);
		mxdata.setUsable(1);
		List<?> lydmxList = allAssetDao.getObjectByExample(mxdata," lydid ='"+lydData.getLydid()+"' ");
		
		
		for(Object lydmxdt:lydmxList){
			if(!lyhcType.contains(((CnstLydmxData)lydmxdt).getLydmxname())){
				if(((CnstLydmxData)lydmxdt).getLydmxtype().contains("日用耗材")){
				lyhcType.add(((CnstLydmxData)lydmxdt).getGbdm());
		     	}
	     		lydmxData.add((CnstLydmxData)lydmxdt);
			}
		}
		//验证固定资产已选择数量
		for(CnstLydmxData eachlydmx:lydmxData){
			int sfsl = (int) eachlydmx.getSfsl();
			int yxzsl = 0;
			if(eachlydmx.getLydmxtype().contains("日用耗材")){
				List rstList = allAssetDao.getObjectBySql("select sum(SQSL) from cnst_lydmx_lysl_data  where sqdmxid='"+eachlydmx.getLydmxid()+"' and gbdm='"+eachlydmx.getGbdm()+"'");
				if(rstList != null && !rstList.isEmpty() && rstList.get(0) != null){
					yxzsl = Integer.parseInt(rstList.get(0).toString());
				}
			}else{
				yxzsl = allAssetDao.getCountByExample(new CnstAssetbgjlData(), " SQDID= '"+eachlydmx.getLydmxid()+"'");
			}
			if(sfsl != yxzsl){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo(eachlydmx.getLydmxname()+"已分配数量 "+yxzsl+" 必须等于领用实发数量 "+sfsl+" !");
			return doResult.GetJSONByDoResult();
			}
		}
		
		//TODO 一个批次内的耗材数量不够,需要选择多个批次
		//验证耗材数量 start 
		//验证耗材已选择数量
		
		
		float count0;
		ArrayList<Float> lyhcSl = new ArrayList<Float>();
		for(String lytype:lyhcType){
	    count0 = 0;
			for(Object lydmxdt:lydmxList){
				if(lytype.equals(((CnstLydmxData)lydmxdt).getGbdm())){
					count0 += ((CnstLydmxData)lydmxdt).getSfsl();
				}
			}
		lyhcSl.add(count0);
		}
		
		for(int i=0;i<lyhcType.size();i++){
			String hctype = lyhcType.get(i);
			Float hcsl = lyhcSl.get(i);
			String strSLSQL=StringHelper.Format("SELECT COUNT(0) FROM CNSV_RYHC WHERE RYHCBH='%1$s' AND SYSL>=%2$s",hctype,hcsl);
			int numcount=this.allAssetDao.GetDataCount(strSLSQL);
			if(numcount<1){
				String hctypeStr = "";	
				doResult.setRetCode(Errors.INVALIDDATA);
				List cclList = allAssetDao.getObjectByExample(new CnstCodelistData()," CODETYPE='RYHC' AND CODEBS='"+hctype+"'");
				if(!cclList.isEmpty()){
					hctypeStr = "'"+((CnstCodelistData)cclList.get(0)).getCodename()+"'的";
				}
				doResult.setErrorInfo(hctypeStr+"领用实发数量之和'"+hcsl.toString().substring(0,hcsl.toString().length()-2)+"'不能超过库存数量!");
				return doResult.GetJSONByDoResult();
			}
		} 
		//验证耗材数量 end
		
		}
		
		
		String dataid = lydData.getLydid();
		if (StringHelper.IsNullOrEmpty(dataid)) {
			String strGUID = CommonHelper.GenGuidEx();
			lydData.setLydid(strGUID);
			lydData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			lydData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			lydData.setUsable(1);
			if (!this.allAssetDao.SavaOrUpdateData(lydData, true)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建领用单失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		} else {
			CnstLydData tempLydData=(CnstLydData)this.allAssetDao.GetDataById(CnstLydData.class, dataid);
			JsonHelper.GetBeanByJsonString(LYDJson, tempLydData);
			tempLydData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if (!this.allAssetDao.SavaOrUpdateData(tempLydData, false)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新领用单失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempLydData.getLydid());
			
			lydData.setCuser(tempLydData.getCuser());
			lydData.setBmldid(tempLydData.getBmldid());
		}
		
			//通知推送  任务提醒
			//保存成功后添加提醒
		    if(lydData.getLyzt() != null && !lydData.getLyzt().contains("初稿")){
			List<Map<String,String>> tzrList = new ArrayList<Map<String,String>>();
			Map<String,String> tznrMap = new HashMap<String, String>();
			tznrMap.put("cuserid", lydData.getCuser());
			tznrMap.put("uuserid", lydData.getUuser());
			tznrMap.put("wftype", "LYSQ");
			if(!lydData.getLyzt().contains("结束")){tznrMap.put("nextstepuserid",lydData.getBmldid()+"");} 
			tznrMap.put("wfid", lydData.getLydid());
			tznrMap.put("wfzt", lydData.getLyzt());
			tzrList.add(tznrMap);
			
			
			///耗材数量不足提醒
			if(lydData.getLyzt().contains("结束") && !lyhcType.isEmpty()){
			String lyhcTypeCtn = "(";
			for(String lytype:lyhcType){
				lyhcTypeCtn +=  "'"+lytype+"'";
			}
			lyhcTypeCtn += ")";
			lyhcTypeCtn = lyhcTypeCtn.replace("''", "','");
			
			List<Object[]> list = this.allAssetDao.getObjectBySql(" select * from cnsv_ryhc where usable=1 and sysl<="+CommonMethods.MinRyhcsl+" and zsl>0 and ryhcbh in "+lyhcTypeCtn+" ");
			if(!list.isEmpty()){
				Map<String,String> ryhcMap ;
				List<Rules> rulelist = super.getObjectList(new Rules(), " mc='物资管理员'  or mc='超级管理员' ");
				if(!rulelist.isEmpty()){
					for(Rules rulec:rulelist){
						List<Users> userList = rulec.getUserses();
						for(Users curUser:userList){
							ryhcMap= new HashMap<String, String>();
							ryhcMap.put("cuserid", curUser.getId().longValue()+"");
							ryhcMap.put("uuserid", lydData.getUuser());
							ryhcMap.put("wftype", "RYHC");
							ryhcMap.put("lyhcTypeCtn", lyhcTypeCtn);
							tzrList.add(ryhcMap);
						}
					}
				}
			}
			}
			pushSysMsg(tzrList);
		    }
		return doResult.GetJSONByDoResult();
	}

	// 删除领用单
	public String RemoveLYD(String lydid) {
		DoResult doResult = new DoResult();
		doResult.setRetCode(Errors.OK);
		if (StringHelper.IsNullOrEmpty(lydid)) {
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid = StringHelper.ConvertStrToDBStr(lydid);
		// 检测领用单
		int count = this.allAssetDao.GetDataCount("CNST_LYDMX_DATA", "LYDID",tempid);
		if (count > 0) {
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("当前领用单存在明细记录无法删除,请首先删除明细记录!");
			return doResult.GetJSONByDoResult();
		}
		// 删除领用单
		if (!this.allAssetDao.RemoveData("CNST_LYD_DATA", "LYDID", tempid)) {
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除领用单数据失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}
	
	// 保存或更改领用明细
	public String SaveOrUpdateLYDMX(String LYDMXJson) {
		DoResult doResult = new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstLydmxData lydmxData=(CnstLydmxData)JsonHelper.GetBeanByJsonString(LYDMXJson, CnstLydmxData.class);
		if (lydmxData == null) {
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		
		//耗材领用数量验证  1.领用单内 所有同lydmxtype的申请明细的耗材申请数量不大于剩余数量
		//保存或修改都做验证
		float lyzsl = lydmxData.getSfsl();
		String condition = " select lymx.Sfsl from CNST_LYDMX_DATA lymx where lydid ='"+lydmxData.getLydid()+"' and lydmxtype ='"+lydmxData.getLydmxtype()+"'";
		if (!StringHelper.IsNullOrEmpty(lydmxData.getLydmxid())) {
			condition += " and lydmxid <> '" +lydmxData.getLydmxid()+"'";
		} 
		List<?> lydmxList = allAssetDao.getObjectBySql(condition);
		for(Object lydmxdt:lydmxList){
			lyzsl += Float.parseFloat(lydmxdt.toString());
		}
		//领用数量 在选择批次时 验证
		/*String strSLSQL=StringHelper.Format("SELECT COUNT(0) FROM CNSV_RYHC WHERE RYHCBH='%1$s' AND SYSL>=%2$s",lydmxData.getLydmxtype(),lyzsl);
		int numcount=this.allAssetDao.GetDataCount(strSLSQL);
		if(numcount<1){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("领用单明细实发数量之和不能大于库存数量");
			return doResult.GetJSONByDoResult();
		}*/
		
		String dataid = lydmxData.getLydmxid();
		if (StringHelper.IsNullOrEmpty(dataid)) {
			String strGUID = CommonHelper.GenGuidEx();
			lydmxData.setLydmxid(strGUID);
			lydmxData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			lydmxData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			lydmxData.setUsable(1);
			if (!this.allAssetDao.SavaOrUpdateData(lydmxData, true)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建领用明细失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		} else {
			CnstLydmxData tempLydmxData=(CnstLydmxData)this.allAssetDao.GetDataById(CnstLydmxData.class, dataid);
			JsonHelper.GetBeanByJsonString(LYDMXJson, tempLydmxData);
			tempLydmxData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if (!this.allAssetDao.SavaOrUpdateData(tempLydmxData, false)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新领用明细失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempLydmxData.getLydmxid());
		}
		return doResult.GetJSONByDoResult();
	}

	// 删除领用明细
	public String RemoveLYDMX(String lydmxid) {
		DoResult doResult = new DoResult();
		doResult.setRetCode(Errors.OK);
		if (StringHelper.IsNullOrEmpty(lydmxid)) {
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid = StringHelper.ConvertStrToDBStr(lydmxid);
		// 删除领用明细
		if (!this.allAssetDao.RemoveData("CNST_LYDMX_DATA", "LYDMXID", tempid)) {
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除领用明细失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}	
	
	///退库的CRUD
	
		//保存或更改退库登记
			public String SaveOrUpdateTKDJ(String TKDJJson){
				DoResult doResult=new DoResult();
				doResult.setRetCode(Errors.OK);
				CnstTkdData tkdData=(CnstTkdData)JsonHelper.GetBeanByJsonString(TKDJJson, CnstTkdData.class);
				if(tkdData==null){
					doResult.setRetCode(Errors.INVALIDDATA);
					doResult.setErrorInfo("请求数据无效");
					return doResult.GetJSONByDoResult();
				} 
				String dataid=tkdData.getTkdid();
				if(StringHelper.IsNullOrEmpty(dataid)){
					String strGUID=CommonHelper.GenGuidEx();
					tkdData.setTkdid(strGUID);
					tkdData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
					tkdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
					tkdData.setUsable(1);
					if(!this.allAssetDao.SavaOrUpdateData(tkdData, true)){
						doResult.setRetCode(Errors.INTERNALERROR);
						doResult.setErrorInfo("创建退库单失败");
						return doResult.GetJSONByDoResult();
					}
					doResult.setErrorInfo(strGUID);
				}else{
					CnstTkdData tempRkdData=(CnstTkdData)this.allAssetDao.GetDataById(CnstTkdData.class, dataid);
					JsonHelper.GetBeanByJsonString(TKDJJson, tempRkdData);
					tempRkdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
					if(!this.allAssetDao.SavaOrUpdateData(tempRkdData, false)){
						doResult.setRetCode(Errors.INTERNALERROR);
						doResult.setErrorInfo("更新退库单失败");
						return doResult.GetJSONByDoResult();
					}
					doResult.setErrorInfo(tempRkdData.getTkdid());
				}
				return doResult.GetJSONByDoResult();
			}
			
			//删除退库单
			public String RemoveTKD(String tkdid){
				DoResult doResult=new DoResult();
				doResult.setRetCode(Errors.OK);
				if(StringHelper.IsNullOrEmpty(tkdid)){
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("无法删除空数据");
					return doResult.GetJSONByDoResult();
				}
				String tempid=StringHelper.ConvertStrToDBStr(tkdid);
				//检测退库单
				int count=this.allAssetDao.GetDataCount("CNST_RYHCMX_DATA", "TKDID",tempid);
				if(count>0){
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("当前退库单存在明细记录无法删除,请首先删除明细记录!");
					return doResult.GetJSONByDoResult();
				}
				//删除退库单
				if(!this.allAssetDao.RemoveData("CNST_TKD_DATA","TKDID",tempid)){
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("删除退库单数据失败");
					return doResult.GetJSONByDoResult();
				}
				return doResult.GetJSONByDoResult();
			}
		// 保存或更改退库明细
		public String SaveOrUpdateTKDMX(String TKDMXJson,Boolean isSqPage) {
			DoResult doResult = new DoResult();
			doResult.setRetCode(Errors.OK);
			CnstTkdmxData tkdmxData=(CnstTkdmxData)JsonHelper.GetBeanByJsonString(TKDMXJson, CnstTkdmxData.class);
			if (tkdmxData == null) {
				doResult.setRetCode(Errors.INVALIDDATA);
				doResult.setErrorInfo("请求数据无效");
				return doResult.GetJSONByDoResult();
			}
			if(isSqPage){
			//耗材退库数量验证  1.退库单内 所有同lydmxtype的申请明细的耗材实际数量不大于剩余数量
			//保存或修改都做验证
			float tkzsl = tkdmxData.getTksl();
			String condition = " select lymx.Tksl from CNST_TKDMX_DATA lymx where tkdid ='"+tkdmxData.getTkdid()+"' and tkdmxtype ='"+tkdmxData.getTkdmxtype()+"'";
			if (!StringHelper.IsNullOrEmpty(tkdmxData.getTkdmxid())) {
				condition += " and tkdmxid <> '" +tkdmxData.getTkdmxid()+"'";
			} 
			List<?> tkdmxList = allAssetDao.getObjectBySql(condition);
			for(Object lydmxdt:tkdmxList){
				tkzsl += Float.parseFloat(lydmxdt.toString());
			}
			String strSLSQL=StringHelper.Format("SELECT COUNT(0) FROM CNSV_RYHC WHERE RYHCBH='%1$s' AND YYSL>=%2$s",tkdmxData.getGbdm(),tkzsl);
			int numcount=this.allAssetDao.GetDataCount(strSLSQL);
			if(numcount<1){
				doResult.setRetCode(Errors.INVALIDDATA);
				doResult.setErrorInfo("退库单明细申请数量之和不能大于已领用总数量");
				return doResult.GetJSONByDoResult();
			}
			}
			
			String dataid = tkdmxData.getTkdmxid();
			if (StringHelper.IsNullOrEmpty(dataid)) {
				String strGUID = CommonHelper.GenGuidEx();
				tkdmxData.setTkdmxid(strGUID);
				tkdmxData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
				tkdmxData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
				tkdmxData.setUsable(1);
				if (!this.allAssetDao.SavaOrUpdateData(tkdmxData, true)) {
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("创建领用明细失败");
					return doResult.GetJSONByDoResult();
				}
				doResult.setErrorInfo(strGUID);
			} else {
				CnstTkdmxData tempTkdmxData=(CnstTkdmxData)this.allAssetDao.GetDataById(CnstTkdmxData.class, dataid);
				JsonHelper.GetBeanByJsonString(TKDMXJson, tempTkdmxData);
				tempTkdmxData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
				if (!this.allAssetDao.SavaOrUpdateData(tempTkdmxData, false)) {
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("更新领用明细失败");
					return doResult.GetJSONByDoResult();
				}
				doResult.setErrorInfo(tempTkdmxData.getTkdmxid());
			}
			return doResult.GetJSONByDoResult();
		}

		// 删除退库明细
		public String RemoveTKDMX(String tkdmxid) {
			DoResult doResult = new DoResult();
			doResult.setRetCode(Errors.OK);
			if (StringHelper.IsNullOrEmpty(tkdmxid)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("无法删除空数据");
				return doResult.GetJSONByDoResult();
			}
			String tempid = StringHelper.ConvertStrToDBStr(tkdmxid);
			// 删除退库明细
			if (!this.allAssetDao.RemoveData("CNST_TKDMX_DATA", "TKDMXID", tempid)) {
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("删除退库明细失败");
				return doResult.GetJSONByDoResult();
			}
			return doResult.GetJSONByDoResult();
		}
	
	///报废的CRUD
	
	//保存或更改报废登记
	public String SaveOrUpdateBFDJ(String BFDJJson){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstBfdData bfdData=(CnstBfdData)JsonHelper.GetBeanByJsonString(BFDJJson, CnstBfdData.class);
		if(bfdData==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid=bfdData.getBfdid();
		if(StringHelper.IsNullOrEmpty(dataid)){
			String strGUID=CommonHelper.GenGuidEx();
			bfdData.setBfdid(strGUID);
			bfdData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			bfdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(bfdData, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建报废单失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		}else{
			CnstBfdData tempBfdData=(CnstBfdData)this.allAssetDao.GetDataById(CnstBfdData.class, dataid);
			JsonHelper.GetBeanByJsonString(BFDJJson, tempBfdData);
			tempBfdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(tempBfdData, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新报废单失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempBfdData.getBfdid());
		}
		return doResult.GetJSONByDoResult();
	}

	//删除报废单
	public String RemoveBFDJ(String bfdid){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(bfdid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(bfdid);
		//检测报废单
		int count=this.allAssetDao.GetDataCount("CNST_BFDMX_DATA", "BFDID",tempid);
		if(count>0){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("当前报废单存在明细记录无法删除,请首先删除明细记录!");
			return doResult.GetJSONByDoResult();
		}
		//删除报废单
		if(!this.allAssetDao.RemoveData("CNST_BFD_DATA","BFDID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除报废单数据失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}

	//保存或更改报废明细
	public String SaveOrUpdateBFDJMX(String BFDMXJson){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstBfdmxData bfdmxData=(CnstBfdmxData)JsonHelper.GetBeanByJsonString(BFDMXJson, CnstBfdmxData.class);
		if(bfdmxData==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid=bfdmxData.getBfdmxid();
		if(StringHelper.IsNullOrEmpty(dataid)){
			String strGUID=CommonHelper.GenGuidEx();
			bfdmxData.setBfdmxid(strGUID);
			bfdmxData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			bfdmxData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			
			if(!this.allAssetDao.SavaOrUpdateData(bfdmxData, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建报废明细失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		}else{
			CnstBfdmxData tempBfdData=(CnstBfdmxData)this.allAssetDao.GetDataById(CnstBfdmxData.class, dataid);
			JsonHelper.GetBeanByJsonString(BFDMXJson, tempBfdData);
			tempBfdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(tempBfdData, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新报废明细失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempBfdData.getBfdmxid());
		}
		return doResult.GetJSONByDoResult();
	}

	//删除报废明细
	public String RemoveBFDJMX(String bfdmxid){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(bfdmxid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(bfdmxid);
		//删除报废明细
		if(!this.allAssetDao.RemoveData("CNST_BFDMX_DATA","BFDMXID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除报废明细失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}

	///维修 的CRUD

	//保存或更改维修登记
	public String SaveOrUpdateWXDJ(String WXDJJson){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstWxdData wxdData=(CnstWxdData)JsonHelper.GetBeanByJsonString(WXDJJson, CnstWxdData.class);
		if(wxdData==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid=wxdData.getWxdid();
		if(StringHelper.IsNullOrEmpty(dataid)){
			String strGUID=CommonHelper.GenGuidEx();
			wxdData.setWxdid(strGUID);
			wxdData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			wxdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(wxdData, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建维修单失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		}else{
			CnstWxdData tempWxdData=(CnstWxdData)this.allAssetDao.GetDataById(CnstWxdData.class, dataid);
			JsonHelper.GetBeanByJsonString(WXDJJson, tempWxdData);
			tempWxdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(tempWxdData, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新维修单失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempWxdData.getWxdid());
		}
		return doResult.GetJSONByDoResult();
	}

	//删除维修单
	public String RemoveWXDJ(String wxdid){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(wxdid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(wxdid);
		//检测维修单
		int count=this.allAssetDao.GetDataCount("CNST_WXDMX_DATA", "WXDID",tempid);
		if(count>0){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("当前维修单存在明细记录无法删除,请首先删除明细记录!");
			return doResult.GetJSONByDoResult();
		}
		//删除维修单
		if(!this.allAssetDao.RemoveData("CNST_WXD_DATA","WXDID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除维修单数据失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}

	//保存或更改维修明细
	public String SaveOrUpdateWXDJMX(String WXDMXJson){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstWxdmxData wxdmxData=(CnstWxdmxData)JsonHelper.GetBeanByJsonString(WXDMXJson, CnstWxdmxData.class);
		if(wxdmxData==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid=wxdmxData.getWxdmxid();
		if(StringHelper.IsNullOrEmpty(dataid)){
			String strGUID=CommonHelper.GenGuidEx();
			wxdmxData.setWxdmxid(strGUID);
			wxdmxData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			wxdmxData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(wxdmxData, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建维修明细失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		}else{
			CnstWxdmxData tempWxdData=(CnstWxdmxData)this.allAssetDao.GetDataById(CnstWxdmxData.class, dataid);
			JsonHelper.GetBeanByJsonString(WXDMXJson, tempWxdData);
			tempWxdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(tempWxdData, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新维修明细失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempWxdData.getWxdmxid());
		}
		return doResult.GetJSONByDoResult();
	}

	//删除维修明细
	public String RemoveWXDJMX(String wxdmxid){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(wxdmxid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(wxdmxid);
		//删除维修明细
		if(!this.allAssetDao.RemoveData("CNST_WXDMX_DATA","WXDMXID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除维修明细失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}
	
    //保存或更改提醒
	public String SaveOrUpdateNotice(String NoticeJson){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstNotice noticeData=(CnstNotice)JsonHelper.GetBeanByJsonString(NoticeJson, CnstNotice.class);
		if(noticeData==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		String dataid=noticeData.getNtid();
		if(StringHelper.IsNullOrEmpty(dataid)){
			String strGUID=CommonHelper.GenGuidEx();
			noticeData.setNtid(strGUID);
			noticeData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
			noticeData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(noticeData, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("创建提醒失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(strGUID);
		}else{
			CnstNotice tempWxdData=(CnstNotice)this.allAssetDao.GetDataById(CnstNotice.class, dataid);
			JsonHelper.GetBeanByJsonString(NoticeJson, tempWxdData);
			tempWxdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
			if(!this.allAssetDao.SavaOrUpdateData(tempWxdData, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新提醒失败");
				return doResult.GetJSONByDoResult();
			}
			doResult.setErrorInfo(tempWxdData.getNtid());
		}
		return doResult.GetJSONByDoResult();
	}

	//删除提醒
	public String RemoveNotice(String ntid){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(ntid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		String tempid=StringHelper.ConvertStrToDBStr(ntid);
		//删除提醒
		if(!this.allAssetDao.RemoveData("CNST_NOTICE","NTID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除提醒失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}
	
	//删除用户的所有提醒
	public String RemoveUserNotice(String userid){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		if(StringHelper.IsNullOrEmpty(userid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("无法删除空数据");
			return doResult.GetJSONByDoResult();
		}
		if(userid.endsWith(".0")){userid = userid.substring(0,userid.length()-2);}
		String tempid=StringHelper.ConvertStrToDBStr(userid);
		//删除提醒
		if(!this.allAssetDao.RemoveData("CNST_NOTICE","TZRID",tempid)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除提醒失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}
		
	//更改流程默认审核人
		public String SaveOrUpdateWFStep(String WXDJJson){
			DoResult doResult=new DoResult();
			doResult.setRetCode(Errors.OK);
			CnstWfstepData wfstepData=(CnstWfstepData)JsonHelper.GetBeanByJsonString(WXDJJson, CnstWfstepData.class);
			if(wfstepData==null){
				doResult.setRetCode(Errors.INVALIDDATA);
				doResult.setErrorInfo("请求数据无效");
				return doResult.GetJSONByDoResult();
			}
			String dataid=wfstepData.getWfstepid();
			if(!StringHelper.IsNullOrEmpty(dataid)){
				CnstWfstepData tempwfstepData=(CnstWfstepData)this.allAssetDao.GetDataById(CnstWfstepData.class, dataid);
			//	JsonHelper.GetBeanByJsonString(WXDJJson, tempwfstepData);
				tempwfstepData.setDefaultuser(wfstepData.getDefaultuser());
				tempwfstepData.setMemo(wfstepData.getMemo());
				tempwfstepData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
				if(CommonMethods.isNullOrWhitespace(wfstepData.getDefaultuser()) || !this.allAssetDao.SavaOrUpdateData(tempwfstepData, false)){
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("更新流程信息失败");
					return doResult.GetJSONByDoResult();
				}
				doResult.setErrorInfo(tempwfstepData.getWfstepid());
			}else{
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更新维流程信息失败");
			}
			return doResult.GetJSONByDoResult();
		}
		
		
	///流程方法

	/**
	 * 获取预定义的流程
	 * @param flowType 流程类型
	 * @return 返回有序的流程Vo List
	 */
	public List<?> getFlowDefine(String flowType){
		return allAssetDao.getFlowDefine(flowType);
	}

	/**
	 * 报废转交下一步
	 * @param fwLsjlJson 历史记录VO
	 * @param fwJson 流程中步骤 、状态 、下一步操作人 等数据
	 * @return 操作成功的提示信息
	 */
	public String passToNextSetp(String fwLsjlJson,String fwJson){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstWfhistoryData lsjlData=(CnstWfhistoryData)JsonHelper.GetBeanByJsonString(fwLsjlJson, CnstWfhistoryData.class);
		if(lsjlData==null){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}

		JSONObject tempJsonObj=JSONObject.fromObject(fwJson);
		/*Object objValue=tempJsonObj.get("dqclrdata");
			Object objValue1=tempJsonObj.get("xgbz");
			Object objValue4=tempJsonObj.get("dqbz");*/
		Object objNextStep=tempJsonObj.get("xybclrdata");//下一步处理人ID
		Object objFWType=tempJsonObj.get("wfsteptype");//流程类型
		Object objDqbz=tempJsonObj.get("dqbz");//当前步骤
		//	Object objValue3=tempJsonObj.get("wfstepid");//下一步cnst_wfstep_data表ID

		//通知推送
		List<Map<String,String>> tzrList = new ArrayList<Map<String,String>>();
		Map<String,String> tznrMap;
		
		if(objFWType.toString().indexOf("TKSQ") > -1){
			//1.更新/提交 退库单信息	
			String dataid = lsjlData.getWfdataid();//报废单ID
			if(!StringHelper.IsNullOrEmpty(dataid)){
				CnstTkdData tkdData=(CnstTkdData)this.allAssetDao.GetDataById(CnstTkdData.class, dataid);

				//退库结束时做数量验证
				//1.获取所有类型2.获取所有类型的数量3.每个类型做数量验证
				if(tkdData.getZt() != null && tkdData.getZt().contains("审核")){
				CnstTkdmxData mxdata = new CnstTkdmxData();
				mxdata.setUsable(1);
				List<?> tkdmxList = allAssetDao.getObjectByExample(mxdata," tkdid ='"+tkdData.getTkdid()+"' ");
				ArrayList<String> lyhcType = new ArrayList<String>();
				ArrayList<Float> lyhcSl = new ArrayList<Float>();
				
				for(Object tkdmxdt:tkdmxList){
					if(!lyhcType.contains(((CnstTkdmxData)tkdmxdt).getTkdmxtype())){
						lyhcType.add(((CnstTkdmxData)tkdmxdt).getTkdmxtype());
					}
				}
				
				float count0;
				for(String lytype:lyhcType){
			    count0 = 0;
					for(Object tkdmxdt:tkdmxList){
						if(lytype.equals(((CnstTkdmxData)tkdmxdt).getTkdmxtype())){
							count0 += ((CnstTkdmxData)tkdmxdt).getSjsl();
						}
					}
				lyhcSl.add(count0);
				}
				
				//退库数量 在选择批次时 验证
				/*for(int i=0;i<lyhcType.size();i++){
					String hctype = lyhcType.get(i);
					Float hcsl = lyhcSl.get(i);
					String strSLSQL=StringHelper.Format("SELECT COUNT(0) FROM CNSV_RYHC WHERE RYHCBH='%1$s' AND YYSL>=%2$s",hctype,hcsl);
					int numcount=this.allAssetDao.GetDataCount(strSLSQL);
					if(numcount<1){
						doResult.setRetCode(Errors.INVALIDDATA);
						String hctypeStr = "";	
						List cclList = allAssetDao.getObjectByExample(new CnstCodelistData()," CODETYPE='RYHC' AND CODEBS='"+hctype+"'");
						if(!cclList.isEmpty()){
							hctypeStr = "'"+((CnstCodelistData)cclList.get(0)).getCodename()+"'的";
						}
						doResult.setErrorInfo(hctypeStr+"退库实际数量之和'"+hcsl.toString().substring(0,hcsl.toString().length()-2)+"'不能超过已领用数量!");
						return doResult.GetJSONByDoResult();
					}
				}*/
				}else{
					CnstTkdmxData mxdata = new CnstTkdmxData();
					mxdata.setUsable(1);
					List<?> tkdmxList = allAssetDao.getObjectByExample(mxdata," tkdid ='"+tkdData.getTkdid()+"' ");
					for(int i=0;i<tkdmxList.size();i++){
						CnstTkdmxData tkdmxdt = (CnstTkdmxData)tkdmxList.get(i);
						int yxzsl = 0;
						List rstList = allAssetDao.getObjectBySql("select sum(SQSL) from cnst_lydmx_lysl_data  where sqdmxid='"+tkdmxdt.getTkdmxid()+"' and gbdm='"+tkdmxdt.getGbdm()+"'");
						if(rstList != null && !rstList.isEmpty() && rstList.get(0) != null){
							yxzsl = Integer.parseInt(rstList.get(0).toString());
						}
						if(tkdmxdt.getSjsl() != yxzsl){
						doResult.setRetCode(Errors.INTERNALERROR);
						doResult.setErrorInfo(tkdmxdt.getWpmc()+"退库已选择数量 "+ yxzsl +" 必须等于退库实际数量 "+tkdmxdt.getSjsl()+" !");
						return doResult.GetJSONByDoResult();
						}
					}
				}
				
				tkdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
				tkdData.setUuser(lsjlData.getUuser());
				tkdData.setZt(objDqbz.toString());
				if(!this.allAssetDao.SavaOrUpdateData(tkdData, false)){
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("提交退库单失败");
					return doResult.GetJSONByDoResult();
				}
				doResult.setErrorInfo(dataid);
				
				//保存成功后添加提醒    通知物质管理员
				if(tkdData.getZt().contains("物资管理员审核")){
				//退库审核  通知所有物质管理员  和 超级管理员
				 List<Rules> rulelist = super.getObjectList(new Rules(), " mc='物资管理员' or mc='超级管理员' ");
				if(!rulelist.isEmpty()){
					for(Rules rulec:rulelist){
						List<Users> userList = rulec.getUserses();
						for(Users curUser:userList){
							tznrMap= new HashMap<String, String>();
							tznrMap.put("cuserid", curUser.getId(). longValue()+"");
							tznrMap.put("uuserid", tkdData.getUuser());
							tznrMap.put("wftype", "TKSQ");
							tznrMap.put("wfid", tkdData.getTkdid());
							tzrList.add(tznrMap);
						}
					}
				}
					/*//只通知一个物资管理员
					tznrMap= new HashMap<String, String>();
					tznrMap.put("cuserid", "13205");//Jack
					tznrMap.put("uuserid", tkdData.getUuser());
					tznrMap.put("wftype", "TKSQ");
					tznrMap.put("wfid", tkdData.getTkdid());
					tzrList.add(tznrMap);*/
				}else{
				//通知申请人
				tznrMap = new HashMap<String, String>();
				tznrMap.put("cuserid", tkdData.getCuser());
				tznrMap.put("uuserid", tkdData.getUuser());
				tznrMap.put("wftype", "TKSQ");
				tznrMap.put("wfid", tkdData.getTkdid());
				tznrMap.put("wfzt", tkdData.getZt());
				tzrList.add(tznrMap);
				}
			}
			
		}else if(objFWType.toString().indexOf("BFSQ") > -1){
			//1.更新/提交 报废单信息	
			String dataid = lsjlData.getWfdataid();//报废单ID
			if(!StringHelper.IsNullOrEmpty(dataid)){
				CnstBfdData tempBfdData=(CnstBfdData)this.allAssetDao.GetDataById(CnstBfdData.class, dataid);
				tempBfdData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
				tempBfdData.setUuser(lsjlData.getUuser());
				if(objNextStep.toString().indexOf("结束") < 0){
					tempBfdData.setBmldid(Long.parseLong(objNextStep.toString()));
				}
	
				String bzzt = getNextBzAndZt(lsjlData.getWfhstepid());
				if(bzzt == null){
					doResult.setRetCode(Errors.INVALIDDATA);
					doResult.setErrorInfo("流程ID错误!");
					return doResult.GetJSONByDoResult();
				}
				tempBfdData.setBfsqzt(bzzt.substring(0,bzzt.indexOf(";")));
				tempBfdData.setBfsqbz(bzzt.substring(bzzt.indexOf(";")+1,bzzt.length()));
	
				if(!this.allAssetDao.SavaOrUpdateData(tempBfdData, false)){
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("提交报废单失败");
					return doResult.GetJSONByDoResult();
				}
				//修改明细为报废状态
				if(objNextStep.toString().indexOf("结束") > -1){
					List<?> bfmxList = this.allAssetDao.getObjectByExample(new CnstBfdmxData(), " bfdid='"+tempBfdData.getBfdid()+"'");
					String ztColumn = "zt";
					String zt = "报废";
					for(Object obj:bfmxList){
						if("T_FURNITURE".equals(((CnstBfdmxData)obj).getAssetTableName()) || "TB_HERITAGE".equals(((CnstBfdmxData)obj).getAssetTableName())){
							ztColumn = "status";
							zt = "322";
						}
					//	this.updateAssetZt(((CnstBfdmxData)obj).getAssetTableName(), ((CnstBfdmxData)obj).getAssetIdColumn(),((CnstBfdmxData)obj).getAssetId(), ztColumn, zt);
					}
				}
				doResult.setErrorInfo(dataid);
				
				//保存成功后添加提醒
				tznrMap = new HashMap<String, String>();
				tznrMap.put("cuserid", tempBfdData.getCuser());
				tznrMap.put("uuserid", tempBfdData.getUuser());
				tznrMap.put("wftype", "BFSQ");
				if(objNextStep.toString().indexOf("结束") < 0)tznrMap.put("nextstepuserid",objNextStep.toString());
				tznrMap.put("wfid", tempBfdData.getBfdid());
				tznrMap.put("wfzt", tempBfdData.getBfsqzt());
				tzrList.add(tznrMap);
			}
		}
		
		//2.保存历史记录
		String lsjlDataid=lsjlData.getWfhistoryid();
		if(StringHelper.IsNullOrEmpty(lsjlDataid)){
			String strGUID=CommonHelper.GenGuidEx();
			lsjlData.setWfhistoryid(strGUID);
		}
		lsjlData.setCtime(DateHelper.GetCurrentUtilDateTimeClass());
		lsjlData.setUtime(DateHelper.GetCurrentUtilDateTimeClass());
		if(!this.allAssetDao.SavaOrUpdateData(lsjlData, true)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("创建流程历史记录失败");
			return doResult.GetJSONByDoResult();
		}
		
		pushSysMsg(tzrList);
		return doResult.GetJSONByDoResult();
	}
	//获取下一步的状态、步骤
	private String getNextBzAndZt(String stepId){
		CnstWfstepData stepData=(CnstWfstepData)this.allAssetDao.GetDataById(CnstWfstepData.class,stepId);
		if(stepData == null)return null;
		//("SELECT * FROM cnst_wfstep_data WHERE  wfsteptype='"+stepType+"' && wfstepname = '"+currBz+"'");   
		//, strId)(CnstWfstepData.class, " wfsteptype='"+stepType+"' && wfstepname = '"+currBz+"'");
		String nextStep =  stepData.getNextstep();
		if(nextStep != null){
			try {
				for(Iterator<?> itor= DocumentHelper.parseText(nextStep).getRootElement().elementIterator();itor.hasNext();){
					Element element = (Element) itor.next();
					if(element.attribute("NEXTSTEPID") !=null){
						nextStep =  element.attribute("NEXTSTEPID").getValue() ;
						break;
					}
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			//	nextStep = nextStep.substring(nextStep.indexOf("NEXTSTEPID=")+11,nextStep.indexOf("CONDITION=")-2);
			stepData = (CnstWfstepData)this.allAssetDao.GetDataById(CnstWfstepData.class,nextStep);
			if(stepData == null)return null;
			return stepData.getWfstepname()+";"+stepData.getWfstepid();
		}else{
			return "已结束;结束" ;
		}
	}
	
	/**
	 * 推送最新流程变动信息
	 * tznrMap.put("cuserid", tempBfdData.getCuser());
	 * tznrMap.put("uuserid", lydData.getUuser());
	 *	tznrMap.put("wftype", "TKSQ");
	 *	tznrMap.put("nextstepuserid",objNextStep.toString());
	 *	tznrMap.put("wfid", tempBfdData.getBfdid());
	 *	tznrMap.put("wfzt", tempBfdData.getBfsqzt());
	 * 只指定通知人
	 * 通知内容在前台调用请求
	 */
	 public void pushSysMsg(List<Map<String,String>> tzrArray){
		 if(tzrArray == null)return;
		 
		 CnstNotice cnt;
		 for(Map<String,String> tzrMap:tzrArray){
		//通知创建人  提交申请时不用通知
		 if("RYHC".equals(tzrMap.get("wftype")) || ("TKSQ".equals(tzrMap.get("wftype")) && tzrMap.get("wfzt")!=null && tzrMap.get("wfzt").contains("物资管理员审核")) 
				 ||  (tzrMap.get("wfzt")!=null &&tzrMap.get("wfzt").contains("部门领导"))) 	{ 
		 }else{
		     if(!CommonMethods.isNullOrWhitespace(tzrMap.get("cuserid"))){
		    	//保存 领用/报废/退库 申请 通知数据到DB
		    	 cnt = new CnstNotice();
				 cnt.setWfid(tzrMap.get("wfid")); 
				 cnt.setNttype(tzrMap.get("wftype")+"-SQ");
				 cnt.setIsread("1");
				 cnt.setTzrid(tzrMap.get("cuserid"));
				 cnt.setCuser(tzrMap.get("uuserid"));
				 cnt.setCtime(new Date());
				 cnt.setUuser(tzrMap.get("uuserid"));
				 cnt.setUtime(new Date());
				 allAssetDao.save(cnt);
				 
				 //调用前台
		    	 CommonMethods.pushSysMsg(tzrMap.get("cuserid"), tzrMap);
			 }
		 }
		 
		if("RYHC".equals(tzrMap.get("wftype")) && !CommonMethods.isNullOrWhitespace(tzrMap.get("cuserid")) && !CommonMethods.isNullOrWhitespace(tzrMap.get("lyhcTypeCtn"))) {
		 List<Object[]> list = this.allAssetDao.getObjectBySql(" select * from cnsv_ryhc where usable=1 and sysl<="+CommonMethods.MinRyhcsl+" and zsl>0 and ryhcbh in "+tzrMap.get("lyhcTypeCtn")+" ");
			if(!list.isEmpty()){
				//保存日用耗材数量不足 通知信息到DB
				for(Object[] objc : list){
					//已经有的提醒不在保存
					List<CnstNotice> countList = this.allAssetDao.getObjectByExample(new CnstNotice()," nttype='RYHC-"+objc[1]+"' and tzrid="+tzrMap.get("cuserid"));
					if(!countList.isEmpty()){
						for(CnstNotice cnt1:countList){
							cnt1.setUtime(new Date());
							cnt1.setUuser(tzrMap.get("uuserid"));
							allAssetDao.update(cnt1);
						}
					}else{
					cnt = new CnstNotice();
					 cnt.setWfid(objc[0].toString()); 
					 cnt.setNttype("RYHC-"+objc[1].toString());
					 cnt.setIsread("1");
					 cnt.setTzrid(tzrMap.get("cuserid"));
					 cnt.setCuser(tzrMap.get("uuserid"));
					 cnt.setCtime(new Date());
					 cnt.setUuser(tzrMap.get("uuserid"));
					 cnt.setUtime(new Date());
					 allAssetDao.save(cnt);
					}
				}
				CommonMethods.pushSysMsg(tzrMap.get("cuserid"), tzrMap);
			}
		 }
		 
	     //通知下一步处理人
	     if(!CommonMethods.isNullOrWhitespace(tzrMap.get("nextstepuserid")) && !tzrMap.get("nextstepuserid").contains("结束")){
	    	//保存 领用/报废/退库 审核通知数据到DB
	    	 cnt = new CnstNotice();
			 cnt.setWfid(tzrMap.get("wfid")); 
			 cnt.setNttype(tzrMap.get("wftype")+"-SH");
			 cnt.setIsread("1");
			 cnt.setTzrid(tzrMap.get("nextstepuserid"));
			 cnt.setCuser(tzrMap.get("uuserid"));
			 cnt.setCtime(new Date());
			 cnt.setUuser(tzrMap.get("uuserid"));
			 cnt.setUtime(new Date());
			 allAssetDao.save(cnt);
	    	 
	    	 CommonMethods.pushSysMsg(tzrMap.get("nextstepuserid"), tzrMap);
	     }
		 }
		 //通知系统管理员admin
		 CommonMethods.pushSysMsg("1", "");
		 
	 }
	 
	  public String getWFHistory(String wfId)
	  {
	    JSONArray resultAry = this.allAssetDao.RunSelectJSONArrayBySql(" select * from cnsv_wfhistory wfhis where wfhis.wfdataid='" + wfId + "' order by wfhis.ctime ", null);
	    List list = this.allAssetDao.getObjectBySql("select bmldid nextstepuserid from cnst_bfd_data where bfdid='"+wfId+"' and bfsqzt!='已结束'" +
	    		"union select bmldid nextstepuserid from cnst_lyd_data where lydid='"+wfId+"' and lyzt!='已结束'");
	    Map<String, String> userInfo = new HashMap<String, String>();
	    if(!list.isEmpty() && !CommonMethods.isNullOrWhitespace(list.get(0))){
	    	long nextStepUserid = Long.parseLong(list.get(0).toString());
	    	UserService usv = (UserService) this.apc.getBean("UserService");
	    	UserVo userVo  = usv.getUserVoById(nextStepUserid);
			userInfo.put("nextStepUserid", nextStepUserid+"");
			userInfo.put("dept", userVo.getDeptName()) ;
			userInfo.put("username", userVo.getUserName()) ;
			userInfo.put("rulename", userVo.getRuleName()) ;
	    }
	    Map<String,Object> rstMap = new HashMap<String, Object>();
	    rstMap.put("nextstepuserInfo", userInfo);
	    rstMap.put("hsitoryArray", resultAry.toString());
	    return  JSONObject.fromObject(rstMap).toString();
	  }

	  public String getWFShHistory(String userId, String WFType) {
	    String sql = " select BFD.BFDID from cnst_bfd_data  bfd inner join  CNST_WFHISTORY_DATA  his on bfd.bfdid = his.wfdataid  and bfd.cuser <> '" + userId + "' and his.cuser = '" + userId + "' ";
	    JSONArray resultAry = this.allAssetDao.RunSelectJSONArrayBySql(sql, null);
	    return resultAry.toString();
	  }
	  /**
	   * 修改固定资产明细为报废状态
	   */
	public void updateAssetZt(String tableName,String idColumn,String id,String ztColumn,String zt){
		allAssetDao.RunUpdateBySQL(" update "+tableName+" set "+ztColumn+"= '"+zt+"' where "+idColumn+" ='"+id+"' ",null);
	}
	/**
	 * 验证编号WFBH是否重复
	 * @param wfbh 编号
	 * @param tableName 表名
	 * @param dataId id
	 * @return true 重复
	 */
	public boolean validateWFBH(String wfbh,String idColumn,String bhColumn,String tableName,String dataId){
		boolean flag = false;
		if(CommonMethods.isNullOrWhitespace(wfbh)|| CommonMethods.isNullOrWhitespace(idColumn)|| CommonMethods.isNullOrWhitespace(bhColumn)
				|| CommonMethods.isNullOrWhitespace(tableName)){return true;}
		
		String sql = "select count(0) from "+tableName+" where "+bhColumn+" = '"+wfbh+"' ";
		if(!CommonMethods.isNullOrWhitespace(dataId)){
			sql += " and "+idColumn+" <> '"+dataId+"'";
		}
		List list = allAssetDao.getObjectBySql(sql);
		if(list!= null && !list.isEmpty() && Integer.parseInt(list.get(0).toString()) > 0){
			flag = true;
		}
		return flag;
	}
	
	public AllAssetDao getAllAssetDao() {
		return allAssetDao;
	}

	public void setAllAssetDao(AllAssetDao allAssetDao) {
		this.allAssetDao = allAssetDao;
	}

	public WFHandleDao getWfHandleDao() {
		return wfHandleDao;
	}

	public void setWfHandleDao(WFHandleDao wfHandleDao) {
		this.wfHandleDao = wfHandleDao;
	}
	
	public void setApplicationContext(ApplicationContext apc)
			throws BeansException {
			this.apc = apc;	
	}
	
	
}
