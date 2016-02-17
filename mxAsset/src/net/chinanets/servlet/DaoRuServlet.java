package net.chinanets.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.*;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileUploadException;

public class DaoRuServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public DaoRuServlet() {
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

		doPost(request,response);
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
	  private static final long serialVersionUID = 1L;   
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();   
		  ServletFileUpload fileUpload = new ServletFileUpload(factory);   
	        fileUpload.setSizeMax(1024 * 1025 * 1024);   
	  
	        try {   
	            List items = fileUpload.parseRequest(request);   
	            Iterator iter = items.iterator();   
	            while (iter.hasNext()) {   
	                FileItem item = (FileItem) iter.next();   
	  
	                if (item.isFormField()) {   
	                    String name = item.getFieldName();   
	                    String value = item.getString();   
	                    System.out.println(name + ":" + value);   
	                } else {   
	                    String fieldName = item.getFieldName();   
	                    String fileName = item.getName();   
	                    String contentType = item.getContentType();   
	                    boolean isInMemory = item.isInMemory();   
	                    long sizeInBytes = item.getSize();   
	                    String path = getServletContext().getRealPath("/");   
	                    File uploadedFile = new File(path,fileName);   
	                    item.write(uploadedFile);   
	                }   
	            }   
	        } catch (FileUploadException e) {   
	            e.printStackTrace();   
	        } catch (Exception e) {   
	            e.printStackTrace();   
	        }   
	  

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String st = request.getRealPath("/")+ "plugin/imp_neiwang.bat";
		Runtime.getRuntime().exec("cmd /c start "+st);
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
