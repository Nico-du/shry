package flex.pojos
{
	
	import flex.pojos.MeetingVo;
	import flex.pojos.TbTypeVo;
	import flex.pojos.UsersVo;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbPlan")]	
	public class PlanVo
	{
		public function PlanVo()//会议安排资源
		{
		}
		
		
	public var id:String;
	public var roomsid:MeetingVo;
	public var address:String;
	
	public var typeid:TbTypeVo;//分类
	public var price:Number;
	public var amount:Number;
	public var charges:Number;//应收费用
	public var realcharge:Number;//实收费用
	
	public var dept:Number;//操作部门
	public var deptName:String;
	public var userid:UsersVo;
	public var useridupd:UsersVo;
	public var plandate:Date;//创建时间时间
	public var upddate:Date;//修改时间
	public var usable:Number;
	public var reserveid:MeeReseveVo;
	
	public var selected:Boolean=false;

	}
}