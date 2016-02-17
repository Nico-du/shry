package flex.pojos
{
	
	import flex.pojos.TbTypeVo;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbRooms")]	
	public class MeetingVo
	{
		public function MeetingVo()
		{
		}
	public var roomId:String;
	public var roomName:String;//会议资源名称
	public var roomNum:String;//会议室门牌号
	public var maxper:Number;
	public var tbType:TbTypeVo;
	public var roomInfor:String;//信息说明
	public var roomBack:String;
	public var uniNum:String;
	public var model:TbTypeVo;//会议室型号，只有会议室才有型号
	public var address:TbTypeVo;
	public var dept:Number;
	public var project:UsersVo;//负责人
	public var usable:Number;
	public var applymeet:String;
	public var deptName:String;
	public var roomDate:Date;
	
	
	
	public var selected:Boolean=false;
	
	}
}