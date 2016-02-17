package cn.cnsasfram.entity.enum{
	/**
	 * 系统错误信息
	 * @author RLiuyong
	 *
	 */
	public class Errors{
		public function Errors(){
		}
		/**
		 * 成功，无错误
		 */
		public static var OK:int = 0;
		
		/**
		 * 内部发生错误
		 */
		public static var INTERNALERROR:int = 1;
		
		/**
		 * 访问被拒绝
		 */
		public static var ACCESSDENY:int = 2; 
		
		/**
		 * 无效的数据
		 */
		public static var INVALIDDATA:int = 3; 
		
		/**
		 * 无效的数据键
		 */
		public static var INVALIDDATAKEYS:int = 4;
		
		/**
		 * 输入的信息有误
		 */
		public static var INPUTERROR:int = 5;
		
		/**
		 * 重复的数据键值
		 */
		public static var DUPLICATEKEY:int = 6;
		
		/**
		 * 重复的数据
		 */
		public static var DUPLICATEDATA:int = 7;
		
		/**
		 * 删除限制
		 */
		public static var DELETEREJECT:int = 8;
		
		/**
		 * 逻辑处理错误
		 */
		public static var LOGICERROR:int = 9;
		
		/**
		 * 数据不匹配
		 */
		public static var DATANOTMATCH:int = 10; //数据不匹配
		
		/**
		 * 没有实现指定功能
		 */
		public static var NOTIMPL:int = 20; //没有实现指定功能
		
		/**
		 * 系统中不存在的用户CA
		 */
		public static var NOTEXISTUSERCA:int = 30; //系统中不存在的用户CA
		
		/**
		 * 用户自定义错误编号开始
		 */
		public static var USERERROR:int=1000; //用户错误从1000开始
		
		
		/**
		 * 判断返回值是否为用户自定义错误
		 * @param nErrorCode
		 * @return
		 */
		public static function isUserError(nErrorCode:int):Boolean{
			return nErrorCode>=USERERROR;
		}
		
		
		/**
		 * 获取错误的描述信息
		 * @param nErrorCode
		 * @return
		 */
		public static function getErrorInfo(nErrorCode:int):String{
			if(isUserError(nErrorCode))
				return "不明的用户自定义错误";
			
			switch(nErrorCode){
				case INTERNALERROR:
					return "系统内部发生错误";
				case ACCESSDENY:
					return "访问被拒绝，可能由于权限原因导致";
				case INVALIDDATA:
					return "数据不存在";
				case INVALIDDATAKEYS:
					return "数据的索引条件有误或不足";
				case INPUTERROR:
					return "数据的信息有误或不足";		
				case DUPLICATEKEY:
					return "重复的数据键";
				case DUPLICATEDATA:
					return "重复的数据";
				case DELETEREJECT:
					return "删除拒绝，可能由于权限原因导致";
				case LOGICERROR:
					return "逻辑处理错误";
				case DATANOTMATCH:
					return "数据不一致，可能后台数据已经被修改";
				case NOTIMPL:
					return "没有实现指定功能";
				case NOTEXISTUSERCA:
					return "系统中不存在的用户CA,请确认CA是否所属闵行法院,如确认请联系系统管理员进行数据同步.";
				default:
					return "不明错误";
			}
		}
	}
}