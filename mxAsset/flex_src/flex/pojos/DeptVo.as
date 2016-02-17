package flex.pojos
{
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias="net.chinanets.pojos.Dept")]
	public class DeptVo
	{
		public function DeptVo()
		{
		}
		
		public var  id:String;
		public var  mc:String;
		public var  parentid:DeptVo;
		public var  showOrder:Number;
		public var  bz:String;
		public var  userses:ArrayCollection;
		[Bindable]public var  selected:Boolean = false;
	}
}