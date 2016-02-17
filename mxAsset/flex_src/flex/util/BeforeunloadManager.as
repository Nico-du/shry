package flex.util
{
	import flash.external.ExternalInterface;
	/**
	 *刷新Flex页面提示离开 
	 **/
	public class BeforeunloadManager
	{
		static private const javascriptdereg:XML = 
			<script>
			<![CDATA[
			function()
			{
			window.onbeforeunload = null;
			}
		        ]]>
			</script>;
		
		static private const javascript:XML = 
			<script>
			<![CDATA[
			function()
			{				
			var beforeunload = {
			init: function () {
			window.onbeforeunload = beforeunload.onbeforeunload_handler;  
			window.onunload = beforeunload.onunload_handler;  
			},
			/**
			*  it will be called before Going to new page
			*/
			onbeforeunload_handler: function(){  
			var warning="离开本页面将丢失一切未保存的工作,确认退出?";          
			return warning;  
			},
			onunload_handler: function() {
		//	var warning="谢谢光临23";  
		//	alert(warning);  
			}
			}
			beforeunload.init();
			}
			]]>
	</script>;
		
		public function BeforeunloadManager()
		{
			return;
		}
		
		static public function regist() : Boolean
		{
			if (ExternalInterface.available)
			{
				ExternalInterface.call(javascript);
			}// end if
			return true;
		}
		
		static public function deregist() : Boolean
		{
			if (ExternalInterface.available)
			{
				ExternalInterface.call(javascriptdereg);
			}// end if
			return true;
		}
		
	}
}