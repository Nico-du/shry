package net.chinanets.dao.imp;

import net.chinanets.dao.CodeListDao;
import net.chinanets.utils.helper.StringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 所有代码表接口实现
 * @author Liuyong
 *
 */
public class CodeListDaoImp extends CommonDaoImp implements CodeListDao {

	//所有代码表查询数据
	public String GetCodeListByPaging(String tableName,int tempPageSize,int tempPageCurrent,String strcondition){
		String strSQL;
		if(!strcondition.startsWith("{")){
			strSQL = "from CNSV_ALLDICTIONARY TMPT where 1>0 and " + strcondition +" ";
		}else{
			strSQL = this.FindHqlBySearchForm(tableName, strcondition);
		}
		
		String selectResultCount=StringHelper.Format("SELECT COUNT(0) %1$s",strSQL);
	//	String selectResultHql=StringHelper.Format("SELECT ROWNUM AS RN,TMPT.* %1$s",strSQL);
		String selectResultHql=StringHelper.Format("SELECT TMPT.* %1$s",strSQL);
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
	
	//根据ID得到对象
	public Object GetDataById(Class<?> tempClass,String strId){
		return this.FindByIdEx(tempClass, strId);
	}
	
	//根据代码表类型查询数据
	public JSONArray GetDataByCodeType(String strType){
		String strSQL="SELECT CODEBS AS VALUE,CODENAME AS LABEL FROM CNST_CODELIST_DATA WHERE 1>0";
		if(!StringHelper.IsNullOrEmpty(strType)){
			strType=StringHelper.ConvertStrToDBStr(strType);
			strSQL+=StringHelper.Format(" AND CODETYPE IN (%1$s)",strType);
		}
		return this.RunSelectJSONArrayBySql(strSQL +" ORDER BY CODEBS", null);
	}
	
	//查询数据总量
	public int GetDataCount(String strSQL){
		return this.RunSelectCountBySql(strSQL, null);
	}
	
	//查找数据总量
	public int GetDataCount(String tableName,String colunmName,String strid){
		String strSQL=StringHelper.Format("SELECT COUNT(0) FROM %1$s WHERE USABLE=1 AND %2$s in (%3$s)",tableName,colunmName,strid);
		int count=this.RunSelectCountBySql(strSQL, null);
		return count;
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
