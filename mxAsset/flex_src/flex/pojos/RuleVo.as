package flex.pojos
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.Rules")]
	public class RuleVo
	{
		
		public function RuleVo(){
			
		}
		
		public var id:Number;
		public var mc:String;
		public var bz:String;
		public var yxj:String;
		public var bmid:String;
		public var bmmc:String;
		public var kmcz:String;//科目操作
		public var userId:Number;//对应的代表该角色权限的用户
		public var userses:ArrayCollection;
		public var rulesMenuses:ArrayCollection;
		public var selected:Boolean = false;
		
	}
}