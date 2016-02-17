		import flex.pojos.DZclbVo;
		import flex.util.CommonMethod;
		
		import mx.collections.ArrayCollection;
		import mx.controls.Alert;
		import mx.managers.PopUpManager;
		import mx.rpc.events.ResultEvent;
		import mx.rpc.remoting.mxml.RemoteObject;
		import mx.utils.StringUtil;

		private var  kct:int = 0;
		public function initComboboxesAdded():void
		{
			var docRem:RemoteObject = new RemoteObject();
			docRem.destination = "commonService";
			docRem.endpoint = "/mxAsset/messagebroker/amf";
			docRem.showBusyCursor = true;
			
			docRem.getObjectList(new DZclbVo());
			docRem.addEventListener(ResultEvent.RESULT,initComboboxesBack);
		}
		
		private function initComboboxesBack(evt:ResultEvent):void
		{
			switch(kct)
			{
				case 0:zclb.dataProvider=evt.result as ArrayCollection;
					if(zclb.data != null){
						zclb.selectedIndex = CommonMethod.getCbxItemIndex(zclb.data+"",zclb);
					}
					
					break;
			}
			kct++;
		}
		
		
