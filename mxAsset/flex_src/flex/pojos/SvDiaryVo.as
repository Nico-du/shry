package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.SvDiary")]
	public class SvDiaryVo
	{
		public function SvDiaryVo()
		{
		}
		public var id:Number;
		public var svPeople:SvPeopleVo;
		public var diaryContent:String;
		public var  diaryDate:Date;
		public var planContent:String;
		public var diaryCheckStatus:String;
		public var auditorId:String;
		public var auditorName:String;
		public var planCheckStatus:String;
		public var diaryComment:String;
		public var planComment:String;
		public var filePath:String;
		public var selected:Boolean = false;
		
	}
}