package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.BookBorrowModel")]
	public class BookBorrowVo{
		
		public function BookBorrowVo(){
			
		}
		
		public var id:int;//借阅ID
		public var book:BookVo;//图书ID
		public var user:UsersVo;//借阅人ID
		public var beginDate:Date;//借阅日期
		public var endDate:Date;//归还日期
		public var remarks:String;//备注信息
		public var inserTime:Date;//添加日期
		public var updateTime:Date;//更新日期
		public var status:int;//状态 0：借阅，1：退还
		
	    public var selected:Boolean = false;
		
		
	}
}