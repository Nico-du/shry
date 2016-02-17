package net.chinanets.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import net.chinanets.service.IQueryService;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportAllExcel extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ExportAllExcel() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String excelName = "三合一数据导出.xls";
		String title  = "三合一数据导出";
		
		String jdbcSql = request.getParameter("jdbcSql");
		ServletContext servletContext = request.getSession()
		.getServletContext();
		ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
		.getWebApplicationContext(servletContext);
		IQueryService qService = (IQueryService) ap.getBean("QueryService");
		//String[] headerTexts={"户籍号","类别","设备名称","品牌型号","序列号","购置日期","使用状态","部门名称","责任人","物理位置","主机名","主要用途","ip地址","mac地址",
		//		"cpu规格","内存信息","操作系统","硬盘型号","硬盘数量","硬盘容量","硬盘序列号","光驱名称"};
		String[] headerText={"code","typeName","brand","serialNumber","purchaseTime","useStatus","deptName","owner","address","computerName","usages","ip","macIP",
				"cupSpec","memorySize","operatingSystem","hardDiskModel","hardDiskVolume","hardDiskSerialNumber","cdromModel"};
		String[] headerTexts={"户籍号","类别","品牌型号","序列号","购置日期","使用状态","部门名称","责任人","物理位置","主机名","主要用途","ip地址","mac地址",
						"cpu规格","内存信息","操作系统","硬盘型号","硬盘容量","硬盘序列号","光驱型号"};
		int length = headerTexts.length;
				response.setContentType("application/application/msexcel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(excelName.getBytes(), "ISO-8859-1"));
		List list = new ArrayList();
		list = qService.executeJdbcSql(jdbcSql, length);
		ServletOutputStream out = response.getOutputStream();
		
		WritableWorkbook workbook = Workbook.createWorkbook(out);
		WritableSheet sheet = workbook.createSheet("Sheet1", 0);
		Label label;
			try {
				WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD);
				WritableCellFormat wcfF = new WritableCellFormat(wf);
				label = new Label(0, 0, title);
				//sheet.addCell(label);
				for (int i = 0; i < headerText.length; i++) {
					sheet.addCell(new Label(i, 0, headerText[i],wcfF));
				}
				for (int i = 0; i < headerTexts.length; i++) {
					sheet.addCell(new Label(i, 1, headerTexts[i],wcfF));
				}
				
				for (int i = 2; i < list.size() + 2; i++) {
					for (int j = 0; j < length; j++) {
						label = new Label(j, i, ((String[]) list.get(i - 2))[j]);
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
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
