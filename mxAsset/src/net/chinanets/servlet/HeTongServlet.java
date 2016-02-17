package net.chinanets.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.chinanets.service.CommonService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Blob;


public class HeTongServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public HeTongServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

/*
		response.setContentType("text/html");
		OutputStream out = response.getOutputStream();
		try {
			String pid = request.getParameter("id");
			ServletContext servletContext = request.getSession()
					.getServletContext();
			ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			CommonService cs = (CommonService) app.getBean("commonService");
			Hetong doc = (Hetong) cs.getObjectById(new Hetong(),
					Long.parseLong(pid));
			String type=doc.getType();
			if(".doc".equals(type) || ".DOC".equals(type)){
				response.setContentType("application/msword");
			}else if(".pdf".equals(type) || ".PDF".equals(type)){
				response.setContentType("application/pdf");
			}else if(".xls".equals(type) || ".XLS".equals(type)){
				response.setContentType("application/vnd.ms-excel");
			}else if(".gif".equals(type) || ".GIF".equals(type)){
				response.setContentType("image/gif");
			}
			Blob nr = doc.getNr();
			InputStream in = nr.getBinaryStream();
			byte[] byt = new byte[1024];
			int len;
			while((len=in.read(byt))!= -1){
				out.write(byt, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}*/
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		List lt = null;
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
					//String name = item.getFieldName();
					String fileName = item.getName();
					//获得文件名类型
					int index = fileName.lastIndexOf(".");
					String docType=fileName.substring(index);
					String docName=fileName.substring(0, index);
					//获得时间
					Date date=new Date();
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
					String time=format.format(date);
					
					//获得dao对象
					ServletContext servletContext = request.getSession()
							.getServletContext();
					ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
							.getWebApplicationContext(servletContext);
					CommonService cs = (CommonService) app
							.getBean("commonService");
					
					 * 初始pojo对象并赋值
					 *        
					 
				String typeID=	request.getParameter("typeid");
				String qyr=	request.getParameter("qyr");
				String bz=request.getParameter("bz");
				Blob nr=Hibernate.createBlob(item.getInputStream());
					Hetong ht = new Hetong();
					ht.setMc(docName);
					ht.setQyr(qyr);
					ht.setSj(time);
					ht.setType(docType);
					ht.setTypeid(Long.parseLong(typeID));
					ht.setNr(nr);
					ht.setBz(bz);
					cs.saveObject(ht);
				}
			}
          out.write("success");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}*/
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
	 
	 
	 public static String removeSpaces(String s) {
	        StringTokenizer st = new StringTokenizer(s, " ", false);
	        String t = "";
	        while (st.hasMoreElements()) {
	            t += st.nextElement();
	        }
	        return t;
	    }

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
