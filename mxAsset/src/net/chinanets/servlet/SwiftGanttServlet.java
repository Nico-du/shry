package net.chinanets.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.chinanets.service.CommonService;

import org.springframework.context.ApplicationContext;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.yuxingwang.gantt.GanttChart;
import com.yuxingwang.gantt.LogoView;
import com.yuxingwang.gantt.model.GanttModel;
import com.yuxingwang.gantt.model.Task;
import com.yuxingwang.gantt.ui.TimeUnit;

//@SuppressWarnings("unused")
public class SwiftGanttServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public SwiftGanttServlet() {
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
		/*response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache"); 
		response.setHeader("Pragma", "no-cache"); 
		response.setDateHeader("Expires", 0);
		OutputStream out = response.getOutputStream();
		String path = request.getRealPath("/");
		String pid = request.getParameter("id");
		try {    
		ServletContext servletContext = request.getSession()
				.getServletContext();
		ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		CommonService cs = (CommonService) app.getBean("commonService");

		ProjectXm xm = (ProjectXm) cs.getObjectById(new ProjectXm(), Long
				.parseLong(pid));
		String start = xm.getSsrq();
		String end = xm.getWcrq();
		XmProcess bz = new XmProcess();
		bz.setParentID(Long.parseLong(pid));
		List bzList = new ArrayList();
		bzList = cs.getObjectList(bz, " 1=1 order by bzdesc asc");

		// 创建甘特图

		
		 LogoView logo = new LogoView(new GanttChart()); logo.setText("创至科技");
		 GanttChart gantt = logo.getGanttChart();
		 
		gantt.setBackground(Color.white);
		gantt.setTimeUnit(TimeUnit.Day);
		gantt.setFont(new Font("Dialog", 1, 14));
		GanttModel model = new GanttModel();
		Calendar s1 = calendar(start);
		Calendar s2 = calendar(end);
		model.setKickoffTime(s1);
		model.setDeadline(s2);

		Task taskGroup = new Task(xm.getXmmc(), s1, s2);
		taskGroup.setBackcolor(Color.GREEN);

		Task[] group = new Task[bzList.size()];
		for (int i = 0; i < bzList.size(); i++) {
			XmProcess p = (XmProcess) bzList.get(i);
			Task task = null;
			if (i == 0) {
				task = new Task(p.getBzmc(), s1, calendar(p.getBztime()));
			} else if (i == bzList.size() - 1) {
				XmProcess m = (XmProcess) bzList.get(i - 1);
				task = new Task(p.getBzmc(), calendar(m.getBztime()), s2);
				task.addPredecessor(group[i - 1]);
			} else {
				XmProcess r = (XmProcess) bzList.get(i - 1);
				task = new Task(p.getBzmc(), calendar(r.getBztime()),
						calendar(p.getBztime()));
				task.addPredecessor(group[i - 1]);
			}
			task.setBackcolor(Color.RED);
			// task.setBackcolor(createRandomColor());
			group[i] = task;
		}
		taskGroup.add(group);
		model.addTask(taskGroup);
		gantt.setModel(model);
		String filePath = path + "gantt.png";
		//pressImage(path+"QQ.png",filePath,0,0);
		gantt.generateImageFile(filePath);
		*//**
		 * 清空缓存
		 *//*
		taskGroup.removeAllChildren();
		model.removeAll();
		gantt.removeAll();
		
		 * 读取图片文件
		 
	
			InputStream in = new FileInputStream(filePath);
			byte[] byt = new byte[1024];
			int len;
			while ((len = in.read(byt)) != -1) {
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
/*
		response.setContentType("text/html");
		OutputStream out = response.getOutputStream();
		String path = request.getRealPath("/");
		String pid = request.getParameter("id");

		ServletContext servletContext = request.getSession()
				.getServletContext();
		ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		CommonService cs = (CommonService) app.getBean("commonService");

		ProjectXm xm = (ProjectXm) cs.getObjectById(new ProjectXm(), Long
				.parseLong(pid));
		String start = xm.getSsrq();
		String end = xm.getWcrq();
		XmProcess bz = new XmProcess();
		bz.setParentID(Long.parseLong(pid));
		List bzList = new ArrayList();
		bzList = cs.getObjectList(bz, " 1=1 order by bzdesc asc");

		// 创建甘特图

		GanttChart gantt = new GanttChart();
		gantt.setBackground(Color.white);

		gantt.setTimeUnit(TimeUnit.Day);
		gantt.setFont(new Font("Dialog", 1, 14));
		GanttModel model = new GanttModel();
		Calendar s1 = calendar(start);
		Calendar s2 = calendar(end);
		model.setKickoffTime(s1);
		model.setDeadline(s2);

		Task taskGroup = new Task(xm.getXmmc(), s1, s2);
		taskGroup.setBackcolor(Color.GREEN);

		Task[] group = new Task[bzList.size()];
		for (int i = 0; i < bzList.size(); i++) {
			XmProcess p = (XmProcess) bzList.get(i);
			Task task = null;
			if (i == 0) {
				task = new Task(p.getBzmc(), s1, calendar(p.getBztime()));
			} else if (i == bzList.size() - 1) {
				XmProcess m = (XmProcess) bzList.get(i - 1);
				task = new Task(p.getBzmc(), calendar(m.getBztime()), s2);
				task.addPredecessor(group[i - 1]);
			} else {
				XmProcess r = (XmProcess) bzList.get(i - 1);
				task = new Task(p.getBzmc(), calendar(r.getBztime()),
						calendar(p.getBztime()));
				task.addPredecessor(group[i - 1]);
			}
			task.setBackcolor(Color.RED);
			// task.setBackcolor(createRandomColor());
			group[i] = task;
		}
		taskGroup.add(group);
		model.addTask(taskGroup);
		gantt.setModel(model);
		String filePath = path + "gantt.png";
		gantt.generateImageFile(filePath);
	
	
		taskGroup.removeAllChildren();
		model.removeAll();
		gantt.removeAll();
		try {
			InputStream in = new FileInputStream(filePath);
			byte[] byt = new byte[1024];
			int len;
			while ((len = in.read(byt)) != -1) {
				out.write(byt, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		*/
	}

	public  void pressImage(String pressImg, String targetImg,
			int x, int y) {
		try {
			// 目标文件
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			File _filebiao = new File(pressImg);
			Image src_biao = ImageIO.read(_filebiao);
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.drawImage(src_biao, (wideth - wideth_biao) / 2,
					(height - height_biao) / 2, wideth_biao, height_biao, null);
			// 水印文件结束
			g.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Color createRandomColor() {
		return new Color((new Double(Math.random() * 128)).intValue() + 128,
				(new Double(Math.random() * 128)).intValue() + 128,
				(new Double(Math.random() * 128)).intValue() + 128);
	}

	private Calendar calendar(String time) {
		String[] s = time.split("-");
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(s[0]), Integer.parseInt(s[1]) - 1, Integer
				.parseInt(s[2]));
		// GregorianCalendar sg= new GregorianCalendar(Integer.parseInt(s[0]),
		// Integer.parseInt(s[1])-1, Integer.parseInt(s[2]));
		return c;
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
