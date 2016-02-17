package flex.pojos
{
	
	import flex.pojos.MeeReseveVo;
	
	import flex.pojos.UsersVo;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbAtteeden")]	
	public class AtteedenVo
	{
		public function AtteedenVo()//会议出席者
		{
		}
	public var id:String;

	public var userid:UsersVo;//操作人
	public var username:String;
	public var dept:Number;//操作部门
	public var remark:String;//会议回执
	public var reserveid:MeeReseveVo;
	public var deptName:String;
	public var usable:Number;

	
	public var selected:Boolean=false;

	}
}