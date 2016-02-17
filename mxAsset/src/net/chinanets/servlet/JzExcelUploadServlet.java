package net.chinanets.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import net.chinanets.service.CommonService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.context.ApplicationContext;

public class JzExcelUploadServlet extends HttpServlet {
	public StringBuffer sb = new StringBuffer();
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String opName = "";
		try {
			// 获取上传文件流，写入到服务器文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String path = request.getRealPath("/");
			factory.setRepository(new File(path));
			factory.setSizeThreshold(1024 * 1024);
			ServletFileUpload upload = new ServletFileUpload(factory);
			List list = upload.parseRequest(request);
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString("gbk");
				} else {
					String name = item.getFieldName();
					String value = item.getName();
					// 由于浏览器的不同，所以只讲文件名分割过来
					int start = value.lastIndexOf("\\");
					String fileName = value.substring(start + 1);
					try {
						item.write(new File(path, fileName));
					} catch (Exception e) {
						e.printStackTrace();
					}
					opName = path + fileName;
				}
			}
			// 读取上传文件、解析插入数据库
			List jzList = parseExcel(opName);
			if (jzList.size() > 0) {
				ServletContext servletContext = request.getSession()
						.getServletContext();
				ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
						.getWebApplicationContext(servletContext);
			/*	AssetJzService ajs = (AssetJzService) app
						.getBean("assetJzService");
	
				for(int i = 0; i < jzList.size(); i++) {
					AssetJz assetJz = (AssetJz) jzList.get(i);
					AssetJz jz = new AssetJz();
					jz.setSbbh(assetJz.getSbbh());
					List jzList2 = ajs.getObjectList(jz);
					
					if (jzList2.size() > 0) {
						Long id = ((AssetJz) jzList2.get(0)).getId();
						assetJz.setId(id);
						sb.append("<font color='#ED2B0B'>错误:excel第" + (i+3) + "行数据已存在,编号:" + assetJz.getSbbh() + "添加失败.</font>\n");
					} else {
						try{
							System.out.println("启用时间：——————————————————————————"+assetJz.getQysj());
							   sdf.parse(assetJz.getQysj());
						
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							sb.append("<font color='#ED2B0B'>错误:excel第" + (i+3) + "行时间格式错误，编号:" + assetJz.getSbbh() + "添加失败.</font>\n");
							continue;
						}
						
						sb.append("<font color='#0CBF43'>介质编号:" + assetJz.getSbbh() + "添加成功.</font>\n");
						ajs.saveOrUpdateJz(assetJz);
					}
				}
				out.write(sb.toString());
			} else {
				out.write("excel文件解析出错,请检查excel文件内容格式是否正确.");*/
			}
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		} finally {
			sb.setLength(0);
			out.flush();
			out.close();
		}
	}

	// 解析excel文件转换成对象
	private List parseExcel(String fileName) {
		List jzList = new ArrayList();
		
	/*	FileInputStream input = null;
		Workbook wbk = null;
		try {
			input = new FileInputStream(fileName);
			wbk = Workbook.getWorkbook(input);// 创建工作面板
			Sheet st = wbk.getSheet(0); // 获取excel中的第一个sheet
			// String qysj = sdf.format( ((DateCell)st.getCell(3, 12)).getDate()
			// );
			int rowCount = st.getRows();
			String lx="";
			
			// int columnCount = st.getColumns();
			for (int r = 2; r < rowCount; r++) {
				AssetJz assetJz = new AssetJz();
				
//				Long typeID=Long.valueOf(st.getCell(0, r).getContents());
//				if(typeID==1){
//					lx="U盘";
//				}else{
//					lx="移动硬盘";
//				}
//				assetJz.setLx(lx);
				assetJz.setMc(st.getCell(0, r).getContents().trim());
				assetJz.setBmmc(st.getCell(1, r).getContents().trim());
				assetJz.setSbbh(st.getCell(2, r).getContents().trim());
				
				
				if(st.getCell(3, r).getContents() != null & "" != st.getCell(3, r).getContents()){
					try{
					   assetJz.setQysj(sdf.format(((DateCell)st.getCell(3, r)).getDate()).trim());
					} catch (RuntimeException e) {
						
					}
			    }
//				assetJz.setQysj(st.getCell(3, r).getContents().replace('"',' '));
				assetJz.setSyr(st.getCell(4, r).getContents().trim());
				assetJz.setSydd(st.getCell(5, r).getContents().trim());
				assetJz.setZrr(st.getCell(6, r).getContents().trim());
				assetJz.setLxdh(st.getCell(7, r).getContents().trim());
				assetJz.setSmsx(st.getCell(8, r).getContents().trim());
				assetJz.setZyyt(st.getCell(9, r).getContents().trim());
				assetJz.setLx(st.getCell(0, r).getContents().trim());
				assetJz.setPp(st.getCell(11, r).getContents().trim());
				assetJz.setXh(st.getCell(12, r).getContents().trim());
				assetJz.setXlh(st.getCell(13, r).getContents().trim());
			    assetJz.setSfbgm(st.getCell(14, r).getContents());
				jzList.add(assetJz);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			
			wbk.close();
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 删除上传文件
			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
		}*/
		return jzList;
	}
}
