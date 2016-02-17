package flex.pojos
{
	//import mx.charts.chartClasses.DataDescription;
	import mx.formatters.DateFormatter;
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.ComputerInfo")]	
	public class ComputerInfoVo
	{
		public function ComputerInfoVo()
		{
		}
	public var id:Number;
	public var deviceId:String;
	public var computerName:String;
	public var cpuName:String;
	public var hdSn:String;
	public var systemPatch:String;
	public var antivirusName:String;
	public var antivirusVersion:String;
	public var s360Version:String;
	public var osVersion:String;
	public var cpuModel:String;
	public var cpuCore:String;
	public var level2Cache:String;
	public var cpuSocket:String;
	public var cpuNum:String;
	public var biosModel:String;
	public var memorySize:String;
	public var memoryModel:String;
	public var hdModel:String;
	public var hdSize:String;
	public var motherboardModel:String;
	public var motherboardName:String;
	public var motherboardSn:String;
	public var macAddress:String;
	public var ipAddress:String;
	public var graphicsModel:String;
	public var graphicsMemorySize:String;
	public var collectDate:String;

	}
}