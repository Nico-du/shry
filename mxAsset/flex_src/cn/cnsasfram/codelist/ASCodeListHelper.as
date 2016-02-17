package cn.cnsasfram.codelist{
	import cn.cnsasfram.helper.ASStringHelper;
	
	import com.adobe.serialization.json.JSON;
	
	import mx.collections.ArrayCollection;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.FlexGlobals;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;

	/**
	 * 代码表辅助类
	 * */
	public class ASCodeListHelper{
		
		//保存代码表值
		public var codelistarray:ArrayCollection=new ArrayCollection();
		
		//远程调用odelist对象
		public var rmtCodeList:RemoteObject=null;
		
		public function ASCodeListHelper(){
			rmtCodeList=this.getCodeListRemote();
		}

		//加载CODELIST
		public function initCodeList():void{
			rmtCodeList.GetCodeListByType("");
			rmtCodeList.addEventListener(ResultEvent.RESULT,initCodeListBack);
		}
		
		//加载CODELIST回调方法
		protected function initCodeListBack(rmtResult:ResultEvent):void{
			rmtCodeList.removeEventListener(ResultEvent.RESULT,initCodeListBack);
			var objResult:Object=rmtResult.result;
			if(objResult==null){
				return;
			}
			var strresult:String=objResult.toString();
			var jsonObj:Object=JSON.decode(strresult,true);
			this.codelistarray=new ArrayCollection(jsonObj as Array);
		}
		//得到远程调用对象
		protected function getCodeListRemote():RemoteObject{
			var tempRemote:RemoteObject=new RemoteObject();
			tempRemote.destination="codeListService";
			tempRemote.endpoint="/mxAsset/messagebroker/amf";
			tempRemote.showBusyCursor=false;
			return tempRemote;
		}
		
		//代码表分析
		public static function getCodeListLabel(item:Object,column:DataGridColumn):String{
			var strFiled:String=item[column.dataField];
		//	if(int(strFiled) != 0){return strFiled;}
			var tempArray:ArrayCollection=FlexGlobals.topLevelApplication.codelistheler.codelistarray;
			for(var i:int=0;i<tempArray.length;i++){
				var tempObject:Object=tempArray.getItemAt(i);
				if(ASStringHelper.stringCompare(strFiled,tempObject.value,true)==0){
					return tempObject.label;
				}
			}
			return "";
		}
	}
}