package flex.util
{
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.net.navigateToURL;

	public class ExportWord
	{
		public function ExportWord()
		{
			super();
		}
		/*
		public function ExportPcToWord():void {
			var variables:URLVariables = new URLVariables();
			variables.syr = "Abin";
			variables.zrr = "[上海]";
		} 
		*/
		public function ExportToWord(variables:URLVariables,url:String):void {
			//var u:URLRequest = new URLRequest("/asset/exportTemplate/template.jsp");
			var u:URLRequest = new URLRequest(url);
			u.data = variables; 
			u.method = URLRequestMethod.POST; 
			navigateToURL(u,"_self");			
		}
	}
}