package net.chinanets.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.chinanets.service.IQueryService;

import org.springframework.context.ApplicationContext;

public class ZcglToExcel extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String excelName = request.getParameter("excelName");
		String title = request.getParameter("title");
		String jdbcSql = request.getParameter("jdbcSql");

		int length = Integer.parseInt(request.getParameter("length"));

		String[] headerTexts = request.getParameterValues("headerTexts");
		ServletContext servletContext = request.getSession()
				.getServletContext();
		ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		IQueryService qService = (IQueryService) ap.getBean("QueryService");
		Object kService = (Object) ap.getBean("kmzcService");
		List list = qService.executeJdbcSql(jdbcSql, length);

		response
				.setContentType("application/application/msexcel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(excelName.getBytes(), "ISO-8859-1"));

		ServletOutputStream out = response.getOutputStream();
		WritableWorkbook workbook = Workbook.createWorkbook(out);
		WritableSheet sheet = workbook.createSheet(excelName, 0);
		Label label;
		try {
			sheet.mergeCells(0, 0, length - 1, 1);// 合并单元格(左列，左行，右列，右行)
													// 从第1行第1列到第2行第3列
			label = new Label(0, 0, title, getHeader());
			sheet.addCell(label);
			for (int i = 0; i < length; i++) {
				sheet.addCell(new Label(i, 2, headerTexts[i], getTitle()));
				if (i == 0 || i == 2)
					sheet.setColumnView(i, 30);
				else
					sheet.setColumnView(i, 20);

			}
			for (int i = 3; i < list.size() + 3; i++) {
				for (int j = 0; j < length; j++) {
					// 自定义审批状态打印格式
					if (headerTexts[j].equals("审批状态")) {
						if (((String[]) list.get(i - 3))[j].equals("0")) {
							((String[]) list.get(i - 3))[j] = "等待管理员审批";
						} else if (((String[]) list.get(i - 3))[j].equals("1")) {
							((String[]) list.get(i - 3))[j] = "已审批";
						} else if (((String[]) list.get(i - 3))[j].equals("2")) {
							((String[]) list.get(i - 3))[j] = "已支出";
						}
					}
					// 自定义科目名称打印格式
					if (headerTexts[j].equals("项目名称")) {
						if (((String[]) list.get(i - 3))[j] != null) {
							((String[]) list.get(i - 3))[j] = "11";/*kService
									.findByID(
											Long.valueOf(((String[]) list
													.get(i - 3))[j])).getXmmc();*/
						} else {
							((String[]) list.get(i - 3))[j] = "未指定";
						}
					}
					// 自定义时间打印格式
					if (headerTexts[j].equals("支出日期")) {
						if (((String[]) list.get(i - 3))[j] != null) {
							((String[]) list.get(i - 3))[j] = ((String[]) list
									.get(i - 3))[j].substring(0, 10);
						}
					}
					// 常规打印直接读取数组中的数据
					label = new Label(j, i, ((String[]) list.get(i - 3))[j],
							getNormolCell());
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
	 * 设置表头样式
	 * 
	 * @return
	 */
	public static WritableCellFormat getHeader() {
		WritableFont font = new WritableFont(WritableFont.TIMES, 24,
				WritableFont.BOLD);// 定义字体
		try {
			font.setColour(Colour.BLUE);// 蓝色字体
		} catch (WriteException e1) {
			e1.printStackTrace();
		}
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);// 黑色边框
			format.setBackground(Colour.YELLOW);// 黄色背景
			format.setBackground(Colour.YELLOW2);
		} catch (WriteException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return format;
	}

	/**
	 * 设置标题样式
	 * 
	 * @return
	 */
	public static WritableCellFormat getTitle() {
		WritableFont font = new WritableFont(WritableFont.TIMES, 14);
		try {
			font.setColour(Colour.BLUE);// 蓝色字体
		} catch (WriteException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		WritableCellFormat format = new WritableCellFormat(font);

		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		} catch (WriteException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return format;
	}

	/**
	 * 设置其他单元格样式
	 * 
	 * @return
	 */
	public static WritableCellFormat getNormolCell() {// 12号字体,上下左右居中,带黑色边框
		WritableFont font = new WritableFont(WritableFont.TIMES, 12);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			format.setAlignment(jxl.format.Alignment.CENTRE);
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		} catch (WriteException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return format;
	}

}
