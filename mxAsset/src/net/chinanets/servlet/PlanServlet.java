package net.chinanets.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.io.BufferedInputStream;   
import java.io.File;   
import java.io.FileInputStream;   
import java.io.FileOutputStream;   
import java.io.IOException;   
import java.io.InputStream;   
import java.io.OutputStream;   
import java.util.Date;   
import java.util.Properties;   

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PlanServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PlanServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	private static String propertyFile = "";   

	/**  
	     * 根据Key 读取Value  
	     *   
	    * @param key  
	     * @return  
	     */  
	    public static String readData(String key) {   
	        Properties props = new Properties();   
	        try {   
	            InputStream in = new BufferedInputStream(new FileInputStream(propertyFile));   
            props.load(in);   
            in.close();   
	            String value = props.getProperty(key);   
	            return value;   
	        } catch (Exception e) {   
	            e.printStackTrace();   
	            return null;   
	        }   
	    }   

	    /**  
	         * 修改或添加键值对 如果key存在，修改 反之，添加。  
	         *   
	         * @param key  
	         * @param value  
	         */  
	        public static void writeData(String key, String value) {   
	            Properties prop = new Properties();   
	            try {   
	                File file = new File(propertyFile);   
	                if (!file.exists())   
	                    file.createNewFile();   
	                InputStream fis = new FileInputStream(file);   
	                prop.load(fis);   
	                fis.close();//一定要在修改值之前关闭fis   
	                OutputStream fos = new FileOutputStream(propertyFile);   
	                prop.setProperty(key, value);   
	                prop.store(fos, "Update '" + key + "' value");   
	                fos.close();   
	            } catch (IOException e) {   
	                System.err.println("Visit " + propertyFile + " for updating "  
	                        + value + " value error");   
	            }   
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
		
		String value = readData("1");
		
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(value);
//		out.println("<HTML>");
//		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
//		out.println("  <BODY>");
//		out.print("    This is ");
//		out.print(this.getClass());
//		out.println(", using the GET method");
//		out.println("  </BODY>");
//		out.println("</HTML>");
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
		propertyFile=request.getRealPath("/")+"data.properties";
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		writeData(key,value);
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		out
//				.println(value);
//		out.println("<HTML>");
//		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
//		out.println("  <BODY>");
//		out.print("    This is ");
//		out.print(this.getClass());
//		out.println(", using the POST method");
//		out.println("  </BODY>");
//		out.println("</HTML>");
//		out.flush();
//		out.close();
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
