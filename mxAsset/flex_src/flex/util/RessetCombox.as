         	import flex.pojos.DSfbgmVo;
         	import flex.pojos.DSmsxVo;
         	import flex.pojos.DSmzqztVo;
         	import flex.pojos.DSyztVo;
         	import flex.pojos.DWlVo;
         	import flex.pojos.DYuVo;
         	import flex.pojos.DZclbVo;
         	import flex.pojos.DeptVo;
         	
         	import mx.collections.ArrayCollection;
         	import mx.controls.Alert;
         	import mx.managers.PopUpManager;
         	import mx.rpc.events.ResultEvent;
         	import mx.utils.StringUtil;

           public function resetComnox():void
            {
            	commonService.getAllBm();
            	commonService.getObjectList(new DWlVo());
            	commonService.getObjectList(new DYuVo());
            	commonService.getObjectList(new DSmzqztVo());
            	commonService.getObjectList(new DSmsxVo());
            	commonService.getObjectList(new DSyztVo());
            	commonService.getObjectList(new DSfbgmVo());
				commonService.getObjectList(new DZclbVo());
            	commonService.addEventListener(ResultEvent.RESULT,resetComnoxBack);
            }
   
            private function resetComnoxBack(evt:ResultEvent):void
            {
	              switch(k)
	              {
	              	case 0:
		              	if (obj.bmmc != null && obj.bmmc != "")	{bm.prompt=obj.bmmc;}
		              	else {bm.prompt=" ";}bm.dataProvider=evt.result as ArrayCollection;
		              	break;
	              	case 1:
	              		/*if (obj.wl != null && obj.wl != "")	{wl.prompt=obj.wl;}
		              	else {wl.prompt=" ";}wl.dataProvider=evt.result as ArrayCollection;*/
		              	break;
	              	case 2:
	              		/*if (obj.yu != null && obj.yu != "")	{yu.prompt=obj.yu;}
		              	else {yu.prompt=" ";}yu.dataProvider=evt.result as ArrayCollection;*/
		              	break;
	              	case 3:
	              	   /* if(obj.smzqzt != null && obj.smzqzt != ""){smzqzt.prompt=obj.smzqzt}
	              	    else{smzqzt.prompt=" ";}smzqzt.dataProvider=evt.result as ArrayCollection;*/
	              	    break;
	              	case 4:
	              		if(obj.smsx != null && obj.smsx != ""){smsx.prompt=obj.smsx}
	              	    else{smsx.prompt=" ";}smsx.dataProvider=evt.result as ArrayCollection;
	              	    break;
	              	case 6:
	              		/*if(obj.sfbgm != null && obj.sfbgm != ""){sfbgm.prompt=obj.sfbgm}
	              	    else{sfbgm.prompt=" ";}sfbgm.dataProvider=evt.result as ArrayCollection;*/
	              	    break;
	              	case 7:
	              		if(obj.zclb != null && obj.zclb != ""){zclb.prompt=obj.zclb}
	              	    else{zclb.prompt=" ";}zclb.dataProvider=evt.result as ArrayCollection;
	              	    break;
	              }
	              k++;
            }