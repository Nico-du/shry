package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.AssetJzPancha")]
	public class JzPcVo
	{
		public function JzPcVo()
		{
		}
		
		public var id:Number; //介质检查ID
		public var sbbh:String; //介质设备编号
		public var ccsjmj:String; //存储数据的密级属性。（安全保密、非安全保密介质共有属性）
		public var ljjsjmj:String; //连接计算机的密级属性
		public var sbsfwh:String; //设备是否完好
		public var bgsfaq:String;	//保管是否安全
		public var sfxdwc:String; //是否携带外出
		public var wcxdjl:String;  //是否有外出携带审批或归还记录
		public var sfgrbd:String;	//是否感染病毒
		public var bz:String;	//备注	
		public var pcrq:String;	//检查日期
		public var pcr:String;	//检查人
		public var pcdw:String;	//检查单位
		public var sfgrmm:String;	//是否感染木马
		public var qtjcxm:String;	//其他检查项目
		public var jccs:Number;
	}
}