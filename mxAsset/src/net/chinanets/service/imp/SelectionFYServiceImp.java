package net.chinanets.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.chinanets.entity.CnstWfstepData;
import net.chinanets.pojos.ShryFyZsData;
import net.chinanets.pojos.ShryFyxnData;
import net.chinanets.utils.Arith;
import net.chinanets.utils.Guass;
import net.chinanets.utils.common.DoResult;
import net.chinanets.utils.common.Errors;
import net.chinanets.utils.helper.JsonHelper;
import net.chinanets.utils.helper.StringHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SelectionFYServiceImp extends CommonServiceImp{
//
//	public String selectFYAction(String selectionJson){
//		DoResult doResult = new DoResult();
//		doResult.setRetCode(Errors.OK);
//		String sFbzj,sDlhzj,sJy,sLl,sGl,sZs,sFzid;
//		
//		JSONObject jsonObj = JSONObject.fromObject(selectionJson);
//		sFbzj = jsonObj.getString("fbzj");
//		sDlhzj = jsonObj.getString("dlhzj");
//		sJy = jsonObj.getString("jy");
//		sLl = jsonObj.getString("ll");
//		sGl = jsonObj.getString("gl");
//		sZs = jsonObj.getString("zs");
//		sFzid = jsonObj.getString("fzid");
//		
//		
//		// 查询翻边直径选型范围-导流环直径选型范围
//		String sFFZJ_FW = commonDao.getObjectBySql("  SELECT attribute1 FROM cnst_codelist_data   WHERE CODETYPE='FYXX_PARAMS' and CODEBS='FFZJ_FW'  ").get(0)+"";
//		
//		String sDLHZJ_FW = commonDao.getObjectBySql("   SELECT attribute1 FROM cnst_codelist_data   WHERE CODETYPE='FYXX_PARAMS' and CODEBS='DLHZJ_FW' ").get(0)+"";
//		
//		CnstWfstepData wfStepData=(CnstWfstepData)JsonHelper.GetBeanByJsonString(selectionJson, CnstWfstepData.class);
//		if (wfStepData == null) {
//			doResult.setRetCode(Errors.INVALIDDATA);
//			doResult.setErrorInfo("请求数据无效");
//		}
//		
//		String strSQL = "select  fyxn.* from shry_fyxn_data fyxn  ";
//		if(!StringUtils.isBlank(sFbzj)){
//			strSQL += "where fyxn.fyid in ( select fy.fyid from shry_fy_data fy where fy.fbzj >='"+(Double.parseDouble(sFbzj) - Double.parseDouble(sFFZJ_FW) )+"' and fy.fbzj <= '"
//		+(Double.parseDouble(sFbzj) +Double.parseDouble(sFFZJ_FW) )+"')  ";
//		}else if(!StringUtils.isBlank(sDlhzj)){
//			strSQL += "where fyxn.fyid in ( select fy.fyid from shry_fy_data fy where fy.dlhzj>='"+(Double.parseDouble(sDlhzj) - Double.parseDouble(sDLHZJ_FW) )+"'  and fy.dlhzj<='"
//		+(Double.parseDouble(sDlhzj) - Double.parseDouble(sDLHZJ_FW) )+"' )  ";
//		}else if(StringUtils.isBlank(sDlhzj) && StringUtils.isBlank(sDlhzj) && !StringUtils.isBlank(sFzid) ){//分组数据 暂不使用
//			strSQL += "where fyxn.fyid in ( select fz.glid from shry_xxfz_data fz where fz.fzid='"+sFzid+"' )  ";
//		}
//		strSQL += " order by fyxn.fyid,fyxn.fyxnid ";
//		
//		String tempClassPath = "net.chinanets.pojos.ShryFyxnData";
//		
//		System.out.println("---------------------------风叶选型开始---------------------------------");
//		System.out.println("查询SQL："+strSQL);
//		
//		List<ShryFyxnData> fyxnList =	(List<ShryFyxnData>) commonDao.RunSelectClassBySql(strSQL, tempClassPath);
//		System.out.println("搜索范围："+fyxnList.size() +" 条数据！");
//		
//		String dlhzjIn = "";
//		String fbzjIn = "";
//		
//		List<String> xxjgList = doFySelection(fyxnList,Double.parseDouble(sJy),Double.parseDouble(sLl),
//				Double.parseDouble(sGl),Double.parseDouble(sZs),Double.parseDouble(dlhzjIn),Double.parseDouble(fbzjIn));
//		
///*		String selectResultHql=StringHelper.Format("SELECT ROWNUM AS RN,TMPT.* %1$s",strHQL);
//		JSONArray tempJson=this.RunSelectJSONArrayBySql(selectResultHql, tempPageSize, tempPageCurrent);
//		String total=this.RunSelectCountBySql(selectResultCount, null)+"";
//		String items="";
//		if(tempJson!=null && tempJson.size()>0){
//			items=tempJson.toString();
//		}
//		JSONObject result=new JSONObject();
//		result.put("itemtotal", total);
//		result.put("othermsg", "");
//		result.put("items", items);
//		return result.toString();*/
//		
//		
//		System.out.println("---------------------------风叶选型结束---------------------------------");
//		
//		return doResult.GetJSONByDoResult();
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	/**
//	 * @param fyxnList
//	 * @return
//	 */
//	private List<String> doFySelection(List<ShryFyxnData> fyxnList,double dJy,double dLl,double dGl,double dZs,Double dlhzjIn,Double fbzjIn) {
//		/**
// --结构参数：1.翻边直径  2.导流环直径 差值正负10
//一般情况下选型时可能以上两个参数只会2选1输入
//
//--性能参数：1.静压  2.流量  3.功率 4.噪声
//选型要求：
//0.翻边直径 或 导流环直径 相等
//1.同静压点流量值大于输入值（120pa 2600m³/h）或者同流量点静压大于输入值（2500m³/h 130pa）；
//2.目标功率、噪声比输入值小
//3.优先找最接近的实验值
//		 */
//		List<String> fylist = new ArrayList<String>();
//		int stepRange = 50;
//		double dZj = 0;
//		//按风叶ID分组 
//		List<List<ShryFyxnData>> xnListGroup = new ArrayList<List<ShryFyxnData>>();
//		List<ShryFyxnData> eachFyxnList;
//		Long curfyid = null;  
//		
//		String columnName = "";
//		if(dlhzjIn != null){ columnName = "dlhzj"; dZj= dlhzjIn;
//		}else	if(fbzjIn != null){ columnName = "fbzj"; dZj = fbzjIn;}
//		
//		ShryFyxnData eachFyxn;
//		while(!fyxnList.isEmpty()){
//				curfyid = fyxnList.get(0).getFyid(); 
//				eachFyxnList = new ArrayList<ShryFyxnData>();
//				for(int idx=0;idx<fyxnList.size();idx++){
//						eachFyxn = fyxnList.get(idx);
//						if(eachFyxn.getFyid() == curfyid){
//							eachFyxnList.add(eachFyxn);
//							fyxnList.remove(idx);
//					}
//		    	}
//				xnListGroup.add(eachFyxnList);
//		}
//		
//		//[不使用这种方式]   通过增大/减小转速 实现选型， 转速步长50+-
//		// 读取 转速1-50时不满主流量和静压都大于输入值的点 转速1为最佳转速 
//		//结果返回该结果转速
//		
//		/*
//		选型要求：
//	1.同静压时流量值大于输入值（120pa 2600m³/h）或者同流量时静压大于输入值（2500m³/h 130pa）；
//	2.同时满足目标功率、噪声比输入值小
//	
//	实现方式:
//	1.通过目标流量获取[转速1]  在通过转速1推出其他值做对比 
//	2.通过目标静压获取[转速2]  在通过转速2推出其他值做对比
//	3.同时满足目标功率、噪声比输入值小 
//	4.满足以上条件后，在推出一个[使目标点在曲线右上方的]转速  
//	((当前转速 - 转速1)%50 > 25 ? 1 : 0 + (int)(当前转速 - 转速1)/50) * 50) * 当前转速 
//		 */
//		
//		//缺失数据的 基础参数
//		List<String> missDataList = new ArrayList<String>();
//		//1.获取初始转速值
//		String eachJy,eachLl,eachGl,eachZs;
//		label1:	for(List<ShryFyxnData> eachXnList : xnListGroup){
//			//数据校验
//			for(ShryFyxnData each : eachXnList){
//				eachJy = each.getJyl();
//				eachLl = each.getLl();
//				eachGl = each.getZgl();
//				eachZs = each.getZzs();
//				if(StringUtils.isBlank(eachJy) || StringUtils.isBlank(eachLl) || StringUtils.isBlank(eachGl) || StringUtils.isBlank(eachZs) ){
//					String fyxh = (String) commonDao.getOneColumnValueByHql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+each.getFyid()+"' ");
//					String lxdh = (String) commonDao.getOneColumnValueByHql("select lxd.lxdh from shry_syd_data lxd where lxd.lxdid= ='"+each.getLxdid()+"' ");
//					missDataList.add("联系单号为"+lxdh+"的风叶性能数据中的部分数据缺失,选型忽略该联系单下的风叶"+fyxh);
//					continue label1;
//				}
//				if(Double.parseDouble(eachJy) < 0 || Double.parseDouble(eachJy) < 0 || Double.parseDouble(eachJy) < 0 || Double.parseDouble(eachJy) < 0){
//					String fyxh = (String) commonDao.getOneColumnValueByHql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+each.getFyid()+"' ");
//					String lxdh = (String) commonDao.getOneColumnValueByHql("select lxd.lxdh from shry_syd_data lxd where lxd.lxdid= ='"+each.getLxdid()+"' ");
//					missDataList.add("联系单号为"+lxdh+"的风叶性能数据中的部分数据异常(小于0的数据),选型忽略该联系单下的风叶"+fyxh);
//					continue label1;
//				}
//			}
//			String dlhzj  = (String) commonDao.getOneColumnValueByHql("SELECT fy."+columnName+" FROM shry_fy_data fy WHERE fy.FYID ='"+eachXnList.get(0).getFyid()+"' ");
//			
//			ShryFyxnData startFyxhData = null;
//			double minJyRange,minLlRange;
//			minJyRange = minLlRange = 100000000;
//			//一定 满足 同静压时流量值大于输入值或者同流量时静压大于输入值,套公式改转速
//			//取两个值同时大于且最接近于 目标 的值 为起始值
//			for(ShryFyxnData each : eachXnList){
//				eachJy = each.getJyl();
//				eachLl = each.getLl();
//				if(Double.parseDouble(eachJy) > dJy && Double.parseDouble(eachLl) > dLl 
//						&& ( (Double.parseDouble(eachJy) - dJy) < minJyRange || (Double.parseDouble(eachLl) - dLl) < minLlRange )){
//					startFyxhData = each;
//					minJyRange = Double.parseDouble(eachJy) - dJy;
//					minLlRange = Double.parseDouble(eachLl) - dLl;
//				}
//			}
//			if(startFyxhData == null){ continue; }
//			//获取风叶 流量-静压 数据
//			List<ShryFyZsData> fyzsList = commonDao.getObjectBySql("select fyzs.* from shry_fy_zs_data fyzs where fyzs.fyid='"+eachXnList.get(0).getFyid()+"' ");
//			double x[] = new double[fyzsList.size()];
//			double y[] = new double[fyzsList.size()];
//			int i=0;
//			for(ShryFyZsData each:fyzsList){
//				if(StringUtils.isBlank(each.getSpeed()) || StringUtils.isBlank(each.getNoise())){ 
//					String fyxh = (String) commonDao.getOneColumnValueByHql("SELECT fy.XH FROM shry_fy_data fy WHERE fy.FYID ='"+eachXnList.get(0).getFyid()+"' ");
//					missDataList.add("风叶噪声性能数据中的部分数据异常,选型忽略该风叶"+fyxh);
//					continue label1;
//					}
//				x[i] = Double.parseDouble(each.getSpeed());
//				y[i] = Double.parseDouble(each.getNoise());
//				i++;
//			}
//			
//			//从起始值开始 以步长stepRange 递减 进行推算
//			//跳出条件为 不满足 [同静压时流量值大于输入值 或 者同流量时静压大于输入值]
//			int startZs = Integer.parseInt(startFyxhData.getZzs());
//			int startIndex = eachXnList.indexOf(startFyxhData);
//			double dEachJy,dEachLl,dEachGl,dEachZs;
//			double zsAry[] = {reversePressure(Double.parseDouble(dlhzj), startZs, Double.parseDouble(startFyxhData.getZgl()), dGl, Double.parseDouble(dlhzj)),
//					                    reverseQuantity(startZs, Double.parseDouble(dlhzj), Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getZgl()), dGl)  };
//			int ict = 0;
//			//获取较小的功率
//			double dEachGl1 = 0;
//			String sStr1 = "";
//			for(double zs : zsAry ){
//				dEachJy = translatePressure(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getJyl()), zs, Double.parseDouble(dlhzj));
//				dEachLl = translateQuantity(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getLl()), zs, Double.parseDouble(dlhzj));
//				dEachGl = translatePower(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getZgl()), zs, Double.parseDouble(dlhzj));
//
//				//使用最小二乘法 拟合曲线并求值
//				double dCurZs = Guass.getGuassValue(x,y,zs);
//					if(dCurZs < dZs && dEachGl < dGl ){
//						if(ict == 0 && dEachLl > dLl){ //同静压时流量值大于输入值 或者同流量时静压大于输入值 
//							dEachGl1 = dEachGl;
//							sStr1 = "fyid="+eachXnList.get(0).getFyid()+";zzs="+zs+";jy="+dEachJy+";ll="+dEachLl+";gl="+dEachGl+";zs="+dCurZs+";";
//						
//						}else if(ict == 1 && dEachJy > dJy){
//							if(dEachGl1 < dEachGl){//较小功率对比
//								fylist.add(sStr1);
//							}else{
//							fylist.add("fyid="+eachXnList.get(0).getFyid()+";zzs="+zs+";jy="+dEachJy+";ll="+dEachLl+";gl="+dEachGl+";zs="+dCurZs+";");
//							}
//					    }
//					ict++;
//				}
//			}
//			
//		/*	for(double zs = startZs ; zs > 0 ; zs -= 50 ){
//				dEachJy = translatePressure(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getJyl()), zs, Double.parseDouble(dlhzj));
//				dEachLl = translateQuantity(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getLl()), zs, Double.parseDouble(dlhzj));
//				dEachGl = translatePower(startZs, Double.parseDouble(dlhzj), Double.parseDouble(startFyxhData.getZgl()), zs, Double.parseDouble(dlhzj));
//				
//				double dCurZs = Guass.getGuassValue(x,y,zs);
//				if(dCurZs < dZs && dEachGl < dGl && ( () || () )){
//					
//				}
//			}*/
//			}
//		
//		
//		return fylist;
//	}
//
//
//
//
//
//
//
//
//
//
//	///换算方法集 
//	
//	//相似换算
//	
//	
//	
//	
//	/**
//	 * 压力反推转速
//	 * @param dN
//	 * @param dD
//	 * @return 结果转速
//	 */
//	public double reversePressure(double dD1,double dn1,double dP1,double dP2,double dD2){
//		if(dP2 <= 0 || dD1 <= 0 || dP1<= 0 || dn1 <= 0 || dD2 <= 0){
//			return -1;
//		}
//		return Arith.mul( Math.sqrt( Arith.div( Arith.div(dP2,dP1) , Math.pow(Arith.div(dD2, dD1), 2) ) ) , dn1 );
//	}
//	/**
//	 * 流量反推转速
//	 * @param dN
//	 * @param dD
//	 * @return 结果转速
//	 */
//	public double reverseQuantity(double dn1,double dD1,double dD2,double dQ1,double dQ2){
//		if(dn1 <= 0 || dD1 <= 0 || dQ1<= 0 || dD2 <= 0 || dQ2 <= 0){
//			return -1;
//		}
//		return  Arith.mul(  Arith.div( Arith.div(dQ2,dQ1) , Math.pow(Arith.div(dD2, dD1), 3) )  , dn1 );
//	}
//	
//	/**
//	 * 压力换算
//	 * @param dN
//	 * @param dD
//	 * @return 结果压力
//	 */
//	public double translatePressure(double dn1,double dD1,double dP1,double dn2,double dD2){
//		if(dn1 <= 0 || dD1 <= 0 || dP1<= 0 || dn2 <= 0 || dD2 <= 0){
//			return -1;
//		}
//		return Arith.mul ( Arith.mul( Math.pow(Arith.div(dn2, dn1), 2),   Math.pow(Arith.div(dD2, dD1), 2) ) , dP1);
//	}
//	/**
//	 * 流量换算
//	 * @param dN
//	 * @param dD
//	 * @return 结果流量
//	 */
//	public double translateQuantity(double dn1,double dD1,double dQ1,double dn2,double dD2){
//		if(dn1 <= 0 || dD1 <= 0 || dQ1<= 0 || dn2 <= 0 || dD2 <= 0){
//			return -1;
//		}
//		return Arith.mul ( Arith.mul( Arith.div(dn2, dn1) ,   Math.pow(Arith.div(dD2, dD1), 3) ) , dQ1);
//		}
//	
//	/**
//	 * 功率换算
//	 * @param dN
//	 * @param dD
//	 * @return 结果功率
//	 */
//	public double translatePower(double dn1,double dD1,double dN1,double dn2,double dD2){
//		if(dn1 <= 0 || dD1 <= 0 || dN1<= 0 || dn2 <= 0 || dD2 <= 0){
//			return -1;
//		}
//		return Arith.mul ( Arith.mul( Math.pow(Arith.div(dn2, dn1), 3),   Math.pow(Arith.div(dD2, dD1), 5) ) , dN1);
//	}
//	
	
	//等比换算
	
	/**
	//找到 推算起始点--流量/静压最接近目标的值 
				//找起始点 
				//3. 找到最接近目标的值(流量/静压 均小于或均大于 且最接近目标的值) 
				String startSpeed;
				double maxLl,minLl,maxJy,minJy;
				maxLl =  maxJy  = 0;
				minLl = minJy  = 1000000000;
				ShryFyxnData mxLlFyxnData,minLlFyxnData;
				mxLlFyxnData = minLlFyxnData = null;
				double stepLength = 50;
				int increaseOrDecrease = 1;
				for(ShryFyxnData each : eachXnList){
					if(Double.parseDouble(each.getLl()) > maxLl){
						maxLl = Double.parseDouble(each.getLl());
						mxLlFyxnData = each;
					}
					if(Double.parseDouble(each.getJyl()) > maxJy){
						maxJy = Double.parseDouble(each.getJyl());
					}
					
					if(Double.parseDouble(each.getLl()) < minLl){
						minLl = Double.parseDouble(each.getLl());
						minLlFyxnData = each;
					}
					if(Double.parseDouble(each.getJyl()) < minJy){
						minJy = Double.parseDouble(each.getJyl());
					}
				}
	 */
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
