package net.chinanets.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExpExcelTemplateServlet extends HttpServlet {
	
	public ExpExcelTemplateServlet() {
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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String[] headerTexts = request.getParameterValues("headerTexts");
		String fname = request.getParameter("fileName");
		String formTitle = request.getParameter("formTitle");
    	
    	
    	OutputStream os = response.getOutputStream();//取得输出流
    	response.reset();//清空输出流

    	//下面是对中文文件名的处理
    	response.setCharacterEncoding("UTF-8");//设置相应内容的编码格式
    	fname = java.net.URLEncoder.encode(fname,"UTF-8");
    	response.setHeader("Content-Disposition","attachment;filename="+new String(fname.getBytes("UTF-8"),"GBK")+".xls");
    	response.setContentType("application/msexcel");//定义输出类型
    	try{
    		//创建工作薄
    		WritableWorkbook workbook = Workbook.createWorkbook(os);
    		//创建新的一页
    		WritableSheet sheet = workbook.createSheet("First Sheet", 0);
    		//构造表头
    /* 		sheet.mergeCells(0, 0, headerTexts.length-1, 0);//添加合并单元格，第一个参数是起始列，第二个参数是起始行，第三个参数是终止列，第四个参数是终止行
   		    if(formTitle != null && formTitle.length() > 0){
    		WritableFont bold = new WritableFont(WritableFont.TAHOMA,20,WritableFont.BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
    		WritableCellFormat titleFormate = new WritableCellFormat(bold);//生成一个单元格样式控制对象
    		titleFormate.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
    		titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中
    		Label title = new Label(0,0,formTitle,titleFormate);
    		sheet.setRowView(0, 600, false);//设置第一行的高度
    		sheet.addCell(title);
    		}*/
    		sheet.setRowView(0, 450, false);

    		//创建要显示的具体内容
    		WritableFont color = new WritableFont(WritableFont.COURIER,14,WritableFont.BOLD);//选择字体黑体14加粗
    		//   color.setColour(Colour.GOLD);//设置字体颜色为金黄色
    		WritableCellFormat colorFormat = new WritableCellFormat(color);
    		for(int i=0;i<headerTexts.length;i++){
    			Label fmtCell = new Label(i,0,headerTexts[i].toString(),colorFormat);
    			sheet.addCell(fmtCell);
    			sheet.setColumnView(i, 16);
    		}
    		//把创建的内容写入到输出流中，并关闭输出流
    		workbook.write();
    		workbook.close();
    		os.close();
    		
    	}catch (Exception e) {

    	}
        
	}

}
