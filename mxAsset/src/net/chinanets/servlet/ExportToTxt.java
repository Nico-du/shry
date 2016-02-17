package net.chinanets.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExportToTxt extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ExportToTxt() {
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
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

		response.setContentType("text/html");
		String[] sbbh = request.getParameterValues("cmpID");
		String path = request.getParameter("path").toString();
		String s = new String();
		  String s1 = new String();
		  try {
		   File f = new File(request.getRealPath("/")+path+".txt");
		   if(f.exists()){
		    System.out.print("文件存在");
		   }else{
		    System.out.print("文件不存在");
		    f.createNewFile();//不存在则创建
		   }
		   BufferedReader input = new BufferedReader(new FileReader(f));
		   
		   while((s = input.readLine())!=null){
		    s1 += s+"\r\n";
		   }
		   System.out.println(s1);
		   input.close();
		   for(int i=0;i<sbbh.length;i++){
			   s1 += sbbh[i]+";"+"\r\n";
		   
		   }
		   BufferedWriter output = new BufferedWriter(new FileWriter(f));
		   output.write(s1);
		   output.close();
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
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
