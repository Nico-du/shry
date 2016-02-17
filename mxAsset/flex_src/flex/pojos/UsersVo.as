package flex.pojos
{
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.Users")]
	public class UsersVo{
		
		public function UsersVo(){
			
		}
		public var id:Number;
		public var mc:String;
		public var mm:String;
		public var zsxm:String;
		public var bmmc:String;
		public var cjsj:String;
		public var isleader:String;
		public var iswfshr:String;
		public var zwid:String;	//职位ID
		public var zwmc:String;	//职位名称
		public var zwlbmc:String;	//职位类别名称
		public var showOrder:Number;
		public var depts:ArrayCollection = new ArrayCollection();
		public var ruleses:ArrayCollection = new ArrayCollection();
		public var usersMenuses:ArrayCollection = new ArrayCollection();
		
		public var selected:Boolean = false;
		
		
	}
}