package cn.cnsasfram.entity{
	
	import cn.cnsasfram.entity.enum.Errors;
	
	
	/**
	 *自定义FLEX中错误信息类
	 *@author RLiuyong
	 */
	public class DoResult{
		
		private var _nRetCode:int = Errors.OK;
		
		private var _strErrorInfo:String = "";
		
		private var _strErrorInfoRes:String = "";
		
		private var _strErrorInfoResArg:String = "";
		
		private var _userObject:Object = null;
		
		public function DoResult(){
		}

		/**
		 * 返回代码
		 */
		public function get nRetCode():int{
			return _nRetCode;
		}

		/**
		 * @设置返回代码
		 */
		public function set nRetCode(value:int):void{
			_nRetCode = value;
		}

		/**
		 * 错误信息
		 */
		public function get strErrorInfo():String{
			return _strErrorInfo;
		}

		/**
		 * @设置错误信息
		 */
		public function set strErrorInfo(value:String):void{
			_strErrorInfo = value;
		}

		/**
		 * 错误信息语言资源编号
		 */
		public function get strErrorInfoRes():String{
			return _strErrorInfoRes;
		}

		/**
		 * @设置错误信息语言资源编号
		 */
		public function set strErrorInfoRes(value:String):void{
			_strErrorInfoRes = value;
		}

		/**
		 * 错误信息语言资源参数
		 */
		public function get strErrorInfoResArg():String{
			return _strErrorInfoResArg;
		}

		/**
		 * @设置错误信息语言资源参数
		 */
		public function set strErrorInfoResArg(value:String):void{
			_strErrorInfoResArg = value;
		}

		/**
		 * 用户对象
		 */
		public function get userObject():Object{
			return _userObject;
		}

		/**
		 * @设置用户对象
		 */
		public function set userObject(value:Object):void{
			_userObject = value;
		}
	}
}