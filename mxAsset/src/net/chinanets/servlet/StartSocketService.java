package net.chinanets.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.chinanets.dao.CommonDao;
import net.chinanets.service.CommonService;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartSocketService extends HttpServlet {

	private static final int PORT = 8888; // 端口号
	private static List<Socket> list = new ArrayList<Socket>(); // 保存连接对象
	private ExecutorService exec;
	private ServerSocket server;
//	private static List<CaseComputer> caseComputerList = new ArrayList<CaseComputer>();
	private static CommonDao commonDao;

	public static void setCommonDao(CommonDao commonDao) {
		StartSocketService.commonDao = commonDao;
	}
	
	public void init() throws ServletException {
		
	}
/*
 * 内部结收客户端发送的数据信息类
 */
	static class ChatTask implements Runnable {
		private Socket socket;
		private BufferedReader br;
	//	private PrintWriter pw;
		private String msg;

		public ChatTask(Socket socket) throws IOException {
			this.socket = socket;
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
		}
		public void run() {
			try {
				StringBuilder xml = new StringBuilder();
				while ((msg = br.readLine()) != null) {
					if (msg.trim().equals("</Doc>")) {
						OutputStream out = socket.getOutputStream();
						byte[] byt = new byte[2];
						byt[0] = 'o';
						byt[1] = 'k';
						out.write(byt);
						list.remove(socket);
						br.close();
						socket.close();
						System.out.println("客户端关闭");
						break;
					} else {
						xml.append(msg.trim());
					}
				}
				//StringReader r = new StringReader(new String((xml.toString()+"</Doc>").getBytes("iso-8859-1"),"gb2312"));
				StringReader r = new StringReader((xml.toString()+"</Doc>"));
				String s=xml.toString();
				
				s=new String(s.getBytes(),"GBK");
//				CaseComputer caseComputer = testReadXml(r);
//				commonDao.saveOrUpdateObject(caseComputer);
			} catch (Exception e) {
				System.out.println("=====客户端关闭======");
//				e.printStackTrace();
			}
		}
	}
/*	*//**
	 * 解析xml树
	 * @param xml
	 *//*
	public static CaseComputer testReadXml(StringReader xml) {
		//String xml1 = "c:/a.xml";
		//File file = new File(xml1);
		SAXReader saxReader = new SAXReader();
		CaseComputer cc = new CaseComputer();
		try {
			Document doc = saxReader.read(xml);
			Element root = doc.getRootElement();
			
			//根据设备编号查询该对象是否存在(不要查询，当做新设备来保存)
			String deviceId = ((Element)root.elementIterator().next()).getTextTrim();
			cc.setTempDeviceId(deviceId);
			List<CaseComputer> tempList = commonDao.getObjectByExample(cc);
			if(tempList != null && tempList.size() > 0) {
				cc = (CaseComputer)tempList.get(0);
				cc.setHdModel("");
				cc.setHdSize("");
				cc.setHdSn("");
				cc.setMemorySize("");
			}
			
			for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				String tempName = element.getName();
				String tempText = element.getTextTrim();
				if("PCId".equals(tempName)) {
					cc.setTempDeviceId(tempText);
				}else if("DisplayVersion".equals(tempName)) {
					cc.setS360Version(tempText);
				}else if("AntivirusName".equals(tempName)) {
					cc.setAntivirusName(tempText);
				}else if("AntivirusVersion".equals(tempName)) {
					cc.setAntivirusVersion(tempText);
				}
				for (Iterator nodes = element.elementIterator(); nodes
						.hasNext();) {
					Element node = (Element) nodes.next();
					String tempNodeName = node.getName();
					String tempNodeText = node.getTextTrim();
					if("Win32_OperatingSystem".equals(tempName)) {
						if("Name".equals(tempNodeName)) {
							cc.setOsVersion(tempNodeText);
						}else if("CSDVersion".equals(tempNodeName)) {
							cc.setSystemPatch(tempNodeText);
						}
					}else if("Win32_BaseBoard".equals(tempName)) {
						if("Product".equals(tempNodeName)) {
							cc.setMotherboardModel(tempNodeText);
						}else if("Manufacturer".equals(tempNodeName)) {
							cc.setMotherboardName(tempNodeText + cc.getMotherboardModel());
						}
					}else if("Win32_BIOS".equals(tempName)) {
						if("SMBIOSBIOSVersion".equals(tempNodeName)) {
							cc.setBiosModel(tempNodeText);
						}else if("SerialNumber".equals(tempNodeName)) {
							cc.setMotherboardSn(tempNodeText);
						}
					}else if("Win32_DiskDrive".equals(tempName)) {
						if("Size".equals(tempNodeName)) {
							if(null==cc.getHdSize() || "".equals(cc.getHdSize())){
								cc.setHdSize(tempNodeText);
							}
							else{
								if(null==cc.getHdSize1()){
								  cc.setHdSize1(tempNodeText);
							    }else{
							    	if(null==cc.getHdSize2()){
								 cc.setHdSize2(tempNodeText);}
							    	else{ if(null==cc.getHdSize3()){
								cc.setHdSize3(tempNodeText);
							}}
							}
							
							}
						}else if("Model".equals(tempNodeName)) {
							if(null==cc.getHdModel() || "".equals(cc.getHdModel()))
							cc.setHdModel(tempNodeText);
							else {if(null==cc.getHdModel1())
								cc.setHdModel1(tempNodeText);
							else {if(null==cc.getHdModel2())
								cc.setHdModel2(tempNodeText);
							else {if(null==cc.getHdModel3())
								cc.setHdModel3(tempNodeText);}}}
							
							
						}else if("Signature".equals(tempNodeName)) {
							if(null==cc.getHdSn() || "".equals(cc.getHdSn()))
							cc.setHdSn(tempNodeText);
							else {if(null==cc.getHdSn1() )
								cc.setHdSn1(tempNodeText);else {if(null==cc.getHdSn2())
								cc.setHdSn2(tempNodeText);else {if(null==cc.getHdSn3())
								cc.setHdSn3(tempNodeText);}}}
							
							
						}
					}else if("Win32_NetworkAdapterConfiguration".equals(tempName)) {
						if(cc.getIpAddress() == null || "".equals(cc.getIpAddress())) {
							if("MACAddress".equals(tempNodeName)) {
								if(tempNodeText != null && !"".equals(tempNodeText)) {
									cc.setMacAddress(tempNodeText);
								}
							}else if("IPAddress".equals(tempNodeName)) {
								if(tempNodeText != null && !"".equals(tempNodeText) && !", 0.0.0.0".equals(tempNodeText)) {
									cc.setIpAddress(tempNodeText.substring(2, tempNodeText.length()));
								}
							}
						}
					}else if("Win32_PhysicalMemory".equals(tempName)) {
						if("Capacity".equals(tempNodeName)) {
							if(null==cc.getMemorySize() || "".equals(cc.getMemorySize()))
								cc.setMemorySize(tempNodeText);
							
							else {if(null==cc.getMemorySize1())
								cc.setMemorySize1(tempNodeText);
							else {if(null==cc.getMemorySize2())
								cc.setMemorySize2(tempNodeText);
							else {if(null==cc.getMemorySize3())
								cc.setMemorySize3(tempNodeText);
							}}
							}
						}else if("MemoryType".equals(tempNodeName)) {
							cc.setMemoryModel(tempNodeText);
						}
					}else if("Win32_Processor".equals(tempName)) {
						if("SocketDesignation".equals(tempNodeName)) {
							cc.setCpuSocket(tempNodeText);
						}else if("Name".equals(tempNodeName)) {
							cc.setCpuName(tempNodeText);
							cc.setCpuModel(tempNodeText);
						}else if("L2CacheSize".equals(tempNodeName)) {
							cc.setLevel2Cache(tempNodeText);
						}else if("NumberOfCores".equals(tempNodeName)) {
							cc.setCpuCore(tempNodeText);
						}else if("ProcessorId".equals(tempNodeName)) {
							cc.setCpuNum(tempNodeText);
						}
					}else if("Win32_CacheMemory".equals(tempName)){
						if("MaxCacheSize".equals(tempNodeName)){
							cc.setCpuCore(tempNodeName);
						}
						
					}else if("Win32_VideoController".equals(tempName)) {
						if("Description".equals(tempNodeName)) {
							cc.setGraphicsModel(tempNodeText);
						}else if("AdapterRAM".equals(tempNodeName)) {
							cc.setGraphicsMemorySize(tempNodeText);
						}
					}else if("Win32_UserAccount".equals(tempName)) {
						if("Domain".equals(tempNodeName)) {
							cc.setComputerName(tempNodeText);
						}
					}else if("Win32_CDROMDrive".equals(tempName)){
						if("Name".equals(tempNodeName)){
							if(null==cc.getCdRomDrive()){
								cc.setCdRomDrive(tempNodeText);
							}else {
								cc.setCdRomDrive1(tempNodeText);
							}
							
						}
					}
				}
			}
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD");
			String stringDate = format.format(date);
			cc.setCollectDate(stringDate);
			cc.setIsNewItem("是");
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			return cc;
		}
	}*/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		if("close".equals(request.getParameter("method"))) {
			server = (ServerSocket)session.getAttribute("server");
			if(server != null && !server.isClosed()) {
				server.close();
			}
		}else {
		
			Socket client = null;
			try {
				ServletContext servletContext = request.getSession()
					.getServletContext();
				ApplicationContext ap = org.springframework.web.context.support.WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
				commonDao = (CommonDao) ap
					.getBean("commonDao");
				//System.out.println("开始");
				server = (ServerSocket)servletContext.getAttribute("server");
//				server = (ServerSocket)request.getSession().getAttribute("server");
				if(server == null || server.isClosed()) {
					server = new ServerSocket(PORT);
					servletContext.setAttribute("server", server);
				}
				exec = Executors.newCachedThreadPool();
				while (!server.isClosed()) {
					client = server.accept(); // 接收客户连接
					list.add(client);
					exec.execute(new ChatTask(client));
				} 
			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("服务已关闭");
			}
			
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

}
