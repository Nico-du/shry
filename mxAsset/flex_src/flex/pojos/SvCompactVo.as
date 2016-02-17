package flex.pojos
{
	import mx.controls.DateField;

	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.SvCompact")]
	public class SvCompactVo
	{
		public function SvCompactVo()
		{
		}
		public var id:Number;
		public var svSupplier:SvSupplierVo;
		public var signingDate:Date;
		public var serverEnddate:Date;
		public var serverContent:String;
		public var serverPeopleName:String;
		public var serverPeoplePhone:String;
		public var serverPeopleComment:String;
		public var signingComment:String;
		public var signingName:String;
		public var selected:Boolean = false;
		
		/*public function getSigningDate():String {
			return DateField.dateToString(signingDate,"YYYY-MM-DD");
		}
		
		public function setSigningDate(signingDate:String):void {
			this.signingDate = DateField.stringToDate(signingDate,"YYYY-MM-DD");
		}
		
		public function getServerEnddate():String {
			return DateField.dateToString(serverEnddate,"YYYY-MM-DD");
		}
		
		public function setServerEnddate(serverEnddate:String):void {
			this.serverEnddate = DateField.stringToDate(serverEnddate,"YYYY-MM-DD");
		}*/
	}
}