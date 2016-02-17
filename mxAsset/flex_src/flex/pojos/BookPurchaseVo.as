package flex.pojos
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.BookPurchaseModel")]
	public class BookPurchaseVo{
		
		public function BookPurchaseVo(){
			
		}
		public var id:int;
		public var book:BookVo;
		public var purchasecount:int;
		public var singleprice:Number;
		public var purchasedate:Date;
		public var supplier:SvSupplierVo;
		public var purchasedept:String;
		public var remarks:String;
		public var inserttime:Date;
		public var updatetime:Date;

	    public var selected:Boolean = false;
		
		
	}
}