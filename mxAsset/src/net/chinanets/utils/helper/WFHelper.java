package net.chinanets.utils.helper;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 工作流辅助类
 * @author RLiuyong
 *
 */
public class WFHelper {
	/**
	 * 得到根节点
	 * @return
	 */
	public static Element GetWFTreeRootElement(){
		Element rootElement= DocumentHelper.createElement("node");
		rootElement.addAttribute("id", "wftree");
		rootElement.addAttribute("label", "工作流");
		Element stateRoot=DocumentHelper.createElement("node");
		stateRoot.addAttribute("id", "mywork");
		stateRoot.addAttribute("label", "我的发起");
		stateRoot.add(GetWFStateElement("CG","草稿箱"));
		stateRoot.add(GetWFStateElement("CLZ","办理中"));
		stateRoot.add(GetWFStateElement("YJS","已结束"));
		rootElement.add(stateRoot);
		return rootElement;
	}
	
	/**
	 * 根据信息得到状态节点
	 * @param strid
	 * @param strName
	 * @return
	 */
	public static Element GetWFStateElement(String strid,String strName){
		Element tempElement=DocumentHelper.createElement("node");
		tempElement.addAttribute("id", strid);
		tempElement.addAttribute("label", strName);
		tempElement.addAttribute("wftype", "state");
		return tempElement;
	}
}
