	import flash.events.DataEvent;
	import flash.events.Event;
	import flash.events.ProgressEvent;
	import flash.net.FileReference;
	import flash.net.URLRequest;
	import mx.controls.Alert;
	
	import flex.pojos.JzVo;
	
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
		public var jzService:RemoteObject = new RemoteObject("jzService");
		public var excelFile:FileReference = new FileReference();
		public var url:String;
		private var bh:String;
        //上传excel文件
        public function uploadExcel():void
        {
        	var excelFilter:FileFilter= new FileFilter("Office2003文件(*.xls)","*.xls");
        	var textTypes:FileFilter = new FileFilter("Office2007文件(*.xlsx)","*.xlsx;");

        	excelFile.addEventListener(Event.SELECT,selectHandler);
        	excelFile.addEventListener(ProgressEvent.PROGRESS,progressHandler);
        	excelFile.addEventListener(Event.COMPLETE,completeHandler);
        	excelFile.addEventListener(DataEvent.UPLOAD_COMPLETE_DATA,dataHandler);
        	try{
        	  excelFile.browse([excelFilter,textTypes]);
        	}catch(error:Error)
        	{
        		trace(error.message);
        	}
        }
        //连接后台服务
        public function selectHandler(evt:Event):void
        {
        	var request:URLRequest= new URLRequest(url);
        	try{
        		excelFile.upload(request);//uploadCompleteData
//        		excelFile.addEventListener(DataEvent.UPLOAD_COMPLETE_DATA,uploadCompleteDataBack);
        	}catch(error:Error)
        	{
        		trace(error.message);
        	}
        }
       
//        private function uploadCompleteDataBack(evt:DataEvent):void
//        {
//        	if(evt.text == null)
//        	{
//        		Alert.show("导入成功！！","信息提示");
//        	}
//        	else
//        	{
//        		Alert.show("未导入的设备号："+evt.text,"信息提示");    
//        	}	
//        }
//        
        private function progressHandler(event:ProgressEvent):void
        {
            lbProgress.text = " 已上传 " + (event.bytesLoaded/1024).toFixed(2)+ " K，共 " + (event.bytesTotal/1024).toFixed(2) + " K";
		    var proc: uint = event.bytesLoaded / event.bytesTotal * 100;
		    progress1.setProgress(proc, 100);
		    progress1.label= "当前进度: " + " " + proc + "%";
        }
        //关闭当前窗体
        private function cancel():void
        {
        	excelFile.removeEventListener(Event.SELECT,selectHandler);
        	excelFile.removeEventListener(ProgressEvent.PROGRESS,progressHandler);
        	PopUpManager.removePopUp(this);
        }
        

        private function completeHandler(event:Event):void {
        	progress1.label = "当前进度：" + event.target.name + "上传成功,等待数据导入...";
        	
        }
        
        private function dataHandler(event:DataEvent):void {
//        	jzService.endpoint = "/mxAsset/messagebroker/amf";
//        	jzService.showBusyCursor = true;
//        	var dataXml:XMLList = new XMLList(event.data);
//        	trace(dataXml.children().length());
//        	for each(var xml:XML in dataXml.asset) {
//        		bh = xml.@bh;
//        		progress1.label = "当前进度：设备编号 " + bh +" 正在导入...";
//        		var jzVo:JzVo = new JzVo();
//        		jzVo.bh = xml.@bh;
//        		jzVo.bmMc = xml.@bmMc;
//        		jzService.saveOrUpdateJz(jzVo);
//        		jzService.addEventListener(ResultEvent.RESULT,sucessHandler);
//        		jzService.addEventListener(FaultEvent.FAULT,faultHandler);
//        	}
			Alert.show(event.data,"提示");
        	trace(event.data);
        }
        
        private function sucessHandler(event:ResultEvent):void {
        	progress1.label = "当前进度：设备编号 " + bh +" 导入成功!";
        }
        
        private function faultHandler(event:FaultEvent):void {
        	progress1.label = "当前进度：设备编号 " + bh +" 导入失败!";
        }
        

