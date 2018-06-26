package flex.pojos
{
	[Bindable]
	[RemoteClass(alias="net.chinanets.pojos.ShryUploadfileData")]
	public class FileUploadVo
	{
		public function FileUploadVo()
		{
		}
	public var  docid:Number;
	public var   uploaddate:String;
	public var   filename:String;
	public var   filetype:String;
	public var   filepath:String;
	public var   filesize:String;
	public var   tablename:String;
	public var   columnname:String;
	public var   dataid:String;
	public var   datatype:String = "1";//数据类型(图示图片:1,原始数据图:2)
	public var   sydid:String;
	public var   memo:String;

	}
}