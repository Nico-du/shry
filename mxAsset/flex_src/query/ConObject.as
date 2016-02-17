package query
{
	public class ConObject
	{
		public var name:String;
		public var type:String;
		public var column:String;
		
		public function ConObject(_name:String,_type:String,_column:String)
		{
			name = _name;
			type = _type;
			column = _column;
		}
		
		public function toString():String {
			return "";
		}
		
	}
}