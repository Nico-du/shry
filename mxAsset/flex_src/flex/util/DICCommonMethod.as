
		/*import asset_Book.BookList;
		
		import asset_Cable.CableList;
		
		import asset_Computer.ComputerList;
		
		import asset_Furniture.FurnitureList;
		
		import asset_Jz.JzList;
		
		import asset_Module.ModuleList;
		
		import asset_Video.VideoList;
		
		import asset_Wssb.WssbList;
		
		import asset_dirtyEvidence.DirtyList;
		
		import asset_heritage.HerList;
		
		import asset_ryhc.RYHCGridMoudel;
		
		import asset_server.ServerList;
		
		import asset_storage.Net;
		import asset_storage.Storage;
		
		import asset_ups.UpsList;
		
		import dic_bookheritage.DICBookHeritageGridMoudel;
		
		import dic_furniture.DICFunrGridMoudel;
		
		import dic_generalequ.DICGeneralequGridMoudel;
		
		import dic_house.DICHouseGridMoudel;
		
		import dic_intangibleasset.DICIntangibleGridMoudel;
		
		import dic_land.DICLandGridMoudel;
		
		import dic_transportequ.DICTransportGridMoudel;
		
		import flash.display.DisplayObject;
		import flash.utils.Dictionary;
		
		*/

		import mx.containers.ViewStack;
		import mx.controls.Alert;
		import mx.core.Application;
		import flex.util.CommonMethod;
		import flex.util.CommonXMLData;

		private var DICLANDLIST:String="101|100";
		//房屋
		private var DICHOUSELIST:String="102|200";
		//构筑物
		private var DICSTRUCTURESLIST:String="103|300";
		//通用设备
		private var DICGENERALEQULIST:String="210|310|400";
		//专用设备
		private var DICSPECIALEQULIST:String="301|302|303|305|306|307|308|" +
			"313|314|315|316|317|318|319|320|321|322|324|325|326|327|328|399|500";

		//交通运输设备
		private var DICTRANSPORTEQULIST:String="203|304|310|350|351|352|600";
		//电器设备
		private var DICELECTRICALEQULIST:String="220|323|700";
		//电子产品及通讯设备
		private var DICCOMMUNICATIONEQULIST:String="201|202|230|231|232|800";
		//仪器仪表及量具
		private var DICINSTRUMENTLIST:String="240|241|242|360|900";
		//文艺体育设备
		private var DICSPORTSEQULIST:String="370|371|372";
		//文艺体育设备主菜单
		private var DICSPORTSEQULISTROOT:String="1000000000";

		//图书档案文物陈列品
		private var DICBOOKHERITAGELIST:String="401|402|501|110";
		//家具用具被服装具、特殊用途动物/植物
		private var DICFURNITUREOTHERLIST:String="601|602|603|604|120";
		//无形资产
		private var DICINTANGIBLEASSETLIST:String="701|130";


		/**
		 * 得到相应的组件
		 * 
		 * isDoAUValidate  是否执行权限验证
		 **/
		public function getDICModelBySelect(selectItem:XML,modelMap:Dictionary=null,managerStack:ViewStack=null,isDoAUValidate:Boolean=true):INavigatorContent{
			if(selectItem==null){
				return null;
			}
			var strid:String=selectItem.@id;
			var strname:String=selectItem.@label;
			var strpageurl:String=selectItem.@id;
			if(strname == "固定资产" || strname=="物资信息"){return null;}
	//		if(strid.indexOf("00000000") > -1){ return null;}
			if(modelMap != null){
				var targetModel:Object = modelMap[strid];
				if(targetModel!=null){
					return targetModel as INavigatorContent;
				}
			}
			
			var tempObj:Object = null;
			/*if(CommonMethod.isEqualDicId(strpageurl,SERVERLIST,7)){
				tempObj=new ServerList();
				tempObj.type=strname;
			}else if(CommonMethod.isEqualDicId(strpageurl,COMPUTERLIST,5)){
				tempObj=new ComputerList();
				tempObj.headTitle=strname;
			}else if(CommonMethod.isEqualDicId(strpageurl,JZLIST,7)){
				tempObj=new JzList();
				tempObj.myTitle=strname;
			}else if(CommonMethod.isEqualDicId(strpageurl,STORAGE,5)){
				tempObj=new Storage();
				tempObj.type=strname;
			}else if(CommonMethod.isEqualDicId(strpageurl,NET,5)){
				tempObj=new Net();
				tempObj.type=strname;
			}else if(CommonMethod.isEqualDicId(strpageurl,UPSLIST,5)){
				tempObj=new UpsList();
				tempObj.type=strname;
			}else if(CommonMethod.isEqualDicId(strpageurl,CABLELIST,7)){
				tempObj=new CableList();
				tempObj.headTitle=strname;
			}else if(CommonMethod.isEqualDicId(strpageurl,MODULELIST,7)){
				tempObj=new ModuleList();
				tempObj.headTitle=strname;
			}else if(CommonMethod.isEqualDicId(strpageurl,VIDEOLIST,5)){
				tempObj=new VideoList();
				tempObj.headTitle=strname;
			}else if(CommonMethod.isEqualDicId(strpageurl,WSSBLIST,5)){
				tempObj=new WssbList();
				tempObj.assetType=strname;
			}else
			if(CommonMethod.isEqualDicId(strpageurl,DICBOOKHERITAGELIST,3)){
				tempObj = new DICBookHeritageGridMoudel();
			}else if(CommonMethod.isEqualDicId(strpageurl,DICFURNITUREOTHERLIST,3)){
				tempObj=new DICFunrGridMoudel();
			}else if(CommonMethod.isEqualDicId(strpageurl,DICGENERALEQULIST,3)){
				tempObj=new DICGeneralequGridMoudel();
				tempObj.tempObjBeanName = tempObj.tempObjViewName = "DIC_GENERALEQU";
				tempObj.idColumnName = "GENERALEQUID"; tempObj.pojoName="DicGeneralequ";
				tempObj.childPageTitle="通用设备";
			}else if(CommonMethod.isEqualDicId(strpageurl,DICSPECIALEQULIST,3)){
				tempObj=new DICGeneralequGridMoudel();
				tempObj.tempObjBeanName = tempObj.tempObjViewName = "DIC_SPECIALEQU";
				tempObj.idColumnName = "SPECIALEQUID"; tempObj.pojoName="DicSpecialequ";
				tempObj.childPageTitle="专用设备";
			}else if(CommonMethod.isEqualDicId(strpageurl,DICINSTRUMENTLIST,3)){
				tempObj=new DICGeneralequGridMoudel();
				tempObj.tempObjBeanName = tempObj.tempObjViewName = "DIC_INSTRUMENT";
				tempObj.idColumnName = "INSTRUMENTID"; tempObj.pojoName="DicInstrument";
				tempObj.childPageTitle="仪器仪表及量具";
			}else if(CommonMethod.isEqualDicId(strpageurl,DICELECTRICALEQULIST,3)){
				tempObj=new DICGeneralequGridMoudel();
				tempObj.tempObjBeanName = tempObj.tempObjViewName = "DIC_ELECTRICALEQU";
				tempObj.idColumnName = "ELECTRICALEQUID"; tempObj.pojoName="DicElectricalequ";
				tempObj.childPageTitle="电器设备";
			}else if(CommonMethod.isEqualDicId(strpageurl,DICSPORTSEQULIST,3) || CommonMethod.isEqualDicId(strpageurl,DICSPORTSEQULISTROOT,10)){
				tempObj=new DICGeneralequGridMoudel();
				tempObj.tempObjBeanName = tempObj.tempObjViewName = "DIC_SPORTSEQU";
				tempObj.idColumnName = "SPORTSEQUID"; tempObj.pojoName="DicSportsequ";
				tempObj.childPageTitle="文艺体育设备";
			}else if(CommonMethod.isEqualDicId(strpageurl,DICCOMMUNICATIONEQULIST,3)){
				tempObj=new DICGeneralequGridMoudel();
				tempObj.tempObjBeanName = tempObj.tempObjViewName = "DIC_COMMUNICATIONEQU";
				tempObj.idColumnName = "COMMUNICATIONEQUID"; tempObj.pojoName="DicCommunicationequ";
				tempObj.childPageTitle="电子产品及通讯设备";
			}else if(CommonMethod.isEqualDicId(strpageurl,DICHOUSELIST,3)){
				tempObj=new DICHouseGridMoudel();
				tempObj.tempObjBeanName = tempObj.tempObjViewName = "DIC_HOUSE";
				tempObj.idColumnName = "HOUSEID"; tempObj.pojoName="DicHouse";
				tempObj.childPageTitle="房屋";
			}else if(CommonMethod.isEqualDicId(strpageurl,DICSTRUCTURESLIST,3)){
				tempObj=new DICHouseGridMoudel();
				tempObj.tempObjBeanName = tempObj.tempObjViewName = "DIC_STRUCTURES";
				tempObj.idColumnName = "STRUCTURESID"; tempObj.pojoName="DicStructures";
				tempObj.childPageTitle="构筑物";
			}else if(CommonMethod.isEqualDicId(strpageurl,DICLANDLIST,3)){//TODO 待添加表单
				tempObj=new DICLandGridMoudel();
			}else if(CommonMethod.isEqualDicId(strpageurl,DICTRANSPORTEQULIST,3)){
				tempObj=new DICTransportGridMoudel();
			}else if(CommonMethod.isEqualDicId(strpageurl,"RYHCGRIDMOUDEL",15)){
				tempObj=new RYHCGridMoudel();
			}else if(CommonMethod.isEqualDicId(strpageurl,DICINTANGIBLEASSETLIST,3)){
				tempObj=new DICIntangibleGridMoudel();
			}*/ 
			if(tempObj == null && CommonXMLData.IsTestEnvironment){Alert.show("尚未映射的设备类型"+strpageurl); return  null;}
			if(isDoAUValidate){if(CommonMethod.executePwdAuthority(tempObj,selectItem) == null)return null;} 	//执行权限验证	
			
			//传值给每个页面
			selectItem.@dicids = CommonMethod.currentPageUrl;
			CommonMethod.currentPageUrl = "";
			tempObj.data = selectItem;
			setDicGridDefaultAU(tempObj);
			if(modelMap != null){ modelMap[strid]=tempObj; }
			if(managerStack != null){ managerStack.addChild(tempObj as DisplayObject); }
			return tempObj as INavigatorContent;
		}

		//控制超级管理员  国标资产基础数据 页面权限
		private function setDicGridDefaultAU(currentWd:Object):void{
			if(!currentWd.hasOwnProperty("idColumnName") || Application.application.userVo.ruleId != "41")return;
			currentWd.isAddUsable = false;
			currentWd.isDelUsable = false;
			currentWd.isEditUsable = false;
		}

