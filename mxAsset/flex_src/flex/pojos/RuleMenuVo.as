package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.vo.RuleMenuVo")]
	public class RuleMenuVo
	{
		public function RuleMenuVo()
		{
		}
		
		public var id:Number;
		public var sfAdd:String;
		public var sfModify:String;
		public var sfDelete:String;
		public var sfSearch:String;
		public var sfImport:String;
		public var sfExport:String;
		public var ruleID:Number;
		public var menuID:Number;
		
	}
}