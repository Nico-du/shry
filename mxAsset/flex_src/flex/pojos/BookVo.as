package flex.pojos
{
	import flex.pojos.PurchaseVo;
	import flex.pojos.TbTypeVo;
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.BookModel")]
	public class BookVo{
		
		public function BookVo(){
			
		}
		
		public var id:int;//图书ID
		public var name:String;//图书名称
		public var bookno:String;//图书编号
		public var author:String;//图书作者
		public var type:DictionaryVo;//图书分类ID
		public var publisher:String;//出版社
		public var publishdate:Date;//出版日期
		public var stockcount:int;//库存数量
		public var brokencount:int;//损坏数量
		public var totalcount:int;//总数量
		public var singleprice:uint;//单价
		public var remarks:String;//其他说明
		public var inserttime:Date;//购买日期
		public var updatetime:Date;//购买日期
		
		public var tbtype:TbTypeVo;
		public  var purchaseId:PurchaseVo ;//采购编号
		public  var status:TbTypeVo ;//状态
		public var usable:Number ;//是否可用
		public var bookTypeName:String;
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