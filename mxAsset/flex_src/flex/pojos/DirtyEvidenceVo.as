package flex.pojos
{
	//import mx.charts.chartClasses.DataDescription;
	import flex.pojos.DeptVo;
	import flex.pojos.TbTypeVo;
	import flex.pojos.UsersVo;
	
	import mx.formatters.DateFormatter;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbDirtyEvidence")]	
	public class DirtyEvidenceVo
	{
		public function DirtyEvidenceVo()
		{
		}
	public var dirtyevidenceId:Number;
	public var tbType:TbTypeVo;
	public var dirtyevidenceName:String;
	public var dirtyevidenceImg:String;
	public var dirtyevidenceAh:String;
	
	public var dirtyevidenceAddress:String;
	public var dirtyevidenceDetail:String;
	public var status:TbTypeVo;
	
	public var appendix:String;
	public var usable:Number;
	public var hirdate:Date;
	public var jzlx:String;
	public var jz:String;
	
	public var syrid:String;
	public var zrrid:String;
	public var bmid:String;
	public var gysid:String;
	
	/*public var bm:DeptVo;
	public var syr:UsersVo;
	public var zrr:UsersVo;*/
	
	public var selected:Boolean=false;

	}
}