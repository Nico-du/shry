package net.chinanets.service.imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import net.chinanets.dao.IQueryDAO;
import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Users;
import net.chinanets.service.IQueryService;

public class QueryServiceImpl implements IQueryService {
	private IQueryDAO queryDAO;

	public void setQueryDAO(IQueryDAO queryDAO) {
		this.queryDAO = queryDAO;
	}

	public int getCountByObject(Object obj) {
		return queryDAO.getCountByExample(obj);
	}

	public List getObjectList(Object obj, int pageSize, int pageNumber,
			String condition) {
		List list = queryDAO.getObjectByExample(obj, pageSize, (pageNumber-1)*pageSize, condition);
		return list;
	}

	public List getObjectList(Object obj, int pageSize, int pageNumber) {
		return queryDAO.getObjectByExample(obj, pageSize, (pageNumber-1)*pageSize);
	}

	public List executeJdbcSql(String jdbcSql, int columns) {
		return queryDAO.executeJdbcSql(jdbcSql, columns);
	}

	public int getCountByObject(Object obj, String condition) {
		// TODO Auto-generated method stub
		return queryDAO.getCountByExample(obj, condition);
	}
	
	public List getObjectList(Object obj) {
		return queryDAO.getObjectByExample(obj);
	}
	
	public List getObjectList(Object obj,String condition) {
		return queryDAO.getObjectByExample(obj,condition);
		
	}

	public String getBmzc(String type,String name) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		/*AssetComputer ac = new AssetComputer();
		AssetWssb wssb=new AssetWssb();
		AssetJz aj = new AssetJz();
		AssetServer server = new AssetServer();
		AssetStorage storage = new AssetStorage();
		AssetNet net = new AssetNet();
		AssetVideo video = new AssetVideo();
		
		List list = null;
		//List ajList = null;
		//int num = 0;
		//sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<root>");
		if(type.equals("bmmc")) {
			//部门
			//PC
			ac.setZclxid(Long.valueOf(361));
			ac.setBmmc(name);
			list = getObjectList(ac, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='PC机' num='"+list.size()+"'></asset>");
			
			ac =new AssetComputer();
			ac.setZclxid(Long.valueOf(362));
			ac.setBmmc(name);
			list = getObjectList(ac, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='笔记本' num='"+list.size()+"'></asset>");
			//介质
			aj.setBmmc(name);
			list = getObjectList(aj, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='介质' num='"+list.size()+"'></asset>");
		} else if (type.equals("zsxm")){
			//责任人
			//PC
			ac.setZclxid(Long.valueOf(361));
			ac.setZrr(name);
			list = getObjectList(ac, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='PC机' num='"+list.size()+"'></asset>");
			
			ac =new AssetComputer();
			ac.setZclxid(Long.valueOf(362));
			ac.setZrr(name);
			list = getObjectList(ac, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='笔记本' num='"+list.size()+"'></asset>");
			
			//介质
			aj.setZrr(name);
			list = getObjectList(aj, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='介质' num='"+list.size()+"'></asset>");
			//打印机
			wssb.setZrr(name);wssb.setZclxid(Long.parseLong("364"));
			list = getObjectList(wssb, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='打印机' num='"+list.size()+"'></asset>");
			//扫描仪
			wssb.setZrr(name);wssb.setZclxid(Long.parseLong("365"));
			list = getObjectList(wssb, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='扫描仪' num='"+list.size()+"'></asset>");
			//传真机
			wssb.setZrr(name);wssb.setZclxid(Long.parseLong("366"));
			list = getObjectList(wssb, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='传真机' num='"+list.size()+"'></asset>");
			//主机   
			server.setZrr(name);
			list = getObjectList(server, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='主机' num='"+list.size()+"'></asset>");
			//存储设备
			storage.setZrr(name);
			list = getObjectList(storage, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='存储设备' num='"+list.size()+"'></asset>");
			//网络设备
			net.setZrr(name);
			list = getObjectList(net, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='网络设备' num='"+list.size()+"'></asset>");
			//视频设备
			video.setZrr(name);
			list = getObjectList(video, "");
			if(list!=null&&list.size()!=0) sb.append("<asset label='视频设备' num='"+list.size()+"'></asset>");
		}
	
		sb.append("</root>");
	//	System.out.println(sb);
*/		return sb.toString();
	}
	
	//获取部门组织结构
	public String getBmjg(String deptName) {
		StringBuffer sb = new StringBuffer();
		List list = null;
		sb.append("<root>");
		Users user = new Users();
		String temp = null;
		if(deptName==null||"".equals(deptName)) {
			List deptList = queryDAO.getDistinctDept();
			for(int j=0;j<deptList.size();j++) {
				//System.out.println(list.get(j));
				temp = (String)deptList.get(j);
				sb.append("<dept name='bmmc' label='"+temp+"'>");
				list = getObjectList(new Users(), "bmmc like '"+temp+"'");
				if(list!=null) {
					for(int i=0;i<list.size();i++) {
						user = (Users)list.get(i);
						sb.append("<user name='zsxm' label='"+user.getZsxm()+"'></user>");
					}
				}
				sb.append("</dept>");
			}
		} else {
			list = getObjectList(new Users(), " bmmc like '"+deptName+"'");
			sb.append("<dept name='bmmc' label='"+deptName+"'>");
			if(list!=null) {
				for(int i=0;i<list.size();i++) {
					user = (Users)list.get(i);
					sb.append("<user name='zsxm' label='"+user.getZsxm()+"'></user>");
				}
			}
			sb.append("</dept>");
		}
		sb.append("</root>");
	
		return sb.toString();
	}

	public List executerHsql(String sql, int columns) {
		// TODO Auto-generated method stub
		return queryDAO.executerHsql(sql, columns);
	}
}
