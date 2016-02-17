	import flash.display.DisplayObject;
	import flash.events.DataEvent;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.net.FileReference;
	import flash.net.URLRequest;
	
	import flex.util.InDataBack;
	
	import mx.managers.PopUpManager;

	
		public var variables:URLVariables = new URLVariables(); 
		public var excelFile:FileReference = new FileReference();
		public var url:String;
        //上传excel文件
        public function uploadExcel():void
        {
        	var excelFilter:FileFilter= new FileFilter("Office2003文件(*.xls)","*.xls");
        	var textTypes:FileFilter = new FileFilter("Office2007文件(*.xls)","*.xls");

        	excelFile.addEventListener(Event.SELECT,selectHandler);
        	excelFile.addEventListener(ProgressEvent.PROGRESS,progressHandler);
        	excelFile.addEventListener(IOErrorEvent.IO_ERROR,ioErrorHandler);
        	excelFile.addEventListener(DataEvent.UPLOAD_COMPLETE_DATA,uploadCompleteDataBack);
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
        	request.data = variables;
        	try{
        		excelFile.upload(request);//uploadCompleteData
        	}catch(error:Error)
        	{
        		trace(error.message);
        	}
        }
       
        private function uploadCompleteDataBack(evt:DataEvent):void
        {
        	var s:String=evt.text;
        	 //  Alert.show(s,"信息提示");
           var back:InDataBack = new InDataBack();
           back.msg=s;
           PopUpManager.addPopUp(back,this.parentApplication as DisplayObject,true);
           PopUpManager.centerPopUp(back);
           progress1.label = "当前进度:数据处理完成。";
           close.enabled = true;
           loading.visible = false;
            
        }
        private function progressHandler(event:ProgressEvent):void
        {
            lbProgress.text = " 已上传 " + (event.bytesLoaded/1024).toFixed(2)+ " K，共 " + (event.bytesTotal/1024).toFixed(2) + " K";
		    var proc: uint = event.bytesLoaded / event.bytesTotal * 100;
		    progress1.setProgress(proc, 100);
		    progress1.label= "当前进度: " + " " + proc + "%";
		    if(proc == 100){
		    	progress1.label= "当前进度:excel上传完成。等待数据导入...";
		    	close.enabled = false;
		    	loading.visible = true;
		    }
        }
        
        private function ioErrorHandler(ioEvent:IOErrorEvent):void {
        	
        }
        //关闭当前窗体
        private function cancel():void
        {
        	excelFile.removeEventListener(Event.SELECT,selectHandler);
        	excelFile.removeEventListener(ProgressEvent.PROGRESS,progressHandler);
        	PopUpManager.removePopUp(this);
        }
        
     