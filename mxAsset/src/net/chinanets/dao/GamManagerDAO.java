package net.chinanets.dao;

/**
 * 物资管理DAO层
 * @author dzj
 *
 */
public interface GamManagerDAO extends CommonDao {
	//分页查询数据
	public String SelectGamAPI(int tempPageSize,int tempPageCurrent,String strcondition);
	
	//删除数据
	public String RemoveGamAPI(String gamid);
	
	//导出数据
	public String ExportGamAPI(int start,int end);
}
