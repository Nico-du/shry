package flex.pojos
{
	
	import flex.pojos.MeetingVo;
	import flex.pojos.UsersVo;
	import flex.pojos.TbTypeVo;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbMeeReseve")]	
	public class MeeReseveVo
	{
		public function MeeReseveVo()//会议
		{
		}
	public var id:String;
	public var tbType:TbTypeVo;//分类
	public var users:UsersVo;//操作人
	public var dept:Number;//操作部门
	public var deptName:String;
	public var tbRooms:MeetingVo;//会议资源  地址
	public var redate:Date;//操作时间
	public var bedate:Date;
	public var endate:Date;//结束时间
	public var message:String;//议程
	public var backfile:String;//附件
	public var meetingname:String;//会议名称
	public var cbrid:UsersVo;//承办人
	public var jyy:UsersVo;//纪要员
	public var mark:String;
	public var usable:Number;
	public var notefile:String;//纪要文件
	
	public var selected:Boolean=false;

	}
}