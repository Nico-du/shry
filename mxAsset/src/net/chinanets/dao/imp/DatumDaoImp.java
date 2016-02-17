package net.chinanets.dao.imp;

import java.util.List;

import net.chinanets.dao.DatumDao;
import net.chinanets.pojos.CnstDatumData;
import net.chinanets.pojos.CnstDirectoryData;
import net.chinanets.utils.helper.JsonHelper;
import net.chinanets.utils.helper.StringHelper;
import net.sf.json.JSONObject;

/**
 * 相关资料DAO层接口实现
 * @author RLiuyong
 *
 */
public class DatumDaoImp extends CommonDaoImp implements DatumDao {
	//得到所有目录信息
	@SuppressWarnings("unchecked")
	public List<CnstDirectoryData> GetDatumDirectoryList() {
		String strhql="from CnstDirectoryData as directory where directory.usable=1 order by directory.ctime asc";
		List<?> tempList=this.FindListByHQLEx(strhql, null);
		if(tempList!=null){
			return (List<CnstDirectoryData>)tempList;
		}
		return null;
	}

	//根据ID获取目录值
	public CnstDirectoryData GetDirectoryById(String directoryId){
		return (CnstDirectoryData)this.FindByIdEx(CnstDirectoryData.class,directoryId);
	}
	
	//根据目录得到文件信息
	@SuppressWarnings("unchecked")
	public List<CnstDatumData> GetDatumListByDirectoryId(String directoryId) {
		String strhql=StringHelper.Format("from CnstDatumData as datum where datum.usable=1 and datum.cnstDirectoryData.directoryid='%1$s'",directoryId);
		List<?> tempList=this.FindListByHQLEx(strhql, null);
		if(tempList!=null){
			return (List<CnstDatumData>)tempList;
		}
		return null;
	}

	//根据ID获取文件值
	public CnstDatumData GetDatumById(String datumId){
		return (CnstDatumData)this.FindByIdEx(CnstDatumData.class,datumId);
	}
	
	//根据条件查询总数量
	public int GetCountByHQL(String strhql){
		return this.getObjectItemTotalCount(strhql);
	}
	
	//分页查询数据
	@SuppressWarnings("unchecked")
	public String GetDatumListByPaging(int tempPageSize,int tempPageCurrent,String strcondition){
		String strHQL=this.FindHqlBySearchForm(CnstDatumData.class, strcondition);
		String selectResultHql="select * "+strHQL;
		String selectResultCount="select count(0) "+strHQL;
		List<CnstDatumData> datumList=(List<CnstDatumData>)this.FindPoPageByCondition(selectResultHql, tempPageSize, tempPageCurrent);
		String total=this.GetCountByHQL(selectResultCount)+"";
		String items="";
		if(datumList!=null && datumList.size()>0){
			items=JsonHelper.GetJsonObjByList(datumList).toString();
		}
		JSONObject result=new JSONObject();
		result.put("itemtotal", total);
		result.put("othermsg", "");
		result.put("items", items);
		return result.toString();
	}
	
	//添加或更改目录
	public boolean SaveOrUpdateDatumDirectory(CnstDirectoryData directory,boolean isSave) {
		if(isSave){
			return this.SaveEx(directory);
		}else{
			return this.UpdateEx(directory);
		}
	}

	//添加或更改文件
	public boolean SaveOrUpdateDatum(CnstDatumData datum,boolean isSave){
		if(isSave){
			return this.SaveEx(datum);
		}else{
			return this.UpdateEx(datum);
		}
	}
	
	//通过HQL语句更新数据
	public boolean UpdateDataByHQL(String strHQL){
		return this.UpdateByHqlEx(strHQL);
	}
}
