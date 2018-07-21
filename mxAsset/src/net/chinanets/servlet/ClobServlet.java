package net.chinanets.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.chinanets.pojos.ShryUploadfileData;
import net.chinanets.service.CommonService;
import net.chinanets.utils.CommonMethods;
import net.chinanets.utils.Hanzi2Pinyin;

import java.sql.Blob;
import java.text.SimpleDateFormat;

import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Hibernate;
@SuppressWarnings("unused")
public class ClobServlet extends HttpServlet {

	public ClobServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}
//执行删除
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		OutputStream out = response.getOutputStream();
		try {
			String pid = request.getParameter("docid");
			String nr = request.getParameter("filePath");
			String path = request.getRealPath("/")+"sysArgFiles/"+nr;
			//获得dao对象
			ServletContext servletContext = request.getSession()
					.getServletContext();
			ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			CommonService cs = (CommonService) app
					.getBean("commonService");
			/*
			 * 初始pojo对象并赋值
			 */
		
			ShryUploadfileData doc = new ShryUploadfileData();
			doc.setDocid(Long.parseLong(pid));
			cs.deleteObject(doc);
			// 删除上传文件
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			/*
			if(".doc".equals(type) || ".DOC".equals(type)){
				response.setContentType("application/msword");
			}else if(".pdf".equals(type) || ".PDF".equals(type)){
				response.setContentType("application/pdf");
			}else if(".xls".equals(type) || ".XLS".equals(type)){
				response.setContentType("application/vnd.ms-excel");
			}else if(".gif".equals(type) || ".GIF".equals(type)){
				response.setContentType("image/gif");
			}
			response.sendRedirect("/asset/uploadFile/"+nr);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}
	
	
//执行上传
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//缺少事务回滚的方法
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		List lt = new ArrayList();
		try {
			// 上传文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String path = request.getRealPath("/");
			factory.setRepository(new File(path));
			factory.setSizeThreshold(1024 * 1024);
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> list = upload.parseRequest(request);

			for (FileItem item : list) {
				if (item.isFormField()) {
				} else {
					String value =trimAll(item.getName());
					
					int index = value.lastIndexOf(".");
					String docType=value.substring(index+1);
					String docName=value.substring(0, index);
					String fileSize = item.getSize()+"";
					//获得时间
					Date date=new Date();
					SimpleDateFormat format=new SimpleDateFormat("yyMMddHHmmss");
					String time=format.format(date);
					
					Hanzi2Pinyin hanziPinyin = new Hanzi2Pinyin();
					value = java.net.URLEncoder.encode(hanziPinyin.getPinYin(value));//转意中文和特殊字符 下载使用URL下载，需要进行URL转意
					String docNr=time+value;
					docNr = CommonMethods.replaceSpecStr(docNr.substring(0,docNr.lastIndexOf(".")))+docNr.substring(docNr.lastIndexOf("."));
					//写入文件
					//float l=item.getSize()/1024;
					File fldir = new File(path + CommonMethods.UploadFile_BasePath+docNr.substring(0,4));
					fldir.mkdirs();
					item.write(new File(path +CommonMethods.UploadFile_BasePath + docNr.substring(0,4),docNr));
					
					//获得dao对象
					ServletContext servletContext = request.getSession()
							.getServletContext();
					ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
							.getWebApplicationContext(servletContext);
					CommonService cs = (CommonService) app.getBean("commonService");
					
					/*
					 * 初始pojo对象并赋值
					 */
				
					ShryUploadfileData doc = new ShryUploadfileData();
					doc.setFilesize(fileSize);
					doc.setFilename(docName);
					doc.setFilepath(docNr.substring(0,4)+"/"+docNr);
					doc.setFiletype(docType);
	                doc.setTablename(request.getParameter("tablename"));
	                String rq=new SimpleDateFormat("yyyy-MM-dd").format(date);
	                doc.setUploaddate(rq);
	                
					String pid=request.getParameter("dataid");
					if(pid != null && pid.length() > 0)doc.setDataid(pid);
					String fjColumnname=request.getParameter("columnname");
					if(fjColumnname != null && fjColumnname.length() > 0)doc.setColumnname(fjColumnname);
					String datatype=request.getParameter("datatype");
					if(StringUtils.isBlank(datatype))datatype="1";
					doc.setDatatype(datatype);
					String sydid=request.getParameter("sydid");
					if(StringUtils.isNotBlank(datatype)){doc.setSydid(NumberUtils.toLong(sydid));}
				//	System.out.println("pid="+pid);
				//	System.out.println("xmid="+xmid);
					//dao添加
					cs.saveObject(doc);
				}
			}
			
          out.write("success");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}
	
	  private String trimAll(String str) {
	        StringBuilder sb = new StringBuilder();
	        char c = ' ';
	        for (int i = 0; i < str.length(); i++) {
	            char s = str.charAt(i);
	            if (s != c) {
	                sb.append(s);
	            }
	        }
	        return sb.toString();
	    }

	   
		private  String removeSpaces(String s) {
	        StringTokenizer st = new StringTokenizer(s, " ", false);
	        String t = "";
	        while (st.hasMoreElements()) {
	            t += st.nextElement();
	        }
	        return t;
	    }

	public void init() throws ServletException {

	}

}
