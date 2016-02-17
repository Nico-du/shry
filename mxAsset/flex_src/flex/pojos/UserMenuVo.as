package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.vo.UserMenuVo")]
	public class UserMenuVo
	{
		public function UserMenuVo()
		{
		}
		
		public var id:Number;
		public var sfAdd:String;
		public var sfModify:String;
		public var sfDelete:String;
		public var sfSearch:String;
		public var sfImport:String;
		public var sfExport:String;
		public var searchCondition:String;
		public var sfOther:String;
		public var ruleID:Number;
		public var menuID:Number;
		
	}
}