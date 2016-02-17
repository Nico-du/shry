package flex.util
{
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.net.navigateToURL;

	public class ExportExcel2
	{
		public function ExportExcel2()
		{
			super();
		}
		
		public function ExportDGToExcel(columns:Array,tableName:String,excelName:String,title:String,url:String="/mxAsset/servlet/ExportServlet"):void {
				
			var variables:URLVariables = new URLVariables(); 
			variables.excelName = excelName;
			variables.title = title;
			var jdbcSql:String = "select ";
			var length:int = columns.length;
			var colArr:Array = new Array(length);
			for(var i:int=0;i<length;i++) {
				colArr[i] = columns[i].headerText;
				if(i==length-1)
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\" ";
				else
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\", ";
			}
			jdbcSql += " from "+tableName;
			trace(jdbcSql);
			variables.jdbcSql = jdbcSql;
			variables.length = length;
			variables.headerTexts = colArr;
			ExportToExcel(variables);
		} 
		
	public function ExportDGToExcel2(columns:Array,tableName:String,excelName:String,title:String,where:String,url:String="/mxAsset/servlet/ExportServlet",idColumn:String="id",isWFExp:String="false"):void {
				
			var variables:URLVariables = new URLVariables(); 
			variables.excelName = excelName;
			variables.title = title;
			var jdbcSql:String = "select ";
			
			var length:int = columns.length;
			var colArr:Array = new Array(length);
			for(var i:int=0;i<length;i++) {
				colArr[i] = columns[i].headerText;
				if(i==length-1)
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\" ";
				else
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\", ";
			}
			jdbcSql += " from "+tableName+" "+where +" order by "+idColumn+" desc";
			
			if("from" == where.substr(0, 4)) {
				jdbcSql = where;
			}else if("idSt" == where.substr(0, 4)) {
				jdbcSql = "from "+tableName+" where id in "+where.substr(4, where.length) +" idStr";
			}
			
			trace(jdbcSql);
			variables.jdbcSql = jdbcSql;
			variables.length = length;
			variables.headerTexts = colArr;
			variables.isWFExp = isWFExp;
			ExportToExcel(variables);
		} 
	
	public function ExportDoubleTitleDGToExcel(columns:Array,tableName:String,excelName:String,title:String,where:String,secondHeaderTexts:Array,
											   secondjdbcSql:String,url:String="/mxAsset/servlet/ExportServlet",idColumn:String="id",isWFExp:String="false"):void {
				
			var variables:URLVariables = new URLVariables(); 
			variables.excelName = excelName;
			variables.title = title;
			var jdbcSql:String = "select ";
			
			var length:int = columns.length;
			var colArr:Array = new Array(length);
			for(var i:int=0;i<length;i++) {
				colArr[i] = columns[i].headerText;
				if(i==length-1)
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\" ";
				else
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\", ";
			}
			jdbcSql += " from "+tableName+" "+where +" order by "+idColumn+" desc";
			
			if("from" == where.substr(0, 4)) {
				jdbcSql = where;
			}else if("idSt" == where.substr(0, 4)) {
				jdbcSql = "from "+tableName+" where id in "+where.substr(4, where.length) +" idStr";
			}
			
			trace(jdbcSql);
			var secondSql:String = "select ";
			var colArr2:Array = new Array(secondHeaderTexts.length);
			for(var i:int=0;i<secondHeaderTexts.length;i++) {
				colArr2[i] = secondHeaderTexts[i].headerText;
				if(i == secondHeaderTexts.length-1)
					secondSql += secondHeaderTexts[i].dataField+" \""+secondHeaderTexts[i].headerText+"\" ";
				else
					secondSql += secondHeaderTexts[i].dataField+" \""+secondHeaderTexts[i].headerText+"\", ";
			}
			secondSql += secondjdbcSql;
			
			variables.jdbcSql = jdbcSql;
			variables.length = length;
			variables.headerTexts = colArr;
			variables.isWFExp = isWFExp;
			
			variables.secondjdbcSql = secondSql;
			variables.secondHeaderTexts = colArr2;
			ExportToExcel(variables);
		} 
		
		public function ExportDGToExcelC(columns:Array,tableName:String,excelName:String,title:String,where:String,url:String="/mxAsset/servlet/ExportServlet"):void {
			var sArr:Array = new Array("tempDeviceId","deviceId","computerName","cpuName",
			"hdSn","systemPatch","antivirusVersion","antivirusName","s360Version","osVersion",
			"cpuModel","cpuCore","level2Cache","cpuSocket","cpuNum","biosModel","memorySize",
			"memoryModel","hdModel","hdSize","motherboardModel","motherboardName","motherboardSn","ntime","macAddress","ipAddress","collectDate");
			
			var aArr:Array = new Array("temp_Device_Id","device_Id","computer_Name","cpu_Name",
			"hd_Sn","system_Patch","antivirus_Version","antivirus_Name","s360_Version","os_Version",
			"cpu_Model","cpu_Core","level_2_Cache","cpu_Socket","cpu_Num","bios_Model","memory_Size",
			"memory_Model","hd_Model","hd_Size","motherboard_Model","motherboard_Name","motherboard_Sn","n_time","mac_Address","ip_Address","collectDate");
			var variables:URLVariables = new URLVariables(); 
			variables.excelName = excelName;
			variables.title = title;
			var jdbcSql:String = "select ";
			var length:int = columns.length;
			var colArr:Array = new Array(length);
			for(var i:int=0;i<length;i++) {
				colArr[i] = columns[i].headerText;
				if(i==length-1)
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\" ";
				else
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\", ";
			}
			jdbcSql += " from "+tableName+" "+where +" order by id desc";
			
			if("from" == where.substr(0, 4)) {
				jdbcSql = where;
			}else if("idSt" == where.substr(0, 4)) {
				jdbcSql = "from "+tableName+" where id in "+where.substr(4, where.length) +" idStr";
			}
			for(var j:int=0;j<sArr.length;j++){
				var temp:int =jdbcSql.indexOf(sArr[j],0);
				if(temp!=-1){
					jdbcSql=jdbcSql.replace(sArr[j],aArr[j]);
				}
			}
			trace(jdbcSql);
			variables.jdbcSql = jdbcSql;
			variables.length = length;
			variables.headerTexts = colArr;
			ExportToExcel(variables);
		} 
		
		public function ExportDGToExcel5(columns:Array,a:String,tableName:String,excelName:String,title:String,where:String,url:String="/mxAsset/servlet/ExportServlet"):void {
				
			var variables:URLVariables = new URLVariables(); 
			variables.excelName = excelName;
			variables.title = title;
			var jdbcSql:String = "select ";
			var length:int = columns.length;
			var colArr:Array = new Array(length);
			for(var i:int=0;i<length;i++) {
				colArr[i] = columns[i].headerText;
				if(i==length-1)
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\" ";
				else
					jdbcSql += columns[i].dataField+" \""+columns[i].headerText+"\", ";
			}
			jdbcSql += " from "+tableName+" "+where +" order by id desc";
			
			if("from" == where.substr(0, 4)) {
				jdbcSql = where;
			}else if("idSt" == where.substr(0, 4)) {
				jdbcSql = "from "+tableName+" where id in "+where.substr(4, where.length) +" idStr";
			}
			
			trace(jdbcSql);
			variables.jdbcSql = jdbcSql;
			variables.length = length;
			variables.a=a;
			variables.headerTexts = colArr;
			ExportToExcel(variables);
		} 
		//导出任务相关的excel
		public function ExportDGToExcel_renwu(columns:Array,tableName:String,excelName:String,title:String,where:String,url:String="/mxAsset/servlet/ExportServlet"):void {
				
			var variables:URLVariables = new URLVariables(); 
			variables.excelName = excelName;
			variables.title = title;
			var jdbcSql:String = "select ";
			
			var columns2:Array = new Array();
			for(var a:int=0;a<columns.length;a++) {
				trace(columns[a].headerText+i+columns[a].visible);
				if(columns[a].visible){
					columns2.push(columns[a]);
				}
				
			}
			var length:int = columns2.length;
			var colArr:Array = new Array(length);
			for(var i:int=0;i<length;i++) {
				trace(columns2[i].headerText+i+columns2[i].visible);
				colArr[i] = columns2[i].headerText;
				if(i==length-1)
					jdbcSql += columns2[i].dataField+" \""+columns2[i].headerText+"\" ";
				else
					jdbcSql += columns2[i].dataField+" \""+columns2[i].headerText+"\", ";
			}
			jdbcSql += " from "+tableName+" "+where +" order by id desc";
			
			if("from" == where.substr(0, 4)) {
				jdbcSql = where;
			}
			
			trace(jdbcSql);
			variables.jdbcSql = jdbcSql;
			variables.length = length;
			variables.headerTexts = colArr;
			ExportToExcel(variables);
		} 
		public function ExportSqlToExcel(jdbcSql:String,excelName:String,title:String,headerTexts:Array,url:String="/mxAsset/servlet/ExportServlet"):void {
			var variables:URLVariables = new URLVariables();
			var length:int = headerTexts.length;
			var colArr:Array = new Array(length);
			for(var i:int=0;i<length;i++) {
				colArr[i] = headerTexts[i].headerText;
			}
			variables.excelName = excelName;
			variables.title = title;
			variables.jdbcSql = jdbcSql;
			variables.headerTexts = colArr;
			variables.length = headerTexts.length;
			ExportToExcel(variables);
		}
		
		 public function ExportSqlToExcel3(jdbcSql:String,excelName:String,title:String,headerTexts:Array,length:int,url:String="/mxAsset/servlet/ExportServlet"):void {
			var variables:URLVariables = new URLVariables();
			variables.excelName = excelName;
			variables.title = title;
			variables.jdbcSql = jdbcSql;
			variables.headerTexts = headerTexts;
			variables.length = length;
			ExportToExcel(variables);
		}
		public function ExportAll(where:String,url:String="/mxAsset/servlet/ExportAllExcel"):void {
				var variables:URLVariables = new URLVariables(); 
			var jdbcSql:String = "select a.sbbh,a.mc,a.ppxh,a.sbxlh,a.gzrq,a.zt,a.bmmc,a.zrr,a.sydd,c.computer_name,a.zyyt,c.ip_address,c.mac_address,c.cpu_model,c.memory_size,c.os_version,c.hd_model,c.hd_size,c.hd_sn,c.cdromdrive from asset_computer a,case" + 
					"_computer c "+where +" order by a.id desc";
			
			trace(jdbcSql);
			variables.jdbcSql = jdbcSql;
			ExportToExcel1(variables);
		}
		
		private function ExportToExcel(variables:URLVariables):void {
		//	flex.util.BeforeunloadManager.deregist();
			var u:URLRequest = new URLRequest("/mxAsset/servlet/ExportServlet");
			u.data = variables; 
			u.method = URLRequestMethod.POST; 
			navigateToURL(u,"_self");		
		//	flex.util.BeforeunloadManager.regist();
		}
		private function ExportToExcel1(variables:URLVariables):void {
			var u:URLRequest = new URLRequest("/mxAsset/servlet/ExportAllExcel");
			u.data = variables; 
			u.method = URLRequestMethod.POST; 
			navigateToURL(u,"_self");			
		}
	}
}