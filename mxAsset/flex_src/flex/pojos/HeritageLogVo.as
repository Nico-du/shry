package flex.pojos
{
	import flex.pojos.HeritageVo;
	import flex.pojos.UserVo;
	
	import mx.formatters.DateFormatter;
	
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.TbHeritageLog")]	
	public class HeritageLogVo
	{
		public function HeritageLogVo()
		{
		}
		
	public var logId:Number;
	public var tbHeritage:HeritageVo;
	public var logTypeId:String;
	public var logDate:Date;
	public var logUserId:UsersVo;
	public var logDetail:String;
	public var logBack:String;
	public var logAddress:String;
	public var logAmount:Number;
	
	public var logHername:String;
	public var logUser:String;
	public var logNum:String;
	public var selected:Boolean=false;

	}
}