package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.ConsumeModel")]
	public class ConsumeVo{
		
		public function ConsumeVo(){
			
		}
		
		public var id:int;//
		public var brand:DictionaryVo;//品牌ID
		public var category:DictionaryVo;//类别ID
		public var version:String;//型号
		public var assetNo:String;//资产标识
		public var color:String;//颜色
		public var material:String;//材质
		public var measure:String;//尺寸
		public var stockCount:int;//库存数量
		public var brokenCount:int;//损毁数量
		public var totalCount:int;//总数量
		public var singlePrice:Number;//购买单价
		public var remarks:String;//其他说明
		public var insertTime:Date;//添加日期
		public var updateTime:Date;//更新日期
		
		
		public  var purchaseId:PurchaseVo ;//采购编号
		public  var status:TbTypeVo ;//状态
		public var usable:Number ;//是否可用
	    public var selected:Boolean = false;
		
		
	}
}