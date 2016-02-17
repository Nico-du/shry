package cn.cnsasfram.helper{
	/**
	 *日期帮助类
	 * @author RLiuyong 
	 */
	public class ASDateHelper{
		
		public function ASDateHelper(){
		}
		
		/**
		 *将字符串转换为日期 
		 */
		public static function GetDateByString(dateStr:String):Date{
			try{
				return new Date(dateStr);
			}catch(e:Error){
				return null;
			}
			return null;
		}
	}
}