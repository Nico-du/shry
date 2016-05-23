package flex.util
{
	import com.adobe.serialization.json.JSON;
	import com.benstucki.utilities.IconUtility;
	
	import flash.net.SharedObject;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	import mx.containers.Grid;
	import mx.containers.GridItem;
	import mx.containers.GridRow;
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Button;
	import mx.controls.ComboBox;
	import mx.controls.DataGrid;
	import mx.controls.Label;
	import mx.controls.LinkButton;
	import mx.controls.Tree;
	import mx.core.Application;

	/**
	 *静态公共方法类 
	 **/
	public class CommonUIMethod
	{
		///控制UI的公共方法
	
		/**
		 * 风叶选型数据转换
		 * 用来显示转换后的数据表格
		 * 根据 arcIn数据 生成一个 表格样式的Table
		 **/
		public static function generateTable(dataType:String,arcIn:ArrayCollection):Grid{
			var titleDataFyxn: Array = [
				{title: "流量(m^3/h)", columnname: "ll"},
				{title: "静压(Pa)", columnname: "jyl"},
				{title: "主转速(r/min)", columnname: "zzs"},
				{title: "辅转速(r/min)", columnname: "fzs"},
				{title: "扭矩(N·m)", columnname: "nj"},
				{title: "轴功率(W)", columnname: "zgl"},
				{title: "效率(%)", columnname: "xl"},
			];
			var titleDataZcxn: Array = [
				{title: "流量(m^3/h)", columnname: "ll"},
				{title: "静压(Pa)", columnname: "jyl"},
				{title: "主转速(r/min)", columnname: "zzs"},
				{title: "辅转速(r/min)", columnname: "fzs"},
				{title: "电压(V)", columnname: "dy"},
				{title: "电流(A)", columnname: "dl"},
				{title: "输入(W)", columnname: "srgl"},
				{title: "效率(%)", columnname: "xl"},
			];
			
		var curTitleData:Array = null;
		if(dataType == "fyxn"){
			curTitleData =  titleDataFyxn;
		}else if(dataType == "zcxn"){
			curTitleData =  titleDataZcxn;
		}else{ return null;}
		
		var grid:Grid = new Grid();
		grid.setStyle("borderStyle","solid");
		grid.setStyle("horizontalGap","0");
		grid.setStyle("verticalGap","0");
		var gridRowTitle:GridRow = new GridRow();
		//生成Title行
		for each(var eachClm:Object in curTitleData){
			if(eachClm == null || eachClm == "" || eachClm.length < 1){ continue;}
			var gridItem:GridItem = new GridItem();
			gridItem.width = 90;
			gridItem.height = 25;
			gridItem.setStyle("borderStyle","solid");
			gridItem.setStyle("borderThickness","1");
			gridItem.setStyle("horizontalAlign","center");
			gridItem.setStyle("verticalAlign","middle");
			gridItem.setStyle("paddingTop","-1");
			gridItem.setStyle("paddingBottom","-1");
			gridItem.setStyle("paddingLeft","-1");
			gridItem.setStyle("paddingRight","-1");
			var gLable:Label = new Label();
			gLable.text = eachClm.title;
			gLable.setStyle("paddingTop","3");
			gLable.setStyle("paddingLeft","3");
			
			gridItem.addChild(gLable);
			gridRowTitle.addChild(gridItem);
		}
		grid.addChild(gridRowTitle);
		//生成data行
		for each(var eachObj:Object in arcIn){
			var gridRow:GridRow = new GridRow();
			
			for each(var eachClm:Object in curTitleData){
				var gridItem:GridItem = new GridItem();
				gridItem.width = 90;
				gridItem.height = 25;
				gridItem.setStyle("borderStyle","solid");
				gridItem.setStyle("borderThickness","1");
				gridItem.setStyle("horizontalAlign","center");
				gridItem.setStyle("verticalAlign","middle");
				gridItem.setStyle("paddingTop","-1");
				gridItem.setStyle("paddingBottom","-1");
				gridItem.setStyle("paddingLeft","-1");
				gridItem.setStyle("paddingRight","-1");
				
				var gLable:Label = new Label();
				gLable.text = eachObj[eachClm.columnname];
				gLable.setStyle("paddingTop","3");
				gLable.setStyle("paddingLeft","2");
				
				gridItem.addChild(gLable);
				gridRow.addChild(gridItem);
			}
			grid.addChild(gridRow);
		}
			return grid;
		}
		
		
		
		/**
		 *全选/反选
		 **/
		public static function ui_SelectAll_Reverse(dataGrid:DataGrid,lbl:String):void {
			switch(lbl){
				case "Select": break;
				case "All"://全选
					for(var i:int = 0; i < dataGrid.dataProvider.length; i++) {
						if(dataGrid.dataProvider.getItemAt(i).id != 41)
							dataGrid.dataProvider.getItemAt(i).selected = true;
					}
					break;
				case "Reverse"://反选
					for(var i:int = 0; i < dataGrid.dataProvider.length; i++) {
						if(dataGrid.dataProvider.getItemAt(i).id != 41)
							dataGrid.dataProvider.getItemAt(i).selected = !dataGrid.dataProvider.getItemAt(i).selected;
					}
					break;
				default:throw new Error("参数错误!"); break;
			}
		} 
		/**
		 *点击高级查询 改变按钮状态 
		 **/
		public static function ui_advancedSearchChange(advancedSearchBtn:Button,advancedSearchHbx:VBox,normalSearchHbx:HBox = null):void{
			var highSearchtxt:String=advancedSearchBtn.label;
			if(highSearchtxt == "高级"){
				if(normalSearchHbx != null)	CommonMethod.setEnable(normalSearchHbx,false,false);
				advancedSearchBtn.label="隐藏";
				advancedSearchHbx.visible = true;
				advancedSearchHbx.includeInLayout = true;
			}else{					
				if(normalSearchHbx != null)CommonMethod.setEnable(normalSearchHbx,true,true);
				advancedSearchHbx.visible = false;
				advancedSearchHbx.includeInLayout = false;
				advancedSearchBtn.label="高级";
			}
		}
		/**
		 *从DBFrame.xml secondMenu的id根据获取资产子类型combobox的数据源 
		 **/
		public static function ui_getAssetComoboxDataprovter(secondMenuId:String,cbx:ComboBox):void{
			if(CommonXMLData.Asset_ALLType_XML == null)return;
			/*var commxml:XML = CommonXMLData.Asset_ALLType_XML;
			var obj2:Object =  CommonXMLData.Asset_ALLType_XML.secondMenu;
			var obj3:Object =  CommonXMLData.Asset_ALLType_XML.secondMenu.(@id==secondMenuId);
			var obj4:Object =  CommonXMLData.Asset_ALLType_XML.secondMenu.(@id==secondMenuId)[0];*/
			cbx.dataProvider = CommonXMLData.Asset_ALLType_XML.secondMenu.(@id==secondMenuId)[0].thirdMenu;
		}
		/**
		 *备注输入框获取焦点时增高备注的height 
		 **/
		public static function ui_resetMemoHeight(memo:Object,datumFormTabNV:Object,aboutheight:Number):void{
			//resizeEffect.play();
			if(memo.height < (3*aboutheight)){
				datumFormTabNV.height += 2*aboutheight;
			}
			memo.height = 3*aboutheight;
		}
		/**
		 *折叠Asset.xml左侧LeftMenu的方法 ；
		 * zdMenu:折叠按钮
		 **/
		public static function ui_foldLeftMenu(zdMenu:LinkButton):void{
			var menuLeft:Object = Application.application.mainPage.AssetPage.menuLeft;
			zdMenu.setStyle("icon",null);
			if(menuLeft.visible){
				zdMenu.toolTip = "展开";
				var tempClass:Class=IconUtility.getClass(zdMenu,"images/pg_next1.gif",11,11);
				zdMenu.setStyle("icon",tempClass);
			}else{
				zdMenu.toolTip = "收起";
				var tempClass:Class=IconUtility.getClass(zdMenu,"images/pg_prev1.gif",11,11);
				zdMenu.setStyle("icon",tempClass);
			}
			menuLeft.visible = !menuLeft.visible;
			menuLeft.includeInLayout = !menuLeft.includeInLayout;
		}
		/**
		 *设置  所有 imgFold是否可见
		 **/
		public static function ui_resetImgFoldVisible(currentID:String):void{
			if(currentID != "DataAsset"){
				Application.application.mainPage.DataAssetPage.imgFold.visible = false;
				//102 领用  105 报废
				if(Application.application.mainPage.DataAssetPage.modelMap["102"] != null){
					Application.application.mainPage.DataAssetPage.modelMap["102"].imgFold.visible = false;
				}
				if(Application.application.mainPage.DataAssetPage.modelMap["105"] != null){
					Application.application.mainPage.DataAssetPage.modelMap["105"].imgFold.visible = false;
				}
			}
			if(currentID != "SelectionMain"){
			Application.application.mainPage.SelectionMainPage.imgFold.visible = false;
			}
			if(currentID != "ServiceMain"){
			Application.application.mainPage.ServiceMainPage.imgFold.visible = false;
			}
			if(currentID != "SystemMain"){
				Application.application.mainPage.SystemMainPage.imgFold.visible = false;
			}
			
		}
		
		
		
		/**
		 * 使用LSO——Local Share Object（本地共享对象）其实类似于cookie 存储数据
		 * WIN7默认路径 C:\Users\Administrator\AppData\Roaming\Macromedia\Flash Player\#SharedObjects\BUMFXN7W\#localhost
		 * XP C:\Documents and Settings\username\Application Data\Macromedia\Flash Player\#SharedObjects
		 * */
		public static function getShareObjectValue(key:String):Object{
			return (SharedObject.getLocal("mydata","/").data.CookiesMap == null ? null : SharedObject.getLocal("mydata","/").data.CookiesMap[key]);
		}
		public static function getTreeDefaultData():Object{
			return getShareObjectValue("defaultmenutreeItems");
		}
		//存储选择的显示列
		public static function saveDefaultTreeData(value:Object):void{
			var so: SharedObject = SharedObject.getLocal("mydata","/");
			var objt:Object = so.data.CookiesMap;
			//	SharedObject.getLocal("mydata","/").data.CookiesMap = null;
			if(objt == null )objt = new Object();
			
			objt["defaultmenutreeItems"] = value;
			so.data.CookiesMap = objt;
			so.flush();
		}
		/**
		 * 记住最后一次选择的显示列
		 * 读取存储在LSO中的数据 作为默认显示列
		 **/
		//		<node id="wzxx_ryhc" label="日用耗材" pageUrl="RYHCGRIDMOUDEL"/>
		public static function setTreeDefaultData(tree:Tree):void{
			var arrclm:ArrayCollection = getShareObjectValue("defaultmenutreeItems") as ArrayCollection;
			if(arrclm != null && arrclm.length>0){
				var xmlAll:XML = 
				<node id="2014" label="物资管理">
					<node id="wzxx" label="物资信息" pageUrl="">
					</node>
				</node>;
				
				for each(var eachItem in arrclm){
					var xmleach:XML = new XML(<node />);
					xmleach.@id = eachItem.id;
					xmleach.@label = eachItem.label;
					xmleach.@isSearchXml = "true";
					var asm:DataAssetMain = new DataAssetMain();
					xmleach.@dicids = asm.getCurrentDicids(xmleach);
					xmlAll.node[0].appendChild(xmleach);
					asm=null;
				}
				tree.dataProvider = xmlAll;
			}
		}	
		
	}
}