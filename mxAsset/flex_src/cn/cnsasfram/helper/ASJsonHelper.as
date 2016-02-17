package cn.cnsasfram.helper{
	import cn.cnsasfram.entity.DoResult;
	
	import com.adobe.serialization.json.JSON;
	
	import mx.collections.ArrayCollection;

	/**
	 *FlexJSON辅助类 
	 * @author RLiuyong
	 */
	public class ASJsonHelper{
		
		public function ASJsonHelper(){
		}
		
		/**
		 * 分析JSON,并予以更正
		 * @param strJSON
		 * @return
		 */
		public static function AnsyJsonString(strJSON:String):String{
			if(ASStringHelper.isNullOrEmpty(strJSON)){
				return "";
			}
			if(strJSON.indexOf("[")==0){
				strJSON=strJSON.substr(1,strJSON.length-1);
			}
			if(strJSON.lastIndexOf("]")!=-1){
				strJSON=strJSON.substr(0,strJSON.length-1);
			}
			return strJSON;
		}
		
		/**
		 *将JSON结果信息转换为DoResult
		 */
		public static function stringJsonConverToDoResult(resultJson:String):DoResult{
			if(ASStringHelper.isNullOrEmpty(resultJson)){
				return null;
			}
			resultJson=AnsyJsonString(resultJson);
			var tempJson:Object=new Object();
			tempJson=JSON.decode(resultJson);
			var doResult:DoResult=new DoResult();
			doResult.nRetCode=parseInt(tempJson.nRetCode);
			doResult.strErrorInfo=tempJson.strErrorInfo;
			doResult.strErrorInfoRes=tempJson.strErrorInfoRes;
			doResult.strErrorInfoResArg=tempJson.strErrorInfoResArg;			
			return doResult;
		}
		
		/**
		 *将JSON结果信息转换为ArrayCollection 
		 */
		public static function stringJsonConverToArrayConllection(resultJson:String):ArrayCollection{
			return null;
		}
	}
}