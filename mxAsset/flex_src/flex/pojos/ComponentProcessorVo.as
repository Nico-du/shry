package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.ComponentProcessor")]
	public class ComponentProcessorVo
	{
		public function ComponentProcessorVo()
		{
		}
			private var _id:int;
			private var _computerId:int;
			private var  _mc:String;
			private var _pl:String;
			private var _ppxh:String;
			private var _bz:String;
			private var _test:Boolean=false;

			public function get test():Boolean
			{
				return _test;
			}

			public function set test(v:Boolean):void
			{
				_test = v;
			}
			public function get bz():String
			{
				return _bz;
			}

			public function set bz(v:String):void
			{
				_bz = v;
			}

			public function get ppxh():String
			{
				return _ppxh;
			}

			public function set ppxh(v:String):void
			{
				_ppxh = v;
			}

			public function get pl():String
			{
				return _pl;
			}

			public function set pl(v:String):void
			{
				_pl = v;
			}

			public function get mc():String
			{
				return _mc;
			}

			public function set mc(v:String):void
			{
				_mc = v;
			}

			public function get computerId():int
			{
				return _computerId;
			}

			public function set computerId(v:int):void
			{
				_computerId = v;
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