package flex.pojos
{
	
	import flex.pojos.HeritageVo;
	import flex.pojos.SvSupplierVo;
	import flex.pojos.UsersVo;
	
	import mx.collections.ArrayCollection;
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbPurchase")]	
	public class PurchaseVo
	{
		public function PurchaseVo()
		{
		}
	public var id:Number;

	public var pname:String;
	
	public var pnameid:String;
	public var supplierid:String;
	/*public var users:UsersVo;
	public var svSupplier:SvSupplierVo;*/
	public var pdate:Date;
	public var price:Number;
	public var pdesc:String;
	public var purchaseNum:String;
	public var departid:Number;
	public var usable:Number;
	public var tbMark:TbTypeVo;
	public var buyStatus:TbTypeVo;
	public var deptName:String;
	public var selected:Boolean=false;

	}
}