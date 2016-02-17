package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.FurnitureModel")]
	public class FurnitureVo{
		
		public function FurnitureVo(){
			
		}
		
		public var id:int;//
		public var bh:String;//
		public var brand:String;//品牌
		public var category:DictionaryVo;//类别ID
		public var version:String;//型号
		public var assetNo:String;//资产标识
		public var color:String;//颜色
		public var material:String;//材质
		public var measure:String;//尺寸
		public var functionType:DictionaryVo;//功能（空调机：中央空调，立柜式空调；风扇：落地扇，台扇...）
		public var stockCount:int;//库存数量
		public var brokenCount:int;//损毁数量
		public var totalCount:int;//总数量
		public var singlePrice:Number;//购买单价
		public var remarks:String;//其他说明
		public var insertTime:Date;//添加日期
		public var updateTime:Date;//更新日期
		
		public var tbtype:TbTypeVo;
		public  var purchaseId:PurchaseVo ;//采购编号
		public  var status:TbTypeVo ;//状态
		public var usable:Number ;//是否可用
		
		public var gzrq:Date;
		public var rkrq:Date;
		public var qyrq:Date;
		public var bxq:Date;
		
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
		
	    public var selected:Boolean = false;
		
		
	}
}