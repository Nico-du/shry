package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.ConsumePurchaseModel")]
	public class ConsumePurchaseVo{
		
		public function ConsumePurchaseVo(){
			
		}
		
		public var id:int;//
		public var category:DictionaryVo;//类别ID
		public var assetNo:String;//资产标识
		public var purchaseCount:int;//购买数量
		public var singlePrice:Number;//购买单价
		public var purchaseDate:Date;//购买日期
		public var supplier:SvSupplierVo;//供应商
		public var purchaseDept:String;//采购部门
		public var remarks:String;//其他说明
		public var insertTime:Date;//添加日期
		public var updateTime:Date;//更新日期
		
	    public var selected:Boolean = false;
		
		
	}
}