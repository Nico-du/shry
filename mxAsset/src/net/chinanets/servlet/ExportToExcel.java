package net.chinanets.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.chinanets.dao.AllAssetDao;
import net.chinanets.dao.imp.AllAssetDaoImp;
import net.chinanets.pojos.Rules;
import net.chinanets.pojos.Users;
import net.chinanets.service.IQueryService;
import net.chinanets.service.UserService;
import net.chinanets.vo.UserVo;

import org.springframework.context.ApplicationContext;

public class ExportToExcel extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		ServletContext servletContext = request.getSession().getServletContext();
		ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		IQueryService qService = (IQueryService) ap.getBean("QueryService");
		UserService userService = (UserService) ap.getBean("UserService");
		
		String excelName = request.getParameter("excelName");
		String title = request.getParameter("title");
		String jdbcSql = request.getParameter("jdbcSql");
		String isWFExp  = request.getParameter("isWFExp");
		String tableName = request.getParameter("tableName");
		if("dbt".equals(isWFExp)){
			exportDoubleTitle(request,response,qService,ap);
			return;
		}
		if("true".equals(isWFExp)){
			AllAssetDao allAssetDao = (AllAssetDao) ap.getBean("allAssetDao");
			jdbcSql = allAssetDao.FindHqlBySearchForm(tableName,jdbcSql);
		}
		
		jdbcSql = jdbcSql.replace("zclxid=0", "1=1");
		int length = Integer.parseInt(request.getParameter("length"));
		String[] headerTexts = request.getParameterValues("headerTexts");
		
		
		List list = new ArrayList();
		if("from".equals(jdbcSql.substring(0, 4))) {
			list = getUserList(userService, jdbcSql);
		}else {
			System.out.println(jdbcSql+length);
				list = qService.executeJdbcSql(jdbcSql, length);
			
		}
		

		response
				.setContentType("application/application/msexcel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(excelName.getBytes(), "ISO-8859-1"));

		ServletOutputStream out = response.getOutputStream();

		WritableWorkbook workbook = Workbook.createWorkbook(out);
		WritableSheet sheet = workbook.createSheet("Sheet1", 0);
		Label label;
			try {
				WritableFont wf = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD, true);
				WritableCellFormat wcfF = new WritableCellFormat(wf);
				label = new Label(0, 0, title);
				sheet.addCell(label);
				for (int i = 0; i < headerTexts.length; i++) {
					sheet.addCell(new Label(i, 2, headerTexts[i],wcfF));
				}
				for (int i = 3; i < list.size() + 3; i++) {
					for (int j = 0; j < length; j++) {
						label = new Label(j, i, ((String[]) list.get(i - 3))[j]);
						sheet.addCell(label);
					}
				}
	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					workbook.write();
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		//	writeJs(response.getWriter());
		out.flush();
		out.close();
	}
	
	//导出到Excel 导出双标题的入库单及明细/退库单及明细  --暂时不用
	private void exportDoubleTitle(HttpServletRequest request, HttpServletResponse response,
			IQueryService queryService,ApplicationContext ap)throws ServletException, IOException {
		String excelName = request.getParameter("excelName");
		String title = request.getParameter("title");
		String jdbcSql = request.getParameter("jdbcSql");
		String secondjdbcSql = request.getParameter("secondjdbcSql");
		
		jdbcSql = jdbcSql.replace("zclxid=0", "1=1");
		int length = Integer.parseInt(request.getParameter("length"));
		String[] headerTexts = request.getParameterValues("headerTexts");
		String[] secondHeaderTexts = request.getParameterValues("secondHeaderTexts");
		
		//领用明细单信息
		List list = new ArrayList();
		System.out.println(jdbcSql+length);
		list = queryService.executeJdbcSql(jdbcSql, length);
		
		//领用单信息
		List secondlist = new ArrayList();
		System.out.println(secondjdbcSql+secondHeaderTexts.length);
		secondlist = queryService.executeJdbcSql(secondjdbcSql, secondHeaderTexts.length);

		response.setContentType("application/application/msexcel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="
		+ new String(excelName.getBytes(), "ISO-8859-1"));

		ServletOutputStream out = response.getOutputStream();

		WritableWorkbook workbook = Workbook.createWorkbook(out);
		WritableSheet sheet = workbook.createSheet("Sheet1", 0);
		Label label;
			try {
				WritableFont wf = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD, true);
				WritableCellFormat wcfF = new WritableCellFormat(wf);
				label = new Label(0, 0, title+"登记信息");
				sheet.addCell(label);
				label = new Label(0, 1, title+"单信息");
				sheet.addCell(label);
				label = new Label(0, 4, title+"单明细信息");
				sheet.addCell(label);
				
				//入库单信息 secondHeaderTexts secondlist
				for (int i = 0; i < secondHeaderTexts.length; i++) {
					sheet.addCell(new Label(i, 2, secondHeaderTexts[i],wcfF));
				}
				for (int i = 3; i < secondlist.size() + 3; i++) {
					for (int j = 0; j < secondHeaderTexts.length; j++) {
						label = new Label(j, i, ((String[]) secondlist.get(i - 3))[j]);
						sheet.addCell(label);
					}
				}
				
				//明细列表 headerTexts list
				for (int i = 0; i < headerTexts.length; i++) {
					sheet.addCell(new Label(i, 5, headerTexts[i],wcfF));
				}
				for (int i = 6; i < list.size() + 6; i++) {
					for (int j = 0; j < length; j++) {
						label = new Label(j, i, ((String[]) list.get(i - 6))[j]);
						sheet.addCell(label);
					}
				}
				
				
	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					workbook.write();
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		//	writeJs(response.getWriter());
		out.flush();
		out.close();
	}
	
	private void writeJs(PrintWriter out){
		try {
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		
		out.println("<script type=\"text/javascript\">");
		out.println("alert('ddssdfsd');");
		out.println("</script>");
		out.println("</head>");
		out.println("<body>");
		out.println("</body>");
		out.println("</html>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
	private List getUserList(UserService userService, String sql) {
		System.out.println(sql);
		List userList = new ArrayList();
		List resultList = new ArrayList();
		
		String idStr = "";
		if("idStr".equals(sql.substring(sql.length()-5, sql.length()))) {
			sql = sql.substring(sql.indexOf("id"), sql.length());
			sql = sql.substring(0, sql.length()-5);
			userList = userService.findUsers(1000, 1, sql +  " and mc <> 'admin'");
		}else {
			String properties = sql.substring(sql.indexOf(" ") + 1, sql.indexOf("="));
			String value = sql.substring(sql.indexOf("=") + 1, sql.length());
			if("js".equals(properties)) {
				userList = userService.findUsers(1000, 1, "js=" + value + " and mc <> 'admin'");
			}else if("zsxm".equals(properties)) {
				userList = userService.findUsers(1000, 1, "zsxm like '%" + value + "%'" + " and mc <> 'admin'");//这是一个分页方法，这里用1000表示查询所有的用户，所有用户应该不会超过1000条
			}else if("bmmc".equals(properties)) {
				userList = userService.findUsers(1000, 1, "bmmc like '%" + value + "%'" + " and mc <> 'admin'");
			}else if("mc".equals(properties)) {
				userList = userService.findUsers(1000, 1, "mc like '%" + value + "%'" + " and mc <> 'admin'");
			}
		}
		
		for(int i = 0; i < userList.size(); i++) {
			UserVo userVo = (UserVo)userList.get(i);
			String[] str = {userVo.getMc(), userVo.getUserName(), userVo.getBmmc(), userVo.getRuleName()};
			resultList.add(str);
		}
		
		return resultList;
	}
}
