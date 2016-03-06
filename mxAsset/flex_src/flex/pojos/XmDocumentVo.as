package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.XmDocument")]
	public class XmDocumentVo
	{
		public function XmDocumentVo()
		{
		}
	public var id:Number;
	public var docMc:String;
	public var docRq:String;
	public var docParentid:Number;
	public var docType:String;
	public var docNr:String;
	public var docParenttable:String;
	public var fjColumnname:String;
	public var docSize:String;

	}
}