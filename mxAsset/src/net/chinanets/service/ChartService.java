package net.chinanets.service;

import java.util.List;
import java.util.Map;

import net.chinanets.pojos.ShryFyxnData;
import net.chinanets.vo.UserVo;
import net.sf.json.JSONArray;
@SuppressWarnings("unchecked")
public interface ChartService {
	/**
	 * 风叶选型的方法
	 * @param selectionJson
	 * @return
	 */
	public String selectFYAction(String selectionJson);
	
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
	public List<ShryFyxnData> getFYXNChartList(String sSql,String fyid,Double hsbl);
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
	
	public List yearCount();
	
	public List xmJe(String s);
	
	public List xmZq(String s);
	
	public List getNf();

	public String getClockInfo(String time);
	
	public void changeProcessZt(Long id);
	//获取巡检记录 月度/年度   正常/不正常 的巡检数量
	public List<Map<String, String>> getXjjlChart(String dateRange);
	
	
}
