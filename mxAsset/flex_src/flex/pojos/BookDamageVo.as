package flex.pojos
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.BookDamageModel")]
	public class BookDamageVo{
		
		public function BookDamageVo(){
			
		}
		
		public var id:int;
		public var book:BookVo;
		public var damageCount:int;
		public var damageDate:Date;
		public var remarks:String;
		public var insertTime:Date;
		public var updateTime:Date;

	    public var selected:Boolean = false;
		
		
	}
}