package asset_bfdj
{
	import cn.cnsasfram.entity.DoResult;
	import cn.cnsasfram.entity.enum.Errors;
	import cn.cnsasfram.helper.ASJsonHelper;
	
	import com.adobe.serialization.json.JSON;
	
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.events.Event;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import flex.util.CommonMethod;
	import flex.util.CommonXMLData;
	
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.controls.LinkButton;
	import mx.core.Application;
	import mx.core.FlexGlobals;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class ProcessMethods
	{
		public function ProcessMethods()
		{
		}
		/**
		 *转交到下一步的公共流程方法 
		 **/
		public static function PassToNextStep(fwLsjlVo:Object,nextStepVo:Object,currentWd:Object,callBackFunc:Function=null):void{
			if(nextStepVo.xybclr == "流程结束"){
			}
			var docRem:RemoteObject = new RemoteObject();
			docRem.destination = "allAssetService";
			docRem.endpoint = "/mxAsset/messagebroker/amf";
			docRem.showBusyCursor = true;
			
			docRem.passToNextSetp(JSON.encode(fwLsjlVo),JSON.encode(nextStepVo));
			docRem.addEventListener(ResultEvent.RESULT,passDataToCallBackFunc(nextStepVo,currentWd,callBackFunc));  
			docRem.addEventListener(FaultEvent.FAULT,initfaultZJ);  
		} 
		public static function passDataToCallBackFunc(nextStepVo:Object,passData:Object,callBackFunc:Function):Function{
			return function(event:ResultEvent){
				initbackZJ(event,nextStepVo,passData,callBackFunc);
			}
		}
		private static function initbackZJ(event:ResultEvent,nextStepVo:Object,currentWd:Object,callBackFunc:Function):void
		{
			var doResult:DoResult=ASJsonHelper.stringJsonConverToDoResult(event.result.toString());
			if(doResult!=null && doResult.nRetCode!=Errors.OK){
				Alert.show("流程操作失败! "+doResult.strErrorInfo);
				return;
			}
			Alert.show("提交成功！",CommonXMLData.Alert_Title);
			currentWd.parentPage.loadData();
			PopUpManager.removePopUp(currentWd as IFlexDisplayObject);
			if(nextStepVo.xybclr.indexOf("结束") > -1){
				if(callBackFunc != null){callBackFunc.call();}
			}
		//	PopUpManager.removePopUp(currentWd.parentDocument as IFlexDisplayObject);
		}
		private static function initfaultZJ(evt:FaultEvent):void
		{
			Alert.show("提交失败！",CommonXMLData.Alert_Title);
		}
		
		
		/**
		 *初始化Form页面时 根据流程状态更改页面状态 
		 **/
		public static function changeProcessBtnState(currentPwd:Object,currentStatus:String){
			var saveBtn:Object = null;
			var newBtn:Object = null;
			var submitBtn:Object = null;
			if(currentPwd.hasOwnProperty("saveAction"))saveBtn = currentPwd["saveAction"];
			if(currentPwd.hasOwnProperty("newAction"))newBtn = currentPwd["newAction"];
			if(currentPwd.hasOwnProperty("nextStepAction")) submitBtn = currentPwd["nextStepAction"];
			if(!currentPwd.hasOwnProperty("dataFormBox")) {Alert.show("未找到流程权限相关的按钮ID : dataFormBox!流程权限将失效!");return;}
			if(saveBtn == null || submitBtn == null || newBtn==null){Alert.show("未找到流程权限相关的按钮ID!流程权限将失效!");return;}
			if(currentStatus.indexOf("草稿") > -1){
				
			}
			if(currentStatus.indexOf("待办理") > -1 ){
					saveBtn.visible = false;saveBtn.includeInLayout=false;
					var dqbzBas:String = currentPwd.nextStepVo.xgbz;
					var dqbzStr:String = (dqbzBas.indexOf("审核") > -1 ? ("提交至" + dqbzBas.replace("审核","")) : "报废结束");
					submitBtn.label =  dqbzStr;//"审核";
					submitBtn.toolTip = "审核通过," + dqbzStr;
			//		CommonMethod.setEnable(currentPwd.dataFormBox as DisplayObjectContainer);
			}
			if(currentStatus.indexOf("结束") > -1 || currentStatus.indexOf("办理中") > -1 || currentStatus.indexOf("已办理") > -1 ){
					saveBtn.visible = false;saveBtn.includeInLayout=false;
					submitBtn.visible = false;submitBtn.includeInLayout=false;
			//		CommonMethod.setEnable(currentPwd.dataFormBox as DisplayObjectContainer);
			}
		}
		/**
		 *隐藏流程页面按钮 
		 **/
		public static function changeCNSTModuelState(currentWd:Object,isAddEnable:Boolean,isEditEnable:Boolean,isDelEnable:Boolean):void{
			var isGridPage:Boolean = false;//是否GRID列表页面
			isGridPage = !(currentWd.hasOwnProperty("saveAction") && currentWd.hasOwnProperty("saveAndNewAction"));
			
			if(currentWd == null){Alert.show("null param currentWd -- changeCNSTPageState() !");return;}
			
			if(currentWd.hasOwnProperty("saveAndNewAction")){currentWd["saveAndNewAction"].visible = isAddEnable;currentWd["saveAndNewAction"].includeInLayout=isAddEnable;}
			if(currentWd.hasOwnProperty("newAction")){currentWd["newAction"].visible = isAddEnable;currentWd["newAction"].includeInLayout=isAddEnable;}
			if(currentWd.hasOwnProperty("historyAction")){currentWd["historyAction"].visible = !isAddEnable;currentWd["historyAction"].includeInLayout= !isAddEnable;}
			
			if(isGridPage){
				if(currentWd.hasOwnProperty("editAction")){currentWd["editAction"].visible = isEditEnable;currentWd["editAction"].includeInLayout=isEditEnable;}
			}else{
				if(!isEditEnable && !isAddEnable){
					if(currentWd.hasOwnProperty("saveAction")){currentWd["saveAction"].visible = false;currentWd["saveAction"].includeInLayout=false;}
					if(currentWd.hasOwnProperty("saveAndCloseAction")){currentWd["saveAndCloseAction"].visible = false;currentWd["saveAndCloseAction"].includeInLayout=false;}
					if(currentWd.hasOwnProperty("editAction")){currentWd["editAction"].visible = false;currentWd["editAction"].includeInLayout=false;}
				}else{
					if(currentWd.hasOwnProperty("saveAction")){currentWd["saveAction"].visible = true;currentWd["saveAction"].includeInLayout=true;}
					if(currentWd.hasOwnProperty("saveAndCloseAction")){currentWd["saveAndCloseAction"].visible = true;currentWd["saveAndCloseAction"].includeInLayout=true;}
					if(currentWd.hasOwnProperty("editAction")){currentWd["editAction"].visible = true;currentWd["editAction"].includeInLayout=true;}
				}
			}
			
			if(currentWd.hasOwnProperty("removeAndCloseAction")){currentWd["removeAndCloseAction"].visible = isDelEnable;currentWd["removeAndCloseAction"].includeInLayout=isDelEnable;}
			if(currentWd.hasOwnProperty("removeAction")){currentWd["removeAction"].visible = isDelEnable;currentWd["removeAction"].includeInLayout=isDelEnable;}
		}
		
		/**
		 * 查看历史记录
		 **/
		public static function historyActionActionClick(dataId:String):void{
		//	var hist:HistoryInfo = new HistoryInfo();
			var hist:WFStepShow2 = new WFStepShow2();
			hist.dataId = dataId;
			PopUpManager.addPopUp(hist,Application.application as DisplayObject,true);
			PopUpManager.centerPopUp(hist);
		}
	   /**
	   * 跳转到我的任务
	   * 
	   * ID使用数据库中的
	   *    101	入库登记
		*	102	领用申请
		*	103	退库登记
		*	104	维修登记
		*	105	报废申请
	   **/
		public static function gotoMyMession( rwlx:String, rwid:String,rwmc:String,dqzt:String,bmldid:String=null):void{
			if(CommonMethod.isNullOrWhitespace(rwlx) || CommonMethod.isNullOrWhitespace(rwid)) return;
			//只能再主页跳转 加入当前所处页面判断
			FlexGlobals.topLevelApplication.mainPage.AssetPage.shouldSetTreeIndex = true;
			var curTreeIdx:int = 2;
			if(rwmc.indexOf("审核")> -1){
				curTreeIdx = 5;
				if(bmldid != null && bmldid != Application.application.userVo.id.toString() && Application.application.userVo.ruleId != 41){
					curTreeIdx = 6;
				}
				if(dqzt.indexOf("结束")> -1){
					curTreeIdx = 6;
				}
			}else if(rwmc.indexOf("初稿")> -1){
				curTreeIdx = 1;
			}else if(rwmc.indexOf("申请")> -1){
				curTreeIdx = 2;
				if( dqzt.indexOf("结束") > -1){
					curTreeIdx = 3;
				}
			}
			 
			switch(rwlx){
				case "领用":
					FlexGlobals.topLevelApplication.mainPage.AssetPage.defaultMenuID = "smzq_lysq";
					FlexGlobals.topLevelApplication.mainPage.AssetPage.defaultTreeIndex = 2;
					FlexGlobals.topLevelApplication.mainPage.AssetPage.modelMap["203"] = null;
					FlexGlobals.topLevelApplication.mainPage.AssetPage.defaultAssetWFTreeIndex = curTreeIdx;
					break;
				case "报废":
					FlexGlobals.topLevelApplication.mainPage.AssetPage.defaultMenuID = "smzq_bfsq";
					FlexGlobals.topLevelApplication.mainPage.AssetPage.defaultTreeIndex = 5;
					FlexGlobals.topLevelApplication.mainPage.AssetPage.modelMap["206"] = null;
					FlexGlobals.topLevelApplication.mainPage.AssetPage.defaultAssetWFTreeIndex = curTreeIdx;
					break;
				case "退库":
					FlexGlobals.topLevelApplication.mainPage.AssetPage.defaultMenuID = "smzq_tksq";
					FlexGlobals.topLevelApplication.mainPage.AssetPage.defaultTreeIndex = 3;
					FlexGlobals.topLevelApplication.mainPage.AssetPage.modelMap["204"] = null;
					break;
			}
			shouldShowWF = true;
			if(FlexGlobals.topLevelApplication.mainPage.tempSelectMenuItem.id  == "Asset"){
				FlexGlobals.topLevelApplication.mainPage.AssetPage.afterInitPage();
			}else{
			    FlexGlobals.topLevelApplication.mainPage.menuItemClickAsset();//切换到asset页面
			}
			wfrwlx = rwlx;
			wfrwid = rwid;
		}
		
		private static function getWFObjectBack(event:ResultEvent):void{
			if(event.result == null) return;
			if(wfpwd.hasOwnProperty("lookDataAction") &&　shouldShowWF){
				/*if(wfrwlx == "领用" && event.result.lyzt != "初稿"){
				wfpwd.myWFTree.defaultTreeIndex = 5;
				}
				if(wfrwlx == "报废" && event.result.bfsqzt != "初稿"){
				wfpwd.workFlowTree.defaultTreeIndex = 5;
				}*/
				wfpwd.lookDataAction(event.result);
				shouldShowWF = false;
			}
		}
		
		
		private static var wfpwd:Object = null;
		private static var wfrwlx:String = null;
		private static var wfrwid:String = null;
		private static var shouldShowWF:Boolean = true;
		/**
		 *HomeModule '我的任务'跳转到任务详情的方法 ,在Grid页面dataSearchActionBack方法中调用
		 **/
		public static function wfPageUpdateComplete(pwd:Object):void{
			wfpwd = pwd;
			var docRem:RemoteObject = new RemoteObject();
			docRem.destination = "allAssetService";
			docRem.endpoint = "/mxAsset/messagebroker/amf";
			docRem.showBusyCursor = true;
			docRem.getWFObjectById(wfrwlx,wfrwid);
			docRem.addEventListener(ResultEvent.RESULT,getWFObjectBack);  
		}
		/**
		 * 跳转到耗材详细信息页面
		 * 
		 * ID使用数据库中的
		 *	201	领用申请
		 **/
		public static function gotoMyRyhc(ryhcid:String):void{
			//只能再主页跳转 加入当前所处页面判断
			FlexGlobals.topLevelApplication.mainPage.AssetPage.shouldSetTreeIndex = true;
			FlexGlobals.topLevelApplication.mainPage.AssetPage.defaultTreeIndex = 7;
			FlexGlobals.topLevelApplication.mainPage.AssetPage.modelMap["201"] = null;
			if(FlexGlobals.topLevelApplication.mainPage.tempSelectMenuItem.id  == "Asset"){
				FlexGlobals.topLevelApplication.mainPage.AssetPage.afterInitPage();
			}else{
				FlexGlobals.topLevelApplication.mainPage.menuItemClickAsset();//切换到asset页面
			}
			wfrwid = ryhcid;
			shouldShowWF = true;
		}
		/**
		 *SysMsg 提醒 跳转到任务详情的方法 ,在Grid页面dataSearchActionBack方法中调用
		 **/
		public static function ryhcPageUpdateComplete(pwd:Object):void{
			wfpwd = pwd;
			var docRem:RemoteObject = new RemoteObject();
			docRem.destination = "allAssetService";
			docRem.endpoint = "/mxAsset/messagebroker/amf";
			docRem.showBusyCursor = true;
			docRem.getRyhcObjectById(wfrwid);
			docRem.addEventListener(ResultEvent.RESULT,getWFObjectBack);  
		}
		
	    
			
		public static const Process_Status_Name_CG:String = "初稿";
		public static const Process_Status_Name_CLZ:String = "处理中";
		public static const Process_Status_Name_YJS:String = "已结束";
		
		public static const Process_Step_Name_CG:String = "初稿";
		public static const Process_Step_Name_BMLD:String = "部门领导审核";
		public static const Process_Step_Name_YLD:String = "院领导审核";
		public static const Process_Step_Name_GLY:String = "物资管理员审核";
	}
}