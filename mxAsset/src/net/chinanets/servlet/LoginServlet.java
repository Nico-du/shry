package net.chinanets.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.chinanets.pojos.Users;
import net.chinanets.service.CommonService;
import net.chinanets.utils.CommonMethods;

import org.springframework.context.ApplicationContext;


import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

public class LoginServlet extends HttpServlet {

	public LoginServlet() {
		super();
	}

	public void destroy() {
		super.destroy();

	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		String userName = request.getParameter("loginName");
		String param1 = request.getParameter("param1");
		String param2 = (String) request.getAttribute("param1");
		String zsxm = request.getParameter("userName");
		String userId = request.getParameter("userId");
	//	loginName = new String(loginName.getBytes("GBK"));
		ServletContext servletContext = request.getSession().getServletContext();
		ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		CommonService cpt = (CommonService) ap.getBean("commonService");
		List rstList =cpt.getObjectBySql("select count(0) from users where mc='"+userName+"' and zsxm='"+zsxm+"' and id="+userId);
		
		if(!rstList.isEmpty() && Integer.parseInt(rstList.get(0)+"") > 0){
			CommonMethods.LoginedUserArray.add(userId);
			response.sendRedirect("/mxAsset/Login.html?logid="+userId);
		}else{
			response.sendRedirect("/mxAsset/Login.html");
		}
		//	request.getRequestDispatcher("/Login.html").forward(request, response);//sendRedirect("http://localhost:8080/mxAsset");
		/*PrintWriter out = response.getWriter();
        out.println("<h1 color='red'>获取数据启动！！</h1>");
		out.flush();
		out.close();*/
	}

	public void init() throws ServletException {
	}

}
