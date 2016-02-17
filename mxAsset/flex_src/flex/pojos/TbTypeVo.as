package flex.pojos
{
	//import mx.charts.chartClasses.DataDescription;
	import mx.formatters.DateFormatter;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbType")]	
	public class TbTypeVo
	{
		public function TbTypeVo()
		{
		}
	public var typeId:Number;
	public var typeName:String;
	public var typeMark:String;
	public var typeBack:String;
	public var usable:Number;
	public var gzNumber:String;
	public var selected:Boolean=false;

	}
}