package net.chinanets.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.chinanets.service.CommonService;

import org.springframework.context.ApplicationContext;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ImpDocumentServlet extends HttpServlet {

	public ImpDocumentServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		FileOutputStream  fileOut=null;
		try {

			ServletContext servletContext = request.getSession()
					.getServletContext();
			ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			CommonService cs = (CommonService) app.getBean("commonService");

			String[] nameItem = request.getParameterValues("name");
			String[] urlItem = request.getParameterValues("url");
			String path = request.getRealPath("/") + "docFile/";
			String pid=request.getParameter("parentID");
			
			Date date =null;
			SimpleDateFormat format = new SimpleDateFormat("yyMMddhhssmm");
			
			for (int i = 0; i < urlItem.length; i++) {
				// 获得时间
			    date = new Date();
				String time = format.format(date);
				URL url = new URL(urlItem[i]);
				
				InputStream in = url.openStream();
			//	File file = new File(path+time+".html");
				fileOut = new FileOutputStream(path+time+".html");

				byte[] byt = new byte[1024];
				int len;
				while ((len = in.read(byt)) != -1) {
					fileOut.write(byt, 0, len);
				}
		
				/*XmDocument doc = new XmDocument();
				doc.setDocMc(nameItem[i]);
				doc.setDocNr(time+".html");
				doc.setDocType("html");
				String rq = new SimpleDateFormat("yy-MM-dd").format(date);
				doc.setDocRq(rq);
				doc.setDocParentid(pid);
				cs.saveObject(doc);*/
			}
			out.write("ok");
		} catch (Exception e) {
			out.write("no");
		} finally {
			fileOut.flush();
			fileOut.close();
			out.flush();
			out.close();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.flush();
		out.close();
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
