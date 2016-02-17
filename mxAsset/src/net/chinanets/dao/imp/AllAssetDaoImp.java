package net.chinanets.dao.imp;

import java.util.List;

import net.chinanets.dao.AllAssetDao;
import net.chinanets.data.DataEntity;
import net.chinanets.utils.helper.StringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 所有物资接口实现
 * @author Liuyong
 *
 */
public class AllAssetDaoImp extends CommonDaoImp implements AllAssetDao {
	
	//所有物资分页查询数据
	public String GetAllAssetListByPaging(String tableName,int tempPageSize,int tempPageCurrent,String strcondition){
		String strHQL=this.FindHqlBySearchForm(tableName, strcondition);
		String selectResultCount="select count(0) "+strHQL;
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
	
	//根据SQL查询DataEntity
	public List<DataEntity> GetDataEntityBySQL(String strSQL){
		return this.RunSelectDataEntityBySql(strSQL, null);
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
	
	//根据ID得到对象
	public Object GetDataById(Class<?> tempClass,String strId){
		return this.FindByIdEx(tempClass, strId);
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
	
	//查询流程定义数据
	public JSONArray getFlowDefine(String flowType){
		Object []objs ={};
		return super.RunSelectJSONArrayBySql("select * from CNSV_WFSTEP where WFSTEPTYPE='"+flowType+"'", objs);
	}
}
