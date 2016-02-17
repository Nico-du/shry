package net.chinanets.service.imp;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.BeanUtils;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;

import net.chinanets.dao.UsersDao;
import net.chinanets.dao.imp.UserDaoImp;
import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Employee;
import net.chinanets.pojos.Menus;
import net.chinanets.pojos.Rules;
import net.chinanets.pojos.RulesMenus;
import net.chinanets.pojos.Users;
import net.chinanets.pojos.UsersMenus;
import net.chinanets.service.UserService;
import net.chinanets.utils.ChinaNetsUtil;
import net.chinanets.utils.CommonMethods;
import net.chinanets.utils.PassWordErrorException;
import net.chinanets.utils.UserNameNotFoundException;
import net.chinanets.utils.helper.StringHelper;
import net.chinanets.vo.MenuVo;
import net.chinanets.vo.RuleVo;
import net.chinanets.vo.UserVo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserServiceImp extends CommonServiceImp implements UserService {

	private UsersDao usersDao;
	private ExecutorService exc;

	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}

	/**
	 * 查询用户
	 * @return
	 */
	public List findUsers(int pageSize, int pageNumber, String condition) {
		int stYxj = 0;
		if(condition.contains("ruleyxj<=")){
			String yxjstr = condition.substring(condition.indexOf("ruleyxj<=")+"ruleyxj<=".length(), condition.indexOf("ruleyxj<=")+"ruleyxj<=".length()+3).trim();
			stYxj = Integer.parseInt(yxjstr);
			condition = condition.replace("ruleyxj<=", stYxj+"=");
			condition = condition.replace("and  this_.id <> 1", "");
		}
		
		List returnUserList = new ArrayList();
		if("js=".equals(condition.substring(0, 3))) {
			List list =  this.findUsersByRole(pageSize, pageNumber, condition.substring(3, condition.length()));
			return list;
		}
		try{
		List userList = super.getObjectList(new Users(), pageSize, pageNumber,
				condition);

		for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
			Users user = (Users) iterator.next();
			UserVo userVo = new UserVo();
			BeanUtils.copyProperties(user, userVo);
			userVo.setIsLeader(user.getIsleader());
			userVo.setIsShr(user.getIswfshr());
			userVo.setUserName(user.getZsxm());
			// Employee employee = user.getEmployee();
			if(user.getDepts() != null &&  user.getDepts().size()>0){
				Dept dept = (Dept) user.getDepts().get(0);
				userVo.setDeptId(dept.getId()+"");
				userVo.setDeptName(dept.getMc());
			}else{
				Users employee = (Users) super.getObjectById(new Users(),user.getId());
				if (employee != null && employee.getDepts() != null &&  employee.getDepts().size()>0) {
					Dept dept = (Dept) employee.getDepts().get(0);
					userVo.setDeptId(dept.getId()+"");
					userVo.setDeptName(dept.getMc());
				}
			}
			List ruleList = user.getRuleses();
			StringBuffer ruleId = new StringBuffer();
			StringBuffer ruleName = new StringBuffer();
			for (int i = 0; i < ruleList.size(); i++) {
				Rules rule = (Rules) ruleList.get(i);
				if (i == ruleList.size() - 1) {
					ruleId.append(rule.getId());
					ruleName.append(rule.getMc());
				} else {
					ruleId.append(rule.getId()).append(",");
					ruleName.append(rule.getMc()).append(",");
				}
			}
			 Rules rule1 = null;
				for(int i=0;i<user.getRuleses().size();i++){ //判断 角色优先级
					if(user.getRuleses().get(i) == null)continue;
					if(rule1 == null){rule1 = (Rules)user.getRuleses().get(0); continue;}
					if(Integer.parseInt(rule1.getYxj()) > Integer.parseInt(((Rules)user.getRuleses().get(i)).getYxj())){
						rule1 = (Rules)user.getRuleses().get(i);
					}
				}
			if(stYxj != 0 && Integer.parseInt(rule1.getYxj()) >= stYxj)continue;
			userVo.setRuleYxj(rule1.getYxj());
			userVo.setRuleId(ruleId.toString());
			userVo.setRuleName(ruleName.toString());
			returnUserList.add(userVo);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnUserList;
	}

	/**
	 * 根据角色查询用户
	 * @param pageSize
	 * @param pageNumber
	 * @param condition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findUsersByRole(int pageSize, int pageNumber, String condition) {
		List roleList = super.getObjectList(new Rules(), "mc like '%" + condition + "%'"); //根据角色名，得到所有的角色
		List usersList = new ArrayList();
		List resultList = new ArrayList();
		
		for(int i = 0; i < roleList.size(); i++) { //根据不同的角色获得所有的用户
			Rules rule = (Rules)roleList.get(i);
			List<Users> list = rule.getUserses();
			for(Users user : list) {
				usersList.add(getUserVoById(user.getId()));
			}
		}
		usersList = distinctObj(usersList);
		
		//开始分页
		int n = 0;
		for(int i = pageSize * pageNumber - pageSize; i < usersList.size(); i++) {
			resultList.add(usersList.get(i));
			n++;
			if(n > pageSize) {
				break;
			}
//			Users user = (Users)usersList.get(i);
//			System.out.println(user.getMc());
		}
		
		return resultList;
	}
	
	/**
	 * 为查询用户方法的结果排重
	 * @param list
	 * @return
	 */
	private List distinctObj(List list) {
		for(int i = 0; i < list.size(); i++) {
			UserVo first = (UserVo)list.get(i);
			if("admin".equals(first.getMc())) {
				list.remove(first);
			}
			for(int j = i + 1; j < list.size(); j++) {
				UserVo second = (UserVo)list.get(j);
				if(first.getMc().equals(second.getMc())) {
//					distinctList.add(first);
					list.remove(second);
				}
			}
		}
		return list;
	}

	/**
	 * 检验账号是否存在
	 */
	@SuppressWarnings("unchecked")
	public boolean validateUserByMc(String username) {
		Users user = new Users();
		user.setMc(username);
		List<Users> userList = usersDao.getUserByExample(user);
		if (userList==null || userList.size()<1) {
			return false;
		}
		return true;
	}
	/**
	 * 单点登录验证
	 * @param userId
	 * @return
	 */
	public UserVo validateSingleLogin(String userId){
		if(CommonMethods.isNullOrWhitespace(userId))return null;
		ArrayList ary = CommonMethods.LoginedUserArray;
		if(CommonMethods.LoginedUserArray.contains(userId)){
			CommonMethods.LoginedUserArray.remove(userId);
			ary = CommonMethods.LoginedUserArray;
			return getUserVoById(Long.parseLong(userId));
		}
		return null;
	}
	
	/**
	 * CA登录验证
	 * @param userId
	 * @return
	 */
	public UserVo validateCALogin(String zybm){
		if(CommonMethods.isNullOrWhitespace(zybm))return null;
		ArrayList<String> userIds = (ArrayList<String>) super.getObjectBySql(" select id from users where id='"+zybm+"'");
		if(userIds.isEmpty())return null;
		return getUserVoById(Long.parseLong(zybm));
	}
	
	
	/**
	 * 用户登录
	 */
	@SuppressWarnings("unchecked")
	public UserVo validateUser(String username, String userpwd){
		if (!validateUserByMc(username)) {
			return null;
		}
		Users user=new Users();
		user.setMc(username);
		user.setMm(userpwd);
		List<Users> userList=usersDao.getObjectByExample(user);
		if(userList==null || userList.size()<1){
			return null;
		}
		user = userList.get(0);
		StringBuffer ruleId = new StringBuffer();
		StringBuffer ruleMc = new StringBuffer();
		StringBuffer bmid = new StringBuffer();
		StringBuffer bmmc = new StringBuffer();
		List<Dept> deptList = user.getDepts();
		if(deptList!=null && deptList.size()>0){
			for (int i = 0; i < deptList.size(); i++) {
				Dept dept = (Dept) deptList.get(i);
				if (dept!= null) {
					if (i == deptList.size() - 1) {
						bmid.append(dept.getId());
						bmmc.append(dept.getMc());
					} else {
						ruleId.append(dept.getId().toString()).append(",");
						bmid.append(dept.getId()+",");
						bmmc.append(dept.getMc()+",");
					}
				}
			}
		}
		Rules rule = null;
		for(int i=0;i<user.getRuleses().size();i++){ //判断 角色优先级
			if(user.getRuleses().get(i) == null)continue;
			if(rule == null){rule = (Rules)user.getRuleses().get(0); continue;}
			if(Integer.parseInt(rule.getYxj()) > Integer.parseInt(((Rules)user.getRuleses().get(i)).getYxj())){
				rule = (Rules)user.getRuleses().get(i);
			}
			/*if (i == deptList.size() - 1) {
				ruleId.append(((Rules)user.getRuleses().get(i)).getId().toString());
				ruleMc.append(((Rules)user.getRuleses().get(i)).getMc().toString());
			} else {
				ruleId.append(((Rules)user.getRuleses().get(i)).getId().toString()+",");
				ruleMc.append(((Rules)user.getRuleses().get(i)).getMc().toString()+",");
			}*/
		}
		if(rule != null) {
			ruleId.append(rule.getId().toString());
			ruleMc.append(rule.getMc().toString());
		}
		String bmids = "";
		if(user.getUsersMenuses() != null && user.getUsersMenuses().size() > 0){
			bmids = ((UsersMenus)user.getUsersMenuses().get(0)).getSearchCondition();
		}
		UserVo userVo = new UserVo();
		userVo.setId(user.getId());
		userVo.setMc(user.getMc());
		userVo.setMm(user.getMm());
		userVo.setBmid(bmids);
		userVo.setBmmc(convertDeptidToDeptname(bmids));
		userVo.setUserName(user.getZsxm());
		userVo.setDeptName(user.getBmmc());
		userVo.setRuleYxj(rule.getYxj());
		userVo.setIsShr(user.getIswfshr());
		userVo.setIsLeader(user.getIsleader());
		userVo.setRuleId(ruleId.toString());
		userVo.setRuleName(ruleMc.toString());
		userVo.setDeptId(bmid.toString());
		userVo.setDeptName(bmmc.toString());
		return userVo;
	}
	
	/**
	 * 获取用户的权限信息
	 * @param userid
	 * @return
	 */
	public UserVo getUserVoById(Long userid){
		Users user = (Users) super.getObjectById(new Users(), userid);
		if(user == null){ return null;}
		StringBuffer ruleId = new StringBuffer();
		StringBuffer ruleMc = new StringBuffer();
		StringBuffer bmid = new StringBuffer();
		StringBuffer bmmc = new StringBuffer();
		List<Dept> deptList = user.getDepts();
		List<Rules> rulesList = user.getRuleses();
		List<UsersMenus> usermenusList = user.getUsersMenuses();
		///TODO 插入数据同步过来的用户的权限 
		/*if(userid.toString().startsWith("21600")){
			Long roleId = 13214L;//普通用户
			
			Users userRole = (Users) super.getObjectById(new Users(), roleId);
			deptList = userRole.getDepts();
			rulesList = userRole.getRuleses();
			usermenusList = userRole.getUsersMenuses();
		}*/
		
		if(deptList!=null && deptList.size()>0){
			for (int i = 0; i < deptList.size(); i++) {
				Dept dept = (Dept) deptList.get(i);
				if (dept!= null) {
					if (i == deptList.size() - 1) {
						bmid.append(dept.getId());
						bmmc.append(dept.getMc());
					} else {
						ruleId.append(dept.getId().toString()).append(",");
						bmid.append(dept.getId()+",");
						bmmc.append(dept.getMc()+",");
					}
				}
			}
		}
		Rules rule = null;
		for(int i=0;i<rulesList.size();i++){ //判断 角色优先级
			if(rulesList.get(i) == null)continue;
			if(rule == null){rule = (Rules)rulesList.get(0); continue;}
			if(Integer.parseInt(rule.getYxj()) > Integer.parseInt(((Rules)rulesList.get(i)).getYxj())){
				rule = (Rules)rulesList.get(i);
			}
		}
		if(rule != null) {
			ruleId.append(rule.getId().toString());
			ruleMc.append(rule.getMc().toString());
		}
		String bmids = "";
		if(usermenusList != null && usermenusList.size() > 0){
			bmids = ((UsersMenus)usermenusList.get(0)).getSearchCondition();
		}
		UserVo userVo = new UserVo();
		userVo.setId(user.getId());
		userVo.setMc(user.getMc());
		userVo.setMm(user.getMm());
		userVo.setBmid(bmids);
		userVo.setBmmc(convertDeptidToDeptname(bmids));
		userVo.setUserName(user.getZsxm());
		userVo.setIsLeader(user.getIsleader());
		userVo.setRuleId(ruleId.toString());
		userVo.setRuleName(ruleMc.toString());
		userVo.setDeptId(bmid.toString());
		userVo.setDeptName(bmmc.toString());
		if(rule != null)userVo.setRuleYxj(rule.getYxj());
		userVo.setIsShr(user.getIswfshr());
		return userVo;
	}
	
	/**
	 * 获取用户菜单 不包含无权限菜单,不整理结构
	 */
	public String getUserAuthority(Long userId){
		/*if(userId.toString().startsWith("21600")){
		userId = 13214L;//普通用户
			}*/
		return getUserMenuStructure(userId,false,false,null);
	}
	/**
	 * 获取用户菜单 包含无权限菜单, 整理结构
	 */
	public String  getUserMenu(Long userId){
		return getUserMenuStructure(userId,true,true,null);
	}
	/**
	 * 获取用户菜单 不包含无权限菜单 ,整理结构
	 */
	public String getUserStructureMenu(Long userId,String wdType){
		return getUserMenuStructure(userId,false,true,wdType);
	}
	
	/**
	 * 加入权限控制的getUserMenuStructure
	 * @param userId 用户ID
	 * @param showNullNodes 是否显示user没有权限的菜单
	 * @param showStructure 是否进行结构整理
	 * @param wdType 菜单类型(menu表字段 bz)
	 * @return
	 */
	public String getUserMenuStructure(Long userId,boolean showNullNodes,boolean showStructure,String wdType){
		Users user = usersDao.findUserById(userId);// 查询当前用户
/*		List<Rules> userRuleList = user.getRuleses();
		for( Rules rule : userRuleList){
			if(rule.getId()==41){
				Document document = DocumentHelper.createDocument();
				Element root = document.addElement("Root");
				Element tempElement=root.addElement("node");
				tempElement.addAttribute("id", "41");
				tempElement.addAttribute("label", "超级管理员");
				tempElement.addAttribute("searchCondition", "all");
				tempElement.addAttribute("url", "*");
			//	root.add(tempElement);
				return root.asXML();
			}
		}*/
		return getUserMenuStructure(user,showNullNodes,showStructure,wdType);
	}
	
	/**
	 * 获取用户优先级最大的角色
	 **/
	private Rules getUserMaxClassRule(Users user){
		Rules maxClsRule = new Rules();
		List<Rules> userRuleList = user.getRuleses();
		if(userRuleList!=null && !userRuleList.isEmpty()){
			maxClsRule = (Rules)userRuleList.get(0);
		}
		for( Rules rule : userRuleList){
			if(Integer.parseInt(maxClsRule.getYxj()) > Integer.parseInt(rule.getYxj())){
				maxClsRule = rule;
			}
		}
		return maxClsRule;
	}
	
	
	/**
	 * 获取用户权限菜单XML
	 * 分3步
	 * 1.获取所有的加入权限信息的menu list
	 * 2.根据menu list生成 所有的dom4j Element
	 * 3.根据parentid将相应的Element 加入到父Element下(不能在将Element加入父Element同时删除该Element,因为该Element也可能有子节点)
	 * @param showNullNodes  是否显示user没有权限的菜单
	 * @param showStructure  是否进行菜单结构整理
	 */
	public String getUserMenuStructure(Users user,boolean showNullNodes,boolean showStructure,String wdType) {
		boolean isSupervisor = false;//是否超级管理员 超级管理员返回所有菜单
		if(user.getRuleses()!=null && !user.getRuleses().isEmpty() && getUserMaxClassRule(user).getId() == 41){
			isSupervisor = true;
		}
		user = convertRuleToUser(user);
		// 创建xml对象
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Root");
		List<MenuVo> returnMenuvoAllList = new ArrayList<MenuVo>();// 存放菜单项，去掉重复值
		
		String sqlCdn = (CommonMethods.isNullOrWhitespace(wdType) ? "" : " this_.bz='"+wdType+"' and ") + " this_.bz is not null and 1=1 order by this_.id asc";
		List<?> usersMenusList = user.getUsersMenuses(); // 当前用户对应的中间表列表
		List<Menus> menusAllList = super.getObjectList(new Menus(),sqlCdn);//所有菜单
		List<Menus> menusList = getUserMenus(user,wdType);//用户对应的菜单
		
		for(int i=0;i<menusAllList.size();i++){
			    Menus menuCurr = menusAllList.get(i);
				MenuVo mv = new MenuVo();
				BeanUtils.copyProperties(menuCurr, mv);
				mv.setPageUrl(menuCurr.getPageUrl());
				mv.setParentId(menuCurr.getMenus() == null ? null : menuCurr.getMenus().getId());
				if(menusList.contains(menuCurr)){
					for (int j = 0; j < usersMenusList.size(); j++) {
				    UsersMenus usersMunes = (UsersMenus) usersMenusList.get(j);
				    if(usersMunes.getMenus().getId() == menuCurr.getId()){
				    Menus menu = usersMunes.getMenus();
					BeanUtils.copyProperties(menu, mv);
					mv.setParentId(menu.getMenus() == null ? null : menu.getMenus().getId());
					mv.setSfAdd(usersMunes.getSfAdd());
					mv.setSfDelete(usersMunes.getSfDelete());
					mv.setSfModify(usersMunes.getSfModify());
					mv.setSfSearch(usersMunes.getSfSearch());
					mv.setSfImport(usersMunes.getSfImport());
					mv.setSfExport(usersMunes.getSfExport());
					mv.setSfOther(usersMunes.getSfOther());
					mv.setSearchCondition(usersMunes.getSearchCondition());
					if(!showNullNodes){returnMenuvoAllList.add(mv);}
					break;
				    }
				}
			}
				if(showNullNodes || isSupervisor){returnMenuvoAllList.add(mv);}
		}
		//System.out.println("returnMenuvoAllList.size() :"+returnMenuvoAllList.size());
		try{
			Map<String,Element> treeMap = new LinkedHashMap<String,Element>();
			MenuVo deptIterS1;
			Element tempElement;
			for(Iterator<MenuVo> iterator = returnMenuvoAllList.iterator(); iterator.hasNext();){
				deptIterS1 = iterator.next();
				tempElement=DocumentHelper.createElement("node");
				tempElement.addAttribute("id", deptIterS1.getId().toString());
				tempElement.addAttribute("label", deptIterS1.getMc());
				tempElement.addAttribute("icon", deptIterS1.getMenuImg());
				tempElement.addAttribute("url", deptIterS1.getUrl());
				tempElement.addAttribute("pageUrl", deptIterS1.getPageUrl());
				if(deptIterS1.getSfAdd()!=null || deptIterS1.getSfDelete()!=null || deptIterS1.getSfModify()!=null ||
						deptIterS1.getSfExport()!=null || deptIterS1.getSfImport()!= null || deptIterS1.getSfSearch()!=null || 
						deptIterS1.getSfOther()!=null || deptIterS1.getSearchCondition()!=null){
					tempElement.addAttribute("id", deptIterS1.getId().toString());
					tempElement.addAttribute("label", deptIterS1.getMc());
					tempElement.addAttribute("icon", deptIterS1.getMenuImg());
					tempElement.addAttribute("pageUrl", deptIterS1.getPageUrl());
					tempElement.addAttribute("url", deptIterS1.getUrl());
					tempElement.addAttribute("sfAdd", deptIterS1.getSfAdd());
					tempElement.addAttribute("sfDelete", deptIterS1.getSfDelete());
					tempElement.addAttribute("sfSearch", deptIterS1.getSfSearch());
					tempElement.addAttribute("sfModify", deptIterS1.getSfModify());
					tempElement.addAttribute("sfImport", deptIterS1.getSfImport());
					tempElement.addAttribute("sfExport", deptIterS1.getSfExport());
					tempElement.addAttribute("searchCondition", convertDeptidToDeptname(deptIterS1.getSearchCondition()));
					tempElement.addAttribute("sfOther", deptIterS1.getSfOther());
					if(!showNullNodes){treeMap.put(deptIterS1.getId().toString(),tempElement);}
				  }
				if(showNullNodes || isSupervisor){treeMap.put(deptIterS1.getId().toString(),tempElement); }//超级管理员 开放所有菜单
			}
			if(showStructure){//整理菜单结构
				//System.out.println("1.treeMap.size() :"+treeMap.keySet().size());
				List<Long> needRemoveIds = new ArrayList<Long>();
				for(Iterator<MenuVo> iterator = returnMenuvoAllList.iterator(); iterator.hasNext();){
					deptIterS1 = iterator.next();
					if(deptIterS1.getParentId()!=null){
						Object obj = treeMap.get(deptIterS1.getParentId().toString());
						if(obj != null){
							treeMap.get(deptIterS1.getParentId().toString()).add(treeMap.get(deptIterS1.getId().toString()));
							needRemoveIds.add(deptIterS1.getId());
						}else{
							//	System.out.println(" treeMap.get(deptIterS1.getParentId().toString()) == null :"+(obj == null)+" parentid = "+deptIterS1.getParentId());
						}
					}
				}
				for(int idx=0;idx<needRemoveIds.size();idx++){
					treeMap.remove(needRemoveIds.get(idx).toString());
				}
			}
			//System.out.println("2.treeMap.size() :"+treeMap.keySet().size());
			for(Iterator<String> iterator = treeMap.keySet().iterator(); iterator.hasNext();){
				tempElement = treeMap.get(iterator.next());
				root.add(tempElement);
				//	 System.out.println(tempElement.asXML());
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	    ChinaNetsUtil.writXml(document);
		return document.asXML();
	}

	/**
	 * 将只有角色默认权限的用户 转换为角色对应用户
	 * (未手动设定权限则为默认角色权限)
	 * @return
	 */
	private Users convertRuleToUser(Users user){
		if(user.getUsersMenuses().isEmpty()){
			if(user.getRuleses().isEmpty()){return user;}
			Rules maxRule = getUserMaxClassRule(user);
			String sql = " this_.mc='" + maxRule.getMc()+"' and this_.zsxm is null and this_.bmmc is null";
			List usList = super.getObjectList(new Users(), sql);
			if(!usList.isEmpty()){
				return (Users) usList.get(0);
			}
		}
		return user;
	}
	private String convertDeptidToDeptname(String deptid){
		if(deptid == null){return "";}
		if(deptid.endsWith(",")){deptid = deptid.substring(0, deptid.length()-1);}
		String []deptids = deptid.split(",");
		if(deptids.length < 2){ return deptid;}
		List deptList = super.getObjectList(new Dept());
		String deptStr="";
		for(int i=0;i<deptids.length;i++){
			if(deptids[i].length() < 1){continue;}
			for(int j=0;j<deptList.size();j++){
			if(deptids[i].equals(((Dept)deptList.get(j)).getId()+"")){
				deptStr += ((Dept)deptList.get(j)).getMc()+",";
				break;
			}
			}
		}
		if(deptStr.endsWith(",")){deptStr = deptStr.substring(0, deptStr.length()-1);}
		return deptStr;
	}
	
	private List getUserMenus(Users user,String wdType){
		if(user.getMc() == "admin"){//管理员 后面添加管理员
			return super.getObjectList(new Menus(), " 1=1 order by id asc");
		}
		List listRst = new ArrayList();
		UsersMenus ums;
		for(int i=0;i<user.getUsersMenuses().size();i++){
			ums = (UsersMenus) user.getUsersMenuses().get(i);
			if(ums == null || (ums != null &&  ums.getMenus()!=null && !CommonMethods.isNullOrWhitespace(wdType) 
					&& !wdType.equals(ums.getMenus().getBz()) ))continue;
			listRst.add(ums.getMenus());
		}
		return listRst;
	}
	/**
	 * 根据ID 删除用户
	 * 
	 * @param id
	 */
	public void deleteUser(Long[] id) {
		for (int i = 0; i < id.length; i++) {
			Long long1 = id[i];
	//		super.deleteByHql("delete from UsersMenus  where USER_ID="+long1);
	//		super.deleteByHql("delete from USERS_RULES  where USER_ID="+long1);
	//		super.deleteByHql("delete from USERS_DEPTS  where USER_ID="+long1);
			Users user = usersDao.findUserById(long1);
		//	super.deleteObject(user);
			usersDao.deleteUser(user);
		}

	}

	/**
	 * 添加帐号
	 * 
	 * @param userVo
	 * @return
	 */
	public Long saveUser(UserVo userVo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Users user = new Users();
		user.setBmmc(userVo.getDeptName());
		user.setCjsj(sdf.format(new Date()));
		/*
		 * Employee employee = (Employee) super.getObjectById(new Employee(),
		 * userVo.getId()); user.setEmployee(employee);
		 */
		user.setId(userVo.getId());
		user.setMc(userVo.getMc());
		user.setZsxm(userVo.getUserName());
		user.setMm(userVo.getMm()); 
		user.setIsleader(userVo.getIsLeader());
		user.setIswfshr(userVo.getIsShr());
		
		Long ruleUserId = 0L;
		if (userVo.getRuleId() != "") {
			String[] ruleId = userVo.getRuleId().split(",");
			for (int i = 0; i < ruleId.length; i++) {
				String rId = ruleId[i];
				Rules rule = (Rules) super.getObjectById(new Rules(), new Long(rId));
				if(i == 0){ ruleUserId = rule.getUserId();}
				user.getRuleses().add(rule);
			}
		}
		if (userVo.getDeptId() != "") {
			String[] ruleId = userVo.getDeptId().split(",");
			for (int i = 0; i < ruleId.length; i++) {
				String rId = ruleId[i];
				Dept rule = (Dept) super.getObjectBySId(new Dept(), rId);
				user.getDepts().add(rule);
			}
		}
		Long userId = super.saveObject(user);
		//TODO 添加  复制默认权限到users_menus --
		List usersMenus = super.getObjectList(new UsersMenus(), " 1=1 and USER_ID="+ruleUserId);
		UsersMenus um;
		UsersMenus um1;
		for(int i=0;i<usersMenus.size();i++){
			um1 = (UsersMenus) usersMenus.get(i);
			um = new UsersMenus();
			BeanUtils.copyProperties(um1, um);
			um.setId(null);
			um.setUsers(user);
			super.saveObject(um);
			user.getUsersMenuses().add(um);
		}
		super.updateObject(user);
/*		Users ruleUser = (Users) super.getObjectById(new Users(), ruleUserId);
		
		if(ruleUser != null){
			List usersMenus = new ArrayList(ruleUser.getUsersMenuses());
			UsersMenus um;
			for(int i=0;i<usersMenus.size();i++){
				um = (UsersMenus) usersMenus.get(i);
				um.setUsers(user);
				user.getUsersMenuses().add(um);
			}
			super.updateObject(user);
			ruleUser.setUsersMenuses(usersMenus);
			super.updateObject(ruleUser);
		}*/
		
		return userId;
	}

	/**
	 * 查询用户总数
	 * 
	 * @param condition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getCountByUsers(String condition) {
		int stYxj = 0;
		if(condition.contains("ruleyxj<=")){
			String yxjstr = condition.substring(condition.indexOf("ruleyxj<=")+"ruleyxj<=".length(), condition.indexOf("ruleyxj<=")+"ruleyxj<=".length()+3).trim();
			stYxj = Integer.parseInt(yxjstr);
			condition = condition.replace("ruleyxj<=", stYxj+"=");
			condition = condition.replace("and  this_.id <> 1", "");
			condition = condition.replace("this_", "US");
			List list = super.getObjectBySql("SELECT COUNT(distinct(US.ID)) FROM USERS US INNER JOIN USERS_RULES URS ON US.ID = URS.USER_ID INNER JOIN RULES  RU ON URS.RULE_ID = RU.ID  AND RU.YXJ<"+stYxj+" and "+condition);
			return list == null ? 0 : Integer.parseInt(list.get(0).toString());
		}
		
		if("js=".equals(condition.substring(0, 3))) {
			List roleList = super.getObjectList(new Rules(), "mc like '%" + condition.substring(3, condition.length()) + "%'"); //根据角色名，得到所有的角色
			List usersList = new ArrayList();
			
			for(int i = 0; i < roleList.size(); i++) { //根据不同的角色获得所有的用户
				Rules rule = (Rules)roleList.get(i);
				List<Users> list = rule.getUserses();
				for(int j = 0; j < list.size(); j++) {
					UserVo userVo = new UserVo();
					Users user = (Users)list.get(j);
					BeanUtils.copyProperties(user, userVo);
					usersList.add(userVo);
				}
			}
			usersList = distinctObj(usersList);//删除集合重复对象
			return usersList.size();
		}
		return super.getCountByObject(new Users(), condition);
	}

	/**
	 * 根据ID查询用户
	 * 
	 * @param id
	 */
	public UserVo findUserById(Long id) {
		Users user = (Users) super.getObjectById(new Users(), id);
		UserVo userVo = new UserVo();
		userVo.setDeptName(user.getBmmc());
		userVo.setId(user.getId());
		userVo.setMc(user.getMc());
		userVo.setMm(user.getMm());
		userVo.setUserName(user.getZsxm());
		userVo.setIsLeader(user.getIsleader());
		List ruleList = user.getRuleses();
		StringBuffer ruleId = new StringBuffer();
		StringBuffer ruleName = new StringBuffer();
		for (int i = 0; i < ruleList.size(); i++) {
			Rules rule = (Rules) ruleList.get(i);
			if (i == ruleList.size() - 1) {
				ruleId.append(rule.getId().toString());
				ruleName.append(rule.getMc());
			} else {
				ruleId.append(rule.getId().toString()).append(",");
				ruleName.append(rule.getMc()).append(",");
			}
		}
		 Rules rule1 = null;
			for(int i=0;i<user.getRuleses().size();i++){ //判断 角色优先级
				if(user.getRuleses().get(i) == null)continue;
				if(rule1 == null){rule1 = (Rules)user.getRuleses().get(0); continue;}
				if(Integer.parseInt(rule1.getYxj()) > Integer.parseInt(((Rules)user.getRuleses().get(i)).getYxj())){
					rule1 = (Rules)user.getRuleses().get(i);
				}
			}
		userVo.setRuleYxj(rule1.getYxj());
		userVo.setRuleId(ruleId.toString());
		userVo.setRuleName(ruleName.toString());
		userVo.setIsShr(user.getIswfshr());
		return userVo;
	}

	/**
	 * 修改帐号
	 * 
	 * @param userVo
	 */
	public void updateUser(UserVo userVo) {
		Users user = (Users) super.getObjectById(new Users(), userVo.getId());
		user.setMc(userVo.getMc());
		user.setIsleader(userVo.getIsLeader());
		user.setIswfshr(userVo.getIsShr());
		user.setBmmc(userVo.getDeptName());
		user.getRuleses().clear();
		Long ruleUserId = 0L;
		if (!CommonMethods.isNullOrWhitespace(userVo.getRuleId())) {
			user.getRuleses().clear();
			String[] ruleId = userVo.getRuleId().split(",");
			for (int i = 0; i < ruleId.length; i++) {
				String rId = ruleId[i];
				Rules rule = (Rules) super.getObjectById(new Rules(), new Long(rId));
				if(i == 0){ ruleUserId = rule.getUserId();}
				user.getRuleses().add(rule);
			}
		}
		if (!CommonMethods.isNullOrWhitespace(userVo.getDeptId())) {
			user.getDepts().clear();
			String[] ruleId = userVo.getDeptId().split(",");
			for (int i = 0; i < ruleId.length; i++) {
				String rId = ruleId[i];
				Dept rule = (Dept) super.getObjectBySId(new Dept(), rId);
				user.getDepts().add(rule);
			}
		}
		
		super.deleteByHql("delete from UsersMenus where user_id = " + user.getId());
		//TODO 添加  复制默认权限到users_menus --
		List usersMenus = super.getObjectList(new UsersMenus(), " 1=1 and USER_ID="+ruleUserId);
		UsersMenus um;
		UsersMenus um1;
		user.getUsersMenuses().clear();
		for(int i=0;i<usersMenus.size();i++){
			um1 = (UsersMenus) usersMenus.get(i);
			um = new UsersMenus();
			BeanUtils.copyProperties(um1, um);
			um.setId(null);
			um.setUsers(user);
			super.saveObject(um);
			user.getUsersMenuses().add(um);
		}
		super.updateObject(user);
		//super.mergeObject(user);
	}

	/**
	 * 修改密码
	 * 
	 * @param newPw
	 * @param userId
	 */
	public void modifyPw(String newPw, Long userId) {
		Users user = (Users) super.getObjectById(new Users(), userId);
		user.setMm(newPw);
		super.updateObject(user);
	}
	
	/*
	 * 备份数据库(non-Javadoc)
	 * @see net.chinanets.service.UserService#backDB()
	 */
	public int backDB(){
		try {
			Runtime.getRuntime().exec("cmd /c exp zcgl/zcgl@chinanet file=D:\\zcgl%date%.dmp owner(zcgl)");
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		//exp zcgl/zcgl@orcl file=.\zcgl%date%.dmp owner(zcgl)
		return 1;
	}
	/*
	 *  查看备份文件(non-Javadoc)
	 * @see net.chinanets.service.UserService#selectDBback()
	 */
	public List selectDBback(){
		FileFilter filter = new FileFilter(){
			public boolean accept(File arg0) {
				 String tmp=arg0.getName().toLowerCase();
		            if(tmp.endsWith(".dmp") || tmp.endsWith(".DMP") ){
		                return true;
		            }
		          return false;
			}
		};
		File file = new File("d:\\");
		File[] files=file.listFiles(filter);
		for(int i=0;i<files.length;i++){
			System.out.println(files[i].getName()+"    "+files[i].getPath());
		}
		return null;
	}

	public RuleVo getRule(Long ruleId) {
//		Long id = Long.valueOf(userVo.getRuleId());
		Rules rule = (Rules) super.getObjectById(new Rules(), ruleId);
		RuleVo ruleVo = new RuleVo();
		ruleVo.setId(rule.getId());
		ruleVo.setBmid(rule.getBmid());
		ruleVo.setBmmc(rule.getBmmc());
		ruleVo.setBz(rule.getBz());
		ruleVo.setKmcz(rule.getKmcz());
		ruleVo.setMc(rule.getMc());
		ruleVo.setYxj(rule.getYxj());
		return ruleVo;
	}

	@SuppressWarnings("unchecked")
	public List getUserByMenu(String url) {
		List userList = new ArrayList();
		Menus menu = new Menus();
//		menu.setMc("任务列表");
		menu.setUrl(url);
		List menuList = super.getObjectList(menu);
		if(menuList != null && menuList.size() > 0) {
			menu = (Menus)menuList.get(0); //得到一个菜单对象
		}
		
		Long rmId;
		List rmList = menu.getRulesMenuses();
		for(int i = 0; i < rmList.size(); i++) {
			RulesMenus rm = (RulesMenus)rmList.get(i);
			Long ruleId = 0L;
			rmId = rm.getId();
//			rm = (RulesMenus)super.getObjectById(new RulesMenus(), rmId);
			List ruleIdList = super.getObjectBySql("select rule_id from rules_menus where id = " + rmId);
			if(ruleIdList != null && ruleIdList.size() > 0) {
				String str = ruleIdList.get(0).toString();
				ruleId = Long.valueOf(str);
			}
			Rules rule = (Rules) super.getObjectById(new Rules(), ruleId);
			List list = rule.getUserses();
			for(int j = 0; j < list.size(); j++) {
				userList.add(list.get(j));
			}
		}
		
		for(int i = 0; i < userList.size(); i++) {
			System.out.println(((Users)userList.get(i)).getZsxm());
		}
		return null;
	}

	
	
	public List<Dept> getAllDepts(){
		 return usersDao.getAllDepts();
	}
	
	public List getAllRules(){
		 return usersDao.getAllRules();
	}
	
}
