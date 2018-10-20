package net.chinanets.service.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.chinanets.data.DataEntity;
import net.chinanets.pojos.ShryDjData;
import net.chinanets.pojos.ShryDjxnData;
import net.chinanets.pojos.ShryFyData;
import net.chinanets.pojos.ShryFyZsData;
import net.chinanets.pojos.ShryFyxnData;
import net.chinanets.pojos.ShryZcData;
import net.chinanets.pojos.ShryZcxnData;
import net.chinanets.service.ChartService;
import net.chinanets.utils.Arith;
import net.chinanets.utils.CommonMethods;
import net.chinanets.utils.Guass;
import net.chinanets.utils.MatlabInterp1Util;
import net.chinanets.utils.common.DoResult;
import net.chinanets.utils.common.Errors;
import net.chinanets.vo.UserVo;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;



@SuppressWarnings("unchecked")
public class ChartServiceImp extends CommonServiceImp implements ChartService {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private final String CODETYPE = "FYXX_PARAMS";
	
	/**
	 * 风叶选型入口
	 * selectionJson选型参数JSON
	 */
	public String selectFYAction(String selectionJson){
		DoResult doResult = new DoResult();
		doResult.setRetCode(Errors.OK);
		String sFbzj,sDlhzj,sJy,sLl,sGl,sZs,sFzid;
		sFbzj = sDlhzj = sJy = sLl=  sGl = sZs= sFzid = "";
		
		JSONObject jsonObj = JSONObject.fromObject(selectionJson);
		sFbzj = jsonObj.getString("fbzj");
		sDlhzj = jsonObj.getString("dlhzj");
		sJy = jsonObj.getString("jy");
		sLl = jsonObj.getString("ll");
		sGl = jsonObj.getString("gl");
		sZs = jsonObj.getString("zs");
		if(jsonObj.containsKey("fzid")){
		sFzid = jsonObj.getString("fzid");
		}
		
		//step_xx1.翻边直径、导流环直径误差范围
		String sFw = "0";
		
		String strSQL = "select  fyxn.* from shry_fyxn_data fyxn  ";
		double dValMin,dValMax; dValMin = dValMax = 0;
		if(!StringUtils.isBlank(sFbzj)){
			sFw = commonDao.getDictValue(CODETYPE, "FFZJ_FW");
			dValMin = NumberUtils.toDouble(sFbzj) - 0.0001  - NumberUtils.toDouble(sFw);
			dValMax = NumberUtils.toDouble(sFbzj) + NumberUtils.toDouble(sFw);
			strSQL += "where fyxn.fyid in ( select fy.fyid from shry_fy_data fy where fy.fbzj  between "+dValMin+" and "+dValMax+" )  ";
		}else if(!StringUtils.isBlank(sDlhzj)){
			sFw = commonDao.getDictValue(CODETYPE, "DLHZJ_FW");
			dValMin = NumberUtils.toDouble(sDlhzj) - 0.0001 - NumberUtils.toDouble(sFw);
			dValMax = NumberUtils.toDouble(sDlhzj) + NumberUtils.toDouble(sFw);
			strSQL += "where fyxn.fyid in ( select fy.fyid from shry_fy_data fy where fy.dlhzj between "+dValMin+" and "+dValMax+" )  )  ";
		}else if(StringUtils.isBlank(sDlhzj) && StringUtils.isBlank(sDlhzj) && !StringUtils.isBlank(sFzid) ){//分组ID
			strSQL += "where fyxn.fyid in ( select fz.glid from shry_xxfz_data fz where fz.fzid='"+sFzid+"' )  ";
		}
		strSQL += " order by fyxn.fyid,fyxn.fyxnid ";
		
		System.out.println("/r/n/r/n/r/n---------------------------风叶选型开始---------------------------------");
		System.out.println("查询SQL："+strSQL);
		
		String tempClassPath = "net.chinanets.pojos.ShryFyxnData";
		List<ShryFyxnData> fyxnList =	(List<ShryFyxnData>) commonDao.RunSelectClassBySql(strSQL, tempClassPath);
		System.out.println("搜索范围："+fyxnList.size() +" 条数据！");
		
		Double dFbzj = StringUtils.isBlank(sFbzj) ? null : NumberUtils.toDouble(sFbzj);
		Double dDlhzj = StringUtils.isBlank(sDlhzj) ? null : NumberUtils.toDouble(sDlhzj);
		
		//执行选型
		Map<String,Map<String,ShryFyxnData>>  xxjgListMap = new HashMap<String,Map<String,ShryFyxnData>> ();
		try {
			xxjgListMap = doFySelection(fyxnList,NumberUtils.toDouble(sJy),NumberUtils.toDouble(sLl),
					NumberUtils.toDouble(sGl),NumberUtils.toDouble(sZs),dDlhzj,dFbzj);
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("风叶选型报错：",e);
		}
		//转换选型结果
		JSONArray xxjgObjList  = transMapToViewData(xxjgListMap);
		System.out.println("\r\n\r\n\r\n---------------------------风叶选型结束,找到 "+xxjgObjList.size()+" 个符合条件的数据---------------------------------\r\n\r\n\r\n");
		
		JSONObject result=new JSONObject();
		result.put("itemtotal", xxjgObjList.size());
		result.put("othermsg", "");
		result.put("items", xxjgObjList.toString());
		return result.toString();
		
	}
	
	
	
	
	
	
	
	
	//转换成页面需要的数据格式
	private JSONArray transMapToViewData(Map<String, Map<String, ShryFyxnData>> xxjgListMap) {
		if(xxjgListMap == null || xxjgListMap.isEmpty()){
			return new JSONArray();
		}
		StringBuffer fyids = new StringBuffer("'',");
		for(String chKey: xxjgListMap.keySet()){
			fyids.append(chKey+",");
		}
		JSONArray rstJsonArray = new JSONArray();
		if(fyids.toString().endsWith(",")){ fyids = new StringBuffer(fyids.toString().substring(0,fyids.length()-1)); }
		JSONArray xxjgObjList = commonDao.RunSelectJSONArrayBySql("select *  from shry_fy_data where fyid in ("+fyids+") ", null);
		if( xxjgObjList == null || xxjgObjList.isEmpty()){
			logger.error("数据查询异常",new Exception("数据查询异常,数据量不匹配"));
			return new JSONArray();
		}
		JSONObject chNewObj ;
		for(int i=0;i<xxjgObjList.size();i++){
			JSONObject chObj =  xxjgObjList.getJSONObject(i);
			Map<String, ShryFyxnData> tmpMap = xxjgListMap.get(chObj.get("fyid")+"");
			if(tmpMap == null || tmpMap.isEmpty()){ logger.info("数据异常,数据匹配.fyid="+chObj.get("fyid")); continue;}

			for(String chZsKey:tmpMap.keySet()){
				chNewObj = new JSONObject();
				chNewObj.putAll(chObj);
				chNewObj.putAll(JSONObject.fromObject(tmpMap.get(chZsKey)));
				rstJsonArray.add(chNewObj);
			}
		}
		
		

		return rstJsonArray;
	}









	/**
	 * 返回满足条件的fyid List
	 * 使用默认的噪声数据进行选型 fyid为4的数据
	 * @param fyxnList
	 * @return {风叶ID:{转速:性能Obj}}
	 */
private Map<String,Map<String,ShryFyxnData>>  doFySelection(List<ShryFyxnData> fyxnList,double dJy,double dLl,double dGl,double dZs,Double dlhzjIn,Double fbzjIn) {
	
		/**
--结构参数：1.翻边直径  2.导流环直径 差值正负10
一般情况下选型时可能以上两个参数只会2选1输入

--性能参数：1.静压  2.流量  3.功率 4.噪声

选型要求：--20181008(如WebRoot/ReadMe/风叶选型-具体过程.png)
整个的流程就是 提取样本数据——>换算为待拟合数据——>拟合到指定点——>判断是否记录——>循环
1.提取样本数据：翻边直径 或 导流环直径 相等,筛选直径范围内的所有风叶性能数据(SQL查询).
S1:假设该风叶只有一组性能数据,以该数据为样本数据
S2:假设该风叶有多组性能数据：S2_1:以n_each/n_min/n_max为参数,选择跨距小的实验转速作为样本数据;以样本数据为基础,换算到转速n;S2_2:样本转速小于n_min,取该转速为n_min;
                            S2_3:样本转速大于n_max,取该转速为n_max
2.换算为待拟合数据：以n_each/n_min/n_max为参数切分((n_max-n_min)/n_each)个样本转速点,每个转速点都要有对应样本数据,没有样本数据取最近的样本转速点获取转换数据。
                                  循环获取所有待拟合数据。
3.优先找最接近的实验值 ;目标功率、噪声比输入值小；
    插值获取目标静压下的流量/扭矩/轴功率/效率，如果该流量>=目标值,记录该条数据；循环所有待拟合数据,显示所有符合条件数据。

选型要求：--old
0.翻边直径 或 导流环直径 相等
1.同静压点流量值大于输入值（120pa 2600m³/h）或者同流量点静压大于输入值（2500m³/h 130pa）；
2.目标功率、噪声比输入值小
3.优先找最接近的实验值
		 */
		//选型结果
		//{风叶ID:{转速:性能}}
		Map<String,Map<String,ShryFyxnData>> rstMap = new HashMap<String,Map<String,ShryFyxnData>>();
		if(fyxnList == null || fyxnList.isEmpty()){ return rstMap; }
		
		//Step1 分组:按风叶ID+LXDID分组,key:风叶ID_该组平均转速
		Map<String,List<ShryFyxnData>> xnListMap = new HashMap<String,List<ShryFyxnData>>();
		Long zsAll;	List<ShryFyxnData> eachFyxnList;
		Long curfyid = null; Long curLxdid = null; 
		ShryFyxnData eachFyxn;	
		while(!fyxnList.isEmpty()){
			   zsAll = 0L; 
				curfyid = fyxnList.get(0).getFyid(); curLxdid = fyxnList.get(0).getLxdid();
				eachFyxnList = new ArrayList<ShryFyxnData>();
				for(int idx=0;idx<fyxnList.size();idx++){
						eachFyxn = fyxnList.get(idx);
						if(eachFyxn.getLxdid() == curLxdid){
							zsAll += NumberUtils.toLong(eachFyxn.getZzs());
							eachFyxnList.add(eachFyxn);
							fyxnList.remove(idx);
							idx--;
					}
		    	}
				Long avgZs = zsAll/eachFyxnList.size();
				xnListMap.put(curfyid+"_"+avgZs,eachFyxnList);
		}
		
		//校验缺失的数据记录
		List<String> missDataList = new ArrayList<String>();
		
		//转速步长
		double dZj = 0;	String columnName = "";
		if(dlhzjIn != null){ columnName = "dlhzj"; dZj= dlhzjIn;
		}else	if(fbzjIn != null){ columnName = "fbzj"; dZj = fbzjIn;}
		
		
		String eachJy,eachLl,eachGl,eachZs;
		//筛选缺失数据
		Set<String> keySet = new HashSet<String>();
		for(String chKey : xnListMap.keySet()){
			keySet.add(chKey);
		}
		for(String chKey : keySet){
			List<ShryFyxnData> eachXnList = xnListMap.get(chKey);
			for(ShryFyxnData each : eachXnList){
				eachJy = each.getJyl();
				eachLl = each.getLl();
				eachGl = each.getZgl();
				eachZs = each.getZzs();
				if(StringUtils.isBlank(eachJy) || StringUtils.isBlank(eachLl) || StringUtils.isBlank(eachGl) || StringUtils.isBlank(eachZs) ){
					String fyxh = (String) commonDao.getObjectBySql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+each.getFyid()+"' ").get(0);
					String lxdh = (String) commonDao.getObjectBySql("select lxd.lxdh from shry_syd_data lxd where lxd.lxdid ='"+each.getLxdid()+"' ").get(0);
					missDataList.add("联系单号为"+lxdh+"的风叶性能数据中的部分数据缺失,选型忽略该联系单下的风叶"+fyxh);
					logger.info("联系单号为"+lxdh+"的风叶性能数据中的部分数据缺失,选型忽略该联系单下的风叶"+fyxh);
					xnListMap.remove(chKey);
					break;
				}
				if(NumberUtils.toDouble(eachJy) < 0 || NumberUtils.toDouble(eachLl) < 0 || NumberUtils.toDouble(eachGl) < 0 || NumberUtils.toDouble(eachZs) < 0){
					String fyxh = (String) commonDao.getObjectBySql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+each.getFyid()+"' ").get(0);
					String lxdh = (String) commonDao.getObjectBySql("select lxd.lxdh from shry_syd_data lxd where lxd.lxdid ='"+each.getLxdid()+"' ").get(0);
					missDataList.add("联系单号为"+lxdh+"的风叶性能数据中的部分数据异常(小于0的数据),选型忽略该联系单下的风叶"+fyxh);
					logger.info("联系单号为"+lxdh+"的风叶性能数据中的部分数据缺失,选型忽略该联系单下的风叶"+fyxh);
					xnListMap.remove(chKey);
					break;
				}
			}
		}
		
		//Step 2 数据转换：成待拟合数据
		/*
1.提取样本数据：翻边直径 或 导流环直径 相等,筛选直径范围内的所有风叶性能数据(SQL查询).
S1:假设该风叶只有一组性能数据,以该数据为样本数据
S2:假设该风叶有多组性能数据：S2_1:以n_each/n_min/n_max为参数,选择跨距小的实验转速作为样本数据;以样本数据为基础,换算到转速n;S2_2:样本转速小于n_min,取该转速为n_min;
                            S2_3:样本转速大于n_max,取该转速为n_max
		 */
		//选型参数
		Long arg_zs_bc = NumberUtils.toLong(commonDao.getDictValue(CODETYPE, "FYXX_ZS_BC"));
		arg_zs_bc = arg_zs_bc < 1 ? 50 : arg_zs_bc;
		Long arg_zs_min = NumberUtils.toLong(commonDao.getDictValue(CODETYPE, "FYXX_ZS_MIN"));
		arg_zs_min = arg_zs_min < 1 ? 100 : arg_zs_min;
		Long arg_zs_max = NumberUtils.toLong(commonDao.getDictValue(CODETYPE, "FYXX_ZS_MAX"));
		arg_zs_max = arg_zs_max < 1 ? 8000 : arg_zs_max;
		//在zs_min到zs_max之间按zs_bc切分点
		List<Long> zsDitList = new ArrayList<Long>();
		Long sfCntZs = arg_zs_min;
		while(sfCntZs < arg_zs_max){
			zsDitList.add(sfCntZs);
			sfCntZs += arg_zs_bc;
			if(sfCntZs >= arg_zs_max){ zsDitList.add(arg_zs_max);}
		}
		
		//以风叶ID分组,做样本筛选,数据转换; 数据结构：{风叶ID:[转速]}
		Map<String,List<Long>> fyListMap = new HashMap<String, List<Long>>();
		for(String chKey : xnListMap.keySet()){
			String curFyid =  chKey.split("_")[0];
			String curZs =  chKey.split("_")[1];
			if(fyListMap.get(curFyid) != null && !fyListMap.get(curFyid).isEmpty()){
				fyListMap.get(curFyid).add(NumberUtils.toLong(curZs));
			}else{
				List<Long> chList = new ArrayList<Long>();
				chList.add(NumberUtils.toLong(curZs));
				fyListMap.put(curFyid, chList);
			}
		}
		
		//{风叶ID:{转速:[性能]}}
		Map<String,Map<String,List<ShryFyxnData>>> transZsListMap = new HashMap<String,Map<String,List<ShryFyxnData>>>();
		//{转速:[性能]}
		Map<String,List<ShryFyxnData>> transListMap = new HashMap<String, List<ShryFyxnData>>(); 
		//chKey:风叶ID
		for(String chKey : fyListMap.keySet()){
			if(fyListMap.get(chKey).isEmpty()){continue;}
			//只有一个转速,直接换算所有Dit
			if(fyListMap.get(chKey).size() == 1){
				String chFKey = chKey+"_"+fyListMap.get(chKey).get(0);
				transListMap = this.getFYXNTransList(xnListMap.get(chFKey), toLongArray(zsDitList),chKey);
				transZsListMap.put(chKey, transListMap);
			}else{
				//多个转速,筛选样本点
				List<Long>  zsStrList = new ArrayList<Long>();//样本转速
				zsStrList.addAll(fyListMap.get(chKey));
				Collections.sort(zsStrList);
				//对比最小转速
				if(zsStrList.get(0) < zsDitList.get(0)){
					transListMap.put(zsStrList.get(0)+"", xnListMap.get(chKey+"_"+zsStrList.get(0)));
				}
				//对比最大转速
				if(zsStrList.get(zsStrList.size()-1) > zsDitList.get(zsDitList.size()-1)){
					transListMap.put(zsStrList.get(zsStrList.size()-1)+"", xnListMap.get(chKey+"_"+zsStrList.get(zsStrList.size()-1)));
				}
				//换算
				for(int i=0;i<zsDitList.size();i++){
					Long eachZsDit = zsDitList.get(i);//目标点Dit
					//获取最接近Dit点的样本点
					Long sampZsStr = getNearestZs(eachZsDit,zsStrList);
					//使用样本点进行数据换算
					Map<String,List<ShryFyxnData>> transListMapPt = this.getFYXNTransList(xnListMap.get(chKey+"_"+sampZsStr), new Long[]{eachZsDit},chKey);
					transListMap.putAll(transListMapPt);
				}
				transZsListMap.put(chKey, transListMap);
			}
		}
		
		//Step3 拟合,筛选符合条件数据,记录结果.
		/*
		 3.优先找最接近的实验值 ;目标功率、噪声比输入值小；
    插值获取目标静压下的流量/扭矩/轴功率/效率，如果该流量>=目标值,记录该条数据；循环所有待拟合数据,显示所有符合条件数据。
		 */
		List<ShryFyxnData>[] fyxnListAry = new ArrayList[transZsListMap.keySet().size() * (zsDitList.size()+2)];
		String[][] aArray =  new String[transZsListMap.keySet().size() * (zsDitList.size()+2)][30];
		setDefaultA(aArray);
		int chZsListIdx = 0; 
		Map<String,Integer> zsIndexMap = new HashMap<String,Integer>();//风叶ID_转速对应insertXnRstAry的Index
		for(String chKey:transZsListMap.keySet()){
			transListMap = transZsListMap.get(chKey);
			for(String chZsKey:transListMap.keySet()){
				//准备数据,批量进行插值,速度较快
				//插值拟合到指定静压dJyl时的流量大于dLl即符合条件
					fyxnListAry[chZsListIdx] = transListMap.get(chZsKey);
					aArray[chZsListIdx] = new String[]{dJy+""};
					zsIndexMap.put(chKey+"_"+chZsKey, chZsListIdx);
					chZsListIdx++;
			}
		}
		//批量插值计算 当前只有一个插值点,即 目标静压
		List<ShryFyxnData>[] insertXnRstAry = null;
		try {
			insertXnRstAry = this.convertFYXNInsertChartList(fyxnListAry, aArray);
		} catch (Exception e) {
			logger.error("插值报错,退出！", e);
			return null;
		}
		
		ShryFyxnData insertXnData = null;  Map<String,ShryFyxnData> eachUseMap;
		for(String chKey:transZsListMap.keySet()){
			transListMap = transZsListMap.get(chKey);
			eachUseMap = new HashMap<String,ShryFyxnData>();
			for(String chZsKey:transListMap.keySet()){
				//插值拟合到指定静压dJyl时的流量大于dLl即符合条件
				try {
					//当前只有一个插值点,即 目标静压
					insertXnData = insertXnRstAry[zsIndexMap.get(chKey+"_"+chZsKey)].get(0);
				} catch (Exception e) {
					logger.error("获取插值结果报错,跳过当前数据!chKey="+chKey+"chZsKey="+chZsKey, e);
					continue;
				}
				if(insertXnData ==  null || StringUtils.isBlank(insertXnData.getLl())){
					logger.info("插值数据有误,跳过当前数据!chKey="+chKey+"chZsKey="+chZsKey);
					continue;
				}
				if(NumberUtils.toDouble(insertXnData.getLl()) >= dLl){
					eachUseMap.put(chZsKey, insertXnData);
				}
			}
			//按效率排序取前N个
			eachUseMap = getFirstNXLData(eachUseMap);
			
			
			//{风叶ID:{转速:性能}}
			if(!eachUseMap.isEmpty()){ rstMap.put(chKey, eachUseMap);}
		}
		
		
		
		
		return rstMap;
	}


    //按效率排序取前N个
	private Map<String, ShryFyxnData> getFirstNXLData(Map<String, ShryFyxnData> eachUseMap) {
		Map<String, ShryFyxnData> rstMap = new HashMap<String, ShryFyxnData>();
		int arg_xxjg_xl = NumberUtils.toInt(commonDao.getDictValue(CODETYPE, "FYXX_XXJG_XL"));
		arg_xxjg_xl = arg_xxjg_xl < 1 ? 1 : arg_xxjg_xl;
		Map<Double,String> xlMap = new HashMap<Double,String>();
		for(String ch:eachUseMap.keySet()){
			if(StringUtils.isBlank(ch) || eachUseMap.get(ch)==null){ continue;}
			xlMap.put(NumberUtils.toDouble(eachUseMap.get(ch).getXl()), ch);
		}
		Object[] keyAry = xlMap.keySet().toArray();
		Arrays.sort(keyAry);
		keyAry = Arrays.asList(keyAry).subList(keyAry.length-arg_xxjg_xl, keyAry.length).toArray();
//		keyAry = Arrays.copyOfRange(keyAry, keyAry.length-arg_xxjg_xl, keyAry.length);
		for(Object ch:keyAry){
			rstMap.put(xlMap.get(ch), eachUseMap.get(xlMap.get(ch)));
		}
		
		return rstMap;
	}








	private void setDefaultA(String[][] aArray) {
		for(int i=0;i<aArray.length;i++){
			for(int j=0;j<aArray[i].length;j++){
				aArray[i][j] = "-0.123456";
			}
		}
	}

	private Long[] toLongArray(List<Long> zsDitList) {
		Long[] ary = new Long[zsDitList.size()];
		int i=0;
		for(Long ch : zsDitList){
			ary[i] = ch;
			i++;
		}
		return ary;
	}


	/**
	 * 获取zsStrList中最接近zsDit的点
	 */
	private Long getNearestZs(Long zsDit, List<Long> zsStrList) {
		Long minLgth = 10000L; //跨距
		Long rstZs = 0L;//转速
		for(int j=0;j<zsStrList.size()-1;j++){
			if(Math.abs(zsStrList.get(j) - zsDit) < minLgth){
				minLgth = Math.abs(zsStrList.get(j) - zsDit);
				rstZs = zsStrList.get(j);
			}
		}
		logger.info("getNearestZs---获取最小跨距点：zsDit="+zsDit+",zsStrList="+JSONArray.fromObject(zsStrList)+",rstZs="+rstZs);
		return rstZs;
	}









	/**
	 * 风叶性能数据图 换算
	 * 等比利换算
	 * 根据转速变换 取其他性能参数
	 * @param fyid 风叶id
	 * @param hszsbl 转速换算比利，%数
	 * @param hsdlhzj 导流环直径 换算值
	 * @return {转速:[性能]}
	 */
	public Map<String,List<ShryFyxnData>> getFYXNTransList(List<ShryFyxnData> sampList,Long[] hszsAry,String fyid){
		Map<String,List<ShryFyxnData>> listOutMap = new HashMap<String,List<ShryFyxnData>>();
		if(sampList == null || sampList.isEmpty()){
			logger.info("getFYXNTransList---sampList为空异常退出,fyid="+fyid);
			return listOutMap;
		}
		for(Long chHszs : hszsAry){
			String djy,dll,dzs,dgl,dnj,dxl;
			djy = dll = dzs = dgl = "";
			dzs = chHszs+"";
//			List<ShryFyxnData> chSampList = new ArrayList<ShryFyxnData>();
//			Collections.copy(chSampList, sampList);//TODO 复制失败？？？？
			List<ShryFyxnData> listEach = new ArrayList<ShryFyxnData>();
			for(ShryFyxnData srcEach : sampList){
				ShryFyxnData each = new ShryFyxnData();
				BeanUtils.copyProperties(srcEach, each);
				//静压
				djy = translatePressure(NumberUtils.toDouble(each.getZzs()), 1D, 
						NumberUtils.toDouble(each.getJyl()), chHszs, 1D) +"";
				//流量
				double eachLl = translateQuantity(NumberUtils.toDouble(each.getZzs()), 1D,
						NumberUtils.toDouble(each.getLl()), chHszs,	1D);
				dll = eachLl<0 ? "0" :  (eachLl+"") ;
				//功率
				dgl = translatePower(NumberUtils.toDouble(each.getZzs()), 1D, 
						NumberUtils.toDouble(each.getZgl()), chHszs, 1D) +"";
				//扭矩
				//			扭矩值=9.55*轴功率/主风扇转速
				dnj = (9.55 * NumberUtils.toDouble(dgl)/chHszs) + "";

				//    		效率值=流量*静压/输入/36
				//效率 
				dxl = (NumberUtils.toDouble(dll) * NumberUtils.toDouble(djy) / NumberUtils.toDouble(dgl) / 36 ) +"" ;

				each.setZzs(CommonMethods.formateDouble(dzs,2)); each.setJyl(CommonMethods.formateDouble(djy,2)); 
				each.setLl(CommonMethods.formateDouble(dll,2)); each.setZgl(CommonMethods.formateDouble(dgl,2)); 
				each.setNj(CommonMethods.formateDouble(dnj,2)); each.setXl(CommonMethods.formateDouble(dxl,2));

				listEach.add(each);
			}
			listOutMap.put(chHszs+"", listEach);//TODO 复制失败？？？？
		}
		return listOutMap;
	}
	
	/**
	 *  风叶选型结果的 数据换算
	 * 获取选型结果曲线数据列表
	 * @param sSql
	 * @param jsonObjectIn
	 * @param type
	 * @return
	 */
	public List<ShryFyxnData> getXXJGChartList(String sSql,String jsonObjectIn){
		List<ShryFyxnData> rstList = (List<ShryFyxnData>) commonDao.RunSelectClassBySql(sSql,"net.chinanets.pojos.ShryFyxnData");	
		List<ShryFyxnData> listOut = new ArrayList<ShryFyxnData>();
		JSONObject jsonObjIn = JSONObject.fromObject(jsonObjectIn);
		String stdzs = jsonObjIn.getString("zzs");
		for(ShryFyxnData each : rstList){
			each.setJyl(translatePressure(NumberUtils.toDouble(each.getZzs()), NumberUtils.toDouble(jsonObjIn.getString("dlhzj")), 
					NumberUtils.toDouble(each.getJyl()), NumberUtils.toDouble(stdzs), 
					NumberUtils.toDouble(jsonObjIn.getString("dlhzj")) )+"");
			double eachLl = translateQuantity(NumberUtils.toDouble(each.getZzs()), NumberUtils.toDouble(jsonObjIn.getString("dlhzj")),
					NumberUtils.toDouble(each.getLl()), NumberUtils.toDouble(stdzs),
					NumberUtils.toDouble(jsonObjIn.getString("dlhzj")));
			each.setLl(eachLl<0 ? "0" :  (eachLl+"") );
			listOut.add(each);
		}
		return listOut;
	}
	
	
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
	public Map<String,List<ShryFyxnData>> getFYXNInsertChartList(String sSql,String fyid,Double hszsbl,Double hsdlhzj){
		logger.info("风叶性能数据图 换算 参数:sSql="+sSql+",fyid="+fyid+",hszsbl="+hszsbl+",hsdlhzj="+hsdlhzj);
		Map<String,List<ShryFyxnData>> outMap = new HashMap<String, List<ShryFyxnData>>();
		List<ShryFyxnData> insertList = new ArrayList<ShryFyxnData>();
		List<ShryFyxnData> convertList =  new ArrayList<ShryFyxnData>();
		//是否进行插值计算
		boolean isInsert = "是".equals(super.getDictionaryByKey("INTERP1_PARAMS", "IS_FY_INSERT"));
		String insertAValue = null;
		try{
			convertList= this.getFYXNChartList(sSql, fyid, hszsbl, hsdlhzj);
			if(isInsert && convertList != null &&  convertList.size() > 1){
				//风叶插值指定点,分隔符为,
				insertAValue = super.getDictionaryByKey("INTERP1_PARAMS", "FY_INSERT_VALUE");
				if(StringUtils.isBlank(insertAValue)){ throw new Exception("数据字典(插值指定点)INTERP1_PARAMS.ZC_INSERT_VALUE配置错误-未配置!");}
				String[] insertAry = insertAValue.split(",");
				if(!CommonMethods.isDoubleAry(insertAry)){	throw new Exception("数据字典(插值指定点)INTERP1_PARAMS.ZC_INSERT_VALUE配置错误-格式错误/非数字!");	}

				insertList = convertFYXNInsertChartList(convertList,insertAry);
			}else{
				insertList = convertList;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.info("fyid="+fyid+",是否进行插值计算："+isInsert+",风叶插值指定点："+insertAValue);
		outMap.put("insertList", insertList);
		outMap.put("convertList", convertList);

		return outMap;
	}
	/**
	 * 风叶性能数据插值
	 * @param convertList
	 * @param insertAry
	 * @return
	 * @throws Exception 
	 */
	public List<ShryFyxnData> convertFYXNInsertChartList(List<ShryFyxnData> convertList,String[] insertAry) throws Exception{
		insertAry = convertFyMaxValue(convertList,insertAry);
		List<ShryFyxnData> outList =  new ArrayList<ShryFyxnData>();
		//流量、扭矩、轴功率、效率
		double[] xAry,aAry,yLlAry,yZglAry,yXlAry,yZzsAry,yNjAry;
		xAry = new double[convertList.size()];  aAry = CommonMethods.toDoubleAry(insertAry); yLlAry = new double[convertList.size()]; 
		yZzsAry = new double[convertList.size()]; yZglAry = new double[convertList.size()]; yXlAry = new double[convertList.size()];
		yNjAry = new double[convertList.size()];
		for(int i=0;i<convertList.size();i++){
			ShryFyxnData ch = convertList.get(i);
			xAry[i] = NumberUtils.toDouble(ch.getJyl());
			
			yLlAry[i] = NumberUtils.toDouble(ch.getLl());
//			yZzsAry[i] = NumberUtils.toDouble(ch.getZzs());
			yZglAry[i] = NumberUtils.toDouble(ch.getZgl());
			yXlAry[i] = NumberUtils.toDouble(ch.getXl());
			yNjAry[i] = NumberUtils.toDouble(ch.getNj());
		}
		logger.info("--------------开始调用MatlabInterp1Util.InterpOneX进行风叶插值计算----------------");
		Double[] yVLlAry = MatlabInterp1Util.InterpOneX(xAry, yLlAry, aAry);
		Double[] yVZglAry = MatlabInterp1Util.InterpOneX(xAry, yZglAry, aAry);
		Double[] yVXlAry = MatlabInterp1Util.InterpOneX(xAry, yXlAry, aAry);
		Double[] yVNjAry = MatlabInterp1Util.InterpOneX(xAry, yNjAry, aAry);
		
		//拟合四个值
		for(int i=0;i<aAry.length;i++){
			ShryFyxnData ch = new ShryFyxnData();
			ch.setJyl(new Double(aAry[i]).toString());
			
			ch.setLl(new Double(yVLlAry[i]).toString());
			ch.setZgl(new Double(yVZglAry[i]).toString());
			ch.setXl(new Double(yVXlAry[i]).toString());
			ch.setNj(new Double(yVNjAry[i]).toString());
			ch.setZzs(convertList.get(0).getZzs());
			ch.setFzs(convertList.get(0).getFzs());
			outList.add(ch);
		}
		return outList;
	}
	/**
	 * 风叶性能数据插值--多条批量
	 * @param convertList
	 * @param insertAry
	 * @return
	 * @throws Exception 
	 */
	public List<ShryFyxnData>[] convertFYXNInsertChartList(List<ShryFyxnData>[] convertList,String[][] insertAry) throws Exception{
		if(convertList == null || insertAry == null || convertList.length != insertAry.length || convertList[0].isEmpty() || insertAry[0].length<1){
			logger.info("参数校验失败,请检查插值参数格式!");
			return null;
		}
		List<ShryFyxnData>[] outList =  new ArrayList[convertList.length];
		int convertListLg = 0;
		for(int i=0;i<convertList.length;i++){
			if(convertList[i] == null){continue;}
			convertListLg++;
		}
		convertList = Arrays.copyOfRange(convertList, 0, convertListLg);
		insertAry = Arrays.copyOfRange(insertAry, 0, convertListLg);
		
		insertAry = convertFyMaxValue(convertList,insertAry);
		//流量、扭矩、轴功率、效率
		double[][] xAry,aAry,yLlAry,yZglAry,yXlAry,yZzsAry,yFzsAry,yNjAry;
		int cLg = insertAry.length;
		xAry = new double[cLg][30];  aAry = CommonMethods.toDoubleAry(insertAry); yLlAry = new double[cLg][30]; 
		yZglAry = new double[cLg][30]; yXlAry = new double[cLg][30]; yNjAry = new double[cLg][30];
		yZzsAry = new double[cLg][1]; yFzsAry = new double[cLg][1]; 
		for(int i=0;i<convertList.length;i++){
			Double zzsAll,fzsAll; zzsAll = fzsAll = 0D;
			List<ShryFyxnData> tempList = convertList[i];
			if(tempList == null || tempList.isEmpty()){ continue;}
			for(int j=0;j<tempList.size();j++){
				ShryFyxnData ch = tempList.get(j);
				xAry[i][j] = NumberUtils.toDouble(ch.getJyl());

				yLlAry[i][j] = NumberUtils.toDouble(ch.getLl());
				//			yZzsAry[i] = NumberUtils.toDouble(ch.getZzs());
				yZglAry[i][j] = NumberUtils.toDouble(ch.getZgl());
				yXlAry[i][j] = NumberUtils.toDouble(ch.getXl());
				yNjAry[i][j] = NumberUtils.toDouble(ch.getNj());
				zzsAll += NumberUtils.toDouble(ch.getZzs());
				fzsAll += NumberUtils.toDouble(ch.getFzs());
				if(j == tempList.size()-1){
					xAry[i] = Arrays.copyOfRange(xAry[i], 0, tempList.size());
					yLlAry[i] = Arrays.copyOfRange(yLlAry[i], 0, tempList.size());
					yZglAry[i] = Arrays.copyOfRange(yZglAry[i], 0, tempList.size());
					yXlAry[i] = Arrays.copyOfRange(yXlAry[i], 0, tempList.size());
					yNjAry[i] = Arrays.copyOfRange(yNjAry[i], 0, tempList.size());
					yZzsAry[i][0] =  zzsAll/tempList.size();
					yFzsAry[i][0] =  fzsAll/tempList.size();
				}
			}
		}
		logger.info("--------------开始调用MatlabInterp1Util.InterpMultipX进行风叶插值计算----------------");
		Double[][] yVLlAry = MatlabInterp1Util.InterpMultiX(xAry, yLlAry, aAry);
		Double[][] yVZglAry = MatlabInterp1Util.InterpMultiX(xAry, yZglAry, aAry);
		Double[][] yVXlAry = MatlabInterp1Util.InterpMultiX(xAry, yXlAry, aAry);
		Double[][] yVNjAry = MatlabInterp1Util.InterpMultiX(xAry, yNjAry, aAry);
		
		//拟合四个值
		for(int i=0;i<insertAry.length;i++){
			List<ShryFyxnData> fyxnList = new ArrayList<ShryFyxnData>();
			for(int j=0;j<insertAry[i].length;j++){
				ShryFyxnData ch = new ShryFyxnData();
				ch.setJyl(CommonMethods.formateDouble(aAry[i][j],2));

				ch.setLl(CommonMethods.formateDouble(yVLlAry[i][j],2));
				ch.setZgl(CommonMethods.formateDouble(yVZglAry[i][j],2));
				ch.setXl(CommonMethods.formateDouble(yVXlAry[i][j],2));
				ch.setNj(CommonMethods.formateDouble(yVNjAry[i][j],2));
				ch.setZzs(CommonMethods.formateDouble(yZzsAry[i][0],2));
				ch.setFzs(CommonMethods.formateDouble(yFzsAry[i][0],2));
				fyxnList.add(ch);
			}
			outList[i] = fyxnList;
		}
		return outList;
	}
	/**
	 * 如果实验数据表静压(Pa)最大值大于指定“静压数据”某点数值，仅显示小于该点静压下的数据
	 * 基于insertAry 从小到大排序,非有序则数据显示错误。
	 * @param convertList
	 * @param insertAry
	 * @return
	 */
	private String[] convertFyMaxValue(List<ShryFyxnData> convertList,String[] insertAry) {
		double maxInsert = 0d;
		double maxJy = 0D;
		for(ShryFyxnData ch : convertList){
			if(NumberUtils.toDouble(ch.getJyl()) > maxJy){ maxJy = NumberUtils.toDouble(ch.getJyl());}
		}
		
		int insertI = 0;
		for(int i=0;i<insertAry.length-1;i++){
			if(NumberUtils.toDouble(insertAry[i]) < maxJy && NumberUtils.toDouble(insertAry[i+1]) > maxJy){
				maxInsert = maxJy;//NumberUtils.toDouble(insertAry[i]);
				insertI = i;
			}
		}
		if( maxInsert ==0d){ maxInsert = NumberUtils.toDouble(insertAry[insertAry.length-1]);}
		insertAry = (String[]) ArrayUtils.subarray(insertAry, 0, insertI+1);
		insertAry[insertAry.length-1] = maxInsert+"";
		
		return insertAry;
	}
	//多条曲线批量插值
	//过滤空值
	private String[][] convertFyMaxValue(List<ShryFyxnData>[] convertList,String[][] insertAry) {
		for(int i=0;i<convertList.length;i++){
			if(convertList[i] == null){continue;}
			insertAry[i] = convertFyMaxValue(convertList[i], insertAry[i]);
		}
		return insertAry;
	}
	/**
	 * 风叶性能数据图 换算
	 * 等比利换算
	 * 根据转速变换 取其他性能参数
	 * @param sSql
	 * @param fyid 风叶id
	 * @param hszsbl 转速换算比利，%数
	 * @param hsdlhzj 导流环直径 换算值
	 * @return
	 */
	public List<ShryFyxnData> getFYXNChartList(String sSql,String fyid,Double hszsbl,Double hsdlhzj){
		ShryFyData fyObj =  (ShryFyData) commonDao.getObjectByExample(new ShryFyData(), " fyid = '"+fyid+"'").get(0);
		List<ShryFyxnData> rstList = (List<ShryFyxnData>) commonDao.RunSelectClassBySql(sSql,"net.chinanets.pojos.ShryFyxnData");	
		List<ShryFyxnData> listOut = new ArrayList<ShryFyxnData>();
		
		double dlhzj = NumberUtils.toDouble(fyObj.getDlhzj());
		
		double dhsbl = hszsbl/100;
		if(dhsbl == 1 && dlhzj == hsdlhzj){return rstList;}
		String djy,dll,dzs,dgl,dnj,dxl;
		djy = dll = dzs = dgl = "";
		for(ShryFyxnData each : rstList){
			dzs = (NumberUtils.toDouble(each.getZzs())*dhsbl)+"";
			//静压
			djy = translatePressure(NumberUtils.toDouble(each.getZzs()), NumberUtils.toDouble(fyObj.getDlhzj()), 
					NumberUtils.toDouble(each.getJyl()), NumberUtils.toDouble(each.getZzs())*dhsbl, 
					hsdlhzj) +"";
			//流量
			double eachLl = translateQuantity(NumberUtils.toDouble(each.getZzs()), NumberUtils.toDouble(fyObj.getDlhzj()),
					NumberUtils.toDouble(each.getLl()), NumberUtils.toDouble(each.getZzs())*dhsbl,
					hsdlhzj);
			dll = eachLl<0 ? "0" :  (eachLl+"") ;
			//功率
			dgl = translatePower(NumberUtils.toDouble(each.getZzs()), NumberUtils.toDouble(fyObj.getDlhzj()), 
					NumberUtils.toDouble(each.getZgl()), NumberUtils.toDouble(each.getZzs())*dhsbl, 
					hsdlhzj) +"";
			//扭矩
//			扭矩值=9.55*轴功率/主风扇转速
			dnj = (9.55 * NumberUtils.toDouble(dgl)/NumberUtils.toDouble(dzs)) + "";
			
//    		效率值=流量*静压/输入/36
			//效率 
			dxl = (NumberUtils.toDouble(dll) * NumberUtils.toDouble(djy) / NumberUtils.toDouble(dgl) / 36 ) +"" ;
			
			each.setZzs(CommonMethods.formateDouble(dzs,2)); each.setJyl(CommonMethods.formateDouble(djy,2)); 
			each.setLl(CommonMethods.formateDouble(dll,2)); each.setZgl(CommonMethods.formateDouble(dgl,2)); 
			each.setNj(CommonMethods.formateDouble(dnj,2)); each.setXl(CommonMethods.formateDouble(dxl,2));
			
			listOut.add(each);
		}
		return listOut;
	}
	
	
	
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
	public Map<String,List<ShryZcxnData>> getZCXNInsertChartList(String sSql,String zcid,Double hszsbl,Double hsdlhzj) throws Exception{
		logger.info("总成性能数据图 换算 参数:sSql="+sSql+",zcid="+zcid+",hszsbl="+hszsbl+",hsdlhzj="+hsdlhzj);
		Map<String,List<ShryZcxnData>> outMap = new HashMap<String, List<ShryZcxnData>>();
		List<ShryZcxnData> insertList = new ArrayList<ShryZcxnData>();
		List<ShryZcxnData> convertList= this.getZCXNChartList(sSql, zcid, hszsbl, hsdlhzj);
		//是否进行插值计算
		boolean isInsert = "是".equals(super.getDictionaryByKey("INTERP1_PARAMS", "IS_ZC_INSERT"));
		String insertAValue = null;
		if(isInsert && convertList != null && convertList.size() > 1){
			//总成插值指定点,分隔符为,
			insertAValue = super.getDictionaryByKey("INTERP1_PARAMS", "ZC_INSERT_VALUE");
			if(StringUtils.isBlank(insertAValue)){ throw new Exception("数据字典(插值指定点)INTERP1_PARAMS.ZC_INSERT_VALUE配置错误-未配置!");}
			String[] insertAry = insertAValue.split(",");
			if(!CommonMethods.isDoubleAry(insertAry)){	throw new Exception("数据字典(插值指定点)INTERP1_PARAMS.ZC_INSERT_VALUE配置错误-格式错误/非数字!");	}
			
			insertList = convertZCXNInsertChartList(convertList,insertAry);
			
		}else{
			insertList = convertList;
		}
		logger.info("zcid="+zcid+"是否进行插值计算："+isInsert+"总成插值指定点："+insertAValue);
		outMap.put("insertList", insertList);
		outMap.put("convertList", convertList);
		
		return outMap;
	}
	
	/**
	 * 总成性能数据插值
	 * @param convertList
	 * @param insertAry
	 * @return
	 * @throws Exception 
	 */
	public List<ShryZcxnData> convertZCXNInsertChartList(List<ShryZcxnData> convertList,String[] insertAry) throws Exception{
		insertAry = convertZcMaxValue(convertList,insertAry);
		List<ShryZcxnData> outList =  new ArrayList<ShryZcxnData>();
		//流量、扭矩、轴功率、效率
		double[] xAry,aAry,yLlAry,yZglAry,yXlAry,yZzsAry;
		xAry = new double[convertList.size()];  aAry = CommonMethods.toDoubleAry(insertAry); yLlAry = new double[convertList.size()]; 
		yZzsAry = new double[convertList.size()]; yZglAry = new double[convertList.size()]; yXlAry = new double[convertList.size()];
		for(int i=0;i<convertList.size();i++){
			ShryZcxnData ch = convertList.get(i);
			xAry[i] = NumberUtils.toDouble(ch.getJyl());
			
			yLlAry[i] = NumberUtils.toDouble(ch.getLl());
			yZzsAry[i] = NumberUtils.toDouble(ch.getZzs());
			yZglAry[i] = NumberUtils.toDouble(ch.getSrgl());
			yXlAry[i] = NumberUtils.toDouble(ch.getXl());
		}
		logger.info("--------------开始调用MatlabInterp1Util.InterpOneX进行总成插值计算----------------");
		Double[] yVLlAry = MatlabInterp1Util.InterpOneX(xAry, yLlAry, aAry);
		Double[] yVZzsAry = MatlabInterp1Util.InterpOneX(xAry, yZzsAry, aAry);
		Double[] yVZglAry = MatlabInterp1Util.InterpOneX(xAry, yZglAry, aAry);
		Double[] yVXlAry = MatlabInterp1Util.InterpOneX(xAry, yXlAry, aAry);
		
		//拟合四个值 添加转速插值
		for(int i=0;i<insertAry.length;i++){
			ShryZcxnData ch = new ShryZcxnData();
			ch.setJyl(new Double(aAry[i]).toString());
			
			ch.setLl(new Double(yVLlAry[i]).toString());
			ch.setZzs(new Double(yVZzsAry[i]).toString());
			ch.setSrgl(new Double(yVZglAry[i]).toString());
			ch.setXl(new Double(yVXlAry[i]).toString());
			ch.setFzs(convertList.get(i).getFzs());
			outList.add(ch);
		}
		return outList;
	}
	
	/**
	 * 如果实验数据表静压(Pa)最大值大于指定“静压数据”某点数值，仅显示小于该点静压下的数据
	 * 基于insertAry 从小到大排序,非有序则数据显示错误。
	 * @param convertList
	 * @param insertAry
	 * @return
	 */
	private String[] convertZcMaxValue(List<ShryZcxnData> convertList,String[] insertAry) {
		double maxInsert = 0d;
		double maxJy = 0D;
		for(ShryZcxnData ch : convertList){
			if(NumberUtils.toDouble(ch.getJyl()) > maxJy){ maxJy = NumberUtils.toDouble(ch.getJyl());}
		}
		
		int insertI = 0;
		for(int i=0;i<insertAry.length-1;i++){
			if(NumberUtils.toDouble(insertAry[i]) < maxJy && NumberUtils.toDouble(insertAry[i+1]) > maxJy){
				maxInsert = maxJy;//NumberUtils.toDouble(insertAry[i]);
				insertI = i;
			}
		}
		if( maxInsert ==0d){ maxInsert = NumberUtils.toDouble(insertAry[insertAry.length-1]);}
		insertAry = (String[]) ArrayUtils.subarray(insertAry, 0, insertI+1);
		insertAry[insertAry.length-1] = maxInsert+"";
		
		return insertAry;
	}


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
	public Map<String,List<ShryDjxnData>> getDJXNInsertChartList(String sSql,String djid) throws Exception{
		logger.info("总成性能数据图 换算 参数:sSql="+sSql+",djid="+djid);
		Map<String,List<ShryDjxnData>> outMap = new HashMap<String, List<ShryDjxnData>>();
		List<ShryDjxnData> insertList = new ArrayList<ShryDjxnData>();
		
		ShryDjData djObj =  (ShryDjData) commonDao.getObjectByExample(new ShryDjData(), " djid = '"+djid+"'").get(0);
		String isysdj = djObj.getIsysdj();//无刷电机不插值
		
		List<ShryDjxnData> convertList = (List<ShryDjxnData>) commonDao.RunSelectClassBySql(sSql,"net.chinanets.pojos.ShryDjxnData");
		
		
		//是否进行插值计算
		boolean isInsert = "是".equals(super.getDictionaryByKey("INTERP1_PARAMS", "IS_DJ_INSERT"));
		String insertAValue = null;
		if(isInsert && convertList != null && convertList.size() > 1 && "Y".equalsIgnoreCase(isysdj)){
			//电机插值指定点,分隔符为,
			insertAValue = super.getDictionaryByKey("INTERP1_PARAMS", "DJ_INSERT_VALUE");
			if(StringUtils.isBlank(insertAValue)){ throw new Exception("数据字典(插值指定点)INTERP1_PARAMS.ZC_INSERT_VALUE配置错误-未配置!");}
			String[] insertAry = insertAValue.split(",");
			if(!CommonMethods.isDoubleAry(insertAry)){	throw new Exception("数据字典(插值指定点)INTERP1_PARAMS.ZC_INSERT_VALUE配置错误-格式错误/非数字!");	}

			insertList = convertDJXNInsertChartList(convertList,insertAry);

		}else{
			insertList = convertList;
		}
		logger.info("djid="+djid+"是否进行插值计算："+isInsert+"电机插值指定点："+insertAValue);
		outMap.put("insertList", insertList);
		outMap.put("convertList", convertList);

		return outMap;
	}
	
	/**
	 * 总成性能数据插值
	 * @param convertList
	 * @param insertAry
	 * @return
	 * @throws Exception 
	 */
	public List<ShryDjxnData> convertDJXNInsertChartList(List<ShryDjxnData> convertList,String[] insertAry) throws Exception{
//		insertAry = convertDjMaxValue(convertList,insertAry);
		List<ShryDjxnData> outList =  new ArrayList<ShryDjxnData>();
		//Speed 、 Current、Torque oz-in  通过以Torque为自变量步长0.1插值拟合而来
		double[] xAry,aAry,yLlAry,yZglAry,yXlAry,yZzsAry,yTozAry;
		xAry = new double[convertList.size()];  aAry = CommonMethods.toDoubleAry(insertAry); yLlAry = new double[convertList.size()]; 
		yZzsAry = new double[convertList.size()]; yZglAry = new double[convertList.size()]; yXlAry = new double[convertList.size()];yTozAry = new double[convertList.size()];
		for(int i=0;i<convertList.size();i++){
			ShryDjxnData ch = convertList.get(i);
			xAry[i] = NumberUtils.toDouble(ch.getTorqueNm());//按顺序设值
			
			yLlAry[i] = NumberUtils.toDouble(ch.getSpeed());
//			yZzsAry[i] = NumberUtils.toDouble(ch.getEff());
			yZglAry[i] = NumberUtils.toDouble(ch.getCurrent());
//			yXlAry[i] = NumberUtils.toDouble(ch.getPowOut());
			yTozAry[i] = NumberUtils.toDouble(ch.getTorqueOzIn());
		}
		logger.info("--------------开始调用MatlabInterp1Util.InterpOneX进行电机插值计算----------------");
		Double[] yVLlAry = MatlabInterp1Util.PolyfitX(xAry, yLlAry, aAry);
//		Double[] yVZzsAry = MatlabInterp1Util.InterpOneX(xAry, yZzsAry, aAry);
		Double[] yVZglAry = MatlabInterp1Util.PolyfitX(xAry, yZglAry, aAry);
//		Double[] yVXlAry = MatlabInterp1Util.InterpOneX(xAry, yXlAry, aAry);
		Double[] yVTozAry = MatlabInterp1Util.InterpOneX(xAry, yTozAry, aAry);
		
		for(int i=0;i<insertAry.length;i++){
			ShryDjxnData ch = new ShryDjxnData();
			ch.setTorqueNm(new Double(aAry[i]).toString());
			
			ch.setSpeed(new Double(yVLlAry[i]).toString());
			ch.setCurrent(new Double(yVZglAry[i]).toString());
			ch.setTorqueOzIn(new Double(yVTozAry[i]).toString());
			
			/**
			 其他列数据计算方法：
			“Pow. In”=“Voltage”X“Current”
			“Pow. Out”=“speed”X “Torque”/9.55
			“Eff.”=“Pow. In”/“Pow. Out”
			“Voltage”=“Voltage”
			Input = TorqueNm;
			 */
			ch.setInput(ch.getTorqueNm());
			ch.setVoltage(convertList.get(convertList.size()/2).getVoltage());
			Double powIn = NumberUtils.toDouble(ch.getVoltage())*NumberUtils.toDouble(ch.getCurrent());
			Double powOut = NumberUtils.toDouble(ch.getSpeed())*NumberUtils.toDouble(ch.getTorqueNm())/9.55;
			
			ch.setPowIn(CommonMethods.formateDouble(powIn,2));
			ch.setPowOut(CommonMethods.formateDouble(powOut,2));
			ch.setEff(CommonMethods.formateDouble(powOut/powIn*100,4));
			
			outList.add(ch);
		}
		return outList;
	}
	
	/**
	 * 如果实验数据表静压(Pa)最大值大于指定“静压数据”某点数值，仅显示小于该点静压下的数据
	 * 基于insertAry 从小到大排序,非有序则数据显示错误。
	 * @param convertList
	 * @param insertAry
	 * @return
	 */
	private String[] convertDjMaxValue(List<ShryDjxnData> convertList,String[] insertAry) {
		double maxInsert = 0d;
		double maxJy = 0D;
		for(ShryDjxnData ch : convertList){
			if(NumberUtils.toDouble(ch.getTorqueNm()) > maxJy){ maxJy = NumberUtils.toDouble(ch.getTorqueNm());}
		}
		
		int insertI = 0;
		for(int i=0;i<insertAry.length-1;i++){
			if(NumberUtils.toDouble(insertAry[i]) < maxJy && NumberUtils.toDouble(insertAry[i+1]) > maxJy){
				maxInsert = maxJy;//NumberUtils.toDouble(insertAry[i]);
				insertI = i;
			}
		}
		if( maxInsert ==0d){ maxInsert = NumberUtils.toDouble(insertAry[insertAry.length-1]);}
		insertAry = (String[]) ArrayUtils.subarray(insertAry, 0, insertI+1);
		insertAry[insertAry.length-1] = maxInsert+"";
		
		return insertAry;
	}


	/**
	 * 总成性能数据图 换算
	 * 等比利换算
	 * 根据转速变换 取其他性能参数
	 * @param sSql
	 * @param hsbl 换算比利，%数
	 * @param hszsbl 转速换算比利，%数
	 * @param hsdlhzj 导流环直径 换算值
	 * @return
	 */
	public List<ShryZcxnData> getZCXNChartList(String sSql,String zcid,Double hszsbl,Double hsdlhzj){
		ShryZcData zcObj =  (ShryZcData) commonDao.getObjectByExample(new ShryZcData(), " zcid = '"+zcid+"'").get(0);
		ShryFyData fyObj =  (ShryFyData) commonDao.getObjectByExample(new ShryFyData(), " fyid = '"+zcObj.getFyid()+"'").get(0);
		List<ShryZcxnData> rstList = (List<ShryZcxnData>) commonDao.RunSelectClassBySql(sSql,"net.chinanets.pojos.ShryZcxnData");	
		List<ShryZcxnData> listOut = new ArrayList<ShryZcxnData>();
		
		double dlhzj = NumberUtils.toDouble(fyObj.getDlhzj());
		
		double dhsbl = hszsbl/100;
		if(dhsbl == 1 && dlhzj == hsdlhzj){return rstList;}
		String djy,dll,dzs,dgl,dxl;
		djy = dll = dzs = dgl = "";
		for(ShryZcxnData each : rstList){
			dzs = (NumberUtils.toDouble(each.getZzs())*dhsbl)+"";
			//静压
			djy = translatePressure(NumberUtils.toDouble(each.getZzs()), NumberUtils.toDouble(fyObj.getDlhzj()), 
					NumberUtils.toDouble(each.getJyl()), NumberUtils.toDouble(each.getZzs())*dhsbl, 
					hsdlhzj) +"";
			//流量
			double eachLl = translateQuantity(NumberUtils.toDouble(each.getZzs()), NumberUtils.toDouble(fyObj.getDlhzj()),
					NumberUtils.toDouble(each.getLl()), NumberUtils.toDouble(each.getZzs())*dhsbl,
					hsdlhzj);
			dll = eachLl<0 ? "0" :  (eachLl+"") ;
			//功率
			dgl = translatePower(NumberUtils.toDouble(each.getZzs()), NumberUtils.toDouble(fyObj.getDlhzj()), 
					NumberUtils.toDouble(each.getSrgl()), NumberUtils.toDouble(each.getZzs())*dhsbl, 
					hsdlhzj) +"";
			
//    		效率值=流量*静压/输入/36
	     //效率 
			dxl = (NumberUtils.toDouble(dll) * NumberUtils.toDouble(djy) / NumberUtils.toDouble(dgl) / 36 ) +"" ;
			
			each.setZzs(CommonMethods.formateDouble(dzs,2)); each.setJyl(CommonMethods.formateDouble(djy,2)); 
			each.setLl(CommonMethods.formateDouble(dll,2)); each.setSrgl(CommonMethods.formateDouble(dgl,2)); 
			each.setXl(CommonMethods.formateDouble(dxl,2));
			
			listOut.add(each);
		}
		return listOut;
	}
	
	
/**
 *NOT USED
 * @param jsonArrayStrIn
 * @param  type 转换类型 JL：静压/流量转换
 * @return
 */
public String translateData(String jsonArrayStrIn,String jsonObjectIn,String type){
	if(StringUtils.isBlank(jsonArrayStrIn)){return "";}
	JSONArray jsonAryIn = JSONArray.fromObject(jsonArrayStrIn);
	JSONObject jsonObjIn = JSONObject.fromObject(jsonObjectIn);
	JSONArray jsonAryOut = new JSONArray();
	JSONObject eachJson;
	String stdzs,zzs;//对比标准转速
	stdzs = jsonObjIn.getString("stdzs");
	if("JL".equals(type)){
		for(int i=0;i<jsonAryIn.size();i++){
			eachJson = jsonAryIn.getJSONObject(i);
			if(eachJson == null || eachJson.isEmpty()){ continue; }
//			stdzs = eachJson.getString("stdzs");
			zzs = eachJson.getString("zzs");
			
			eachJson.put("jy", translatePressure(NumberUtils.toDouble(zzs), NumberUtils.toDouble(jsonObjIn.getString("dlhzj")), 
					NumberUtils.toDouble(eachJson.getString("jyl")), NumberUtils.toDouble(stdzs), 
					NumberUtils.toDouble(jsonObjIn.getString("dlhzj")) ));
			eachJson.put("ll", translateQuantity(NumberUtils.toDouble(zzs), NumberUtils.toDouble(jsonObjIn.getString("dlhzj")),
					NumberUtils.toDouble(eachJson.getString("ll")), NumberUtils.toDouble(stdzs), NumberUtils.toDouble(jsonObjIn.getString("dlhzj"))));
			jsonAryOut.add(eachJson);
		}
	}
	return jsonAryOut.toString();
}




	///换算方法集 
	
	//相似换算
	
	
	
	
	/**
	 * 压力反推转速
	 * @param dN
	 * @param dD
	 * @return 结果转速
	 */
	public double reversePressure(double dD1,double dn1,double dP1,double dP2,double dD2){
		if(dP2 <= 0 || dD1 <= 0 || dP1<= 0 || dn1 <= 0 || dD2 <= 0){
			return -1;
		}
		return Arith.mul( Math.sqrt( Arith.div( Arith.div(dP2,dP1) , Math.pow(Arith.div(dD2, dD1), 2) ) ) , dn1 );
	}
	/**
	 * 流量反推转速
	 * @param dN
	 * @param dD
	 * @return 结果转速
	 */
	public double reverseQuantity(double dn1,double dD1,double dD2,double dQ1,double dQ2){
		if(dn1 <= 0 || dD1 <= 0 || dQ1<= 0 || dD2 <= 0 || dQ2 <= 0){
			return -1;
		}
		return  Arith.mul(  Arith.div( Arith.div(dQ2,dQ1) , Math.pow(Arith.div(dD2, dD1), 3) )  , dn1 );
	}
	
	/**
	 * 压力换算
	 * D-导流环直径
	 * n-转速
	 * N-功率/轴功率
	 * P-静压
	 * @param dN
	 * @param dD
	 * @return 结果压力
	 */
	public double translatePressure(double dn1,double dD1,double dP1,double dn2,double dD2){
		if(dn1 <= 0 || dD1 <= 0 || dP1<= 0 || dn2 <= 0 || dD2 <= 0){
			return -1;
		}
		return Arith.mul ( Arith.mul( Math.pow(Arith.div(dn2, dn1), 2),   Math.pow(Arith.div(dD2, dD1), 2) ) , dP1);
	}
	/**
	 * 流量换算
	 * D-导流环直径
	 * n-转速
	 * N-功率/轴功率
	 * P-静压
	 * @param dN
	 * @param dD
	 * @return 结果流量
	 */
	public double translateQuantity(double dn1,double dD1,double dQ1,double dn2,double dD2){
		if(dn1 <= 0 || dD1 <= 0 || dQ1<= 0 || dn2 <= 0 || dD2 <= 0){
			return -1;
		}
		return Arith.mul ( Arith.mul( Arith.div(dn2, dn1) ,   Math.pow(Arith.div(dD2, dD1), 3) ) , dQ1);
		}
	
	/**
	 * 功率换算
	 * D-导流环直径
	 * n-转速
	 * N-功率/轴功率
	 * P-静压
	 * @param dN
	 * @param dD
	 * @return 结果功率
	 */
	public double translatePower(double dn1,double dD1,double dN1,double dn2,double dD2){
		if(dn1 <= 0 || dD1 <= 0 || dN1<= 0 || dn2 <= 0 || dD2 <= 0){
			return -1;
		}
		return Arith.mul ( Arith.mul( Math.pow(Arith.div(dn2, dn1), 3),   Math.pow(Arith.div(dD2, dD1), 5) ) , dN1);
	}
	
	
	
	
	
	
		///主页homeModel的方法
			/**
			 * 获取我的任务
			 * 1.显示所有待审核数据
			 * 2.显示我的申请最近7天已经审核结束数据
			 * 3.超级管理员查看所有人员的1 2 数据
			 * @param userid
			 * @return
			 */
			public String getMyMessions(UserVo userVo){
				/*				String sql1 = " select '报废申请' rwmc,'报废' rwlx,bfdid rwid, CUSERNAME sqr,bfsqzt sqzt from CNSV_BFD WHERE bfsqzt != '已结束' and (cuser="+userid+" or bmldid="+userid+") " +
								"UNION select '领用申请' rwmc,'领用' rwlx,lydid rwid,CUSERNAME sqr,lyzt sqzt from CNSV_LYD WHERE lyzt != '已结束' and (cuser="+userid+" or bmldid="+userid+")" +
								"UNION select '领用申请' rwmc,'领用' rwlx,lydid rwid,CUSERNAME sqr,lyzt sqzt from CNSV_LYD WHERE lyzt != '已结束' and (cuser="+userid+" or bmldid="+userid+") ";
				 */				
		//				String sql = " select '报废审核' rwmc,'报废' rwlx,bfdid rwid,bfdbh rwbh, CUSERNAME sqr,ctime sqsj,bfsqzt sqzt from CNSV_BFD WHERE bfsqzt != '已结束' and bfsqzt != '初稿' and cuser!= "+userVo.getId() +" and (bmldid="+userVo.getId()+" or '41'='"+userVo.getRuleId()+"')" +
		//						"UNION select '领用审核' rwmc,'领用' rwlx,lydid rwid,lydbh rwbh,CUSERNAME sqr,ctime sqsj,lyzt sqzt from CNSV_LYD WHERE lyzt != '已结束' and lyzt != '初稿' and  cuser!= "+userVo.getId() +" and (bmldid="+userVo.getId()+" or '41'='"+userVo.getRuleId()+"')" +
		//						"UNION select '报废申请' rwmc,'报废' rwlx,bfdid rwid,bfdbh rwbh, CUSERNAME sqr,ctime sqsj,bfsqzt sqzt from CNSV_BFD WHERE bfsqzt != '已结束' and cuser=" +userVo.getId()+
		//						"UNION select '领用申请' rwmc,'领用' rwlx,lydid rwid,lydbh rwbh,CUSERNAME sqr,ctime sqsj,lyzt sqzt from CNSV_LYD WHERE lyzt != '已结束' and cuser= "+userVo.getId() +
		//						"UNION select '退库操作' rwmc,'退库' rwlx,tkdid rwid,tkdbh rwbh,CUSERNAME sqr,ctime sqsj,zt sqzt from CNSV_TKD WHERE zt != '审核通过' and (cuser="+userVo.getId()+" or '41'='"+userVo.getRuleId()+"')" ;
			/*	String sql = " select '报废审核' rwmc,'报废' rwlx,bfdid rwid,bfdbh rwbh, CUSERNAME sqr,ctime sqsj,bfsqzt sqzt,utime from CNSV_BFD WHERE  (bfsqzt != '已结束' or ('41'='"+userVo.getRuleId()+"' and bfsqzt = '已结束' and utime > (SYSDATE - 1))) and bfsqzt != '初稿' and cuser!= "+userVo.getId() +" and (bmldid="+userVo.getId()+" or '41'='"+userVo.getRuleId()+"')" +
						" UNION select '领用审核' rwmc,'领用' rwlx,lydid rwid,lydbh rwbh,CUSERNAME sqr,ctime sqsj,lyzt sqzt,utime from CNSV_LYD WHERE  (lyzt != '已结束' or ('41'='"+userVo.getRuleId()+"' and lyzt = '已结束' and utime > (SYSDATE - 1))) and lyzt != '初稿' and  cuser!= "+userVo.getId() +" and (bmldid="+userVo.getId()+" or '41'='"+userVo.getRuleId()+"')" +
						" UNION select '报废申请' rwmc,'报废' rwlx,bfdid rwid,bfdbh rwbh, CUSERNAME sqr,ctime sqsj,bfsqzt sqzt,utime from CNSV_BFD WHERE bfsqzt != '初稿' and  cuser=" +userVo.getId() +" and (bfsqzt != '已结束' or (bfsqzt = '已结束' and utime > (SYSDATE - 7))) "+
						" UNION select '领用申请' rwmc,'领用' rwlx,lydid rwid,lydbh rwbh,CUSERNAME sqr,ctime sqsj,lyzt sqzt,utime from CNSV_LYD WHERE lyzt != '初稿' and cuser= "+userVo.getId() +" and (lyzt != '已结束' or (lyzt = '已结束' and utime > (SYSDATE - 7))) "+
						" UNION select '退库操作' rwmc,'退库' rwlx,tkdid rwid,tkdbh rwbh,CUSERNAME sqr,ctime sqsj,zt sqzt,utime from CNSV_TKD WHERE zt != '初稿' and cuser="+userVo.getId()+" or (('41'='"+userVo.getRuleId()+"' or '44'='"+userVo.getRuleId()+"') and zt='物资管理员审核') order by utime desc" ;
				JSONArray resultAry = super.commonDao.RunSelectJSONArrayBySql(sql, null);
				return resultAry.toString();*/
				return null;
			}
			
			/**
			 * 获取我的最新任务
			 * 1.由自己审核/提交的即时新任务
			 * 2.由自己提交的最近1天审核已结束的任务
			 * @param userid
			 * @return
			 */
			public String getMyLatestMessions(UserVo userVo){
/*				String sql = " select '报废审核' rwmc,'报废' rwlx,bfdid rwid,bfdbh rwbh, CUSERNAME sqr,ctime sqsj,bfsqzt sqzt,utime from CNSV_BFD WHERE (bfsqzt != '已结束' or ('41'='"+userVo.getRuleId()+"' and bfsqzt = '已结束' and utime > (SYSDATE - 1))) and bfsqzt != '初稿' and cuser!= "+userVo.getId() +" and (bmldid="+userVo.getId()+" or '41'='"+userVo.getRuleId()+"')" +
						" UNION select '领用审核' rwmc,'领用' rwlx,lydid rwid,lydbh rwbh,CUSERNAME sqr,ctime sqsj,lyzt sqzt,utime from CNSV_LYD WHERE (lyzt != '已结束' or ('41'='"+userVo.getRuleId()+"' and lyzt = '已结束' and utime > (SYSDATE - 1))) and lyzt != '初稿' and  cuser!= "+userVo.getId() +" and (bmldid="+userVo.getId()+" or '41'='"+userVo.getRuleId()+"')" +
						" UNION select '报废申请' rwmc,'报废' rwlx,bfdid rwid,bfdbh rwbh, CUSERNAME sqr,ctime sqsj,bfsqzt sqzt,utime from CNSV_BFD WHERE  cuser=" +userVo.getId() + " and utime > (SYSDATE - 1) and bfsqzt != '初稿' " +
						" UNION select '领用申请' rwmc,'领用' rwlx,lydid rwid,lydbh rwbh,CUSERNAME sqr,ctime sqsj,lyzt sqzt,utime from CNSV_LYD WHERE  cuser= "+userVo.getId() + " and  utime > (SYSDATE - 1) and lyzt != '初稿' " +
						" UNION select '退库操作' rwmc,'退库' rwlx,tkdid rwid,tkdbh rwbh,CUSERNAME sqr,ctime sqsj,zt sqzt,utime from CNSV_TKD WHERE zt != '初稿' and  utime > (SYSDATE - 1) and (cuser="+userVo.getId()+" or '41'='"+userVo.getRuleId()+"') order by utime desc" ;*/
				String sql = "  select '报废审核' rwmc,'报废' rwlx,wfd.bfdid rwid,wfd.bfdbh rwbh, wfd.CUSERNAME sqr,wfd.ctime sqsj,wfd.bfsqzt sqzt,nt.uusername shr,wfd.bmldid,nt.ntid,nt.utime from CNSV_BFD wfd inner join CNSV_NOTICE nt ON wfd.bfdid = nt.wfid and nt.nttype='BFSQ-SH' and (nt.tzrid="+userVo.getId()+" or 41="+userVo.getRuleId() +")"+
						"  UNION select '领用审核' rwmc,'领用' rwlx,wfd.lydid rwid,wfd.lydbh rwbh,wfd.CUSERNAME sqr,wfd.ctime sqsj,wfd.lyzt sqzt,nt.uusername shr,wfd.bmldid,nt.ntid,nt.utime from CNSV_LYD wfd inner join CNSV_NOTICE nt ON wfd.lydid = nt.wfid and nt.nttype='LYSQ-SH' and (nt.tzrid="+userVo.getId() +" or 41="+userVo.getRuleId() +")"+
						"  UNION select '报废申请' rwmc,'报废' rwlx,wfd.bfdid rwid,wfd.bfdbh rwbh, wfd.CUSERNAME sqr,wfd.ctime sqsj,wfd.bfsqzt sqzt,nt.uusername shr,wfd.bmldid,nt.ntid,nt.utime from CNSV_BFD wfd inner join CNSV_NOTICE nt ON wfd.bfdid = nt.wfid and  nt.nttype='BFSQ-SQ' and nt.tzrid="+userVo.getId() +
						"  UNION select '领用申请' rwmc,'领用' rwlx,wfd.lydid rwid,wfd.lydbh rwbh,wfd.CUSERNAME sqr,wfd.ctime sqsj,wfd.lyzt sqzt,nt.uusername shr,wfd.bmldid,nt.ntid,nt.utime from CNSV_LYD wfd inner join CNSV_NOTICE nt ON wfd.lydid = nt.wfid and  nt.nttype='LYSQ-SQ' and nt.tzrid="+userVo.getId() +
						"  UNION select '退库操作' rwmc,'退库' rwlx,wfd.tkdid rwid,wfd.tkdbh rwbh,wfd.CUSERNAME sqr,wfd.ctime sqsj,wfd.zt sqzt,nt.uusername shr,0 bmldid,nt.ntid,nt.utime from CNSV_TKD wfd inner join CNSV_NOTICE nt ON wfd.tkdid = nt.wfid and (nt.tzrid="+userVo.getId() +")"+ 
						"  order by utime desc";
				JSONArray resultAry = super.commonDao.RunSelectJSONArrayBySql(sql, null);
		//		return resultAry.toString();
				return null;
			}
	
			/**
			 * 获取数量不足提醒数量
			 * @param userId 当前用户ID
			 * @return
			 */
			public String getMyLatestRyhcMsgs(Long userid){
				String sql = "select * from CNSV_NOTICE nt inner join cnsv_ryhc ryhc on ryhc.ryhcid=nt.wfid and nt.isread=1 and tzrid="+userid+" order by nt.utime desc " ;
				JSONArray resultAry = super.commonDao.RunSelectJSONArrayBySql(sql, null);
				return resultAry!= null ? resultAry.toString() : "";
			}
	
	/**
	 * 获取 主页 物资总体购买情况
	 */
	public Map getAssetBuyInfo(int yearStr){
		Map countMap = new HashMap();
	/*	if((yearStr+"").length() != 4)return null;
		String xxsbSql = "SELECT count(0) FROM (" +
				"SELECT GZRQ FROM ASSET_CABLE" +
				" UNION ALL" +
				" SELECT GZRQ  FROM  ASSET_COMPUTER" +
				"  UNION ALL" +
				" SELECT GZRQ  FROM  ASSET_JZ" +
				"  UNION ALL" +
				" SELECT GZRQ   FROM  ASSET_MODULE" +
				"  UNION ALL" +
				" SELECT  GZRQ  FROM  ASSET_NET" +
				"  UNION ALL" +
				" SELECT  GZRQ  FROM  ASSET_SERVER" +
				"  UNION ALL" +
				" SELECT GZRQ   FROM  ASSET_STORAGE" +
				"  UNION ALL" +
				" SELECT GZRQ   FROM  ASSET_UPS" +
				"  UNION ALL" +
				" SELECT GZRQ   FROM  ASSET_VIDEO" +
				"  UNION ALL" +
				" SELECT  GZRQ  FROM  ASSET_WSSB ) WHERE instr(GZRQ,'"+yearStr+"')>0 ";
		String wwclpSql = "SELECT count(0) FROM TB_HERITAGE WHERE instr(TO_CHAR(GZRQ,'YYYY-MM-DD'),'"+yearStr+"')>0 ";
		String ryhcSql = "SELECT count(0) FROM CNST_RYHCMX_DATA WHERE instr(TO_CHAR(CTIME,'YYYY-MM-DD'),'"+yearStr+"')>0 ";
		String jjyjSql = "SELECT count(0) FROM T_FURNITURE WHERE instr(TO_CHAR(GZRQ,'YYYY-MM-DD'),'"+yearStr+"')>0 ";
		String tsdaSql = "SELECT count(0) FROM T_BOOK WHERE instr(TO_CHAR(GZRQ,'YYYY-MM-DD'),'"+yearStr+"')>0 ";
		
	
		List rstList = super.getObjectBySql(xxsbSql);
		countMap.put("xxsb", rstList.get(0));
		
		rstList = super.getObjectBySql(wwclpSql);
		countMap.put("wwclp", rstList.get(0));
		
		rstList = super.getObjectBySql(ryhcSql);
		countMap.put("ryhc", rstList.get(0));
		
		rstList = super.getObjectBySql(jjyjSql);
		countMap.put("jjyj", rstList.get(0));
		
		rstList = super.getObjectBySql(tsdaSql);
		countMap.put("tsda", rstList.get(0));
	*/
		return countMap;
	}
	/**
	 * 获取日用耗材使用情况
	 */
	public List getXXSBInfo(){
		int curYear = (new Date()).getYear()+1900;
		String ryhcSql = "";
		int year;
		for(int i=2008;i<=curYear;i++){
			year = i;
			ryhcSql += "SELECT "+year+",count(0) count FROM CNST_RYHCMX_DATA WHERE instr(TO_CHAR(CTIME,'YYYY-MM-DD'),'"+year+"')>0 ";
			if(i<curYear){
				ryhcSql += " UNION ALL ";
			}
		}
		List rstList = super.getObjectBySql(ryhcSql);
		return rstList;
	}
	/**
	 * 获取所有物资数量信息 
	 * @return
	 */
	public Map getAssetAllCountInfo(){
		String xxsbSql = "SELECT count(0) FROM (" +
				"SELECT GZRQ FROM ASSET_CABLE" +
				" UNION ALL" +
				" SELECT GZRQ  FROM  ASSET_COMPUTER" +
				"  UNION ALL" +
				" SELECT GZRQ  FROM  ASSET_JZ" +
				"  UNION ALL" +
				" SELECT GZRQ   FROM  ASSET_MODULE" +
				"  UNION ALL" +
				" SELECT  GZRQ  FROM  ASSET_NET" +
				"  UNION ALL" +
				" SELECT  GZRQ  FROM  ASSET_SERVER" +
				"  UNION ALL" +
				" SELECT GZRQ   FROM  ASSET_STORAGE" +
				"  UNION ALL" +
				" SELECT GZRQ   FROM  ASSET_UPS" +
				"  UNION ALL" +
				" SELECT GZRQ   FROM  ASSET_VIDEO" +
				"  UNION ALL" +
				" SELECT  GZRQ  FROM  ASSET_WSSB )";
		String wwclpSql = "SELECT count(0) FROM TB_HERITAGE";
		String ryhcSql = "SELECT count(0) FROM CNST_RYHCMX_DATA ";
		String jjyjSql = "SELECT count(0) FROM T_FURNITURE ";
		String tsdaSql = "SELECT count(0) FROM T_BOOK ";
		
		Map countMap = new HashMap();
		List rstList = super.getObjectBySql(xxsbSql);
		countMap.put("xxsb", rstList.get(0));
		
		rstList = super.getObjectBySql(wwclpSql);
		countMap.put("wwclp", rstList.get(0));
		
		rstList = super.getObjectBySql(ryhcSql);
		countMap.put("ryhc", rstList.get(0));
		
		rstList = super.getObjectBySql(jjyjSql);
		countMap.put("jjyj", rstList.get(0));
		
		rstList = super.getObjectBySql(tsdaSql);
		countMap.put("tsda", rstList.get(0));
	
		return countMap;
		
	}
	
	///报表方法
	
	/**
	 * 所有部门每个季度耗材领用状况
	 * @param year
	 * @return
	 */
	public List<Map> getBmhclyqk(int year){
		List<Map> resultList = new ArrayList<Map>();
		String sqlodrQ = " select t1.id deptid,t1.mc deptname,t1.show_order showorder, " +
				"t2.wpzje firwpzje,t2.sfzsl firsfzsl,t2.countcs fircountcs, " +
				"t3.wpzje secwpzje,t3.sfzsl secsfzsl,t3.countcs seccountcs, " +
				"t4.wpzje thirwpzje,t4.sfzsl thirsfzsl,t4.countcs thircountcs, " +
				"t5.wpzje fourwpzje,t5.sfzsl foursfzsl,t5.countcs fourcountcs " +
				"from dept t1 " +
				"left join cnsv_report_bmjdlyzk t2 on  t1.mc=t2.slbmname and t2.sljd='"+year+"-1' " +
				"left join cnsv_report_bmjdlyzk t3 on  t1.mc=t3.slbmname and t3.sljd='"+year+"-2' " +
				"left join cnsv_report_bmjdlyzk t4 on  t1.mc=t4.slbmname and t4.sljd='"+year+"-3' " +
				"left join cnsv_report_bmjdlyzk t5 on  t1.mc=t5.slbmname and t5.sljd='"+year+"-4' " +
				"order by showorder ";
		List<DataEntity> selectList = commonDao.RunSelectDataEntityBySql(sqlodrQ, null);
		for(DataEntity eachMap : selectList){
			resultList.add(eachMap.GetValueMap());
		}
		return resultList;
	}
	
	
	

}
