package net.chinanets.service;

import java.util.List;
import java.util.Map;

import net.chinanets.pojos.ShryDjxnData;
import net.chinanets.pojos.ShryFyxnData;
import net.chinanets.pojos.ShryZcxnData;
import net.chinanets.vo.UserVo;
@SuppressWarnings("unchecked")
public interface ChartService {
	/**
	 * 风叶选型的方法
	 * @param selectionJson
	 * @return
	 */
	public String selectFYAction(String selectionJson);
	
	/**
	 * 总成性能数据图 换算-->1.性能数据换算 2.matlab插值计算
	 * 等比利换算
	 * 根据转速变换 取其他性能参数
	 * @param sSql
	 * @param hsbl 换算比利，%数
	 * @param hszsbl 转速换算比利，%数
	 * @param hsdlhzj 导流环直径 换算值
	 * @return
	 * @throws Exception 
	 */
	public Map<String,List<ShryZcxnData>> getZCXNInsertChartList(String sSql,String zcid,Double hszsbl,Double hsdlhzj) throws Exception;
	/**
	 * 风叶性能数据图 换算
	 * 等比利换算
	 * 根据转速变换 取其他性能参数
	 * @param sSql
	 * @param fyid 风叶id
	 * @param hszsbl 转速换算比利，%数
	 * @param hsdlhzj 导流环直径 换算值
	 * @return
	 * @throws Exception 
	 */
	public Map<String,List<ShryFyxnData>> getFYXNInsertChartList(String sSql,String fyid,Double hszsbl,Double hsdlhzj) throws Exception;
	/**
	 * 电机性能数据图 插值计算
	 * @param sSql
	 * @param djid 电机id
	 * @return
	 * @throws Exception 
	 */
	public Map<String,List<ShryDjxnData>> getDJXNInsertChartList(String sSql,String djid) throws Exception;
	
	/**
	 * 数据换算
	 * @param jsonArrayStrIn
	 * @param type
	 * @return
	 */
	public String translateData(String jsonArrayStrIn,String jsonObjectIn,String type);
	
	/**
	 * 风叶选型结果的 数据换算
	 * 获取选型结果曲线数据列表
	 */
	public List<ShryFyxnData> getXXJGChartList(String sSql,String jsonObjectIn);
	
	/**
	 * 风叶性能数据图 换算
	 * 等比利换算
	 */
	public List<ShryFyxnData> getFYXNChartList(String sSql,String fyid,Double hszsbl,Double hsdlhzj);
	/**
	 * 风叶性能数据图 换算
	 * 等比利换算
	 */
	public List<ShryZcxnData> getZCXNChartList(String sSql,String fyid,Double hszsbl,Double hsdlhzj);
	///主页homeModel的方法
	/**
	 * 获取我的任务
	 * @param userid
	 * @return
	 */
	public String getMyMessions(UserVo userVo);
	/**
	 * 获取我的最新任务
	 * @param userid
	 * @return
	 */
	public String getMyLatestMessions(UserVo userVo);
	/**
	 * 获取数量不足提醒数量
	 * @param minSl 提醒数量的最低值
	 * @return
	 */
	public String getMyLatestRyhcMsgs(Long userid);
	
	
	
	/**
	 * 获取 主页 物资总体购买情况
	 */
	public Map getAssetBuyInfo(int yearStr);
	/**
	 * 获取日用耗材使用情况
	 */
	public List getXXSBInfo();
	/**
	 * 获取所有物资数量信息 
	 * @return
	 */
	public Map getAssetAllCountInfo();
	
	///报表的方法
	/**
	 * 所有部门季度耗材领用状况
	 * @param year
	 * @return
	 */
	public List<Map> getBmhclyqk(int year);
	
	
}
