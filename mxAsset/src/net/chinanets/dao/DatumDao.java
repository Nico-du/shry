package net.chinanets.dao;

import java.util.List;

import net.chinanets.pojos.CnstDatumData;
import net.chinanets.pojos.CnstDirectoryData;

/**
 * 相关资料DAO层接口
 * @author dzj
 *
 */
public interface DatumDao extends CommonDao {
	//得到所有目录信息
	public List<CnstDirectoryData> GetDatumDirectoryList();
	
	//根据ID获取值
	public CnstDirectoryData GetDirectoryById(String directoryId);
	
	//根据目录得到文件信息
	public List<CnstDatumData> GetDatumListByDirectoryId(String directoryId);
	
	//根据文件ID获取文件信息
	public CnstDatumData GetDatumById(String datumid);
	
	//得到数据总数量
	public int GetCountByHQL(String strhql);
	
	//分页查询数据
	public String GetDatumListByPaging(int tempPageSize,int tempPageCurrent,String strcondition);
	
	//添加或更改目录
	public boolean SaveOrUpdateDatumDirectory(CnstDirectoryData directory,boolean isSave);
	
	//添加或更改文件
	public boolean SaveOrUpdateDatum(CnstDatumData datum,boolean isSave);
	
	//通过HQL语句更新数据
	public boolean UpdateDataByHQL(String strHQL);
}
