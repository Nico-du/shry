			import mx.collections.ArrayCollection;
			import mx.rpc.events.ResultEvent;
			import mx.managers.PopUpManager;
			import mx.utils.StringUtil;
			import mx.controls.Alert;
			 import flex.pojos.DSfbgmVo;
			import flex.pojos.DeptVo;
            import flex.pojos.DWlVo;
            import flex.pojos.DYuVo;
            import flex.pojos.DSmzqztVo;
            import flex.pojos.DSmsxVo;	
            import flex.pojos.DSyztVo;
	      //初始化combobox
	            public function initComnox():void
            {
            	commonService.getAllBm();
            	commonService.getObjectList(new DWlVo());
            	commonService.getObjectList(new DYuVo());
            	commonService.getObjectList(new DSmzqztVo());
            	commonService.getObjectList(new DSmsxVo());
            	commonService.getObjectList(new DSyztVo());
            	commonService.getObjectList(new DSfbgmVo());
            	commonService.addEventListener(ResultEvent.RESULT,initAddPrinBback);
            }
            
             private function initAddPrinBback(evt:ResultEvent):void
            {
	              switch(k)
	              {
	              	case 0:bm.dataProvider=evt.result as ArrayCollection;break;
	              	case 1:/*wl.dataProvider=evt.result as ArrayCollection;*/break;
	              	case 2:/*yu.dataProvider=evt.result as ArrayCollection;*/break;
	              	case 3:/*smzqzt.dataProvider=evt.result as ArrayCollection;*/break;
	              	case 4:smsx.dataProvider=evt.result as ArrayCollection;break;
	              	case 6:/*sfbgm.dataProvider=evt.result as ArrayCollection;*/ break;
	              }
	              k++;
            }
            
           private function reset():void
           {
           	  	var ary:Array=this.getChildren();
 				for each(var o:Object in ary)
 				 {
 					if(o is TextInput){o.text="";}
 					else if(o is ComboBox){o.text="";}	
 					else if(o is MyDateField){o.text="";}	
 					else if(o is TextArea){o.text="";}	
 					else if(o is NumericStepper){o.value=0;}
                 }
           } 
           
 
		