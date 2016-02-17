
import com.commonpages.SysMsg;
import flash.events.Event;

import flex.util.CommonMethod;

import mx.events.FlexEvent;
import mx.events.ResizeEvent;
import mx.managers.PopUpManager;
import mx.messaging.FlexClient;

	/**
	 *初始化系统样式 
	 **/
	private function initApplicationStyle():void{
		//设置LINKBUTTON全局样式
		var tempCssClass:CSSStyleDeclaration=ASCSSClassHelper.getLinkButtonCSSClass();
		tempCssClass.setStyle("cornerRadius","0");
		tempCssClass.setStyle("fontWeight","none");
		//设置COMBOX全局样式
		tempCssClass=ASCSSClassHelper.getComBoxCSSClass();
		tempCssClass.setStyle("cornerRadius","0");
		tempCssClass.setStyle("fontSize","12");
		tempCssClass.setStyle("borderThickness","0");
		tempCssClass.setStyle("textInputStyleName","comboxtextinput");
		//设置BUTTON全局样式
		tempCssClass=ASCSSClassHelper.getButtonCSSClass();
		tempCssClass.setStyle("cornerRadius","0");
		//设置NumericStepper全局样式
		tempCssClass=ASCSSClassHelper.getNumericStepperCSSClass();
		tempCssClass.setStyle("cornerRadius","0");
		tempCssClass.setStyle("fontSize","11");
		//设置DateField全局样式
		tempCssClass=ASCSSClassHelper.getDateFieldCSSClass();
		tempCssClass.setStyle("fontSize","12");
		//设置TOOLTIP全局样式
		tempCssClass=ASCSSClassHelper.getToolTipCSSClass();
		tempCssClass.setStyle("fontSize","12");
		//设置TEXTINPUT全局样式
		tempCssClass=ASCSSClassHelper.getTextInputCSSClass();
		tempCssClass.setStyle("borderStyle","solid");
		tempCssClass.setStyle("height","22");
		tempCssClass.setStyle("borderThickness","1");
		tempCssClass.setStyle("focusThickness","1");
		//设置DATAGRID全局样式
		tempCssClass=ASCSSClassHelper.getDataGridCSSClass();
		tempCssClass.setStyle("horizontalGridLines","true");
		tempCssClass.setStyle("horizontalGridLineColor","#CCCCCC");
		tempCssClass.setStyle("rowHeight","24");
		tempCssClass.setStyle("verticalAlign","middle");
		//设置Tree样式图标
		tempCssClass=ASCSSClassHelper.getTreeCssClass();
		tempCssClass.setStyle("defaultLeafIcon",treeDefaultLeafIcon);
		//设置滚动条样式
		tempCssClass=ASCSSClassHelper.getScrollbarCssClass();
		tempCssClass.setStyle("cornerRadius","0");
		//设置panle全局样式
		tempCssClass=ASCSSClassHelper.getPanelCssClass();
		tempCssClass.setStyle("cornerRadius","0");
		tempCssClass.setStyle("dropShadowEnabled","false");
		tempCssClass.setStyle("borderColor","#99CCFF");
		tempCssClass.setStyle("borderAlpha","1.00");
		tempCssClass.setStyle("headerHeight","20");
		tempCssClass.setStyle("borderThicknessLeft","1");
		tempCssClass.setStyle("borderThicknessBottom","1");
		tempCssClass.setStyle("borderThicknessRight","1");
		tempCssClass.setStyle("titleStyleName","titleStyle");
		//设置TitleWindow全局样式
		tempCssClass=ASCSSClassHelper.getTitleWindowCssClass();
		tempCssClass.setStyle("dropShadowEnabled","false");
		tempCssClass.setStyle("modalTransparency","0.9");
		tempCssClass.setStyle("modalTransparencyBlur","0.9");
		tempCssClass.setStyle("cornerRadius","0");
		tempCssClass.setStyle("headerHeight","25");
	}

	/**
	 *登陆成功后初始化系统数据 系统启动后调用的方法,只在每次启动调用一次
	 **/
     private function initSysData():void{
		 //初始化代码表数据
		 CommonMethod.getCurrentURL();
		 this.codelistheler.initCodeList();
		 
		 //任务提醒
		 initSysMsgWd();
	 }

    

   //系统提醒
	public	var sysMsg:SysMsg = new SysMsg();
    private function initSysMsgWd():void{
		sysMsg.y =this.height;
		sysMsg.x = this.width - sysMsg.width;
		PopUpManager.addPopUp(sysMsg, this.mainPage,false);
		this.addEventListener(ResizeEvent.RESIZE,sysMsg.resetMsgWdLoc);
		
		//订阅推送
		this.mainPage.addEventListener(FlexEvent.CREATION_COMPLETE,sysMsg.subscribeRss);
	}


