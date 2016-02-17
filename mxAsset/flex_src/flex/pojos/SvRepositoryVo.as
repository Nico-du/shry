package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.SvRepository")]
	public class SvRepositoryVo
	{
		public function SvRepositoryVo()
		{
		}
		public var id:Number;
		public var issueTitle:String;
		public var issueType:String;
		public var issueDescribe:String;
		public var issueKeyword:String;
		public var issueComment:String;
		public var handleMethod:String;
		public var handleComment:String;
		public var addDate:Date;
		public var issueLevel:String;
		public var selected:Boolean = false;
	}
}