package flex.pojos
{
	import flex.pojos.TbTypeVo;
	import flex.pojos.UsersVo;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbCar")]	
	public class CarVo
	{
		public function CarVo()//会议安排资源
		{
		}
		
		public var id:String;
		public  var carKpbh:String;//卡片编号
		public var carYzcbh:String;//原资产编号
		public var carZcflmc:TbTypeVo;//资产分类名称
		public var carZcmc:String;//资产名称
		public var carYsxmbh:String;//预算项目编号
		public var  carSyzk:TbTypeVo;//使用状况
		public var carCgzzxs:TbTypeVo;//采购组织形式
		public var carQdfs:TbTypeVo;//取得方式
		public var carClyt:TbTypeVo;//车辆用途
		public var carSyxz:String;//使用性质
		public var carPp:String;//品牌
		public var carGgxh:String;//规格型号
		public var carClcd:String;//车辆产地
		public var carClsbh:String;//车辆识别号
		public var carHpzl:TbTypeVo;//号牌种类
		public var carCph:String;//车牌号
		public var carPql:Number;//排气量（升）
		public  var carFdjh:String;//发动机号
		public var carHtbh:String;//合同编号
		public var carFph:String;//发票号
		public var carBzqk:TbTypeVo;//编制情况
		public var carGlbm:DeptVo;//管理部门
		public var carGlr:UsersVo;//管理人
		public var carJzlx:TbTypeVo;//价值类型
		public var carJz:Number;//价值（元）
		public  var carCcrq:Date;//出厂日期
		public var carYslczxzj:Number;//部门预算内-财政性资金
		public  var carRzrq:Date;//财务入账日期
		public  var carYslfczxzj:Number;//部门预算内-非财政性资金
		public  var carKjpzh:String;//会计凭证号
		public var carYswczxzj:Number;//部门预算外-财政性资金
		public var carZjzt:TbTypeVo;//折旧状态
		public var carYswfczxzj:Number;//部门预算外-非财政性资金
		public var carXss:String;//销售商
		public  var carSyrq:Date;//投入使用日期
		public var carZmjz:Number;//账面净值
		public var carBxjzrq:Date;//保修截止日期
		public var carMark:String;//备注
		public var carImg:String;//照片
		public var carYjsynx:Number;//预计使用年限（月）
		public  var carDate:Date;//操作时间
		public var  carZcrq:Date;//车辆行驶证注册日期
	public var usable:Number;
	
	public var selected:Boolean=false;

	}
}