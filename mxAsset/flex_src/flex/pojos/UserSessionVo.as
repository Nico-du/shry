package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.vo.UserVo")]
	public class UserSessionVo{
		
		public function UserSessionVo(){
			
		}
		
		public var id:Number;
		public var deptId:String;	//所属部门ID
		public var deptName:String;//所属部门名称
		public var userName:String;//真实姓名
		public var ruleYxj:String;//角色优先级
		public var ruleId:String;//角色ID
		public var ruleName:String;//角色名称组
		public var mc:String;	//帐号
		public var mm:String;	//密码
		public var isLeader:String;	//是否部门领导
		public var isShr:String;	//是否流程审核人
		public var zwid:String;	//职位ID
		public var zwmc:String;	//职位名称
		public var zwlbmc:String;	//职位类别名称
		public var bmid:String;//可以查看部门ID组，逗号分隔
		public var bmmc:String;//可以查看部门名称组,逗号分隔
		
		public var selected:Boolean = false;
		
		
	}
}