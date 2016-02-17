package net.chinanets.service.imp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Employee;
import net.chinanets.pojos.Rules;
import net.chinanets.pojos.Users;
import net.chinanets.service.DeptService;
import net.chinanets.u.Hanzi2Pinyin;

public class DeptServiceImp extends CommonServiceImp implements DeptService {

	/**
	 * 添加设备清单 部门
	 */
	public String getSyBm() {
		Document document = org.dom4j.DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Dept dept = new Dept();
		List listDept = super.getObjectList(dept);
		for (Iterator iterator = listDept.iterator(); iterator.hasNext();) {
			Dept deptIter = (Dept) iterator.next();
			Element deptElement = root.addElement("dept");
			deptElement.addAttribute("label", deptIter.getMc());
			deptElement.addAttribute("id", deptIter.getId().toString());
		}
		// System.out.println(document.asXML());
		return document.asXML();
	}

	/**
	 * 返回部门组织结构树
	 * 
	 * @return
	 
	public String getBmzz() {
		Document document = org.dom4j.DocumentHelper.createDocument();
		Element root = document.addElement("root");
		root.addAttribute("label", "局管部门（馆）");
		Dept dept = new Dept();
		List listDept = super.getObjectList(dept);
		for (Iterator iterator = listDept.iterator(); iterator.hasNext();) {
			Dept deptIter = (Dept) iterator.next();
			Element deptElement = root.addElement("dept");
			deptElement.addAttribute("label", deptIter.getMc());
			deptElement.addAttribute("id", deptIter.getId().toString());
			List empList = deptIter.getEmployees();
			for (Iterator iterator2 = empList.iterator(); iterator2.hasNext();) {
				Employee employee = (Employee) iterator2.next();
				//Users user = employee.getUsers();
				Users user=(Users)super.getObjectById(new Users(),employee.getId());
				Element empElement = deptElement.addElement("employee");
				if (user != null) {
					empElement.addAttribute("label", employee.getXm()
							+ "(已有帐号)");
					empElement.addAttribute("mc", user.getMc());
				} else {
					empElement.addAttribute("label", employee.getXm());
					empElement.addAttribute("id", employee.getId().toString());
					empElement.addAttribute("name", employee.getLoginName());
					empElement.addAttribute("emp", "true");
				}
			}
		}
		return document.asXML();
	}
	*/
	public String getBmzz(){
		return getBmzzXML(true);
	}
	private String getBmzzXML(boolean showSelect) {
		Document document = org.dom4j.DocumentHelper.createDocument();
		Element root = document.addElement("root");
		root.addAttribute("label", "部门组织结构");
		//局管部门
		List<Dept> listDeptAll = (List<Dept>) super.RunSelectClassBySql("select * from dept order by SHOW_ORDER asc","net.chinanets.pojos.Dept");
		
		Map<String,Element> treeMap = new LinkedHashMap<String,Element>();
		int k=0;
		Dept deptIterS1;
		Element tempElement;
		for(Iterator<Dept> iterator = listDeptAll.iterator(); iterator.hasNext();k++){
			deptIterS1 = iterator.next();
			tempElement=DocumentHelper.createElement("node");
			tempElement.addAttribute("id", deptIterS1.getId().toString());
			tempElement.addAttribute("label", deptIterS1.getMc());
			if(showSelect){
				tempElement.addAttribute("isRule","0");
				tempElement.addAttribute("isSelect","0");
			}
			treeMap.put(deptIterS1.getId().toString(),tempElement);
		}
		for(Iterator<Dept> iterator = listDeptAll.iterator(); iterator.hasNext();){
			deptIterS1 = iterator.next();
			if(deptIterS1.getParentid()!=null){
				if(showSelect){
					((Element)treeMap.get(deptIterS1.getParentid().getId().toString())).setAttributeValue("isRule", "0");
					((Element)treeMap.get(deptIterS1.getParentid().getId().toString())).setAttributeValue("isSelect", "0");
					((Element)treeMap.get(deptIterS1.getId().toString())).setAttributeValue("isRule", "1");
					((Element)treeMap.get(deptIterS1.getId().toString())).setAttributeValue("isSelect", "0");
				}
				
				treeMap.get(deptIterS1.getParentid().getId().toString()).add(treeMap.get(deptIterS1.getId().toString()));
				treeMap.remove(deptIterS1.getId().toString());
			}
		}
		for(Iterator<String> iterator = treeMap.keySet().iterator(); iterator.hasNext();){
			tempElement = treeMap.get(iterator.next());
			root.add(tempElement);
			//	 System.out.println(tempElement.asXML());
		}
		//writXml(document);
	//	System.out.println(document.asXML());
		return document.asXML();
	}
	public Map getBmzzMap() {
		Document document = org.dom4j.DocumentHelper.createDocument();
		Element root = document.addElement("root");
		root.addAttribute("label", "部门组织结构");
		//局管部门
		//	List<Dept> listDeptAll = super.getObjectList(new Dept()," (1=1) order by SHOW_ORDER asc");
	 	List<Dept> listDeptAll = (List<Dept>) super.RunSelectClassBySql("select * from dept order by SHOW_ORDER asc","net.chinanets.pojos.Dept");
		
		Map<String,Element> treeMap = new LinkedHashMap<String,Element>();
		int k=0;
		Dept deptIterS1;
		Element tempElement;
		for(Iterator<Dept> iterator = listDeptAll.iterator(); iterator.hasNext();k++){
			deptIterS1 = iterator.next();
			tempElement=DocumentHelper.createElement("MENU"+k);
			tempElement.addAttribute("id", deptIterS1.getId().toString());
			tempElement.addAttribute("label", deptIterS1.getMc());
			treeMap.put(deptIterS1.getId().toString(),tempElement);
		}
		for(Iterator<Dept> iterator = listDeptAll.iterator(); iterator.hasNext();){
			deptIterS1 = iterator.next();
			if(deptIterS1.getParentid()!=null){
				treeMap.get(deptIterS1.getParentid().getId().toString()).add(treeMap.get(deptIterS1.getId().toString()));
				treeMap.remove(deptIterS1.getId().toString());
			}
		}
		for(Iterator<String> iterator = treeMap.keySet().iterator(); iterator.hasNext();){
			tempElement = treeMap.get(iterator.next());
			root.add(tempElement);
		//	 System.out.println(tempElement.asXML());
		}
		//writXml(document);
		System.out.println(document.asXML());
		
		Map rstMap = new HashMap();
		rstMap.put("bmXML", document.asXML());
		rstMap.put("bmList", listDeptAll);
		return rstMap;
	}
	

	

	public String getBmUser() {
	/*	Document document = org.dom4j.DocumentHelper.createDocument();
		Element root = document.addElement("root");
		root.addAttribute("label", "部门组织结构");
		//局管部门
		Element ju=root.addElement("ju");
		ju.addAttribute("label", "局管部门");
		Dept dept = new Dept();dept.setParentId(Long.parseLong("402"));
		List listDept = super.getObjectList(dept);
		for (Iterator iterator = listDept.iterator(); iterator.hasNext();) {
			Dept deptIter = (Dept) iterator.next();
			Element deptElement = null;
			List empList = deptIter.getEmployees();
			for (Iterator iterator3 = empList.iterator(); iterator3.hasNext();) {
				Employee employee = (Employee) iterator3.next();
			//	Users user = employee.getUsers();
				Users user=(Users)super.getObjectById(new Users(),employee.getId());
				if (user != null) {
					deptElement = ju.addElement("node");
					deptElement.addAttribute("label", deptIter.getMc());
					deptElement.addAttribute("id", deptIter.getId().toString());
					deptElement.addAttribute("dept", "true");
					break;
				}
			}
			for (Iterator iterator2 = empList.iterator(); iterator2.hasNext();) {
				Employee employee = (Employee) iterator2.next();
				//Users user = employee.getUsers();
				Users user=(Users)super.getObjectById(new Users(),employee.getId());
				if (user != null) {
					Element empElement = deptElement.addElement("node");
					empElement.addAttribute("label", employee.getXm());
					empElement.addAttribute("mc", user.getMc());
					empElement.addAttribute("id", user.getId().toString());
				}
			}
		}
		
		//馆管部门
		Element guan=root.addElement("guan");
		guan.addAttribute("label", "馆管部门");
		Dept dept2 = new Dept();dept2.setParentId(Long.parseLong("403"));
		List listDept2 = super.getObjectList(dept2);
		for (Iterator iterator = listDept2.iterator(); iterator.hasNext();) {
			Dept deptIter = (Dept) iterator.next();
			Element deptElement = null;
			List empList = deptIter.getEmployees();
			for (Iterator iterator3 = empList.iterator(); iterator3.hasNext();) {
				Employee employee = (Employee) iterator3.next();
			//	Users user = employee.getUsers();
				Users user=(Users)super.getObjectById(new Users(),employee.getId());
				if (user != null) {
					deptElement = guan.addElement("node");
					deptElement.addAttribute("label", deptIter.getMc());
					deptElement.addAttribute("id", deptIter.getId().toString());
					deptElement.addAttribute("dept", "true");
					break;
				}
			}
			for (Iterator iterator2 = empList.iterator(); iterator2.hasNext();) {
				Employee employee = (Employee) iterator2.next();
				//Users user = employee.getUsers();
				Users user=(Users)super.getObjectById(new Users(),employee.getId());
				if (user != null) {
					Element empElement = deptElement.addElement("node");
					empElement.addAttribute("label", employee.getXm());
					empElement.addAttribute("mc", user.getMc());
					empElement.addAttribute("id", user.getId().toString());
				}
			}
		}
		return document.asXML();*/
		return null;
	}
	/**
	 * 添加部门
	 *  如果添加部门的优先级与已有部门优先级相同则交换两个部门的优先级
	 * @param deptVo
	 */
	public void addDept(Dept deptVo) {
		    Dept rule = new Dept();
			Dept rulePo = new Dept();
			rulePo.setShowOrder(deptVo.getShowOrder());
			List ruleList = super.getObjectList(rulePo);
			if (ruleList.size() > 0) {
				Dept rule2 = (Dept) ruleList.get(0);
				Long maxYxj = Long.parseLong(super.getAllObjectByHql("select max(dt.showOrder) from Dept dt").get(0)+"")+1;
				rule2.setShowOrder(maxYxj);
				super.updateObject(rule2);
			}
			rule.setShowOrder(deptVo.getShowOrder());
			rule.setMc(deptVo.getMc());
			rule.setBz(deptVo.getBz());
			rule.setParentid(deptVo.getParentid());
			super.saveObject(rule);
			
	}
	/**
	 * 修改部门
	 *  如果修改后的优先级与已有部门优先级相同则交换两个部门的优先级
	 * @param deptVo
	 */
	public void updateDept(Dept deptVo) {
		Dept rule = (Dept) super.getObjectBySId(new Dept(), deptVo.getId());
		Long aYxj = rule.getShowOrder();
		if (aYxj.equals(deptVo.getShowOrder())) {
			rule.setMc(deptVo.getMc());
			rule.setBz(deptVo.getBz());
			rule.setParentid(deptVo.getParentid());
			super.updateObject(rule);
		} else {
			Dept rulePo = new Dept();
			rulePo.setShowOrder(deptVo.getShowOrder());
			List ruleList = super.getObjectList(rulePo);
			if (ruleList.size() > 0) {
				Dept rule2 = (Dept) ruleList.get(0);
				rule2.setShowOrder(aYxj);
				super.updateObject(rule2);
			}
			
			rule.setShowOrder(deptVo.getShowOrder());
			rule.setMc(deptVo.getMc());
			rule.setBz(deptVo.getBz());
			rule.setParentid(deptVo.getParentid());
			super.updateObject(rule);
			
		}
	}

}
