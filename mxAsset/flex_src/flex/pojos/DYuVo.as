package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.DYu")]
	public class DYuVo
	{
		public function DYuVo()
		{
		}
		private var _id:int;
	    private var _dm:String;
	    private var _mc:String;
	    
	    public function get mc():String
	    {
	    	return _mc;
	    }

	    public function set mc(v:String):void
	    {
	    	_mc = v;
	    }

	    public function get dm():String
	    {
	    	return _dm;
	    }

	    public function set dm(v:String):void
	    {
	    	_dm = v;
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