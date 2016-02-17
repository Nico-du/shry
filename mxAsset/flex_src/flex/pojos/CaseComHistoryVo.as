package flex.pojos
{
	//import mx.charts.chartClasses.DataDescription;
	import mx.formatters.DateFormatter;
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.CaseComHistory")]	
	public class CaseComHistoryVo
	{
		public function CaseComHistoryVo()
		{
		}
	public var id:Number;
	public var deviceId:String;
	public var tempDeviceId:String;
	public var computerName:String;
	public var cpuName:String;
	public var hdSn:String;
	public var hdSn1:String;
	public var hdSn2:String;
	public var hdSn3:String;
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
	public var memorySize1:String;
	public var memorySize2:String;
	public var memorySize3:String;
	public var memoryModel:String;
	public var hdModel:String;
	public var hdModel1:String;
	public var hdModel2:String;
	public var hdModel3:String;
	public var hdSize:String;
	public var hdSize1:String;
	public var hdSize2:String;
	public var hdSize3:String;
	public var motherboardModel:String;
	public var motherboardName:String;
	public var motherboardSn:String;
	public var macAddress:String;
	public var ipAddress:String;
	public var graphicsModel:String;
	public var graphicsMemorySize:String;
	public var collectDate:String;
	public var cdRomDrive:String;
	public var cdRomDrive1:String;
	public var isNewItem:String;
	public var status:String = "老设备，无更新";//设备的状态，取值为：新设备、老设备，无更新，老设备，有更新。默认为老设备，无更新

	}
}