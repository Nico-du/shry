package net.chinanets.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.chinanets.service.AllAssetService;
import net.chinanets.service.ChartService;
import net.chinanets.service.IQueryService;
import net.chinanets.service.UserService;
import net.chinanets.utils.CommonMethods;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.context.ApplicationContext;

public class ReportServlet extends HttpServlet {
	public ReportServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

    private static final long serialVersionUID = 1L;   
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		//expType:String,tempObjViewName:String,strcondition:String
		String expType = request.getParameter("expType");
		if(CommonMethods.isNullOrWhitespace(expType)){
			throw new ServletException("wrong parameter exception : parameter [expType] is null in calling ReportServlet !");
			}
		if(expType.contains("变更单")){
		exportBGDToWord(request,response);
		}else if("report_bmlyzk".equals(expType)){
			exportToExcel(request,response);
		}
		
		
	}
	
	
	/**
	 * 导出部门领用情况Excel
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void exportToExcel(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = request.getSession().getServletContext();
		ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		int currentYear = Integer.parseInt(request.getParameter("dataId"));
		if(currentYear == 0){return;}
		ChartService chartService = (ChartService) ap.getBean("chartService");
		List rstList = chartService.getBmhclyqk(currentYear);
		request.setAttribute("dataList", rstList);
		request.getRequestDispatcher("/exportTemplate/report_bmlyzk.jsp").forward(request, response);
	}
	/**
	 * 导出入库/领用单到word模板
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void exportBGDToWord(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = request.getSession().getServletContext();
		ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		AllAssetService allAssetService = (AllAssetService) ap.getBean("allAssetService");
		String expType = request.getParameter("expType");
		String dataId = request.getParameter("dataId");
		String strCondition,tempObjViewName,strmxCondition,tempmxObjViewName;
		List dataList,datamxList;
		strCondition = tempObjViewName = strmxCondition = tempmxObjViewName = "";
	    dataList = datamxList = null;
		if(expType.contains("入库")){
			strCondition = " {} and rkdid = '"+dataId+"'";
			strmxCondition = " {} and rkdid = '"+dataId+"'";
			tempObjViewName = "CNSV_RKD";
			tempmxObjViewName = "CNSV_RYHCMX";
			dataList = getAllAssetServiceItems(allAssetService.GetAllAsset(tempObjViewName,Integer.MAX_VALUE,1,strCondition));
			datamxList =  getAllAssetServiceItems(allAssetService.GetAllAsset(tempmxObjViewName,Integer.MAX_VALUE,1,strmxCondition));
			
			request.setAttribute("dataList", dataList);
			request.setAttribute("datamxList", datamxList);
			request.getRequestDispatcher("/exportTemplate/report_rkdandrkdmx.jsp").forward(request, response);
		}else if(expType.contains("领用")){
			strCondition = " {} and lydid = '"+dataId+"'";
			strmxCondition = "  {} and lydid = '"+dataId+"'";
			tempObjViewName = "CNSV_LYD";
			tempmxObjViewName = "CNSV_LYDMX";
			dataList = getAllAssetServiceItems(allAssetService.GetAllAsset(tempObjViewName,Integer.MAX_VALUE,1,strCondition));
			datamxList =  getAllAssetServiceItems(allAssetService.GetAllAsset(tempmxObjViewName,Integer.MAX_VALUE,1,strmxCondition));
			
			request.setAttribute("dataList", dataList);
			request.setAttribute("datamxList", datamxList);
			request.getRequestDispatcher("/exportTemplate/report_lydandlydmx.jsp").forward(request, response);
		}
	
		
	//	this.allAssetService.GetAllAsset(tempObjViewName,tempPageSize,tempPageCurrent,strcondition);
		
	}
	
	//解析JSONObject中的items
	private List getAllAssetServiceItems(String resultStr){
		JSONObject jsonObj = JSONObject.fromObject(resultStr);
		return (List) jsonObj.get("items");
	}
	
	
	public void init() throws ServletException {
		// Put your code here
	}

}
