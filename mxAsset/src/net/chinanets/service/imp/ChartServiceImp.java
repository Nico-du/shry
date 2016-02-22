package net.chinanets.service.imp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import net.chinanets.data.DataEntity;
import net.chinanets.service.ChartService;
import net.chinanets.utils.CommonMethods;
import net.chinanets.vo.Chart;
import net.chinanets.vo.UserVo;
import net.sf.json.JSONArray;



@SuppressWarnings("unchecked")
public class ChartServiceImp extends CommonServiceImp implements ChartService {
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
