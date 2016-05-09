package net.chinanets.service.imp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import net.chinanets.data.DataEntity;
import net.chinanets.entity.CnstWfstepData;
import net.chinanets.pojos.ShryFyData;
import net.chinanets.pojos.ShryFyZsData;
import net.chinanets.pojos.ShryFyxnData;
import net.chinanets.pojos.ShryZcData;
import net.chinanets.pojos.ShryZcxnData;
import net.chinanets.service.ChartService;
import net.chinanets.utils.Arith;
import net.chinanets.utils.CommonMethods;
import net.chinanets.utils.Guass;
import net.chinanets.utils.common.DoResult;
import net.chinanets.utils.common.Errors;
import net.chinanets.utils.helper.JsonHelper;
import net.chinanets.vo.Chart;
import net.chinanets.vo.UserVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@SuppressWarnings("unchecked")
public class ChartServiceImp extends CommonServiceImp implements ChartService {
	
	
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
			dValMin = Double.parseDouble(sFbzj) - 0.0001  - Double.parseDouble(sFw);
			dValMax = Double.parseDouble(sFbzj) + Double.parseDouble(sFw);
			strSQL += "where fyxn.fyid in ( select fy.fyid from shry_fy_data fy where fy.fbzj  between '"+dValMin+"' and '"+dValMax+"' )  ";
		}else if(!StringUtils.isBlank(sDlhzj)){
			queryList = commonDao.getObjectBySql("select attribute1 from cnst_codelist_data where codebs='DLHZJ_FW' limit 1");
			if(queryList != null && !queryList.isEmpty()){ sFw = queryList.get(0)+""; }
			dValMin = Double.parseDouble(sDlhzj) - 0.0001 - Double.parseDouble(sFw);
			dValMax = Double.parseDouble(sDlhzj) + Double.parseDouble(sFw);
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
		
		Double dFbzj = StringUtils.isBlank(sFbzj) ? null : Double.parseDouble(sFbzj);
		Double dDlhzj = StringUtils.isBlank(sDlhzj) ? null : Double.parseDouble(sDlhzj);
		
		//执行选型
		List<String> xxjgList = doFySelection(fyxnList,Double.parseDouble(sJy),Double.parseDouble(sLl),
				Double.parseDouble(sGl),Double.parseDouble(sZs),dDlhzj,dFbzj);
		String fyids = "'',";
		String caseWhenSql = ", case when '' then '' ";
		for(String eachStr:xxjgList){
			fyids += " '"+eachStr.split(";")[0]+"',";
			caseWhenSql += " when fyid='"+eachStr.split(";")[0]+"' then '"+eachStr.split(";")[1]+"' ";
		}
		caseWhenSql += " end as stdzs ";
		if(fyids.endsWith(",")){ fyids = fyids.substring(0,fyids.length()-1);}
		JSONArray xxjgObjList = commonDao.RunSelectJSONArrayBySql("select * "+caseWhenSql+" from shry_fy_data where fyid in ("+fyids+") ", null);
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
		
		int stepRange = 50;
		double dZj = 0;
		//按风叶ID分组 
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
		
		//缺失数据的 基础参数
		List<String> missDataList = new ArrayList<String>();
		//1.获取初始转速值
		String eachJy,eachLl,eachGl,eachZs;
		label1:	for(List<ShryFyxnData> eachXnList : xnListGroup){
			//数据校验
			for(ShryFyxnData each : eachXnList){
				eachJy = each.getJyl();
				eachLl = each.getLl();
				eachGl = each.getZgl();
				eachZs = each.getZzs();
				if(StringUtils.isBlank(eachJy) || StringUtils.isBlank(eachLl) || StringUtils.isBlank(eachGl) || StringUtils.isBlank(eachZs) ){
					String fyxh = (String) commonDao.getOneColumnValueByHql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+each.getFyid()+"' ");
					String lxdh = (String) commonDao.getOneColumnValueByHql("select lxd.lxdh from shry_syd_data lxd where lxd.lxdid= ='"+each.getLxdid()+"' ");
					missDataList.add("联系单号为"+lxdh+"的风叶性能数据中的部分数据缺失,选型忽略该联系单下的风叶"+fyxh);
					continue label1;
				}
				if(Double.parseDouble(eachJy) < 0 || Double.parseDouble(eachJy) < 0 || Double.parseDouble(eachJy) < 0 || Double.parseDouble(eachJy) < 0){
					String fyxh = (String) commonDao.getOneColumnValueByHql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+each.getFyid()+"' ");
					String lxdh = (String) commonDao.getOneColumnValueByHql("select lxd.lxdh from shry_syd_data lxd where lxd.lxdid= ='"+each.getLxdid()+"' ");
					missDataList.add("联系单号为"+lxdh+"的风叶性能数据中的部分数据异常(小于0的数据),选型忽略该联系单下的风叶"+fyxh);
					continue label1;
				}
			}
			String dlhzj  = (String) commonDao.getObjectBySql("SELECT fy."+columnName+" FROM shry_fy_data fy WHERE fy.FYID ='"+eachXnList.get(0).getFyid()+"' ").get(0);
			
			ShryFyxnData startFyxhData = null;
			double minJyRange,minLlRange;
			minJyRange = minLlRange = 100000000;
			//一定 满足 同静压时流量值大于输入值或者同流量时静压大于输入值,套公式改转速
			//取两个值同时大于且最接近于 目标 的值 为起始值
			for(ShryFyxnData each : eachXnList){
				eachJy = each.getJyl();
				eachLl = each.getLl();
				if(Double.parseDouble(eachJy) > dJy && Double.parseDouble(eachLl) > dLl 
						&& ( (Double.parseDouble(eachJy) - dJy) < minJyRange || (Double.parseDouble(eachLl) - dLl) < minLlRange )){
					startFyxhData = each;
					minJyRange = Double.parseDouble(eachJy) - dJy;
					minLlRange = Double.parseDouble(eachLl) - dLl;
				}
			}
			if(startFyxhData == null){ continue; }
			//获取风叶 流量-静压 数据
			ShryFyZsData objExp = new ShryFyZsData();
			objExp.setFyid(eachXnList.get(0).getFyid());
			List<ShryFyZsData> fyzsList = commonDao.getObjectByExample(objExp);//("select * from shry_fy_zs_data fyzs where fyzs.fyid='"+eachXnList.get(0).getFyid()+"' ");
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
				x[i] = Double.parseDouble(each.getSpeed());
				y[i] = Double.parseDouble(each.getNoise());
				i++;
			}
			
			//从起始值开始 以步长stepRange 递减 进行推算
			//跳出条件为 不满足 [同静压时流量值大于输入值 或 者同流量时静压大于输入值]
			int startZs = Integer.parseInt(startFyxhData.getZzs());
			int startIndex = eachXnList.indexOf(startFyxhData);
			double dEachJy,dEachLl,dEachGl,dEachZs;
			double zsAry[] = {reversePressure(Double.parseDouble(dlhzj), startZs, Double.parseDouble(startFyxhData.getZgl()), dGl, Double.parseDouble(dlhzj)),
					                    reverseQuantity(startZs, Double.parseDouble(dlhzj), Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getZgl()), dGl)  };
			int ict = 0;// 0 : 同静压反推转速  1 : 同流量反推转速
			//获取较小的功率
			double dEachGl1 = 0;
			String sStr1 = "";
			for(double zs : zsAry ){
				dEachJy = translatePressure(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getJyl()), zs, Double.parseDouble(dlhzj));
				dEachLl = translateQuantity(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getLl()), zs, Double.parseDouble(dlhzj));
				dEachGl = translatePower(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getZgl()), zs, Double.parseDouble(dlhzj));

				//TODO 目标转速的记录转换，显示到前台 zs
				//使用最小二乘法 拟合曲线并求值
				double dCurZs = Guass.getGuassValue(x,y,zs);
					if(dCurZs < dZs && dEachGl < dGl ){
						if(ict == 0 && dEachLl > dLl){ //同静压时流量值大于输入值
							dEachGl1 = dEachGl;
//							sStr1 = "fyid="+eachXnList.get(0).getFyid()+";zzs="+zs+";jy="+dEachJy+";ll="+dEachLl+";gl="+dEachGl+";zs="+dCurZs+";";
							fylist.add(eachXnList.get(0).getFyid().intValue() +";"+(float)(zs));
					//		fylist.add("fyid="+eachXnList.get(0).getFyid()+";zzs="+zs+";jy="+dEachJy+";ll="+dEachLl+";gl="+dEachGl+";zs="+dCurZs+";");
						}else if(ict == 1 && dEachJy > dJy){// 同流量时静压大于输入值 
							fylist.add(eachXnList.get(0).getFyid().intValue() +";"+(float)(zs));
							//fylist.add("fyid="+eachXnList.get(0).getFyid()+";zzs="+zs+";jy="+dEachJy+";ll="+dEachLl+";gl="+dEachGl+";zs="+dCurZs+";");
							/*if(dEachGl1 < dEachGl){//较小功率对比
								fylist.add(sStr1);
							}else{
							fylist.add("fyid="+eachXnList.get(0).getFyid()+";zzs="+zs+";jy="+dEachJy+";ll="+dEachLl+";gl="+dEachGl+";zs="+dCurZs+";");
							}*/
					    }
					ict++;
				}
			}
			
		/*	for(double zs = startZs ; zs > 0 ; zs -= 50 ){
				dEachJy = translatePressure(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getJyl()), zs, Double.parseDouble(dlhzj));
				dEachLl = translateQuantity(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getLl()), zs, Double.parseDouble(dlhzj));
				dEachGl = translatePower(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getZgl()), zs, Double.parseDouble(dlhzj));
				
				double dCurZs = Guass.getGuassValue(x,y,zs);
				if(dCurZs < dZs && dEachGl < dGl && ( () || () )){
					
				}
			}*/
			}
		
		
		return fylist;
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
			each.setJyl(translatePressure(Double.parseDouble(each.getZzs()), Double.parseDouble(jsonObjIn.getString("dlhzj")), 
					Double.parseDouble(each.getJyl()), Double.parseDouble(stdzs), 
					Double.parseDouble(jsonObjIn.getString("dlhzj")) )+"");
			double eachLl = translateQuantity(Double.parseDouble(each.getZzs()), Double.parseDouble(jsonObjIn.getString("dlhzj")),
					Double.parseDouble(each.getLl()), Double.parseDouble(stdzs),
					Double.parseDouble(jsonObjIn.getString("dlhzj")));
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
	 * @param hsbl 换算比利，%数
	 * @return
	 */
	public List<ShryFyxnData> getFYXNChartList(String sSql,String fyid,Double hszsbl,Double hsdlhzj){
		ShryFyData fyObj =  (ShryFyData) commonDao.getObjectByExample(new ShryFyData(), " fyid = '"+fyid+"'").get(0);
		List<ShryFyxnData> rstList = (List<ShryFyxnData>) commonDao.RunSelectClassBySql(sSql,"net.chinanets.pojos.ShryFyxnData");	
		List<ShryFyxnData> listOut = new ArrayList<ShryFyxnData>();
		
		double dlhzj = Double.parseDouble(fyObj.getDlhzj());
		
		double dhsbl = hszsbl/100;
		if(dhsbl == 1 && dlhzj == hsdlhzj){return rstList;}
		String djy,dll,dzs,dgl,dnj,dxl;
		djy = dll = dzs = dgl = "";
		for(ShryFyxnData each : rstList){
			dzs = (Double.parseDouble(each.getZzs())*dhsbl)+"";
			//静压
			djy = translatePressure(Double.parseDouble(each.getZzs()), Double.parseDouble(fyObj.getDlhzj()), 
					Double.parseDouble(each.getJyl()), Double.parseDouble(each.getZzs())*dhsbl, 
					hsdlhzj) +"";
			//流量
			double eachLl = translateQuantity(Double.parseDouble(each.getZzs()), Double.parseDouble(fyObj.getDlhzj()),
					Double.parseDouble(each.getLl()), Double.parseDouble(each.getZzs())*dhsbl,
					hsdlhzj);
			dll = eachLl<0 ? "0" :  (eachLl+"") ;
			//功率
			dgl = translatePower(Double.parseDouble(each.getZzs()), Double.parseDouble(fyObj.getDlhzj()), 
					Double.parseDouble(each.getZgl()), Double.parseDouble(each.getZzs())*dhsbl, 
					hsdlhzj) +"";
			//扭矩
//			扭矩值=9.55*轴功率/主风扇转速
			dnj = (9.55 * Double.parseDouble(dgl)/Double.parseDouble(dzs)) + "";
			
//    		效率值=流量*静压/输入/36
			//效率 
			dxl = (Double.parseDouble(dll) * Double.parseDouble(djy) / Double.parseDouble(dgl) / 36 ) +"" ;
			
			each.setZzs(dzs); each.setJyl(djy); each.setLl(dll); each.setZgl(dgl); each.setNj(dnj); each.setXl(dxl);
			
			listOut.add(each);
		}
		return listOut;
	}
	
	/**
	 * 总成性能数据图 换算
	 * 等比利换算
	 * 根据转速变换 取其他性能参数
	 * @param sSql
	 * @param hsbl 换算比利，%数
	 * @return
	 */
	public List<ShryZcxnData> getZCXNChartList(String sSql,String zcid,Double hszsbl,Double hsdlhzj){
		ShryZcData zcObj =  (ShryZcData) commonDao.getObjectByExample(new ShryZcData(), " zcid = '"+zcid+"'").get(0);
		ShryFyData fyObj =  (ShryFyData) commonDao.getObjectByExample(new ShryFyData(), " fyid = '"+zcObj.getFyid()+"'").get(0);
		List<ShryZcxnData> rstList = (List<ShryZcxnData>) commonDao.RunSelectClassBySql(sSql,"net.chinanets.pojos.ShryZcxnData");	
		List<ShryZcxnData> listOut = new ArrayList<ShryZcxnData>();
		
		double dlhzj = Double.parseDouble(fyObj.getDlhzj());
		
		double dhsbl = hszsbl/100;
		if(dhsbl == 1 && dlhzj == hsdlhzj){return rstList;}
		String djy,dll,dzs,dgl,dxl;
		djy = dll = dzs = dgl = "";
		for(ShryZcxnData each : rstList){
			dzs = (Double.parseDouble(each.getZzs())*dhsbl)+"";
			//静压
			djy = translatePressure(Double.parseDouble(each.getZzs()), Double.parseDouble(fyObj.getDlhzj()), 
					Double.parseDouble(each.getJyl()), Double.parseDouble(each.getZzs())*dhsbl, 
					hsdlhzj) +"";
			//流量
			double eachLl = translateQuantity(Double.parseDouble(each.getZzs()), Double.parseDouble(fyObj.getDlhzj()),
					Double.parseDouble(each.getLl()), Double.parseDouble(each.getZzs())*dhsbl,
					hsdlhzj);
			dll = eachLl<0 ? "0" :  (eachLl+"") ;
			//功率
			dgl = translatePower(Double.parseDouble(each.getZzs()), Double.parseDouble(fyObj.getDlhzj()), 
					Double.parseDouble(each.getSrgl()), Double.parseDouble(each.getZzs())*dhsbl, 
					hsdlhzj) +"";
			
//    		效率值=流量*静压/输入/36
	     //效率 
			dxl = (Double.parseDouble(dll) * Double.parseDouble(djy) / Double.parseDouble(dgl) / 36 ) +"" ;
			
			each.setZzs(dzs); each.setJyl(djy); each.setLl(dll); each.setSrgl(dgl); each.setXl(dxl);
			
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
			
			eachJson.put("jy", translatePressure(Double.parseDouble(zzs), Double.parseDouble(jsonObjIn.getString("dlhzj")), 
					Double.parseDouble(eachJson.getString("jyl")), Double.parseDouble(stdzs), 
					Double.parseDouble(jsonObjIn.getString("dlhzj")) ));
			eachJson.put("ll", translateQuantity(Double.parseDouble(zzs), Double.parseDouble(jsonObjIn.getString("dlhzj")),
					Double.parseDouble(eachJson.getString("ll")), Double.parseDouble(stdzs), Double.parseDouble(jsonObjIn.getString("dlhzj"))));
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
	
	
	/**
	 * 年份与项目数
	 * 
	 * @return
	 */
	public List yearCount() {
		String sql = "select nf,count(nf) from project_xm  group by nf order by nf asc";
		return findObject(sql);
	}

	/**
	 * 项目与金额
	 * 
	 * @return
	 */
	public List xmJe(String s) {
		String sql = "select xmmc,je from project_xm " + s;
		return findObject(sql);
	}

	/**
	 * 项目与日期
	 * 
	 * @return
	 */
	public List xmZq(String s) {
		String sql = "select  xmmc,ROUND(MONTHS_BETWEEN(TO_DATE(wcrq,'yyyy-MM-dd'),TO_DATE(ssrq,'yyyy-MM-dd'))) from project_xm  ";
		return findObject(sql + s);
	}

	// 获得所有项目年份
	public List getNf() {
		String sql = "select distinct nf from project_xm  order by nf desc";
		return super.getObjectBySql(sql);
	}

	private List findObject(String sql) {
		List lt = super.getObjectBySql(sql);
		List ary = new ArrayList();
		for (Iterator iterator = lt.iterator(); iterator.hasNext();) {
			Object[] obj = (Object[]) iterator.next();
			Chart cr = new Chart();
			cr.setName(obj[0].toString());
			cr.setCount(Double.parseDouble((obj[1].toString())));
			ary.add(cr);
		}
		return ary;
	}

	/*
	 * 获得提示项目信息
	 * 
	 * @see net.chinanets.service.ChartService#getProject()
	 */
	public String getClockInfo(String time) {
		String hql = "From ProjectXm xm  where xm.wcrq > '" + time + "' ";
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		List xmList = super.getAllObjectByHql(hql);

		Document document = org.dom4j.DocumentHelper.createDocument();
		Element root = document.addElement("root");

		/*for (Iterator iterator = xmList.iterator(); iterator.hasNext();) {
			ProjectXm xm = (ProjectXm) iterator.next();

			XmProcess pro = new XmProcess();
			pro.setParentID(xm.getId());
			List proList = super.getObjectList(pro);
			for (Iterator proIt = proList.iterator(); proIt.hasNext();) {
				XmProcess p = (XmProcess) proIt.next();
				String bzTime = p.getBztime();
				if (bzTime != null) {
					try {
						c1.setTime(fmt.parse(bzTime));
						c2.setTime(fmt.parse(time));
						long l1 = c1.getTimeInMillis();
						long l2 = c2.getTimeInMillis();
						// 计算天数
						long day = (l1 - l2) / (24 * 60 * 60 * 1000);
						if (day < xm.getStage() && day > 0) {
							Element node = root.addElement("node");
							node.addAttribute("xmName", xm.getXmmc());
							node.addAttribute("xmID", xm.getId().toString());
							node.addAttribute("xmWcrq", xm.getWcrq());
							node.addAttribute("xmBz", p.getBzmc());
							node.addAttribute("xmTime", p.getBztime());
							node.addAttribute("xmDay", String.valueOf(day));
							node.addAttribute("zt", p.getZt().toString());
							node.addAttribute("pid", p.getId().toString());
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}*/

		// net.chinanets.utils.ChinaNetsUtil.writXml(document);
		return document.asXML();
	}
	
	public void changeProcessZt(Long id){
		/*XmProcess p=(XmProcess) super.getObjectById(new XmProcess(), id);
		p.setZt(Long.parseLong("1"));
		super.updateObject(p);*/
	}
	
	public List<Map<String, String>> getXjjlChart(String dateRange){
		if(dateRange == null || (dateRange.length()!=4 && dateRange.length() != 7))return null;
		List<Map<String, String>> rstList= new ArrayList<Map<String,String>>();
	/*	Map<String, String> hashMap;
		Xjjl xjjlVo = new Xjjl();
		String xjsj;
		if(dateRange.length() == 4){
			for(int i=1;i<=12;i++){
				hashMap = new LinkedHashMap<String, String>(); 
				xjsj = dateRange + "-"+ (i<10 ? "0"+i : i)+"%";
				xjjlVo.setXjsj(xjsj);
				xjjlVo.setXjjglx("正常");
				hashMap.put("month",i+"月");
				hashMap.put("normal",super.getCountByObject(xjjlVo)+"");
				xjjlVo.setXjjglx("异常");
				hashMap.put("unnormal",super.getCountByObject(xjjlVo)+"");
				rstList.add(hashMap);
			}
		}else 	if(dateRange.length() == 7){
			for(int i=1;i<=31;i++){
				hashMap = new LinkedHashMap<String, String>(); 
				xjsj = dateRange + "-"+ (i<10 ? "0"+i : i)+"%";
				xjjlVo.setXjsj(xjsj);
				xjjlVo.setXjjglx("正常");
				hashMap.put("day",i+"日");
				hashMap.put("normal",super.getCountByObject(xjjlVo)+"");
				xjjlVo.setXjjglx("异常");
				hashMap.put("unnormal",super.getCountByObject(xjjlVo)+"");
				rstList.add(hashMap);
			}
		}*/
		return rstList;
	}

}
