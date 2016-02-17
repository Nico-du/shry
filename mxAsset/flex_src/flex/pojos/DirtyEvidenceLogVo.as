package flex.pojos
{
	//import mx.charts.chartClasses.DataDescription;

	import flex.pojos.DirtyEvidenceVo;
	import flex.pojos.UsersVo;
	import mx.formatters.DateFormatter;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbDirtyEvidenceLog")]	
	public class DirtyEvidenceLogVo
	{
		public function DirtyEvidenceLogVo()
		{
		}
			public var logId:Number;
			public var dirId:DirtyEvidenceVo;
			public var logDate:Date;
			public var logUserId:UsersVo;
			public var driTypeId:String;
			public var logDetail:String;
			public var logBack:String;
			public var logDirah:String;
			public var selected:Boolean=false;
	

	}
}