package flex.pojos
{
	//import mx.charts.chartClasses.DataDescription;
	import flex.pojos.PurchaseVo;
	import flex.pojos.TbTypeVo;
	import flex.pojos.UsersVo;
	import mx.formatters.DateFormatter;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbHeritage")]	
	public class HeritageVo
	{
		public function HeritageVo()
		{
		}
	public var heritageId:Number;
	public var heritageTypeId:TbTypeVo;
	public var heritageName:String;
	public var heritageNum:String;
	public var heritageSum:Number;
	public var heritageImg:String;
	public var heritageDetail:String;
	public var heritageAddress:String;
	public var heritageBack:String;
	
	
	public var spec:TbTypeVo;//规格
	public var status:TbTypeVo;
	public  var herdate:Date;//改变状态时间
	public var appendix:String;
	public var usable:Number;
	public var purchaseId:PurchaseVo;
	
	public var userss:Number;
	public var usersname:String;
	
	public var gzrq:Date;
	public var rkrq:Date;
	public var qyrq:Date;
	public var bxq:Date;
	public var userids:Number; 
	
	public var jzlx:String;
	public var jz:String;
	
	public var syrid:String;
	public var zrrid:String;
	public var bmid:String;
	public var gysid:String;
	
	/*public var syr:UsersVo;
	public var zrr:UsersVo;
	public var bm:DeptVo;
	public var gys:SvSupplierVo;*/
	
	public var selected:Boolean=false;

	}
}