
import flex.util.CommonXMLData;

public function loadPictures():void{
	CommonMethod.loadTablePictures(this.tempObjBeanName,idColumnName,dataId,this,loadPicturesBack);
}
private function loadPicturesBack(result:Object):void{
	fyts.removeAllChildren();
	var existedFiles:ArrayCollection = result as ArrayCollection;
	if( !(existedFiles == null || existedFiles.length < 1)){
		for(var idx:int=0;idx<existedFiles.length;idx++){
			var eachObj:Object = existedFiles.getItemAt(idx);
			var eachImg:Image = new Image();
			eachImg.source= CommonXMLData.UploadFile_BasePath + eachObj.filepath;
			eachImg.width = aboutheight*5;
			eachImg.height = aboutheight*5;
			eachImg.data = idx;
			eachImg.addEventListener(MouseEvent.CLICK,CommonMethod.doshowZoomSelectedPic(this.tempObjBeanName,idColumnName,this.dataId,this));
			eachImg.addEventListener(MouseEvent.MOUSE_OVER, mouseOverFunction);
			eachImg.addEventListener(MouseEvent.MOUSE_OUT, mouseOutFunction);
			fyts.addChild(eachImg);
		}
	}
	var addMoreBtn:Button = new Button();
	addMoreBtn.label="添加图片";
	addMoreBtn.addEventListener(MouseEvent.CLICK,addMoreBtnClick);
	fyts.addChild(addMoreBtn);
}

private function addMoreBtnClick(evt:Event):void{
	CommonMethod.modifyUploadFile(this,tempObjBeanName,idColumnName,this.dataId);
}
private function mouseOverFunction(evt:Event):void{
	Mouse.cursor = MouseCursor.BUTTON;
}
private function mouseOutFunction(evt:Event):void{
	Mouse.cursor = MouseCursor.AUTO;
}