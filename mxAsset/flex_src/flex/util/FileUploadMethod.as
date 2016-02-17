import flash.events.DataEvent;
import flash.events.Event;
import flash.events.IOErrorEvent;
import flash.events.ProgressEvent;
import flash.net.FileFilter;
import flash.net.FileReference;
import flash.net.FileReferenceList;

import flex.util.CommonXMLData;

import mx.collections.ArrayCollection;
import mx.managers.PopUpManager;

import upGrade.MyProgressBar;

/**
 *添加界面上传文件的方法 
 **/
[Bindable]
private var selectedFiles:ArrayCollection = new ArrayCollection();

public var uploadFileList:FileReferenceList=null;
private function select():void
{   
	// uploadFile= new FileReference();
	selectedFiles = new ArrayCollection();
	uploadFileList = new FileReferenceList();
	var imagesFilter:FileFilter = new FileFilter("图片类型(*.jpg;*.gif;*.png;*.bmp)", "*.jpg;*.gif;*.png;*.bmp");
	var docFilter:FileFilter = new FileFilter("文档类型(*.pdf;*.doc;*.docx;*.txt;*.xls;*.xlsx;)", "*.pdf;*.doc;*.docx;*.txt;*.xls;*.xlsx;");
	var fileFilter:FileFilter = new FileFilter("文件类型(*.*;)", "*.*;");
	uploadFileList.addEventListener(Event.SELECT,selectHandler);
	uploadFileList.addEventListener(IOErrorEvent.IO_ERROR,ioErrorHandler);
	try
	{
		uploadFileList.browse([docFilter,imagesFilter,fileFilter]);
	}catch(error:Error)
	{
		trace(error);
	}
}
public function validateFileSize(){
	
	
	
}


private function selectHandler(evt:Event):void
{
	if(CommonMethod.validateFileUpload((uploadFileList.fileList.length + selectedFiles.length ),uploadFileList)){
		for each(var file:FileReference in uploadFileList.fileList){
			selectedFiles.addItem(file);
		}
		setFileName();
	}
}

private function setFileName():void{
	var fileName:String = "";
	for each(var f: FileReference in selectedFiles){
		fileName += "\""+f.name +"\" "; 
	}
	fj.text = fileName;
}
private var isEdit:Boolean = false;
private var fileCount:int;
private var uploadCount:int;
var progressBar:MyProgressBar = null;
private function uploadFile(id:Object,fjColumnname:String=null):void{
	fileCount = selectedFiles.length;
	uploadCount = 0;
	if(fileCount > 0){
		progressBar = new MyProgressBar();
		PopUpManager.addPopUp(progressBar,this.parent as DisplayObject,true);
		PopUpManager.centerPopUp(progressBar); 
		for each(var f: FileReference in selectedFiles){
			try{
				//为了防止request内的data丢失每次要new一个新的url
				var data:URLVariables = new URLVariables();
				data.parentID= id;
				data.fjColumnname = fjColumnname;
				data.parentTable = docTableName;
				var request:URLRequest= new URLRequest("/mxAsset/servlet/ClobServlet");
				request.data=data;
				
				f.upload(request);//uploadCompleteData
				f.addEventListener(DataEvent.UPLOAD_COMPLETE_DATA,uploadCompleteDataBack);
				f.addEventListener( ProgressEvent.PROGRESS, progressBar.onProgress );
			}catch(error:Error)
			{
				Alert.show(error.message);
			}
		}
	}else{
		saveSucessBack(null);
	}
}

private function uploadCompleteDataBack(evt:DataEvent):void
{
	if(evt.text=="success"){
		uploadCount++;
		//	Alert.show("上传成功！","信息提示");
		//	PopUpManager.removePopUp(this);
		//	parentWindow.init();parentWindow.newDoc();
	}
	if(uploadCount==fileCount){
		PopUpManager.removePopUp(progressBar);
		saveSucessBack(null);
	}
}
private function validateSubmit(id:int):void{
	if(uploadCount!=fileCount){
		//回滚 1. 删除文档表中xmid 为id的数据
		//2.删除项目申请表中id 为id的数据 
		//	commonService.deleteByHql("delete from XmDocument where xmid="+id);
		Alert.show("validate ...");
	}
}
private function ioErrorHandler(ioEvent:IOErrorEvent):void {
}

public function deleteFile(obj:FileReference):void{
	var index:int=selectedFiles.getItemIndex(obj);
	selectedFiles.removeItemAt(index);
	selectedFiles.refresh();
}

private function resetForm():void {
	fj.text = "请选择文件。。。";
	selectedFiles.removeAll();
}