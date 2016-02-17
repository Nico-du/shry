package flex.pojos
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.DictionaryModel")]
	public class DictionaryVo{
		
		public function DictionaryVo(){
			
		}
		
		public var id:int;
		public var name:String;
		public var pId:int;
		public var pName:String;
		public var status:int;
		public var remarks:String;
		public var children:ArrayCollection;

	    public var selected:Boolean = false;
		
		
	}
}