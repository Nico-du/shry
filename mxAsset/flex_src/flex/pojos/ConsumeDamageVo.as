package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.ConsumeDamageModel")]
	public class ConsumeDamageVo{
		
		public function ConsumeDamageVo(){
			
		}
		
		public var id:int;//
		public var category:DictionaryVo;//类别ID
		public var assetNo:String;//资产标识
		public var damageCount:int;//损毁数量
		public var damageTime:Date;//损毁日期
		public var remarks:String;//其他说明
		public var insertTime:Date;//添加日期
		public var updateTime:Date;//更新日期
		public var consumeStockCount:int;//库存数量
		
	    public var selected:Boolean = false;
		
		
	}
}