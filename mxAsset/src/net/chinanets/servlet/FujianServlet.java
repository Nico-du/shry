package net.chinanets.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

public class FujianServlet extends HttpServlet {


	public FujianServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
        String file=  "\\"+request.getParameter("file");
        String path = request.getRealPath("/")+request.getParameter("name")+file;
        File f = new File(path);
        if(f.exists()){
        	f.delete();
        }
        out.write("");
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String opName = "";

		try {
			// 获取上传文件流，写入到服务器文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String path = request.getRealPath("/")+request.getParameter("name");
//			String filename = request.getParameter("filename");//接收到url的参数，但是接收到的是乱码
//			String str = new URLDecoder().decode(filename,"utf-8");//进行转码
			
			factory.setRepository(new File(path));
			factory.setSizeThreshold(1024 * 1024);
			ServletFileUpload upload = new ServletFileUpload(factory);
			List list = upload.parseRequest(request);
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {
				} else {
	
					String fileName = item.getName();
					//获得文件名类型
					int index = fileName.lastIndexOf(".");
					String docType=fileName.substring(index);
				//	String docName=fileName.substring(0, index);
					
					//获得时间
					Date date=new Date();
					SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhssmm");
					String time=format.format(date);
					opName=time+docType;//用时间做为文件名保存
					try {
						item.write(new File(path, opName));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
          out.write(opName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}


	public void init() throws ServletException {
		// Put your code here
	}

}
