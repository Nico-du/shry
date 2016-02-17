package flex.pojos
{
	import mx.messaging.channels.StreamingAMFChannel;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.vo.JzPanChaVo")]
	public class JzPanchaVo
	{
		public function JzPanchaVo()
		{
		}
		
		public var id:Number;
		public var mc:String;
		public var bmmc:String;
		public var bmId:String;
		public var sbbh:String;
		public var qysj:String;
		public var syr:String;
		public var syrId:Number;
		public var sydd:String;
		public var zrr:String;
		public var zrrId:Number;
		public var lxdh:String;
		public var smsx:String;
		public var zyyt:String;
		public var lx:String;
		public var pp:String;
		public var xh:String;
		public var xlh:String;
		public var bz:String;
		public var smzqzt:String;
		public var gzh:String;
		public var jzPcId:Number; //介质检查ID
		public var ccsjmj:String; //存储数据的密级属性。（安全保密、非安全保密介质共有属性）
		public var ljjsjmj:String; //连接计算机的密级属性
		public var sbsfwh:String; //设备是否完好
		public var bgsfaq:String;	//保管是否安全
		public var sfxdwc:String; //是否携带外出
		public var wcxdjl:String;  //是否有外出携带审批或归还记录
		public var sfgrbd:String;	//是否感染病毒
		public var pcbz:String;	//备注	
		public var pcrq:String;	//检查日期
		public var pcr:String;	//检查人
		public var pcdw:String;	//检查单位
		public var sfgrmm:String;	//是否感染木马
		public var qtjcxm:String;	//其他检查项目
		public var jccs:Number;//检查次数
		public var selected:Boolean = false;
	}
}