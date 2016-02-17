package flex.pojos
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.SvSupplier")]
	public class SvSupplierVo
	{
		public function SvSupplierVo()
		{
		}
		public var id:Number;
		public var supplierName:String;
		public var legalName:String;
		public var legalPhone:String;
		public var linkmanName:String;
		public var linkmanPhone:String;
		public var supplierAddress:String;
		public var supplierComment:String;
		public var svCompacts:ArrayCollection;
		public var selected:Boolean = false;
	    
	}
}