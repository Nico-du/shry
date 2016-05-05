package net.chinanets.service.imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;

import net.chinanets.dao.CommonDao;
import net.chinanets.dao.RulesDao;
import net.chinanets.pojos.Dept;
import net.chinanets.pojos.Employee;
import net.chinanets.pojos.Menus;
import net.chinanets.pojos.Rules;
import net.chinanets.pojos.RulesMenus;
import net.chinanets.pojos.Users;
import net.chinanets.pojos.UsersMenus;
import net.chinanets.service.RuleService;
import net.chinanets.utils.ChinaNetsUtil;
import net.chinanets.utils.CommonMethods;
import net.chinanets.vo.RuleMenuVo;
import net.chinanets.vo.UserMenuVo;
import net.chinanets.vo.UserVo;

public class RuleServiceImp extends CommonServiceImp implements RuleService {

	private RulesDao ruleDao;

	public void setRuleDao(RulesDao ruleDao) {
		this.ruleDao = ruleDao;
	}

	/**
	 * 查询角色列表
	 * 
	 * @return
	 */
	public List findAllRules() {
		List returnList = new ArrayList();
		List ruleList = ruleDao.findAllRules();
		for (Iterator iterator = ruleList.iterator(); iterator.hasNext();) {
			Rules rule = (Rules) iterator.next();
			/*Rules rv = new Rules();
			BeanUtils.copyProperties(rule, rv);*/
			returnList.add(rule);
		}
		return returnList;
	}

	/**
	 * 添加角色
	 * 
	 * @param ruleVo
	 */
	public Long addRules(Rules ruleVo) {
		Users ruleUser = new Users();
		ruleUser.setMc(ruleVo.getMc());
		ruleUser.setMm("111111");
		Long userId = (Long) super.saveObject(ruleUser);
		Rules rule = new Rules();
		rule.setYxj(ruleVo.getYxj());
		List ruleList = super.getObjectList(rule);
		if (ruleList.size() > 0) {
			Rules rule2 = (Rules) ruleList.get(0);
			int maxYxj = ruleDao.getMaxYxj() + 1;
			String yxj = maxYxj + "";
			rule2.setYxj(yxj);
			super.updateObject(rule2);
		}
		rule.setUserId(userId);
		rule.setMc(ruleVo.getMc());
		rule.setBz(ruleVo.getBz());
		return (Long) super.saveObject(rule);

	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 */
	public void deleteRules(Long[] id) {
		int firstYxj = 0;
		int maxYxj = 0;
		int yxj = 0;
		for (int i = 0; i < id.length; i++) {
			Long long1 = id[i];
			Rules rule = (Rules) super.getObjectById(new Rules(), long1);
			Long ruleUserId = rule.getUserId();
			if (i == 0) {
				firstYxj = Integer.parseInt(rule.getYxj());
			} else {
				if (i == 1) {
					maxYxj = Integer.parseInt(rule.getYxj());
				} else {
					firstYxj = maxYxj;
					maxYxj = Integer.parseInt(rule.getYxj());
				}
			}
			super.deleteObject(rule);
			super.deleteObject(super.getObjectById(new Users(), ruleUserId)); 
			if (id.length > 1) {
				if (i >= 1 && i < id.length - 1) {
					yxj += 1;
					ruleDao.updateRules(firstYxj, maxYxj, yxj);
				}
				if (i == id.length - 1) {
					yxj += 1;
					ruleDao.updateRules(firstYxj, maxYxj, yxj);
					ruleDao.updateRules(0, maxYxj, yxj + 1);
				}
			} else {
				ruleDao.updateRules(firstYxj, 0, 1);
			}
		}
	}

	/**
	 * 根据用户ID得到所属角色
	 * 
	 * @param id
	 * @return
	 */
	public List findRulesByUserId(Long userId) {
		List returnList = new ArrayList();
		Users user = (Users) super.getObjectById(new Users(), userId);
		List ruleList = user.getRuleses();
		for (int i = 0; i < ruleList.size(); i++) {
			Rules rule = (Rules) ruleList.get(i);
			Rules ruleVo = new Rules();
			BeanUtils.copyProperties(rule, ruleVo);
			returnList.add(ruleVo);
		}
		return returnList;
	}

	/**
	 * 添加角色时，查询所有菜单树
	 */
	public String getMenuTree(Long id) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		root.addAttribute("label", "所有功能菜单");
		root.addAttribute("id", "0");
		root.addAttribute("isSelect", "1");
		List menuList = super.getObjectList(new Menus(), " 1=1 order by id asc");
		List ruleMenuList = getMenuByRuleId(id);
		for (Iterator iterator = menuList.iterator(); iterator.hasNext();) {
			Menus menu = (Menus) iterator.next();
			if (menu.getMenus() == null) {// 第一级菜单
				Element firstElement = root.addElement("node");
				firstElement.addAttribute("id", menu.getId().toString());
				firstElement.addAttribute("label", menu.getMc());
				if(ruleMenuList.indexOf(menu) > -1 ) {
					firstElement.addAttribute("isSelect", "1");
				}else {
					firstElement.addAttribute("isSelect", "0");
				}
			}
			if (menu.getMenus() != null && menu.getMenus().getMenus() == null) {// 第二级菜单
				String xPath = "/root/node[@id='" + menu.getMenus().getId() + "']";
				Element firstElement = (Element) document.selectNodes(xPath).get(0);
				Element secondElement = firstElement.addElement("node");
				secondElement.addAttribute("id", menu.getId().toString());
				secondElement.addAttribute("label", menu.getMc());
				if(ruleMenuList.indexOf(menu) > -1 ) {
					secondElement.addAttribute("isSelect", "1");
					if(menu.getMenuses() != null && menu.getMenuses().size() == 0) {
						RulesMenus ruleMenu = ruleDao.getRuleMenuByRMid(id, menu.getId());
						secondElement.addAttribute("sfAdd", ruleMenu.getSfAdd());
						secondElement.addAttribute("sfModify", ruleMenu.getSfModify());
						secondElement.addAttribute("sfDelete", ruleMenu.getSfDelete());
						secondElement.addAttribute("sfSearch", ruleMenu.getSfSearch());
						secondElement.addAttribute("sfExport", ruleMenu.getSfExport());
						secondElement.addAttribute("sfImport", ruleMenu.getSfImport());
						secondElement.addAttribute("isRule", "1");
					}
				}else {
					secondElement.addAttribute("isSelect", "0");
					if(menu.getMenuses() != null && menu.getMenuses().size() == 0) {
						secondElement.addAttribute("isRule", "1");
					}
				}
			}
			if (menu.getMenus() != null && menu.getMenus().getMenus() != null) {// 第三级菜单
				String xPath = "/root/node/node[@id='" + menu.getMenus().getId() + "']";
				Element secondElement = (Element) document.selectNodes(xPath).get(0);
				Element thirdElement = secondElement.addElement("node");
				thirdElement.addAttribute("id", menu.getId().toString());
				thirdElement.addAttribute("label", menu.getMc());
				thirdElement.addAttribute("isRule", "1");
				if(ruleMenuList.indexOf(menu) > -1 ) {
					thirdElement.addAttribute("isSelect", "1");
					RulesMenus ruleMenu = ruleDao.getRuleMenuByRMid(id, menu.getId());
					thirdElement.addAttribute("sfAdd", ruleMenu.getSfAdd());
					thirdElement.addAttribute("sfModify", ruleMenu.getSfModify());
					thirdElement.addAttribute("sfDelete", ruleMenu.getSfDelete());
					thirdElement.addAttribute("sfSearch", ruleMenu.getSfSearch());
					thirdElement.addAttribute("sfExport", ruleMenu.getSfExport());
					thirdElement.addAttribute("sfImport", ruleMenu.getSfImport());
				}else {
					thirdElement.addAttribute("isSelect", "0");
				}
			}
		}
		ChinaNetsUtil.writXml(document);
		return document.asXML();
	}
	/**
	 * 编辑用户权限时，查询所有菜单树
	 */
	public String getUserMenuTree(Long userid){
		Document document = DocumentHelper.createDocument();
		return getUserMenuTree(userid,true);
	}
	
	/**
	 * 获取用户登录成功后的菜单XML
	 * @param userid 用户ID
	 * @return
	 */
	public String getUserLoginTree(Long userid){
		return getUserMenuTree(userid,false);
	}
	
	/**
	 * @param userid 用户ID
	 * @param isAddUnselected 是否添加未选中的节点
	 * @return
	 */
	private String getUserMenuTree(Long userid,Boolean isAddUnselected) {
		Document document = DocumentHelper.createDocument();
		
		Element root = document.addElement("root");
		root.addAttribute("label", "所有功能菜单");
		root.addAttribute("id", "0");
		root.addAttribute("isSelect", "1");
		Users user = (Users) super.getObjectById(new Users(), userid);
		if(user == null) return null;
		root = getUserMenuPart(document,root,user,"wzgl","物资管理");
		root = getUserMenuPart(document,root,user,"hwgl","会务管理");
		root = getUserMenuPart(document,root,user,"clgl","车辆管理");
		root = getUserMenuPart(document,root,user,"rcfw","日常服务");
		root = getUserMenuPart(document,root,user,"xtfw","系统服务");
		
		ChinaNetsUtil.writXml(document);
		return document.asXML();
	}
	
		private Element getUserMenuPart(Document document,Element root,Users user,String bz,String nodeLael){
			try{
				List menuList = super.getObjectList(new Menus(), "this_.bz='"+bz+"' and 1=1 order by id asc");
				List userMenuList = getUserMenus(user);
				if(menuList!=null && menuList.isEmpty()){return root;}
				
				Element secondNode = root.addElement("node");
				secondNode.addAttribute("label",nodeLael );
				secondNode.addAttribute("isSelect", "0");
			
			for (Iterator iterator = menuList.iterator(); iterator.hasNext();) {
				Menus menu = (Menus) iterator.next();
				if (menu.getMenus() == null) {// 第一级菜单
					Element firstElement = secondNode.addElement("node");
					firstElement.addAttribute("id", menu.getId().toString());
					firstElement.addAttribute("label", menu.getMc());
					if(userMenuList.contains(menu)) {
						firstElement.addAttribute("isSelect", "1");
					}else {
						firstElement.addAttribute("isSelect", "0");
					}
				}
				if (menu.getMenus() != null && menu.getMenus().getMenus() == null) {// 第二级菜单
					String xPath = "/root/node/node[@id='" + menu.getMenus().getId() + "']";
					Element firstElement = (Element) document.selectNodes(xPath).get(0);
					Element secondElement = firstElement.addElement("node");
					secondElement.addAttribute("id", menu.getId().toString());
					secondElement.addAttribute("label", menu.getMc());
					if(userMenuList.contains(menu) ) {
						updateParentTree(secondElement);
						secondElement.addAttribute("isSelect", "1");
						if(menu.getMenuses() != null && menu.getMenuses().size() == 0) {
							UsersMenus userMenu = getUsersMenusByMenuid(user, menu.getId());
							secondElement.addAttribute("sfAdd", userMenu.getSfAdd());
							secondElement.addAttribute("sfModify", userMenu.getSfModify());
							secondElement.addAttribute("sfDelete", userMenu.getSfDelete());
							secondElement.addAttribute("sfSearch", userMenu.getSfSearch());
							secondElement.addAttribute("sfExport", userMenu.getSfExport());
							secondElement.addAttribute("sfImport", userMenu.getSfImport());
							secondElement.addAttribute("searchCondition", userMenu.getSearchCondition());
							secondElement.addAttribute("sfOther", userMenu.getSfOther());
							secondElement.addAttribute("isRule", "1");
						}
					}else {
						secondElement.addAttribute("isSelect", "0");
						if(menu.getMenuses() != null && menu.getMenuses().size() == 0) {
							secondElement.addAttribute("isRule", "1");
						}
					}
				}
				if (menu.getMenus() != null && menu.getMenus().getMenus() != null) {// 第三级菜单
					String xPath = "/root/node/node/node[@id='" + menu.getMenus().getId() + "']";
					Element secondElement = (Element) document.selectNodes(xPath).get(0);
					Element thirdElement = secondElement.addElement("node");
					thirdElement.addAttribute("id", menu.getId().toString());
					thirdElement.addAttribute("label", menu.getMc());
					thirdElement.addAttribute("isRule", "1");
					if(userMenuList.contains(menu)) {
						updateParentTree(thirdElement);
						thirdElement.addAttribute("isSelect", "1");
						UsersMenus userMenu = getUsersMenusByMenuid(user, menu.getId());
						thirdElement.addAttribute("sfAdd", userMenu.getSfAdd());
						thirdElement.addAttribute("sfModify", userMenu.getSfModify());
						thirdElement.addAttribute("sfDelete", userMenu.getSfDelete());
						thirdElement.addAttribute("sfSearch", userMenu.getSfSearch());
						thirdElement.addAttribute("sfExport", userMenu.getSfExport());
						thirdElement.addAttribute("sfImport", userMenu.getSfImport());
						thirdElement.addAttribute("searchCondition", userMenu.getSearchCondition());
						thirdElement.addAttribute("sfOther", userMenu.getSfOther());
					}else {
						thirdElement.addAttribute("isSelect", "0");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return root;
		}
		
	private void updateParentTree(Element thirdElement){
		/*List rootList = docm.getRootElement().elements();
		int isSelectFirst = 0;
		for(int i=0;i<rootList.size();i++){
			int isSelectSecond = 0;
			Element secondElem = (Element)rootList.get(i);
			List secondList = secondElem.elements();
			if(secondList != null)
			for(int j=0;j<secondList.size();j++){
				int isSelectThird = 0;
				Element thirdElem = (Element)secondList.get(j);
				List thirddList = secondElem.elements();
				for(int k=0;k<thirddList.size();k++){
					Element  forthElem = (Element)thirddList.get(k);
					if(forthElem.attribute("isSelect") != null){
						System.out.println("forthElem " +forthElem.attribute("isSelect").getText() );
						if(forthElem.attribute("isSelect").getText() != "0"){isSelectThird = 2;}
						if(isSelectThird == 2 && forthElem.attribute("isSelect").getText() != "1"){ break;}
					if((forthElem.attribute("isSelect").getText() == "1" && i==(thirddList.size()-1))){
						isSelectThird = 1;
					}
					}
					forthElem.getParent().setAttributeValue("isSelect", isSelectThird+"");
				
				}
			
				if(thirdElem.attribute("isSelect") != null){
					System.out.println("thirdElem " +thirdElem.attribute("isSelect").getText() );
					if(thirdElem.attribute("isSelect").getText()!= "0"){isSelectSecond = 2;}
					if(isSelectSecond == 2 && thirdElem.attribute("isSelect").getText() != "1"){ break;}
				if((thirdElem.attribute("isSelect").getText() == "1" && i==(secondList.size()-1))){
					isSelectSecond = 1;
				}
				}
				thirdElem.getParent().setAttributeValue("isSelect", isSelectSecond+"");
			}
			
			if(secondElem.attribute("isSelect") != null){
				System.out.println("secondElem " +secondElem.attribute("isSelect").getText() );
				if(secondElem.attribute("isSelect").getText()!= "0"){isSelectFirst = 2;}
				if(isSelectFirst == 2 && secondElem.attribute("isSelect").getText() != "1"){ break;}
			if((secondElem.attribute("isSelect").getText() == "1" && i==(rootList.size()-1))){
				isSelectFirst = 1;
			}
			}
			secondElem.getParent().setAttributeValue("isSelect", isSelectFirst+"");
		}*/
		
		
		
		if(thirdElement.getParent() != null){
			thirdElement.getParent().setAttributeValue("isSelect", "2");
			List childList = thirdElement.getParent().elements();
			if(childList != null)
			for(int scdi = 0;scdi < childList.size();scdi++){
				if(((Element)childList.get(scdi)).attribute("isSelect") != null){
					if(((Element)childList.get(scdi)).attribute("isSelect").getText() != "1")break;
				if(((Element)childList.get(scdi)).attribute("isSelect").getText() == "1" && scdi==(childList.size()-1)){
					thirdElement.getParent().setAttributeValue("isSelect", "1");
				}
				}
			}
			if(thirdElement.getParent().getParent()!=null){
				thirdElement.getParent().getParent().setAttributeValue("isSelect", "2");
				 childList = thirdElement.getParent().getParent().elements();
				if(childList != null)
					for(int scdi = 0;scdi < childList.size();scdi++){
						if(((Element)childList.get(scdi)).attribute("isSelect") != null){
							if(((Element)childList.get(scdi)).attribute("isSelect").getText() != "1")break;
						if(((Element)childList.get(scdi)).attribute("isSelect").getText() == "1" && scdi==(childList.size()-1)){
							thirdElement.getParent().setAttributeValue("isSelect", "1");
						}
					}
				}
			}
		}
	}
	private List getUserMenus(Users user){
		if(user.getMc() == "admin"){//管理员 后面添加管理员
			return super.getObjectList(new Menus(), " 1=1 order by id asc");
		}
		List listRst = new ArrayList();
		UsersMenus ums;
		for(int i=0;i<user.getUsersMenuses().size();i++){
			ums = (UsersMenus) user.getUsersMenuses().get(i);
			if(ums == null )continue;
			listRst.add(ums.getMenus());
		}
		return listRst;
	}
	private UsersMenus getUsersMenusByMenuid(Users user,Long umid){
		UsersMenus ums;
		for(int i=0;i<user.getUsersMenuses().size();i++){
			ums = (UsersMenus) user.getUsersMenuses().get(i);
			if(ums == null )continue;
			if(ums.getMenus().getId() == umid){
				return ums;
			}
		}
		return null;
	}
	
	/**
	 * 得到最大的优先级
	 * 
	 * @return
	 */
	public int getMaxYxj() {
		return ruleDao.getMaxYxj();
	}

	/**
	 * 验证角色名称是否存在
	 */
	public boolean validateRuleMc(String mc) {
		Rules rule = new Rules();
		rule.setMc(mc);
		List ruleList = super.getObjectList(rule);
		if (ruleList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据ID查询角色
	 */
	public Rules findRuleById(Long id) {
		Rules rule = (Rules) super.getObjectById(new Rules(), id);
		/*Rules ruleVo = new Rules();
		ruleVo.setId(rule.getId());
		ruleVo.setMc(rule.getMc());
		ruleVo.setYxj(rule.getYxj());
		ruleVo.setBz(rule.getBz());
		ruleVo.setBmid(rule.getBmid());
		ruleVo.setBmmc(rule.getBmmc());*/
		return rule;
	}
	
	/**
	 * 查询用户的科目权限
	 */
	public Rules findKmRuleById(Long id) {
		Rules rule = (Rules) super.getObjectById(new Rules(), id);
		/*Rules ruleVo = new Rules();
		ruleVo.setId(rule.getId());
		ruleVo.setMc(rule.getMc());
		ruleVo.setYxj(rule.getYxj());
		ruleVo.setBz(rule.getBz());
		ruleVo.setBmid(rule.getBmid());
		ruleVo.setBmmc(rule.getBmmc());
		ruleVo.setKmcz(rule.getKmcz());*/
		return rule;
	}

	/**
	 * 修改角色
	 * 
	 * @param ruleVo
	 */
	public void updateRule(Rules ruleVo) {
		Rules rule = (Rules) super.getObjectById(new Rules(), ruleVo.getId());
		String aYxj = rule.getYxj();
		if (aYxj.equals(ruleVo.getYxj())) {
			rule.setMc(ruleVo.getMc());
			rule.setBz(ruleVo.getBz());
			rule.setKmcz(ruleVo.getKmcz());
			super.updateObject(rule);
		} else {
			Rules rulePo = new Rules();
			rulePo.setYxj(ruleVo.getYxj());
			List ruleList = super.getObjectList(rulePo);
			if (ruleList.size() > 0) {
				Rules rule2 = (Rules) ruleList.get(0);
				rule2.setYxj(aYxj);
				super.updateObject(rule2);
			}
			
			rule.setYxj(ruleVo.getYxj());
			rule.setMc(ruleVo.getMc());
			rule.setBz(ruleVo.getBz());
			rule.setKmcz(ruleVo.getKmcz());
			super.updateObject(rule);
			
		}
	}

	/**
	 * 根据角色ID查询所对应的菜单
	 * @param id
	 */
	public List getMenuByRuleId(Long id) {
		List returnList = new ArrayList();
		Rules rule = (Rules) super.getObjectById(new Rules(), id);
		List ruleMenuList = rule.getRulesMenuses();
		for (Iterator iterator = ruleMenuList.iterator(); iterator.hasNext();) {
			RulesMenus ruleMenu = (RulesMenus) iterator.next();
			Menus menu = ruleMenu.getMenus();
			returnList.add(menu);
		}
		return returnList;
	}

	/**
	 * 保存角色菜单中间表
	 * @param ruleMenuList
	 * @param ruleId
	 */
	public void saveRuleMenu(List ruleMenuList, Long ruleId,String deptId,String deptName) {
		ruleDao.deleteRuleMenuByruleId(ruleId);
		Rules rule = (Rules) super.getObjectById(new Rules(), ruleId);
		rule.setBmid(deptId);
		rule.setBmmc(deptName);
		super.updateObject(rule);
		for (Iterator iterator = ruleMenuList.iterator(); iterator.hasNext();) {
			RuleMenuVo rmVo = (RuleMenuVo) iterator.next();
			RulesMenus rm = new RulesMenus();
			BeanUtils.copyProperties(rmVo, rm);
			Menus menu = (Menus) super.getObjectById(new Menus(), rmVo.getMenuID());
			rm.setMenus(menu);
			rm.setRules(rule);
			super.saveObject(rm);
		}
	}
	private boolean isinValStr(String str){
		return (!"是".equals(str));
	}
	/**
	 * 保存用户-菜单中间表USERS_MENUS
	 */
	public void saveUserMenu(List ruleMenuList, Long userId,String deptId,String deptName) {
	//	ruleDao.deleteRuleMenuByruleId(ruleId);
		Users user = (Users) super.getObjectById(new Users(), userId);
		if(user == null) return;
		//setBmid(deptId);
	//	user.setBmmc(deptName);
	//	super.updateObject(rule);
		ruleDao.deleteUserMenuByruleId(userId);
		List userMenuList = new ArrayList();
		try{
		for (Iterator iterator = ruleMenuList.iterator(); iterator.hasNext();) {
			UserMenuVo rmVo = (UserMenuVo) iterator.next();
			if(rmVo.getMenuID() == 0 || CommonMethods.isNullOrWhitespace(rmVo.getMenuID()+"")){continue;}
			UsersMenus rm = new UsersMenus();
			BeanUtils.copyProperties(rmVo, rm);
			if(isinValStr(rm.getSfAdd()) && isinValStr(rm.getSfDelete()) &&isinValStr(rm.getSfSearch()) 
					&& isinValStr(rm.getSfModify()) && isinValStr(rm.getSfImport()) &&isinValStr(rm.getSfExport()) 
					 &&isinValStr(rm.getSfOther())  && isinValStr(rm.getSearchCondition())) continue;
			Menus menu = (Menus) super.getObjectById(new Menus(), rmVo.getMenuID());
			rm.setUsers(user);
			rm.setMenus(menu);
			rm.setSearchCondition(deptId);//统一设置菜单的setSearchCondition
			super.saveObject(rm);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 保存角色权限
	 * 同时更新属于这个角色的所有用户的权限
	 * @param ruleMenuList
	 * @param ruleId
	 */
	public void saveRuleUserMenu(List ruleMenuList, Long ruleId,String deptId,String deptName) {
		Rules rule = (Rules) super.getObjectById(new Rules(),ruleId);
		if(rule.getUserses() != null)
		for(int i=0;i<rule.getUserses().size();i++){
			Users usch =(Users) rule.getUserses().get(i);
			if(!usch.getUsersMenuses().isEmpty()){
			saveUserMenu(ruleMenuList,usch.getId(),deptId,deptName);
			}
		}
		saveUserMenu(ruleMenuList,rule.getUserId(),deptId,deptName);
	}
	
	/**
	 * 获取所处该角色下所有用户
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getUserByRule(Rules ruleVo){
		//add by songmz
		if(ruleVo == null) {
			return new ArrayList();
		}
		//end
		
		List list = new ArrayList();
		Rules rule=(Rules)super.getObjectById(new Rules(), ruleVo.getId());
		List lt=rule.getUserses();
		for(Iterator iterator=lt.iterator();iterator.hasNext();){
		 Users user=(Users)iterator.next();
		 UserVo vo = new UserVo();
		 vo.setMc(user.getMc());
		 vo.setUserName(user.getZsxm());
		 vo.setBmmc(user.getBmmc());
		 vo.setIsShr(user.getIswfshr());
		 
		 Rules rule1 = null;
			for(int i=0;i<user.getRuleses().size();i++){ //判断 角色优先级
				if(user.getRuleses().get(i) == null)continue;
				if(rule1 == null){rule1 = (Rules)user.getRuleses().get(0); continue;}
				if(Integer.parseInt(rule1.getYxj()) > Integer.parseInt(((Rules)user.getRuleses().get(i)).getYxj())){
					rule1 = (Rules)user.getRuleses().get(i);
				}
			}
		 vo.setRuleYxj(rule1.getYxj());
		 
		 
		 Employee employee = (Employee) super.getObjectById(new Employee(),
					user.getId());
		 if (employee != null) {
				vo.setUserName(employee.getXm());
				Dept dept = employee.getDept();
				vo.setDeptId(dept.getId()+"");
				vo.setDeptName(dept.getMc());
		}
		List ruleList = user.getRuleses();
		StringBuffer ruleId = new StringBuffer();
		StringBuffer ruleName = new StringBuffer();
		ruleId.append(rule.getId());
		ruleName.append(rule.getMc());
		vo.setRuleId(ruleId.toString());
		vo.setRuleName(ruleName.toString());
		vo.setIsLeader(user.getIsleader());
		//end
		 
		 list.add(vo);
	  }
		return list;

		/*
		 List lt=super.getObjectBySql("select user_id from users_rules where rule_id="+ruleVo.getId());
		 List list = null;
		 if(lt != null){
			 list = new ArrayList();
			 for(int i=0;i<lt.size();i++){
				 BigDecimal bigDecimal= (BigDecimal)lt.get(i);
				 Long id=bigDecimal.longValue();
				 Users user=(Users)super.getObjectById(new Users(), id);
				 UserVo vo = new UserVo();
				 vo.setMc(user.getMc());
				 vo.setMm(user.getMm());
				 vo.setBmmc(user.getBmmc());
				 list.add(vo);
			 }
		 }
		return list;
		*/
	}

	/**
	 * add by songmz
	 * 根据ruleVo属性查询ruleVo的列表
	 */
	@SuppressWarnings("unchecked")
	public List getRuleVoList(Rules roleVo) {
		return  super.getObjectList(new Rules(), "mc like '%" + roleVo.getMc() + "%' order by YXJ");
		/*List<Rules> resultList = new ArrayList<Rules>();
		List<Rules> list = super.getObjectList(new Rules(), "mc like '%" + roleVo.getMc() + "%' order by YXJ");
//		List list = super.getObjectList(rule);
		for(Rules rule : list) {
			Rules ruleVo = new Rules();
			ruleVo.setId(rule.getId());
			ruleVo.setMc(rule.getMc());
			ruleVo.setYxj(rule.getYxj());
			ruleVo.setBz(rule.getBz());
			ruleVo.setBmid(rule.getBmid());
			ruleVo.setBmmc(rule.getBmmc());
			ruleVo.setKmcz(rule.getKmcz());
			resultList.add(ruleVo);
		}
		return resultList;*/
	}

	@SuppressWarnings("unchecked")
	public List getUserByRuleList(List<Rules> list) {
		List userList = new ArrayList();
		for(int i = 0; i < list.size(); i++) {
			List<UserVo> tempList = getUserByRule(list.get(i));
			for(UserVo userVo : tempList) {
				userList.add(userVo);
			}
		}
		return userList;
	}

	/**
	 * 获取所处该部门下所有用户
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getUserByDept(Dept ruleVo){
		//add by songmz
		if(ruleVo == null) {
			return new ArrayList();
		}
		//end
		
		List list = new ArrayList();
		Dept deptVo=(Dept)super.getObjectBySId(new Dept(), ruleVo.getId());
		List lt=deptVo.getUserses();
		for(Iterator iterator=lt.iterator();iterator.hasNext();){
		 Users user=(Users)iterator.next();
		 UserVo vo = new UserVo();
		 vo.setMc(user.getMc());
		 vo.setUserName(user.getZsxm());
		 vo.setBmmc(user.getBmmc());
		 vo.setIsShr(user.getIswfshr());
		 //add by songmz
		 Employee employee = (Employee) super.getObjectById(new Employee(),
					user.getId());
		 if (employee != null) {
				vo.setUserName(employee.getXm());
				Dept dept = employee.getDept();
				vo.setDeptId(dept.getId()+"");
				vo.setDeptName(dept.getMc());
		}
		 Rules rule1 = null;
			for(int i=0;i<user.getRuleses().size();i++){ //判断 角色优先级
				if(user.getRuleses().get(i) == null)continue;
				if(rule1 == null){rule1 = (Rules)user.getRuleses().get(0); continue;}
				if(Integer.parseInt(rule1.getYxj()) > Integer.parseInt(((Rules)user.getRuleses().get(i)).getYxj())){
					rule1 = (Rules)user.getRuleses().get(i);
				}
			}
		StringBuffer ruleId = new StringBuffer();
		StringBuffer ruleName = new StringBuffer();
		ruleId.append(rule1.getId());
		ruleName.append(rule1.getMc());
		vo.setRuleYxj(rule1.getYxj());
		vo.setRuleId(ruleId.toString());
		vo.setRuleName(ruleName.toString());
		vo.setIsLeader(user.getIsleader());
		//end
		 
		 list.add(vo);
	  }
		return list;
	}
}
