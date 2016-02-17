import flash.display.DisplayObject;

import mx.core.UIComponent;

/**
 * include到国标数据表Form页面
 * 包含
 * 1.一些常用公共方法
 * 2.添加/删除入库等变更 关联数据 时同时添加/删除历史记录
 * */
		protected function setDicNameData(dicobj:Object,isDicFormPage:Boolean=true):void{
			
			if(this.hasOwnProperty("WPMC")){this["WPMC"].text = dicobj.dicname;	}
			if(this.hasOwnProperty("dataTitles")){this["dataTitles"] = dicobj.dicname;	}
			
			
		}

		//控制datumFormTabNV大小
		private var prvHeight:Number = -1;
		protected function datumFormTabNV_tabChildrenChangeHandler(event:Event):void
		{
			if(datumFormTabNV.selectedIndex == 1 ){
				prvHeight = datumFormTabNV.height;
				datumFormTabNV.height =  this.height - formToolBar.height - dataTitle.height - 30;
				errormsgbox.visible = false; errormsgbox.includeInLayout=false;
			}else if(prvHeight != -1){
				datumFormTabNV.height = prvHeight;
			}
			
		}
		/**
		 *隐藏历史记录 
		 **/
		private function removeLSJLCanvas(nvPage3:Object):void{
			ASCommonHelper.controlVisible(nvPage3 as UIComponent,false);
			datumFormTabNV.removeChild(nvPage3 as  DisplayObject);
		}
	
		/**
		 ** 添加/删除  单个历史记录的方法 (添加/删除数据时执行回调函数) 
		 **/
		
		//入库/领用/退库/维修/报废 登记时添加/删除入库数据同时添加历史记录信息
		public var alterBackObj:Object = new Object();
		/**
		 * step 1:设置添加/删除时的数据
		 **/
		private function setCallBackDelData(altertype:String,idColumnName:String):void{
			alterBackObj.altertype = altertype;
			alterBackObj.asetetid = this.dataId;
			if(this.hasOwnProperty("idColumnName")){
			alterBackObj.assetidcolumn = this["idColumnName"];
			}
			if(this.hasOwnProperty("tempObjBeanName")){
			alterBackObj.assettabnename = this["tempObjBeanName"]; 
			}
		}
		/**
		 *step 2:执行回调函数 
		 **/
		private function doCallBackFunc(parentPage:Object):void{
			if(parentPage.alterBackFunc != null){
				parentPage.alterBackFunc.call(null,alterBackObj);
			}
		}
		/**
		 *添加/删除 关联数据时同时 添加/删除历史记录 
		 **/
		private function callAlterBackFunc(altertype:String,parentPage:Object,idColumnName:String):void{
			setCallBackDelData(altertype,idColumnName);
			doCallBackFunc(parentPage);
		}


