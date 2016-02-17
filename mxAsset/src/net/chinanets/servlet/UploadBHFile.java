package net.chinanets.servlet;

import java.io.File;
import java.io.IOException;
import  net.chinanets.service.CommonService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;

import net.chinanets.dao.CommonDao;

public class UploadBHFile extends HttpServlet {

	private static final long serialVersionUID = 5425836142860976977L;
	
	// 定义文件的上传路径
	private String uploadPath = "";
	private String re = "0";
	// 限制文件的上传大小
	private int maxPostSize = 100 * 1024 * 1024; // 最大100M
	static CommonService cpt ;
	private String fname="";
	
	private static CommonDao commonDao;
	
	/**
	 * Constructor of the object.
	 */
	public UploadBHFile() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Access !");
		response.setContentType("text/html;charset=UTF-8");
		// 保存文件到服务器中
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("ISO8859_1");//fileitem对中文的支持不好，因此需要在两个地方进行转换
		upload.setSizeMax(maxPostSize);
		
		try {
			List fileItems = upload.parseRequest(request);
			Iterator iter = fileItems.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if(item.getFieldName().equals("fname")) {
					String   fileName=item.getString();  
					fileName   =   new   String(fileName.getBytes("ISO8859_1"),"utf-8");//第二次转换
					fname = fileName;
				}
				
				if (!item.isFormField()) {
					
				   	String name;
					 name = item.getName();
					try {
						System.out.println(uploadPath+fname+"/"+name);
						refreshFileList(name);
//						String[] na = name.split("\\.");
	//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//				try{
			//				sdf.parse(na[0]);
				//		}
					//	catch (Exception e) {
							// TODO: handle exception
						//	re="2";
						//}
						
						if(re=="1"){
							//response.getWriter().write("文件"+name +"已存在，上传失败。");
							//re="0";
							//return;
						}
						if(re=="2"){
							response.getWriter().write("文件格式错误，上传失败。");
							re="0";
							return;
						}
						item.write(new File(uploadPath +fname+"/" + name));
//						testReadXml(uploadPath+name);
						response.getWriter().write("上传成功。");
						
					} catch (Exception e) {
						e.printStackTrace();
						response.getWriter().write(e.getMessage());
					}
					
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			response.getWriter().write(e.getMessage());
			System.out.println(e.getMessage() + "结束");
		} 
			//refreshFileList("C:/Program Files/a");
		
	}
	/**
	 * 获得所有txt文件名
	 * @param strPath
	 */

		
		 
		 public void refreshFileList(String strPath) throws Exception {
			 //盘点文件夹存在
			 File f = new File(uploadPath);
			 File[] sub = f.listFiles();
			 if(sub==null){
				  f.mkdirs();
//				  return;
			 }
			 //新建的文件夹是否存在
			 File file = new File(uploadPath+fname);
			 File[] fileName = file.listFiles();
			 if(fileName==null){
				 file.mkdirs();
			 }
				for(int i=0; i<sub.length; i++){
					if(sub[i].isDirectory()){
//						continue;
						File[] fi = sub[i].listFiles();
						for(int j=0; j<fi.length; j++){
						if(strPath.equals(fi[j].getName())){
							re="1";
							break;
							}
						
						}
						
					}
				}
		  }
	
	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = request.getSession()
		.getServletContext();

		ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		 cpt = (CommonService) ap.getBean("commonService");
			processRequest(request, response); 
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = request.getSession()
		.getServletContext();

		ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		 cpt = (CommonService) ap.getBean("commonService");
		 commonDao = (CommonDao) ap
			.getBean("commonDao");
		 uploadPath = request.getRealPath("/")+"pandian/";
		processRequest(request, response); 
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
	
	public String getServletInfo() { 
        return "Short description"; 
    } 

}
