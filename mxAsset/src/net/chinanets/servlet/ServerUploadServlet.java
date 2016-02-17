package net.chinanets.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.context.ApplicationContext;

public class ServerUploadServlet extends HttpServlet {
	public SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
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
		/*	// 读取上传文件、解析插入数据库
			List serverList = parseExcel(opName);
			if (serverList.size() > 0) {
				ServletContext servletContext = request.getSession()
						.getServletContext();
				ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
						.getWebApplicationContext(servletContext);
				AssetServerService ass = (AssetServerService) app
						.getBean("serverService");
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < serverList.size(); i++) {
					AssetServer server = (AssetServer) serverList.get(i);
					AssetServer serverPo = new AssetServer();
					serverPo.setSbbh(server.getSbbh());
					serverPo.setZclx(server.getZclx());
					List jzList2 = ass.getObjectList(serverPo);
					if (jzList2.size() > 0) {
						Long id = ((AssetServer) jzList2.get(0)).getId();
						server.setId(id);
						sb.append("<font color='#ED2B0B'>错误:excel第" + (i+3) + "行数据已存在," + server.getZclx() + " 的设备编号:" + server.getSbbh() + "添加失败.</font>\n");
					} else {
						try{
							System.out.println("启用时间：——————————————————————————"+server.getQysj());
							System.out.println("设备编号：——————————————————————————"+server.getSbbh());
							if(!"".equals(server.getQysj())&&server.getQysj()!=null){   
							fmt.parse(server.getQysj());
							}
							if(!"".equals(server.getGzrq())&&server.getGzrq()!=null){   
								fmt.parse(server.getGzrq());
								}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							sb.append("<font color='#ED2B0B'>错误:excel第" + (i+3) + "行时间格式错误，编号:" + server.getSbbh() + "添加失败.</font>\n");
							continue;
						}
						sb.append("<font color='#0CBF43'>" + server.getZclx() + " 的设备编号:" + server.getSbbh() + "添加成功.</font>\n");
						ass.saveOrUpdateServer(server);
					}
				}
				out.write(sb.toString());
			} else {
				out.write("excel文件解析出错,请检查excel文件内容格式是否正确.");
			}*/
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

	// 解析excel文件转换成对象
	private List parseExcel(String fileName) {
		List serverList = new ArrayList();
		FileInputStream input = null;
		Workbook wbk = null;
		try {
			
			input = new FileInputStream(fileName);
			wbk = Workbook.getWorkbook(input);// 创建工作面板
			Sheet st = wbk.getSheet(0); // 获取excel中的第一个sheet
			/*int rowCount = st.getRows();
			String zclx="";
			for (int r = 2; r < rowCount; r++) {
				if(st.getCell(0, r).getContents()!=null&&!st.getCell(0, r).getContents().equals("")){
				AssetServer server = new AssetServer();
				String type=st.getCell(0,r).getContents();
				if("1".equals(type)){
					zclx="小型机";
				}else if("2".equals(type)){
					zclx="PC服务器";
				}else if("3".equals(type)){
					zclx="工控机";
				}else if("4".equals(type)){
					zclx="刀片服务器";
				}
				server.setZclx(zclx);
				server.setSbbh(st.getCell(1,r).getContents());
				server.setGzh(st.getCell(2,r).getContents());
				server.setSbxlh(st.getCell(3, r).getContents());
				server.setCzxtbb(st.getCell(4,r).getContents());
				server.setPpxh(st.getCell(5,r).getContents());
				
				if(st.getCell(6, r).getContents() != null & "" != st.getCell(6, r).getContents()){
					try{
					server.setGzrq(fmt.format(((DateCell)st.getCell(6, r)).getDate()));
					}catch (Exception e) {
						
					}
			    }
//				server.setGzrq(st.getCell(6, r).getContents().replace('"',' '));
				server.setSynx(st.getCell(7, r).getContents());
				server.setGzje(st.getCell(8, r).getContents());
				server.setBxq(st.getCell(9, r).getContents());
				server.setZt(st.getCell(10, r).getContents());
				server.setSmzqzt(st.getCell(11, r).getContents());
				server.setZrr(st.getCell(12, r).getContents());
				server.setLxdh(st.getCell(13, r).getContents());
				server.setSydd(st.getCell(14, r).getContents());
				server.setIp(st.getCell(15, r).getContents());
				server.setWl(st.getCell(16, r).getContents());
				server.setYu(st.getCell(17, r).getContents());
				
				if(st.getCell(18, r).getContents() != null & "" != st.getCell(18, r).getContents()){
					try{
					server.setQysj(fmt.format(((DateCell)st.getCell(18, r)).getDate()));
					}
					catch (Exception e) {
						
					}
			    }
//				server.setQysj(st.getCell(18, r).getContents().replace('"',' '));
				server.setYpxlh(st.getCell(19, r).getContents());
				server.setJbpz(st.getCell(20, r).getContents());
				server.setZyyt(st.getCell(21, r).getContents());
			    server.setSfbgm(st.getCell(22, r).getContents());
				serverList.add(server);
			}
			}*/
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
		}
		return serverList;
	}
}
