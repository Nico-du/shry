						
					
		 import flex.pojos.TbTypeVo;

	    
		private var tbtypeVo:TbTypeVo=null;
			public function initTbType(meet:String):void
		{
				 tbtypeVo=new TbTypeVo();
				tbtypeVo.typeMark=meet;
				commonService.getObjectList(tbtypeVo);
			
				commonService.addEventListener(ResultEvent.RESULT,initAddPrinBback);
				
		} 

	private function initAddPrinBback(evt:ResultEvent):void
	{
	
	}
		