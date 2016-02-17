package flex.util
{
	import mx.validators.Validator;
	import mx.validators.ValidationResult;

	public class IpValidator extends Validator
	{
		public function IpValidator()
		{
			super();
		}
		
		private var _ipInvalid:String="IP地址不正确";
  		private var _ipInvalidCode:String='10001';
		protected var ip:RegExp =  /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/
		
		//存取器
		public function get ipInvalidError():String {
			return _ipInvalid;
		}
	 	public function set ipInvalidError(invalidString:String):void {
	 		this._ipInvalid = invalidString;
	 	}
	  	
	  	//验证
		private function doIpValidation(validator:IpValidator,
				value:Object,baseField:String):Array {
			var rs:Array = [];
			if(!ip.test(String(value))){
				rs.push(new ValidationResult(true,baseField,validator._ipInvalidCode,validator._ipInvalid));
			}
			return rs;
		}
		//覆盖原始验证
		override protected function doValidation(value:Object):Array {
			var rs:Array = super.doValidation(value);
			var val:String = value ? String(value) : '';
			if(rs.length >0 || ((val.length == 0) && !required) ){
				return rs;
			}
			return doIpValidation(this,value,null);
		}

	}
}