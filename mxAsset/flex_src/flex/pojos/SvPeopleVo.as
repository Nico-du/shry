package flex.pojos
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.SvPeople")]
	public class SvPeopleVo
	{
		public function SvPeopleVo()
		{
		}
		public var id:Number;
		public var PName:String;
		public var PCompany:String;
		public var PPosition:String;
		public var jobContent:String;
		public var dutyProject:String;
		public var dutyHardware:String;
		public var dutyEvent:String;
		public var contactWay:String;
		public var workTime:String;
		public var PComment:String;
		public var svDiaries:ArrayCollection;
		public var selected:Boolean = false;
	}
}