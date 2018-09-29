package net.chinanets.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;



@SuppressWarnings("unchecked")
public class ChartServiceImp extends CommonServiceImp implements ChartService {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
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
		
		
//		
//		CnstWfstepData wfStepData=(CnstWfstepData)JsonHelper.GetBeanByJsonString(selectionJson, CnstWfstepData.class);
//		if (wfStepData == null) {
//			doResult.setRetCode(Errors.INVALIDDATA);
//			doResult.setErrorInfo("请求数据无效");
//		}
		//翻边直径、导流环直径误差范围
		String sFw = "0";
		
		String strSQL = "select  fyxn.* from shry_fyxn_data fyxn  ";
		List<Object> queryList ;
		double dValMin,dValMax; dValMin = dValMax = 0;
		if(!StringUtils.isBlank(sFbzj)){
			queryList = commonDao.getObjectBySql("select attribute1 from cnst_codelist_data where codebs='FFZJ_FW' limit 1");
			if(queryList != null && !queryList.isEmpty()){ sFw = queryList.get(0)+""; }
			dValMin = NumberUtils.toDouble(sFbzj) - 0.0001  - NumberUtils.toDouble(sFw);
			dValMax = NumberUtils.toDouble(sFbzj) + NumberUtils.toDouble(sFw);
			strSQL += "where fyxn.fyid in ( select fy.fyid from shry_fy_data fy where fy.fbzj  between '"+dValMin+"' and '"+dValMax+"' )  ";
		}else if(!StringUtils.isBlank(sDlhzj)){
			queryList = commonDao.getObjectBySql("select attribute1 from cnst_codelist_data where codebs='DLHZJ_FW' limit 1");
			if(queryList != null && !queryList.isEmpty()){ sFw = queryList.get(0)+""; }
			dValMin = NumberUtils.toDouble(sDlhzj) - 0.0001 - NumberUtils.toDouble(sFw);
			dValMax = NumberUtils.toDouble(sDlhzj) + NumberUtils.toDouble(sFw);
			strSQL += "where fyxn.fyid in ( select fy.fyid from shry_fy_data fy where fy.dlhzj between '"+dValMin+"' and '"+dValMax+"' )  )  ";
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
		List<String> xxjgList = new ArrayList<String>();
		try {
			xxjgList = doFySelection(fyxnList,NumberUtils.toDouble(sJy),NumberUtils.toDouble(sLl),
					NumberUtils.toDouble(sGl),NumberUtils.toDouble(sZs),dDlhzj,dFbzj);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		String fyids = "'',";
		String caseWhenSql = ", case when '' then '' ";
		for(String eachStr:xxjgList){
			fyids += " '"+eachStr.split(";")[0]+"',";
			caseWhenSql += " when fyid='"+eachStr.split(";")[0]+"' then '"+eachStr.split(";")[1]+"' ";
		}
		caseWhenSql += " end as stdzs ";
		
		String caseWhenSql2 = ", case when '' then '' ";
		for(String eachStr:xxjgList){
		//	fyids += " '"+eachStr.split(";")[0]+"',";
			caseWhenSql2 += " when fyid='"+eachStr.split(";")[0]+"' then '"+eachStr.split(";")[2]+"' ";
		}
		caseWhenSql2 += " end as stdgl ";
		
		if(fyids.endsWith(",")){ fyids = fyids.substring(0,fyids.length()-1);}
		JSONArray xxjgObjList = commonDao.RunSelectJSONArrayBySql("select * "+caseWhenSql+caseWhenSql2+" from shry_fy_data where fyid in ("+fyids+") ", null);
		//electClassBySql("select * from shry_fy_data where fyid in ("+fyids+") ", "net.chinanets.pojos.ShryFyData.ShryFyData");
		//(new ShryFyData(), " fyid in  ("+fyids+")");
		if(xxjgObjList == null){xxjgObjList = new JSONArray();}
		System.out.println("\r\n\r\n\r\n---------------------------风叶选型结束,找到 "+xxjgObjList.size()+" 个符合条件的数据---------------------------------\r\n\r\n\r\n");
		
		JSONObject result=new JSONObject();
		result.put("itemtotal", xxjgObjList.size());
		result.put("othermsg", "");
		result.put("items", xxjgObjList.toString());
		return result.toString();
		
/*		String selectResultHql=StringHelper.Format("SELECT ROWNUM AS RN,TMPT.* %1$s",strHQL);
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
		return result.toString();*/
	//	return doResult.GetJSONByDoResult();
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 返回满足条件的fyid List
	 * 使用默认的噪声数据进行选型 fyid为4的数据
	 * @param fyxnList
	 * @return
	 */
	private List<String> doFySelection(List<ShryFyxnData> fyxnList,double dJy,double dLl,double dGl,double dZs,Double dlhzjIn,Double fbzjIn) {
		/**
 --结构参数：1.翻边直径  2.导流环直径 差值正负10
一般情况下选型时可能以上两个参数只会2选1输入

--性能参数：1.静压  2.流量  3.功率 4.噪声
选型要求：
0.翻边直径 或 导流环直径 相等
1.同静压点流量值大于输入值（120pa 2600m³/h）或者同流量点静压大于输入值（2500m³/h 130pa）；
2.目标功率、噪声比输入值小
3.优先找最接近的实验值
		 */
		List<String> fylist = new ArrayList<String>();
		
		if(fyxnList == null || fyxnList.isEmpty()){ return fylist; }
		
		int stepRange = 20;
		double dZj = 0;
		//Step 1:按风叶ID分组 
		List<List<ShryFyxnData>> xnListGroup = new ArrayList<List<ShryFyxnData>>();
		List<ShryFyxnData> eachFyxnList;
		Long curfyid = null;  
		
		String columnName = "";
		if(dlhzjIn != null){ columnName = "dlhzj"; dZj= dlhzjIn;
		}else	if(fbzjIn != null){ columnName = "fbzj"; dZj = fbzjIn;}
		
		ShryFyxnData eachFyxn;	
		while(!fyxnList.isEmpty()){
				curfyid = fyxnList.get(0).getFyid(); 
				eachFyxnList = new ArrayList<ShryFyxnData>();
				for(int idx=0;idx<fyxnList.size();idx++){
						eachFyxn = fyxnList.get(idx);
						if(eachFyxn.getFyid() == curfyid){
							eachFyxnList.add(eachFyxn);
							fyxnList.remove(idx);
					}
		    	}
				xnListGroup.add(eachFyxnList);
		}
		
		//[不使用这种方式]   通过增大/减小转速 实现选型， 转速步长50+-
		// 读取 转速1-50时不满主流量和静压都大于输入值的点 转速1为最佳转速 
		//结果返回该结果转速
		
		/*
		选型要求：
	1.同静压时流量值大于输入值（120pa 2600m³/h）或者同流量时静压大于输入值（2500m³/h 130pa）；
	2.同时满足目标功率、噪声比输入值小
	
	实现方式:
	1.通过目标流量获取[转速1]  在通过转速1推出其他值做对比 
	2.通过目标静压获取[转速2]  在通过转速2推出其他值做对比
	3.同时满足目标功率、噪声比输入值小 
	4.满足以上条件后，在推出一个[使目标点在曲线右上方的]转速  
	((当前转速 - 转速1)%50 > 25 ? 1 : 0 + (int)(当前转速 - 转速1)/50) * 50) * 当前转速 
		 */
		
		//缺失的数据记录
		List<String> missDataList = new ArrayList<String>();
		
		//Step 2:获取初始转速值
		String eachJy,eachLl,eachGl,eachZs;
		label1:	for(List<ShryFyxnData> eachXnList : xnListGroup){
			//数据校验
			for(ShryFyxnData each : eachXnList){
				eachJy = each.getJyl();
				eachLl = each.getLl();
				eachGl = each.getZgl();
				eachZs = each.getZzs();
				if(StringUtils.isBlank(eachJy) || StringUtils.isBlank(eachLl) || StringUtils.isBlank(eachGl) || StringUtils.isBlank(eachZs) ){
					String fyxh = (String) commonDao.getObjectBySql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+each.getFyid()+"' ").get(0);
					String lxdh = (String) commonDao.getObjectBySql("select lxd.lxdh from shry_syd_data lxd where lxd.lxdid ='"+each.getLxdid()+"' ").get(0);
					missDataList.add("联系单号为"+lxdh+"的风叶性能数据中的部分数据缺失,选型忽略该联系单下的风叶"+fyxh);
					continue label1;
				}
				if(NumberUtils.toDouble(eachJy) < 0 || NumberUtils.toDouble(eachJy) < 0 || NumberUtils.toDouble(eachJy) < 0 || NumberUtils.toDouble(eachJy) < 0){
					String fyxh = (String) commonDao.getObjectBySql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+each.getFyid()+"' ").get(0);
					String lxdh = (String) commonDao.getObjectBySql("select lxd.lxdh from shry_syd_data lxd where lxd.lxdid ='"+each.getLxdid()+"' ").get(0);
					missDataList.add("联系单号为"+lxdh+"的风叶性能数据中的部分数据异常(小于0的数据),选型忽略该联系单下的风叶"+fyxh);
					continue label1;
				}
			}
			
			//Step 2.1:获取直径参数
			String dlhzj  = (String) commonDao.getObjectBySql("SELECT fy."+columnName+" FROM shry_fy_data fy WHERE fy.FYID ='"+eachXnList.get(0).getFyid()+"' ").get(0);
			
			ShryFyxnData startFyxhData = null;
			double minJyRange,minLlRange;
			minJyRange = minLlRange = 100000000;
			
			//Step 2.2:获取初始转速
			//一定 满足 同静压时流量值大于输入值或者同流量时静压大于输入值,套公式改转速
			//取两个值同时大于且最接近于 目标 的值 为起始值
			//Step 2.2.1: 基于已有性能数据 获取初始转速
			for(ShryFyxnData each : eachXnList){
				eachJy = each.getJyl();
				eachLl = each.getLl();
				if(NumberUtils.toDouble(eachJy) >= dJy && NumberUtils.toDouble(eachLl) >= dLl 
						&& ( (NumberUtils.toDouble(eachJy) - dJy) < minJyRange || (NumberUtils.toDouble(eachLl) - dLl) < minLlRange )){
					startFyxhData = each;
					minJyRange = NumberUtils.toDouble(eachJy) - dJy;
					minLlRange = NumberUtils.toDouble(eachLl) - dLl;
				}
			}
			if(startFyxhData == null){
			//TODO Step 2.2.2:基于推算数据 获取初始转速  可推算的最大转速设定为4500 
			//暂时不考虑 默认风叶试验数据会覆盖到这个尽量大的范围
				
				continue; }
			
			//Step 3: 获取该风叶型号 噪声参考 数据
			ShryFyZsData objExp = new ShryFyZsData();
//			objExp.setFyid(eachXnList.get(0).getFyid());
			objExp.setFyid(4l);//当前版本噪声数据非必须数据  使用默认数据作为参考
			
			List<ShryFyZsData> fyzsList = commonDao.getObjectByExample(objExp); //("select * from shry_fy_zs_data fyzs where fyzs.fyid='"+eachXnList.get(0).getFyid()+"' ");
			double x[] = new double[fyzsList.size()];
			double y[] = new double[fyzsList.size()];
			int i=0;
			if(fyzsList == null || fyzsList.isEmpty()){ continue;}
			for(ShryFyZsData each:fyzsList){
				if(StringUtils.isBlank(each.getSpeed()) || StringUtils.isBlank(each.getNoise())){ 
					String fyxh = (String) commonDao.getObjectBySql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+eachXnList.get(0).getFyid()+"' ").get(0);
					missDataList.add("风叶噪声性能数据中的部分数据异常,选型忽略该风叶"+fyxh);
					continue label1;
					}
				x[i] = NumberUtils.toDouble(each.getSpeed());
				y[i] = NumberUtils.toDouble(each.getNoise());
				i++;
			}
			
			//Step 4.1: 推算噪声数据  并 根据初始性能数据 反推两个临界转速
			//跳出条件为 不满足 [同静压时流量值大于输入值 且 者同流量时静压大于输入值]
			int startZs = Integer.parseInt(startFyxhData.getZzs());
			int startIndex = eachXnList.indexOf(startFyxhData);
			double dEachJy,dEachLl,dEachGl,dEachZs;
			double zsAry[] = {reversePressure(NumberUtils.toDouble(dlhzj), startZs, NumberUtils.toDouble(startFyxhData.getJyl()), dJy, NumberUtils.toDouble(dlhzj)),
					                    reverseQuantity(startZs, NumberUtils.toDouble(dlhzj), NumberUtils.toDouble(dlhzj), NumberUtils.toDouble(startFyxhData.getLl()), dLl)  };
			int ict = 0;   // 0 : 同静压反推转速  1 : 同流量反推转速
			//获取较小的功率
			double dEachGl_part = 0;	int fyid_part = -1; double dSpeed_part = -1; //满足条件的结果
			for(double zs : zsAry ){
				dEachJy = translatePressure(startZs, NumberUtils.toDouble(dlhzj), NumberUtils.toDouble(startFyxhData.getJyl()), zs, NumberUtils.toDouble(dlhzj));
				dEachLl = translateQuantity(startZs, NumberUtils.toDouble(dlhzj), NumberUtils.toDouble(startFyxhData.getLl()), zs, NumberUtils.toDouble(dlhzj));
				dEachGl = translatePower(startZs, NumberUtils.toDouble(dlhzj), NumberUtils.toDouble(startFyxhData.getZgl()), zs, NumberUtils.toDouble(dlhzj));

				//目标转速的记录, 显示到前台 zs
				//Step 4.2:使用最小二乘法 拟合曲线并求值, 满足情况1或情况2都可
				double dCurZs = Guass.getGuassValue(x,y,zs);
					if(dCurZs < dZs && dEachGl <= dGl ){
						if(ict == 0 && dEachLl > dLl){ //情况1:同静压时流量值大于输入值
							dEachGl_part = dEachGl;
							fyid_part = eachXnList.get(0).getFyid().intValue() ;
							dSpeed_part = zs;
//							xxrst_part = eachXnList.get(0).getFyid().intValue() +";"+(float)(zs);
//							fylist.add(xxrst_part);
						}else if(ict == 1 && dEachJy > dJy){//情况2: 同流量时静压大于输入值 
							if(dEachGl_part < dEachGl){//较小功率对比
								dEachGl_part = dEachGl;
								fyid_part = eachXnList.get(0).getFyid().intValue() ;
								dSpeed_part = zs;
//								xxrst_part = eachXnList.get(0).getFyid().intValue() +";"+(float)(zs);
							}
					    }
					ict++;
				}
			}
			
			//TODO Step 5: 从起始值开始 以步长stepRange 递减 进行推算
			//当前数据满足选型条件时 继续推算至最小转速,记录推算过程中的数据,   汇总对比取 [功率最小] 的转速
			//暂不考虑效率 没有效率换算公式
			//命中的数据 进行最优功率选择   满足静压和流量大于目标值  translatePressure(double dn1, double dD1, double dP1, double dn2, double dD2)
			if(fyid_part != -1){
				double minSpeed_part,minGl_part; minSpeed_part = minGl_part = 0;
				for(double zs = dSpeed_part ; zs > 0 ; zs -= stepRange ){
					dEachJy = translatePressure(startZs, NumberUtils.toDouble(dlhzj), NumberUtils.toDouble(startFyxhData.getJyl()), zs, NumberUtils.toDouble(dlhzj));
					dEachLl = translateQuantity(startZs, NumberUtils.toDouble(dlhzj), NumberUtils.toDouble(startFyxhData.getLl()), zs, NumberUtils.toDouble(dlhzj));
					dEachGl = translatePower(startZs, NumberUtils.toDouble(dlhzj), NumberUtils.toDouble(startFyxhData.getZgl()), zs, NumberUtils.toDouble(dlhzj));
					
//					double dCurZs = Guass.getGuassValue(x,y,zs);//暂不考虑噪声
					if(dEachGl <= dGl && ( (dEachJy >= dJy && dEachLl>=  dLl))){
						if(dEachGl < dEachGl_part){
							dEachGl_part = dEachGl; dSpeed_part = zs;
						}
					}else{
						break;
					}
			 }
				//记入选型结果
				fylist.add(fyid_part  +";"+ dSpeed_part+";"+dEachGl_part);
			}
			}
		//过滤结果集合
		List<String> fylist_rst = new ArrayList<String>();
		String eachFyid="";
		List<String> fylist_temp = new ArrayList<String>();
		for(String each:fylist){fylist_temp.add(each); }
		for(String each:fylist){
			eachFyid = each.split(";")[0];
			for(String eachSn:fylist_temp){
				if(eachFyid.equals(eachSn.split(";")[0]) && NumberUtils.toDouble(each.split(";")[2]) > NumberUtils.toDouble(eachSn.split(";")[2])){
					fylist_temp.remove(each); break;
				}
			}
		}
		fylist_rst = fylist_temp;
		
		return fylist_rst;
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
		String stdzs = jsonObjIn.getString("stdzs");
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
	public Map<String,List<ShryFyxnData>> getFYXNInsertChartList(String sSql,String fyid,Double hszsbl,Double hsdlhzj) throws Exception{
		logger.info("风叶性能数据图 换算 参数:sSql="+sSql+",fyid="+fyid+",hszsbl="+hszsbl+",hsdlhzj="+hsdlhzj);
		Map<String,List<ShryFyxnData>> outMap = new HashMap<String, List<ShryFyxnData>>();
		List<ShryFyxnData> insertList = new ArrayList<ShryFyxnData>();
		List<ShryFyxnData> convertList= this.getFYXNChartList(sSql, fyid, hszsbl, hsdlhzj);
		//是否进行插值计算
		boolean isInsert = "是".equals(super.getDictionaryByKey("INTERP1_PARAMS", "IS_FY_INSERT"));
		String insertAValue = null;
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
		
		//拟合四个值 添加转速插值
		for(int i=0;i<insertAry.length;i++){
			ShryFyxnData ch = new ShryFyxnData();
			ch.setJyl(new Double(aAry[i]).toString());
			
			ch.setLl(new Double(yVLlAry[i]).toString());
			ch.setZgl(new Double(yVZglAry[i]).toString());
			ch.setXl(new Double(yVXlAry[i]).toString());
			ch.setNj(new Double(yVNjAry[i]).toString());
			ch.setZzs(convertList.get(i).getZzs());
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
