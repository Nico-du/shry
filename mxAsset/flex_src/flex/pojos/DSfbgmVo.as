package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.DSfbgm")]
	public class DSfbgmVo
	{
		public function DSfbgmVo()
		{
		}
		private var _id:int;
	    private var _mc:String;
	    
	    public function get mc():String
	    {
	    	return _mc;
	    }

	    public function set mc(v:String):void
	    {
	    	_mc = v;
	    }


		public function get id():int
		{
			return _id;
		}

		public function set id(v:int):void
		{
			_id = v;
		}
	}
}