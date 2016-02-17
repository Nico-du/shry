						
						import flex.pojos.DSfbgmVo;
						import flex.pojos.DSmsxVo;
						import flex.pojos.DSmzqztVo;
						import flex.pojos.DSyztVo;
						import flex.pojos.DWlVo;
						import flex.pojos.DYuVo;
						import flex.pojos.DeptVo;

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
	              	case 1://wl.dataProvider=evt.result as ArrayCollection;break;
	              	case 2://yu.dataProvider=evt.result as ArrayCollection;break;
	              	case 3://smzqzt.dataProvider=evt.result as ArrayCollection;break;
	              	case 4:smsx.dataProvider=evt.result as ArrayCollection;break;
	              	case 5:zt.dataProvider=evt.result as ArrayCollection;break;
	              	case 6://sfbgm.dataProvider=evt.result as ArrayCollection;
	              	    break;
	              }
	              k++;
            }

		