package cn.cnsasfram.helper{
	
	/**
	 *String辅助类
	 *@author dzj
	 */
	public class ASStringHelper{
		public function ASStringHelper(){
		}
		
		/**
		 *比较两个字符串是否相等-1代表不相等，0代表相等 
		 */
		public static function stringCompare(strOne:String,strTwo:String,bIgnoreCase:Boolean):int{
			if(strOne==null || strTwo==null){
				return -1;
			}
			if(bIgnoreCase){
				if(strOne.toLocaleUpperCase()==strTwo.toLocaleUpperCase()){
					return 0;
				}else{
					return -1;
				}
			}else{
				if(strOne==strTwo){
					return 0;
				}else{
					return -1;
				}
			}
		}
		
		/**
		 *判断字符串是否为空
		 */
		public static function isNullOrEmpty(strValue:String):Boolean{
			return stringLength(strValue)==0;
		}
		
		/**
		 *返回字符串长度 
		 */
		public static function stringLength(strValue:String):int{
			if(strValue==null){
				return 0;
			}
			return strValue.length;
		}
	}
}