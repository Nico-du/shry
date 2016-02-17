package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.FurnitureApplyModel")]
	public class FurnitureApplyVo{
		
		public function FurnitureApplyVo(){
			
		}
		
		public var id:int;//
		public var category:DictionaryVo;//类别ID
		public var assetNo:String;//资产标识
		public var applyCount:int;//申请数量
		public var applier:String;//申请人
		public var appliyTime:Date;//申请日期
		public var remarks:String;//其他说明
		public var insertTime:Date;//添加日期
		public var updateTime:Date;//更新日期
		public var furnitureStockCount:int;//库存数量
		
	    public var selected:Boolean = false;
		
		
	}
}