package flex.util
{
	
	import com.commonpages.DetailDocument;
	import com.commonpages.EditDocument;
	
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.events.Event;
	import flash.net.FileReference;
	import flash.net.FileReferenceList;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.net.navigateToURL;
	import flash.system.System;
	
	import flex.pojos.DeptVo;
	import flex.pojos.FileUploadVo;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	import mx.containers.FormItem;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.controls.ComboBox;
	import mx.controls.DataGrid;
	import mx.controls.DateField;
	import mx.controls.LinkButton;
	import mx.controls.NumericStepper;
	import mx.controls.TextArea;
	import mx.controls.TextInput;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.Application;
	import mx.core.IFlexDisplayObject;
	import mx.core.IUIComponent;
	import mx.events.FlexEvent;
	import mx.formatters.DateFormatter;
	import mx.managers.PopUpManager;
	import mx.printing.FlexPrintJob;
	import mx.printing.FlexPrintJobScaleType;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	import mx.utils.StringUtil;
	
	import shry_tpgl.PICZoomModuel;
	
	/**
	 *静态公共方法类 
	 **/
	public class CommonMethod
	{
		
		/**
		 *四舍五入保留scale位小数 
		 **/
		public static function numberRandom(numIn:Number,scale:int=2):String{
			return (Math.round(numIn*Math.pow(10, scale))/Math.pow(10, scale)).toFixed(scale);
		}
		
		/**
		 *  将 ArrayCollection 转换为 number数组
		 *  keyAry:srcAry中包含的key
		 **/
		public static function getNumberArray(srcAry:ArrayCollection,keyAry:Array):Array{
			if(srcAry == null || srcAry.length<1 || keyAry==null || keyAry.length <1){
				return new Array();
			}
			//判断是否多维数组
			var isMutlAry:Boolean = keyAry.length >1;
			var rstAry:Array = new Array();
			for(var i:int = 0; i < srcAry.length; i++){
				var ch:Object = srcAry.getItemAt(i);
				if(isMutlAry){
					var sonAry:Array = new Array();
					for(var j:int = 0;j<keyAry.length;j++){
						sonAry.push(ch[keyAry[j]]);
					}
					rstAry.push(sonAry);
				}else{
					rstAry.push(keyAry[0]);
				}
			}
			
			return rstAry;
		}
		
		/**
		 *过滤数据 过滤Y轴  小于0的数据   Y轴 0到-2直接转换为0 小于-2的数据不显示
		 * 保留两位小数,并且四舍五入
		 **/
		public static function filterZeroData(dpArray:ArrayCollection,fileds:Array):ArrayCollection{
			if(dpArray == null || dpArray.length < 1){ return null;}
			var dpArrayNew:ArrayCollection = new ArrayCollection();
			for each(var eachObj:Object in  dpArray){
				var isCtu:Boolean = false;
				for each(var eachFd:String in fileds){
					if(eachObj[eachFd] == null || eachObj[eachFd] ==  undefined){ continue;}
					if(eachObj[eachFd] < 0){
						if(eachObj[eachFd] < 0 && eachObj[eachFd] >= -2){
							eachObj[eachFd]  = 0;
						}else{ isCtu=true;}
					}
					
				    eachObj[eachFd] = Math.round(new Number(eachObj[eachFd])*100)/100
				}
				if(isCtu){continue;}
				dpArrayNew.addItem(eachObj);
			}
			return dpArrayNew;
		}
		
		/**
		 *选择 一个 风叶/电机/总成数据 
		 **/
		/**
		 *选择耗材的公共页面
		 * currentWd 当前页面 (传 this);
		 * mcInput 物品名称TextInput;
		 * lxCbx 物品类型Combobox;
		 * dwCbx 物品单位Combobox;
		 * 部门Vo会存储在TextInput.data中和deptIDInput.text中;
		 **/
		public static function getAssetBH(currentWd:Object,chooseType:String,mcInput:TextInput,xhInput:TextInput=null,fyInput:TextInput=null):void{
			//var ast:SelectAssetBH= pageCacheObj.getAssetBHPage;
			//if(ast == null){ 
			var ast:SelectAssetBH = new SelectAssetBH(); 
			pageCacheObj.getAssetBHPage = ast;
			//	ast.labelName = "mc";
			//	ast.isIdOrVo = isSetVo;
			ast.mcInput = mcInput;
			ast.xhInput = xhInput;
			ast.fyInput = fyInput;
			ast.chooseType = chooseType;
			ast.changeWdVsb();
			PopUpManager.addPopUp(ast,currentWd.parentApplication as DisplayObject,true);
			PopUpManager.centerPopUp(ast);
		}
		
		/**
		 * 处理基础数据的Grid的双击事件
		 *判断是否 选择基础数据页面 如果是：双击事件就调用选中赋值 不是：执行查看详情事件 
		 **/
		public static function switchGridDoubleClickHandle(tempWindow,event:Event):void{
			if(tempWindow.pagePanel.getStyle('headerHeight') >0){
				tempWindow.gridItemDataDoubleClick(event);
			}else{
				var ast:SelectAssetBH= pageCacheObj.getAssetBHPage;
				if(ast != null){
					ast.setInputValue(event.currentTarget.selectedItem);
					ast.closeHandler();
				}
			}
			
		}
		
		//上传图片 文件上传的公共方法
		public static function modifyUploadFile(parentWd:DisplayObject,tablename:String,columnname:String,dataid:String,isDetailPage:Boolean=false,datatype:String="1",sydid:String=null):void
		{
			var editDoc:Object;
			if(isDetailPage){
				editDoc = new DetailDocument();
			}else{
				editDoc = new EditDocument();
			}
			editDoc.datatype = datatype;
			editDoc.columnname = columnname.toLowerCase();
			editDoc.paretnId = dataid;
			editDoc.tablename = tablename.toLowerCase();
			editDoc.sydId = sydid;
			editDoc.parentWindow = parentWd;
			PopUpManager.addPopUp(editDoc as IFlexDisplayObject,parentWd as DisplayObject,true);
			PopUpManager.centerPopUp(editDoc as IFlexDisplayObject); 
		}
		
		
		public static function loadTablePictures(tablename:String,columnname:String,dataid:String,datatype:String,parentPage:Object,callBackFunc:Function,sydid:String=null,args:Array=null):void{
			var docRem:RemoteObject = new RemoteObject();
			docRem.destination = "commonService";
			docRem.endpoint = "/mxAsset/messagebroker/amf";
			docRem.showBusyCursor = true;
			var xmdoc:FileUploadVo = new FileUploadVo();
			xmdoc.dataid = dataid;
			xmdoc.tablename = tablename;
			xmdoc.columnname = columnname;
			xmdoc.datatype = datatype;
			xmdoc.sydid = sydid;
			docRem.getObjectList(xmdoc);
			docRem.addEventListener(ResultEvent.RESULT,doLoadTablePictures(parentPage,callBackFunc,args));
		}
		private static function doLoadTablePictures(parentPage:Object,callBackFunc:Function,args:Array=null):Function{
			return function(event:ResultEvent){
				loadTablePicturesBack(event,parentPage,callBackFunc,args);
			}
		}
		
		private static function loadTablePicturesBack(event:ResultEvent,parentPage:Object,callBackFunc:Function,args:Array=null):void{
			if(parentPage == null || callBackFunc == undefined || callBackFunc==null){return;}
			callBackFunc.call(parentPage,event.result);
		}
		
		//显示放大图片
		public static function doshowZoomSelectedPic(tablename:String,columnname:String,dataid:String,parentPage:Object):Function{
			return function(event:Event){
				showZoomSelectedPic(event,tablename,columnname,dataid,parentPage);
			}
		}
		//显示放大图片
		public static function showZoomSelectedPic(evt:Event,tablename:String,columnname:String,dataid:String,parentPage:Object):void{
			//Alert.show("");
			if(evt.currentTarget.data == null || Number(evt.currentTarget.data) ==NaN){ return ;}
			var picMd:PICZoomModuel = new PICZoomModuel();
			picMd.tablename = tablename;
			picMd.columnname = columnname;
			picMd.paretnId = dataid;
			picMd.defaultImgIndex = int(evt.currentTarget.data);
			PopUpManager.addPopUp(picMd, parentPage as DisplayObject,true);
			PopUpManager.centerPopUp(picMd);
		}
		
		/**
		 * 打印targetWd
		 **/
		public static function  printWd(targetWd:IUIComponent):void{
			var printjob:FlexPrintJob = new FlexPrintJob();
			if(printjob.start()){
			printjob.addObject(targetWd,FlexPrintJobScaleType.NONE);
			printjob.send();
			}
		}
		
		/**
		 *选择部门的公共页面
		 * currentWd 当前页面 (传 this);
		 * deptInput 显示供应商名称的TextInput ;
		 * deptIDInput 隐藏的TextInput 存储部门ID的TextInput 可不传;
		 * 部门Vo会存储在TextInput.data中和deptIDInput.text中;
		 **/
		public static function getDept(currentWd:Object,deptInput:TextInput,isSetVo:Boolean=true,isAddAnotherDept:Boolean=true,deptIDInput:TextInput=null):void{
			var ast:SelectGYS= pageCacheObj.bmPage;
			if(ast == null){ ast = new SelectGYS(); pageCacheObj.bmPage = ast;}
			ast.title = "选择部门";
			ast.isAddAnotherDept = isAddAnotherDept;
			ast.voType = new DeptVo();
			ast.labelName = "mc";
			ast.isIdOrVo = isSetVo;
			ast.syrInput = deptInput;
			ast.syrIDInput = deptIDInput;
			PopUpManager.addPopUp(ast,currentWd.parentApplication as DisplayObject,true);
			PopUpManager.centerPopUp(ast);
		}
		
		
		//读取XML文件
		private static var ipCfgXml:XML;
		public static function loadIpConfigXml():void{
			if(ipCfgXml == null){
			var urlLoader:URLLoader = new URLLoader();
			urlLoader.addEventListener(Event.COMPLETE,handleURLLoaderCompleted);
			urlLoader.load(new URLRequest("xml/IPConfig.xml"));
			}
		}
		private static function handleURLLoaderCompleted(event:Event):void {
			var loader:URLLoader = event.target as URLLoader;
			ipCfgXml = XML(loader.data);
		}
		public static function readIpConfigXml(id:String):String{
			loadIpConfigXml();
			var address:String = null;
			if(ipCfgXml != null){
		//	trace(ipCfgXml.VALUE.length());
			for(var i:int=0;i<ipCfgXml.VALUE.length();i++){
				var stri:String = ipCfgXml.VALUE[i].ID;
				if(StringUtil.trim(stri)== id){
					address = ipCfgXml.VALUE[i].ADDR;
					break;
				}
			}
			}
			return address;
		}
		public static const arrBtnLastClm:ArrayCollection = new ArrayCollection(new Array("选择项","批复","执行","选择","转交","修改")); // 选择显示列时需要排除的按钮列
		
		//导出excel文件 使用get 跳转到servlet 或者只用做跳转
		public static function expExcelJsp(servletName:String,expType:String=""):void
		{
			var sendData:URLVariables = new URLVariables();
			sendData.expType=expType;
			var request:URLRequest = new URLRequest();
			request.url="/mxAsset/servlet/"+servletName;
			request.data=sendData;
			request.method=URLRequestMethod.GET;
			navigateToURL(request,"_self");
			
		}
		
		/**
		 *导出入库单及明细到Word模板
		 * 1.勾选数据 导出勾选的数据 不勾选导出所有
		 **/
		//导出当前dataGrid数据
		public static function exportBGDToWord(expType:String,dataId:String):void{
			if(isNullOrWhitespace(expType) || isNullOrWhitespace(dataId)){return;}
			var variables:URLVariables = new URLVariables();
			variables.expType = expType;
			variables.dataId = dataId;
			
			var u:URLRequest = new URLRequest("/mxAsset/servlet/ReportServlet");
			u.data = variables; 
			u.method = URLRequestMethod.POST; 
			navigateToURL(u,"_self");		
		}
		
		/**
		 * 弃用
		 *导出入库单及明细EXCEL数据 
		 * 1.勾选数据 导出勾选的数据 不勾选导出所有
		 **/
		//导出当前dataGrid数据
		public static function exportDoubleWFExcelData(dataGrid:DataGrid,searchCdn:String,idColumn:String,tableName:String,fileName:String
			                                          ,sheetTitle:String,parentDataGrid:DataGrid,parentSearchCdn:String):void {
			idColumn = idColumn.toLowerCase();
			var item:ArrayCollection=dataGrid.dataProvider as ArrayCollection;
			var arry:Array=new Array();//用来保存需要导出数据的id
			var idStr:String = "(";
			if(item!=null){
				for(var i:int=0;i<item.length;i++)
				{
					if(item.getItemAt(i).selected)
					{
						arry.push(item.getItemAt(i)[idColumn]);
						idStr += ("'" + item.getItemAt(i)[idColumn] + "', ")
					}
				} 
			}
			idStr = idStr.substr(0, idStr.lastIndexOf(","));
			idStr += ")";
			var where:String = "where 1=1 ";
			if(arry.length > 0) {
				where += " and "+idColumn+" in " + idStr;
			}else{
			where = (CommonMethod.isNullOrWhitespace(searchCdn)? "" :  searchCdn);
			}
			var exportExcel:ExportExcel2 = new ExportExcel2();
			var ary:Array = new Array();
			for(var i:int=1;i<dataGrid.columns.length;i++){
				if(CommonMethod.isNullOrWhitespace(dataGrid.columns[i].dataField) || CommonMethod.isNullOrWhitespace(dataGrid.columns[i].headerText)){
					continue;
				}
				if(!dataGrid.columns[i].visible){continue;}
				ary.push(dataGrid.columns[i]);
			}
			
			var parentAry:Array = new Array();
			for(var i:int=1;i<parentDataGrid.columns.length;i++){
				if(CommonMethod.isNullOrWhitespace(parentDataGrid.columns[i].dataField) || CommonMethod.isNullOrWhitespace(parentDataGrid.columns[i].headerText)){
					continue;
				}
				if(!parentDataGrid.columns[i].visible){continue;}
				parentAry.push(parentDataGrid.columns[i]);
			}
			
			exportExcel.ExportDoubleTitleDGToExcel(ary,tableName,fileName+".xls",sheetTitle,where,parentAry,parentSearchCdn,"/mxAsset/servlet/ExportServlet",idColumn,"dbt");
		}
		/**
		 *导出EXCEL数据 
		 * 1.勾选数据 导出勾选的数据 不勾选导出所有
		 **/
		//导出当前dataGrid数据
		public static function exportWFExcelData(dataGrid:DataGrid,searchCdn:String,idColumn:String,tableName:String,fileName:String,sheetTitle:String="信息列表"):void {
			idColumn = idColumn.toLowerCase();
			var item:ArrayCollection=dataGrid.dataProvider as ArrayCollection;
			var arry:Array=new Array();//用来保存需要导出数据的id
			var idStr:String = "(";
			if(item!=null){
				for(var i:int=0;i<item.length;i++)
				{
					if(item.getItemAt(i).selected)
					{
						arry.push(item.getItemAt(i)[idColumn]);
						idStr += ("'" + item.getItemAt(i)[idColumn] + "', ")
					}
				} 
			}
			idStr = idStr.substr(0, idStr.lastIndexOf(","));
			idStr += ")";
			var isWFExp:String = "false";
			var where:String = "where 1=1 ";
			if(arry.length > 0) {
				where += " and "+idColumn+" in " + idStr;
			    isWFExp = "false";
			}else{
			where = (CommonMethod.isNullOrWhitespace(searchCdn)? "" :  searchCdn);
			}
			var exportExcel:ExportExcel2 = new ExportExcel2();
			var ary:Array = new Array();
			for(var i:int=1;i<dataGrid.columns.length;i++){
				if(CommonMethod.isNullOrWhitespace(dataGrid.columns[i].dataField) || CommonMethod.isNullOrWhitespace(dataGrid.columns[i].headerText)){
					continue;
				}
				if(!dataGrid.columns[i].visible){continue;}
				ary.push(dataGrid.columns[i]);
			}
			
			exportExcel.ExportDGToExcel2(ary,tableName,fileName+".xls",sheetTitle,where,"/mxAsset/servlet/ExportServlet",idColumn,isWFExp);
		}
		
		
		/**
		 * 文件上传验证 1.数量10个以内 2.单个大小1m
		**/
		public static function validateFileUpload(fileCount:int,uploadFileList:FileReferenceList):Boolean{
			if(fileCount>10){
				Alert.show("您选择的文件过多,每个项目最多上传10个附件!请重新选择!",CommonXMLData.Alert_Title);
				return false;
			}
			for each(var file:FileReference in uploadFileList.fileList){
				if(file.size > (1024*1024*1)){
					Alert.show("您选择的文件过大,单个文件最大不超过1M!请重新选择!",CommonXMLData.Alert_Title);	
					return false;
				}
			}
			return true;
		}
		
		//文件下载
		public static function downloadExcel(url:String,type:String="_blank"):void {
			//"excelDownLoad/jz.xls"
			var urlRequest:URLRequest = new URLRequest(url);
			navigateToURL(urlRequest,type);
		}
		
		public static var ApplicationURL:String; 
		//获取当前访问IP
		public static function getCurrentURL():void{
			var url:String = Application.application.url;
			if(url.indexOf("/Login") > -1){
			ApplicationURL = url.substr(0,url.indexOf("/Login")+1);
			}else if(url.indexOf("/mxAsset") > -1){
				ApplicationURL = url.substr(0,url.indexOf("/mxAsset") + 9);
			}else if(url.lastIndexOf("/") == (url.length - 1)){
				ApplicationURL = url+"mxAsset/";
			}
		}
		
		
		
		/**
		 *根据text获取Comobox item索引 
		 * strictStr 强制设置labelField
		 **/
		public static function getCbxItemIndex(str:String,cbx:ComboBox,columnStr:String="mc",strictStr:String=null):int{
			if(str == null || cbx == null )return 0;
			columnStr = cbx.labelField;
			if(strictStr != null){columnStr = strictStr;}
			if(cbx.dataProvider is ArrayCollection){
			var arr:ArrayCollection = cbx.dataProvider as ArrayCollection;
			if(arr == null) return -1;
			for(var i:int=0;i<arr.length;i++){
				var obj:Object = arr[i];
				if(obj == null)continue;
				if(obj[columnStr] == str) return i;
			}
			}else if(cbx.dataProvider is XMLListCollection){
				var arr1:XMLListCollection = cbx.dataProvider as XMLListCollection;
				if(arr1 == null) return -1;
				for(var i:int=0;i<arr1.length;i++){
				if(arr1[i][columnStr] == str) return i;
				}
			}
			return -1;
		}
		
		
		/**
		 *自动执行表单所有验证  (仅应用与项目管理模块,根据绝对坐标实现)
		 * 1.表单控件以绝对布局排列
		 * 2.验证内容: 非空验证 、数字验证
		 * 返回:true 无错,false 有错
		 **/
		public static function setValidator(doc:DisplayObjectContainer):Boolean 
		{
			var totalNum:int = doc.numChildren;
			
			for(var i:int=0;i<totalNum;i++){
				var o:Object = doc.getChildAt(i);
				if(setLabelEnableType(o))
				{
					if(o is DisplayObjectContainer)
					{
					//	setValidator(DisplayObjectContainer(o));
					}
				}else{
						var errMsg:String = "";
						var regp:RegExp = /\s/g;
					for(var j:int=0;j<totalNum;j++){
						var obj:Object = doc.getChildAt(j);
						if(obj.hasOwnProperty("visible") && obj.visible && obj.hasOwnProperty("x") && (obj.x < o.x + o.width + 150) && (obj.x > o.x + o.width - 25) && (obj.y < o.y + 10) && (obj.y > o.y -10)){
							var isNullCtn:Boolean = false;//非空验证
							if(o.required && obj.hasOwnProperty("text")){ if(obj.text == null || obj.text.length < 1){isNullCtn=true; errMsg ="不能为空!";  }}
							if(!isNullCtn){//验证单价数量 数字
								var yzStr:String = o.label.replace(regp,"");
						//		trace(o.label+" = "+yzStr);
							if(o.label.replace(regp,"").indexOf("金额") >-1 || o.label.replace(regp,"").indexOf("数量") > -1 || o.label.replace(regp,"").indexOf("单价") > -1 || o.label.replace(regp,"").indexOf("报价") > -1  ) {
								if(Number(obj.text)+"" == "NaN"){
								if(o.label.replace(regp,"").indexOf("报价") > -1 && obj.text.indexOf(";")>-1){
								}else{
									isNullCtn=true; errMsg ="必须填数字!"; 
								}
								}
							}	}
						if(isNullCtn){
							obj.setFocus();
							Alert.show("\""+o.label.replace(regp,"").replace(":","").replace("：","")+"\" " + errMsg , "信息提示框");
							return false;
						}
						break;
						}
					}
				}
			}
			return true;
		}
		
		private static function setLabelEnableType(o:Object):Boolean
		{
			var flag:Boolean = true;
			if(o is FormItem){//找到lablel 再去找对应的输入框 
			flag = false;
			}
			return flag;
		}
		
		
		//共用变量   "新购状态"
		public static const XGZT:String = "新购";
		
		/**
		 * 判断是否为 null "" "null"
		 * */
		public static function isNullOrWhitespace(str:String):Boolean{
			return ((str == null ) || (StringUtil.trim(str).length == 0) || (StringUtil.trim(str).toLowerCase()=="null") || str=="undefined");
		}
		
		/**
		 * 设置DateField值不能为空
		 * */
		public static function getDateFiedlText(str:String):String{
			return ((str == null ) ? "" : str);
		}
		
		public static var currentPageUrl:String="";
		/**
		 *判断是否为对应的dic
		 * 分别取 3 5 7位字符判断相等 
		 * str1为9位国标编码
		 **/
		public static function isEqualDicId(str1:String,strBasic:String,ncount:int):Boolean{
			if(str1 == null || strBasic == null){return false;}
			if(ncount > str1.length){ncount = str1.length;}
			var compareStr:String = str1.substr(0,ncount);
			var sourceArr:Array = strBasic.split("|");
			for each(var scstr:String in sourceArr){
				if(scstr == compareStr){
					currentPageUrl = strBasic;
					return true;
				}
			}
			return false;
		}
		
		/**
		 * 公用方法  设置 详情界面 所有组件可用状态
		*递归所有可视组件
		 **/ 
		public static function setEnable(doc:DisplayObjectContainer,isEnabled:Boolean=true,isEditable:Boolean=false):void 
		{
			var totalNum:int = doc.numChildren;
			
			for(var i:int=0;i<totalNum;i++){
				var o:Object = doc.getChildAt(i);
				if(setEnableType(o,isEnabled,isEditable))
				{
					if(o is DisplayObjectContainer)
					{
						setEnable(DisplayObjectContainer(o),isEnabled,isEditable);
					}
				}
			}
		}
		//设置界面组件 enabled editable属性
		public static function setEnableType(o:Object,isEnabled:Boolean=true,isEditable:Boolean=false):Boolean
		{
			var flag:Boolean = true;
			if(o.hasOwnProperty("visible") && !o.visible)return false;
			if(o is Button && o.label != "取消" && o.label != "关闭"&& o.label !="查询"&& o.label !="搜索" && o.label !="高级"&& o.label !="隐藏"&& o.label !="..."){o.visible = isEditable;o.includeInLayout=isEditable; flag=false; }
			else if(o is Button &&(o.label =="搜索" || o.label =="查询")){o.enabled=isEnabled; flag=false; }
			else if(o is TextInput){ o.enabled=isEnabled; o.editable=isEditable; flag=false; }
			else if(o is NumericStepper){o.enabled=isEnabled;flag=false; }
			else if(o is TextArea){o.enabled=isEnabled; o.editable=isEditable; flag=false; }
			else if(o is DateField){o.enabled=isEnabled; o.editable=isEditable;flag=false; 	}
			else if(o is ComboBox){o.enabled=isEnabled; flag=false; }
			//	else{o.enabled=true;};
			return flag;
		}
		
		/**
		*公用方法  重置 详情界面 所有组件
		*递归所有可视组件
		**/ 
		public static function resetComponent(doc:DisplayObjectContainer):void 
		{
			var totalNum:int = doc.numChildren;
			
			for(var i:int=0;i<totalNum;i++){
				var o:Object = doc.getChildAt(i);
				if(executeValidate(o))
				{
					if(o is DisplayObjectContainer)
					{
						resetComponent(DisplayObjectContainer(o));
					}
				}
			}
		}
		
		//清空组件内容
		public static function executeValidate(o:Object):Boolean
		{
			var flag:Boolean = true;
			if(o is TextInput){if(TextInput(o).editable && TextInput(o).enabled) TextInput(o).text="";  flag=false; }
			else if(o is NumericStepper){if(NumericStepper(o).enabled)NumericStepper(o).value = 0;flag=false; }
			else if(o is TextArea){if(TextArea(o).editable && TextArea(o).enabled)TextArea(o).text=""; flag=false; }
			else if(o is DateField){if(DateField(o).enabled)DateField(o).text="";  flag=false; }
			else if(o is ComboBox){
				if(ComboBox(o).id != null &&( ComboBox(o).id.toLowerCase() == "xh")){
					if(ComboBox(o).enabled)
						ComboBox(o).dataProvider=""; 
				}else{
					ComboBox(o).selectedIndex=0;
				}
				flag=false; }
			//	else{o.enabled=true;};
			return flag;
		}
		/**
		 * 屏蔽 combox 特定item
		 **/
		public static function resetItems(o:ComboBox):void
		{
			var item:ArrayCollection=o.dataProvider as ArrayCollection;
			for(var i:int=0;i<item.length;i++)
			{
				if(item.getItemAt(i).name=="synx" || item.getItemAt(i).name=="gzje" || item.getItemAt(i).name=="bxq" || item.getItemAt(i).name=="zt")
				{
					item.removeItemAt(i);
					item.refresh();
					i--;
				}
				
			}
			o.dataProvider=item;
		}
		/**
		 *转换DataGrideView Date数据列 为"YYYY-MM-DD"格式 
		 **/
		public static function convertDateColumnToString(item:Object, column:DataGridColumn):String
		{
			if(item[column.dataField] is Date)
				return  DateField.dateToString(item[column.dataField],"YYYY-MM-DD");
			else
				return item[column.dataField];
		}
		/**
		 *生成流程中的编号
		 * 格式: head-20150101-四位随机数 
		 **/
		public static function genernateRandomWFBH( head:String):String{
			var dateStr:String = commonDateFormatter.format(new Date());
			return head+"-"+dateStr+"-"+Math.random().toString().substring(2,6);
		}
		/**
		 *生成流程中的编号
		 * 加1 
		 **/
		public static function genernateIncreaseWFBH( currentBh:String):String{
			var bhPrefix:String = currentBh.substr(0,currentBh.lastIndexOf("-")+1);
			var bhSuffix:String =  currentBh.substr(currentBh.lastIndexOf("-")+1);
			var countInt:int = int(bhSuffix) + 1;
			var endStr:String = countInt.toString();
			for(var i:int=countInt.toString().length;i<4;i++){
				endStr = "0" + endStr;
			}	
			
			return  bhPrefix + endStr;
		}
		
		/**
		 *验证WFBH编号是否重复;
		 * pwd:Object,父窗口;
		 * callBackFunc:Function,验证结果回调方法,其参数必须为Boolean型;
		 * args:Array,回调方法的参数;
		 * true 重复 ;
		 **/
		public static function validateWFBH(wfbh:String,idColumn:String,bhColumn:String,tableName:String,dataId:String,parentPage:Object,callBackFunc:Function,args:Array=null):void{
			var docRem:RemoteObject = new RemoteObject();
			docRem.destination = "allAssetService";
			docRem.endpoint = "/mxAsset/messagebroker/amf";
			docRem.showBusyCursor = true;
			docRem.validateWFBH(wfbh,idColumn,bhColumn,tableName,dataId);
			docRem.addEventListener(ResultEvent.RESULT,dovalidateWFBH(parentPage,callBackFunc,args));
		}
		private static function dovalidateWFBH(parentPage:Object,callBackFunc:Function,args:Array=null):Function{
			return function(event:ResultEvent){
				validateWFBHBack(event,parentPage,callBackFunc,args);
			}
		}
		
		private static function validateWFBHBack(event:ResultEvent,parentPage:Object,callBackFunc:Function,args:Array=null):void{
			if(parentPage == null || callBackFunc == undefined || callBackFunc==null){return;}
			callBackFunc.call(parentPage,event.result);
		}
		/**
		 *下载Excel的方法 
		 **/
		public static function downloadExceTemplate(headerTexts:Array,fileName:String):void{
			var variables:URLVariables = new URLVariables();
			variables.headerTexts = headerTexts;
		//	variables.formTitle = formTitle;
			variables.fileName = fileName;
			
			var u:URLRequest = new URLRequest("/mxAsset/servlet/ExpExcelTemplateServlet");
			u.data = variables; 
			u.method = URLRequestMethod.POST; 
			navigateToURL(u,"_self");
		}
		/**
		 *计算总金额 
		 **/
		public static function calculateZJE(zjeInput:Object,dataArray:ArrayCollection,jeColumn:String):void{
			if(zjeInput != null && jeColumn != null && dataArray != null){
				if(dataArray != null){
					var allJe:Number=0;//更新报废单总金额
					for(var i:int=0;i<dataArray.length;i++){
						var curCh:Object = dataArray.getItemAt(i);
						if((curCh[jeColumn] as Number) > 0){
							allJe += Number(curCh[jeColumn]);
						}
					}
					allJe = Number(allJe.toFixed(3));
					zjeInput.text = allJe + "";
				}
			}
		}
		
		
		/**
		 *存储公共页面  加快二次加载速度 
		 **/
		public static var pageCacheObj:Object = new Object();
		
		
		
		
		
		//领用单提交后改变物品库存状态  --暂未使用
		public static function setAssetKcxxSYZTSH(UID:String){
			setAssetKcxxSYZT(UID,"审核中");
		}
		private static function setAssetKcxxSYZT(UID:String,syztStr:String):void{
			var docRem:RemoteObject = new RemoteObject();
			docRem.destination = "allAssetService";
			docRem.endpoint = "/mxAsset/messagebroker/amf";
			docRem.showBusyCursor = true;
			var sqlStr:String = "UPDATE CNST_RYHCMX_DATA SET SYZT='"+syztStr+"' WHERE RYHCMXID='"+UID+"'";
			docRem.updateObjectBySql(sqlStr);
		} 
		
		/**
		 * 获取DICID的信息 填充数据
		 **/
		public static function getDICDataById(dicid:String,dicnameInput:TextInput,sslxInput:TextInput,gbdmInput:TextInput,wpmcInput:TextInput,wpbhInput:TextInput,tableName:String):void{
			if(dicid == null || dicnameInput==null || sslxInput == null || gbdmInput == null){ return; }
			var sqlStr:String = "SELECT * FROM CNST_DICTIONARY_DATA  WHERE  DICID='"+dicid+"' ";
			var docRem:RemoteObject = new RemoteObject();
			docRem.destination = "commonService";
			docRem.endpoint = "/mxAsset/messagebroker/amf";
			docRem.showBusyCursor = true;
			docRem.RunSelectClassBySql(sqlStr,"net.chinanets.entity.CnstDictionaryData");
			docRem.addEventListener(ResultEvent.RESULT,dogetDICData(dicnameInput,sslxInput,gbdmInput,wpmcInput,wpbhInput,tableName));
		}
		private static function dogetDICData(dicnameInput:TextInput,sslxInput:TextInput,gbdmInput:TextInput,wpmcInput:TextInput,wpbhInput:TextInput,tableName:String):Function{
			return function(event:ResultEvent){
				getDICDataBack(event,dicnameInput,sslxInput,gbdmInput,wpmcInput,wpbhInput,tableName);
			}
		}
		//加载相关信息回调方法
		private static function getDICDataBack(rmtResult:ResultEvent,dicnameInput:TextInput,sslxInput:TextInput,gbdmInput:TextInput,wpmcInput:TextInput,wpbhInput:TextInput,tableName:String):void{
			var objResult:ArrayCollection = rmtResult.result as ArrayCollection;
			if(objResult == null || objResult.length < 1){return;}
			var jsonObj:Object= objResult.getItemAt(0);
			dicnameInput.text = jsonObj.dicname;
			wpmcInput.text = jsonObj.dicname;
			sslxInput.text = jsonObj.rootname;
			gbdmInput.text = jsonObj.dicid;
			CommonMethod.getMaxNewDataBH(tableName,"WPBH",jsonObj.dicid,wpbhInput);
		}
		
		
		/**
		 * 获取最大编号 
		 **/
		public static function getMaxNewDataBH(tableName:String,columnName:String,preStr:String,bhInput:TextInput):void{
			if(preStr.charAt(preStr.length-1) != "-"){ preStr = preStr + "-";}
			var sqlStr:String = "SELECT MAX("+columnName+") FROM "+tableName+" WHERE "+columnName+" LIKE '"+preStr+"%' AND ISNUMERIC(SUBSTR("+columnName+",LENGTH('"+preStr+"')))=1";
			var docRem:RemoteObject = new RemoteObject();
			docRem.destination = "commonService";
			docRem.endpoint = "/mxAsset/messagebroker/amf";
			docRem.showBusyCursor = true;
			docRem.getObjectBySql(sqlStr);
			docRem.addEventListener(ResultEvent.RESULT,dogetMaxBH(preStr,bhInput));
		}
		private static function dogetMaxBH(preStr:String,bhInput:TextInput):Function{
			return function(event:ResultEvent){
				getMaxBHBack(event,preStr,bhInput);
			}
		}
		//加载相关信息回调方法
		private static function getMaxBHBack(rmtResult:ResultEvent,preStr:String,bhInput:TextInput):void{
			var resultArr:ArrayCollection = rmtResult.result as ArrayCollection;
			if(resultArr==null || resultArr.length < 1 || resultArr.getItemAt(0)==null){
				var rkdbh:String = preStr;
				if(rkdbh.lastIndexOf("-") == (rkdbh.length-1)){
					bhInput.text = rkdbh + "0001"; 
				}else{
					bhInput.text = rkdbh + "-0001"; 
				}
			}else{
				var rstStr:String = resultArr.getItemAt(0).toString();
				
				var countInt:int = int( rstStr.substring(rstStr.lastIndexOf("-")+1)) + 1;
				var endStr:String = countInt.toString();
				for(var i:int=countInt.toString().length;i<4;i++){
					endStr = "0" + endStr;
				}	
				
				rstStr = rstStr.substring(0,rstStr.lastIndexOf("-")+1) + endStr;
				bhInput.text = rstStr;
			}
		}
		
		
		///用户权限的方法
		
		/**
		 *解析用户权限XML 
		 **/
		
		public static function getConvertedXml(xml:XMLList):XML {
			var newXml:XML = new XML("<root label='勾选的功能菜单' id='0' isRule='0'></root>");
			var newNum1:int = 0;
			var newNum2:int = 0;
			var newNum3:int = 0;
			var newNode:XML;
			for(var i:int = 0; i < xml.node.length(); i++) {
				if(xml.node[i].@isSelect != "0") {
					newNum1++;
					newNum2 = 0;
					var secondNode:XMLList = xml.node[i].node;
					//			if(secondNode.length() > 0) {
					newNode = new XML(<node></node>);
					newNode.@id = xml.node[i].@id;
					newNode.@label = xml.node[i].@label;
					newXml.appendChild(newNode);
					//			}else {
					//				newXml.appendChild("<node id='" + xml.node[i].@id + "' label='" 
					//					+ xml.node[i].@label + "' sfAdd='" + xml.node[i].@sfAdd + "' sfModify='" 
					//					+ xml.node[i].@sfModify + "' sfDelete='" + xml.node[i].@sfDelete 
					//					+ "' sfExport='" + xml.node[i].@sfExport + "' sfImport='" + xml.node[i].@sfImport 
					//					+ "' ></node>");
					//			}
					
					for(var j:int = 0; j < secondNode.length(); j++) {
						if(secondNode[j].@isSelect != "0") {
							newNum2++;
							newNum3 = 0;
							var thirdNode:XMLList = secondNode[j].node;
							if(thirdNode.length() > 0) {
								newNode = new XML(<node></node>);
								newNode.@id = secondNode[j].@id;
								newNode.@label = secondNode[j].@label;
								newXml.node[newNum1-1].appendChild(newNode);
							}else {
								newNode = new XML(<node></node>);
								newNode.@id = secondNode[j].@id;
								newNode.@label = secondNode[j].@label;
								newNode.@sfAdd = secondNode[j].@sfAdd;
								newNode.@sfModify = secondNode[j].@sfModify;
								newNode.@sfDelete = secondNode[j].@sfDelete;
								newNode.@sfExport = secondNode[j].@sfExport;
								newNode.@sfImport = secondNode[j].@sfImport;
								newNode.@sfSearch = secondNode[j].@sfSearch;
								newNode.@isRule = secondNode[j].@isRule;
								newNode.@searchCondition = secondNode[j].@searchCondition;
								newNode.@sfOther = secondNode[j].@sfOther;
								newXml.node[newNum1-1].appendChild(newNode);
							}
							for(var k:int = 0; k < thirdNode.length(); k++) {
								if(thirdNode[k].@isSelect != "0") {
									newNum3++;
									var fourthNode:XMLList = thirdNode[k].node;
									if(fourthNode.length() > 0) {
										newNode = new XML(<node></node>);
										newNode.@id = thirdNode[k].@id;
										newNode.@label = thirdNode[k].@label;
										newXml.node[newNum1-1].node[newNum2-1].appendChild(newNode);
									}else{
									newNode = new XML(<node></node>);
									newNode.@id = thirdNode[k].@id;
									newNode.@label = thirdNode[k].@label;
									newNode.@sfAdd = thirdNode[k].@sfAdd;
									newNode.@sfModify = thirdNode[k].@sfModify;
									newNode.@sfDelete = thirdNode[k].@sfDelete;
									newNode.@sfExport = thirdNode[k].@sfExport;
									newNode.@sfSearch = thirdNode[k].@sfSearch;
									newNode.@sfImport = thirdNode[k].@sfImport;
									newNode.@isRule = thirdNode[k].@isRule;
									newNode.@searchCondition = thirdNode[k].@searchCondition;
									newNode.@sfOther = thirdNode[k].@sfOther;
									newXml.node[newNum1-1].node[newNum2-1].appendChild(newNode);
									}
									for(var m:int = 0; m < fourthNode.length(); m++) {
										if(fourthNode[m].@isSelect != "0") {
											newNode = new XML(<node></node>);
											newNode.@id = fourthNode[m].@id;
											newNode.@label = fourthNode[m].@label;
											newNode.@sfAdd = fourthNode[m].@sfAdd;
											newNode.@sfModify = fourthNode[m].@sfModify;
											newNode.@sfDelete = fourthNode[m].@sfDelete;
											newNode.@sfExport = fourthNode[m].@sfExport;
											newNode.@sfSearch = fourthNode[m].@sfSearch;
											newNode.@sfImport = fourthNode[m].@sfImport;
											newNode.@isRule = fourthNode[m].@isRule;
											newNode.@searchCondition = fourthNode[m].@searchCondition;
											newNode.@sfOther = fourthNode[m].@sfOther;
											newXml.node[newNum1-1].node[newNum2-1].node[newNum3-1].appendChild(newNode);
										} 
								}
							}
						}
					}
				}
			}
			}
			return newXml;
		}
		/**
		 * 权限控制step1
		 *权限验证 主方法 
		 * pwd 验证权限的页面
		 * selectItem 与该页面相关的信息的xml
		 **/
		public static function executePwdAuthority(pwd:Object,selectItem:XML=null):Object{
			if(Application.application.userVo.ruleId == "41"){return 41;}//超级管理员不做任何验证	
			var anthObj= getMenuAuthority(selectItem.@id,pwd);
			
			if(anthObj == null){Alert.show("您没有操作当前菜单的权限!",CommonXMLData.Alert_Title); return null;}
			if(!pwd.hasOwnProperty("deptSearchCondtion")){Alert.show(" 当前页面未引入权限initAllMenuRule.as !");return null;}
			pwd.deptSearchCondtion = anthObj.searchCondition;
			pwd.setBmCondition();
			pwd.addEventListener(FlexEvent.INITIALIZE,doAutority(selectItem));
			return 1;
		}
		
		private static function doAutority(selectItem:XML):Function{
			return function(event:Event){
				CommonMethod.executeAuthority(event,selectItem);
			}
		}
		/**
		 *对页面执行权限验证 
		 **/
		public static function executeAuthority(event:Event,selectItem:Object):void{
			var currentPwd:Object = event.target;
			var anthObj:Object = getMenuAuthority(selectItem.@id,currentPwd);
			if(anthObj == null){return;}
			//newAction  editAction removeAction refeshAction  exportAction otherAction
			if(currentPwd.hasOwnProperty("newAction")){ currentPwd["newAction"].visible = (anthObj.sfAdd=="是");currentPwd["newAction"].includeInLayout = (anthObj.sfAdd=="是");}
			if(currentPwd.hasOwnProperty("removeAction")){ currentPwd["removeAction"].visible = (anthObj.sfDelete=="是");currentPwd["removeAction"].includeInLayout = (anthObj.sfDelete=="是");}
			if(currentPwd.hasOwnProperty("editAction")){ currentPwd["editAction"].visible = (anthObj.sfModify=="是");currentPwd["editAction"].includeInLayout = (anthObj.sfModify=="是");}
			if(currentPwd.hasOwnProperty("exportAction")){ currentPwd["exportAction"].visible = (anthObj.sfExport=="是");currentPwd["exportAction"].includeInLayout = (anthObj.sfExport=="是");}
			if(currentPwd.hasOwnProperty("otherAction")){ currentPwd["otherAction"].visible =false;/* (anthObj.sfOther=="是");*/currentPwd["otherAction"].includeInLayout =false;/* (anthObj.sfOther=="是");*/	}		
			
			//流程页面控制权限
			if(currentPwd.hasOwnProperty("isAddUsable")){ currentPwd["isAddUsable"] =  (anthObj.sfAdd=="是");}
			if(currentPwd.hasOwnProperty("isDelUsable")){ currentPwd["isDelUsable"] = (anthObj.sfDelete=="是");}
			if(currentPwd.hasOwnProperty("isEditUsable")){ currentPwd["isEditUsable"] = (anthObj.sfModify=="是");}
			if(currentPwd.hasOwnProperty("isExportUsable")){ currentPwd["isExportUsable"] = (anthObj.sfExport=="是");}
			if(currentPwd.hasOwnProperty("isOtherUsable")){ currentPwd["isOtherUsable"] = false; /*(anthObj.sfOther=="是");*/}
		}
		private static function executeAuthorityEnable(lkbCh:Object){
			var currentMenuAuthorityXML:XML;
			if(lkbCh is Button || lkbCh is LinkButton){
				currentMenuAuthorityXML = Application.application.userAutorityXML;
			}
				var kindFunc:String = "add";
				switch(kindFunc){
					case "add":
						break;
				}
		}
		/**
		 * 获取当前菜单的权限Object
		 * pageId selectItem.@id
		 * pwd     执行验证的页面
		 **/
		public static function getMenuAuthority(pageId:String,pwd:Object=null):Object{
		//	if(pageId == "102" || pageId == "103" || pageId == "104" || pageId == "105"){return getFullAuthorityObject();}
			if(pwd != null){
				if((pwd.hasOwnProperty("tempObjBeanName") && pwd["tempObjBeanName"].indexOf("DIC_") > -1 ) 
					|| (pwd.hasOwnProperty("tempObjViewName") && pwd["tempObjViewName"] == "CNSV_RYHC")){
					//	anthObj = getMenuAuthority("201");		if(anthObj == null){anthObj = getMenuAuthority("202");}
					return getNullAuthorityObject();
				}
			}
			var xmlList:XMLList = Application.application.userAutorityXML.node.(@id==pageId);	
			if(xmlList.length() < 1){return null;	}
			
			var objAuthor:Object = new Object();			
			objAuthor.sfAdd = xmlList.@sfAdd +"";
			objAuthor.sfDelete = xmlList.@sfDelete  +"";
			objAuthor.sfSearch = xmlList.@sfSearch  +"";
			objAuthor.sfModify = xmlList.@sfModify  +"";
			objAuthor.sfExport = xmlList.@sfExport  +"";
			objAuthor.sfImport = xmlList.@sfImport  +"";
			objAuthor.searchCondition = xmlList.@searchCondition  +"";
			objAuthor.sfOther = xmlList.@sfOther  +"";
			
			return objAuthor;
		}
		private static function getFullAuthorityObject():Object{
			var objAuthor:Object = new Object();			
			objAuthor.sfAdd = "是";
			objAuthor.sfDelete = "是";
			objAuthor.sfSearch = "是";
			objAuthor.sfModify = "是";
			objAuthor.sfExport = "是";
			objAuthor.sfImport = "是";
			objAuthor.searchCondition = "all";
			objAuthor.sfOther = "是";
			
			return objAuthor;
		}
		private static function getNullAuthorityObject():Object{
			var objAuthor:Object = new Object();			
			objAuthor.sfAdd = "否";
			objAuthor.sfDelete = "否";
			objAuthor.sfSearch = "否";
			objAuthor.sfModify = "否";
			objAuthor.sfExport = "是";
			objAuthor.sfImport = "否";
			objAuthor.searchCondition = "all";
			objAuthor.sfOther = "否";
			
			return objAuthor;
		}
		/**
		 *获取this.data中的selectItem XML中的dicids 
		 **/
		public static function getDicidsOfPageData(dataObj:Object):String{
			//顶级菜单 不加DICID查询条件
			if((dataObj as XML) != null){
				var dataid:String = dataObj.@id;
				if(dataid.indexOf("00000000") > -1){//顶级菜单
					return dataObj.@dicids;
				}
				return dataid;
			}
			return null;
		}
		/**
		 *获取this.data中的selectItem XML中的id 
		 **/
		public static function getDicidOfPageData(dataObj:Object):String{
			//顶级菜单 不加DICID查询条件
			//dataObj.@isSearchXml != "true" 搜索结果列表要加DICID查询条件
			if(dataObj.@isSearchXml != "true" && ((dataObj as XML).parent() == undefined || (dataObj as XML).parent().parent() == undefined)) return null;
			if((dataObj as XML) != null){
				return dataObj.@id;;
			}
			return null;
		}
		
		/**
		 *获取 DICID Prefix
		 **/
		public static function getDICIDPrefix(dicid:String):String{
			if(dicid != null){
				while(dicid.charAt(dicid.length-1) == "0"){
					dicid = dicid.substr(0,dicid.length-1);
				}
				return dicid;
			}
			return "";
		}
		
		public static function resetUserMenu(currentXML:Object):XML{
			if(Application.application.userVo.ruleId == "41"){ return currentXML as XML;}
			var curXML:XML = currentXML as XML;
			convertXMLString = (currentXML as XML).toXMLString();
			for(var i:int=0;i<4;i++){
			convertXML(new XML(convertXMLString));
			convertXMLString = new XML(convertXMLString).toXMLString();
			trace(convertXMLString + " 1");
			}
			return new XML(convertXMLString);
		//	return convertXML(currentXML);
		}
		
		private static var convertXMLString:String;
		private static function convertXML(currentXML:Object):void{
			
			if(currentXML.hasComplexContent()){
				if(currentXML is XML){
					for(var i:int=0;i<(currentXML as XML).children().length();i++){
						if((currentXML as XML).children()[i].hasComplexContent()){
							convertXML((currentXML as XML).children()[i]);
						}else{
							executeConvert((currentXML as XML).children()[i]);
						}
					}
				}else if(currentXML is XMLList){
					for(var i:int=0;i<currentXML.length();i++){
						if(currentXML[i].hasComplexContent()){
							convertXML(currentXML[i]);
						}else{
							executeConvert(currentXML[i]);
						}
					}
				}
				}else{
					 executeConvert(currentXML);
				}
			
			}
		
		private static function executeConvert(ctXML:Object):void{
			trace("ctXML is XML :"+ (ctXML is XML));
			trace("ctXML is XMLList :"+ (ctXML is XMLList));
			trace(ctXML.toXMLString());
			trace(convertXMLString.indexOf(ctXML.toXMLString()));
			if(ctXML is XML){
				if(((ctXML as XML).parent()!=undefined && (ctXML as XML).parent().parent()!=undefined && (ctXML as XML).parent().parent().@label =="信息化设备")){
					return;
				}
				if(getMenuAuthority(ctXML.@id) == null ){
					convertXMLString = convertXMLString.substr(0,convertXMLString.indexOf(ctXML.toXMLString())) 
						+ convertXMLString.substr(convertXMLString.indexOf(ctXML.toXMLString())+ctXML.toXMLString().length,convertXMLString.length);
				//	(ctXML as XML).parent() = 
				//	convertXMLString.replace((ctXML as XML).toXMLString(),"");
				//	(ctXML as XML).parent() =	deleteByXMLString((ctXML as XML).parent(),(ctXML as XML).toXMLString());
				}
			}else{
				Alert.show("解析XML递归方法错误!--convertXML ");
			}
			
		}
		
		/**
		 *删除节点  用节点的toXMLString()的值查找
		 **/
		public static function deleteByXMLString(xmlNode:XML,xmlStr:String):XML{ 
			trace("xmlStr :"+xmlStr);
			trace("before del :"+xmlNode.toXMLString());
			var newStr:String=""; 
			newStr+="<"+xmlNode.localName(); 
			for each (var att:XML in xmlNode.attributes()){ 
				newStr+=" "+att.localName()+"='"+att.toString()+"'"; 
			} 
			newStr+=">"; 
			var i:Number=0; 
			for each(var node:XML in xmlNode.children()){
				if(node.toXMLString() != xmlStr) 
					newStr+=node.toXMLString(); 
			}
			newStr+="</"+xmlNode.localName()+">"; 
			trace("after del :"+newStr);
			return new XML(newStr); 
		}
		
		/**
		 *删除节点  用节点的index查找
		 **/
		public static function deleteByIndex(xmlNode:XML,index:Number):XML{ 
			var newStr:String=""; 
			newStr+="<"+xmlNode.localName(); 
			for each (var att:XML in xmlNode.attributes()){ 
				newStr+=" "+att.localName()+"='"+att.toString()+"'"; 
			} 
			newStr+=">"; 
			var i:Number=0; 
			for each(var node:XML in xmlNode.children()){
				if(i!=index) 
					newStr+=node.toXMLString(); 
				i++; 
			}
			newStr+="</"+xmlNode.localName()+">"; 
			return new XML(newStr); 
		}
		
		/**
		 * 
		 **/
		public static function setMenuXML(pwd:Object,treeData:XML,callBackFunc:Function){
			callBackFunc.call(treeData); 
		}
	
	 public static var commonLongDateFormatter:DateFormatter = new DateFormatter();
	 public static var commonDateFormatter:DateFormatter = new DateFormatter();
	 commonLongDateFormatter.formatString = "YYYY-MM-DD";
	 commonDateFormatter.formatString = "YYYYMMDD";
		
	}
}