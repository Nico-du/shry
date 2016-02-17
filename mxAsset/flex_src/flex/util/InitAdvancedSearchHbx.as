
import asset_Computer.SelectAsset;
//import asset_Computer.SelectColumn;

import flash.display.DisplayObjectContainer;

import flex.util.CommonMethod;
import flex.util.CommonXMLData;

import mx.containers.Tile;
import mx.containers.VBox;
import mx.controls.Button;
import mx.controls.ComboBox;
import mx.controls.DataGrid;
import mx.controls.DateField;
import mx.controls.NumericStepper;
import mx.controls.TextArea;
import mx.controls.TextInput;
import mx.formatters.DateFormatter;
import mx.managers.PopUpManager;
import mx.rpc.http.HTTPService;
import mx.rpc.http.mxml.HTTPService;
import mx.utils.StringUtil;

import query.ConObject;

public function initNewArr(){
	
	var service:mx.rpc.http.HTTPService = new mx.rpc.http.HTTPService();
	service.url= dataXmlPath;
	service.resultFormat="object";
	service.addEventListener(ResultEvent.RESULT,initNewArrBack);
	service.send();
}

private function initNewArrBack(evt:ResultEvent):void
{
	var rstArc:ArrayCollection = evt.result.root.paremt as ArrayCollection;
	if(rstArc == null) return;
	var itemType:String = "text";
	for(var i:int=0;i<searchCh.length;i++){
		var idx:int = int(searchCh[i]);
		if(idx >= rstArc.length) break;
		var obj:Object = rstArc[idx];
		if(obj.name == "gzrq" || obj.name == "qysj" || obj.name == "rkrq" || obj.name == "bxq")itemType = "date"; else itemType = "text";
		newArr.addItem(new ConObject(obj.name,itemType,obj.column));
	} 
	initAdvancedQuery(inputTile,dateTile);
}



private var conArr:ArrayCollection = new ArrayCollection();   //查询条件 控件 集合
private var newArr:ArrayCollection = new ArrayCollection();;  //已选中条件

private var dateSecond:DateField = null;//第二个日期控件，用来从高级页面中移除多余的日期控件
private var dateFourth:DateField = null;//第四个日期控件
private var conditionStr:String = "";
//		private var asset:Object = new ComputerVo();  //分页查询  条件obj
public function initAdvancedQuery(inputTile:Tile,dateTile:Tile):void{
	var input:TextInput;
	var date:DateField;
	var date2:DateField;
	var combo:ComboBox;
	var label:Label;
	var obj:Object;
	var hbox:HBox =null;
	var dateId:int = 0;//用来表示date的id属性
	if(newArr!=null) {
		var length:int = newArr.length;
		var n:int  = 0;
		
		for(var i:int=0;i<length;i++) {
			hbox=new HBox();
			obj = newArr[i];
			//trace(obj.type);
			label = new Label();
			label.text = obj.column+":";
			label.percentWidth = 100;
			//						label.width = 80;
			//		hbox.width = 220;
			//		hbox.setStyle("borderStyle","insert");
			hbox.addChild(label);
			
			if(obj.type=="text") {
				n++;
				//		if(!this.canSetSelection(obj.column,hbox,obj)){
				if(true){
					input = new TextInput();
					input.initialize();
					input.name = obj.name;
					input.data = new ConObject(obj.name,"text",obj.column);
					input.width = 110;
					input.height = 20;
					hbox.addChild(input);
					conArr.addItem(input);
				}
			} else if(obj.type=="date") {
				n = n + 2;
				date = new DateField();
				date.initialize();
				date.id = "dateStart" + obj.name; //标记 时间开始
				date.name = obj.name;
				date.data = new ConObject(obj.name,"date",obj.column);
				date.formatString = "YYYY-MM-DD";
				date.dayNames = ["日","一","二","三","四","五","六"];
				date.monthNames = ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"];
				date.yearNavigationEnabled = true;
				date.editable = false;
				date.width = 90;
				date.height = 20;
				
				date2 = new DateField();
				date2.initialize();
				date2.id = "dateEnd" + obj.name; //标记 时间结束
				date2.name = obj.name;
				date2.data = new ConObject(obj.name,"date",obj.column);
				date2.formatString = "YYYY-MM-DD";//2002
				date2.dayNames = ["日","一","二","三","四","五","六"];
				date2.monthNames = ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"];
				date2.yearNavigationEnabled = true;
				date2.editable = false;
				date2.width = 90;
				date2.height = 20;
				if(dateSecond == null) {
					dateSecond = date2;
				}else {
					dateFourth = date2;
				}
				
				//date.disabledDays=[0,1,2,3,4,5,6];
				var fromLabel:Label = new Label();
				var toLabel:Label = new Label();
				fromLabel.text = "从";
				toLabel.text = "到";
				
				hbox.addChild(fromLabel);
				hbox.addChild(date);
				hbox.addChild(toLabel);
				hbox.addChild(date2);//在查询页面中显示该控件
				conArr.addItem(date);
				conArr.addItem(date2);
				//							conArr.addItem(date2);//在高级查询窗口中显示该控件
				dateTile.addChild(hbox);
				hbox=new HBox();
				continue;
			} else {
				n++;
				input = new TextInput();
				input.initialize();
				input.name = obj.name;
				input.data = new ConObject(obj.name,"number",obj.column);
				input.width = 110; 
				input.height =20;
				hbox.addChild(input);
				conArr.addItem(input);
			}
			inputTile.addChild(hbox);
			//						if((n+1)%4==0){hbox=new HBox();hbox.width=800;}
			//	if(n/4>=1){hbox=new HBox();hbox.width=fbox.width; n = 0;}
		}
	}
}

//执行高级查询
private function submitQuery(asset:Object):void {
	var formatter:DateFormatter = new DateFormatter();
	formatter.formatString = "YYYY-MM-DD";
	var length:int = conArr.length;
	var obj:Object;
	var input:TextInput;
	var date:DateField;
	var type:String;
	var temp:String;
	var name:String;
	conditionStr=" 1=1";
	var date1:String = "null";
	var date2:String = "null";
	var isDataEnd:Boolean = false; //判定为结束日期
	for(var i:int=0;i<length;i++) {
		obj = conArr[i];
		temp = StringUtil.trim(obj.text);
		name = obj.data.name;
		
		if((obj is TextInput) || (obj is ComboBox)) {
			type=obj.data.type;
			if((obj is ComboBox) && temp=="全部"){temp = "";}
			if(type == "number") {
				asset[obj.data.name] = (temp.length > 0 )? (temp) : null;
				//							conditionStr += temp==""?"":" and "+name+" = "+temp;
			} else {
				try {
					asset[obj.data.name] =  (temp.length > 0 )? ("%"+temp+"%") : null;
				}catch (e:Error) {
					trace("error message :"+e.message);
					trace("error :"+e.name);
					trace("error :"+e.toString());
					/* asset["bmmc"] = (temp.length > 0 )?("%"+temp+"%") : null;
					*/
					Alert.show("该设备类型字段映射的xml文件有误!"+e.message); 
				}
				//	conditionStr += temp==""?"":" and "+name+" like "+"'%"+obj.text+"%'";
			}
		} else if((obj is DateField) && (temp.length > 0 )) {
			/* date.id = "dateStart" + obj.name; //标记 时间开始
			date2.id = "dateEnd" + obj.name; //标记 时间结束
			*/
			
			if(DateField(obj).id == ("dateStart"+name)) date1 = temp;
			if(DateField(obj).id == ("dateEnd"+name)){ date2 = temp; isDataEnd = true; }
			var str:String;
			if(name == "qysj") str = "启用日期";
			if(name == "gzrq") str = "购置日期";
			if(name == "bxq" || name == "bxrq") str =  "保修期";
			if(name == "rkrq") str = "入库日期";
			
			if(date1 == "" && date2 != "null" && date2 != "") {
				Alert.show("请输入" + str + "的开始日期", "提示");
				return;
			}else if(date1 != "" && date2 == "") {
				date2 = formatter.format(new Date());
				obj.text = date2;
				temp = obj.text;
			}else if(date1 > date2) {
				Alert.show(str + "的开始时间必须小于结束时间。", "提示");
				return;
			}
			if(date1 != "" && date1 != "null" && !isDataEnd){
				
			}
			if(date2 != "" && date2 != "null" && isDataEnd){
				
			}
			if(isDataEnd){
				if((date1 != "" && date1 != "null") && (date2 != "" && date2 != "null")) {
					conditionStr += "  and "+name+" between '"+date1 + "' and '" + date2 + "' ";
				}
				if((date1 == "" || date1 == "null") && (date2 != "" && date2 != "null")) {
					conditionStr += " and "+name+" < '"+date2 + "'";
				}
				if((date1 != "" && date1 != "null") && (date2 == "" || date2 == "null")) {
					conditionStr += " and "+name+" > '"+date1 + "'";
				}
				date1 = date2 = "null";
				isDataEnd = false;
			}
		}
	}
	
	pagination.dg=dataGrid;
		
	pagination.obj=asset;
	pagination.condition = conditionStr+" order by sbbh";
	pagination.getPage(pagination.pageSize,pagination.currentPage);
	
	//	pagination.initDividePage(dg, asset, this.conStrZl+ conditionStr);
	//	querySuccess = true;
}

private function resetQuery():void {
	conditionStr="";
	var length:int = conArr.length;
	for(var i:int=0;i<length;i++) {
		if((conArr[i] is ComboBox) && (ComboBox(conArr[i]).selectedIndex != -1)){
			ComboBox(conArr[i]).selectedIndex = 0;
			if(ComboBox(conArr[i]).data.name == "xh"){ ComboBox(conArr[i]).dataProvider = "";}
		}else{
			conArr[i].text = "";
		}
	}
}
//执行其他功能
private function doOtherFunc(funcName:String):void{
	switch(funcName){
		case "下载模版":if(mbExcelPath != "")pagination.downloadExcel(mbExcelPath); break;
		case "导入数据": inData();break;
		default: break;
	}
}

//选择显示列
private function selectColumn():void
{
/*	var sel:SelectColumn = new SelectColumn();
	sel.url= dataXmlPath;
	sel.dataGrid=this.dataGrid;
	PopUpManager.addPopUp(sel,this.parentApplication as DisplayObject,true);
	PopUpManager.centerPopUp(sel);*/
}
//添加设备类型combobox
private function assetSelectFunc(item:Object):String{
	return item.@label +"列表";
}
//切换页面时重置combobox selectedIndex
private function assetSelectResetFunc(headTitle:String):void{
	if(this["assetSelectAction"] != null)
		this["assetSelectAction"].selectedIndex = CommonMethod.getCbxItemIndex(headTitle,this["assetSelectAction"]);
}

//移除两个tile间多出的一行
private function resetFboxSize(inputTile:Tile,dateTile:Tile,advancedSearchVbx:VBox):void
{
	if(conArr.length > 0){
	var arrSlct:Array =  inputTile.getChildren();
	var widthAll:int = inputTile.width;
	var heightPer:int = conArr[0].height;
	var comsCount:int = arrSlct.length;
	var widthPer:int = arrSlct[0].width;
	for(var i:int=0;i<arrSlct.length;i++){
		if(arrSlct[i].width > widthPer)
			widthPer = arrSlct[i].width;
	}
	
	
	var perRowCount:int = ((widthAll - 30)/(widthPer + 4)) ;
	var rows:int = comsCount / perRowCount + ((comsCount % perRowCount > 0 ) ? 1 : 0);
	inputTile.height = (rows) * (heightPer+6);
	/* if(fbox.height > ( (rows) * (heightPer+8)) ){
	fbox.height -= (heightPer + 5);
	}
	*/
	if(dateTile.getChildren().length < 1){
		slctCndTm.includeInLayout = false;
	}
	/* var arr :Array = fbox1.getChildren();
	if(arr.length == 3){
	fbox1.height -= (heightPer + 5);
	}  */
	}else{
		inputTile.visible = false;
		inputTile.includeInLayout = false;
	}
	//设置样式
	setSearchAreaStyle(advancedSearchVbx.parent);
	dataGrid.rowHeight = 24;
	advancedSearchVbx.visible=false;
	advancedSearchVbx.includeInLayout=false;
}
//递归所有可视组件 
public function setSearchAreaStyle(doc:DisplayObjectContainer):void 
{
	var totalNum:int = doc.numChildren;
	
	for(var i:int=0;i<totalNum;i++){
		var o:Object = doc.getChildAt(i);
		if(setEnableType(o))
		{
			if(o is DisplayObjectContainer)
			{
				setSearchAreaStyle(DisplayObjectContainer(o));
			}
		}
	}
}

//设置界面组件 样式 
public function setEnableType(o:Object):Boolean
{
	var flag:Boolean = true;
//	if(o.hasOwnProperty("visible") && !o.visible)return false;
	if(o is Button){o.height = 20; flag=false; }
	else if(o is TextInput){o.height=20; flag=false; }
	else if(o is NumericStepper){o.height=20; flag=false; }
	else if(o is TextArea){ flag=false; }
	else if(o is DateField){
		o.height = 20; flag=false; 
	}
	else if(o is ComboBox){o.height = 20;if(ComboBox(o).labelField=="@mc"){o.blendMode="overlay";} flag=false; }
	//	else{o.enabled=true;};
	return flag;
}

