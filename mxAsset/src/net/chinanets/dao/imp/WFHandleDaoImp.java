package net.chinanets.dao.imp;

import java.util.List;

import net.chinanets.dao.WFHandleDao;
import net.chinanets.entity.CnstWfstepData;
import net.chinanets.utils.helper.StringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 工作流相关操作实现类
 * @author dzj
 *
 */
public class WFHandleDaoImp extends CommonDaoImp implements WFHandleDao {
	//查询所有流程步骤数据
	public String GetWFStepByPaging(String tableName,int tempPageSize,int tempPageCurrent,String strcondition){
		String strHQL=this.FindHqlBySearchForm(tableName, strcondition);
		String selectResultCount=StringHelper.Format("select count(0) %1$s",strHQL);
		String selectResultHql=StringHelper.Format("SELECT ROWNUM AS RN,TMPT.* %1$s",strHQL);
		JSONArray tempJson=this.RunSelectJSONArrayBySql(selectResultHql, tempPageSize, tempPageCurrent);
		String total=this.RunSelectCountBySql(selectResultCount, null)+"";
		String items="";
		if(tempJson!=null && tempJson.size()>0){
			items=tempJson.toString();
		}
		JSONObject result=new JSONObject();
		result.put("itemtotal", total);
		result.put("othermsg", "");
		result.put("items", items);
		return result.toString();
	}	
	
	//根据类型查找对应的流程步骤
	@SuppressWarnings("unchecked")
	public List<CnstWfstepData> GetWFStepByType(String strType){
		if(StringHelper.IsNullOrEmpty(strType)){
			return null;
		}
		String strSQL=StringHelper.Format("SELECT * FROM CNSV_WFSTEP A1 WHERE A1.WFSTEPTYPE='%1$s'",strType);
		List<CnstWfstepData> wfstepList=(List<CnstWfstepData>)this.RunSelectClassBySql(strSQL, null, CnstWfstepData.class);
		return wfstepList;
	}
	
	//根据ID得到对象
	public Object GetDataById(Class<?> tempClass,String strId){
		return this.FindByIdEx(tempClass, strId);
	}	
	
	//根据数据ID查询流程历史步骤
	public String GetWFHistoryByDataId(String strDataId){
		if(StringHelper.IsNullOrEmpty(strDataId)){
			return null;
		}
		String strSQL=StringHelper.Format("SELECT * FROM CNSV_WFHISTORY A1 WHERE A1.WFDATAID='%1$s'",strDataId);
		JSONArray tempJson=this.RunSelectJSONArrayBySql(strSQL, null);
		if(tempJson==null){
			return null;
		}
		return tempJson.toString();
	}
	
	//保存或更改数据
	public boolean SavaOrUpdateData(Object tempObj,boolean isSave){
		if(isSave){
			return this.SaveEx(tempObj);
		}else{
			return this.UpdateEx(tempObj);
		}
	}
	
	//删除数据
	public boolean RemoveData(String strSQL){
		return this.RunUpdateBySQL(strSQL, null);
	}
	
	//删除数据
	public boolean RemoveData(String tableName,String colunmName,String strid){
		String strSQL=StringHelper.Format("DELETE FROM %1$s WHERE %2$s in (%3$s)",tableName,colunmName,strid);
		return this.RunUpdateBySQL(strSQL, null);
	}	
}
