package net.chinanets.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import net.chinanets.pojos.ShryFyData;
import net.chinanets.pojos.ShrySydData;
import net.chinanets.pojos.ShryZcData;
import net.chinanets.service.IQueryService;
import net.chinanets.utils.CommonMethods;
import net.chinanets.utils.DownLoadUtil;
import net.chinanets.utils.ExcelUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ExportAllExcel extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String jdbcSql = request.getParameter("jdbcSql");//SQL查询语句
		String dataid = request.getParameter("dataid");//SQL查询语句
		String dataid2 = request.getParameter("dataid2");//SQL查询语句
		String exportType = request.getParameter("exportType");//导出类型
		String extraData = request.getParameter("extraData");//要导出的风叶性能数据
		
		ServletContext servletContext = request.getSession().getServletContext();
		ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
		.getWebApplicationContext(servletContext);
		
		IQueryService qService = (IQueryService) ap.getBean("QueryService");
		response.setContentType("application/application/msexcel;charset=UTF-8");
		try {
		//风叶性能换算数据导出
		if("FYXNHS".equals(exportType)){
			exportFYXNHSData(request,response, qService, dataid, dataid2, extraData);
		}else if("ZCXNHS".equals(exportType)){
			exportZCXNHSData(request,response, qService, dataid, dataid2, extraData);
		}else if("DJXNCZ".equals(exportType)){
			exportZCXNHSData(request,response, qService, dataid, dataid2, extraData);
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 风叶性能转换的数据导出
	 * @param response
	 * @param dataid 风叶ID
	 * @param dataid2 联系单ID
	 * @param fyxnData
	 * @throws UnsupportedEncodingException 
	 */
	private String exportFYXNHSData(HttpServletRequest request,HttpServletResponse response,IQueryService queryService,String dataid,String dataid2,String jsonStr) throws Exception{ 
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		//获取字段数据Object
		JSONObject argsMap = jsonObj.getJSONObject("argObj");
		JSONArray fyxnJsonAry = jsonObj.getJSONObject("chartDataList").getJSONArray("source");
		List<ShryFyData> tempList = queryService.getObjectList(new ShryFyData(), " fyid='"+dataid+"' ");
		if(tempList == null || tempList.size() < 1){ return ExcelUtil.ErrorPrefix+"找不到当前风叶的相关数据";}
		List<ShrySydData> tempList2 = queryService.getObjectList(new ShrySydData(), " lxdid='"+dataid2+"' ");
		if(tempList2 == null || tempList2.size() < 1){ return ExcelUtil.ErrorPrefix+"找不到当前联系单的相关数据";}
		ShryFyData fydataObj = tempList.get(0);
		ShrySydData syddataObj = tempList2.get(0);
		Map<String,String> fydataMap = new HashMap<String,String>();
		Map<String,int[]> fydataXYMap = new HashMap<String,int[]>();
		
		fydataMap.put("syrq", CommonMethods.commonFullDateFormate.format(syddataObj.getSyrq()));    fydataXYMap.put("syrq", new int[]{1,3});
		fydataMap.put("syfd", syddataObj.getSyfd());    fydataXYMap.put("syfd", new int[]{1,4});
		fydataMap.put("syfs", syddataObj.getSyfs());    fydataXYMap.put("syfs", new int[]{1,5});
		fydataMap.put("fjxh", syddataObj.getZcxh());    fydataXYMap.put("fjxh", new int[]{1,6});
		fydataMap.put("ypbh", syddataObj.getFyxh());    fydataXYMap.put("ypbh", new int[]{1,7});
		fydataMap.put("syxz", syddataObj.getSymd());    fydataXYMap.put("syxz", new int[]{1,8});
		fydataMap.put("sply", syddataObj.getSply());    fydataXYMap.put("sply", new int[]{1,9});
		fydataMap.put("syry", syddataObj.getSyry());    fydataXYMap.put("syry", new int[]{1,10});
		
		fydataMap.put("lxdh", syddataObj.getLxdh());    fydataXYMap.put("lxdh", new int[]{4,3});
		fydataMap.put("fjxs", syddataObj.getFjxs());    fydataXYMap.put("fjxs", new int[]{6,3});
		fydataMap.put("ckmj", syddataObj.getCkmj());    fydataXYMap.put("ckmj", new int[]{7,4});
		fydataMap.put("fyzj", syddataObj.getFyzj());    fydataXYMap.put("fyzj", new int[]{7,5});
		fydataMap.put("yps", syddataObj.getYps());    fydataXYMap.put("yps", new int[]{6,6});
		fydataMap.put("dqy", syddataObj.getDqy());    fydataXYMap.put("dqy", new int[]{7,7});
		fydataMap.put("kqwd", syddataObj.getKqwd());    fydataXYMap.put("kqwd", new int[]{7,8});
		fydataMap.put("xdsd", syddataObj.getXdsd());    fydataXYMap.put("xdsd", new int[]{7,9});
		fydataMap.put("skqbz", syddataObj.getSkqbz());    fydataXYMap.put("skqbz", new int[]{7,10});
		
		fydataMap.put("memo", syddataObj.getMemo()+",当前直径:"+argsMap.getString("targetDlhzj")+"mm,参考风叶型号:"+fydataObj.getXh() +",导流环直径:"+argsMap.getString("srcDlhzj")+
				",转速:"+argsMap.getString("srcZs")+",联系单号:"+syddataObj.getLxdh());    fydataXYMap.put("memo", new int[]{1,11});
		
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		Map<String,String> eachMap;
		for(int i =0;i<fyxnJsonAry.size();i++){
			eachMap = fyxnJsonAry.getJSONObject(i);
			eachMap.put("xh", (i+1)+"");
			listMap.add(eachMap);
		}
//		listMap.addAll(fyxnJsonAry);
		
		Map<String,int[]> listXYMap = new HashMap<String,int[]>();
		listXYMap.put("ll", new int[]{1,15});		listXYMap.put("jyl", new int[]{2,15});
		listXYMap.put("zzs", new int[]{3,15});		listXYMap.put("fzs", new int[]{4,15});
		listXYMap.put("nj", new int[]{5,15});		listXYMap.put("zgl", new int[]{6,15});
		listXYMap.put("xl", new int[]{7,15});		listXYMap.put("xh", new int[]{0,15});		
		
		String excelName = fydataObj.getXh()+"风叶性能数据导出.xls";
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(excelName.getBytes(), "ISO-8859-1"));
		
		String pathPrefix = request.getRealPath("");
		
		//生成新文件
		String sourcePath = pathPrefix +"/sysArgFiles/template/template_FYXNExport.xls";
		String excelPath = pathPrefix + "/sysArgFiles/templefiles/"+UUID.randomUUID()+"template_FYXNExport.xls";
		boolean iscopyOk = true;//ExcelUtil.copyFile(sourcePath, excelPath, true);
		if(!iscopyOk){return ExcelUtil.ErrorPrefix+"复制文件失败";}
		String expRst = ExcelUtil.writeDataToExcel(sourcePath,excelPath, fydataMap, fydataXYMap, listMap, listXYMap);
		
		if(!expRst.startsWith(ExcelUtil.ErrorPrefix)){ DownLoadUtil.downLoad(excelPath, response,excelName, false); }
		ExcelUtil.deleteFile(excelPath);
		
		return expRst;
	}
	/**
	 * 风叶性能转换的数据导出
	 * @param response
	 * @param dataid 风叶ID
	 * @param dataid2 联系单ID
	 * @param fyxnData
	 * @throws UnsupportedEncodingException 
	 */
	private String exportZCXNHSData(HttpServletRequest request,HttpServletResponse response,IQueryService queryService,String dataid,String dataid2,String fyxnData) throws Exception{ 
		//获取字段数据Object
		
		List<ShryZcData> tempList = queryService.getObjectList(new ShryZcData(), " zcid='"+dataid+"' ");
		if(tempList == null || tempList.size() < 1){ return ExcelUtil.ErrorPrefix+"找不到当前总成的相关数据";}
		List<ShrySydData> tempList2 = queryService.getObjectList(new ShrySydData(), " lxdid='"+dataid2+"' ");
		if(tempList2 == null || tempList2.size() < 1){ return ExcelUtil.ErrorPrefix+"找不到当前联系单的相关数据";}
		List<ShryFyData> tempList3 = queryService.getObjectList(new ShryFyData(), " fyid='"+tempList.get(0).getFyid()+"' ");
		if(tempList2 == null || tempList2.size() < 1){ return ExcelUtil.ErrorPrefix+"找不到当前风叶的相关数据";}
		ShryZcData zcdataObj = tempList.get(0);
		ShryFyData fydataObj = tempList3.get(0);
		ShrySydData syddataObj = tempList2.get(0);
		Map<String,String> fydataMap = new HashMap<String,String>();
		Map<String,int[]> fydataXYMap = new HashMap<String,int[]>();
		
		
		fydataMap.put("syrq", CommonMethods.commonFullDateFormate.format(syddataObj.getSyrq()));    fydataXYMap.put("syrq", new int[]{1,3});
		fydataMap.put("syfd", syddataObj.getSyfd());    fydataXYMap.put("syfd", new int[]{1,4});
		fydataMap.put("syfs", syddataObj.getSyfs());    fydataXYMap.put("syfs", new int[]{1,5});
		fydataMap.put("fjxh", syddataObj.getZcxh());    fydataXYMap.put("fjxh", new int[]{1,6});
		fydataMap.put("ypbh", syddataObj.getFyxh());    fydataXYMap.put("ypbh", new int[]{1,7});
		fydataMap.put("syxz", syddataObj.getSymd());    fydataXYMap.put("syxz", new int[]{1,8});
		fydataMap.put("sply", syddataObj.getSply());    fydataXYMap.put("sply", new int[]{1,9});
		fydataMap.put("syry", syddataObj.getSyry());    fydataXYMap.put("syry", new int[]{1,10});
		
		fydataMap.put("lxdh", syddataObj.getLxdh());    fydataXYMap.put("lxdh", new int[]{4,3});
		fydataMap.put("fjxs", syddataObj.getFjxs());    fydataXYMap.put("fjxs", new int[]{6,3});
		fydataMap.put("ckmj", syddataObj.getCkmj());    fydataXYMap.put("ckmj", new int[]{7,4});
		fydataMap.put("fyzj", syddataObj.getFyzj());    fydataXYMap.put("fyzj", new int[]{7,5});
		fydataMap.put("yps", syddataObj.getYps());    fydataXYMap.put("yps", new int[]{6,6});
		fydataMap.put("dqy", syddataObj.getDqy());    fydataXYMap.put("dqy", new int[]{7,7});
		fydataMap.put("kqwd", syddataObj.getKqwd());    fydataXYMap.put("kqwd", new int[]{7,8});
		fydataMap.put("xdsd", syddataObj.getXdsd());    fydataXYMap.put("xdsd", new int[]{7,9});
		fydataMap.put("skqbz", syddataObj.getSkqbz());    fydataXYMap.put("skqbz", new int[]{7,10});
		
//		fydataMap.put("fbzj", fydataObj.getFbzj());    fydataXYMap.put("fbzj", new int[]{2,3});
//		fydataMap.put("ypsm", fydataObj.getYpsm());    fydataXYMap.put("ypsm", new int[]{2,4});
//		
//		fydataMap.put("dqy", syddataObj.getDqy());    fydataXYMap.put("dqy", new int[]{2,5});
//		fydataMap.put("kqwd", syddataObj.getKqwd());    fydataXYMap.put("kqwd", new int[]{2,6});
//		fydataMap.put("xdsd", syddataObj.getXdsd());    fydataXYMap.put("xdsd", new int[]{2,7});
//		fydataMap.put("skqbz", syddataObj.getSkqbz());    fydataXYMap.put("skqbz", new int[]{2,8});
		
		fydataMap.put("memo", syddataObj.getMemo()+",参考总成型号("+zcdataObj.getXh()+") 参考风叶型号("+fydataObj.getXh() +")");    fydataXYMap.put("memo", new int[]{1,11});
		
		JSONArray fyxnJsonAry = JSONObject.fromObject(fyxnData).getJSONObject("list").getJSONArray("source");
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		Map<String,String> eachMap;
		for(int i =0;i<fyxnJsonAry.size();i++){
			eachMap = fyxnJsonAry.getJSONObject(i);
			eachMap.put("xh", (i+1)+"");
			listMap.add(eachMap);
		}
//		listMap.addAll(fyxnJsonAry);
		
		Map<String,int[]> listXYMap = new HashMap<String,int[]>();
		listXYMap.put("ll", new int[]{1,15});		listXYMap.put("jyl", new int[]{2,15});
		listXYMap.put("zzs", new int[]{3,15});		listXYMap.put("fzs", new int[]{4,15});
		listXYMap.put("dy", new int[]{5,15});		listXYMap.put("dl", new int[]{6,15});
		listXYMap.put("srgl", new int[]{7,15});	  listXYMap.put("xl", new int[]{8,15});			
		listXYMap.put("xh", new int[]{0,15});		
		
		
		
		String pathPrefix = request.getRealPath("");
		
		//生成新文件
		String sourcePath = pathPrefix +"/sysArgFiles/template/template_ZCXNExport.xls";
		String excelPath = pathPrefix + "/sysArgFiles/templefiles/"+UUID.randomUUID()+"template_ZCXNExport.xls";
		boolean iscopyOk = true;//ExcelUtil.copyFile(sourcePath, excelPath, true);
		if(!iscopyOk){return ExcelUtil.ErrorPrefix+"复制文件失败";}
		String expRst = ExcelUtil.writeDataToExcel(sourcePath,excelPath, fydataMap, fydataXYMap, listMap, listXYMap);
		
		String excelName = zcdataObj.getXh()+"总成性能数据导出.xls";
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(excelName.getBytes(), "ISO-8859-1"));
		if(!expRst.startsWith(ExcelUtil.ErrorPrefix)){ DownLoadUtil.downLoad(excelPath, response, excelName, false); }
		ExcelUtil.deleteFile(excelPath);
		
		return expRst;
	}
}
