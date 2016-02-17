package net.chinanets.servlet;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ZipServelt extends HttpServlet {




	public void destroy() {
		super.destroy(); 
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//ZipOutputStream z= new ZipOutputStream();
		

		out.flush();
		out.close();
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SimpleDateFormat format=new SimpleDateFormat("yyMMdd");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String fileName=request.getParameter("title");
		fileName=java.net.URLEncoder.encode(fileName,"utf-8")+format.format(new Date());
        response.setContentType("application/zip");    
        response.setHeader("Content-Disposition", "attachment; filename="+fileName+".zip");   
	    
		String url = request.getRealPath("/")+"docFile/";
		String[] names=request.getParameterValues("name");
        String[] paths=request.getParameterValues("path");
        String[] types=request.getParameterValues("type");

        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());  
        
        for (int i=0;i<names.length;i++){
        	String p=url+paths[i].toString();
        	ZipEntry zipentry = new ZipEntry(names[i]+"."+types[i]);
            out.putNextEntry(zipentry);
            FileInputStream in = new FileInputStream(p);
            int len;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
              out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
		out.flush();
		out.close();
	}



	public void init() throws ServletException {
		// Put your code here
	}

}
